package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;
import rscproject.gs.Instance;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.phandler.PacketHandler;

public class PlayerLogoutRequest implements PacketHandler {
	/**
	 * World instance
	 */
	public static final World world = Instance.getWorld();

	public void handlePacket(Packet p, IoSession session) throws Exception {
		Player player = (Player) session.getAttachment();
		if (player.canLogout()) {
			/**
			 * Call minigame logout check
			 */
			World.getWar().handlePlayerLogout(player);
			World.getCtf().handlePlayerLogout(player);
			World.getLoto().handlePlayerLogout(player);
			/**
			 * Destroy player
			 */
			player.destroy(true, true);
		} else {
			player.getActionSender().sendCantLogout();
		}
	}
}
