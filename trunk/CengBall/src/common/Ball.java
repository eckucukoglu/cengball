package common;

import rare.Pitch;
import rare.vector2D;
import utils.Enums;

/**
 * This class represents the ball in a match.
 *
 */
public class Ball extends MovingObject {
	private transient Player ownerPlayer;
	private transient static final int trapDistance = CommonEnums.TRAP_DISTANCE;
	
	/**
	 * Default constructor of the ball class.
	 */
	public Ball() {
		super(new vector2D(0.0, 0.0), new vector2D(0.0, 0.0), Pitch.DEFAULT_BALL_RADIUS, Enums.ObjectType.BALL);
		this.ownerPlayer = null;
	}
	
	/**
	 * Constructor of the ball class specifying the position and the speed.
	 * @param position	the position of the ball object.
	 * @param speed	the speed of the ball object.
	 */
	public Ball(vector2D position, vector2D speed) {
		super(position, speed, Pitch.DEFAULT_BALL_RADIUS, Enums.ObjectType.BALL);
		this.positionVector = position.clone();
		this.speedVector = speed.clone();
		this.ownerPlayer = null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Ball clone() {
		vector2D newPosition = this.positionVector.clone();
		vector2D newSpeed = this.speedVector.clone();
		Ball newBall = new Ball (newPosition, newSpeed);
		if ( this.ownerPlayer != null ) {
			newBall.setOwner(this.ownerPlayer.clone());	// This is overriden in MatchAssets.clone() function
		}
		
		return newBall;
	}
	
	/**
	 * Puts the ball object in the middle of the given pitch.
	 * @param pitch	the pitch object that the ball is initialized to.
	 */
	public void initialize(Pitch pitch) {
		this.positionVector = new vector2D(Math.round( pitch.getPitchWidth()/2.0 ), Math.round( pitch.getPitchHeight()/2.0)) ;
		this.speedVector = new vector2D(0,0);
		this.setOwner(null);
	}
	
	/**
	 * Checks if the given player is the owner of the ball.
	 * @param player	player to be checked.
	 * @return	true if the given player is the owner, false otherwise.
	 */
	public boolean checkOwner(Player player) {
		if ( ownerPlayer != null && ownerPlayer.getId() == player.getId()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the owner of the ball object.
	 * @param player	the new owner of the ball object.
	 */
	public void setOwner (Player player) {
		this.ownerPlayer = player;
		this.syncWithOwner();
	}
	
	/**
	 * Gets the owner of the ball object. 
	 * @return the owner player of the ball object.
	 */
	public Player getOwner() {
		return ownerPlayer;
	}
	
	/**
	 * Gets the trap distance of the ball.
	 * @return	the trap distance
	 */
	public int getTrapDistance() {
		return trapDistance;
	}
	
	/**
	 * Synchronizes the ball object with its owner. The speed and the position of the ball
	 * is updated according to the owner's speed and position.
	 */
	public void syncWithOwner() {
		if ( ownerPlayer != null ) {
			this.speedVector = ownerPlayer.speedVector;
			double newX = ownerPlayer.getPosition().getX() + (this.radius + ownerPlayer.getRadius()) * Math.cos(ownerPlayer.getSpeedAngle());
			double newY = ownerPlayer.getPosition().getY() + (this.radius + ownerPlayer.getRadius()) * Math.sin(ownerPlayer.getSpeedAngle());
			this.positionVector = new vector2D(newX, newY);
		}
	}

}
