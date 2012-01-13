package rscproject.gs;

import rscproject.gs.core.DelayedEventHandler;
import rscproject.gs.db.DBConnection;
import rscproject.gs.db.ReportHandlerQueries;
import rscproject.gs.model.World;
import rscproject.gs.model.minigames.CTF;
import rscproject.gs.plugins.dependencies.PluginHandler;
import rscproject.irc.IRC;

/**
 * Holds instances to commonly used Objects.
 * 
 * @author Devin, xEnt
 */
public class Instance {

	public static IRC getIRC() {
		return getServer().getIRC();
	}

	public static Server getServer() {
		return World.getWorld().getServer();
	}

	public static World getWorld() {
		return World.getWorld();
	}

	public static DelayedEventHandler getDelayedEventHandler() {
		return getWorld().getDelayedEventHandler();
	}

	public static CacheHandler getCacheHandler() {
		return CacheHandler.getCache();
	}

	public static PluginHandler getPluginHandler() {
		return PluginHandler.getPluginHandler();
	}

	public static ReportHandlerQueries getReport() {
		return DBConnection.getReport();
	}

	public static CTF getCTF() {
		return World.getCtf();
	}

}
