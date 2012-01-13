package rscproject.gs.util;

import rscproject.gs.Instance;
import rscproject.gs.builders.ls.MiscPacketBuilder;
import rscproject.gs.model.World;

/**
 * Dueling log handler.
 * 
 * @author Devin
 */

public class DuelLog {
	public static final World world = Instance.getWorld();

	public static void sendlog(final long from, final long to, final int item,
			final long amount, final int x, final int y, final int type) {
		MiscPacketBuilder loginServer = Instance.getServer()
				.getLoginConnector().getActionSender();
		loginServer.tradeLog(to, from, item, amount, x, y, type);
	}
}
