package rare;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import common.MovingObject;

/**
 * This class represents the pitch the match is played on.
 *
 */
public class Pitch {
	private Dimension dimension;
	private int leftGoalPostID, rightGoalPostID;
	
	public static final double DEFAULT_RADIUS = 9.15;
	public static final double DEFAULT_CORNER_RADIUS = 1.0;
	public static final double DEFAULT_DOT_RADIUS = 0.2;
	public static final double DEFAULT_POST_HEIGHT = 7.32;
	public static final double DEFAULT_POST_WIDTH = 2.0;
	public static final double DEFAULT_GOAL_AREA_LINE_WIDTH = 5.5;
	public static final double DEFAULT_GOAL_AREA_LINE_HEIGHT = 18.32; // standardGoalAreaLineW * 2 + standardPostH;
	public static final double DEFAULT_PENALTY_AREA_WIDTH = 16.5;
	public static final double DEFAULT_PENALTY_AREA_HEIGHT = 33.0; // standardPenaltyAreaW * 2;
	public static final double DEFAULT_DOT_DISTANCE = 11.0;
	public static final double DEFAULT_PLAYER_RADIUS = 1.0;
	public static final double DEFAULT_BALL_RADIUS = 0.5;
	public static final double DEFAULT_VIEW_ANGLE = Math.PI / 2.0;
	
	public static final double DEFAULT_FRICTION = 1;
	public static final double DEFAULT_PATTERN_LINE_COUNT = 16.0;
	
	/**
	 * Default constructor for the Pitch class.
	 * Dimensions are of Santiago Bernabeu.
	 */
	public Pitch () {
		// Dimensions of Santiago Bernabeu
		dimension = new Dimension(108, 72);
	}
	
	/**
	 * Constructor for the Pitch class.
	 * @param w width of the field. 
	 * @param h height of the field.
	 * @param leftGoalId id of the goal at the left side of the field.
	 * @param rightGoalId id of the goal at the right side of the field.
	 */
	public Pitch (int w, int h, int leftGoalId, int rightGoalId) {
		this.dimension = new Dimension(w, h);
		this.leftGoalPostID = leftGoalId;
		this.rightGoalPostID = rightGoalId;
	}
	
	/**
	 * Gets the width of the pitch.
	 * @return width of the pitch.
	 */
	public double getPitchWidth() {
		return dimension.width;
	}
	
	/**
	 * Gets the heigth of the field.
	 * @return height of the field.
	 */
	public double getPitchHeight() {
		return dimension.height;
	}
	
	/**
	 * Gets the boundaries of the field. 
	 * @return an object representing the dimensions of the pitch.
	 */
	public Rectangle2D.Double getBounds() {
		return new Rectangle2D.Double(0, 0, dimension.width, dimension.height);
	}
	
	/**
	 * Calculates the ratio that the pitch extents should be multiplied to fit the screen.
	 * @param windowWidth	the width of the window.
	 * @param windowHeight	the height of the window.
	 * @return	the ratio to multiply the pitch dimensions.
	 */
	public double calculateRatio (int windowWidth, int windowHeight) {
		double widthRatio = ( ((double) windowWidth - (10 + DEFAULT_POST_WIDTH*2 ) ) / getPitchWidth() );
		double heightRatio = ( ((double) windowHeight - 10) / getPitchHeight() );

		return Math.min(widthRatio, heightRatio);
	}
	
