package rscproject.gs.npchandler;

import rscproject.gs.Instance;
import rscproject.gs.event.ShortEvent;
import rscproject.gs.model.ChatMessage;
import rscproject.gs.model.Npc;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;

public class Thrander implements NpcHandler {
    /**
     * World instance
     */
    public static final World world = Instance.getWorld();

    public void handleNpc(final Npc npc, Player player) throws Exception {
        player.informOfNpcMessage(new ChatMessage(npc, "Hello i'm thrander the smith, I'm an expert in armour modification", player));
        player.setBusy(true);
        Instance.getDelayedEventHandler().add(new ShortEvent(player) {
            public void action() {
                owner.informOfNpcMessage(new ChatMessage(npc, "Give me your armour designed for men and I can convert it", owner));
                Instance.getDelayedEventHandler().add(new ShortEvent(owner) {
                    public void action() {
                        owner.setBusy(false);
                        owner.informOfNpcMessage(new ChatMessage(npc, "Into something more comfortable for a woman, and vice versa", owner));
                        npc.unblock();
                    }
                });
            }
        });
        npc.blockedBy(player);
    }

}