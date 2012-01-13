package rscproject.gs.core;

import org.apache.mina.common.IoSession;
import rscproject.config.Constants;
import rscproject.config.Formulae;
import rscproject.gs.Instance;
import rscproject.gs.connection.PacketQueue;
import rscproject.gs.connection.RSCPacket;
import rscproject.gs.event.DelayedEvent;
import rscproject.gs.event.MiniEvent;
import rscproject.gs.model.*;
import rscproject.gs.model.snapshot.Snapshot;
import rscproject.gs.phandler.PacketHandler;
import rscproject.gs.phandler.PacketHandlerDef;
import rscproject.gs.plugins.dependencies.NpcAI;
import rscproject.gs.tools.Captcha;
import rscproject.gs.tools.DataConversions;
import rscproject.gs.util.Logger;
import rscproject.gs.util.PersistenceManager;

import java.io.*;
import java.util.*;

/**
 * The central motor of the game. This class is responsible for the primary
 * operation of the entire game.
 * 
 * @author Devin
 */
public final class GameEngine extends Thread {

	private static Captcha captcha;
	// sudo
	/**
	 * World instance
	 */
	private static final World world = Instance.getWorld();

	/*
	 * Whether or not the war is currently low or high
	 */
	private boolean wartype = false;

	public static Captcha getCaptcha() {
		return captcha;
	}

	/**
	 * Responsible for updating all connected clients
	 */
	private ClientUpdater clientUpdater = new ClientUpdater();
	/**
	 * Handles delayed events rather than events to be ran every iteration
	 */
	private DelayedEventHandler eventHandler = new DelayedEventHandler();
	/**
	 * When the update loop was last ran, required for throttle
	 */
	private long lastSentClientUpdate = System.currentTimeMillis();
	private long lastSentClientUpdateFast = System.currentTimeMillis();
	private long lastCleanedChatlogs = 0;
	private int lastCleanedChatlogsOutput = 0;
	/**
	 * The mapping of packet IDs to their handler
	 */
	private TreeMap<Integer, PacketHandler> packetHandlers = new TreeMap<Integer, PacketHandler>();
	/**
	 * The packet queue to be processed
	 */
	private PacketQueue<RSCPacket> packetQueue;
	/**
	 * Whether the engine's thread is running
	 */
	private boolean running = true;
	long time = 0;
	/**
	 * Processes incoming packets.
	 */
	private Map<String, Integer> written = Collections
			.synchronizedMap(new HashMap<String, Integer>());

	/**
	 * Constructs a new game engine with an empty packet queue.
	 */
	public GameEngine() {
		captcha = new Captcha();
		captcha.init();
		packetQueue = new PacketQueue<RSCPacket>();
		loadPacketHandlers();
		for (Shop shop : world.getShops()) {
			shop.initRestock();
		}
		redirectSystemStreams();
	}

	public void emptyWorld() {
		for (Player p : world.getPlayers()) {
			p.save();
			p.getActionSender().sendLogout();
		}
		Instance.getServer().getLoginConnector().getActionSender()
				.saveProfiles();
	}

	/**
	 * Ban dummy packet flood private Map<InetAddress, Long> clients; private
	 * Map<InetAddress, Integer> counts; private Map<InetAddress, Integer>
	 * written; public void flagSession(IoSession session) { InetAddress addr =
	 * getAddress(session); String ip = addr.toString(); ip =
	 * ip.replaceAll("/",""); long now = System.currentTimeMillis(); int c = 0;
	 * if(counts.containsKey(addr) && clients.containsKey(addr)) { try { c =
	 * counts.get(addr); } catch(Exception e) { System.out.println("Error: " +
	 * e); } if(c >= 10) { if(!written.containsKey(addr)) { try {
	 * System.out.println("Dummy packet flooder IP: " + ip); BufferedWriter bf2
	 * = new BufferedWriter(new FileWriter("dummy.log", true));
	 * bf2.write("sudo /sbin/route add " + addr.getHostAddress() +
	 * " gw 127.0.0.1"); bf2.newLine(); bf2.close(); written.put(addr, 1); }
	 * catch(Exception e) { System.err.println(e);} } } } if
	 * (clients.containsKey(addr)) { long lastConnTime = clients.get(addr); if
	 * (now - lastConnTime < 2000) { if(!counts.containsKey(addr)) {
	 * counts.put(addr, 0); } else c = counts.get(addr) + 1; counts.put(addr,
	 * c); } else { clients.put(addr, now); } } else { clients.put(addr, now); }
	 * }
	 * 
	 * private InetAddress getAddress(IoSession io) { return
	 * ((InetSocketAddress) io.getRemoteAddress()).getAddress(); }
	 */
	/**
	 * Returns the current packet queue.
	 * 
	 * @return A <code>PacketQueue</code>
	 */
	public PacketQueue<RSCPacket> getPacketQueue() {
		return packetQueue;
	}

