package rscproject.gs.plugins.ai;

import rscproject.gs.model.Npc;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.model.minigames.GlobalWar;
import rscproject.gs.plugins.dependencies.NpcAI;
import rscproject.gs.plugins.dependencies.NpcScript;

/**
 * Blue king intelligence class.
 * 
 * @author Pets
 */
public class WarKing extends NpcScript implements NpcAI {

	public GlobalWar war = World.getWar();

	@Override
	public int getID() {
		return 254;
	}

	@Override
	public void onHealthPercentage(Npc npc, int percent) {
		warnTeam(npc);
	}

	@Override
	public void onMageAttack(Player attacker, Npc npc) {
		warnTeam(npc);
	}

	@Override
	public void onMeleeAttack(Player attacker, final Npc npc) {
		warnTeam(npc);
	}

	@Override
	public void onNpcDeath(Npc npc, Player player) {
		war.endWar(npc.getTeam());
	}

	@Override
	public void onRangedAttack(Player p, Npc npc) {
		warnTeam(npc);
		spawnProtection(npc, p);
	}

	@Override
	public void onNpcAttack(Npc npc, Player player) {

	}

	public void spawnProtection(Npc n, Player p) {
		if (war.isRunning()) {
			long now = System.currentTimeMillis();
			if (now - war.getLastSpawn(n.getTeam()) > 30000) {
				p.getActionSender().sendMessage(
						"The queen summons a protector!");
				war.sendMessage(
						n.getTeam(),
						"@whi@[@red@WAR@whi@]@red@ Your queen is under attack, its calling on monsters for backup!");
				war.sendMessage(n.getTeam(),
						"Quickly go help your queen before the war is over!");
				war.spawnProtection(n.getTeam(), p);
			}
		}
	}

	/**
	 * Sends a message to the team about their king being under attack.
	 */
	public void warnTeam(Npc n) {
		if (war.isRunning()) {
			long now = System.currentTimeMillis();
			if (now - war.getLastMessage(n.getTeam()) > 60000) {
				war.sendMessage(n.getTeam(),
						"@whi@[@red@WAR@whi@]@red@ Your queen is under attack!");
			}
		}

	}
}
