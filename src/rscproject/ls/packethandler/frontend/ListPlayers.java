package rscproject.ls.packethandler.frontend;

import java.util.ArrayList;

import org.apache.mina.common.IoSession;

import rscproject.ls.Server;
import rscproject.ls.model.World;
import rscproject.ls.net.FPacket;
import rscproject.ls.net.Packet;
import rscproject.ls.packetbuilder.FPacketBuilder;
import rscproject.ls.packethandler.PacketHandler;

public class ListPlayers implements PacketHandler {
    private static final FPacketBuilder builder = new FPacketBuilder();

    public void handlePacket(Packet p, final IoSession session) throws Exception {
	String[] params = ((FPacket) p).getParameters();
	try {
	    final int worldID = Integer.parseInt(params[0]);
	    System.out.println("Frontend requested player list for world " + worldID);
	    World world = Server.getServer().getWorld(worldID);
	    if (world == null) {
		throw new Exception("Unknown world");
	    }
	    world.getActionSender().playerListRequest(new PacketHandler() {
		public void handlePacket(Packet p, IoSession s) throws Exception {
		    builder.setID(1);

		    ArrayList<String> params = new ArrayList<String>();
		    int count = p.readInt();
		    for (int c = 0; c < count; c++) {
			params.add(p.readLong() + "," + p.readShort() + "," + p.readShort() + "," + worldID);
		    }
		    builder.setParameters(params.toArray(new String[params.size()]));

		    session.write(builder.toPacket());
		}
	    });
	} catch (Exception e) {
	    builder.setID(0);
	    session.write(builder.toPacket());
	}
    }

}