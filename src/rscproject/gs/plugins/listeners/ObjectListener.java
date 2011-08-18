package rscproject.gs.plugins.listeners;

import rscproject.gs.model.GameObject;
import rscproject.gs.model.Player;

/**
 * Used for Plugins.
 * 
 * @author Devin
 */
public interface ObjectListener {

    /**
     * When a user activates an in-game Object.
     * 
     * @param obj
     *            - the object model
     * @param player
     *            - the player model
     */
    public boolean onObjectAction(GameObject obj, String command, Player player);

    /**
     * When an object is removed from the game.
     * 
     * @param obj
     *            - the object model
     */
    public boolean onObjectRemoval(GameObject obj);

    /**
     * When an object is created in-game
     * 
     * @param obj
     *            - the new object model
     * @param player
     *            - the player model (might possibly be null)
     */
    public boolean onObjectCreation(GameObject obj, Player player);

    /**
     * 
     * @return - an integer array of object ID's that trigger these actions
     */
    public int[] getAssociatedIDS();

}
