package rscproject.ls.packethandler.loginserver;

import org.apache.mina.common.IoSession;
import rscproject.ls.Server;
import rscproject.ls.net.Packet;
import rscproject.ls.packethandler.PacketHandler;
import rscproject.ls.util.Config;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/**
 * @author Devin
 */

public class LogHandler implements PacketHandler {
    private static PrintWriter error;

    private static PrintWriter event;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yy");
    private static PrintWriter mod;
    private static PrintWriter exception;

    static {
        try {
            event = new PrintWriter(new File(Config.LOG_DIR, "event.log"));
            error = new PrintWriter(new File(Config.LOG_DIR, "error.log"));
            mod = new PrintWriter(new File(Config.LOG_DIR, "mod.log"));
            exception = new PrintWriter(new File(Config.LOG_DIR, "err.log"));
        } catch (Exception e) {
            Server.error(e);
        }
    }

    private static String getDate() {
        return formatter.format(System.currentTimeMillis());
    }

    public void handlePacket(Packet p, IoSession session) throws Exception {
        byte type = p.readByte();
        String message = getDate() + ": " + p.readString();
        switch (type) {
            case 1:
                event.println(message);
                event.flush();
                break;
            case 2:
                error.println(message);
                error.flush();
                break;
            case 3:
                mod.println(message);
                mod.flush();
                break;
            case 4:
                exception.println(message);
                exception.flush();
                break;
        }
    }

}