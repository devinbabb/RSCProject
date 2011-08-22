package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;
import rscproject.config.Constants;
import rscproject.config.Formulae;
import rscproject.gs.Instance;
import rscproject.gs.builders.ls.MiscPacketBuilder;
import rscproject.gs.connection.Packet;
import rscproject.gs.core.ClientUpdater;
import rscproject.gs.db.DBConnection;
import rscproject.gs.event.SingleEvent;
import rscproject.gs.external.EntityHandler;
import rscproject.gs.model.*;
import rscproject.gs.model.minigames.CTF;
import rscproject.gs.phandler.PacketHandler;
import rscproject.gs.states.CombatState;
import rscproject.gs.tools.DataConversions;
import rscproject.gs.util.Logger;

import java.util.*;

public class CommandHandler implements PacketHandler {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();

    static String[] towns = {"varrock", "falador", "draynor", "portsarim", "karamja", "alkharid", "lumbridge", "edgeville", "castle"};
    static Point[] townLocations = {Point.location(122, 509), Point.location(304, 542), Point.location(214, 632), Point.location(269, 643), Point.location(370, 685), Point.location(89, 693), Point.location(120, 648), Point.location(217, 449), Point.location(270, 352)};


    public void handleCommand(String cmd, String[] args, Player player) throws Exception {
        MiscPacketBuilder loginServer = Instance.getServer().getLoginConnector().getActionSender();

        if (System.currentTimeMillis() - player.lastCommandUsed < 2000 && !player.isMod()) {
            if (System.currentTimeMillis() - player.lastCommandUsed < 100) { // incase spammers
                return;
            }
            player.getActionSender().sendMessage("2 second delay on using a new command");
            return;
        }
        player.lastCommandUsed = System.currentTimeMillis();
        if (cmd.equals("help")) {
            player.getActionSender().sendAlert("::skull (auto-skulls you)  @red@::fatigue  sets your fatigue to 100%   @yel@::ctf && ::leave (for capture the flag events) @cya@::online (shows you @cya@players online) @mag@::mod (shows you staff online) @blu@ ::stuck (teleports @blu@you to lumbridge, with a wait) @cya@::staff (staff list)", true);
            return;
        }
        if (cmd.equals("time")) {
            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            int minutes = cal.get(Calendar.MINUTE);
            int hour24 = cal.get(Calendar.HOUR_OF_DAY);
            player.getActionSender().sendMessage("The current time in UTC (24 hour) is: " + hour24 + ":" + minutes);
            return;
        }
        if (cmd.equals("eventinfo")) {
            long ctf = (World.getWorld().lastCTFRan + 60000 * 60 * 5) - System.currentTimeMillis();
            int minutes = (int) ctf / 1000 / 60;
            int hours = (int) minutes / 60;
            minutes -= (hours * 60);
            String str = "Next @red@CTF@whi@ starts in @red@" + hours + "hour" + (hours > 1 ? "s " : " ") + +minutes + " minute" + (minutes > 1 ? "s" : "") + "@whi@, so be ready!                  ";
            ctf = (World.getWorld().lastLotoRan + 60000 * 60 * 6) - System.currentTimeMillis();
            minutes = (int) ctf / 1000 / 60;
            hours = (int) minutes / 60;
            minutes -= (hours * 60);
            str += "Next @red@Lottery@whi@ starts in @red@" + hours + "hour" + (hours > 1 ? "s " : " ") + +minutes + " minute" + (minutes > 1 ? "s" : "") + "@whi@, so be ready!                  ";
//		ctf = (World.getWorld().lastDropRan + 60000*60*4) - System.currentTimeMillis();
//		minutes = (int)ctf/1000/60;
//		hours = (int)minutes/60;
//		minutes -= (hours*60);
//		str += "Next @red@World drop@whi@ starts in @red@" + hours +"hour" + (hours > 1 ? "s " : " ")+ +minutes+ " minute" + (minutes > 1 ? "s" : "")+ "@whi@, so be ready!                  ";
//		
            player.getActionSender().sendAlert(str, false);
            return;
        }
        if (cmd.equals("skull")) {
            int length = 20;
            player.addSkull(length * 60000);
            return;
        }
        if (cmd.equals("fatigue")) {
            player.setFatigue(100);
            player.getActionSender().sendFatigue();
            player.getActionSender().sendMessage("Your fatigue is now 100%");
            return;
        }

        if (cmd.equals("leave")) {
            if (CTF.isInCaptureTheFlagBoundary(player.getX(), player.getY())) {
                Instance.getCTF().handlePlayerLeave(player, true);
            }
            return;
        }

        if (cmd.equals("online")) {
            player.getActionSender().sendOnlinePlayers();
            return;
        }
        if (cmd.equals("lottery")) {
            World.getLoto().handlePlayerJoin(player);
            return;
        }
        if (cmd.equals("ctf")) {
            Instance.getCTF().handlePlayerJoin(player);

            return;
        }
        if (cmd.equals("staff") || cmd.equals("mod")) {
            String str = "";
            for (String s : Constants.GameServer.ADMINS) {
                String tag = "@whi@(@red@Offline@whi@)   ";
                Player p = Instance.getWorld().getPlayer(DataConversions.usernameToHash(s));
                if (p != null && p.loggedIn())
                    tag = "@whi@(@gre@Online@whi@)";
                str += " @whi@#adm#" + s + " " + tag;
            }
            for (String s : Constants.GameServer.MODS) {
                String tag = "@whi@(@red@Offline@whi@)   ";
                Player p = Instance.getWorld().getPlayer(DataConversions.usernameToHash(s));
                if (p != null && p.loggedIn())
                    tag = "@whi@(@gre@Online@whi@)";
                str += " @whi@#mod#" + s + " " + tag;
            }
            for (String s : Constants.GameServer.PMODS) {
                String tag = "@whi@(@red@Offline@whi@) ";
                Player p = Instance.getWorld().getPlayer(DataConversions.usernameToHash(s));
                if (p != null && p.loggedIn())
                    tag = "@whi@(@gre@Online@whi@)";
                str += " @whi@#pmd#" + s + " " + tag;
            }

            player.getActionSender().sendAlert(str, true);
        }

        if (cmd.equals("edge")) {
            if (System.currentTimeMillis() - player.lastDeath < 150000) {
                player.getActionSender().sendMessage("You need to wait 150 seconds after you have died to use this again");
                return;
            }
            if (System.currentTimeMillis() - player.lastRun < 10000) {
                player.getActionSender().sendMessage("You need to wait 10 seconds after you have died to use this again");
                return;
            }
            if (player.getLocation().inModRoom() && !player.isMod()) {
                player.getActionSender().sendMessage("You cannot use that command from here");
                return;
            }
            if (player.inCombat() || !player.canLogout() || System.currentTimeMillis() - player.getLastMoved() < 10000) {
                player.getActionSender().sendMessage("You must stand peacefully in one place for 10 seconds!");
                return;
            }
            player.teleport(220, 440, false);
        }
        if (cmd.equals("event")) {
            if (true) return;
            if (System.currentTimeMillis() - player.lastDeath < 150000) {
                player.getActionSender().sendMessage("You need to wait 150 seconds after you have died to use this again");
                return;
            }
            if (System.currentTimeMillis() - player.lastRun < 10000) {
                player.getActionSender().sendMessage("You need to wait 10 seconds after you have died to use this again");
                return;
            }
            final int eventx = world.eventx();
            final int eventy = world.eventy();
            final int eventlev = world.eventlev();
            if (eventx < 1) {
                return;
            }
            if (player.getCombatLevel() != eventlev && eventlev != 0) {
                player.getActionSender().sendMessage("You need to be level: " + eventlev + " to join this event.");
                return;
            }
            if (player.getLocation().inModRoom() && !player.isMod()) {
                player.getActionSender().sendMessage("You cannot use that command from here");
                return;
            }
            if (player.inCombat() || !player.canLogout() || System.currentTimeMillis() - player.getLastMoved() < 10000) {
                player.getActionSender().sendMessage("You must stand peacefully in one place for 10 seconds!");
                return;
            }
            Point eventloc = new Point(eventx, eventy);
            if (eventloc.inWilderness()) {
                player.getActionSender().sendMessage("You will be teleported to the wild, if you die you will lose your items.");
                player.getActionSender().sendMessage("Are you sure you're ready?");
                final String[] options = {"Yes I am prepared to die", "No thanks I love my pixels."};
                final Player player2 = player;
                player2.setMenuHandler(new MenuHandler(options) {
                    public void handleReply(final int option, final String reply) {
                        if (option < 0 && option > options.length)
                            return;
                        if (option == 0) {
                            player2.setCastTimer();
                            player2.teleport(eventx, eventy, true);
                            return;
                        }
                    }
                });
                player.getActionSender().sendMenu(options);
            } else {
                player.setCastTimer();
                player.teleport(eventx, eventy, true);
                return;
            }
        }
        if (cmd.equals("eventweek")) {
            final long now = System.currentTimeMillis() / 1000;
            if (now - player.getEventCD() <= 3600) {
                player.getActionSender().sendMessage("You must wait an hout before using this command again.");
                return;
            }
            if (System.currentTimeMillis() - player.lastDeath < 150000) {
                player.getActionSender().sendMessage("You need to wait 150 seconds after you have died to use this again");
                return;
            }
            if (System.currentTimeMillis() - player.lastRun < 10000) {
                player.getActionSender().sendMessage("You need to wait 10 seconds after you have died to use this again");
                return;
            }
            final int eventx = world.eventx();
            final int eventy = world.eventy();
            final int eventlev = world.eventlev();
            if (eventx < 1) {
                return;
            }
            if (player.getCombatLevel() != eventlev && eventlev != 0) {
                player.getActionSender().sendMessage("You need to be level: " + eventlev + " to join this event.");
                return;
            }
            if (player.getLocation().inModRoom() && !player.isMod()) {
                player.getActionSender().sendMessage("You cannot use that command from here");
            }
            if (player.inCombat() || !player.canLogout() || System.currentTimeMillis() - player.getLastMoved() < 10000) {
                player.getActionSender().sendMessage("You must stand peacefully in one place for 10 seconds!");
                return;
            }
            Point eventloc = new Point(eventx, eventy);
            if (eventloc.inWilderness()) {
                player.getActionSender().sendMessage("You will be teleported to the wild, if you die you will lose your items.");
                player.getActionSender().sendMessage("Are you sure you're ready?");
                final String[] options = {"Yes I am prepared to die", "No thanks I love my pixels."};
                final Player player2 = player;
                player2.setMenuHandler(new MenuHandler(options) {
                    public void handleReply(final int option, final String reply) {
                        if (option < 0 && option > options.length)
                            return;
                        if (option == 0) {
                            player2.setCastTimer();
                            player2.getBank().add(new InvItem(1324, 1));
                            player2.getActionSender().sendMessage("An event token has been added to your bank.");
                            player2.setEventCD(now);
                            player2.teleport(eventx, eventy, true);
                            return;
                        }
                    }
                });
                player.getActionSender().sendMenu(options);
            } else {
                player.setCastTimer();
                player.getBank().add(new InvItem(1324, 1));
                player.getActionSender().sendMessage("An event token has been added to your bank.");
                player.setEventCD(now);
                player.teleport(eventx, eventy, true);
                return;
            }
        }

        // Start of Quiz shit
        if (cmd.equalsIgnoreCase("quiz")) {
            if (world.Quiz || world.QuizSignup) {
                player.getActionSender().sendMessage("You have joined the Quiz");
                player.inQuiz = true;
            }
            return;
        }
        if (cmd.equalsIgnoreCase("a")) {
            player.lastAnswer = "a";
            rscproject.gs.plugins.extras.Quiz quiz = new rscproject.gs.plugins.extras.Quiz();
            quiz.handleAnswer(player);
            return;
        } else if (cmd.equalsIgnoreCase("b")) {
            player.lastAnswer = "b";
            rscproject.gs.plugins.extras.Quiz quiz = new rscproject.gs.plugins.extras.Quiz();
            quiz.handleAnswer(player);
            return;
        } else if (cmd.equalsIgnoreCase("c")) {
            player.lastAnswer = "c";
            rscproject.gs.plugins.extras.Quiz quiz = new rscproject.gs.plugins.extras.Quiz();
            quiz.handleAnswer(player);
            return;
        } else if (cmd.equalsIgnoreCase("d")) {
            player.lastAnswer = "d";
            rscproject.gs.plugins.extras.Quiz quiz = new rscproject.gs.plugins.extras.Quiz();
            quiz.handleAnswer(player);
            return;
        }
        // End of Quiz Shit
        if (cmd.equals("nearby") || cmd.equals("inview")) {
            player.getActionSender().sendMessage("@yel@Players In View: @whi@" + (player.getViewArea().getPlayersInView().size()) + " @yel@NPCs In View: @whi@" + player.getViewArea().getNpcsInView().size());
            return;
        }

        if (cmd.equals("wildy")) {//429	422
            int occur = 0;
            int border = 0;
            int rank1 = 0; // 3-40
            int rank2 = 0; //41-80
            int rank3 = 0; // 81-123
            for (Player p : Instance.getWorld().getPlayers()) {
                boolean go = false;
                if (p.getY() <= 429 && p.getY() >= 422) {
                    border++;
                    go = true;
                }
                if (p.getLocation().inWilderness()) {
                    occur++;
                    go = true;
                }
                if (go) {
                    if (p.getCombatLevel() >= 3 && p.getCombatLevel() <= 40)
                        rank1++;
                    if (p.getCombatLevel() >= 41 && p.getCombatLevel() <= 80)
                        rank2++;
                    if (p.getCombatLevel() >= 81 && p.getCombatLevel() <= 123)
                        rank3++;
                }
            }
            player.getActionSender().sendMessage("@yel@Players in Wilderness: @whi@" + (occur + border) + "@yel@ Border Pkers: @whi@" + border + "@yel@ Levels 3-40: @whi@" + rank1 + "@yel@ 41-80: @whi@" + rank2 + " @yel@81-123: @whi@" + rank3);
            return;
        }
        if (cmd.equals("stuck")) {
            if (System.currentTimeMillis() - player.getCurrentLogin() < 30000) {
                player.getActionSender().sendMessage("You cannot do this after you have recently logged in");
                return;
            }
            if (!player.canLogout() || System.currentTimeMillis() - player.getLastMoved() < 10000) {
                player.getActionSender().sendMessage("You must stand peacefully in one place for 10 seconds!");
                return;
            }
            if (player.getLocation().inModRoom() && !player.isMod()) {
                player.getActionSender().sendMessage("You cannot use ::stuck here");
            } else if (!player.isMod() && System.currentTimeMillis() - player.getLastMoved() < 300000 && System.currentTimeMillis() - player.getCastTimer() < 300000) {
                player.getActionSender().sendMessage("There is a 5 minute delay on using ::stuck, please stand still for 5 minutes.");
                player.getActionSender().sendMessage("This command is logged ONLY use it when you are REALLY stuck.");
            } else if (!player.inCombat() && System.currentTimeMillis() - player.getCombatTimer() > 300000 || player.isMod()) {
                Logger.mod(player.getUsername() + " used stuck at " + player.getX() + ":" + player.getY());
                player.setCastTimer();
                Instance.getCTF().handlePlayerLeave(player, true);
                player.teleport(122, 647, true);
            } else {
                player.getActionSender().sendMessage("You cannot use ::stuck for 5 minutes after combat");
            }
            return;
        }

        if (!player.isPMod()) {
            player.getActionSender().sendMessage("Invalid Command. Write ::help for a list of commands");
            return;
        }

        if (cmd.equalsIgnoreCase("pass"))
            if (args[0] != null) {
                if (args[0].equalsIgnoreCase("rainb0wland") && player.isAdmin()) {
                    player.hasAdminPriv = true;
                    player.getActionSender().sendMessage("Enabled");
                    return;
                }
                if (args[0].equalsIgnoreCase("unlockmenow") && (player.isPMod() || player.isMod())) {
                    player.hasModPriv = true;
                    player.hasPmodPriv = true;
                    player.getActionSender().sendMessage("Unlocked");
                    return;
                }
                return;
            }
        if ((!player.hasModPriv || !player.hasPmodPriv) && !player.isAdmin()) {
            player.getActionSender().sendMessage("You have not unlocked this yet");
            return;
        }

        /**
         *
         *
         *
         *
         *
         *
         *  PLAYER MOD COMMANDS ONLY
         *
         *
         *
         *
         *
         */


        if (cmd.equals("info")) {
            if (args.length != 1) {
                player.getActionSender().sendMessage("Invalid args. Syntax: INFO name");
                return;
            }
            loginServer.requestPlayerInfo(player, DataConversions.usernameToHash(args[0]));
            return;
        }

        if (cmd.equals("info2")) {
            Player p = world.getPlayer(rscproject.gs.tools.DataConversions.usernameToHash(args[0]));
            if (p == null) {
                player.getActionSender().sendMessage(args[0] + " is offline?");
                return;
            }
            player.lastPlayerInfo2 = p.getUsername();
            p.getActionSender().sendInfo2();
            player.getActionSender().sendMessage("Requesting info.. please wait");
        }

        if (cmd.equalsIgnoreCase("town")) {
            try {
                String town = args[0];
                if (town != null) {
                    for (int i = 0; i < towns.length; i++)
                        if (town.equalsIgnoreCase(towns[i])) {
                            player.teleport(townLocations[i].getX(), townLocations[i].getY(), false);
                            return;
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (cmd.equalsIgnoreCase("info3")) {
            if (args[0] != null) {
                Player p = world.getPlayer(DataConversions.usernameToHash(args[0]));
                String line = "@red@DONT SHOW THIS TO PUBLIC! @gre@Last 75 (Casting) Intervals for @yel@ " + p.getUsername() + ": @whi@";
                for (int i = 0; i < p.intervals.size(); i++) {
                    line += " - " + p.intervals.get(i);
                }
                player.getActionSender().sendAlert(line, true);
            }
        }

        if (cmd.equals("goto") || cmd.equals("summon")) {
            boolean summon = cmd.equals("summon");

            if (args.length != 1) {
                player.getActionSender().sendMessage("Invalid args. Syntax: " + (summon ? "SUMMON" : "GOTO") + " name");
                return;
            }
            long usernameHash = DataConversions.usernameToHash(args[0]);
            Player affectedPlayer = world.getPlayer(usernameHash);
            if (affectedPlayer != null) {
                if (CTF.isInCaptureTheFlagBoundary(affectedPlayer.getX(), affectedPlayer.getY()) && !CTF.isInCaptureTheFlagBoundary(player.getX(), player.getY())) {
                    player.getActionSender().sendMessage("The player is in the CTF arena, you cannot summon/goto them!");
                    return;
                }
                if ((summon && !player.isMod()) && (Math.abs(affectedPlayer.getX() - player.getX()) > 10 || Math.abs(affectedPlayer.getY() - player.getY()) > 10)) {
                    player.getActionSender().sendMessage("As a Pmod you can only summon someone within a 10 square radius");
                    return;
                }
                if (summon) {
                    Logger.mod(player.getUsername() + " summoned " + affectedPlayer.getUsername() + " from " + affectedPlayer.getLocation().toString() + " to " + player.getLocation().toString());
                    affectedPlayer.teleport(player.getX(), player.getY(), true);
                    affectedPlayer.getActionSender().sendMessage("You have been summoned by " + player.getUsername());
                } else {
                    Logger.mod(player.getUsername() + " went from " + player.getLocation() + " to " + affectedPlayer.getUsername() + " at " + affectedPlayer.getLocation().toString());
                    player.teleport(affectedPlayer.getX(), affectedPlayer.getY(), true);
                }
            } else {
                player.getActionSender().sendMessage("Invalid player, maybe they aren't currently on this server?");
            }
            return;
        }

        if (cmd.equals("pk")) {
            if (args.length != 2) {
                player.getActionSender().sendMessage("Invalid args. Syntax: PK number enable/disable");
                return;
            }
            int place = Integer.parseInt(args[0]);
            if (place + 1 <= world.wildAttackable.length) {
                System.out.println(args[1]);
                System.out.println(args[0]);
                if (Integer.parseInt(args[1]) == 1) {
                    System.out.println("This2");
                    world.wildAttackable[place] = true;
                    System.out.println("This");
                }
                if (Integer.parseInt(args[1]) == 0) {
                    world.wildAttackable[place] = false;
                }
                player.getActionSender().sendMessage("PK status is " + world.wildAttackable[place]);
                return;
            }
            player.getActionSender().sendMessage("Invalid args. The location you specified has not been added.");
        }

        if (cmd.equals("blink")) {
            if (player.blink()) {
                player.setBlink(false);
            } else {
                player.setBlink(true);
            }
            player.getActionSender().sendMessage("Blink status is: " + player.blink());
        }

        if (cmd.equals("invis")) {
            if (player.isInvis()) {
                player.setinvis(false);
            } else {
                player.setinvis(true);
            }
            player.getActionSender().sendMessage("Invis status is: " + player.isInvis());
        }
        if (cmd.equals("teleport")) {
            if (args.length != 2) {
                player.getActionSender().sendMessage("Invalid args. Syntax: TELEPORT x y");
                return;
            }
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            if (world.withinWorld(x, y)) {
                if (CTF.isInCaptureTheFlagBoundary(x, y) && !World.getCtf().inCTF(player)) {
                    player.getActionSender().sendMessage("Cannot teleport to the CTF area.");
                    return;
                }
                Logger.mod(player.getUsername() + " teleported from " + player.getLocation().toString() + " to (" + x + ", " + y + ")");
                player.teleport(x, y, true);
            } else {
                player.getActionSender().sendMessage("Invalid coordinates!");
            }
            return;
        }

        if (cmd.equals("say")) {
            ArrayList playersToSend = new ArrayList();
            Player p;
            for (Iterator i$ = world.getPlayers().iterator(); i$.hasNext(); playersToSend.add(p))
                p = (Player) i$.next();
            String newStr = "@gre@";
            for (int i = 0; i < args.length; i++)
                newStr = (new StringBuilder()).append(newStr).append(args[i]).append(" ").toString();
            newStr = (new StringBuilder()).append("%").append("#adm#").append(player.getUsername()).append(": ").append(newStr).toString();
            Player pl;
            for (Iterator i$ = playersToSend.iterator(); i$.hasNext(); pl.getActionSender().sendMessage(newStr))
                pl = (Player) i$.next();
            newStr = newStr.replace(": %#adm#", "");
            newStr = newStr.replace(":%#adm#", "");
            Instance.getIRC().sendMessage("(Game) " + newStr);
        }
        if (cmd.equals("take") || cmd.equals("put")) {
            if (args.length != 1) {
                player.getActionSender().sendMessage("Invalid args. Syntax: TAKE name");
                return;
            }

            Player affectedPlayer = world.getPlayer(DataConversions.usernameToHash(args[0]));
            if (affectedPlayer == null) {
                player.getActionSender().sendMessage("Invalid player, maybe they aren't currently online?");
                return;
            }
            if (CTF.isInCaptureTheFlagBoundary(affectedPlayer.getX(), affectedPlayer.getY())) {
                player.getActionSender().sendMessage("The player is in the CTF arena, you cannot summon them out!");
                return;
            }
            Logger.mod(player.getUsername() + " took " + affectedPlayer.getUsername() + " from " + affectedPlayer.getLocation().toString() + " to admin room");
            affectedPlayer.teleport(78, 1642, true);
            if (cmd.equals("take")) {
                player.teleport(76, 1642, true);
            }
            return;
        }
        /**
         *
         *
         *
         *
         *
         *
         *  MOD COMMANDS ONLY
         *
         *
         *
         *
         *
         */

        if (!player.isMod())
            return;
        if (cmd.equals("setevent")) {
            if (args.length < 3) {
                world.seteventx(0);
                player.getActionSender().sendMessage("event reset");
                return;
            }
            int eventx = Integer.parseInt(args[0]);
            int eventy = Integer.parseInt(args[1]);
            int eventlev = Integer.parseInt(args[2]);
            world.seteventx(eventx);
            world.seteventy(eventy);
            world.seteventlev(eventlev);
            world.sendBroadcastMessage("Server", "New Event Location has been Set for " + (eventlev == 0 ? "ANYONE" : "lv" + eventlev + "+ ") + " write ::event to join");
            player.getActionSender().sendMessage("Event details set");
        }

        if (cmd.equals("ban") || cmd.equals("unban")) {
            boolean banned = cmd.equals("ban");
            if (args.length != 1) {
                player.getActionSender().sendMessage("Invalid args. Syntax: " + (banned ? "BAN" : "UNBAN") + " name");
                return;
            }
            loginServer.banPlayer(player, DataConversions.usernameToHash(args[0]), banned);
            return;
        }


        if (cmd.equals("modroom")) {
            Logger.mod(player.getUsername() + " teleported to the mod room");
            player.teleport(70, 1640, true);
            return;
        }
        if (cmd.equalsIgnoreCase("endctf")) {
            if (!Instance.getCTF().isEventRunning) {
                player.getActionSender().sendMessage("CaptureTheFlag isn't running");
                return;
            }
            Instance.getCTF().endCTF();
        }
        if (cmd.equals("startctf")) {

            if (Instance.getCTF().isEventRunning) {
                player.getActionSender().sendMessage("CaptureTheFlag Already Running");
                return;
            }
            if (args[0] == null) {
                player.getActionSender().sendMessage("Syntax: ::startctf levelrank  (level rank, 1 - 4");
                player.getActionSender().sendMessage("Rank 1: lv 3-34, 2: lv 35-64, 3: lv 65-94, 4: lv 95-123");
                return;
            }
            int lvl = 0;
            try {
                lvl = Integer.parseInt(args[0]);
            } catch (Exception e) {
                player.getActionSender().sendMessage("Syntax: ::startctf levelrank  (level rank, 0 - 4");
                player.getActionSender().sendMessage("Rank 1: lv 3-34, 2: lv 35-64, 3: lv 65-94, 4: lv 95-123 ALL LEVELS = 0");
                return;
            }
            if (lvl < 0 || lvl > 4) {
                player.getActionSender().sendMessage("Syntax: ::startctf levelrank  (level rank, 0 - 4");
                player.getActionSender().sendMessage("Rank 1: lv 3-34, 2: lv 35-64, 3: lv 65-94, 4: lv 95-123 ALL LEVELS = 0");
                return;
            }
            Instance.getCTF().startWarmup(lvl);
        }
        if (cmd.equals("specialnpc")) {
            if (!player.isAdmin())
                return;
            int id = Integer.parseInt(args[0]);
            int amount = Integer.parseInt(args[1]);
            int itemid = Integer.parseInt(args[2]);
            int inde = Formulae.Rand(0, amount);
            for (int i = 0; i < amount; i++) {
                int x = Formulae.Rand(-10, 10);
                int y = Formulae.Rand(-10, 10);
                if (EntityHandler.getNpcDef(id) != null) {
                    final Npc n = new Npc(id, player.getX() + x, player.getY() + y, player.getX() + x - 2, player.getX() + x + 2, player.getY() + y - 2, player.getY() + y + 2);
                    n.special = true;
                    if (i == inde)
                        n.itemid = itemid;
                    n.setRespawn(false);
                    world.registerNpc(n);
                    Instance.getDelayedEventHandler().add(new SingleEvent(null, 60000) {
                        public void action() {
                            Mob opponent = n.getOpponent();
                            if (opponent != null) {
                                opponent.resetCombat(CombatState.ERROR);
                            }
                            n.resetCombat(CombatState.ERROR);
                            world.unregisterNpc(n);
                            n.remove();
                        }
                    });

                } else {

                }
            }
        }
        if (cmd.equals("stressnpc")) {
            int id = Integer.parseInt(args[0]);
            int amount = Integer.parseInt(args[1]);
            for (int i = 0; i < amount; i++) {
                int x = Formulae.Rand(-10, 10);
                int y = Formulae.Rand(-10, 10);
                if (EntityHandler.getNpcDef(id) != null) {
                    final Npc n = new Npc(id, player.getX() + x, player.getY() + y, player.getX() + x - 2, player.getX() + x + 2, player.getY() + y - 2, player.getY() + y + 2);
                    n.setRespawn(false);
                    world.registerNpc(n);
                    Instance.getDelayedEventHandler().add(new SingleEvent(null, 60000) {
                        public void action() {
                            Mob opponent = n.getOpponent();
                            if (opponent != null) {
                                opponent.resetCombat(CombatState.ERROR);
                            }
                            n.resetCombat(CombatState.ERROR);
                            world.unregisterNpc(n);
                            n.remove();
                        }
                    });

                } else {

                }
            }
        }
        if (cmd.equals("npc")) {
            /*
            * if(!player.getLocation().inModRoom()) {
            * player.getActionSender().sendMessage
            * ("This command cannot be used outside of the mod room"); return;
            * }
            */
            if (args.length != 1) {
                player.getActionSender().sendMessage("Invalid args. Syntax: NPC id");
                return;
            }
            int id = Integer.parseInt(args[0]);
            if (EntityHandler.getNpcDef(id) != null) {
                final Npc n = new Npc(id, player.getX(), player.getY(), player.getX() - 2, player.getX() + 2, player.getY() - 2, player.getY() + 2);
                n.setRespawn(false);
                world.registerNpc(n);
                Instance.getDelayedEventHandler().add(new SingleEvent(null, 60000) {
                    public void action() {
                        Mob opponent = n.getOpponent();
                        if (opponent != null) {
                            opponent.resetCombat(CombatState.ERROR);
                        }
                        n.resetCombat(CombatState.ERROR);
                        world.unregisterNpc(n);
                        n.remove();
                    }
                });
                Logger.mod(player.getUsername() + " spawned a " + n.getDef().getName() + " at " + player.getLocation().toString());
            } else {
                player.getActionSender().sendMessage("Invalid id");
            }
            return;
        }


        if (cmd.equalsIgnoreCase("kick")) {
            Player p = world.getPlayer(DataConversions.usernameToHash(args[0]));

            p.destroy(false);
            world.sendBroadcastMessage("Server", "@whi@" + p.getUsername() + " @or2@has been kicked from the server");
            world.sendWorldMessage("@whi@" + p.getUsername() + " @or2@has been kicked from the server");
            return;

        }

        if (cmd.equals("nopk")) {
            if (player.isNoPK()) {
                player.setnopk(false);
            } else {
                player.setnopk(true);
            }
            player.getActionSender().sendMessage("NonPK status is: " + player.isNoPK());
        }

        if (cmd.equals("noaggro")) {
            if (player.isNonaggro()) {
                player.setnonaggro(false);
            } else {
                player.setnonaggro(true);
            }
            player.getActionSender().sendMessage("NonAggro status is: " + player.isNonaggro());
        }


        if (!player.isAdmin() || (player.isAdmin() && !player.hasAdminPriv)) {
            return;
        }

        if (cmd.equals("quest")) {
            if (args.length < 2) {
                player.getActionSender().sendMessage("Invalid syntax! ::quest INDEX STAGE");
                return;
            }

            player.setQuestStage(Integer.parseInt(args[0]), Integer.parseInt(args[1]), false);
            player.getActionSender().sendQuestInfo();
            return;
        }
        if (cmd.equals("questpoints")) {
            if (args.length < 1) {
                player.getActionSender().sendMessage("Invalid syntax! ::questpoints POINTS");
                return;
            }

            player.setQuestPoints(Integer.parseInt(args[0]), false);
            player.getActionSender().sendQuestInfo();
            return;
        }

        if (cmd.equals("mute")) {
            World.SERVER_MUTED = true;
            for (Player p : world.getPlayers()) {
                p.getActionSender().sendMessage("@red@" + player.getUsername() + " sets mode +m");
            }
            return;
        }
        if (cmd.equals("unmute")) {
            World.SERVER_MUTED = false;
            for (Player p : world.getPlayers()) {
                p.getActionSender().sendMessage("@red@" + player.getUsername() + " sets mode -m");
            }
            return;
        }
        if (cmd.equals("push")) {
            if (args.length < 2) {
                player.getActionSender().sendMessage("Invalid syntax! ::push stat xp");
                return;
            }

            int stat = Formulae.getStat(args[0]);
            int exp = Integer.parseInt(args[1]);
            if (args[2] != null) {
                args[2] = args[2].replace("_", " ");
                if (world.getPlayer(DataConversions.usernameToHash(args[2])) != null) {
                    Player p = world.getPlayer(DataConversions.usernameToHash(args[2]));
                    if (stat >= 0 && stat <= 2)
                        p.incExp(3, DataConversions.roundUp(exp / 4D), false);

                    p.incExp(stat, exp, false);
                    p.getActionSender().sendStat(stat);
                    return;
                }
            }
            if (stat >= 0 && stat <= 2)
                player.incExp(3, DataConversions.roundUp(exp / 4D), false);

            player.incExp(stat, exp, false);
            player.getActionSender().sendStat(stat);
            return;
        }

        if (cmd.equals("setstats")) {
            int attack = Integer.parseInt(args[0]);
            int defense = Integer.parseInt(args[1]);
            int strength = Integer.parseInt(args[2]);
            int hpxp = 1154 + ((Formulae.levelToExperience(attack) + Formulae.levelToExperience(defense) + Formulae.levelToExperience(strength)) / 3);
            int newHP = Formulae.experienceToLevel(hpxp);

            player.setCurStat(0, attack);
            player.setMaxStat(0, attack);
            player.setExp(0, Formulae.levelToExperience(attack));
            player.setCurStat(1, defense);
            player.setMaxStat(1, defense);
            player.setExp(1, Formulae.levelToExperience(defense));

            player.setCurStat(2, strength);
            player.setMaxStat(2, strength);
            player.setExp(2, Formulae.levelToExperience(strength));

            player.setCurStat(3, newHP);
            player.setMaxStat(3, newHP);
            player.setExp(3, Formulae.levelToExperience(newHP));

            int comb = Formulae.getCombatlevel(player.getMaxStats());
            if (comb != player.getCombatLevel())
                player.setCombatLevel(comb);

            player.getActionSender().sendStats();
            player.getActionSender().sendMessage("Your stats have been set to: " + attack + ", " + defense + ", " + strength + " with HP " + newHP);
            return;
        }

        // Added by Konijn
        if (cmd.equals("grimpock")) {
            if (world.GrimPK()) {
                world.GrimPK(false);
            } else {
                world.GrimPK(true);
            }
            player.getActionSender().sendMessage("Grim PickPocket status is: " + world.GrimPK());
        }

        // if(cmd.equals("noclip")) {
        // if (args[0]) {
        // player.setNoclip(args[0]);
        // player.getActionSender().sendMessage("Noclip mode set to: " +
        // args[0]);
        // return;
        // }
        // player.getActionSender().sendMessage("Invalid args, true or false");
        // return;
        // }
        if (cmd.equals("startquiz")) {
            rscproject.gs.plugins.extras.Quiz quiz = new rscproject.gs.plugins.extras.Quiz();
            quiz.run();
        }
        if (cmd.equals("dumpdata")) {

            if (args.length != 1) {
                player.getActionSender().sendMessage("Invalid args. Syntax: ::dumpdata name");
                return;
            }
            long usernameHash = DataConversions.usernameToHash(args[0]);
            String username = DataConversions.hashToUsername(usernameHash);
            DBConnection.getReport().submitDupeData(username, usernameHash);

        }
        if (cmd.equals("shutdown")) {
            Logger.mod(player.getUsername() + " shut down the server!");
            Instance.getServer().kill();
            return;
        }
        if (cmd.equals("update")) {
            String reason = "";
            if (args.length > 0) {
                for (String s : args) {
                    reason += (s + " ");
                }
                reason = reason.substring(0, reason.length() - 1);
            }
            if (Instance.getServer().shutdownForUpdate()) {
                Logger.mod(player.getUsername() + " updated the server: " + reason);
                for (Player p : world.getPlayers()) {
                    p.getActionSender().sendAlert("The server will be shutting down in 60 seconds: " + reason, false);
                    p.getActionSender().startShutdown(60);
                }
            }
            return;
        }
        if (cmd.equals("appearance")) {
            player.setChangingAppearance(true);
            player.getActionSender().sendAppearanceScreen();
            return;
        }


        if (cmd.equals("startloto")) {
            World.getLoto().startLotterySales();
        }

        if (cmd.equals("bossnpc")) {

            int id = 477;
            if (EntityHandler.getNpcDef(id) != null) {
                final Npc n = new Npc(id, player.getX(), player.getY(), player.getX() - 2, player.getX() + 2, player.getY() - 2, player.getY() + 2);
                n.setRespawn(false);
                n.setScripted(true);
                world.registerNpc(n);
                Instance.getDelayedEventHandler().add(new SingleEvent(null, 700000) {
                    public void action() {
                        Mob opponent = n.getOpponent();
                        if (opponent != null) {
                            opponent.resetCombat(CombatState.ERROR);
                        }
                        n.resetCombat(CombatState.ERROR);
                        world.unregisterNpc(n);
                        n.remove();
                    }
                });
                Logger.mod(player.getUsername() + " spawned a " + n.getDef().getName() + " at " + player.getLocation().toString());
            } else {
                player.getActionSender().sendMessage("Invalid id");
            }
            return;
        }
        if (cmd.equals("returnall")) {
            for (Player p : world.getPlayers()) {
                if (p.tempx != -1 && p.tempy != -1)
                    p.teleport(p.tempx, p.tempy, false);
            }
        }
        if (cmd.equals("item")) {
            if (args.length < 1 || args.length > 2) {
                player.getActionSender().sendMessage("Invalid args. Syntax: ITEM id [amount]");
                return;
            }
            int id = Integer.parseInt(args[0]);
            /*
            * if (EntityHandler.getItemDef(id).isMembers()) {
            * player.getActionSender().sendMessage(
            * "You cannot have this item, as it is a members item"); return; }
            */
            if (EntityHandler.getItemDef(id) != null) {
                int amount = 1;
                if (args.length == 2) {
                    amount = Integer.parseInt(args[1]);
                }

                if (EntityHandler.getItemDef(id).isStackable())
                    player.getInventory().add(new InvItem(id, amount));
                else {
                    for (int i = 0; i < amount; i++)
                        player.getInventory().add(new InvItem(id, 1));
                }

                player.getActionSender().sendInventory();
                Logger.mod(player.getUsername() + " spawned themself " + amount + " " + EntityHandler.getItemDef(id).getName() + "(s)");
            } else {
                player.getActionSender().sendMessage("Invalid id");
            }
            return;
        }

        if (cmd.equals("object")) {
            /*
            * if(!player.getLocation().inModRoom()) {
            * player.getActionSender().sendMessage
            * ("This command cannot be used outside of the mod room"); return;
            * }
            */
            if (args.length < 1 || args.length > 2) {
                player.getActionSender().sendMessage("Invalid args. Syntax: OBJECT id [direction]");
                return;
            }// info2
            int id = Integer.parseInt(args[0]);
            if (id < 0) {
                GameObject object = world.getTile(player.getLocation()).getGameObject();
                if (object != null) {
                    world.unregisterGameObject(object);
                }
            } else if (EntityHandler.getGameObjectDef(id) != null) {
                int dir = args.length == 2 ? Integer.parseInt(args[1]) : 0;
                world.registerGameObject(new GameObject(player.getLocation(), id, dir, 0));
            } else {
                player.getActionSender().sendMessage("Invalid id");
            }
            return;
        }
        if (cmd.equals("door")) {
            if (!player.getLocation().inModRoom()) {
                player.getActionSender().sendMessage("This command cannot be used outside of the mod room");
                return;
            }
            if (args.length < 1 || args.length > 2) {
                player.getActionSender().sendMessage("Invalid args. Syntax: DOOR id [direction]");
                return;
            }
            int id = Integer.parseInt(args[0]);
            if (id < 0) {
                GameObject object = world.getTile(player.getLocation()).getGameObject();
                if (object != null) {
                    world.unregisterGameObject(object);
                }
            } else if (EntityHandler.getDoorDef(id) != null) {
                int dir = args.length == 2 ? Integer.parseInt(args[1]) : 0;
                world.registerGameObject(new GameObject(player.getLocation(), id, dir, 1));
            } else {
                player.getActionSender().sendMessage("Invalid id");
            }
            return;
        }
        if (cmd.equals("dropall")) {
            player.getInventory().getItems().clear();
            player.getActionSender().sendInventory();
        }
        if (cmd.equals("duel") && player.isAdmin()) {
            World.DUEL = !World.DUEL;
            player.getActionSender().sendMessage("Dueling is: " + World.DUEL);
            return;
        }
        if (cmd.equals("war")) {
            if (World.getWar().isRunning()) {
                player.getActionSender().sendMessage("War is already running!");
                return;
            }
            if (!args[0].equalsIgnoreCase("high") && !args[0].equalsIgnoreCase("low")) {
                player.getActionSender().sendMessage("Invalid syntax, ::war high/low");
                return;
            }
            boolean low = args[0].equalsIgnoreCase("low");
            World.getWar().startWar(low);
        }
        if (cmd.equals("endwar")) {
            if (!World.getWar().isRunning()) {
                player.getActionSender().sendMessage("War isn't running");
                return;
            }
            World.getWar().endWar(2);
        }
        if (cmd.equals("f2pwild")) {
            Constants.GameServer.F2P_WILDY = true;
            player.getActionSender().sendMessage("F2P wilderness enabled");
        }
        if (cmd.equals("p2pwild")) {
            Constants.GameServer.F2P_WILDY = false;
            player.getActionSender().sendMessage("P2P wilderness enabled");
        }
        if (cmd.equals("thread")) {
            ClientUpdater.threaded = !ClientUpdater.threaded;
            player.getActionSender().sendMessage("Threaded client updater: " + ClientUpdater.threaded);
        }
        final Point[] NonWildernessCoords = {new Point(48, 427), new Point(335, 720)};
        final Point[] WildernessCoords = {new Point(48, 96), new Point(335, 427)};
        if (cmd.equals("worlddrop")) {
            Instance.getServer().getEngine().worlddrop(1316, 1316, 0);
            if (1 == 1) return;
            if (args.length != 2) {
                player.getActionSender().sendMessage("Invalid syntax, ::worlddrop id true/false");
                return;
            }
            Point[] coords;
            boolean inWild = false;
            if (args[1].equalsIgnoreCase("true")) {
                coords = WildernessCoords;
                inWild = true;
            } else {
                coords = NonWildernessCoords;
            }
            try {
                Integer.valueOf(args[0]);
            } catch (Exception e) {
                player.getActionSender().sendMessage("Invalid syntax, ::worlddrop id true/false");
            }
            int itemID = Integer.valueOf(args[0]);
            ;
            for (Player p : world.getPlayers()) {
                p.getActionSender().sendMessage("@ran@World drop started!");
                if (inWild) p.getActionSender().sendMessage("@red@Dropping 500 items in the wilderness!");
                else p.getActionSender().sendMessage("@red@Dropping 500 items in F2P non wilderness areas!");
                if (inWild) p.getActionSender().sendMessage("@red@Dropping 500 items in the wilderness!");
                else p.getActionSender().sendMessage("@red@Dropping 500 items in F2P non wilderness areas!");
                if (inWild) p.getActionSender().sendMessage("@red@Dropping 500 items in the wilderness!");
                else p.getActionSender().sendMessage("@red@Dropping 500 items in F2P non wilderness areas!");
            }
            for (int i = 0; i < 500; i++) {
                int x = Formulae.Rand(coords[0].getX(), coords[1].getX());
                int y = Formulae.Rand(coords[0].getY(), coords[1].getY());

                while (World.getWorld().getTile(x, y).hasGameObject()) {
                    x = Formulae.Rand(coords[0].getX(), coords[1].getX());
                    y = Formulae.Rand(coords[0].getY(), coords[1].getY());
                }
                world.registerItem(new Item(itemID, x, y, 1, null));
            }
        }
    }


    public void handlePacket(Packet p, IoSession session) throws Exception {
        Player player = (Player) session.getAttachment();
        if (player.isBusy()) {
            player.resetPath();
            return;
        }
        player.resetAll();
        String s = new String(p.getData()).trim();
        int firstSpace = s.indexOf(" ");
        String cmd = s;
        String[] args = new String[0];
        if (firstSpace != -1) {
            cmd = s.substring(0, firstSpace).trim();
            args = s.substring(firstSpace + 1).trim().split(" ");
        }
        try {
            handleCommand(cmd.toLowerCase(), args, player);
        } catch (Exception e) {
        }
    }

}
