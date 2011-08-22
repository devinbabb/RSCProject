package rscproject.gs.model.minigames;

import rscproject.config.Formulae;
import rscproject.gs.Instance;
import rscproject.gs.event.DelayedEvent;
import rscproject.gs.event.SingleEvent;
import rscproject.gs.model.InvItem;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;

import java.util.ArrayList;

public class Lottery {
    /**
     * Lottery entriees
     */
    public ArrayList<Player> players = new ArrayList<Player>();
    /**
     * Lottery entreii names (used for re-logging usernames)
     */
    public ArrayList<String> playerUsernames = new ArrayList<String>();
    /**
     * How much is the entry fee?
     */
    public int entryfee = 10000;
    /**
     * How much is the award
     */
    public int award = 0;
    /**
     * How many minutes before announcing lottery winner
     */
    public int waitBeforeAnnouncing = 5;
    /**
     * Is this running?
     */
    boolean running = false;

    /**
     * Start lottery ticket sales
     */
    public void startLotterySales() {
        World.getWorld().sendWorldAnnouncement("The lottery is running! Type ::lottery to buy a ticket for " + entryfee + "!");
        World.getWorld().sendWorldAnnouncement("All the money is put into the pot and winner gets it all!");
        World.getWorld().sendWorldAnnouncement("Current pot is: " + award);
        if (running) {
            return;
        }
        running = true;
        Instance.getDelayedEventHandler().add(new DelayedEvent(null, 60000) {
            @Override
            public void run() {
                if (running) {
                    World.getWorld().sendWorldAnnouncement("The lottery is running! Type ::lottery to buy a ticket for " + entryfee + "!");
                    World.getWorld().sendWorldAnnouncement("All the money is put into the pot and winner gets it all!");
                    World.getWorld().sendWorldAnnouncement("Current pot is: " + award);
                } else {
                    this.stop();
                }

            }

        });
        Instance.getDelayedEventHandler().add(new SingleEvent(null, waitBeforeAnnouncing * 60000) {

            @Override
            public void action() {
                endLottery();

            }

        });
    }

    /**
     * Ends the lottery by declaring winner and initilizing variables
     */
    public void endLottery() {
        running = false;
        World.getWorld().sendWorldAnnouncement("The lottery is over! The winner is: " + findWinner());
        /**
         * Initilize everything
         */
        players = new ArrayList<Player>();
        playerUsernames = new ArrayList<String>();
        award = 0;
    }

    /**
     * Finds a winner and distributes the award
     *
     * @return
     */
    public String findWinner() {
        if (players.size() <= 0) {
            return "No-one participated!";
        }
        Player p = players.get(Formulae.Rand(0, (players.size() - 1)));
        if (p != null && p.getUsername() != null) {
            p.getActionSender().sendMessage("Congratulations, you have won the lottery!");
            InvItem coins = new InvItem(10);
            coins.setAmount(award);
            p.getInventory().add(coins);
            p.getActionSender().sendInventory();
            return p.getUsername();
        } else {
            players.remove(p);
            return findWinner();
        }
    }

    public void handlePlayerJoin(Player p) {
        if (!running) {
            p.getActionSender().sendMessage("Lottery isn't running at the moment!");
            return;
        }
        if (players.contains(p)) {
            p.getActionSender().sendMessage("You have already joined the lottery!");
            return;
        }
        if (p.getInventory().countId(10) >= entryfee && running) {
            if (p.getInventory().remove(10, entryfee) != -1) {
                players.add(p);
                playerUsernames.add(p.getUsername());
                p.getActionSender().sendMessage("You have joined the lottery! Good luck!");
                p.getActionSender().sendInventory();
                award += entryfee;
                return;
            }
        }
        p.getActionSender().sendMessage("Looks like you don't have enough money on you! Get " + entryfee + " to join!");

    }

    public void handlePlayerLogin(Player p) {
        if (playerUsernames.contains(p.getUsername()) && running) {
            players.add(p);
            p.getActionSender().sendMessage("Dont log out like that! The lottery is going on!");
        }
    }

    public void handlePlayerLogout(Player p) {
        if (players.contains(p) && running) {
            players.remove(p);
        }
    }
}
