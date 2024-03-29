import rscproject.gs.Instance;  import rscproject.gs.model.ActiveTile;
import rscproject.gs.model.InvItem;
import rscproject.gs.model.Item;
import rscproject.gs.model.Npc;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.quest.Quest;
import rscproject.gs.quest.QuestAction;

import rscproject.gs.external.EntityHandler;
import rscproject.gs.external.ItemDropDef;
import rscproject.gs.tools.DataConversions;

/**
 * Quest: Christmas! (v1.0) 8/1/2009 Status: COMPLETE Start: Santa (id 798),
 * 290,464 NPCs: Farrel (id 799), 106,670 1st Elf (id 800), 71,589 2nd Elf (id
 * 800), 656,467 3rd Elf (id 800), 403,685 Evil wizard (id 801), 279,454 Items:
 * Santa hat (id 971), Santa top (id 1315), Santa legs (id 1314), Candy cane (id
 * 1316), Toy 1 (id 1317), Toy 2 (id 1318), Toy 3 (id 1319), Toy 4 (id 1320),
 * Gift box (id 1321) Reward: 0 quest points, Magic Gift Box (id 1321) (random
 * item inside)
 * 
 * @author punKrockeR, Luke, Devin
 */
public class Christmas extends Quest {
	private static final int SANTA_ID = 798;
	private static final int FARREL_ID = 799;
	private static final int ELF_ID = 800;
	private static final int DARKWIZ_ID = 801;
	private static final int SANTA_HAT_ID = 971;
	private static final int SANTA_TOP_ID = 1315;
	private static final int SANTA_LEG_ID = 1314;
	private static final int CANDY_CANE_ID = 1316;
	private static final int TOY1_ID = 1317;
	private static final int TOY2_ID = 1318;
	private static final int TOY3_ID = 1319;
	private static final int TOY4_ID = 1320;
	private static final int GIFT_ID = 1321;
	private static final int DEFAULT_DELAY = 3200;
	private static final int QUEST_POINTS = 0;
	private World world = Instance.getWorld();
	private static final ItemDropDef[] BOX_ITEMS = new ItemDropDef[] {
			new ItemDropDef(155, 1, 3200), // Coal, 42%
			new ItemDropDef(SANTA_HAT_ID, 1, 799), // Santa hat, 2.99%
			new ItemDropDef(SANTA_TOP_ID, 1, 2000), // Santa top, 20%
			new ItemDropDef(SANTA_LEG_ID, 1, 2000), // Santa legs, 20%
			new ItemDropDef(CANDY_CANE_ID, 1, 2000), // Candy cane, 15%
			new ItemDropDef(575, 1, 1) // Christmas cracker, 0.01%
	};

	/**
	 * @return the quest's name
	 */
	public String getName() {
		return "Christmas!";
	}

	/**
	 * @return this quest's unique id
	 */
	public int getUniqueID() {
		return 5;
	}

	/**
	 * Initialises the quest
	 */
	public void init() {
		associateNpc(SANTA_ID);
		associateNpc(FARREL_ID);
		associateNpc(ELF_ID);
		associateNpc(DARKWIZ_ID);
		associateItem(TOY1_ID);
		associateItem(TOY2_ID);
		associateItem(TOY3_ID);
		associateItem(TOY4_ID);
		associateItem(GIFT_ID);
	}

