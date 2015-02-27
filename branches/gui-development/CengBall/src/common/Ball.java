package common;

public class Ball {
	
	private Position position;
	private double speedX, speedY;
	
	public Ball() {
		this.position = new Position(0.0, 0.0);
		this.speedX = 0.0;
		this.speedY = 0.0;
	}
	
	public Ball(Position p) {
		this.position = p;
	}
	
	public Position getPosition() {
		return this.position;
	}
}
