package imports;
// Do not specify a package for the team class that is going to be imported.


import java.awt.Color;
import java.awt.geom.Rectangle2D;

import common.Player;
import common.Team;

public class MyTeam extends Team {
	// It is recommended that teams have their static variables declared as static fields.
	private static String teamName = "My Team Name";
	private static Color teamColor1 = new Color(0, 0, 0), teamColor2 = new Color(255, 255, 255);

	public MyTeam() {
		// The team color, team color 1 and team color 2 variables should be set in the constructor
		super(teamName, teamColor1, teamColor2);
	}
	
	@Override
	public Player[] assembleTeam(int numberOfPlayers, Rectangle2D.Double halfCourt) {
		// Creation of the players should be done in this function.
		// Place the players according to the given half court.
		// If the X value of the half court is zero, your team is placed the left side of the pitch,
		// right side otherwise.
		return null;
	}
	
	@Override
	public void move() {
		// Main actions of the players should be set in this function.
		// MatchAssets object of the team class will be synchronized before calling this function.
		// Evaluate the current situation of the game and act accordingly.
		// The simulator will check only the intentions of the players. Any other changes with the
		// player objects will be ignored. Therefore, calculate the actions that you want your players
		// to do and call the relative functions.
	}
}
