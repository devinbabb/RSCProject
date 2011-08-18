package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;

import rscproject.config.Constants;
import rscproject.config.Formulae;
import rscproject.gs.Instance;
import rscproject.gs.connection.Packet;
import rscproject.gs.connection.RSCPacket;
import rscproject.gs.event.FightEvent;
import rscproject.gs.event.RangeEvent;
import rscproject.gs.event.WalkToMobEvent;
import rscproject.gs.model.ChatMessage;
import rscproject.gs.model.InvItem;
import rscproject.gs.model.Mob;
import rscproject.gs.model.Npc;
import rscproject.gs.model.PathGenerator;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.model.minigames.CTF;
import rscproject.gs.model.snapshot.Activity;
import rscproject.gs.phandler.PacketHandler;
import rscproject.gs.states.Action;

public class AttackHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();

    public void handlePacket(Packet p, IoSession session) throws Exception {
	Player player = (Player) session.getAttachment();
	int pID = ((RSCPacket) p).getID();
	if (player.isBusy()) {
	    player.resetPath();
	    return;
	}
	player.resetAll();
	Mob affectedMob = null;
	int serverIndex = p.readShort();
	if (pID == 57) {
	    affectedMob = world.getPlayer(serverIndex);
	} else if (pID == 73) {
	    affectedMob = world.getNpc(serverIndex);
	}
	if(player.isPMod() && !player.isMod())
	    return;
	if (affectedMob == null || affectedMob.equals(player) || (affectedMob instanceof Npc && !World.getQuestManager().isNpcVisible((Npc) affectedMob, player))) {
	    player.resetPath();
	    return;
	}
	if (affectedMob instanceof Player) {

	    Player pl = (Player) affectedMob;
	    if (CTF.isInCaptureTheFlagBoundary(pl.getX(), pl.getY()) && !Instance.getCTF().isEventRunning) {
			pl.teleport(100, 500, false);
			return;
	    }
	    if(Instance.getCTF().isSameTeam(pl, player)) {
	    	player.getActionSender().sendMessage("You cannot attack your own team member");
			return;
	    }
	}
	if(affectedMob instanceof Npc) {
		if(World.getWar().isMyQueen((Npc)affectedMob, player)) {
			(player).getActionSender().sendMessage("This is your queen!");
			return;
		}
		if(World.getWar().inSameTeam(player, (Npc)affectedMob)) {
			(player).getActionSender().sendMessage("This monster is protecting your queen!");
			return;
		}
	}
	if (affectedMob instanceof Npc && player.getRangeEquip() > 0 && affectedMob.inCombat() && World.getQuestManager().isNpcAssociated(affectedMob, player)) {
	    player.getActionSender().sendMessage("You can't range the " + ((Npc) affectedMob).getDef().getName() + " while it's in combat!");
	    player.resetPath();
	    return;
	}
	if (affectedMob instanceof Player) {
	    Player pl = (Player) affectedMob;
	    if (pl.inCombat() && player.getRangeEquip() < 0) { // rev 24/25
							       // attack bugfix
		return;
	    }

	    if (pl.getLocation().inWilderness() && System.currentTimeMillis() - pl.lastRun < 3000) {
		return;
	    }
	}

	player.setFollowing(affectedMob);
	player.setStatus(Action.ATTACKING_MOB);

	if (player.getRangeEquip() < 0) {
	    Instance.getDelayedEventHandler().add(new WalkToMobEvent(player, affectedMob, affectedMob instanceof Npc ? 1 : 2) {
		public void arrived() {
		    owner.resetPath();
		    owner.resetFollowing();
		    boolean cont = false;
		    if (affectedMob instanceof Player) {
			Player opp = (Player) affectedMob;

			if (System.currentTimeMillis() - opp.lastMineTimer < 2000 && opp.isBusy())
			    cont = true;
		    }
		    if(affectedMob instanceof Player) {
		    	world.addEntryToSnapshots(new Activity(owner.getUsername(), owner.getUsername() + " attacked a Player (" + ((Player)affectedMob).getUsername() + ")"));
		    } else {
		    	world.addEntryToSnapshots(new Activity(owner.getUsername(), owner.getUsername() + " attacked a NPC (" + ((Npc)affectedMob).getDef().name + ")"));
		     }
		    if (cont) {
			if (owner.isBusy() || !owner.nextTo(affectedMob) || !owner.checkAttack(affectedMob, false) || owner.getStatus() != Action.ATTACKING_MOB) {
			    return;
			}
		    } else {
			if (owner.isBusy() || affectedMob.isBusy() || !owner.nextTo(affectedMob) || !owner.checkAttack(affectedMob, false) || owner.getStatus() != Action.ATTACKING_MOB) {
			    return;
			}
		    }
		    if (affectedMob.getID() == 35) {
			owner.getActionSender().sendMessage("Delrith can not be attacked without the Silverlight sword");
			return;
		    }
		    if (affectedMob.getID() == 140 && affectedMob.getX() > 327 && affectedMob.getX() < 335 && affectedMob.getY() > 433 && affectedMob.getY() < 439) {
			owner.informOfNpcMessage(new ChatMessage(affectedMob, "a curse be upon you", owner));
			for (int i = 0; i < 3; i++) {
			    int stat = owner.getCurStat(i);
			    if (stat < 3)
				owner.setCurStat(i, 0);
			    else
				owner.setCurStat(i, stat - 3);
			}
			owner.getActionSender().sendStats();

		    }

		    owner.resetAll();
		    owner.setStatus(Action.FIGHTING_MOB);
		    if (affectedMob instanceof Player) {
			Player affectedPlayer = (Player) affectedMob;
			if (!Instance.getCTF().inCTF(owner))
			    owner.setSkulledOn(affectedPlayer);
			affectedPlayer.resetAll();
			affectedPlayer.setStatus(Action.FIGHTING_MOB);
			affectedPlayer.getActionSender().sendSound("underattack");
			affectedPlayer.getActionSender().sendMessage("You are under attack!");
		    }
		    affectedMob.resetPath();

		    owner.setLocation(affectedMob.getLocation(), true);
		    for (Player p : owner.getViewArea().getPlayersInView()) {
			p.removeWatchedPlayer(owner);
		    }

		    owner.setBusy(true);
		    if (affectedMob instanceof Npc) {
			if (((Npc) affectedMob).isScripted()) {
				if(World.getWar().isMyQueen((Npc)affectedMob, owner)) {
					owner.getActionSender().sendMessage("You cannot attack your own king!");
					owner.setBusy(false);
					return;
					}
				System.out.println(World.getWar().inATeam(owner) + " && " +  World.getWar().isNpcAssociated((Npc)affectedMob));
				if(!World.getWar().inATeam(owner) && World.getWar().isNpcAssociated((Npc)affectedMob)) {
					owner.getActionSender().sendMessage("You cannot attack a king! Join a team, be apart of the war!");
					owner.setBusy(false);
					return;
				}
			    Instance.getPluginHandler().getNpcAIHandler(affectedMob.getID()).onMeleeAttack(owner, (Npc) affectedMob);
				}
			}
		    owner.setSprite(9);
		    /*
		     * if(affectedMob instanceof Npc) { Npc n =
		     * (Npc)affectedMob; for(Fighter p : n.fighters) {
		     * p.useCombat = true; if(p.player == owner) if(p.useMagic)
		     * { p.useMagic = false; break; } } }
		     */
		    owner.setOpponent(affectedMob);
		    owner.setCombatTimer();
		    affectedMob.setBusy(true);
		    affectedMob.setSprite(8);
		    affectedMob.setOpponent(owner);
		    affectedMob.setCombatTimer();
		    FightEvent fighting = new FightEvent(owner, affectedMob);
		    fighting.setLastRun(0);
		    Instance.getDelayedEventHandler().add(fighting);
		}
	    });
	} else {
	    if (!new PathGenerator(player.getX(), player.getY(), affectedMob.getX(), affectedMob.getY()).isValid()) {
		player.getActionSender().sendMessage("I can't get a clear shot from here");
		player.resetPath();
		return;
	    }
	    if(Constants.GameServer.F2P_WILDY && player.getLocation().inWilderness()) {
		
		for(InvItem i : player.getInventory().getItems()) {
		    if(i.getID() == 638 || i.getID() == 640 || i.getID() == 642 || i.getID() == 644 || i.getID() == 646) {
			player.getActionSender().sendMessage("You can not have any P2P arrows in your inventory in a F2P wilderness");
			return;
		    }
		}
		
	    }
	    int radius = 7;
	    if (player.getRangeEquip() == 59 || player.getRangeEquip() == 60)
		radius = 5;
	    if (player.getRangeEquip() == 189)
		radius = 4;
	    Instance.getDelayedEventHandler().add(new WalkToMobEvent(player, affectedMob, radius) {
		public void arrived() {
		    owner.resetPath();
		    if (owner.isBusy() || !owner.checkAttack(affectedMob, true) || owner.getStatus() != Action.ATTACKING_MOB) {
			return;
		    }

		    if (!new PathGenerator(owner.getX(), owner.getY(), affectedMob.getX(), affectedMob.getY()).isValid()) {
			owner.getActionSender().sendMessage("I can't get a clear shot from here");
			owner.resetPath();
			return;
		    }
		    if(affectedMob instanceof Player) {
		    	world.addEntryToSnapshots(new Activity(owner.getUsername(), owner.getUsername() + " ranged a Player (" + ((Player)affectedMob).getUsername() + ")"));
		    } else {
		    	world.addEntryToSnapshots(new Activity(owner.getUsername(), owner.getUsername() + " ranged a NPC (" + ((Npc)affectedMob).getDef().name + ")"));
		    }
		    if (affectedMob instanceof Npc) {
				if (((Npc) affectedMob).isScripted()) {
					if(World.getWar().isMyQueen((Npc)affectedMob, owner)) {
						owner.getActionSender().sendMessage("You cannot attack your own king!");
						owner.setBusy(false);
						return;
						}
					if(!World.getWar().inATeam(owner) && World.getWar().isNpcAssociated((Npc)affectedMob)) {
						owner.getActionSender().sendMessage("You cannot attack a king! Join a team, be apart of the war!");
						owner.setBusy(false);
						return;
					}
				    //Instance.getPluginHandler().getNpcAIHandler(affectedMob.getID()).onRangedAttack(owner, (Npc) affectedMob);
				}
			}
		    if (affectedMob.getID() == 35) {
			owner.getActionSender().sendMessage("Delrith can not be attacked without the Silverlight sword");
			return;
		    }
		    owner.resetAll();
		    owner.setStatus(Action.RANGING_MOB);
		    if (affectedMob instanceof Player) {
			Player affectedPlayer = (Player) affectedMob;
			if (!Instance.getCTF().inCTF(owner))
				owner.setSkulledOn(affectedPlayer);
			affectedPlayer.resetTrade();
			if (affectedPlayer.getMenuHandler() != null) {
			    affectedPlayer.resetMenuHandler();
			}
			if (affectedPlayer.accessingBank()) {
			    affectedPlayer.resetBank();
			}
			if (affectedPlayer.accessingShop()) {
			    affectedPlayer.resetShop();
			}
			if (affectedPlayer.getNpc() != null) {
			    affectedPlayer.getNpc().unblock();
			    affectedPlayer.setNpc(null);
			}
		    }
		    if (Formulae.getRangeDirection(owner, affectedMob) != -1)
			owner.setSprite(Formulae.getRangeDirection(owner, affectedMob));

		    owner.setRangeEvent(new RangeEvent(owner, affectedMob));
		}
	    });

	}
    }
}