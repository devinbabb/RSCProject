package rscproject.gs.phandler.ls;

import org.apache.mina.common.IoSession;
import rscproject.gs.Instance;
import rscproject.gs.connection.LSPacket;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.phandler.PacketHandler;

/**
 * @author Devin
 */

public class FriendLogin implements PacketHandler {
	/**
	 * World instance
	 */
	public static final World world = Instance.getWorld();

	public void handlePacket(Packet p, IoSession session) throws Exception {
		long uID = ((LSPacket) p).getUID();
		Player player = world.getPlayer(p.readLong());
		if (player == null) {
			return;
		}
		long friend = p.readLong();
		if (player.isFriendsWith(friend)) {
			player.getActionSender().sendFriendUpdate(friend, p.readShort());
		}
	}

}