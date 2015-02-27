package simulator;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.CBLogger;
import common.Team;

/**
 * This class handles the move function of a team.
 *
 */
public class TeamRunner extends Thread {
	private Team team;
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();
	
	public TeamRunner(Team team) {
		this.team = team;
	}
	
	@Override
	public void run() {
		try {
			team.move();
		} catch ( Exception e ) {
			LOGGER.log(Level.WARNING, "Exception in move function of " + team.getTeamInfo().getName() + " : " + e);
		}
	}
}
