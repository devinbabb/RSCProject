package rscproject.ls.packethandler.loginserver;

import org.apache.mina.common.IoSession;
import rscproject.ls.Server;
import rscproject.ls.model.World;
import rscproject.ls.net.LSPacket;
import rscproject.ls.net.Packet;
import rscproject.ls.packetbuilder.LSPacketBuilder;
import rscproject.ls.packethandler.PacketHandler;

/**
 * Login Server Info Request Manager.
 *
 * @author Devin
 */

public class PlayerInfoRequestHandler implements PacketHandler {

    public void handlePacket(Packet p, final IoSession session) throws Exception {
        final long uID = ((LSPacket) p).getUID();
        final long user = p.readLong();
        final World w = Server.getServer().findWorld(user);
        if (w == null) {
            LSPacketBuilder builder = new LSPacketBuilder();
            builder.setUID(uID);
            builder.addByte((byte) 0);
            session.write(builder.toPacket());
            return;
        }
        w.getActionSender().requestPlayerInfo(user, new PacketHandler() {
            public void handlePacket(Packet p, IoSession s) throws Exception {
                LSPacketBuilder builder = new LSPacketBuilder();
                builder.setUID(uID);
                if (p.readByte() == 0) {
                    builder.addByte((byte) 0);
                } else {
                    builder.addByte((byte) 1);
                    builder.addShort(w == null ? 0 : w.getID());
                    builder.addBytes(p.getRemainingData());
                }
                session.write(builder.toPacket());
            }
        });

    }

}