package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;
import rscproject.gs.Instance;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.phandler.PacketHandler;

public class ChatHandler implements PacketHandler {
	/**
	 * World instance
	 */
	public static final World world = Instance.getWorld();

	public void handlePacket(Packet p, IoSession session) throws Exception {
		Player sender = (Player) session.getAttachment();
		if (World.SERVER_MUTED && !sender.isMod()) {
			sender.getActionSender().sendMessage(
					"@red@Unable to chat while the server is muted");
			return;
		}

		sender.addMessageToChatQueue(p.getData());
	}

}