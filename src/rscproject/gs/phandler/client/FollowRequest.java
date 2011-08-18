package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;

import rscproject.gs.Instance;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.model.snapshot.Activity;
import rscproject.gs.phandler.PacketHandler;

public class FollowRequest implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
	Player player = (Player) session.getAttachment();
	Player affectedPlayer = world.getPlayer(p.readShort());
	if (affectedPlayer == null) {
	    player.setSuspiciousPlayer(true);
	    return;
	}
	if (player.isBusy()) {
	    player.resetPath();
	    return;
	}
	if (System.currentTimeMillis() - player.lastRun < 3000)
	    return;
	player.resetAll();
	player.setFollowing(affectedPlayer, 1);
	player.getActionSender().sendMessage("Now following " + affectedPlayer.getUsername());
	world.addEntryToSnapshots(new Activity(player.getUsername(), player.getUsername() + " started to follow " + affectedPlayer.getUsername() + " at: " + player.getX() + "/" + player.getY() + " | " + affectedPlayer.getX() + "/" + affectedPlayer.getY()));

    }
}
