package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;

import rscproject.gs.Instance;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.Mob;
import rscproject.gs.model.Npc;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.model.snapshot.Activity;
import rscproject.gs.phandler.PacketHandler;
import rscproject.gs.plugins.extras.Thieving;

public class NpcCommand implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
	int serverIndex = p.readShort();
	final Player player = (Player) session.getAttachment();
	if (player.isBusy()) {
	    return;
	}
	final Mob affectedMob = world.getNpc(serverIndex);
	final Npc affectedNpc = (Npc) affectedMob;
	if (affectedNpc == null || affectedMob == null || player == null || !world.getQuestManager().isNpcVisible((Npc) affectedMob, player))
	    return;
	final int npcID = affectedNpc.getID();
	if (!World.isMembers()) {
	    player.getActionSender().sendMessage("This feature is only avaliable on a members server");
	    return;
	}

	Thieving thiev = new Thieving(player, affectedNpc, affectedMob);
	world.addEntryToSnapshots(new Activity(player.getUsername(), player.getUsername() + " thieved a (" + affectedNpc.getDef().name + ")"));
	thiev.beginPickpocket();

    }

}
