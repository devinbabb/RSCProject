package rscproject.gs.phandler;

import org.apache.mina.common.IoSession;

import rscproject.gs.connection.Packet;

public interface PacketHandler {
    public void handlePacket(Packet p, IoSession session) throws Exception;
}