	/**
	 * @return if the given NPC is visible to the player
	 */
	public boolean isNpcVisible(Npc npc, Player player) {

		if (npc.getLoc().getId() == ELF_ID && npc.getLoc().startX() == 71
				&& npc.getLoc().startY() == 589) // First elf
			return player.getQuestStage(this) == 2
					&& player.getQuestStage(this) != COMPLETE;
		else if (npc.getLoc().getId() == ELF_ID && npc.getLoc().startX() == 71
				&& npc.getLoc().startY() == 594) // First elf
			return player.getQuestStage(this) == 2
					&& player.getQuestStage(this) != COMPLETE;
		else if (npc.getLoc().getId() == ELF_ID && npc.getLoc().startX() == 72
				&& npc.getLoc().startY() == 585) // First elf
			return player.getQuestStage(this) == 2
					&& player.getQuestStage(this) != COMPLETE;
		else if (npc.getLoc().getId() == ELF_ID && npc.getLoc().startX() == 656
				&& npc.getLoc().startY() == 467) // Second elf
			return player.getQuestStage(this) == 4
					&& player.getQuestStage(this) != COMPLETE;
		else if (npc.getLoc().getId() == ELF_ID && npc.getLoc().startX() == 653
				&& npc.getLoc().startY() == 467) // Second elf
			return player.getQuestStage(this) == 4
					&& player.getQuestStage(this) != COMPLETE;
		else if (npc.getLoc().getId() == ELF_ID && npc.getLoc().startX() == 403
				&& npc.getLoc().startY() == 685) // Last elf
			return player.getQuestStage(this) == 6
					&& player.getQuestStage(this) != COMPLETE;
		else if (npc.getLoc().getId() == ELF_ID && npc.getLoc().startX() == 419
				&& npc.getLoc().startY() == 684) // Last elf
			return player.getQuestStage(this) == 6
					&& player.getQuestStage(this) != COMPLETE;
		else if (npc.getLoc().getId() == ELF_ID && npc.getLoc().startX() == 408
				&& npc.getLoc().startY() == 685) // Last elf
			return player.getQuestStage(this) == 6
					&& player.getQuestStage(this) != COMPLETE;

		return true;
	}

	/**
	 * @return if the given item is visible to the player
	 */
	public boolean isItemVisible(Item item, Player player) {
		if (item.getID() == TOY1_ID)
			return player.getQuestStage(this) >= 2;
		else if (item.getID() == TOY2_ID)
			return player.getQuestStage(this) >= 4;
		else if (item.getID() == TOY3_ID)
			return player.getQuestStage(this) >= 6;
		else if (item.getID() == TOY4_ID)
			return player.getQuestStage(this) >= 7;

		return true;
	}

