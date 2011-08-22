package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;
import rscproject.config.Formulae;
import rscproject.gs.Instance;
import rscproject.gs.connection.Packet;
import rscproject.gs.event.WalkToMobEvent;
import rscproject.gs.model.Npc;
import rscproject.gs.model.Player;
import rscproject.gs.model.Script;
import rscproject.gs.model.World;
import rscproject.gs.model.snapshot.Activity;
import rscproject.gs.npchandler.NpcHandler;
import rscproject.gs.phandler.PacketHandler;
import rscproject.gs.states.Action;
import rscproject.gs.util.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ConcurrentModificationException;

public class TalkToNpcHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
        Player player = (Player) session.getAttachment();
        if (player.isBusy()) {
            player.resetPath();
            return;
        }
        if (System.currentTimeMillis() - player.lastNPCChat < 1500)
            return;
        player.setLastQuestMenuReply(-2);
        player.lastNPCChat = System.currentTimeMillis();
        player.resetAll();
        final Npc affectedNpc = world.getNpc(p.readShort());
        if (affectedNpc == null || !world.getQuestManager().isNpcVisible(affectedNpc, player)) {
            return;
        }
        world.addEntryToSnapshots(new Activity(player.getUsername(), player.getUsername() + " talked to NPC (" + affectedNpc.getID() + ") at: " + player.getX() + "/" + player.getY() + "|" + affectedNpc.getX() + "/" + affectedNpc.getY()));


        player.setFollowing(affectedNpc);
        player.setStatus(Action.TALKING_MOB);
        Instance.getDelayedEventHandler().add(new WalkToMobEvent(player, affectedNpc, 1) {
            public void arrived() {
                owner.resetFollowing();
                owner.resetPath();
                if (owner.isBusy() || owner.isRanging() || !owner.nextTo(affectedNpc) || owner.getStatus() != Action.TALKING_MOB) {
                    return;
                }
                owner.resetAll();
                if (affectedNpc.isBusy()) {
                    owner.getActionSender().sendMessage(affectedNpc.getDef().getName() + " is currently busy.");
                    return;
                }
                affectedNpc.resetPath();
                NpcHandler handler = world.getNpcHandler(affectedNpc.getID());

                if (Formulae.getDirection(owner, affectedNpc) != -1) {
                    affectedNpc.setSprite(Formulae.getDirection(owner, affectedNpc));
                    owner.setSprite(Formulae.getDirection(affectedNpc, owner));
                }
                if (handler != null) {
                    try {
                        handler.handleNpc(affectedNpc, owner);
                    } catch (Exception e) {
                        Logger.error("Exception with npc[" + affectedNpc.getIndex() + "] from " + owner.getUsername() + " [" + owner.getCurrentIP() + "]: " + e.getMessage());
                        owner.getActionSender().sendLogout();
                        owner.destroy(false);
                    }
                } else {

                    if (!world.getQuestManager().handleNpcTalk(affectedNpc, owner)) {
                        if (affectedNpc.getDef().isAttackable())
                            owner.getActionSender().sendMessage("The " + affectedNpc.getDef().getName() + " doesn't appear interested in talking.");
                        else if (world.npcScripts.containsKey(affectedNpc.getID())) {
                            owner.setBusy(true);
                            affectedNpc.blockedBy(owner);
                            if (owner.interpreterThread != null) {
                                try {
                                    owner.interpreterThread.interrupt();
                                } catch (Exception e) {

                                }
                            }

                            owner.interpreterThread = new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        try {
                                            new Script(owner, affectedNpc);
                                        } catch (ConcurrentModificationException cme) {
                                            Logger.println("CME (Ignore This): " + owner.getUsername());
                                        } catch (Exception e) {

                                        }
                                        owner.setBusy(false);

                                        affectedNpc.unblock();
                                    } catch (Exception e) {
                                        if (!(e instanceof InvocationTargetException)) {
                                            e.printStackTrace();
                                        }
                                        System.out.println(affectedNpc.getID());
                                        affectedNpc.unblock();
                                        owner.setBusy(false);
                                    }
                                }
                            });
                            owner.interpreterThread.start();
                        } else {
                            try {
                                NpcHandler hand = new rscproject.gs.npchandler.OtherNPC();
                                hand.handleNpc(affectedNpc, owner);
                            } catch (Exception e) {
                                Logger.error("Exception with npc[" + affectedNpc.getIndex() + "] from " + owner.getUsername() + " [" + owner.getCurrentIP() + "]: " + e.getMessage());
                                owner.getActionSender().sendLogout();
                                owner.destroy(false);
                            }
                        }
                    }

                }
            }
        });
    }
}
