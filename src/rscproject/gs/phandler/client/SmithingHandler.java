package rscproject.gs.phandler.client;

import org.apache.mina.common.IoSession;
import rscproject.config.Formulae;
import rscproject.gs.Instance;
import rscproject.gs.connection.Packet;
import rscproject.gs.connection.RSCPacket;
import rscproject.gs.external.EntityHandler;
import rscproject.gs.external.ItemSmithingDef;
import rscproject.gs.model.Bubble;
import rscproject.gs.model.InvItem;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.phandler.PacketHandler;

public class SmithingHandler implements PacketHandler {
	/**
	 * World instance
	 */
	public static final World world = Instance.getWorld();

	public void handlePacket(Packet p, IoSession session) throws Exception {
		Player player = (Player) session.getAttachment();
		if (player.isBusy())
			return;

		int pID = ((RSCPacket) p).getID();
		switch (pID) {
		case 201: {
			if (!player.isSmithing()) {
				player.setSuspicious(true);
				return;
			}
			player.setBusy(true);
			int itemToMake = p.readInt();
			int barID = Formulae.getBarIdFromItem(itemToMake);
			if (barID == -1 || barID != player.getSmithingBar()) {
				player.setSuspicious(true);
				player.setSmithing(false);
				player.getActionSender().showSmithing(-1);
				player.setBusy(false);
				return;
			}
			ItemSmithingDef def = EntityHandler.getSmithingDefbyID(itemToMake);
			if (def == null) {
				player.getActionSender().sendMessage(
						"Nothing interesting happens.");
				player.getActionSender().showSmithing(-1);
				player.setBusy(false);
				return;
			}
			if (player.getCurStat(13) < def.getRequiredLevel()) {
				player.getActionSender().sendMessage(
						"You need a smithing level of "
								+ def.getRequiredLevel() + " to make this");
				player.setBusy(false);
				return;
			}
			if (player.getInventory().countId(barID) < def.getRequiredBars()) {
				player.getActionSender().sendMessage(
						"You don't have enough bars to make this.");
				player.setBusy(false);
				return;
			}
			if (EntityHandler.getItemDef(def.getItemID()).isMembers()
					&& !World.isMembers()) {
				player.getActionSender().sendMessage(
						"This feature is only avaliable on a members server");
				player.setBusy(false);
				return;
			}
			player.getActionSender().sendSound("anvil");
			for (int x = 0; x < def.getRequiredBars(); x++) {
				player.getInventory().remove(new InvItem(barID, 1));
			}
			Bubble bubble = new Bubble(player, barID);
			for (Player x : player.getViewArea().getPlayersInView()) {
				x.informOfBubble(bubble);
			}
			if (EntityHandler.getItemDef(def.getItemID()).isStackable()) {
				player.getActionSender().sendMessage(
						"You hammer the metal into some "
								+ EntityHandler.getItemDef(def.getItemID())
										.getName());
				player.getInventory().add(
						new InvItem(def.getItemID(), def.getAmount()));
			} else {
				player.getActionSender().sendMessage(
						"You hammer the metal into a "
								+ EntityHandler.getItemDef(def.getItemID())
										.getName());
				for (int x = 0; x < def.getAmount(); x++) {
					player.getInventory().add(new InvItem(def.getItemID(), 1));
				}
			}
			player.incExp(13,
					Formulae.getSmithingExp(barID, def.getRequiredBars()), true);
			player.getActionSender().sendStat(13);
			player.getActionSender().sendInventory();
			player.setSmithing(false);
			player.getActionSender().showSmithing(-1);
			break;
		}
		case 202: {
			player.setBusy(true);
			player.setSmithing(false);
			player.getActionSender().showSmithing(-1);
			break;
		}
		}
		player.setBusy(false);

	}
}