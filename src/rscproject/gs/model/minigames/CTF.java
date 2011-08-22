package rscproject.gs.model.minigames;

import rscproject.config.Constants;
import rscproject.gs.builders.RSCPacketBuilder;
import rscproject.gs.event.MiniEvent;
import rscproject.gs.model.*;
import rscproject.gs.util.Logger;

import java.util.ArrayList;

/**
 * @author Devin
 * @author Pets
 */

public class CTF {
    /**
     * World instnace
     */
    private static final World world = World.getWorld();
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
     * Determines if the CTF is enabled or not
     */
    private boolean matchRunning = false;

    private boolean warmupRunning = false;

    public boolean isEventRunning = false;

    private Player redFlagCarrier = null;

    private Player blueFlagCarrier = null;

    private int blueScore = 0;

    private int redScore = 0;

    public static final int POINTS_TO_WIN = 3;
    public static final int RED_FLAG_ID = 1170;
    public static final int BLUE_FLAG_ID = 1169;
    public static final Point RED_SPAWN = Point.location(80, 15);
    public static final Point BLUE_SPAWN = Point.location(19, 21);
    public static final InvItem RED_GEAR_CHEST = new InvItem(702);
    public static final InvItem RED_GEAR_LEG = new InvItem(703);
    public static final InvItem BLUE_GEAR_CHEST = new InvItem(184);
    public static final InvItem BLUE_GEAR_LEG = new InvItem(187);
    public static final InvItem BLUE_CARRIER_ITEM = new InvItem(183);
    public static final InvItem RED_CARRIER_ITEM = new InvItem(229);
    public static final int[] FREE_ITEMS = {221, 222, 223, 224, 367, 188, 11, 31, 32, 33, 34, 35, 38, 41};
    public static final int[] DISALLOWED_ITEMS = {40, 42,
            183,
            209, 229,
            370, 373,
            474, 475, 476, 477, 478, 479, 480, 481, 482, 483, 484, 485, 486, 487, 488, 489, 490, 491, 492, 493, 494, 495, 496, 497, 498, 499,
            500, 511, 512, 513, 514, 546, 551, 553, 555, 559, 560, 561, 562, 563, 564, 565, 566, 567, 568, 569, 570, 571, 572, 592,
            619, 637, 638, 639, 640, 641, 642, 643, 644, 645, 646, 647, 648, 649, 650, 651, 652, 653, 654, 655, 656, 657, 676,
            723, 750, 751, 796,
            825,
            1213, 1214, 1215, 1216, 1217, 1218, 1288};
    public static Point RED_FLAG = Point.location(69, 18);
    public static Point BLUE_FLAG = Point.location(30, 18);
    public static int currentLevelRank = 0;
    public static final Point RED_GATES1 = Point.location(77, 16);
    public static final Point RED_GATES2 = Point.location(77, 18);
    public static final Point BLUE_GATES1 = Point.location(23, 18);
    public static final int GATE_ID = 1033;
    public static final Point RED_CHEST = Point.location(82, 14);
    public static final Point BLUE_CHEST = Point.location(17, 22);
    public static final int CHEST_ID = 1005;
    private String oldMOTD = "";
    public int redDeaths = 0;
    public int blueDeaths = 0;
    public int redKills = 0;
    public int blueKills = 0;
    public boolean firstblood = true;
    public static final int DEATHS_FOR_HUMULIATION = 5;
    public static final int RAMPAGE_KILLS = 5;
    public static final int UNSTOPPABLE_KILLS = 10;
    public int PLAYERS_NEEDED = 2;
    private static final Point MIN_RADIUS = Point.location(12, 4);
    private static final Point MAX_RADIUS = Point.location(91, 33);
    public static final int WILDERNESS_LEVEL = 150;

