package rscproject.gs.builders.ls;

import rscproject.gs.Instance;
import rscproject.gs.builders.LSPacketBuilder;
import rscproject.gs.connection.LSPacket;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.util.EntityList;

/**
 * @author Devin
 */

public class PlayerListRequestPacketBuilder {
	/**
	 * World instance
	 */
	public static final World world = Instance.getWorld();
	/**
	 * Packets uID
	 */
	private long uID;

	public LSPacket getPacket() {
		EntityList<Player> players = world.getPlayers();

		LSPacketBuilder packet = new LSPacketBuilder();
		packet.setUID(uID);
		packet.addInt(players.size());
		for (Player p : players) {
			packet.addLong(p.getUsernameHash());
			packet.addShort(p.getX());
			packet.addShort(p.getY());
		}
		return packet.toPacket();
	}

	/**
	 * Sets the packet to reply to
	 */
	public void setUID(long uID) {
		this.uID = uID;
	}
}
