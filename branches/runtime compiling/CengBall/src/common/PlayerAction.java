package common;

import utils.Enums.ActionType;

public class PlayerAction {
	private ActionType type;
	private double x,y;
	
	public PlayerAction() {
	}
	
	public PlayerAction(ActionType type, double x, double y) {
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public ActionType getType() {
		return type;
	}
	public void setType(ActionType type) {
		this.type = type;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	
}
