package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;
import rscproject.gs.Instance;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.phandler.PacketHandler;

public class StyleHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
        Player player = (Player) session.getAttachment();
        int style = p.readByte();
        if (style < 0 || style > 3) {
            player.setSuspiciousPlayer(true);
            return;
        }
        player.setCombatStyle(style);
    }

}