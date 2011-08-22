package rscproject.gs.phandler.ls;

import org.apache.mina.common.IoSession;
import rscproject.gs.Instance;
import rscproject.gs.connection.LSPacket;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.phandler.PacketHandler;
import rscproject.gs.util.Logger;

/**
 * @author Devin
 */

public class GlobalHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
        long uID = ((LSPacket) p).getUID();
        Logger.event("LOGIN_SERVER sent alert (uID: " + uID + ")");
        String message = p.readString();
        for (Player player : world.getPlayers()) {
            player.getActionSender().sendAlert(message, false);
        }
    }

}