	public void kill() {
		Logger.println("Terminating GameEngine");
		running = false;
	}

	/**
	 * Loads the packet handling classes from the persistence manager.
	 */
	protected void loadPacketHandlers() {
		PacketHandlerDef[] handlerDefs = (PacketHandlerDef[]) PersistenceManager
				.load("PacketHandlers.xml");
		for (PacketHandlerDef handlerDef : handlerDefs) {
			try {
				String className = handlerDef.getClassName();
				Class<?> c = Class.forName(className);
				if (c != null) {
					PacketHandler handler = (PacketHandler) c.newInstance();
					for (int packetID : handlerDef.getAssociatedPackets()) {
						packetHandlers.put(packetID, handler);
					}
				}
			} catch (Exception e) {
				Logger.error(e);
			}
		}
	}

	private void processClients() {
		clientUpdater.sendQueuedPackets();
		long now = System.currentTimeMillis();
		if (now - lastSentClientUpdate >= 600) {
			if (now - lastSentClientUpdate >= 1000) {
				// Logger.println("MAJOR UPDATE DELAYED: " + (now -
				// lastSentClientUpdate));
			}
			lastSentClientUpdate = now;
			clientUpdater.doMajor();
		}
		if (now - lastSentClientUpdateFast >= 104) {
			if (now - lastSentClientUpdateFast >= 6000) {
				// Logger.println("MINOR UPDATE DELAYED: " + (now -
				// lastSentClientUpdateFast));
			}
			lastSentClientUpdateFast = now;
			clientUpdater.doMinor();
		}
	}

	private void processEvents() {
		eventHandler.doEvents();
	}