	/**
	 * @return if the player has the given item in his inventory, bank or if
	 *         it's on the ground somewhere
	 */
	public boolean hasItem(Player player, int id) {
		if (player.getInventory().hasItemId(id)) {
			player.getActionSender().sendMessage(
					"You already have this elf's toy!");
			return true;
		}

		if (player.getBank().hasItemId(id)) {
			player.getActionSender().sendMessage(
					"You already have this elf's toy in your bank!");
			return true;
		}

		ActiveTile[][] tiles = player.getViewArea().getViewedArea(20, 20, 20,
				20);
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				ActiveTile t = tiles[x][y];
				if (t != null) {
					for (Item i : t.getItems()) {
						if (i.getID() == id) {
							if (i.getOwner().equals(player)) {
								player
										.getActionSender()
										.sendMessage(
												"You already have this elf's toy nearby on the ground!");
								return true;
							}
						}
					}
				}
			}
		}

		player.getActionSender().sendMessage("The toy falls to the ground");
		return false;
	}

	/**
	 * Handles the given quest action
	 */
	public void handleAction(QuestAction action, Object[] args,
			final Player player) {
		int stage = player.getQuestStage(this);

		if (action == action.USED_ITEM) {
			if (!(args[0] instanceof InvItem))
				return;

			final InvItem item = (InvItem) args[0];

			handleUseItem(item, player);
		} else if (action == action.TALKED_NPC) {
			if (!(args[0] instanceof Npc))
				return;

			final Npc npc = (Npc) args[0];

			if (npc.getID() == SANTA_ID) {
				player.setBusy(true);

				if (stage == -1)
					startQuest(player, npc);
				else
					handleSantaTalk(player, npc);
			} else if (npc.getID() == FARREL_ID) {
				player.setBusy(true);
				handleFarrelTalk(player, npc);
			} else if (npc.getID() == ELF_ID) {
				player.setBusy(true);
				handleElfTalk(player, npc);
			} else if (npc.getID() == DARKWIZ_ID) {
				player.setBusy(true);
				handleWizardTalk(player, npc);
			}
		} else if (action == action.KILLED_NPC) {
			if (!(args[0] instanceof Npc))
				return;

			final Npc npc = (Npc) args[0];

			if (npc.getID() == ELF_ID) // If the player doesn't have the toy in
										// their bank or inventory, drop the
										// toy.
			{
				if (npc.getLoc().startX() == 71 && npc.getLoc().startY() == 589
						&& player.getQuestStage(this) == 2
						&& !hasItem(player, TOY1_ID))
					world.registerItem(new Item(TOY1_ID, npc.getX(),
							npc.getY(), 1, player));
				else if (npc.getLoc().startX() == 71
						&& npc.getLoc().startY() == 594
						&& player.getQuestStage(this) == 2
						&& !hasItem(player, TOY1_ID))
					world.registerItem(new Item(TOY1_ID, npc.getX(),
							npc.getY(), 1, player));
				else if (npc.getLoc().startX() == 72
						&& npc.getLoc().startY() == 585
						&& player.getQuestStage(this) == 2
						&& !hasItem(player, TOY1_ID))
					world.registerItem(new Item(TOY1_ID, npc.getX(),
							npc.getY(), 1, player));
				else if (npc.getLoc().startX() == 653
						&& npc.getLoc().startY() == 467
						&& player.getQuestStage(this) == 4
						&& !hasItem(player, TOY2_ID))
					world.registerItem(new Item(TOY2_ID, npc.getX(),
							npc.getY(), 1, player));
				else if (npc.getLoc().startX() == 656
						&& npc.getLoc().startY() == 467
						&& player.getQuestStage(this) == 4
						&& !hasItem(player, TOY2_ID))
					world.registerItem(new Item(TOY2_ID, npc.getX(),
							npc.getY(), 1, player));
				else if (npc.getLoc().startX() == 403
						&& npc.getLoc().startY() == 685
						&& player.getQuestStage(this) == 6
						&& !hasItem(player, TOY3_ID))
					world.registerItem(new Item(TOY3_ID, npc.getX(),
							npc.getY(), 1, player));
				else if (npc.getLoc().startX() == 419
						&& npc.getLoc().startY() == 684
						&& player.getQuestStage(this) == 6
						&& !hasItem(player, TOY3_ID))
					world.registerItem(new Item(TOY3_ID, npc.getX(),
							npc.getY(), 1, player));
				else if (npc.getLoc().startX() == 408
						&& npc.getLoc().startY() == 685
						&& player.getQuestStage(this) == 6
						&& !hasItem(player, TOY3_ID))
					world.registerItem(new Item(TOY3_ID, npc.getX(),
							npc.getY(), 1, player));
			}
		}
	}

	/**
	 * Handles item use
	 */
	private void handleUseItem(final InvItem item, final Player player) {
		if (item.getID() == GIFT_ID) {
			player.setBusy(true);
			player.getActionSender().sendMessage(
					"You open the " + item.getDef().getName() + "...");
			sleep(2000);
			int total = 0;
			for (ItemDropDef drop : BOX_ITEMS)
				total += drop.getWeight();

			int hit = DataConversions.random(0, total);
			total = 0;
			ItemDropDef reward = null;
			for (ItemDropDef drop : BOX_ITEMS) {
				if (hit >= total && hit < (total + drop.getWeight())) {
					reward = drop;
					break;
				}

				total += drop.getWeight();
			}

			player.getInventory().remove(GIFT_ID, 1);
			if (reward == null)
				player.getActionSender().sendMessage(
						"You don't get shit! (reward is null)");
			else
				player.getInventory().add(
						new InvItem(reward.getID(), reward.getAmount()));

			player.getActionSender().sendMessage(
					"It contained a "
							+ ((reward.getAmount() > 1) ? reward.getAmount()
									: "a ")
							+ EntityHandler.getItemDef(reward.getID())
									.getName()
							+ (reward.getAmount() > 1 ? "s" : "") + "!");
			player.getActionSender().sendInventory();
			player.getActionSender().sendSound("click");
			player.setBusy(false);
		} else
			player.setBusy(false);
	}

	/**
	 * Handles npc chat if the quest hasn't been started yet
	 */
	private void startQuest(final Player player, final Npc npc) {
		player.setBusy(true);
		sendChat("Christmas! It's ruined!", npc, player);
		sleep(DEFAULT_DELAY);
		sendChat("Why?", player, npc);
		sleep(DEFAULT_DELAY);
		sendChat("The presents... they're gone!", npc, player);
		sleep(DEFAULT_DELAY);
		sendChat("What presents? Gone where?", player, npc);
		sleep(DEFAULT_DELAY);
		sendChat("All my Christmas presents have been stolen!", npc, player);
		sleep(DEFAULT_DELAY);
		sendChat("Who stole them?", player, npc);
		sleep(DEFAULT_DELAY);
		queueChat(npc, player, DEFAULT_DELAY, "My own elves!",
				"I landed here last night in my sleigh",
				"To deliver my presents to the citizens of this land",
				"But an evil Dark Knight wizard cast a spell on my Elves",
				"And they stole my presents and ran away!");
		player.setBusy(false);
		int option = getMenuOption(player, "That sucks", "Where are they now?",
				"Good luck with that");
		if (option == -1)
			return;
		player.setBusy(true);
		sleep(DEFAULT_DELAY);
		if (option == 0) // No
		{
			sendChat("Indeed it does!", npc, player);
			player.setBusy(false);
			npc.unblock();
		} else if (option == 1) // Yes
		{
			queueChat(
					npc,
					player,
					DEFAULT_DELAY,
					"I don't know. But one of the elves, Farrel, he's in Lumbridge",
					"It appears the spell didn't affect him",
					"But he's in hiding from the other elves who have turned evil!",
					"And I'm afraid the spell is quite irreversible");
			player.setBusy(false);
			option = getMenuOption(player, "Maybe I can help?",
					"Christmas really is ruined then");
			if (option == -1)
				return;
			player.setBusy(true);
			sleep(2000);
			if (option == 0) {
				if (player.getQuestPoints() < 2) {
					sendChat(
							"I doubt it, you don't have a very good reputation for helping people",
							npc, player);
					player.setBusy(false);
					npc.unblock();
					sleep(2000);
					player
							.getActionSender()
							.sendMessage(
									"You need at least 2 Quest Points to start this quest");
				} else {
					queueChat(
							npc,
							player,
							DEFAULT_DELAY,
							"Perhaps you can... I don't know where the others are",
							"But if you can find Farrel, he can help you find them",
							"All I know is that he's in Lumbridge",
							"He said he's hiding near a church",
							"Please find him and help me save Christmas!");
					player.setQuestStage(getUniqueID(), 1); // Start quest
					player.setBusy(false);
					npc.unblock();
				}
			} else {
				sendChat("I'm afraid so!", npc, player);
				player.setBusy(false);
				npc.unblock();
			}
		} else {
			sendChat("Oh, I'll need it!", npc, player);
			player.setBusy(false);
			npc.unblock();
		}
	}

	/**
	 * Handles Santa's chat if the quest is started but not finished
	 */
	private void handleSantaTalk(final Player player, final Npc npc) {
		if (player.getQuestStage(this) == COMPLETE) {
			sendChat("Merry christmas, hero!", npc, player);
			sleep(2000);
			player.setBusy(false);
			npc.unblock();
		} else if (player.getQuestStage(this) != 7) {
			sendChat("Oh, " + player.getUsername()
					+ ". Please find those elves!", npc, player);
			sleep(2000);
			player.setBusy(false);
			npc.unblock();
		} else {
			if (!player.getInventory().hasItemId(TOY1_ID)
					|| !player.getInventory().hasItemId(TOY2_ID)
					|| !player.getInventory().hasItemId(TOY3_ID)
					|| !player.getInventory().hasItemId(TOY4_ID)) {
				queueChat(npc, player, DEFAULT_DELAY,
						"Farrel said you'd collected the toys",
						"Please bring them all to me as soon as possible!");
				npc.unblock();
				player.setBusy(false);
				return;
			}

			queueChat(npc, player, DEFAULT_DELAY, player.getUsername()
					+ "! I can't express my gratitude with words",
					"You saved Christmas for your land!",
					"I don't have anything I can spare for you",
					"But I can give you your present early",
					"It's a magic gift box. The item within is unknown",
					"I hope you get something fantastic!",
					"You deserve it for your heroic antics.", "Farewell kind "
							+ (player.isMale() ? "sir" : "lady")
							+ "! And meeeeery christmas!");

			player.getActionSender().sendMessage(
					"Santa takes the toys and hands you a small gift box");
			if (player.getInventory().remove(TOY1_ID, 1) == -1) {
				player.setBusy(false);
				return;
			}
			if (player.getInventory().remove(TOY2_ID, 1) == -1) {
				player.setBusy(false);
				return;
			}
			if (player.getInventory().remove(TOY3_ID, 1) == -1) {
				player.setBusy(false);
				return;
			}
			if (player.getInventory().remove(TOY4_ID, 1) == -1) {
				player.setBusy(false);
				return;
			}
			player.getInventory().add(new InvItem(GIFT_ID, 1));
			player.getActionSender().sendInventory();
			player.getActionSender().sendSound("click");
			sleep(DEFAULT_DELAY);
			player.setBusy(false);
			npc.unblock();
			player.setQuestStage(getUniqueID(), COMPLETE);
			sleep(2500);
			player
					.getActionSender()
					.sendMessage(
							"Maybe I should go and talk to that Evil Dark Knight Wizard...");
		}
	}

	/**
	 * Handles Farrel's chat if the quest is started but not finished
	 */
	private void handleFarrelTalk(final Player player, final Npc npc) {
		player.setBusy(true);
		if (player.getQuestStage(this) == -1) {
			sendChat("Bugger off", npc, player);
			sleep(DEFAULT_DELAY);
			player.setBusy(false);
			npc.unblock();
		} else if (player.getQuestStage(this) == 1) {
			player.setBusy(true);
			sendChat("Who are you?", npc, player);
			sleep(DEFAULT_DELAY);
			sendChat("Santa sent me to find you", player, npc);
			sleep(DEFAULT_DELAY);
			sendChat("What for?", npc, player);
			sleep(DEFAULT_DELAY);
			player.setBusy(false);
			int option = getMenuOption(player, "To murder you", "To help");
			if (option == -1)
				return;
			player.setBusy(true);
			sleep(DEFAULT_DELAY);
			if (option == 0) // No
			{
				sendChat(
						"Go for it. It's only a matter of time before they find me anyway.",
						npc, player);
				player.setBusy(false);
				npc.unblock();
			} else // Yes
			{
				queueChat(npc, player, DEFAULT_DELAY,
						"Well if Santa trusts you, I guess I can.",
						"We must get started at once if we're to find the others.");
				player.setBusy(false);
				option = getMenuOption(player, "Any idea where they are?",
						"Maybe later");
				if (option == -1)
					return;
				player.setBusy(true);
				sleep(DEFAULT_DELAY);
				if (option == 0) // Yes
				{
					queueChat(
							npc,
							player,
							DEFAULT_DELAY,
							"One of them ran to the desert nearby",
							"I don't know where he's hiding, but we hate the heat",
							"He must be hiding near cliffs or rocks that offer shade",
							"You know this land better than I",
							"It would be safer for all involved if you went, and not me"

					);
					player.setBusy(false);
					option = getMenuOption(player, "I agree", "Pussy");
					if (option == -1)
						return;
					player.setBusy(true);
					sleep(DEFAULT_DELAY);
					if (option == 0) // Yes
					{
						queueChat(
								npc,
								player,
								DEFAULT_DELAY,
								"Excellent. He should be carrying a small "
										+ EntityHandler.getItemDef(TOY1_ID)
												.getName(),
								"Get it and bring it to me. Use any means necessary. Any.",
								"My comrades cannot be saved now.");
						player.setBusy(false);
						npc.unblock();
						player.setQuestStage(getUniqueID(), 2);
					} else // No
					{
						sendChat(
								"Santa's trust was misplaced. Christmas is truly ruined.",
								npc, player);
						player.setBusy(false);
						npc.unblock();
					}
				} else // No
				{
					sendChat("There won't be a \"later\"!", npc, player);
					player.setBusy(false);
					npc.unblock();
				}
			}
		} else if (player.getQuestStage(this) == 2) {
			sendChat("Have you found the first toy?", npc, player);
			sleep(2000);
			player.setBusy(false);
			int option = getMenuOption(player, "Not yet", "Yeah");
			if (option == -1)
				return;
			sleep(DEFAULT_DELAY);
			if (option == 0) // No
			{
				sendChat(
						"Well please act quickly. Time is running out! Remember, he's in the desert",
						npc, player);
				player.setBusy(false);
				npc.unblock();
			} else // Yes
			{
				if (player.getInventory().hasItemId(TOY1_ID)) {
					sendChat("Great! Please, hand it to me", npc, player);
					sleep(DEFAULT_DELAY);
					player.getActionSender().sendMessage(
							"You give Farrel the "
									+ EntityHandler.getItemDef(TOY1_ID)
											.getName());
					player.setQuestStage(getUniqueID(), 3);
					player.getActionSender().sendSound("click");
					player.getInventory().remove(TOY1_ID, 1);
					player.getActionSender().sendInventory();
					sleep(DEFAULT_DELAY);
					questStage3(npc, player);
				} else {
					sendChat(
							"No you haven't. Please don't fool around. Time is running out!",
							npc, player);
					player.setBusy(false);
					npc.unblock();
				}
			}
		} else if (player.getQuestStage(this) == 3) {
			questStage3(npc, player);
		} else if (player.getQuestStage(this) == 4) {
			sendChat("Have you found the second toy?", npc, player);
			sleep(DEFAULT_DELAY);
			player.setBusy(false);
			int option = getMenuOption(player, "Right on brother", "No");
			if (option == -1)
				return;
			player.setBusy(true);
			sleep(DEFAULT_DELAY);
			if (option == 0) // No
			{
				if (player.getInventory().hasItemId(TOY2_ID)) {
					sendChat(
							"I don't know what that means, but please hand me the toy",
							npc, player);
					sleep(DEFAULT_DELAY);
					player.getActionSender().sendMessage(
							"You give Farrel the "
									+ EntityHandler.getItemDef(TOY2_ID)
											.getName());
					player.getActionSender().sendSound("click");
					player.getInventory().remove(TOY2_ID, 1);
					player.setQuestStage(getUniqueID(), 5);
					player.getActionSender().sendInventory();
					sleep(DEFAULT_DELAY);
					questStage5(npc, player);
				} else {
					sendChat(
							"I don't know what that means, but you don't have the toy",
							npc, player);
					sleep(DEFAULT_DELAY);
					sendChat("Please hurry. Christmas is almost over!", npc,
							player);
					player.setBusy(false);
					npc.unblock();
				}
			} else // Yes
			{
				sendChat(
						"Well please hurry. Christmas is almost over! Remember, \"Baxtorian Falls\"!",
						npc, player);
				player.setBusy(false);
				npc.unblock();
			}
		} else if (player.getQuestStage(this) == 5) {
			questStage5(npc, player);
		} else if (player.getQuestStage(this) == 6) // Final present
		{
			player.setBusy(true);
			sendChat("Did you get izzy's toy?", npc, player);
			sleep(DEFAULT_DELAY);
			player.setBusy(false);
			int option = getMenuOption(player, "Yes.", "No.");
			if (option == -1)
				return;
			player.setBusy(true);
			sleep(DEFAULT_DELAY);
			if (option == 0) // Yes
			{
				if (player.getInventory().hasItemId(TOY3_ID)) {
					queueChat(
							npc,
							player,
							DEFAULT_DELAY,
							"Thank you so much, " + player.getUsername(),
							"You single-handedly saved Christmas.",
							"The elves at the north pole will write songs of you hero",
							"You will be respected and remembered forever, believe me.",
							"The final present, I have kept all along",
							"Here, you take all four presents and give them to santa",
							"I'll tell him of your bravery",
							"I'm sure he'll want to reward you", "Farewell, "
									+ player.getUsername() + ".",
							"Merry christmas.");
					player.getActionSender().sendMessage(
							"Farrel hands you the three toys");
					if (player.getInventory().hasItemId(TOY1_ID)) {
						sendChat("Oh... You already have the toys.", npc,
								player);
						player.setBusy(false);
						return;
					}
					player.getInventory().add(new InvItem(TOY1_ID, 1));
					player.getInventory().add(new InvItem(TOY2_ID, 1));
					// player.getInventory().add(new InvItem(TOY3_ID, 1));
					player.getInventory().add(new InvItem(TOY4_ID, 1));
					player.getActionSender().sendInventory();
					player.getActionSender().sendSound("click");
					player.setBusy(false);
					npc.unblock();
					player.setQuestStage(getUniqueID(), 7);
				} else {
					sendChat("No you don't.", npc, player);
					sleep(DEFAULT_DELAY);
					sendChat(
							"Remember, flames and lava and the ocean are Izzy's favourite things",
							npc, player);
					player.setBusy(false);
					npc.unblock();
				}
			} else // No
			{
				sendChat(
						"Please get it. Don't hesitate just because he's my brother.",
						npc, player);
				player.setBusy(false);
				npc.unblock();
			}
		} else {
			sendChat("Hey there, " + player.getUsername() + "!", npc, player);
			sleep(DEFAULT_DELAY);
			player.setBusy(false);
			npc.unblock();
		}
	}

	/**
	 * Handles quest stage 3 chat
	 */
	private void questStage3(final Npc npc, final Player player) {
		sendChat("So what now?", player, npc);
		sleep(DEFAULT_DELAY);
		queueChat(
				npc,
				player,
				DEFAULT_DELAY,
				"While you were gone, I did some reconnaissance work",
				"I overheard some humans talking about a deranged Elf they saw while sightseeing");
		player.setBusy(false);
		int option = getMenuOption(player, "Yes.", "No.");
		if (option == -1)
			return;
		player.setBusy(true);
		sleep(DEFAULT_DELAY);
		if (option == 0) // Yes
		{
			queueChat(
					npc,
					player,
					DEFAULT_DELAY,
					"A place called the \"Baxtorian Falls\". Somewhere near a gnome stronghold.",
					"It's not much of a lead, but it's all we've got");
			sendChat("I'll get on it then", player, npc);
			player.setBusy(false);
			npc.unblock();
			player.setQuestStage(getUniqueID(), 4);
		} else // No
		{
			sendChat("Suit yourself, grinch.", npc, player);
			player.setBusy(false);
			npc.unblock();
		}
	}

	/**
	 * Handles quest stage 5 chat
	 */
	private void questStage5(final Npc npc, final Player player) {
		sendChat("What's next little guy?", player, npc);
		sleep(DEFAULT_DELAY);
		sendChat("Don't patronize me, " + player.getUsername()
				+ ". I'm stronger in ways you can't even imagine", npc, player);
		sleep(DEFAULT_DELAY);
		sendChat("Well I'll take your word for it. Where's the last elf?",
				player, npc);
		sleep(DEFAULT_DELAY);
		sendChat("I don't know.", npc, player);
		sleep(DEFAULT_DELAY);
		sendChat("What? Not even a clue?", player, npc);
		sleep(DEFAULT_DELAY);
		sendChat("I'm afraid you'll have to find izzy on your own", npc, player);
		sleep(DEFAULT_DELAY);
		sendChat("Izzy? That's his name?", player, npc);
		sleep(DEFAULT_DELAY);
		sendChat("The last toy to get is a "
				+ EntityHandler.getItemDef(TOY3_ID).getName()
				+ ". Izzy took that one.", npc, player);
		sleep(DEFAULT_DELAY);
		sendChat(
				"Well did you know him very well? Where's somewhere he's likely to go?",
				player, npc);
		sleep(DEFAULT_DELAY);
		sendChat("I knew him very well.", npc, player);
		sleep(DEFAULT_DELAY);
		sendChat("He was my younger brother.", npc, player);
		sleep(DEFAULT_DELAY);
		sendChat("I'm sorry, farrel.", player, npc);
		sleep(DEFAULT_DELAY);
		queueChat(
				npc,
				player,
				DEFAULT_DELAY,
				"What he's become... he's not my brother anymore",
				"All I know that might help you is that, unlike the rest of us",
				"Izzy loved the heat. Flames, lava, that sort of thing",
				"And he also loved the ocean. You know this land",
				"Take what little information I have to offer",
				"And find my brother. Get the last toy back.");
		player.setBusy(false);
		int option = getMenuOption(player, "Of course, Farrel",
				"No. I refuse to murder your brother",
				"I've had enough of this");
		if (option == -1)
			return;
		player.setBusy(true);
		sleep(DEFAULT_DELAY);
		if (option == 0) // Yes
		{
			queueChat(npc, player, DEFAULT_DELAY, "Thank you dearly, "
					+ player.getUsername(),
					"Good luck. Please, make his death quick.");
			player.setQuestStage(getUniqueID(), 6);
			player.setBusy(false);
			npc.unblock();
		} else // No
		{
			sendChat("Then the death of Christmas is on your conscience.", npc,
					player);
			player.setBusy(false);
			npc.unblock();
		}
	}

	/**
	 * Handles talking to the evil elves
	 */
	private void handleElfTalk(final Player player, final Npc npc) {
		sendChat("Get out of my way tall one", npc, player);
		sleep(2000);
		player.setBusy(false);
		npc.unblock();
	}

	/**
	 * Handles talking to the evil wizard
	 */
	private void handleWizardTalk(final Player player, final Npc npc) {
		if (player.getQuestStage(this) == COMPLETE) {
			sendChat("You! You thwarted my plans!", npc, player);
			sleep(DEFAULT_DELAY);
			sendChat("Christmas is for everyone! Why do you want to stop it?",
					player, npc);
			sleep(DEFAULT_DELAY);
			queueChat(
					npc,
					player,
					DEFAULT_DELAY,
					"None of your business, fiend! I'm going to send you back in time",
					"To before you ruined my plans!");
			player.setBusy(false);
			int option = getMenuOption(player, "Please don't!",
					"Give it your best shot");
			if (option == -1)
				return;
			player.setBusy(true);
			sleep(DEFAULT_DELAY);
			if (option == 0) // No
			{
				queueChat(npc, player, DEFAULT_DELAY,
						"Bah! It makes no difference anyway",
						"I have more important things to ruin");
				player.setBusy(false);
				npc.unblock();
			} else // Yes
			{
				queueChat(npc, player, DEFAULT_DELAY, "I will!", "Muahahaha!");
				player.getActionSender().sendTeleBubble(
						player.getLocation().getX(),
						player.getLocation().getY(), false);
				player.getActionSender().sendSound("spellfail");
				player.teleport(289, 459, true);
				sleep(DEFAULT_DELAY);
				player.getActionSender().sendTeleBubble(289, 459, false);
				sendChat("Huh? What? Where am i?", player, npc);
				player.setQuestStage(getUniqueID(), -1);
				npc.unblock();
				sleep(DEFAULT_DELAY);
				player.setBusy(false);
				if (player.getInventory().hasItemId(TOY1_ID)
						|| player.getInventory().hasItemId(TOY2_ID)
						|| player.getInventory().hasItemId(TOY3_ID)
						|| player.getInventory().hasItemId(TOY4_ID)
						|| player.getInventory().hasItemId(GIFT_ID))
					player
							.getActionSender()
							.sendMessage(
									"Santa's presents are unaffected by the Wizard's spell!");
			}
		} else {
			sendChat("Hahaha! You may call me, the grinch! Bahaha!", npc,
					player);
			sleep(DEFAULT_DELAY);
			player.getActionSender().sendMessage(
					"He really does look quite insane");
			player.setBusy(false);
			npc.unblock();
		}
	}

	/**
	 * Construct the quest (empty)
	 */
	public Christmas() {
	}
}
