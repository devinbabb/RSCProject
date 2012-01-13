package rscproject.gs.plugins.ai;

import rscproject.config.Constants;
import rscproject.gs.Instance;
import rscproject.gs.event.ObjectRemover;
import rscproject.gs.model.*;
import rscproject.gs.plugins.dependencies.NpcAI;
import rscproject.gs.plugins.dependencies.NpcScript;
import rscproject.gs.tools.DataConversions;

import java.util.ArrayList;

/**
 * KingBlackDragon intelligence class.
 * 
 * @author xEnt
 */
public class RedDragon extends NpcScript implements NpcAI {

	@Override
	public int getID() {
		return 201;
	}

	@Override
	public void onHealthPercentage(Npc npc, int percent) {
	}

	@Override
	public void onMageAttack(Player attacker, Npc npc) {

	}

	@Override
	public void onMeleeAttack(Player attacker, final Npc npc) {

	}

	@Override
	public void onNpcAttack(Npc npc, Player attacker) {

	}

	@Override
	public void onNpcDeath(Npc npc, Player player) {
		if (npc.getLocation().atAltar()) {
			if (Constants.GameServer.F2P_WILDY) {
				switch (DataConversions.random(0, 3)) {
				case 0: {
					if (DataConversions.random(0, 2000) < 4) { // Drop d med
						World.getWorld().registerItem(
								new Item(795, player.getX(), player.getY(), 1,
										player));
					}
				}
				case 1: {
					if (DataConversions.random(0, 1000) < 4) { // Drop d sq
						World.getWorld().registerItem(
								new Item(1278, player.getX(), player.getY(), 1,
										player));
					}
				}
				case 2: {
					if (DataConversions.random(0, 500) < 4) { // Drop d axe
						World.getWorld().registerItem(
								new Item(594, player.getX(), player.getY(), 1,
										player));
					}
				}
				case 3: {
					if (DataConversions.random(0, 500) < 4) { // Drop d long
						World.getWorld().registerItem(
								new Item(593, player.getX(), player.getY(), 1,
										player));
					}
				}
				}
			}
		}
	}

	@Override
	public void onRangedAttack(Player attacker, Npc npc) {
		if (npc.getLocation().nearAltar()
				&& !attacker.getLocation().nearAltar()) {
			GameObject zara = new GameObject(attacker.getLocation(), 1036, 0, 0);
			World.getWorld().registerGameObject(zara);
			Instance.getDelayedEventHandler().add(new ObjectRemover(zara, 500));
			Player affectedPlayer = attacker;

			int damag = 10;
			Projectile projectil = new Projectile(npc, attacker, 1);

			attacker.setLastDamage(damag);
			int newhp = attacker.getHits() - damag;
			attacker.setHits(newhp);
			ArrayList<Player> playersToInfor = new ArrayList<Player>();
			playersToInfor.addAll(npc.getViewArea().getPlayersInView());
			playersToInfor.addAll(attacker.getViewArea().getPlayersInView());
			for (Player p : playersToInfor) {
				p.informOfProjectile(projectil);
				p.informOfModifiedHits(attacker);
			}
			attacker.getActionSender().sendStat(3);

			if (newhp <= 0) {
				attacker.killedBy(npc, false);
			}
			affectedPlayer.getActionSender().sendMessage(
					"@red@The dragon startles you by breathing fire on you!");
			affectedPlayer.getActionSender().sendMessage(
					"Maybe I should go closer and show him I'm not afraid!");
			affectedPlayer.resetRange();
		}
	}

}
