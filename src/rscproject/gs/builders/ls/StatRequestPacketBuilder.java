package rscproject.gs.builders.ls;

import rscproject.config.Config;
import rscproject.gs.Instance;
import rscproject.gs.builders.LSPacketBuilder;
import rscproject.gs.connection.LSPacket;
import rscproject.gs.model.World;

/**
 * @author Devin
 */

public class StatRequestPacketBuilder {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();
    /**
     * Packets uID
     */
    private long uID;

    public LSPacket getPacket() {
        LSPacketBuilder packet = new LSPacketBuilder();
        packet.setUID(uID);
        packet.addInt(world.countPlayers());
        packet.addInt(world.countNpcs());
        packet.addLong(Config.START_TIME);
        return packet.toPacket();
    }

    /**
     * Sets the packet to reply to
     */
    public void setUID(long uID) {
        this.uID = uID;
    }
}
