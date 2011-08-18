package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;

import rscproject.config.Formulae;
import rscproject.gs.Instance;
import rscproject.gs.builders.RSCPacketBuilder;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.phandler.PacketHandler;
import rscproject.gs.util.Logger;

public class SessionRequest implements PacketHandler {

	/**
	 * World instance
	 */
	public static final World world = Instance.getWorld();

	public void handlePacket(Packet p, IoSession session) throws Exception {
		Player player = (Player) session.getAttachment();
		if(player.isInitialized()) {
			Logger.println("[WARNING] SessionRequest for already Initialized player!");
			return;
		}
		byte userByte = p.readByte();
		player.setClassName(p.readString().trim());
		long serverKey = Formulae.generateSessionKey(userByte);
		player.setServerKey(serverKey);
		RSCPacketBuilder pb = new RSCPacketBuilder();
		pb.setBare(true);
		pb.addLong(serverKey);
		session.write(pb.toPacket());
	}
}
