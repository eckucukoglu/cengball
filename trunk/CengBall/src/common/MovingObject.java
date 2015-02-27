package common;

import rare.vector2D;
import utils.Enums;
import utils.Enums.ObjectType;

/**
 * The core class for moving objects in the game.
 *
 */
public class MovingObject {
	protected vector2D positionVector, speedVector, directionVector;
	protected double radius;
	protected Enums.ObjectType type;
	
	/**
	 * Constructor of MovingObject class specifiying the position, speed, radius and the type
	 * @param position	position of the moving object.
	 * @param speed		speed of the moving object.
	 * @param radius	radius of the moving object.
	 * @param type		type of the moving object. ( BALL or PLAYER )
	 */
	MovingObject(vector2D position, vector2D speed, double radius, Enums.ObjectType type) {
		this.positionVector = position;
		this.speedVector = speed;
		this.directionVector = speed;
		this.radius = radius;
		this.type = type;
	}

	/**
	 * Gets the upper left corner position.
	 * @return	the position of the upper left corner of this moving object.
	 */
	public vector2D getUpperLeftCornerPosition() {
		return new vector2D(positionVector.getX() - radius, positionVector.getY() - radius);
	}
	
	/**
	 * Gets the position of the moving object.
	 * @return	the position of the moving object.
	 */
	public vector2D getPosition() {
		return positionVector;
	}

	/**
	 * Sets the position of the moving object.
	 * @param position	the position to be set.
	 */
	public void setPosition(vector2D position) {
		this.positionVector = position;
	}

	/**
	 * Gets the speed of the moving object.
	 * @return	the speed of the moving object.
	 */
	public vector2D getSpeed() {
		return speedVector;
	}

	/**
	 * Sets the speed of the moving object.
	 * @param speed	speed to be set.
	 */
	public void setSpeed(vector2D speed) {
		this.speedVector = speed;
		if ( speed.getX() != 0 || speed.getY() != 0 ) {
			this.directionVector = speed;
		}
	}
	
	/**
	 * Gets the speed angle.
	 * @return	speed angle of the moving object.
	 */
	public double getSpeedAngle() {
		return Math.atan2(speedVector.getY(), speedVector.getX() );
	}
	
	/**
	 * Moves the object with the given friction acceleration in specified time difference.
	 * @param frictionAcc	a constant number to create friction notion.
	 * @param deltaT		the time to move the object. ( in seconds )
	 */
	public void move(double frictionAcc, double deltaT) {
		double angle = this.getSpeedAngle(); // Find the angle in radians
		double deltaX = 0, deltaY = 0;
		double directionalFriction = frictionAcc * Math.cos(angle);
		double prevVx = speedVector.getX();
		speedVector.setX(prevVx - directionalFriction * deltaT);
		if ( speedVector.getX() * prevVx <= 0 ) {	// If the acceleration changes the direction, make it stop
			speedVector.setX(0);
		}
		deltaX = ((prevVx + speedVector.getX())/2.0) * deltaT;

		directionalFriction = frictionAcc * Math.sin(angle);
		double prevVy = speedVector.getY();
		speedVector.setY(prevVy - directionalFriction * deltaT);
		if ( speedVector.getY() * prevVy <= 0 ) {	// If the acceleration changes the direction, make it stop
			speedVector.setY(0);
		}
		deltaY = ((prevVy + speedVector.getY())/2.0) * deltaT;

		double newX = positionVector.getX() + deltaX;
		double newY = positionVector.getY() + deltaY;
		positionVector = new vector2D(newX, newY);
	}

	/**
	 * Gets the radius.
	 * @return	radius of the moving object.
	 */
	public double getRadius() {
		return radius;
	}

	
	/**
	 * Sets the radius.
	 * @param radius	radius to be set.
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Speed : " + speedVector.toString() + " - Position : " + positionVector.toString() + " - Radius : " + radius + " - Speed Angle : " + this.getSpeedAngle();
	}

	/**
	 * Gets the type.
	 * @return	type of the moving object.
	 */
	public ObjectType getType() {
		return type;
	}

	/**
	 * Sets the type of the moving object.
	 * @param type	type to be set.
	 */
	public void setType(ObjectType type) {
		this.type = type;
	}

	/**
	 * Sets the direction vector of the moving object.
	 * @param directionVector	direction vector to be set.
	 */
	public void setDirectionVector(vector2D directionVector) {
		this.directionVector = directionVector;
	}

	/**
	 * Gets the direction vector.
	 * @return	the direction vector of the moving object.
	 */
	public vector2D getDirectionVector() {
		return directionVector;
	}
}
