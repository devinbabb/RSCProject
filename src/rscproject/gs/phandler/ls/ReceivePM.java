package rscproject.gs.phandler.ls;

import org.apache.mina.common.IoSession;
import rscproject.gs.Instance;
import rscproject.gs.connection.LSPacket;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.phandler.PacketHandler;

/**
 * @author Devin
 */

public class ReceivePM implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
        long uID = ((LSPacket) p).getUID();
        long sender = p.readLong();
        Player recipient = world.getPlayer(p.readLong());
        boolean avoidBlock = p.readByte() == 1;
        if (recipient == null || !recipient.loggedIn()) {
            return;
        }
        if (recipient.getPrivacySetting(1) && !recipient.isFriendsWith(sender) && !avoidBlock) {
            return;
        }
        if (recipient.isIgnoring(sender) && !avoidBlock) {
            return;
        }
        recipient.getActionSender().sendPrivateMessage(sender, p.getRemainingData());
    }

}