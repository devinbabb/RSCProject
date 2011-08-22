package rscproject.gs.phandler.ls;

import org.apache.mina.common.IoSession;
import rscproject.gs.Instance;
import rscproject.gs.builders.ls.PlayerListRequestPacketBuilder;
import rscproject.gs.connection.LSPacket;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.World;
import rscproject.gs.phandler.PacketHandler;
import rscproject.gs.util.Logger;

/**
 * @author Devin
 */

public class PlayerListRequestHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();
    private PlayerListRequestPacketBuilder builder = new PlayerListRequestPacketBuilder();

    public void handlePacket(Packet p, IoSession session) throws Exception {
        long uID = ((LSPacket) p).getUID();
        Logger.event("LOGIN_SERVER requested player list (uID: " + uID + ")");
        builder.setUID(uID);
        LSPacket temp = builder.getPacket();
        if (temp != null) {
            session.write(temp);
        }
    }

}