package rscproject.gs.core;

import rscproject.gs.Instance;
import rscproject.gs.event.DelayedEvent;
import rscproject.gs.model.Player;
import rscproject.gs.model.World;
import rscproject.gs.util.Logger;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Devin
 */

public final class DelayedEventHandler {
	private static World world = Instance.getWorld();
	private ArrayList<DelayedEvent> events = new ArrayList<DelayedEvent>();
	private ArrayList<DelayedEvent> toAdd = new ArrayList<DelayedEvent>();

	public DelayedEventHandler() {
		world.setDelayedEventHandler(this);
	}

	public void add(DelayedEvent event) {
		if (!events.contains(event)) {
			toAdd.add(event);
		}
	}

	public boolean contains(DelayedEvent event) {
		return events.contains(event);
	}

	public void doEvents() {
		try {
			if (toAdd.size() > 0) {
				events.addAll(toAdd);
				toAdd.clear();
			}

			Iterator<DelayedEvent> iterator = events.iterator();
			while (iterator.hasNext()) {
				DelayedEvent event = iterator.next();

				if (event == null) {
					iterator.remove();
					continue;
				}

				if (event.shouldRun()) {
					event.run();
					event.updateLastRun();
				}
				if (event.shouldRemove()) {
					iterator.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.println("Error @ doEvents(): " + e);
		}
	}

	public ArrayList<DelayedEvent> getEvents() {
		return events;
	}

	public void remove(DelayedEvent event) {
		events.remove(event);
	}

	public void removePlayersEvents(Player player) {
		try {
			Iterator<DelayedEvent> iterator = events.iterator();
			while (iterator.hasNext()) {
				DelayedEvent event = iterator.next();
				if (event.belongsTo(player)) {
					iterator.remove();
				}
			}
		} catch (Exception e) {
			Logger.println("Error @ removePlayer, IP address:"
					+ player.getCurrentIP() + " Name: " + player.getUsername());
			e.printStackTrace();
		}

	}
}