	/**
	 * Checks if an object is with in the boundaries of the field.
	 * @param object the object to be checked.
	 * @return true if the object is in the field, false otherwise.
	 */
	public boolean contains(vector2D position) {
		Rectangle2D.Double bounds = new Rectangle2D.Double(this.getBounds().getX(), this.getBounds().getY(),
				this.getBounds().getWidth() + 0.1, this.getBounds().getHeight() + 0.1 );
		if (  bounds.contains( position.toPoint() ) ) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if a team scored.
	 * @param object the object to the be scored. (Usually the ball.)
	 * @param teamID the team which scores.
	 * @return true if the team scored, false otherwise.
	 */
	public boolean isScored(MovingObject object, int teamID) {
		if ( teamID == leftGoalPostID ) {
			double currentX = object.getPosition().getX();
			double currentY = object.getPosition().getY();
			if ( this.getGoalPost(rightGoalPostID).contains(new Point2D.Double(currentX, currentY))) {
				return true;
			}
		}
		else if ( teamID == rightGoalPostID ) {
			double currentX = object.getPosition().getX();
			double currentY = object.getPosition().getY();
			if ( this.getGoalPost(leftGoalPostID).contains(new Point2D.Double(currentX, currentY))) {
				return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Pitch clone() {
		return new Pitch(dimension.width, dimension.height, this.leftGoalPostID, this.rightGoalPostID);
	}
	
	/**
	 * Gets the kickoff position of a team.
	 * @param teamID id of the team.
	 * @return kickoff position of the team with the given id.
	 */
	public vector2D getKickOffPosition(int teamID) {
		double angle = Math.PI / 3;
		vector2D kickOffPosition = null;
		if ( teamID == leftGoalPostID ) {
			double newX = dimension.width / 2.0 - (Pitch.DEFAULT_BALL_RADIUS + Pitch.DEFAULT_PLAYER_RADIUS) * Math.cos(angle);
			double newY = dimension.height / 2.0 - (Pitch.DEFAULT_BALL_RADIUS + Pitch.DEFAULT_PLAYER_RADIUS) * Math.sin(angle);
			kickOffPosition = new vector2D(newX, newY);
		}
		else if ( teamID == rightGoalPostID ) {
			double newX = dimension.width / 2.0 + (Pitch.DEFAULT_BALL_RADIUS + Pitch.DEFAULT_PLAYER_RADIUS) * Math.cos(angle);
			double newY = dimension.height / 2.0 + (Pitch.DEFAULT_BALL_RADIUS + Pitch.DEFAULT_PLAYER_RADIUS) * Math.sin(angle);
			kickOffPosition = new vector2D(newX, newY);
		}
		return kickOffPosition;
	}
	
	/**
	 * Gets the half court of the team with the given team ID.
	 * @param teamID	team ID of the team.
	 * @return	the half court of the team.
	 */
	public Rectangle2D.Double getHalfCourt(int teamID) {
		if ( teamID == leftGoalPostID ) {
			return new Rectangle2D.Double(0, 0, dimension.width / 2.0, dimension.height);
		}
		else if ( teamID == rightGoalPostID ) {
			return new Rectangle2D.Double(dimension.width / 2.0, 0, dimension.width / 2.0, dimension.height);
		}
		return null;
	}
	
	/**
	 * Gets the goal post of the team with the given team ID.
	 * @param teamID	team ID of the team.
	 * @return	the goal post of the team.
	 */
	public Rectangle2D.Double getGoalPost(int teamID) {
		if ( teamID == leftGoalPostID ) {
			return new Rectangle2D.Double(-DEFAULT_POST_WIDTH, (dimension.height - DEFAULT_POST_HEIGHT) / 2.0, DEFAULT_POST_WIDTH, DEFAULT_POST_HEIGHT);
		}
		else if ( teamID == rightGoalPostID ) {
			return new Rectangle2D.Double(dimension.getWidth(), (dimension.height - DEFAULT_POST_HEIGHT) / 2.0, DEFAULT_POST_WIDTH, DEFAULT_POST_HEIGHT);
		}
		
		return null;
	}

	/**
	 * Gets the goal post of the opponent team.
	 * @param teamID	team ID of the own team.
	 * @return	goal post of the opponent team.
	 */
	public Rectangle2D.Double getOtherGoalPost(int teamID) {
		if ( teamID == leftGoalPostID ) {
			return new Rectangle2D.Double(dimension.getWidth(), (dimension.height - DEFAULT_POST_HEIGHT) / 2.0, DEFAULT_POST_WIDTH, DEFAULT_POST_HEIGHT);
		}
		else if ( teamID == rightGoalPostID ) {
			return new Rectangle2D.Double(-DEFAULT_POST_WIDTH, (dimension.height - DEFAULT_POST_HEIGHT) / 2.0, DEFAULT_POST_WIDTH, DEFAULT_POST_HEIGHT);
		}
		return null;
	}
	
	/**
	 * Sets ids of the goal posts.
	 * @param left id of the left goal post.
	 * @param right id of the right goal post.
	 */
	public void setGoalPostIDs(int left, int right) {
		this.leftGoalPostID = left;
		this.rightGoalPostID = right;
	}
	
	/**
	 * Gets the dimensions of the pitch.
	 * @return dimensions of the pitch.
	 */
	public Rectangle2D.Double getPitch() {
		return new Rectangle2D.Double(0, 0, dimension.getWidth(), dimension.getHeight());
	}
}
