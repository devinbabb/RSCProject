package rscproject.ls.packetbuilder.loginserver;

import rscproject.ls.Server;
import rscproject.ls.model.BankItem;
import rscproject.ls.model.InvItem;
import rscproject.ls.model.PlayerSave;
import rscproject.ls.model.World;
import rscproject.ls.net.LSPacket;
import rscproject.ls.packetbuilder.LSPacketBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Login Builder
 * 
 * @author Devin
 */

public class PlayerLoginPacketBuilder {
	/**
	 * Players Login Code
	 */
	private byte loginCode;
	/**
	 * Players Saved Data
	 */
	private PlayerSave save;
	/**
	 * Packets uID
	 */
	private long uID;

	public LSPacket getPacket() {
		Server server = Server.getServer();

		LSPacketBuilder packet = new LSPacketBuilder();
		packet.setUID(uID);
		packet.addByte(loginCode);
		if (save != null) {
			packet.addInt(save.getOwner());
			packet.addInt(save.getGroup());

			packet.addLong(save.getSubscriptionExpires());

			packet.addLong(save.getLastIP());
			packet.addLong(save.getLastLogin());

			packet.addShort(save.getX());
			packet.addShort(save.getY());

			packet.addShort(save.getFatigue());
			packet.addByte(save.getCombatStyle());

			packet.addByte((byte) (save.blockChat() ? 1 : 0));
			packet.addByte((byte) (save.blockPrivate() ? 1 : 0));
			packet.addByte((byte) (save.blockTrade() ? 1 : 0));
			packet.addByte((byte) (save.blockDuel() ? 1 : 0));

			packet.addByte((byte) (save.cameraAuto() ? 1 : 0));
			packet.addByte((byte) (save.oneMouse() ? 1 : 0));
			packet.addByte((byte) (save.soundOff() ? 1 : 0));
			packet.addByte((byte) (save.showRoof() ? 1 : 0));
			packet.addByte((byte) (save.autoScreenshot() ? 1 : 0));
			packet.addByte((byte) (save.combatWindow() ? 1 : 0));

			packet.addShort(save.getHairColour());
			packet.addShort(save.getTopColour());
			packet.addShort(save.getTrouserColour());
			packet.addShort(save.getSkinColour());
			packet.addShort(save.getHeadSprite());
			packet.addShort(save.getBodySprite());

			packet.addByte((byte) (save.isMale() ? 1 : 0));
			packet.addLong(save.getSkullTime());

			for (int i = 0; i < 18; i++) {
				packet.addLong(save.getExp(i));
				packet.addShort(save.getStat(i));
			}

			int invCount = save.getInvCount();
			packet.addShort(invCount);
			for (int i = 0; i < invCount; i++) {
				InvItem item = save.getInvItem(i);
				packet.addShort(item.getID());
				packet.addInt(item.getAmount());
				packet.addByte((byte) (item.isWielded() ? 1 : 0));
			}

			int bankCount = save.getBankCount();
			packet.addShort(bankCount);
			for (int i = 0; i < bankCount; i++) {
				BankItem item = save.getBankItem(i);
				packet.addShort(item.getID());
				packet.addInt(item.getAmount());
			}

			ArrayList<Long> friendsWithUs = new ArrayList<Long>();
			try {
				ResultSet result = Server.db
						.getQuery("SELECT p.user FROM `rsca2_friends` AS f INNER JOIN `rsca2_players` AS p ON p.user=f.friend WHERE p.block_private=0 AND f.user='"
								+ save.getUser() + "'");
				while (result.next()) {
					friendsWithUs.add(result.getLong("user"));
				}
				result = Server.db
						.getQuery("SELECT user FROM `rsca2_friends` WHERE friend='"
								+ save.getUser() + "'");
				while (result.next()) {
					friendsWithUs.add(result.getLong("user"));
				}
			} catch (SQLException e) {
				Server.error(e);
			}

			int friendCount = save.getFriendCount();
			packet.addShort(friendCount);
			for (int i = 0; i < friendCount; i++) {
				long friend = save.getFriend(i);
				World world = server.findWorld(friend);
				packet.addLong(friend);
				packet.addShort(world == null
						|| !friendsWithUs.contains(friend) ? 0 : world.getID());
			}

			int ignoreCount = save.getIgnoreCount();
			packet.addShort(ignoreCount);
			for (int i = 0; i < ignoreCount; i++) {
				packet.addLong(save.getIgnore(i));
			}
			packet.addShort(save.getQuestPoints());
			java.util.Set<Integer> keys = save.getQuestStages().keySet();
			packet.addShort(keys.size());

			for (int id : keys) {
				packet.addShort(id);
				packet.addShort(save.getQuestStage(id));
			}
			packet.addLong(save.getMuted());
			packet.addLong(save.getEventCD());
		}
		return packet.toPacket();

	}

	/**
	 * Sets the packet to reply to
	 */
	public void setPlayer(PlayerSave save, byte loginCode) {
		this.save = save;
		this.loginCode = loginCode;
	}

	/**
	 * Sets the packet to reply to
	 */
	public void setUID(long uID) {
		this.uID = uID;
	}
}
