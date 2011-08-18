package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;

import rscproject.gs.Instance;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.phandler.PacketHandler;
import rscproject.gs.util.Logger;

public class ExceptionHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
    	try {
	    	Player player = (Player) session.getAttachment();
			Logger.error("[CLIENT] Exception from " + player.getUsername() + ": " + p.readString());
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	
		//player.getActionSender().sendLogout();
		//player.destroy(false);
    }
}