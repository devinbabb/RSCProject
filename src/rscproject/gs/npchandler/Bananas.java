package rscproject.gs.npchandler;

import rscproject.gs.Instance;
import rscproject.gs.event.ShortEvent;
import rscproject.gs.model.ChatMessage;
import rscproject.gs.model.InvItem;
import rscproject.gs.model.MenuHandler;
import rscproject.gs.model.Npc;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;

/**
 * 
 * @author xEnt
 * 
 */
public class Bananas implements NpcHandler {

    public static final World world = Instance.getWorld();

    String[] names = { "Yes i will sell you 20 bananas", "No sorry, i don't have any" };

    public void handleNpc(final Npc npc, Player player) throws Exception {
	player.setBusy(false);
	player.informOfNpcMessage(new ChatMessage(npc, "Hello, i am after 20 Bananas, do you have 20 you can sell?", player));
	Instance.getDelayedEventHandler().add(new ShortEvent(player) {
	    public void action() {

		owner.setMenuHandler(new MenuHandler(names) {
		    public void handleReply(final int option, final String reply) {
			if (option < 0 && option > names.length)
			    return;

			if (option == 0) {
			    owner.informOfChatMessage(new ChatMessage(owner, reply, npc));
			    Instance.getDelayedEventHandler().add(new ShortEvent(owner) {
				public void action() {

				    Instance.getDelayedEventHandler().add(new ShortEvent(owner) {
					public void action() {
					    owner.informOfNpcMessage(new ChatMessage(npc, "I will give you 30gp for your 20 bananas is that ok?", owner));
					    world.getDelayedEventHandler().add(new ShortEvent(owner) {
						public void action() {
						    String[] s = { "Sure", "Sorry, i would rather eat them" };
						    owner.setMenuHandler(new MenuHandler(s) {
							public void handleReply(final int option, final String reply) {
							    if (option == 0) {

								owner.informOfChatMessage(new ChatMessage(owner, reply, npc));
								world.getDelayedEventHandler().add(new ShortEvent(owner) {
								    public void action() {
									if (owner.getInventory().countId(249) < 20) {
									    owner.informOfNpcMessage(new ChatMessage(npc, "It looks like you don't have enough Bananas, don't waste my time.", owner));
									    owner.setBusy(false);
									    npc.setBusy(false);
									    npc.unblock();
									    return;
									} else {
									    int count = 0;
									    for (int i = 0; i < 20; i++) {
										if (owner.getInventory().remove(249, 1) > -1) {
										    count++;
										}
									    }
									    if (count == 20) {
										owner.getInventory().add(new InvItem(10, 30));
										owner.getActionSender().sendMessage("You receive 30gp");
										owner.getActionSender().sendInventory();
										owner.setBusy(false);
										npc.setBusy(false);
										npc.unblock();
										return;
									    }
									}
								    }
								});
							    } else {
								owner.setBusy(false);
								npc.setBusy(false);
								npc.unblock();
								return;
							    }
							}
						    });
						    owner.getActionSender().sendMenu(s);

						}
					    });

					}
				    });
				}
			    });
			} else {
			    owner.setBusy(false);
			    npc.setBusy(false);
			    npc.unblock();
			    return;
			}

		    }
		});
		owner.getActionSender().sendMenu(names);
	    }
	});
    }
}
