package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;

import rscproject.gs.Instance;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.MenuHandler;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.phandler.PacketHandler;

public class MenuReplyHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
	Player player = (Player) session.getAttachment();
	MenuHandler menuHandler = player.getMenuHandler();
	if (menuHandler == null) {
	    player.setSuspiciousPlayer(true);
	    return;
	}
	int option = (int) p.readByte();
	String reply = menuHandler.getOption(option);
	player.resetMenuHandler();
	if (reply == null) {
	    player.setSuspiciousPlayer(true);
	    return;
	}
	menuHandler.handleReply(option, reply);
    }
}
