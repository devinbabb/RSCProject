package rscproject.gs.builders.ls;

import rscproject.gs.builders.LSPacketBuilder;
import rscproject.gs.connection.LSPacket;
import rscproject.gs.model.*;
import rscproject.gs.tools.DataConversions;

/**
 * @author Devin
 */

public class SavePacketBuilder {
	/**
	 * Player to save
	 */
	private Player player;

	public LSPacket getPacket() {

		LSPacketBuilder packet = new LSPacketBuilder();
		packet.setID(20);
		packet.addLong(player.getUsernameHash());
		packet.addInt(player.getOwner());

		packet.addLong(player.getLastLogin() == 0L
				&& player.isChangingAppearance() ? 0 : player.getCurrentLogin());
		packet.addLong(DataConversions.IPToLong(player.getCurrentIP()));
		packet.addShort(player.getCombatLevel());
		packet.addShort(player.getSkillTotal());
		packet.addShort(player.getX());
		packet.addShort(player.getY());
		packet.addShort(player.getFatigue());

		PlayerAppearance a = player.getPlayerAppearance();
		packet.addByte((byte) a.getHairColour());
		packet.addByte((byte) a.getTopColour());
		packet.addByte((byte) a.getTrouserColour());
		packet.addByte((byte) a.getSkinColour());
		packet.addByte((byte) a.getSprite(0));
		packet.addByte((byte) a.getSprite(1));

		packet.addByte((byte) (player.isMale() ? 1 : 0));
		packet.addLong(player.getSkullTime());
		packet.addByte((byte) player.getCombatStyle());

		for (int i = 0; i < 18; i++) {
			packet.addLong(player.getExp(i));
			packet.addShort(player.getCurStat(i));
		}

		Inventory inv = player.getInventory();
		packet.addShort(inv.size());
		for (InvItem i : inv.getItems()) {
			packet.addShort(i.getID());
			packet.addInt(i.getAmount());
			packet.addByte((byte) (i.isWielded() ? 1 : 0));
		}

		Bank bnk = player.getBank();
		packet.addShort(bnk.size());
		for (InvItem i : bnk.getItems()) {
			packet.addShort(i.getID());
			packet.addInt(i.getAmount());
		}

		packet.addShort(player.getQuestPoints());
		java.util.HashMap<Integer, Integer> questStage = (java.util.HashMap<Integer, Integer>) player
				.getQuestStages().clone();

		packet.addShort(questStage.size());
		java.util.Set<Integer> set = questStage.keySet();

		for (int i : set) {
			packet.addShort(i);
			packet.addShort(questStage.get(i));
		}
		packet.addLong(player.getEventCD());
		return packet.toPacket();
	}

	/**
	 * Sets the player to save
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
}
