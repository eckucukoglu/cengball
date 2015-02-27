package rare;

import utils.Enums.HighlightType;

import common.Player;


/**
 * This class represents a movement done by a player object during the game.
 *
 */
public class Highlight {
	private HighlightType actionType; // Main Action
	private Player actor; // Actor that starts the action
	
	public Highlight() {
		this.actionType = HighlightType.NONE;
		this.actor = null;
	}
	
	/**
	 * Constructor of the Highlight class
	 * @param actionType the action done by the player.
	 * @param actor the player who has done the action.
	 */
	public Highlight(HighlightType actionType, Player actor) {
		this.actionType = actionType;
		this.actor = actor;
	}

	/**
	 * Gets the actor of the highlight.
	 * @return the player who has done the action.
	 */
	public Player getActor() {
		return actor;
	}
	/**
	 * Sets the actor of the highlight.
	 * @param actor the player who has done the action.
	 */
	public void setActor(Player actor) {
		this.actor = actor;
	}

	/**
	 * Gets the type of the action of the highlight. 
	 * @return the type of the action.
	 */
	public HighlightType getActionType() {
		return actionType;
	}

	/**
	 * Sets the type of the action for the highlight.
	 * @param actionType the type of the action. 
	 */
	public void setActionType(HighlightType actionType) {
		this.actionType = actionType;
	}
	
	
}
