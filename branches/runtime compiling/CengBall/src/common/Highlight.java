package common;

import simulator.PlayerInfo;

/*
 * A class for highlights in a match. 
 */
public class Highlight {
	private PlayerAction action; // Main Action
	private PlayerInfo actor1; // Actor that starts the action
	private PlayerInfo actor2; // Actor that is affected by the action
	
	public Highlight() {
		
	}
	
	public Highlight(PlayerAction action, PlayerInfo actor1, PlayerInfo actor2) {
		super();
		this.action = action;
		this.actor1 = actor1;
		this.actor2 = actor2;
	}

	public PlayerAction getAction() {
		return action;
	}
	public void setAction(PlayerAction action) {
		this.action = action;
	}
	public PlayerInfo getActor1() {
		return actor1;
	}
	public void setActor1(PlayerInfo actor1) {
		this.actor1 = actor1;
	}
	public PlayerInfo getActor2() {
		return actor2;
	}
	public void setActor2(PlayerInfo actor2) {
		this.actor2 = actor2;
	}
	
	
}