	/**
	 * Redirects system err
	 */
	public static void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				String line = String.valueOf((char) b);
				Logger.systemerr(line);
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				String line = new String(b, off, len);
				Logger.systemerr(line);
			}

			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);
			}
		};
		System.setErr(new PrintStream(out, true));
	}

	private void processIncomingPackets() {
		for (RSCPacket p : packetQueue.getPackets()) {
			IoSession session = p.getSession();
			Player player = (Player) session.getAttachment();
			if (player.getUsername() == null && p.getID() != 32
					&& p.getID() != 77 && p.getID() != 0) {
				final String ip = player.getCurrentIP();
				// flagSession(session);
				if (!written.containsKey(ip)) {
					eventHandler.add(new DelayedEvent(null, 1800000) {

						public void run() {
							written.remove(ip);
							try {
								Runtime.getRuntime().exec(
										"sudo /sbin/route delete " + ip);
							} catch (Exception err) {
								System.out.println(err);
							}
						}
					});
					try {
						// Runtime.getRuntime().exec(
						// "sudo /sbin/route add " + ip + " gw 127.0.0.1");
					} catch (Exception err) {
						System.out.println(err);
					}
					Logger.println("Dummy packet from " + player.getCurrentIP()
							+ ": " + p.getID());
					written.put(ip, 1);
				}
				continue;
			}
			PacketHandler handler = packetHandlers.get(p.getID());
			player.ping();
			if (handler != null) {
				try {
					handler.handlePacket(p, session);
					try {
						if (p.getID() != 5) {
							// String s = "[PACKET] " +
							// session.getRemoteAddress().toString().replace("/",
							// "") + " : " + p.getID()+
							// " ["+handler.getClass().toString()+"]" + " : "+
							// player.getUsername() + " : ";
							// for(Byte b : p.getData())
							// s += b;
							// Logger.println(s);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					String s;
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw, true);
					e.printStackTrace(pw);
					pw.flush();
					sw.flush();
					s = sw.toString();
					Logger.error("Exception with p[" + p.getID() + "] from "
							+ player.getUsername() + " ["
							+ player.getCurrentIP() + "]: " + s);
					player.getActionSender().sendLogout();
					player.destroy(false);
				}
			} else {
				Logger.error("Unhandled packet from " + player.getCurrentIP()
						+ ": " + p.getID() + "len: " + p.getLength());
			}
		}
	}

	public void processLoginServer() {
		LoginConnector connector = Instance.getServer().getLoginConnector();
		if (connector != null) {
			connector.processIncomingPackets();
			connector.sendQueuedPackets();
		}
	}

	/**
	 * The thread execution process.
	 */
	public void run() {
		Logger.println("GameEngine now running");
		// Captcha.loadCharacters();
		for (Npc n : Instance.getWorld().getNpcs()) {
			for (NpcAI ai : Instance.getPluginHandler().getNpcAI()) {
				if (n.getID() == ai.getID()) {
					n.setScripted(true);
				}
			}
		}
		time = System.currentTimeMillis();
		/*
		 * eventHandler.add(new DelayedEvent(null, 5000) { public void run() {
		 * for(Player p : world.getPlayers()) { if(p != null)
		 * p.getSnapshot().releaseActivities(); } }});
		 */
		/*
		 * eventHandler.add(new DelayedEvent(null, 60000*60*12) { // Ran every
		 * 12hours
		 * 
		 * @Override public void run() { int level = -1; while((level =
		 * DataConversions.random(0, 3)) == world.lastCTFLevel);
		 * world.lastCTFLevel = level; World.getCtf().startWarmup(level);
		 * world.lastCTFRan = System.currentTimeMillis(); }} );
		 */
		// eventHandler.add(new DelayedEvent(null, 60000*60*4) { // Ran every 4
		// minutes atm *60*4
		//
		// @Override
		// public void run() {
		//
		// world.lastDropRan = System.currentTimeMillis();
		// worlddrop(1316,1316,0);
		// }}
		// );
		/*
		 * eventHandler.add(new DelayedEvent(null, 60000*60*6) { // Ran every 6
		 * hours
		 * 
		 * @Override public void run() { World.getLoto().startLotterySales();
		 * world.lastLotoRan = System.currentTimeMillis(); }} );
		 */
		eventHandler.add(new DelayedEvent(null, 300000 * 10 * 2) { // Ran every
																	// 50*2
																	// minutes
					@Override
					public void run() {
						new Thread(new Runnable() {
							public void run() {
								garbageCollect();
							}
						}).start();
					}
				});
		eventHandler.add(new DelayedEvent(null, 300000) { // 5 min
					public void run() {
						// Sunday P2P and War
						Calendar cal = new GregorianCalendar(TimeZone
								.getTimeZone("UTC"));
						int day = cal.DAY_OF_WEEK;
						if (day == cal.SUNDAY) {
							Constants.GameServer.F2P_WILDY = false;
							if (!World.getWar().isRunning()) {
								World.getWar().startWar(!wartype);
								wartype = !wartype;
							}
						}
						if (day == cal.MONDAY) {
							Constants.GameServer.F2P_WILDY = true;
							if (World.getWar().isRunning()) {
								World.getWar().endWar(2);
							}
						}
						// MySQL db keep-alive
						world.dbKeepAlive();
						Long now = System.currentTimeMillis();
						for (Player p : world.getPlayers()) {
							if (now - p.getLastSaveTime() >= 900000) {
								p.save();
								p.setLastSaveTime(now);
							} // 15
						}
						Instance.getServer().getLoginConnector()
								.getActionSender().saveProfiles();
					}
				});
		while (running) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException ie) {
			}
			Long AIDS = System.currentTimeMillis();
			Long Delay;
			processLoginServer();
			Delay = System.currentTimeMillis() - AIDS;
			if (Delay >= 1000)
				Logger.println("processLoginServer is taking longer than it should, exactly "
						+ Delay + "ms");
			AIDS = System.currentTimeMillis();
			processIncomingPackets();
			Delay = System.currentTimeMillis() - AIDS;
			if (Delay >= 1000)
				Logger.println("processIncomingPackets is taking longer than it should, exactly "
						+ Delay + "ms");
			AIDS = System.currentTimeMillis();
			processEvents();
			Delay = System.currentTimeMillis() - AIDS;
			if (Delay >= 1000)
				Logger.println("processEvents is taking longer than it should, exactly "
						+ Delay + "ms");
			AIDS = System.currentTimeMillis();
			processClients();
			Delay = System.currentTimeMillis() - AIDS;
			if (Delay >= 1000)
				Logger.println("processClients is taking longer than it should, exactly "
						+ Delay + "ms");
			AIDS = System.currentTimeMillis();
			cleanSnapshotDeque();
			Delay = System.currentTimeMillis() - AIDS;
			if (Delay >= 1000)
				Logger.println("processSnapshotDeque is taking longer than it should, exactly "
						+ Delay + "ms");
		}
	}

	/**
	 * World drop
	 */

	public void doDrop() {
		final Point[] NonWildernessCoords = { new Point(48, 427),
				new Point(335, 720) };
		final Point[] WildernessCoords = { new Point(48, 141),
				new Point(335, 427) };

		Point[] coords = WildernessCoords;
		boolean inWild = true;
		int itemID = 1156;
		for (int i = 0; i < 3; i++) {
			int x = Formulae.Rand(coords[0].getX(), coords[1].getX());
			int y = Formulae.Rand(coords[0].getY(), coords[1].getY());

			while (world.getWorld().getTile(x, y).hasGameObject()) {
				x = Formulae.Rand(coords[0].getX(), coords[1].getX());
				y = Formulae.Rand(coords[0].getY(), coords[1].getY());
			}
			world.registerItem(new Item(itemID, x, y, 1, null));
		}
		itemID = 677;
		for (int i = 0; i < 11; i++) {
			int x = Formulae.Rand(coords[0].getX(), coords[1].getX());
			int y = Formulae.Rand(coords[0].getY(), coords[1].getY());

			while (world.getWorld().getTile(x, y).hasGameObject()) {
				x = Formulae.Rand(coords[0].getX(), coords[1].getX());
				y = Formulae.Rand(coords[0].getY(), coords[1].getY());
			}
			world.registerItem(new Item(itemID, x, y, 1, null));
		}
	}

	/**
	 * Cleans snapshots of entries over 60 seconds old (executed every second)
	 */
	public void cleanSnapshotDeque() {
		long curTime = System.currentTimeMillis();
		if (curTime - lastCleanedChatlogs > 1000) {
			lastCleanedChatlogs = curTime;
			lastCleanedChatlogsOutput++;
			if (lastCleanedChatlogsOutput > 60 * 5) {
				Logger.println("----------------------------------------------");
				Logger.println(world.getSnapshots().size() + " items on deque");
			}
			Iterator<Snapshot> i = world.getSnapshots().descendingIterator();
			Snapshot s = null;
			while (i.hasNext()) {
				s = i.next();
				if (curTime - s.getTimestamp() > 60000) {
					i.remove();
					s = null;
				} else {
					s = null;
				}
			}
			i = null;
			if (lastCleanedChatlogsOutput > 60 * 5) {
				Logger.println(world.getSnapshots().size()
						+ " items on deque AFTER CLEANUP");
				Logger.println("----------------------------------------------");
				lastCleanedChatlogsOutput = 0;
			}
		}
	}

	/**
	 * Cleans garbage (Tilecleanup)
	 */
	public synchronized void garbageCollect() {
		long startTime = System.currentTimeMillis();
		int curMemory = (int) (Runtime.getRuntime().totalMemory() - Runtime
				.getRuntime().freeMemory()) / 1000;
		int tileObjs = 0;
		int cleaned = 0;
		for (int i = 0; i < Instance.getWorld().tiles.length; i++) {
			for (int in = 0; in < Instance.getWorld().tiles[i].length; in++) {
				ActiveTile tile = Instance.getWorld().tiles[i][in];
				if (tile != null) {
					tileObjs++;
					if (!tile.hasGameObject() && !tile.hasItems()
							&& !tile.hasNpcs() && !tile.hasPlayers()) {
						Instance.getWorld().tiles[i][in] = null;
						cleaned++;
					}
				}
			}
		}
		Runtime.getRuntime().gc();
		int newMemory = (int) (Runtime.getRuntime().totalMemory() - Runtime
				.getRuntime().freeMemory()) / 1000;
		Logger.println("GARBAGE COLLECT | Executing Memory Cleanup");
		Logger.println("GARBAGE COLLECT | Memory before: " + curMemory + "kb"
				+ " Memory after: " + newMemory + " (Freed: "
				+ (curMemory - newMemory) + "kb)");
		Logger.println("GARBAGE COLLECT | Cleanup took "
				+ (System.currentTimeMillis() - startTime) + "ms");
	}

	public void spammsg() {
		Instance.getDelayedEventHandler().add(
				new MiniEvent(null, 11000 + 15000) {
					@Override
					public void action() {
						World.getWorld().sendWorldAnnouncement(
								"@red@Dropping in 3 seconds!");
						Instance.getDelayedEventHandler().add(
								new MiniEvent(null, 1000) {
									@Override
									public void action() {
										World.getWorld().sendWorldAnnouncement(
												"@red@Dropping in 2 seconds!");
										Instance.getDelayedEventHandler().add(
												new MiniEvent(null, 1000) {
													@Override
													public void action() {
														World.getWorld()
																.sendWorldAnnouncement(
																		"@red@Dropping in 1 seconds!");
														Instance.getDelayedEventHandler()
																.add(new MiniEvent(
																		null,
																		1000) {
																	@Override
																	public void action() {
																		World.getWorld()
																				.sendWorldAnnouncement(
																						"@red@Happy New Years from RSCAngel!");
																	}
																});
													}
												});
									}
								});
					}
				});

	}

	static Point[] droplocs = { new Point(291, 568), new Point(219, 642),
			new Point(136, 507), new Point(220, 432) };

	public void worlddrop(final int firstdrop, final int seconddrop,
			final int thirddrop) {

		World.getWorld().sendWorldAnnouncement(
				"@red@World drop started, dropping in 30 seconds!");
		World.getWorld()
				.sendWorldAnnouncement(
						"@whi@Get to Draynor, Falador (east bank), Varrock or Edgeville!");
		World.getWorld().sendWorldAnnouncement(
				"@red@Dropping 2 sets of Candy Canes every 4 hours!");
		spammsg();
		Instance.getDelayedEventHandler().add(new MiniEvent(null, 30000) {
			@Override
			public void action() {
				for (int i = 0; i < 100; i++) {
					Point coords = droplocs[DataConversions.random(0,
							droplocs.length - 1)];

					int x = coords.getX()
							+ (DataConversions.random(0, 1) == 1 ? -20 : 0)
							+ DataConversions.random(0, 20);
					int y = coords.getY()
							+ (DataConversions.random(0, 1) == 1 ? -20 : 0)
							+ DataConversions.random(0, 20);
					while (World.getWorld().getTile(x, y).hasGameObject()) {
						x = coords.getX()
								+ (DataConversions.random(0, 1) == 1 ? -20 : 0)
								+ DataConversions.random(0, 20);
						y = coords.getY()
								+ (DataConversions.random(0, 1) == 1 ? -20 : 0)
								+ DataConversions.random(0, 20);
					}
					world.registerItem(new Item(firstdrop, x, y, 1, null,
							System.currentTimeMillis()
									- DataConversions.random(45000, 60000)));
				}
				spammsg();
				Instance.getDelayedEventHandler().add(
						new MiniEvent(null, 30000) {
							@Override
							public void action() {
								for (int i = 0; i < 100; i++) {
									Point coords = droplocs[DataConversions
											.random(0, droplocs.length - 1)];

									int x = coords.getX()
											+ (DataConversions.random(0, 1) == 1 ? -20
													: 0)
											+ DataConversions.random(0, 20);
									int y = coords.getY()
											+ (DataConversions.random(0, 1) == 1 ? -20
													: 0)
											+ DataConversions.random(0, 20);
									while (World.getWorld().getTile(x, y)
											.hasGameObject()) {
										x = coords.getX()
												+ (DataConversions.random(0, 1) == 1 ? -20
														: 0)
												+ DataConversions.random(0, 20);
										y = coords.getY()
												+ (DataConversions.random(0, 1) == 1 ? -20
														: 0)
												+ DataConversions.random(0, 20);
									}
									world.registerItem(new Item(seconddrop, x,
											y, 1, null, System
													.currentTimeMillis()
													- DataConversions.random(
															45000, 60000)));
								}
								if (thirddrop == 0)
									return;
								spammsg();
								Instance.getDelayedEventHandler().add(
										new MiniEvent(null, 30000) {
											@Override
											public void action() {
												for (int i = 0; i < 100; i++) {
													Point coords = droplocs[DataConversions
															.random(0,
																	droplocs.length - 1)];

													int x = coords.getX()
															+ (DataConversions
																	.random(0,
																			1) == 1 ? -20
																	: 0)
															+ DataConversions
																	.random(0,
																			20);
													int y = coords.getY()
															+ (DataConversions
																	.random(0,
																			1) == 1 ? -20
																	: 0)
															+ DataConversions
																	.random(0,
																			20);
													while (World.getWorld()
															.getTile(x, y)
															.hasGameObject()) {
														x = coords.getX()
																+ (DataConversions
																		.random(0,
																				1) == 1 ? -20
																		: 0)
																+ DataConversions
																		.random(0,
																				20);
														y = coords.getY()
																+ (DataConversions
																		.random(0,
																				1) == 1 ? -20
																		: 0)
																+ DataConversions
																		.random(0,
																				20);
													}
													if (thirddrop != 0)
														world.registerItem(new Item(
																thirddrop,
																x,
																y,
																1,
																null,
																System.currentTimeMillis()
																		- DataConversions
																				.random(45000,
																						60000)));
												}

											}
										});
							}
						});
			}
		});
	}
}
