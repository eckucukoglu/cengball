package rare;

import java.awt.geom.Point2D;

import utils.Utils;

/**
 * This class represents a vector in 2D.
 *
 */
public class vector2D {
	
    private double x;
    private double y;
    
    /**
     * Constructor of vector2D class.
     */
    public vector2D() {
    	this.x = 1;
    	this.y = 1;
    }
    
    /**
     * Constructor of vector2D class specifying the x and y values of the vector.
     * @param x	the value in x axis.
     * @param y	the value in y axis.
     */
    public vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the value in x axis.
	 * @return	the value in x axis.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets the value in x axis.
	 * @param x	the value in x axis to be set.
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Gets the value in y axis.
	 * @return	the value in y axis.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the value in y axis.
	 * @param y	the value in y axis to be set.
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public vector2D clone() {
		vector2D newVector2D = new vector2D (this.x, this.y);
		return newVector2D;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "X: " + Utils.round(this.x, 4) + " - Y: " + Utils.round(this.y, 4);
	}
	
	/**
	 * Gets the magnitude of the vector.
	 * @return	magnitude of the vector.
	 */
	public double getMagnitude() {
		return Math.sqrt(y*y + x*x);
	}
	
	/**
	 * Gets the angle of the vector.
	 * @return	the angle of the vector.
	 */
	public double getAngle() {
		return Math.atan2(y, x);
	}
	
	/**
	 * Gets the positive value of the angle of the vector.
	 * @return	positive value of the angle of the vector.
	 */
	public double getPosAngle() {
		double value = Math.atan2(y, x);
		if ( value < 0 ) {
			value += Math.PI * 2;
		}
		return value;
	}
	
	/**
	 * Multiplies the vector with the given constant.
	 * @param k	the constant to multiply the vector.
	 * @return	multiplied vector
	 */
	public vector2D multiply(double k) {
		return new vector2D(x*k, y*k);
	}
	
	/**
	 * Adds the vector with the given vector.
	 * @param v	the vector to be added.
	 * @return	result vector.
	 */
	public vector2D add(vector2D v) {
		return new vector2D(x + v.getX(), y + v.getY());
	}
	
	/**
	 * Subtracts the given vector from this one.
	 * @param v	the vector to be subtracted.
	 * @return	result vector.
	 */
	public vector2D subtract(vector2D v) {
		return new vector2D(x - v.getX(), y - v.getY());
	}
	
	/**
	 * Normalizes the vector.
	 * @return	normalized vector.
	 */
	public vector2D normalize() {
		double magnitude = this.getMagnitude();
		if ( magnitude != 0 ) {
			return new vector2D(x / magnitude, y / magnitude);
		}
		else {
			return new vector2D(0,0);
		}
	}
	
	/**
	 * Calculates the dot product of the vector with the given vector.
	 * @param v	given vector.
	 * @return	result value.
	 */
	public double dot(vector2D v) {
		return x * v.getX() + y * v.getY();
	}
	
	/**
	 * Calculates the distance between two vectors.
	 * @param vec	other vector.
	 * @return	result value.
	 */
	public double measureDistance (vector2D vec) {
		double retVal = Math.sqrt(
				(this.getX() - vec.getX()) * (this.getX() - vec.getX()) + 
				(this.getY() - vec.getY()) * (this.getY() - vec.getY()));
		return retVal;
	}
	
	/**
	 * Transforms this vector to a Point2D.
	 * @return	vector as a Point2D.
	 */
	public Point2D.Double toPoint() {
		return new Point2D.Double(x, y);
	}
	
	/**
	 * Calculates the angle between two vector.
	 * @param p1	first vector.
	 * @param p2	second vector.
	 * @return	result angle.
	 */
	public static double calculateAngle(vector2D p1, vector2D p2) {
		return Math.atan2(p1.getY()-p2.getY(), p1.getX()-p2.getX());
	}
}
