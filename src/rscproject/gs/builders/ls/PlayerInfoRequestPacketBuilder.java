package rscproject.gs.builders.ls;

import rscproject.gs.Instance;
import rscproject.gs.builders.LSPacketBuilder;
import rscproject.gs.connection.LSPacket;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;

/**
 * @author Devin
 */

public class PlayerInfoRequestPacketBuilder {
	/**
	 * World instance
	 */
	public static final World world = Instance.getWorld();
	/**
	 * The player to provide information on
	 */
	private Player player;
	/**
	 * Packets uID
	 */
	private long uID;

	public LSPacket getPacket() {
		LSPacketBuilder packet = new LSPacketBuilder();
		packet.setUID(uID);
		if (player == null) {
			packet.addByte((byte) 0);
		} else {
			packet.addByte((byte) 1);
			packet.addShort(player.getX());
			packet.addShort(player.getY());
			packet.addLong(player.getCurrentLogin());
			packet.addLong(player.getLastMoved());
			packet.addByte((byte) (player.getPrivacySetting(0) ? 1 : 0));
			packet.addShort(player.getFatigue());
			packet.addBytes(player.getStatus().toString().getBytes());
		}
		return packet.toPacket();
	}

	/**
	 * Sets the player to provide information on
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Sets the packet to reply to
	 */
	public void setUID(long uID) {
		this.uID = uID;
	}
}
