package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;

import rscproject.gs.Instance;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.phandler.PacketHandler;
import rscproject.gs.util.Logger;

public class Trap implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
	Player player = (Player) session.getAttachment();
	Logger.event(player.getUsername() + " [" + player.getUsernameHash() + "] was caught by a trap!");
    }
}
