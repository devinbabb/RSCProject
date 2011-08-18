package rscproject.ls.packethandler.frontend;

import org.apache.mina.common.IoSession;

import rscproject.ls.Server;
import rscproject.ls.model.World;
import rscproject.ls.net.FPacket;
import rscproject.ls.net.Packet;
import rscproject.ls.packetbuilder.FPacketBuilder;
import rscproject.ls.packethandler.PacketHandler;

public class Logout implements PacketHandler {
    private static final FPacketBuilder builder = new FPacketBuilder();

    public void handlePacket(Packet p, IoSession session) throws Exception {
	String[] params = ((FPacket) p).getParameters();
	try {
	    long usernameHash = Long.parseLong(params[0]);
	    World world = Server.getServer().findWorld(usernameHash);
	    if (world == null) {
		throw new Exception("World not found");
	    }
	    world.getActionSender().logoutUser(usernameHash);
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