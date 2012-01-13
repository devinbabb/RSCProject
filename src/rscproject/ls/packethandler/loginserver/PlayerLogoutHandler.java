package rscproject.ls.packethandler.loginserver;

import org.apache.mina.common.IoSession;
import rscproject.ls.model.World;
import rscproject.ls.net.Packet;
import rscproject.ls.packethandler.PacketHandler;

/**
 * @author Devin
 */

public class PlayerLogoutHandler implements PacketHandler {

	public void handlePacket(Packet p, IoSession session) throws Exception {
		long user = p.readLong();
		World world = (World) session.getAttachment();
		world.unregisterPlayer(user);
	}
}
