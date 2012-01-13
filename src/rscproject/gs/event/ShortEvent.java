package rscproject.gs.event;

import rscproject.gs.model.Player;

public abstract class ShortEvent extends SingleEvent {

	public ShortEvent(Player owner) {
		super(owner, 1500);
	}

	public abstract void action();

}