package rare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.CBLogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class holds general info about a match.
 *
 */
public class Metadata {
	private int matchLength;
	private TeamInfo team1;
	private TeamInfo team2;
	private Pitch pitch;
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();
	
	/**
	 * Constructor of the Metadata class.
	 * @param team1 one of the team objects in a match.
	 * @param team2 one of the team objects in a match.
	 * @param pitch the pitch the match is played on.
	 * @param matchLength length of the match. ( turnwise )
	 */
	public Metadata(TeamInfo team1, TeamInfo team2, Pitch pitch, int matchLength) {
		this.team1 = team1;
		this.team2 = team2;
		this.pitch = pitch;
		this.matchLength = matchLength;
	}
	
	/**
	 * Writes the metadata to the savefile.
	 * @param path path of the savefile.
	 */
	public void writeToFile(String path) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(new File(path)));
			String json = gson.toJson(this);
			writer.write(json);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "IOException : " + e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "IOException : " + e);
			}
		}
	}
	

	/**
	 * Gets the (home) team.
	 * @return a team object in the match.
	 */
	public TeamInfo getTeam1() {
		return team1;
	}

	/**
	 * Gets the (away) team.
	 * @return a team object in the match.
	 */
	public TeamInfo getTeam2() {
		return team2;
	}
	
	/**
	 * Gets the info for a team.
	 * @param teamID id which determines the team to be returned. 
	 * @return a team object.
	 */
	public TeamInfo getTeamInfo(int teamID) {
		if ( team1.getId() == teamID ) {
			return team1;
		}
		else if ( team2.getId() == teamID ) {
			return team2;
		}
		return null;
	}

	/**
	 * Gets the pitch object.
	 * @return a pitch object.
	 */
	public Pitch getPitch() {
		return pitch;
	}

	/**
	 * Sets the pitch object.
	 * @param pitch the pitch object to be set.
	 */
	public void setPitch(Pitch pitch) {
		this.pitch = pitch;
	}

	/**
	 * Gets match length.
	 * @return match length (turnwise)
	 */
	public int getMatchLength() {
		return matchLength;
	}

	/**
	 * Sets the match length.
	 * @param matchLength the match length to be set. (turnwise)
	 */
	public void setMatchLength(int matchLength) {
		this.matchLength = matchLength;
	} 
	
}
