package rare;

import java.util.HashMap;

/**
 * Statistics class to show statistical information about running match.
 * Use player's ID as a parameter to use getter functions 
 * which return statistics of a given player.
 */
public class Statistic {
	// Players' pass trials
	private HashMap<Integer, Integer> passTry;
	// Players' successful passes
	private HashMap<Integer, Integer> passSuccess;
	// If players try to pass, then set pass control value 
	private HashMap<Integer, Boolean> passControl;
	// Players' shoot trials
	private HashMap<Integer, Integer> shootTry;
	// Players' tackle trials
	private HashMap<Integer, Integer> trapTry;
	// Players' successful traps
	private HashMap<Integer, Integer> trapSuccess;
	// How many turns does the players have the ball
	private HashMap<Integer, Integer> hasBall;
	// Total distances players covered during match
	private HashMap<Integer, Double> totalDistance;
	// Last distances players covered during last turn
	// These values are needed for fatigue calculations
	// It is being set when totalDistance calculated
	private HashMap<Integer, Double> lastDistance;
	// Players' last positions
	// It is being set when totalDistance calculated
	private HashMap<Integer, vector2D> playerPosition;
	
	/**
	 * Constructor for class.
	 * Each hash map use player's ID as a key, and his statistics as a value.
	 */
	public Statistic () {
		this.setPassTry(new HashMap<Integer, Integer>());
		this.setPassSuccess(new HashMap<Integer, Integer>());
		this.setTrapSuccess(new HashMap<Integer, Integer>());
		this.setPassControl(new HashMap<Integer, Boolean>());
		this.setShootTry(new HashMap<Integer, Integer>());
		this.setTrapTry(new HashMap<Integer, Integer>());
		this.setHasBall(new HashMap<Integer, Integer>());
		this.setTotalDistance(new HashMap<Integer, Double>());
		this.setPlayerPosition(new HashMap<Integer, vector2D>());
		this.setLastDistance(new HashMap<Integer, Double>());
	}
	
	/**
	 * Initialize all players statistics to zero.
	 * @param playerIDs
	 * @param positionDatas
	 * @return
	 */
	public boolean init (int[] playerIDs, vector2D[] positionDatas) {
		for (int i = 0; i < playerIDs.length; ++i) {
			passTry.put(playerIDs[i], 0);
			passSuccess.put(playerIDs[i], 0);
			trapSuccess.put(playerIDs[i], 0);
			passControl.put(playerIDs[i], false);
			shootTry.put(playerIDs[i], 0);
			trapTry.put(playerIDs[i], 0);
			hasBall.put(playerIDs[i], 0);
			totalDistance.put(playerIDs[i], 0.0);
			lastDistance.put(playerIDs[i], 0.0);
			playerPosition.put(playerIDs[i], positionDatas[i]);
		}
		return true;
	}
	
	/**
	 * These inc_ functions increase given player's statistics.
	 * @param playerID id of the player.
	 */
	public void incPassTry (int playerID) {
		this.passTry.put(playerID, this.passTry.get(playerID) + 1);
	}
	
	/**
	 * These inc_ functions increase given player's statistics.
	 * @param playerID id of the player.
	 */
	public void incPassSuccess (int playerID) {
		this.passSuccess.put(playerID, this.passSuccess.get(playerID) + 1);
	}
	
	/**
	 * These inc_ functions increase given player's statistics.
	 * @param playerID id of the player.
	 */
	public void incTrapSuccess (int playerID) {
		this.trapSuccess.put(playerID, this.trapSuccess.get(playerID) + 1);
	}
	
	/**
	 * These inc_ functions increase given player's statistics.
	 * @param playerID id of the player.
	 */
	public void incShootTry (int playerID) {
		this.shootTry.put(playerID, this.shootTry.get(playerID) + 1);
	}
	
	/**
	 * These inc_ functions increase given player's statistics.
	 * @param playerID id of the player.
	 */
	public void incTrapTry (int playerID) {
		this.trapTry.put(playerID, this.trapTry.get(playerID) + 1);
	}
	
	/**
	 * These inc_ functions increase given player's statistics.
	 * @param playerID id of the player.
	 */
	public void incHasBall (int playerID) {
		this.hasBall.put(playerID, this.hasBall.get(playerID) + 1);
	}
	
	/**
	 * These inc_ functions increase given player's statistics.
	 * @param playerID id of the player.
	 * @param playerPosition 
	 */
	public void incTotalDistance (int playerID, vector2D playerPosition) {
		// Calculate the distance player covered
		double distance = Math.abs(playerPosition.measureDistance(this.getPlayerPosition(playerID)));
		// Add total distance to new covered distance
		this.totalDistance.put(playerID, this.totalDistance.get(playerID) + distance);
		// Save last distance for fatigue calculation
		this.lastDistance.put(playerID, distance);
		// Set player's new position
		this.setPlayerPosition(playerID, playerPosition);
	}
	
	public int getPassTry(int playerID) {
		return this.passTry.get(playerID);
	}

	private void setPassTry(HashMap<Integer, Integer> passTry) {
		this.passTry = passTry;
	}

	public int getPassSuccess(int playerID) {
		return this.passSuccess.get(playerID);
	}
	
	public int getTrapSuccess(int playerID) {
		return this.trapSuccess.get(playerID);
	}

	private void setPassSuccess(HashMap<Integer, Integer> passSuccess) {
		this.passSuccess = passSuccess;
	}
	
	private void setTrapSuccess(HashMap<Integer, Integer> trapSuccess) {
		this.trapSuccess = trapSuccess;
	}
	
	public boolean getPassControl(int playerID) {
		return this.passControl.get(playerID);
	}
	
	private void setPassControl(HashMap<Integer, Boolean> passControl) {
		this.passControl = passControl;
	}
	
	public void setPassControl(int playerID, boolean passTry) {
		this.passControl.put(playerID, passTry);
	}
	
	public int getShootTry(int playerID) {
		return this.shootTry.get(playerID);
	}

	private void setShootTry(HashMap<Integer, Integer> shootTry) {
		this.shootTry = shootTry;
	}

	public int getTrapTry(int playerID) {
		return this.trapTry.get(playerID);
	}

	private void setTrapTry(HashMap<Integer, Integer> trapTry) {
		this.trapTry = trapTry;
	}

	public int getHasBall(int playerID) {
		return this.hasBall.get(playerID);
	}

	private void setHasBall(HashMap<Integer, Integer> hasBall) {
		this.hasBall = hasBall;
	}

	public double getTotalDistance(int playerID) {
		return this.totalDistance.get(playerID);
	}

	private void setTotalDistance(HashMap<Integer, Double> totalDistance) {
		this.totalDistance = totalDistance;
	}

	public vector2D getPlayerPosition(int playerID) {
		return this.playerPosition.get(playerID);
	}

	private void setPlayerPosition(HashMap<Integer, vector2D> playerPosition) {
		this.playerPosition = playerPosition;
	}
	
	private void setPlayerPosition(int playerID, vector2D playerPosition) {
		this.playerPosition.put(playerID, playerPosition);
	}

	public double getLastDistance(int playerID) {
		return this.lastDistance.get(playerID);
	}

	private void setLastDistance(HashMap<Integer, Double> lastDistance) {
		this.lastDistance = lastDistance;
	}
}
