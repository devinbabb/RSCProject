package rscproject.gs.npchandler;

import rscproject.gs.model.Npc;
import rscproject.gs.model.Player;

/**
 * @author Devin
 */

public interface NpcHandler {
	public void handleNpc(final Npc npc, Player player) throws Exception;
}
