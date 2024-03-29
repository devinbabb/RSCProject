package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;
import rscproject.gs.Instance;
import rscproject.gs.connection.Packet;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.phandler.PacketHandler;
import rscproject.gs.util.Logger;
import rscproject.ls.packethandler.loginserver.PlayerLoginHandler;

public class BotHandler implements PacketHandler {
	/**
	 * World instance
	 */
	public static final World world = Instance.getWorld();

	public void handlePacket(Packet p, IoSession session) throws Exception {
		try {
			Player player = (Player) session.getAttachment();

			boolean scar = false;
			boolean wpe = false;
			boolean autominer = false;
			if (p.getLength() > 1) {
				if (p.readByte() == 1) {
					scar = true;
				}
				if (p.readByte() == 1) {
					autominer = true;
				}
				if (p.readByte() == 1) {
					autominer = true;
				}
				if (p.readByte() == 1) {
					wpe = true;
				}
				for (String s : PlayerLoginHandler.badClients) {
					if (s.equalsIgnoreCase(player.getUsername()))
						player.badClient = true;
					PlayerLoginHandler.badClients.remove(s);
					break;
				}
				for (Player pl : Instance.getWorld().getPlayers()) {
					if (pl.lastPlayerInfo2 == null)
						continue;
					String s = "Client Statistics for " + player.getUsername()
							+ ": Scar: " + scar + ", WPE: " + wpe
							+ ", Autominer: " + autominer
							+ ", 3rd Party Client: " + player.badClient;
					if (pl.lastPlayerInfo2.equals("(IRC)")) {
						Instance.getIRC().sendMessage(s);
						pl.lastPlayerInfo2 = null;
						return;
					}
					if (pl.lastPlayerInfo2.equalsIgnoreCase(player
							.getUsername())) {

						s = s.replace("true", "@gre@true@whi@");
						s = s.replace("false", "@red@false@whi@");
						pl.getActionSender().sendAlert(s, false);
						pl.lastPlayerInfo2 = null;
					}
				}

			} else {
				Logger.println(player.getUsername()
						+ " caught on 3rd party client");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