    public void startWarmup(int lvlrank) {
        if (matchRunning || warmupRunning || isEventRunning || World.getWar().isRunning())
            return;
        /**
         * Initlizie
         */

        blueTeam = new ArrayList<Player>();
        redTeam = new ArrayList<Player>();
        blueTeamPlayernames = new ArrayList<String>();
        redTeamPlayernames = new ArrayList<String>();

        blueScore = 0;
        redScore = 0;

        redFlagCarrier = null;
        blueFlagCarrier = null;

        /**
         * Settings
         */
        isEventRunning = true;
        warmupRunning = true;
        currentLevelRank = lvlrank;
        doGates(false);
        doFlags();
        doChests(true);
        world.sendBroadcastMessage("@yel@Server@whi@", "@gre@Capture the Flag@whi@ is about to begin! (Levels " + getLevelString(currentLevelRank) + ")");
        world.sendBroadcastMessage("@yel@Server@whi@", "type @yel@::ctf@whi@ to join the event!");
        oldMOTD = Constants.GameServer.MOTD;
        Constants.GameServer.MOTD = "@gre@Capture the Flag is Live.@whi@ (Levels " + getLevelString(currentLevelRank) + ") Type ::ctf to join is elgible.";
    }

    public void startMatchCTF() {
        if (!warmupRunning || matchRunning || World.getWar().isRunning()) {
            return;
        }
        matchRunning = true;
        warmupRunning = false;
        firstblood = true;
        world.getDelayedEventHandler().add(new MiniEvent(null, 5000) {
            public void action() {
                sendCTFAnnouncement("@whi@Round starts in 25 Seconds");
                sendCTFMessage("Turn on your sound, and up your volume for a better experience");
            }
        });
        world.getDelayedEventHandler().add(new MiniEvent(null, 15000) {
            public void action() {
                sendCTFAnnouncement("@whi@Round starts in 15 seconds!");
                sendSound("preparetofight");
                world.getDelayedEventHandler().add(new MiniEvent(null, 14670) {
                    public void action() {
                        doGates(true);
                    }
                });
                world.getDelayedEventHandler().add(new MiniEvent(null, 12000) {
                    public void action() {
                        sendSound("countdown");
                        sendCTFAnnouncement("@red@  3..          ");
                        world.getDelayedEventHandler().add(new MiniEvent(null, 1000) {
                            public void action() {
                                sendCTFAnnouncement("@red@  3..   2..       ");
                                world.getDelayedEventHandler().add(new MiniEvent(null, 1000) {
                                    public void action() {
                                        sendCTFAnnouncement("@red@  3..   2..  1..     ");
                                        world.getDelayedEventHandler().add(new MiniEvent(null, 1000) {
                                            public void action() {
                                                sendCTFAnnouncement("@red@  3..   2..  1.. @whi@Fight!    ");
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    public void endCTF() {
        try {
            world.sendWorldMessage("Capture The Flag is over!");
            matchRunning = false;
            warmupRunning = false;
            isEventRunning = false;
            setGameLocked(false);

            Constants.GameServer.MOTD = oldMOTD;
            GameObject object = world.getTile(RED_FLAG).getGameObject();
            if (object != null)
                world.unregisterGameObject(object);
            object = world.getTile(BLUE_FLAG).getGameObject();
            if (object != null)
                world.unregisterGameObject(object);

            doGates(false);
            doChests(false);

            for (Player p : redTeam) {
                handlePlayerLeave(p, false);
            }
            for (Player p : blueTeam) {
                handlePlayerLeave(p, false);
            }
            redTeam = new ArrayList<Player>();
            blueTeam = new ArrayList<Player>();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void doGates(boolean open) {
        GameObject object;
        if (open) {
            object = world.getTile(RED_GATES1).getGameObject();
            if (object != null)
                world.unregisterGameObject(object);

            object = world.getTile(BLUE_GATES1).getGameObject();
            if (object != null)
                world.unregisterGameObject(object);

            object = world.getTile(RED_GATES2).getGameObject();
            if (object != null)
                world.unregisterGameObject(object);
        } else {
            world.registerGameObject(new GameObject(BLUE_GATES1, GATE_ID, 0, 0));
            world.registerGameObject(new GameObject(RED_GATES2, GATE_ID, 0, 0));
            world.registerGameObject(new GameObject(RED_GATES1, GATE_ID, 0, 0));
        }
    }

    public void doFlags() {
        world.registerGameObject(new GameObject(BLUE_FLAG, BLUE_FLAG_ID, 0, 0));
        world.registerGameObject(new GameObject(RED_FLAG, RED_FLAG_ID, 0, 0));
    }

    public void doChests(boolean spawn) {
        if (spawn) {
            world.registerGameObject(new GameObject(BLUE_CHEST, CHEST_ID, 0, 0));
            world.registerGameObject(new GameObject(RED_CHEST, CHEST_ID, 0, 0));
        } else {
            GameObject object = world.getTile(BLUE_CHEST).getGameObject();
            if (object != null)
                world.unregisterGameObject(object);
            object = world.getTile(RED_CHEST).getGameObject();
            if (object != null)
                world.unregisterGameObject(object);
        }
    }

    public static String getLevelString(int lvlrank) {
        if (lvlrank == 0)
            return "ALL LEVELS!";
        if (lvlrank == 1)
            return "3-34";
        if (lvlrank == 2)
            return "35-64";
        if (lvlrank == 3)
            return "65-94";
        if (lvlrank == 4)
            return "95-123";
        return "error? fail";
    }

    public void handleFlagScore(Player p, GameObject obj) {
        if (isGameLocked())
            return;
        if ((redTeam.contains(p) && obj.getID() == BLUE_FLAG_ID) || (blueTeam.contains(p) && obj.getID() == RED_FLAG_ID)) {
            p.getActionSender().sendMessage("You must steal this flag, then capture it on your own flag");
            return;
        }
        if (((redTeam.contains(p) && obj.getID() == RED_FLAG_ID) || (blueTeam.contains(p) & obj.getID() == BLUE_FLAG_ID)) && !p.isFlagCarrier()) {
            p.getActionSender().sendMessage("You do not have the enemies flag, get it first!");
            return;
        }
        setGameLocked(true);
        p.setFlagCarrier(false);
        p.removeSkull();
        p.getActionSender().sendEquipmentStats();
        if (blueTeam.contains(p)) {
            sendCTFAnnouncement("@blu@Blue Team Scores!");
            sendCTFMessage("@blu@" + p.getUsername() + " @whi@scored 1 point for the @blu@Blue Team");
            p.updateWornItems(BLUE_CARRIER_ITEM.getWieldableDef().getWieldPos(), p.getPlayerAppearance().getSprite(BLUE_CARRIER_ITEM.getWieldableDef().getWieldPos()));
            world.registerGameObject(new GameObject(RED_FLAG, RED_FLAG_ID, 0, 0));
            sendSound("bluescores");
            blueFlagCarrier = null;
            blueScore++;
        } else if (redTeam.contains(p)) {
            sendCTFAnnouncement("@red@Red Team Scores!");
            sendCTFMessage("@red@" + p.getUsername() + " @whi@scored 1 point for the @red@Red Team");
            p.updateWornItems(RED_CARRIER_ITEM.getWieldableDef().getWieldPos(), p.getPlayerAppearance().getSprite(RED_CARRIER_ITEM.getWieldableDef().getWieldPos()));
            world.registerGameObject(new GameObject(BLUE_FLAG, BLUE_FLAG_ID, 0, 0));
            sendSound("redscores");
            redFlagCarrier = null;
            redScore++;
        }
        updateScores();
        if (blueScore >= POINTS_TO_WIN || redScore >= POINTS_TO_WIN) {
            for (Player pl : blueTeam)
                pl.teleport(BLUE_SPAWN.getX(), BLUE_SPAWN.getY(), false);
            for (Player pl : redTeam)
                pl.teleport(RED_SPAWN.getX(), RED_SPAWN.getY(), false);
            doGates(false);

            world.getDelayedEventHandler().add(new MiniEvent(null, 4000) {
                public void action() {
                    if (blueScore >= POINTS_TO_WIN) {
                        sendCTFAnnouncement("@blu@Blue Team Wins!");
                        for (Player pl : blueTeam)
                            sendSound(pl, "youwin");
                    } else {
                        sendCTFAnnouncement("@red@Red Team Wins!");
                        for (Player pl : redTeam)
                            sendSound(pl, "youwin");
                    }
                    world.getDelayedEventHandler().add(new MiniEvent(null, 7500) {
                        public void action() {
                            endCTF();
                        }
                    });
                }
            });
        } else {
            world.getDelayedEventHandler().add(new MiniEvent(null, 3000) {
                public void action() {
                    setGameLocked(false);
                }
            });
        }
    }

    public void handleFlagSteal(Player p, GameObject obj) {
        if (redTeam.contains(p) && obj.getID() == BLUE_FLAG_ID) {
            if (obj.getLocation().equals(BLUE_FLAG)) {
                if (isGameLocked())
                    return;
                if (redFlagCarrier != null) {
                    p.getActionSender().sendMessage("There already is a flag carrier, please report this bug");
                    return;
                }
                redFlagCarrier = p;
                world.unregisterGameObject(obj);
                p.addSkull(60000 * 30);
                p.setFlagCarrier(true);
                p.getActionSender().sendMessage("Your defense has been lowered by 75%");
                sendSound(p, "youhavetheflag");
                p.updateWornItems(BLUE_CARRIER_ITEM.getWieldableDef().getWieldPos(), RED_CARRIER_ITEM.getWieldableDef().getSprite());
                for (Player pl : redTeam) {
                    sendSound(pl, "yourteamhasflag");
                    pl.getActionSender().sendMessage("@red@Your team has the enemy flag!");
                }
                for (Player pl : blueTeam) {
                    sendSound(pl, "enemyhasyourflag");
                    pl.getActionSender().sendMessage("@red@Red team has your flag!");
                }
            }
        } else if (blueTeam.contains(p) && obj.getID() == RED_FLAG_ID) {
            if (obj.getLocation().equals(RED_FLAG)) {
                if (isGameLocked())
                    return;
                if (blueFlagCarrier != null) {
                    p.getActionSender().sendMessage("There already is a flag carrier, please report this bug");
                    return;
                }
                blueFlagCarrier = p;
                world.unregisterGameObject(obj);
                p.addSkull(60000 * 30);
                p.setFlagCarrier(true);
                p.getActionSender().sendMessage("Your defense has been lowered by 75%");
                sendSound(p, "youhavetheflag");
                p.updateWornItems(RED_CARRIER_ITEM.getWieldableDef().getWieldPos(), BLUE_CARRIER_ITEM.getWieldableDef().getSprite());
                for (Player pl : blueTeam) {
                    sendSound(pl, "yourteamhasflag");
                    pl.getActionSender().sendMessage("@red@Your team has the enemy flag!");
                }
                for (Player pl : redTeam) {
                    sendSound(pl, "enemyhasyourflag");
                    pl.getActionSender().sendMessage("@blu@Blue team has your flag!");
                }

            }
        }
    }

    public void handleFlagDrop(Player p) {
        if (blueTeam.contains(p)) {
            if (!p.isFlagCarrier())
                return;
            p.setFlagCarrier(false);
            p.removeSkull();
            blueFlagCarrier = null;
            p.updateWornItems(BLUE_CARRIER_ITEM.getWieldableDef().getWieldPos(), p.getPlayerAppearance().getSprite(BLUE_CARRIER_ITEM.getWieldableDef().getWieldPos()));
            world.registerGameObject(new GameObject(RED_FLAG, RED_FLAG_ID, 0, 0));

            sendSound("redflagreturn");
            sendCTFAnnouncement("@red@     Red flag returned!");

        } else if (redTeam.contains(p)) {
            if (!p.isFlagCarrier())
                return;
            p.setFlagCarrier(false);
            p.removeSkull();
            redFlagCarrier = null;
            p.updateWornItems(RED_CARRIER_ITEM.getWieldableDef().getWieldPos(), p.getPlayerAppearance().getSprite(RED_CARRIER_ITEM.getWieldableDef().getWieldPos()));
            world.registerGameObject(new GameObject(BLUE_FLAG, BLUE_FLAG_ID, 0, 0));
            sendSound("redflagreturn");
            sendCTFAnnouncement("@blu@     Blue flag returned!");
        }
    }

    public void handlePlayerJoin(Player p) {
        if (!isEventRunning) {
            p.getActionSender().sendMessage("Sorry, CaptureTheFlag isn't running at the moment!");
            return;
        }
        if (p.isMod() || p.isPMod()) {
            p.getActionSender().sendMessage("Please use a regular account to join the CTF");
            return;
        }
        if (redTeam.contains(p) || blueTeam.contains(p)) {
            p.getActionSender().sendMessage("You are already apart of a team!");
            return;
        }
        if (!isLevelValid(p.getCombatLevel())) {
            p.getActionSender().sendMessage("Sorry, you are not in the correct level rank for CaptureTheFlag");
            p.getActionSender().sendMessage("Currently levels " + getLevelString(currentLevelRank) + " are allowed");
            return;
        }
        boolean cont = true;
        for (InvItem i : p.getInventory().getItems()) {
            for (int in = 0; in < FREE_ITEMS.length; in++) {
                if (i.getID() == FREE_ITEMS[in]) {
                    p.getActionSender().sendMessage("You may not bring " + i.getDef().name + " to CaptureTheFlag");
                    cont = false;
                }
            }
            for (int in = 0; in < DISALLOWED_ITEMS.length; in++) {
                if (i.getID() == DISALLOWED_ITEMS[in]) {
                    p.getActionSender().sendMessage("You may not bring " + i.getDef().name + " to CaptureTheFlag");
                    cont = false;
                }
            }
        }
        if (!cont)
            return;
        if (redTeam.size() > blueTeam.size()) {
            blueTeam.add(p);
            blueTeamPlayernames.add(p.getUsername());
            p.getActionSender().sendMessage("@blu@You have joined the blue team!");
            p.teleportCTF(BLUE_SPAWN.getX(), BLUE_SPAWN.getY(), false);
        } else {
            redTeam.add(p);
            redTeamPlayernames.add(p.getUsername());
            p.getActionSender().sendMessage("You have joined the red team!");
            p.teleportCTF(RED_SPAWN.getX(), RED_SPAWN.getY(), false);
        }
        handlePlayerClothing(p);
        if (!checkAndStart())
            world.getDelayedEventHandler().add(new MiniEvent(p, 4000) {
                public void action() {
                    if (!matchRunning)
                        owner.getActionSender().sendMessage("&CaptureTheFlag Will start when Both teams have " + PLAYERS_NEEDED + " players ready");
                }
            });

        p.getActionSender().sendMessage("Use ::leave when you want to leave.");
        p.getActionSender().sendMessage("Use ::leave when you want to leave.");
        updateScores();
    }

    public boolean checkAndStart() {
        if (redTeam.size() >= PLAYERS_NEEDED && blueTeam.size() >= PLAYERS_NEEDED) {
            if (warmupRunning && !matchRunning) {
                startMatchCTF();
            }
            return true;
        }
        return false;
    }

    public void handlePlayerLeave(Player p, boolean remove) {
        if (!inCTF(p))
            return;

        handleFlagDrop(p);

        p.teleportCTF(100, 500, false);

        if (redTeam.contains(p)) {
            p.updateWornItems(RED_GEAR_CHEST.getWieldableDef().getWieldPos(), p.getPlayerAppearance().getSprite(RED_GEAR_CHEST.getWieldableDef().getWieldPos()));
            p.updateWornItems(RED_GEAR_LEG.getWieldableDef().getWieldPos(), p.getPlayerAppearance().getSprite(RED_GEAR_LEG.getWieldableDef().getWieldPos()));
            if (remove)
                redTeam.remove(p);
        } else {
            p.updateWornItems(BLUE_GEAR_CHEST.getWieldableDef().getWieldPos(), p.getPlayerAppearance().getSprite(BLUE_GEAR_CHEST.getWieldableDef().getWieldPos()));
            p.updateWornItems(BLUE_GEAR_LEG.getWieldableDef().getWieldPos(), p.getPlayerAppearance().getSprite(BLUE_GEAR_LEG.getWieldableDef().getWieldPos()));
            if (remove)
                blueTeam.remove(p);
        }

        for (int i : FREE_ITEMS) {
            int count = p.getInventory().countId(i);
            InvItem item = new InvItem(i, count);
            if (count > 0) {
                if (item.getDef().stackable) {
                    p.getInventory().remove(item.getID(), item.getAmount());
                } else {
                    for (int in = 0; in < count; in++) {
                        p.getInventory().remove(item.getID(), 1);
                    }
                }
            }
        }

        p.getActionSender().sendInventory();
        p.getActionSender().sendMessage("All your free items have been removed, thanks for playing");

        for (InvItem i : p.getInventory().getItems()) {
            if (i.isWielded())
                p.updateWornItems(i.getWieldableDef().getWieldPos(), i.getWieldableDef().getSprite());
        }

        redTeamPlayernames.remove(p.getUsername());
        blueTeamPlayernames.remove(p.getUsername());


    }

    public void handlePlayerLogin(Player p) {
        if (!isEventRunning) {
            if (!redTeamPlayernames.contains(p.getUsername()) && !blueTeamPlayernames.contains(p.getUsername())) {
                handlePlayerLeave(p, true);
                return;
            }
            if (isInCaptureTheFlagBoundary(p.getX(), p.getY())) {
                handlePlayerLeave(p, true);
                return;
            }
            return;
        }
        if (!redTeamPlayernames.contains(p.getUsername()) && !blueTeamPlayernames.contains(p.getUsername()) && isInCaptureTheFlagBoundary(p.getX(), p.getY())) {
            handlePlayerLeave(p, true);
            return;
        }
        if (!CTF.isInCaptureTheFlagBoundary(p.getX(), p.getY())) {
            handlePlayerLeave(p, true);
            return;
        }
        if (redTeamPlayernames.contains(p.getUsername())) {
            redTeam.add(p);
            p.getActionSender().sendMessage("Don't run off during a match! If you want to leave type ::leave");
        } else if (blueTeamPlayernames.contains(p.getUsername())) {
            blueTeam.add(p);
            p.getActionSender().sendMessage("Don't run off during a match! If you want to leave type ::leave");
        }
        handlePlayerClothing(p);
    }

    public void handlePlayerLogout(Player p) {
        if (!isEventRunning)
            return;

        handleFlagDrop(p);

        redTeam.remove(p);
        blueTeam.remove(p);

    }

    public void handlePlayerClothing(Player p) {
        if (!isEventRunning)
            return;
        if (blueTeamPlayernames.contains(p.getUsername())) {
            p.updateWornItems(BLUE_GEAR_LEG.getWieldableDef().getWieldPos(), BLUE_GEAR_LEG.getWieldableDef().getSprite());
            p.updateWornItems(BLUE_GEAR_CHEST.getWieldableDef().getWieldPos(), BLUE_GEAR_CHEST.getWieldableDef().getSprite());
        } else if (redTeamPlayernames.contains(p.getUsername())) {
            p.updateWornItems(RED_GEAR_LEG.getWieldableDef().getWieldPos(), RED_GEAR_LEG.getWieldableDef().getSprite());
            p.updateWornItems(RED_GEAR_CHEST.getWieldableDef().getWieldPos(), RED_GEAR_CHEST.getWieldableDef().getSprite());
        }
    }

    private boolean gameLocked = false;

    /**
     * Determines if the game is locked or not (someone captures the flag and everyone should be returned)
     *
     * @return
     */
    public boolean isGameLocked() {
        return gameLocked;
    }

    /**
     * gameLocked setter
     *
     * @param gameLocked
     */
    public void setGameLocked(boolean gameLocked) {
        this.gameLocked = gameLocked;
    }

    public void sendSound(Player p, String soundfile) {
        RSCPacketBuilder pb = new RSCPacketBuilder();
        pb.setID(22);
        pb.addBytes(soundfile.getBytes());
        p.getIoSession().write(pb.toPacket());
    }

    public void sendSound(String soundfile) {
        for (Player p : redTeam) {
            sendSound(p, soundfile);
        }
        for (Player p : blueTeam) {
            sendSound(p, soundfile);
        }
    }

    public void sendCTFMessage(String message) {
        for (Player p : redTeam) {
            p.getActionSender().sendMessage(message);
        }
        for (Player p : blueTeam) {
            p.getActionSender().sendMessage(message);
        }
    }

    public void sendCTFAnnouncement(String message) {
        for (Player p : redTeam) {
            p.getActionSender().sendMessage("&" + message);
        }
        for (Player p : blueTeam) {
            p.getActionSender().sendMessage("&" + message);
        }
    }

    public void updateScores() {
        for (Player p : redTeam)
            updateScore(p);
        for (Player p : blueTeam)
            updateScore(p);
    }

    public void updateScore(Player p) {
        RSCPacketBuilder pkt = new RSCPacketBuilder();
        pkt.setID(1);
        pkt.addByte((byte) blueScore);
        pkt.addByte((byte) redScore);
        p.getIoSession().write(pkt.toPacket());
    }


    public void handleDeath(Player opponent) {
        boolean red = redTeam.contains(opponent);
        if (red) {
            blueKills++;
            redDeaths++;
            blueDeaths = 0;
        } else {
            redKills++;
            redDeaths = 0;
            blueDeaths++;
        }
        if (!firstblood) {
            firstblood = false;
            sendSound("firstblood");
            if (red)
                sendCTFMessage("@red@" + opponent.getUsername() + " @whi@from the @red@Red@whi@ team draws FIRST BLOOD");
            if (!red)
                sendCTFMessage("@blu@" + opponent.getUsername() + " @whi@from the @blu@Blue@whi@ team draws FIRST BLOOD");
        }
        if (blueDeaths == DEATHS_FOR_HUMULIATION || blueDeaths == DEATHS_FOR_HUMULIATION * 2) {

            sendSound("humuliation");
            sendCTFMessage("@blu@Blue@whi@ has had " + blueDeaths + " deaths without a kill");
            sendCTFAnnouncement("     Blue Fails!");
            if (blueDeaths != DEATHS_FOR_HUMULIATION)
                blueDeaths = 0;
        }
        if (redDeaths == DEATHS_FOR_HUMULIATION || redDeaths == DEATHS_FOR_HUMULIATION * 2) {

            sendSound("humuliation");
            sendCTFMessage("@red@Red@whi@ has had " + redDeaths + " deaths without a kill");
            sendCTFAnnouncement("     Red Fails!");
            if (redDeaths != DEATHS_FOR_HUMULIATION)
                redDeaths = 0;
        }
    }

    public void handleKill(Player p) {
        p.killStreak++;
        if (p.killStreak == RAMPAGE_KILLS) {
            sendSound("rampage");
            if (redTeam.contains(p))
                sendCTFMessage("@red@" + p.getUsername() + " @whi@from the @red@Red @whi@team has " + RAMPAGE_KILLS + " kills without dying");
            else if (blueTeam.contains(p))
                sendCTFMessage("@blu@" + p.getUsername() + " @whi@from the @blu@Blue @whi@team has " + RAMPAGE_KILLS + " kills without dying");

        } else if (p.killStreak == UNSTOPPABLE_KILLS) {
            sendSound("unstoppable");
            if (redTeam.contains(p))
                sendCTFMessage("@red@" + p.getUsername() + " @whi@from the @red@Red @whi@team has " + UNSTOPPABLE_KILLS + " kills without dying");
            else if (blueTeam.contains(p))
                sendCTFMessage("@blu@" + p.getUsername() + " @whi@from the @blu@Blue @whi@team has " + UNSTOPPABLE_KILLS + " kills without dying");
        }
    }

    public boolean inCTF(Player p) {
        return redTeam.contains(p) || blueTeam.contains(p) || isInCaptureTheFlagBoundary(p.getX(), p.getY());
    }

    public boolean isRedTeam(Player p) {
        return redTeam.contains(p);
    }

    public boolean isBlueTeam(Player p) {
        return blueTeam.contains(p);
    }

    public boolean isSameTeam(Player p1, Player p2) {
        return (redTeam.contains(p1) && redTeam.contains(p2)) || (blueTeam.contains(p1) && blueTeam.contains(p2));
    }

    public static boolean isInCaptureTheFlagBoundary(int x, int y) {
        return (x > MIN_RADIUS.getX() && y > MIN_RADIUS.getY()) && (x < MAX_RADIUS.getX() && y < MAX_RADIUS.getY());
    }

    public void handleOpenChest(Player p, GameObject obj) {
        if (redTeam.contains(p) && (obj.getX() == BLUE_CHEST.getX() && obj.getY() == BLUE_CHEST.getY())) {
            p.getActionSender().sendMessage("You can not open the other teams chest");
            return;
        } else if (blueTeam.contains(p) && (obj.getX() == RED_CHEST.getX() && obj.getY() == RED_CHEST.getY())) {
            p.getActionSender().sendMessage("You can not open the other teams chest");
            return;
        }
        if (p.isFlagCarrier()) {
            p.getActionSender().sendMessage("You have the flag, you are not allowed to use this");
            return;
        }
        p.getActionSender().sendMessage("You open your teams chest and see goods, take your pick");
        String[] options = new String[]{"Strength Potion", "Food", "Archery Supplies", "Magic Supplies"};
        p.setMenuHandler(new MenuHandler(options) {
            public void handleReply(final int option, final String reply) {
                if (!CTF.isInCaptureTheFlagBoundary(owner.getX(), owner.getY())) {
                    owner.getActionSender().sendMessage("Nice try..");
                    Logger.print(owner.getUsername() + " tried to exploit a nasty CaptureTheFlag (fixed) exploit");
                    return;
                }
                if (option == 0) {
                    owner.getInventory().add(new InvItem(221));
                } else if (option == 1) {
                    while (!owner.getInventory().full()) {
                        owner.getInventory().add(new InvItem(367));
                    }
                } else if (option == 2) {
                    owner.getInventory().add(new InvItem(188));
                    owner.getInventory().add(new InvItem(11, 300));
                } else if (option == 3) {
                    owner.getInventory().add(new InvItem(31, 3000));
                    owner.getInventory().add(new InvItem(32, 3000));
                    owner.getInventory().add(new InvItem(33, 3000));
                    owner.getInventory().add(new InvItem(34, 3000));
                    owner.getInventory().add(new InvItem(35, 3000));
                    owner.getInventory().add(new InvItem(38, 300));
                    owner.getInventory().add(new InvItem(41, 300));
                }
                owner.getActionSender().sendInventory();
            }
        });
        p.getActionSender().sendMenu(options);
    }

    public boolean isLevelValid(int combat) {
        if (currentLevelRank == 0)
            return true;
        if (currentLevelRank == 1 && combat > 2 && combat < 35)
            return true;
        if (currentLevelRank == 2 && combat > 34 && combat < 65)
            return true;
        if (currentLevelRank == 3 && combat > 64 && combat < 96)
            return true;
        if (currentLevelRank == 4 && combat > 94 && combat < 124)
            return true;
        return false;
    }
}


