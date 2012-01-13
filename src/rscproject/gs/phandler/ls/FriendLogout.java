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

public class FriendLogout implements PacketHandler {
	/**
	 * World instance
	 */
	public static final World world = Instance.getWorld();

	public void handlePacket(Packet p, IoSession session) throws Exception {
		long uID = ((LSPacket) p).getUID();
		long friend = p.readLong();

		switch (((LSPacket) p).getID()) {
		case 12:
			for (Player player : world.getPlayers()) {
				if (player.isFriendsWith(friend)) {
					player.getActionSender().sendFriendUpdate(friend, 0);
				}
			}
			break;
		case 13:
			Player player = world.getPlayer(p.readLong());
			if (player != null) {
				player.getActionSender().sendFriendUpdate(friend, 0);
			}
			break;
		}
	}

}