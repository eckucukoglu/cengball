package common;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

import rare.Pitch;
import rare.TeamInfo;
import rare.vector2D;
import utils.CengBallException;


/**
 * This class represent a team in the match.
 *
 */
public class Team {	
	protected MatchAssets assets;
	private String name;
	private Color color1, color2;
	private int id;
	private static int count = 0;
	
	/**
	 * Constructor of the Team class 
	 * @param name name of the team.
	 * @param color1 color of the kit.
	 * @param color2 color of the kit.
	 */
	public Team(String name, Color color1, Color color2) {
		this.name = name;
		this.color1 = color1;
		this.color2 = color2;
		this.id = count;
    	count++;
	}
	
	/**
	 * Default constructor for Team class.Name of the team is "Default Team", colors are black and white.
	 * 
	 */
	public Team() {
		this.name = "Default Team";
		this.color1 = new Color(0,0,0);
		this.color2 = new Color(255,255,255);
		this.id = count;
		count++;
	}
	
	
	// Function to be overriden, default is average distribution
	/**
	 * Function to be overriden, default is average distribution
	 * @param numberOfPlayers
	 * @param halfCourt
	 * @return
	 * @throws CengBallException
	 */
	public Player[] assembleTeam(int numberOfPlayers, Rectangle2D.Double halfCourt) {
		Player[] myPlayers = new Player[numberOfPlayers];
		double pDistance1 = (halfCourt.getHeight() - (2*numberOfPlayers/5) * Pitch.DEFAULT_PLAYER_RADIUS * 2) / ((2*numberOfPlayers/5) + 1 );
		int remainingPlayers = numberOfPlayers - 2*(2*numberOfPlayers/5);
		double pDistance2 = (halfCourt.getHeight() - (remainingPlayers) * Pitch.DEFAULT_PLAYER_RADIUS * 2) / (remainingPlayers + 1 );
		double pDistance3 = (halfCourt.getWidth() - numberOfPlayers*Pitch.DEFAULT_PLAYER_RADIUS * 2) / (numberOfPlayers+1);
		int playerIndex = 0;
		if ( halfCourt.getX() == 0 ) {
			for (int i = 0; i < (2*numberOfPlayers/5); i++) {
				myPlayers[playerIndex] = new Player("Player"+(playerIndex+1), playerIndex+1, this.getId(), new vector2D(halfCourt.getWidth() - (pDistance3),pDistance1*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						10,10,10,10,10,10);
				playerIndex++;
			}
			for (int i = 0; i < (2*numberOfPlayers/5); i++) {
				myPlayers[playerIndex] = new Player("Player"+(playerIndex+1), playerIndex+1, this.getId(), new vector2D(halfCourt.getWidth() - (pDistance3*3),pDistance1*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						10,10,10,10,10,10);
				playerIndex++;
			}
			for (int i = 0; i < remainingPlayers; i++) {
				myPlayers[playerIndex] = new Player("Player"+(playerIndex+1), playerIndex+1, this.getId(), new vector2D(halfCourt.getWidth() - (pDistance3*2),pDistance2*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						10,10,10,10,10,10);
				playerIndex++;
			}
		}
		else {
			for (int i = 0; i < (2*numberOfPlayers/5); i++) {
				myPlayers[playerIndex] = new Player("Player"+(playerIndex+1), playerIndex+1, this.getId(), new vector2D(halfCourt.getX() + (pDistance3),pDistance1*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						10,10,10,10,10,10);
				playerIndex++;
			}
			for (int i = 0; i < (2*numberOfPlayers/5); i++) {
				myPlayers[playerIndex] = new Player("Player"+(playerIndex+1), playerIndex+1, this.getId(), new vector2D(halfCourt.getX() + (pDistance3*3),pDistance1*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						10,10,10,10,10,10);
				playerIndex++;
			}
			for (int i = 0; i < remainingPlayers; i++) {
				myPlayers[playerIndex] = new Player("Player"+(playerIndex+1), playerIndex+1, this.getId(), new vector2D(halfCourt.getX() + (pDistance3*2),pDistance2*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						10,10,10,10,10,10);
				playerIndex++;
			}
		}
		return myPlayers;
	}
	
	// Function to be overriden
	/**
	 * Function to be overriden, the one which simulator calls periodically.
	 */
	public void move() {}
	
	/**
	 * Gets the team's players.
	 * @return an array of your players.
	 */
	public final Player[] getMyPlayers() {
		return assets.getPlayerList(this.id);
	}
	
	/**
	 * Synchronizes team's assets.
	 * @param assets the assets to be synchronized.
	 */
	public final void sync(MatchAssets assets) {
		this.assets = assets;
	}

	/**
	 * Gets the team's id.
	 * @return the id of the team.
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * Gets the information about the team.(id, name and colors).
	 * @return an object containing the team information.
	 */
	public final TeamInfo getTeamInfo() {
		return new TeamInfo(this.id, this.name, this.color1, this.color2);
	}

	
	/**
	 * Swaps team's color1 with color2.
	 */
	public void swapColors() {
		Color temp = color1;
		color1 = color2;
		color2 = temp;
	}

}
