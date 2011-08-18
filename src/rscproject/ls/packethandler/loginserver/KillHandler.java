package rscproject.ls.packethandler.loginserver;

import java.sql.SQLException;

import org.apache.mina.common.IoSession;

import rscproject.ls.Server;
import rscproject.ls.net.Packet;
import rscproject.ls.packethandler.PacketHandler;

/**
 * @author Devin
 **/

public class KillHandler implements PacketHandler {

    public void handlePacket(Packet p, IoSession session) throws Exception {
	try {
	    Server.db.updateQuery("INSERT INTO `rsca2_kills`(`user`, `killed`, `time`, `type`) VALUES('" + p.readLong() + "', '" + p.readLong() + "', " + (int) (System.currentTimeMillis() / 1000) + ", " + p.readByte() + ")");
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

}
