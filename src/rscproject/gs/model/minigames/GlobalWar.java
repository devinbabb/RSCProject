package rscproject.gs.model.minigames;

import rscproject.gs.Instance;
import rscproject.gs.event.DelayedEvent;
import rscproject.gs.event.SingleEvent;
import rscproject.gs.external.ItemLoc;
import rscproject.gs.model.*;
import rscproject.gs.states.CombatState;
import rscproject.gs.tools.DataConversions;

import java.util.ArrayList;

/**
 * War system
 *
 * @author Devin
 */
public class GlobalWar {
    /**
     * World instnace
     */
    private static final World world = World.getWorld();
    /**
     * Stores coords for wilderness "bases".
     */
    private int[][] wildLocations = {{219, 231}, {247, 339}, {237, 231}, {265, 339}};

    /**
     * Returns cordinates to where the base is (factors in player team + if event is low or high level)
     *
     * @param p player wishing to teleport.
     * @return
     */
    public int[] getCoords(Player p) {
        if (redTeam.contains(p)) {
            if (low)
                return wildLocations[1];
            else
                return wildLocations[0];
        } else if (blueTeam.contains(p)) {
            if (low)
                return wildLocations[3];
            else
                return wildLocations[2];
        }
        return null;
    }

    public int[] getStartCoords(int team) {
        if (team == 0) {
            if (low)
                return wildLocations[1];
            else
                return wildLocations[0];
        } else if (team == 1) {
            if (low)
                return wildLocations[3];
            else
                return wildLocations[2];
        }
        return null;
    }

    /**
     * Players in the red team are refrenced here
     */
    private ArrayList<Player> redTeam = new ArrayList<Player>();
    /**
     * Player name array list, used to determine if player was in a team when he logged out/in
     */
    private ArrayList<String> redTeamPlayernames = new ArrayList<String>();
    /**
     * Players in the blue team are refrenced here
     */
    private ArrayList<Player> blueTeam = new ArrayList<Player>();
    /**
     * Player name array list, used to determine if player was in a team when he logged out/in
     */
    private ArrayList<String> blueTeamPlayernames = new ArrayList<String>();
    /**
     * Determines if the global war is enabled or not
     */
    private boolean warMode = false;
    /**
     * Determines were the war is beind held, if true the war will be held at wild 15, otherwise wild 30.
     */
    private boolean low = false;
    /**
     * Determines when the last message to the red team was sent (warnings about queens)
     */
    private long lastRedMessage = 0;
    /**
     * Determines when the last message to the blue team was sent (warnings about queens)
     */
    private long lastBlueMessage = 0;

    /**
     * Determines when the last spawn to the red team was done (protection for queens)
     */
    private long lastRedSpawn = 0;
    /**
     * Determines when the last spawn to the blue team was done (protection for queens)
     */
    private long lastBlueSpawn = 0;

    /**
     * King id
     */
    private final int queenId = 254;
    /**
     * Red queen refrence
     */
    private Npc redQueen;
    /**
     * Blue queen refrence
     */
    private Npc blueQueen;
    /**
     * Spawned percival npc refrences (used to remove them after war is over)
     */
    private ArrayList<Npc> percival = new ArrayList<Npc>();
    /**
     * Spawned rovin npc refrences (used to remove them after war is over)
     */
    private ArrayList<Npc> rovin = new ArrayList<Npc>();
    /**
     * Red team spawn item refrences (used to remove them after war is over)
     */
    private ArrayList<Item> redTeamItems = new ArrayList<Item>();
    /**
     * Blue team spawn item refrences (used to remove them after war is over)
     */
    private ArrayList<Item> blueTeamItems = new ArrayList<Item>();
    /**
     * Blue cape (to determine which team someone is apart of)
     */
    public static final InvItem BLUE_CAPE = new InvItem(183);
    /**
     * Red cape (to determine which team someone is apart of)
     */
    public static final InvItem RED_CAPE = new InvItem(229);

