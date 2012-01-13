package rscproject.ls.packethandler.frontend;

import org.apache.mina.common.IoSession;
import rscproject.ls.Server;
import rscproject.ls.model.World;
import rscproject.ls.net.FPacket;
import rscproject.ls.net.Packet;
import rscproject.ls.packetbuilder.FPacketBuilder;
import rscproject.ls.packethandler.PacketHandler;

public class Global implements PacketHandler {
	private static final FPacketBuilder builder = new FPacketBuilder();

	public void handlePacket(Packet p, IoSession session) throws Exception {
		String[] params = ((FPacket) p).getParameters();
		try {
			String message = params[0];
			for (World w : Server.getServer().getWorlds()) {
				w.getActionSender().alert(message);
			}
			builder.setID(1);
		} catch (Exception e) {
			builder.setID(0);
		}
		FPacket packet = builder.toPacket();
		if (packet != null) {
			session.write(packet);
		}
	}

}