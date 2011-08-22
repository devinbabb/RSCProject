package rscproject.gs.event;

import rscproject.gs.model.Player;

public abstract class SingleEvent extends DelayedEvent {

    public SingleEvent(Player owner, int delay) {
        super(owner, delay);
    }

    public SingleEvent(Player owner, int delay, Object[] arg) {
        super(owner, delay, arg);
    }

    public abstract void action();

    public void run() {
        action();
        super.matchRunning = false;
    }

}