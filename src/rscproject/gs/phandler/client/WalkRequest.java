package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;
import rscproject.gs.Instance;
import rscproject.gs.connection.Packet;
import rscproject.gs.connection.RSCPacket;
import rscproject.gs.event.FightEvent;
import rscproject.gs.event.MiniEvent;
import rscproject.gs.event.WalkMobToMobEvent;
import rscproject.gs.model.*;
import rscproject.gs.phandler.PacketHandler;
import rscproject.gs.states.Action;
import rscproject.gs.states.CombatState;

public class WalkRequest implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
        Player player = (Player) session.getAttachment();
        int pID = ((RSCPacket) p).getID();
        if (player.inCombat()) {
            if (pID == 132) {
                Mob opponent = player.getOpponent();
                if (opponent == null) { // This shouldn't happen
                    player.setSuspiciousPlayer(true);
                    return;
                }
                if (opponent.getHitsMade() >= 3) {
                    if (player.isDueling() && player.getDuelSetting(0)) {
                        player.getActionSender().sendMessage("Running has been disabled in this duel.");
                        return;
                    }
                    player.lastRun = System.currentTimeMillis();
                    player.resetCombat(CombatState.RUNNING);

                    if (player.isInfected() && System.currentTimeMillis() - player.getLastMoved() < 1900) {
                        final Packet newpacket = p;
                        final IoSession newsession = session;
                        Instance.getDelayedEventHandler().add(new MiniEvent(player, 2000) {
                            public void action() {
                                try {
                                    handlePacket(newpacket, newsession);
                                } catch (Exception e) {
                                    return;
                                }
                            }
                        });
                    }
                    player.isMining(false);
                    if (opponent instanceof Npc) {
                        Npc n = (Npc) opponent;
                        n.unblock();
                        opponent.resetCombat(CombatState.WAITING);
                        if (n.getDef().aggressive) {
                            player.lastNpcChasingYou = n;
                            Instance.getDelayedEventHandler().add(new MiniEvent(player, 2000) {
                                public void action() {

                                    final Npc npc = owner.lastNpcChasingYou;
                                    owner.lastNpcChasingYou = null;
                                    if (npc.isBusy() || npc.getChasing() != null)
                                        return;

                                    npc.resetPath();
                                    npc.setChasing(owner);

                                    Instance.getDelayedEventHandler().add(new WalkMobToMobEvent(npc, owner, 0) {
                                        public void arrived() {
                                            if (affectedMob.isBusy() || owner.isBusy()) {
                                                npc.setChasing(null);
                                                return;
                                            }
                                            if (affectedMob.inCombat() || owner.inCombat()) {
                                                npc.setChasing(null);
                                                return;
                                            }
                                            Player player = (Player) affectedMob;
                                            player.resetPath();
                                            player.setBusy(true);
                                            npc.resetPath();
                                            player.resetAll();
                                            player.setStatus(Action.FIGHTING_MOB);
                                            player.getActionSender().sendSound("underattack");
                                            player.getActionSender().sendMessage("You are under attack!");

                                            npc.setLocation(player.getLocation(), true);
                                            for (Player p : npc.getViewArea().getPlayersInView())
                                                p.removeWatchedNpc(npc);
                                            player.setBusy(true);
                                            player.setSprite(9);
                                            player.setOpponent(npc);
                                            player.setCombatTimer();

                                            npc.setBusy(true);
                                            npc.setSprite(8);
                                            npc.setOpponent(player);
                                            npc.setCombatTimer();
                                            npc.setChasing(null);
                                            FightEvent fighting = new FightEvent(player, npc, true);
                                            fighting.setLastRun(0);
                                            world.getDelayedEventHandler().add(fighting);
                                        }

                                        public void failed() {
                                            npc.setChasing(null);
                                        }
                                    });
                                }
                            });
                        }

                    } else {
                        opponent.resetCombat(CombatState.WAITING);
                    }
                } else {
                    player.getActionSender().sendMessage("You cannot retreat in the first 3 rounds of battle.");
                    return;
                }
            } else {
                return;
            }
        } else if (player.isBusy() && System.currentTimeMillis() - player.lastMineTimer > 2000) {
            return;
        }

        if (System.currentTimeMillis() - player.lastCast < 600)
            return;

        player.isMining(false);
        player.resetAll();

        int startX = p.readShort();
        int startY = p.readShort();
        int numWaypoints = p.remaining() / 2;
        byte[] waypointXoffsets = new byte[numWaypoints];
        byte[] waypointYoffsets = new byte[numWaypoints];
        for (int x = 0; x < numWaypoints; x++) {
            waypointXoffsets[x] = p.readByte();
            waypointYoffsets[x] = p.readByte();
        }
        Path path = new Path(startX, startY, waypointXoffsets, waypointYoffsets);
        if (player.blink() && waypointXoffsets.length >= 1) {
            player.teleport((int) waypointXoffsets[waypointXoffsets.length - 1] + startX, (int) waypointYoffsets[waypointYoffsets.length - 1] + startY, false);
            return;
        }
        player.setStatus(Action.IDLE);
        player.setPath(path);
    }

}
