package rscproject.gs.phandler.ls;

import org.apache.mina.common.IoSession;
import rscproject.gs.Instance;
import rscproject.gs.builders.ls.ReportInfoRequestPacketBuilder;
import rscproject.gs.connection.LSPacket;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.phandler.PacketHandler;
import rscproject.gs.util.Logger;

/**
 * @author Devin
 */

public class ReportInfoRequestHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();
    private ReportInfoRequestPacketBuilder builder = new ReportInfoRequestPacketBuilder();

    public void handlePacket(Packet p, IoSession session) throws Exception {
        long uID = ((LSPacket) p).getUID();
        Logger.event("LOGIN_SERVER requested report information (uID: " + uID + ")");
        Player player = world.getPlayer(p.readLong());
        if (player == null) {
            return;
        }
        builder.setUID(uID);
        builder.setPlayer(player);
        LSPacket temp = builder.getPacket();
        if (temp != null) {
            session.write(temp);
        }
    }

}