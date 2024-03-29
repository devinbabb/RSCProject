package rscproject.ls.packethandler.loginserver;

import org.apache.mina.common.IoSession;
import rscproject.ls.Server;
import rscproject.ls.model.PlayerSave;
import rscproject.ls.model.World;
import rscproject.ls.net.Packet;
import rscproject.ls.packethandler.PacketHandler;
import rscproject.ls.util.DataConversions;

/**
 * Saves the player.
 * 
 * @author Devin
 */

public class PlayerSaveHandler implements PacketHandler {

	public void handlePacket(Packet p, IoSession session) throws Exception {
		World world = (World) session.getAttachment();
		long usernameHash = p.readLong();
		int owner = p.readInt();
		PlayerSave save = Server.getServer().findSave(usernameHash, world);
		if (save == null) {
			System.out.println("Error loading data for: "
					+ DataConversions.hashToUsername(usernameHash));
			return;
		}
		System.out.println("Adding save data for: " + save.getUsername());

		if (owner != save.getOwner()) {
			System.out.println("WARNING ATTEMPTED DUPE");
		}

		save.setOwner(owner);
		save.setLogin(p.readLong(), p.readLong());
		save.setTotals(p.readShort(), p.readShort());
		save.setLocation(p.readShort(), p.readShort());
		save.setFatigue(p.readShort());
		save.setAppearance(p.readByte(), p.readByte(), p.readByte(),
				p.readByte(), p.readByte(), p.readByte(), p.readByte() == 1,
				p.readLong());
		save.setCombatStyle(p.readByte());

		for (int i = 0; i < 18; i++) {
			save.setStat(i, p.readLong(), p.readShort());
		}

		int invCount = p.readShort();
		save.clearInvItems();
		for (int i = 0; i < invCount; i++) {
			save.addInvItem(p.readShort(), p.readInt(), p.readByte() == 1);
		}

		int bnkCount = p.readShort();
		save.clearBankItems();
		for (int i = 0; i < bnkCount; i++) {
			save.addBankItem(p.readShort(), p.readInt());
		}

		save.setQuestPoints(p.readShort());
		int qstCount = p.readShort();
		for (int i = 0; i < qstCount; i++)
			save.setQuestStage(p.readShort(), p.readShort());

		save.setEventCD(p.readLong());

		save.setLastUpdate(System.currentTimeMillis());
		if (!save.save()) { // we shouldnt always save right away
			System.out.println("Error saving: " + save.getUsername());
		}
	}

}
