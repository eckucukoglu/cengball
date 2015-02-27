package rare;

import common.Ball;
import common.Player;

/**
 * This class represents the moments of the match. Each percept is a snapshot of the current situation of the match.
 *
 */
public class Percept {
	
	private Ball ball;
	private Player[] players;
	private int team1Score, team2Score;
	private Highlight perceptHighlight;
    
    public Percept() {
    	this.ball = new Ball();
    	this.team1Score = 0;
    	this.team2Score = 0;
    }
    
    /**
     * Constructor for the Percept object.
     * @param b the ball
     * @param players list of all players.
     * @param team1Score number of the goals scored by the home team.
     * @param team2Score number of the goals scored by the away team.
     * @param highlight a highlight for this percept.
     */
    public Percept(Ball b, Player[] players, int team1Score, int team2Score, Highlight highlight) {
    	this.ball = b;
    	this.players = players;
    	this.team1Score = team1Score;
    	this.team2Score = team2Score;
    	this.perceptHighlight = highlight;
    }
    
    public Percept(Ball b) {
    	this.ball = b;
    }
    
    /**
     * Gets the ball.
     * @return ball object.
     */
    public Ball getBall() {
    	return this.ball;
    }
    
    
    /**
     * Gets the score of the home team.
     * @return number of goals scored by the home team.
     */
    public int getTeam1Score() {
    	return this.team1Score;
    }
    
    /**
     * Gets the score of the away team.
     * @return number of goals scored by the away team.
     */
    public int getTeam2Score() {
    	return this.team2Score;
    }

    /** Sets the number of goals scored for the home team.
     * @param team1Score the number to be set.
     */
    public void setTeam1Score(int team1Score) {
    	this.team1Score = team1Score;
    }
    
    /** Sets the number of goals scored for the away team.
     * @param team2Score the number to be set.
     */
    public void setTeam2Score(int team2Score) {
    	this.team2Score = team2Score;
    }
    
    /**
     * Gets the highlight for the percept.
     * @return the highlight of this percept.
     */
    public Highlight getHighlight() {
    	return this.perceptHighlight;
    }
    
    /**
     * Sets the highlight for the percept.
     * @param newHighlight the highlight object to be set.
     */
    public void setHighlight(Highlight newHighlight) {
    	this.perceptHighlight = newHighlight;
    }

	/**
	 * Gets the player list of the percept.
	 * @return an array of players.
	 */
	public Player[] getPlayers() {
		return players;
	}

	/**
	 * Sets the player list for the percept.
	 * @param players an array of players to be set to the percept.
	 */
	public void setPlayers(Player[] players) {
		this.players = players;
	}
}