    /**
     * Determines if the NPC is a war NPC or not
     *
     * @param n npc
     * @return true if War is running and Npc is one of the kings
     */
    public boolean isNpcAssociated(Npc n) {
        if (!isRunning()) return false;
        if (n.getID() == 415 || n.getID() == 18)
            return true;
        if (n.getID() == queenId && n.getX() == getStartCoords(0)[0] && n.getY() == getStartCoords(0)[1]) {
            return true;
        } else if (n.getID() == queenId && n.getX() == getStartCoords(1)[0] && n.getY() == getStartCoords(1)[1]) {
            return true;
        }
        return false;
    }

    /**
     * Determines if the npc is the players king
     *
     * @param n
     * @param p
     * @return
     */
    public boolean isMyQueen(Npc n, Player p) {
        if (!isRunning()) return false;
        if (inRed(p) && n.getTeam() == 0 && n.getID() == queenId && n.getX() == getStartCoords(0)[0] && n.getY() == getStartCoords(0)[1]) {
            return true;
        } else if (inBlue(p) && n.getTeam() == 1 && n.getID() == queenId && n.getX() == getStartCoords(1)[0] && n.getY() == getStartCoords(1)[1]) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a player is in a team already
     *
     * @param p
     * @return
     */
    public boolean inATeam(Player p) {
        return inRed(p) || inBlue(p);
    }

    public int getTeam(Player p) {
        if (inRed(p)) {
            return 0;
        }
        if (inBlue(p)) {
            return 1;
        }
        return 2;
    }

    /**
     * Starts the war
     *
     * @param low if true the war will be held at wild 15, otherwise wild 30
     */
    public void startWar(boolean low) {
        this.low = low;
        if (low) {
            this.kingHits = 5000;
        } else {
            this.kingHits = 10000;
        }
        RED_CAPE.setWield(true);
        BLUE_CAPE.setWield(true);
        /**
         * Let everyone know war has begun
         */
        for (Player p : world.getPlayers()) {
            p.getActionSender().sendMessage("@red@Global war mode enabled! RSCAngel is at war!");
            p.getActionSender().sendMessage("@red@Go to edgeville to join a team and protect your queen! Or go PK solo!");
        }
        /**
         * Spawn kings
         */
        redQueen = new Npc(queenId, getStartCoords(0)[0], getStartCoords(0)[1], getStartCoords(0)[0] - 2, getStartCoords(0)[0] + 2, getStartCoords(0)[1] - 2, getStartCoords(0)[1] + 2);
        redQueen.setRespawn(false);
        redQueen.setScripted(true);
        redQueen.setHits(kingHits);
        redQueen.setTeam(0);
        redQueen.updateAppearanceID();
        world.registerNpc(redQueen);

        blueQueen = new Npc(queenId, getStartCoords(1)[0], getStartCoords(1)[1], getStartCoords(1)[0] - 2, getStartCoords(1)[0] + 2, getStartCoords(1)[1] - 2, getStartCoords(1)[1] + 2);
        blueQueen.setTeam(1);
        blueQueen.setRespawn(false);
        blueQueen.setHits(kingHits);
        blueQueen.setScripted(true);
        world.registerNpc(blueQueen);

        /**
         * Spawn needed NPCs
         */
        for (int i = 0; i < 5; i++) {
            Npc perc = new Npc(415, 215 - i, 436, 215 - 2 - i, 215 + 2 - i, 436 - 2, 436 + 2);
            Npc rov = new Npc(18, 217 - i, 438, 217 - 2 - i, 217 + 2 - i, 438 - 2, 438 + 2);
            perc.setRespawn(false);
            rov.setRespawn(false);
            world.registerNpc(perc);
            world.registerNpc(rov);
            percival.add(perc);
            rovin.add(rov);
        }
        /**
         * Spawn red team war ground items
         */
        for (int x = 0; x < 3; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 373;
            il.respawnTime = 10;
            il.x = getRandomCoordsNearQueen(0)[0];
            il.y = getRandomCoordsNearQueen(0)[1];
            il.amount = 1;
            Item i = new Item(il);
            world.registerItem(i);
            redTeamItems.add(i);
        }
        for (int x = 0; x < 3; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 224;
            il.respawnTime = 10;
            il.x = getRandomCoordsNearQueen(0)[0];
            il.y = getRandomCoordsNearQueen(0)[1];
            il.amount = 1;
            Item i = new Item(il);
            world.registerItem(i);
            redTeamItems.add(i);
        }
        for (int x = 0; x < 2; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 188;
            il.respawnTime = 10;
            il.x = getRandomCoordsNearQueen(0)[0];
            il.y = getRandomCoordsNearQueen(0)[1];
            il.amount = 1;
            Item i = new Item(il);
            world.registerItem(i);
            redTeamItems.add(i);
        }
        for (int x = 0; x < 2; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 60;
            il.respawnTime = 10;
            il.x = getRandomCoordsNearQueen(0)[0];
            il.y = getRandomCoordsNearQueen(0)[1];
            il.amount = 1;
            Item i = new Item(il);
            world.registerItem(i);
            redTeamItems.add(i);
        }
        for (int x = 0; x < 2; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 11;
            il.respawnTime = 60;
            il.x = getRandomCoordsNearQueen(0)[0];
            il.y = getRandomCoordsNearQueen(0)[1];
            il.amount = 30;
            Item i = new Item(il);
            world.registerItem(i);
            redTeamItems.add(i);
        }
        for (int x = 0; x < 2; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 190;
            il.respawnTime = 60;
            il.x = getRandomCoordsNearQueen(0)[0];
            il.y = getRandomCoordsNearQueen(0)[1];
            il.amount = 30;
            Item i = new Item(il);
            world.registerItem(i);
            redTeamItems.add(i);
        }
        for (int x = 0; x < 2; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 33;
            il.respawnTime = 60;
            il.x = getRandomCoordsNearQueen(0)[0];
            il.y = getRandomCoordsNearQueen(0)[1];
            il.amount = 60;
            Item i = new Item(il);
            world.registerItem(i);
            redTeamItems.add(i);
        }
        for (int x = 0; x < 2; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 31;
            il.respawnTime = 60;
            il.x = getRandomCoordsNearQueen(0)[0];
            il.y = getRandomCoordsNearQueen(0)[1];
            il.amount = 60;
            Item i = new Item(il);
            world.registerItem(i);
            redTeamItems.add(i);
        }
        for (int x = 0; x < 2; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 35;
            il.respawnTime = 60;
            il.x = getRandomCoordsNearQueen(0)[0];
            il.y = getRandomCoordsNearQueen(0)[1];
            il.amount = 30;
            Item i = new Item(il);
            world.registerItem(i);
            redTeamItems.add(i);
        }
        /**
         * Spawn blue team war ground items
         */
        for (int x = 0; x < 3; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 373;
            il.respawnTime = 10;
            il.x = getRandomCoordsNearQueen(1)[0];
            il.y = getRandomCoordsNearQueen(1)[1];
            il.amount = 1;
            Item i = new Item(il);
            world.registerItem(i);
            blueTeamItems.add(i);
        }
        for (int x = 0; x < 3; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 224;
            il.respawnTime = 10;
            il.x = getRandomCoordsNearQueen(1)[0];
            il.y = getRandomCoordsNearQueen(1)[1];
            il.amount = 1;
            Item i = new Item(il);
            world.registerItem(i);
            blueTeamItems.add(i);
        }
        for (int x = 0; x < 2; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 188;
            il.respawnTime = 10;
            il.x = getRandomCoordsNearQueen(1)[0];
            il.y = getRandomCoordsNearQueen(1)[1];
            il.amount = 1;
            Item i = new Item(il);
            world.registerItem(i);
            blueTeamItems.add(i);
        }
        for (int x = 0; x < 2; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 60;
            il.respawnTime = 10;
            il.x = getRandomCoordsNearQueen(1)[0];
            il.y = getRandomCoordsNearQueen(1)[1];
            il.amount = 1;
            Item i = new Item(il);
            world.registerItem(i);
            blueTeamItems.add(i);
        }
        for (int x = 0; x < 2; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 11;
            il.respawnTime = 60;
            il.x = getRandomCoordsNearQueen(1)[0];
            il.y = getRandomCoordsNearQueen(1)[1];
            il.amount = 30;
            Item i = new Item(il);
            world.registerItem(i);
            blueTeamItems.add(i);
        }
        for (int x = 0; x < 2; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 190;
            il.respawnTime = 60;
            il.x = getRandomCoordsNearQueen(1)[0];
            il.y = getRandomCoordsNearQueen(1)[1];
            il.amount = 30;
            Item i = new Item(il);
            world.registerItem(i);
            blueTeamItems.add(i);
        }
        for (int x = 0; x < 2; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 33;
            il.respawnTime = 60;
            il.x = getRandomCoordsNearQueen(1)[0];
            il.y = getRandomCoordsNearQueen(1)[1];
            il.amount = 60;
            Item i = new Item(il);
            world.registerItem(i);
            blueTeamItems.add(i);
        }
        for (int x = 0; x < 2; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 31;
            il.respawnTime = 60;
            il.x = getRandomCoordsNearQueen(1)[0];
            il.y = getRandomCoordsNearQueen(1)[1];
            il.amount = 60;
            Item i = new Item(il);
            world.registerItem(i);
            blueTeamItems.add(i);
        }
        for (int x = 0; x < 2; x++) {
            ItemLoc il = new ItemLoc();
            il.id = 35;
            il.respawnTime = 60;
            il.x = getRandomCoordsNearQueen(1)[0];
            il.y = getRandomCoordsNearQueen(1)[1];
            il.amount = 30;
            Item i = new Item(il);
            world.registerItem(i);
            blueTeamItems.add(i);
        }
        warMode = true;
        alertUsers();


    }

    /**
     * Gets random coords near the queen
     *
     * @param team 0 for red 1 for blue
     * @return random coords
     */
    public int[] getRandomCoordsNearQueen(int team) {
        int x = getStartCoords(team)[0];
        int y = getStartCoords(team)[1];
        if (DataConversions.random(0, 1) == 0) {
            x += DataConversions.random(0, 5);
        } else {
            x -= DataConversions.random(0, 5);
        }
        if (DataConversions.random(0, 1) == 0) {
            y += DataConversions.random(0, 5);
        } else {
            y -= DataConversions.random(0, 5);
        }
        return new int[]{x, y};
    }

    /**
     * Starts the global advertisement for war (every 60s announces the war state)
     */
    public void alertUsers() {
        Instance.getDelayedEventHandler().add(new DelayedEvent(null, 60000) {
            @Override
            public void run() {
                if (World.getWar().isRunning()) {
                    for (Player p : world.getPlayers()) {
                        p.getActionSender().sendMessage("@gre@RSCA is at war!");
                        p.getActionSender().sendMessage("@red@The red queen has @whi@" + redQueen.getHits() + "@red@/@whi@" + kingHits + "@red@ HP left");
                        p.getActionSender().sendMessage("@blu@The blue queen has @whi@" + blueQueen.getHits() + "@blu@/@whi@" + kingHits + "@blu@ HP left");
                    }
                } else
                    this.stop();
            }

        });
    }

    /**
     * Ends the war
     */
    public void endWar(int team) {
        if (team == 0) {
            for (Player p : world.getPlayers()) {
                p.getActionSender().sendMessage("@red@Red team is victorious and the war is done! RSCAngel is at peace!");
                handleCapeUnWield(p);
            }
        }
        if (team == 1) {
            for (Player p : world.getPlayers()) {
                p.getActionSender().sendMessage("@red@Blue team is victorious and the war is done! RSCAngel is at peace!");
                handleCapeUnWield(p);
            }
        } else {
            for (Player p : world.getPlayers()) {
                p.getActionSender().sendMessage("@red@The war is over! RSCAngel is at peace!");
            }
        }
        for (Player p : redTeam) {
            handleCapeUnWield(p);
        }
        for (Player p : blueTeam) {
            handleCapeUnWield(p);
        }
        /**
         * Remove kings
         */

        redQueen.remove();
        blueQueen.remove();
        world.unregisterNpc(redQueen);
        world.unregisterNpc(blueQueen);

        /**
         * Remove Npcs
         */
        for (Npc n : percival) {
            n.remove();
            world.unregisterNpc(n);
        }
        for (Npc n : rovin) {
            n.remove();
            world.unregisterNpc(n);
        }
        /**
         * Remove items
         */
        for (Item i : redTeamItems) {
            i.getLoc().respawnTime = 0;
            i.remove();
        }
        for (Item i : blueTeamItems) {
            i.getLoc().respawnTime = 0;
            i.remove();
        }
        /**
         * Clear values
         */
        blueTeam = new ArrayList<Player>();
        redTeam = new ArrayList<Player>();
        blueTeamPlayernames = new ArrayList<String>();
        redTeamPlayernames = new ArrayList<String>();
        lastBlueMessage = 0;
        lastRedMessage = 0;
        lastRedSpawn = 0;
        lastBlueSpawn = 0;
        warMode = false;
    }

    /**
     * Determines if the war is running or not
     *
     * @return true false
     */
    public boolean isRunning() {
        return warMode;
    }

    /**
     * Joins a war team
     *
     * @param team determines which team, 0 for red 1 for blue
     * @param p    player that joins
     */
    public void joinTeam(int team, Player p) {
        if (team < 0 || team > 1) return; //Should't happen
        if (team == 0) {
            redTeam.add(p);
            redTeamPlayernames.add(p.getUsername());
            p.getActionSender().sendMessage("You have joined the red team!");
        }
        if (team == 1) {
            blueTeam.add(p);
            blueTeamPlayernames.add(p.getUsername());
            p.getActionSender().sendMessage("You have joined the blue team!");
        }
        handleCapeWield(p);
        p.getActionSender().sendMessage("You can get some free food from your base. Talk to the other NPC");
        p.getActionSender().sendMessage("to be sent to the battlefield in wild " + (low ? "15" : "30") + "!");
    }

    /**
     * Handles all the player logout logic (removes from array, keeps string in array though)
     */
    public void handlePlayerLogout(Player p) {
        if (!isRunning())
            return;
        redTeam.remove(p); // Why check when we can simply remove without a problem
        blueTeam.remove(p); // Why check when we can simply remove without a problem

    }

    /**
     * Handles player login logic (readds to the right team on login etc)
     *
     * @param p
     */
    public void handlePlayerLogin(Player p) {
        if (!isRunning())
            return;
        if (redTeamPlayernames.contains(p.getUsername())) {
            redTeam.add(p);
            p.getActionSender().sendMessage("You're at war soldier! Only quitters log out during wars!");
            p.getActionSender().sendMessage("You're no quitter! Get back into the fight!");
        } else if (blueTeamPlayernames.contains(p.getUsername())) {
            blueTeam.add(p);
            p.getActionSender().sendMessage("You're at war soldier! Only quitters log out during wars!");
            p.getActionSender().sendMessage("You're no quitter! Get back into the fight!");
        }
        handleCapeWield(p);
    }

    /**
     * Handles the cape wielding for players
     *
     * @param p
     */
    public void handleCapeWield(Player p) {
        if (!isRunning())
            return;
        if (blueTeam.contains(p)) {
            p.updateWornItems(RED_CAPE.getWieldableDef().getWieldPos(), RED_CAPE.getWieldableDef().getSprite());
        } else if (redTeam.contains(p)) {
            p.updateWornItems(BLUE_CAPE.getWieldableDef().getWieldPos(), BLUE_CAPE.getWieldableDef().getSprite());
        }
    }

    /**
     * Handles the cape unwielding for players
     *
     * @param p
     */
    public void handleCapeUnWield(Player p) {

        for (InvItem i : p.getInventory().getItems()) {
            if (RED_CAPE.wieldingAffectsItem(i) && i.isWielded()) {
                i.setWield(false);
            }
        }
        p.updateWornItems(RED_CAPE.getWieldableDef().getWieldPos(), 0);
        p.getActionSender().sendInventory();
    }

    /**
     * Checks if two players are in the same team
     *
     * @param p1
     * @param p2
     * @return
     */
    public boolean inSameTeam(Player p1, Player p2) {
        return (redTeam.contains(p1) && redTeam.contains(p2)) || (blueTeam.contains(p1) && blueTeam.contains(p2));
    }

    public boolean inSameTeam(Player p1, Npc n) {
        return ((redTeam.contains(p1) && n.getTeam() == 0) || (blueTeam.contains(p1) && n.getTeam() == 1));
    }

    /**
     * Determines if the player is in the blue team or not
     *
     * @param p
     * @return
     */
    public boolean inBlue(Player p) {
        return blueTeam.contains(p);
    }

    /**
     * Determines if the player is in the red team or not
     *
     * @param p
     * @return
     */
    public boolean inRed(Player p) {
        return redTeam.contains(p);
    }

    /**
     * Sends a message to the entire team
     *
     * @param team    determines which team, 0 for red 1 for blue
     * @param message what to send
     */
    public void sendMessage(int team, String message) {
        if (team == 0) {
            lastRedMessage = System.currentTimeMillis();
            for (Player p : redTeam)
                p.getActionSender().sendMessage(message);
        } else if (team == 1) {
            lastBlueMessage = System.currentTimeMillis();
            for (Player p : blueTeam)
                p.getActionSender().sendMessage(message);
        }
    }

    public Npc getQueen(int team) {
        if (team == 0)
            return redQueen;
        else return blueQueen;
    }

    public void spawnProtection(int team, Player p) {
        if (team == 0) {
            lastRedSpawn = System.currentTimeMillis();
            final Npc x = new Npc(248, p.getX(), p.getY(), p.getX() - 5, p.getX() + 5, p.getY() - 5, p.getY() + 5);
            x.setTeam(team);
            x.setRespawn(false);
            World.getWorld().registerNpc(x);
            Instance.getDelayedEventHandler().add(new SingleEvent(null, 60000) {
                public void action() {
                    Mob opponent = x.getOpponent();
                    if (opponent != null) {
                        opponent.resetCombat(CombatState.ERROR);
                    }
                    x.resetCombat(CombatState.ERROR);
                    world.unregisterNpc(x);
                    x.remove();
                }
            });
        } else if (team == 1) {
            lastBlueSpawn = System.currentTimeMillis();
            final Npc x = new Npc(248, p.getX(), p.getY(), p.getX() - 5, p.getX() + 5, p.getY() - 5, p.getY() + 5);
            x.setTeam(team);
            x.setRespawn(false);
            World.getWorld().registerNpc(x);
            Instance.getDelayedEventHandler().add(new SingleEvent(null, 60000) {
                public void action() {
                    Mob opponent = x.getOpponent();
                    if (opponent != null) {
                        opponent.resetCombat(CombatState.ERROR);
                    }
                    x.resetCombat(CombatState.ERROR);
                    world.unregisterNpc(x);
                    x.remove();
                }
            });
        }
    }

    public long getLastSpawn(int team) {
        if (team == 0)
            return lastRedSpawn;
        else
            return lastBlueSpawn;
    }

    /**
     * Gets the time the last team message was sent
     *
     * @param team determines which team, 0 for red 1 for blue
     * @return
     */
    public long getLastMessage(int team) {
        if (team == 0)
            return lastRedMessage;
        else
            return lastBlueMessage;
    }

    /**
     * Joins a team based on which team has less players
     *
     * @param player
     */
    public void joinRandomTeam(Player player) {
        if (redTeam.size() > blueTeam.size()) joinTeam(1, player);
        else joinTeam(0, player);
    }

    /**
     * Leaves the war
     *
     * @param player
     */
    public void leaveTeam(Player player) {
        handleCapeUnWield(player);
        if (redTeam.contains(player)) {
            redTeam.remove(player);
            redTeamPlayernames.remove(player.getUsername());
        }
        if (blueTeam.contains(player)) {
            blueTeam.remove(player);
            blueTeamPlayernames.remove(player.getUsername());
        }
        player.getActionSender().sendMessage("You have left the war, you damn panzy!");
    }

    private int kingHits = 1000;

    public int getKingHits() {
        return kingHits;
    }

}
