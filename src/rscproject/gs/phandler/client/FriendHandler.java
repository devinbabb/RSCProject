package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;
import rscproject.gs.Instance;
import rscproject.gs.builders.ls.MiscPacketBuilder;
import rscproject.gs.connection.Packet;
import rscproject.gs.connection.RSCPacket;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.model.snapshot.Activity;
import rscproject.gs.model.snapshot.Chatlog;
import rscproject.gs.phandler.PacketHandler;
import rscproject.gs.tools.DataConversions;
import rscproject.gs.util.Logger;

import java.util.ArrayList;

public class FriendHandler implements PacketHandler {
	/**
	 * World instance
	 */
	public static final World world = Instance.getWorld();
	private MiscPacketBuilder loginSender = Instance.getServer()
			.getLoginConnector().getActionSender();

	public void handlePacket(Packet p, IoSession session) throws Exception {
		Player player = (Player) session.getAttachment();
		int pID = ((RSCPacket) p).getID();

		long user = player.getUsernameHash();
		long friend = p.readLong();
		switch (pID) {
		case 168: // Add friend
			if (player.friendCount() >= 200) {
				player.getActionSender().sendMessage(
						"Your friend list is too full");
				return;
			}
			loginSender.addFriend(user, friend);
			player.addFriend(friend, 0);
			world.addEntryToSnapshots(new Activity(player.getUsername(), player
					.getUsername()
					+ " added friend "
					+ DataConversions.hashToUsername(friend)
					+ " at: "
					+ player.getX() + "/" + player.getY()));

			break;
		case 52: // Remove friend
			loginSender.removeFriend(user, friend);
			player.removeFriend(friend);
			world.addEntryToSnapshots(new Activity(player.getUsername(), player
					.getUsername()
					+ " removed friend "
					+ DataConversions.hashToUsername(friend)
					+ " at: "
					+ player.getX() + "/" + player.getY()));

			break;
		case 25: // Add ignore
			if (player.ignoreCount() >= 200) {
				player.getActionSender().sendMessage(
						"Your ignore list is too full");
				return;
			}
			loginSender.addIgnore(user, friend);
			player.addIgnore(friend);
			break;
		case 108: // Remove ignore
			loginSender.removeIgnore(user, friend);
			player.removeIgnore(friend);
			break;
		case 254: // Send PM
			try {
				byte[] data = p.getRemainingData();
				String s = DataConversions.byteToString(data, 0, data.length);
				s = s.toLowerCase();
				String k = s;
				s = s.replace(" ", "");
				s = s.replace(".", "");
				if (s.contains("runeblast")) {
					Logger.println(player.getUsername() + " pmed "
							+ DataConversions.hashToUsername(friend) + ":" + k);
					Instance.getIRC().sendMessage(
							player.getUsername() + " pmed "
									+ DataConversions.hashToUsername(friend)
									+ ":" + k);
					return;
				}
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(DataConversions.hashToUsername(friend));
				world.addEntryToSnapshots(new Chatlog(player.getUsername(),
						"(PM) " + k, temp));

				loginSender.sendPM(user, friend, player.isPMod(), data);
			} catch (NegativeArraySizeException e) {
				player.destroy(false);
			}
			break;
		}
	}

}
