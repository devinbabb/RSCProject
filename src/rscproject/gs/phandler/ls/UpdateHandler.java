package rscproject.gs.phandler.ls;

import org.apache.mina.common.IoSession;
import rscproject.gs.Instance;
import rscproject.gs.connection.LSPacket;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.phandler.PacketHandler;
import rscproject.gs.util.Logger;

/**
 * @author Devin
 */

public class UpdateHandler implements PacketHandler {
	/**
	 * World instance
	 */
	public static final World world = Instance.getWorld();

	public void handlePacket(Packet p, IoSession session) throws Exception {
		long uID = ((LSPacket) p).getUID();
		Logger.event("LOGIN_SERVER sent update (uID: " + uID + ")");
		String reason = p.readString();
		if (Instance.getServer().shutdownForUpdate()) {
			for (Player player : world.getPlayers()) {
				player.getActionSender().sendAlert(
						"The server will be shutting down in 60 seconds: "
								+ reason, false);
				player.getActionSender().startShutdown(60);
			}
		}
	}

}