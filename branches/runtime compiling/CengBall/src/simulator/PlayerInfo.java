package simulator;

import common.Position;

public class PlayerInfo {
	private int id;
	private Position position;
	
	public PlayerInfo(int id, Position position) {
		this.id = id;
		this.position = position;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public int getID() {
		return id;
	}
}
