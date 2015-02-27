package rare;

import common.CommonEnums.ActionType;

/**
 * This class represents an action a player makes during the match.
 *
 */
public class PlayerAction {
	private ActionType type;
	private vector2D intentVector;
	
	/**
	 * Constructor for the PlayerAction class.
	 * @param type type of the action performed by a player.
	 * @param where place where the action is to be performed.
	 */
	public PlayerAction(ActionType type, vector2D where) {
		this.type = type;
		this.intentVector = where;
	}
	
	/**
	 * Constructor for the PlayerActions which doesn't require a position to be performed.
	 * @param type
	 */
	public PlayerAction(ActionType type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public PlayerAction clone() {
		// Check enumeration copy process
		PlayerAction newPlayerAction = null;
		if ( this.intentVector != null ) {
			newPlayerAction = new PlayerAction (this.type, new vector2D(this.intentVector.getX(), this.getIntentVector().getY()));
		}
		else {
			newPlayerAction = new PlayerAction (this.type, null);
		}
		return newPlayerAction;
	}
	
	/**
	 * Gets the type of the action.
	 * @return type of the action.
	 */
	public ActionType getType() {
		return type;
	}
	/**
	 * Sets the type of the action.
	 * @param type type of the action to be set.
	 */
	public void setType(ActionType type) {
		this.type = type;
	}

	/**
	 * Gets the position vector of this action.
	 * @return postion vector of this action.
	 */
	public vector2D getIntentVector() {
		return intentVector;
	}

	/**
	 * Sets the positon vector of this action.
	 * @param intentVector the position vector to be set.
	 */
	public void setIntentVector(vector2D intentVector) {
		this.intentVector = intentVector;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String type = "", intentV = "";
		if ( this.type != null ) {
			if ( this.type == ActionType.NONE ) {
				type = "NONE";
			}
			else if ( this.type == ActionType.MOVE ) {
				type = "MOVE";
			}
			else if ( this.type == ActionType.PASS ) {
				type = "PASS";
			}
			else if ( this.type == ActionType.SHOOT ) {
				type = "SHOOT";
			}
			else if ( this.type == ActionType.TRAP ) {
				type = "TRAP";
			}
			else if ( this.type == ActionType.STOP ) {
				type = "STOP";
			}
		}
		if ( this.intentVector != null ) {
			intentV = intentVector.toString();
		}
		return type + " - " + intentV;
	}
	
}
