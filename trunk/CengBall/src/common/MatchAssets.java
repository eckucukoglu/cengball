package common;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import rare.Pitch;
import rare.vector2D;
import utils.Enums;
import utils.Utils;

/**
 * This class represents the physics of the game. It simulates movements and collisions.
 *
 */
public class MatchAssets {
	private Ball ball;
	private Pitch pitch;
	private HashMap<Integer, Player[]> players;
	private HashMap<Integer, Integer> scores;
	private int startingTeam;
	private double frictionAcceleration;
	private int fps;
	private int scoredTeamId;
	
	/**
	 * Constructor for MatchAssets specifying the ball and the pitch objects.
	 * @param ball	the ball object to be set.
	 * @param pitch	the pitch object to be set.
	 */
	public MatchAssets(Ball ball, Pitch pitch) {
		this.ball = ball;
		this.pitch = pitch;	
		this.frictionAcceleration = Pitch.DEFAULT_FRICTION;	
		this.fps = Enums.DEFAULT_FPS;
		this.players = new HashMap<Integer, Player[]>();
		this.scores = new HashMap<Integer, Integer>();
		this.scoredTeamId = -1;
	}
	
	/**
	 * Constructor for MatchAssets specifying the ball, the pitch, players and the scores.
	 * @param ball	the ball object to be set.
	 * @param pitch	the pitch object to be set.
	 * @param players the players to be set.
	 * @param scores	the scores to be set.
	 */
	public MatchAssets(Ball ball, Pitch pitch, HashMap<Integer, Player[]> players, HashMap<Integer, Integer> scores) {
		this.ball = ball;
		this.pitch = pitch;
		this.players = players;
		this.frictionAcceleration = Pitch.DEFAULT_FRICTION;
		this.fps = Enums.DEFAULT_FPS;
		this.players = players;
		this.scores = scores;
		this.scoredTeamId = -1;
	}
	
	/**
	 * Function to advance the assets for given number of percepts. This function simulates the
	 * physics in the game.
	 * @param numberOfPercepts	the number of percepts to advance.
	 * @return the advanced MatchAssets object.
	 */
	public MatchAssets advance(int numberOfPercepts) {
		MatchAssets advancedAssets = this.clone(-1);
		Integer[] teamIds = advancedAssets.getTeamIDs();
		advancedAssets.scoredTeamId = -1;
		for(int i = 0; i < numberOfPercepts; i++) {
			// Move ball
			if ( ball.getOwner() != null ) {
				advancedAssets.ball.syncWithOwner();
				advancedAssets.ball.move(0, 1.0/fps);
			}
			else {
				advancedAssets.ball.move(frictionAcceleration, 1.0/fps);
			}
			
			// Move Players
			for(int j = 0; j < teamIds.length; j++) {
				for(int p = 0; p < advancedAssets.getPlayers().get(teamIds[j]).length; p++) {
					advancedAssets.getPlayers().get(teamIds[j])[p].move(0, 1.0/fps);
				}
			}
						
			// Check if scored
			if ( advancedAssets.pitch.isScored(advancedAssets.ball, teamIds[0]) ) {
				advancedAssets.increaseScore(teamIds[0]);
				advancedAssets.startingTeam = teamIds[1];
				advancedAssets.scoredTeamId = teamIds[0];
			}
			else if ( advancedAssets.pitch.isScored(advancedAssets.ball, teamIds[1]) ) {
				advancedAssets.increaseScore(teamIds[1]);
				advancedAssets.startingTeam = teamIds[0];
				advancedAssets.scoredTeamId = teamIds[1];
			} 
						
			// Handle Ball - Rear Collisions
			while ( !advancedAssets.pitch.contains(advancedAssets.ball.getPosition()) && advancedAssets.scoredTeamId == -1 ) {
				handleCollisionWithRear(advancedAssets.ball);
			}
			
			// Handle Player - Rear Collisions
			for(int j = 0; j < teamIds.length; j++) {
				for(int p = 0; p < advancedAssets.getPlayers().get(teamIds[j]).length; p++) {
					while ( !advancedAssets.pitch.contains(advancedAssets.getPlayers().get(teamIds[j])[p].getPosition()) ) {
						handleCollisionWithRear(advancedAssets.getPlayers().get(teamIds[j])[p]);
					}
				}
			}
		}
		return advancedAssets;
	}
	
	/**
	 * Simulates a collision with the rear of the pitch. If the given object is the ball, then the collision
	 * is not elastic. If the given object is a player, then the speed of the player in the collision direction
	 * becomes zero.
	 * @param object	the object that collides with the rear of the pitch.
	 */
	private void handleCollisionWithRear(MovingObject object) {
		double excess = 0;
		if ( object.getPosition().getX() - object.getRadius() < 0 ) {
			if ( object.getType() == Enums.ObjectType.BALL ) {
				excess = Utils.round( (object.getPosition().getX()-object.getRadius()) * (-1) , 4);
				while(excess > pitch.getPitchWidth()) {
					excess -= pitch.getPitchWidth();
				}
				object.getPosition().setX(excess + object.getRadius());
				object.getSpeed().setX(object.getSpeed().getX() * (-1));
				object.setSpeed( object.getSpeed().multiply(Enums.DEFAULT_INELASTIC_COLLISION_LOSS) );
			}
			else if ( object.getType() == Enums.ObjectType.PLAYER ) {
				object.getPosition().setX(object.getRadius());
				object.getSpeed().setX(0);
			}
		}
		else if ( object.getPosition().getX() + object.getRadius() > pitch.getPitchWidth() ) {
			if ( object.getType() == Enums.ObjectType.BALL ) {
				excess = Utils.round( object.getPosition().getX() + object.getRadius() - pitch.getPitchWidth() , 4);
				while(excess > pitch.getPitchWidth()) {
					excess -= pitch.getPitchWidth();
				}
				object.getPosition().setX(pitch.getPitchWidth() - (excess + object.getRadius()) );
				object.getSpeed().setX(object.getSpeed().getX() * (-1));
				object.setSpeed( object.getSpeed().multiply(Enums.DEFAULT_INELASTIC_COLLISION_LOSS) );
			}
			else if ( object.getType() == Enums.ObjectType.PLAYER ) {
				object.getPosition().setX(pitch.getPitchWidth() - object.getRadius() );
				object.getSpeed().setX(0);
			}
		}
		else if ( object.getPosition().getY() - object.getRadius() < 0 ) {
			if ( object.getType() == Enums.ObjectType.BALL ) {
				excess = Utils.round( ( object.getPosition().getY() - object.getRadius() ) * (-1) , 4);
				while(excess > pitch.getPitchHeight()) {
					excess -= pitch.getPitchHeight();
				}
				object.getPosition().setY(excess + object.getRadius());
				object.getSpeed().setY(object.getSpeed().getY() * (-1));
				object.setSpeed( object.getSpeed().multiply(Enums.DEFAULT_INELASTIC_COLLISION_LOSS) );
			}
			else if ( object.getType() == Enums.ObjectType.PLAYER ) {
				object.getPosition().setY(object.getRadius());
				object.getSpeed().setY(0);
			}
		}
		else if ( object.getPosition().getY() + object.getRadius() > pitch.getPitchHeight() ) {
			if ( object.getType() == Enums.ObjectType.BALL ) {
				excess = Utils.round( object.getPosition().getY() + object.getRadius() - pitch.getPitchHeight() , 4);
				while(excess > pitch.getPitchHeight()) {
					excess -= pitch.getPitchHeight();
				}
				object.getPosition().setY(pitch.getPitchHeight() - (excess + object.getRadius()) );
				object.getSpeed().setY(object.getSpeed().getY() * (-1));
				object.setSpeed( object.getSpeed().multiply(Enums.DEFAULT_INELASTIC_COLLISION_LOSS) );
			}
			else if ( object.getType() == Enums.ObjectType.PLAYER ) {
				object.getPosition().setY(pitch.getPitchHeight() - object.getRadius());
				object.getSpeed().setY(0);
			}
		}
	}

	/**
	 * Gets the ball object of this MatchAssets object.
	 * @return	the ball object of the MatchAssets object.
	 */
	public Ball getBall() {
		return ball;
	}

	/**
	 * Sets the ball object of this MatchAssets object.
	 * @param ball	the ball object to be set.
	 */
	public void setBall(Ball ball) {
		this.ball = ball;
	}

	/**
	 * Gets the pitch object of this MatchAssets object.
	 * @return	the pitch object of the MatchAssets object.
	 */
	public Pitch getPitch() {
		return pitch;
	}

	/**
	 * Sets the pitch object of this MatchAssets object.
	 * @param pitch	the pitch object to be set.
	 */
	public void setPitch(Pitch pitch) {
		this.pitch = pitch;
	}

	/**
	 * Gets the friction acceleration value set in this MatchAssets object.
	 * @return
	 */
	public double getFrictionAcceleration() {
		return frictionAcceleration;
	}

	/**
	 * Sets the friction acceleration value of this MatchAssets object.
	 * @param frictionAcceleration	the friction acceleration to be set
	 */
	public void setFrictionAcceleration(double frictionAcceleration) {
		this.frictionAcceleration = frictionAcceleration;
	}
	
	/**
	 * Clones this MatchAssets object according to the given callerTeamID. The skills of opponent players
	 * are hidden from the caller team.
	 * @param callerTeamID	the ID of the caller team.
	 * @return	the clone of this MatchAssets object.
	 */
	public MatchAssets clone(int callerTeamID) {
		// TODO: ecank - share player according to callerTeamID
		HashMap<Integer, Player[]> clonePlayers = new HashMap<Integer, Player[]>();
		Integer[] teamIDs = this.getTeamIDs();
		clonePlayers.put(teamIDs[0], Player.clonePlayers(players.get(teamIDs[0])));
		clonePlayers.put(teamIDs[1], Player.clonePlayers(players.get(teamIDs[1])));
		HashMap<Integer, Integer> cloneScores = new HashMap<Integer, Integer>();
		cloneScores.put(teamIDs[0], this.getScores().get(teamIDs[0]));
		cloneScores.put(teamIDs[1], this.getScores().get(teamIDs[1]));
		MatchAssets clone = new MatchAssets(this.ball.clone(), this.pitch.clone(), clonePlayers, cloneScores);
		clone.scoredTeamId = this.scoredTeamId;
		
		if ( this.ball.getOwner() != null ) {
			Integer[] teamIds = this.getTeamIDs();
			// Move Players
			for(int j = 0; j < teamIds.length; j++) {
				for(int p = 0; p < clone.getPlayers().get(teamIds[j]).length; p++) {
					if ( clone.getPlayers().get(teamIds[j])[p].getId() == this.ball.getOwner().getId()) {
						clone.getBall().setOwner(clone.getPlayers().get(teamIds[j])[p]);
						break;
					}
				}
			}
		}
		return clone;
	}
	
	/**
	 * Finds the player that has the same ID with the given player in this MatchAssets object.
	 * @param p	given player.
	 * @return	the matching player.
	 */
	public Player findMatchingPlayer(Player p) {
		Integer[] teamIds = this.getTeamIDs();
		for(int i = 0; i < teamIds.length; i++) {
			for(int j = 0; j < this.getPlayers().get(teamIds[i]).length; j++) {
				if ( this.getPlayers().get(teamIds[i])[j].getId() == p.getId()) {
					return this.getPlayers().get(teamIds[i])[j];
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets the player with the given ID in this MatchAssets object.
	 * @param id	given ID.
	 * @return	player with the given ID.
	 */
	public Player getPlayer(int id) {
		Integer[] teamIDs = this.getTeamIDs();
		for(int i = 0; i < teamIDs.length; i++) {
			for(int j = 0; j < players.get(teamIDs[i]).length; j++) {
				if ( players.get(teamIDs[i])[j].getId() == id ) {
					return players.get(teamIDs[i])[j];
				}
			}
		}
		return null;
	}
	
	/**
	 * Finds the player that has the same ID with the given player in the given player list.
	 * @param p	given player.
	 * @param playerList	player list.
	 * @return	the matching player
	 */
	public static Player findMatchingPlayer(Player p, Player[] playerList) {
		for(int j = 0; j < playerList.length; j++) {
			if ( playerList[j].getId() == p.getId()) {
				return playerList[j];
			}
		}
		return null;
	}
	
	/**
	 * Gets the player which is the closest to the ball among the given player list.
	 * @param players	player list.
	 * @return	the ID of closest player to the ball. If no player is found, returns null
	 */
	public Player getPlayerClosestToBall(Player[] players) {
		if ( players != null && players.length != 0 ) {
			double minDistance = players[0].getPosition().measureDistance(this.ball.getPosition());
			int minIndex = 0;
			for(int i = 1; i < players.length; i++) {
				double distance = players[i].getPosition().measureDistance(this.ball.getPosition());
				if ( distance < minDistance) {
					minDistance = distance;
					minIndex = i;
				}
			}
			return players[minIndex];
		}
		return null;
	}
	
	/**
	 * Gets the player which is the closest to the given object among the given player list.
	 * @param players	player list.
	 * @param target	target object.
	 * @return	the ID of the closest player to the target object. If no player is found, returns null
	 */
	public Player getPlayerClosestToObject(Player[] players, vector2D target) {
		if ( players != null && players.length != 0 && target != null ) {
			double minDistance = players[0].getPosition().measureDistance(target);
			int minIndex = 0;
			for(int i = 1; i < players.length; i++) {
				double distance = players[i].getPosition().measureDistance(target);
				if ( distance < minDistance) {
					minDistance = distance;
					minIndex = i;
				}
			}
			return players[minIndex];
		}
		return null;
	}
	
	/**
	 * Gets the players in the given area.
	 * @param area	specified area.
	 * @return	the player list containing the ones in the given area.
	 */
	public Player[] getPlayersInArea(Rectangle2D.Double area) {
		ArrayList<Player> playersInArea = new ArrayList<Player>();
		if ( area != null ) {
			Integer[] teamIds = this.getTeamIDs();
			for(int i = 0; i < teamIds.length; i++) {
				for(int j = 0; j < this.getPlayers().get(teamIds[i]).length; j++) {
					if ( area.contains(this.getPlayers().get(teamIds[i])[j].getPosition().toPoint()) ) {
						playersInArea.add(this.getPlayers().get(teamIds[i])[j]);
					}
				}
			}
		}
		if ( playersInArea.size() > 0 ) {
			return playersInArea.toArray(new Player[0]);
		}
		return null;
	}
	
	/**
	 * Gets the players that has the given team ID in the given area.
	 * @param area	specified area.
	 * @param teamID	team ID of the players to be found.
	 * @return	the player list containing the ones with the given team ID in the specified area.
	 */
	public Player[] getPlayersInArea(Rectangle2D.Double area, int teamID) {
		ArrayList<Player> playersInArea = new ArrayList<Player>();
		if ( area != null ) {
			Integer[] teamIds = this.getTeamIDs();
			for(int i = 0; i < teamIds.length; i++) {
				for(int j = 0; j < this.getPlayers().get(teamIds[i]).length; j++) {
					if ( area.contains(this.getPlayers().get(teamIds[i])[j].getPosition().toPoint()) && this.getPlayers().get(teamIds[i])[j].getTeamID() == teamID) {
						playersInArea.add(this.getPlayers().get(teamIds[i])[j]);
					}
				}
			}
		}
		if ( playersInArea.size() > 0 ) {
			return playersInArea.toArray(new Player[0]);
		}
		return new Player[0];
	}
	
	/**
	 * Gets the players in the circle specified with the range and the center.
	 * @param center	the center of the circle.
	 * @param range		the range of the circle.
	 * @return			the player list in the specified circle.
	 */
	public Player[] getPlayersInRange(vector2D center, double range) {
		ArrayList<Player> playersInRange = new ArrayList<Player>();
		if ( range > 0 ) {
			Integer[] teamIds = this.getTeamIDs();
			for(int i = 0; i < teamIds.length; i++) {
				for(int j = 0; j < this.getPlayers().get(teamIds[i]).length; j++) {
					if ( center.measureDistance(this.getPlayers().get(teamIds[i])[j].getPosition()) <= range ) {
						playersInRange.add(this.getPlayers().get(teamIds[i])[j]);
					}
				}
			}
		}
		if ( playersInRange.size() > 0 ) {
			return playersInRange.toArray(new Player[0]);
		}
		return new Player[0];
	}
	
	/**
	 * Gets the players with the given team ID in the circle specified with the range and the center.
	 * @param center	the center of the circle.
	 * @param range		the range of the circle.
	 * @param teamID	the team ID of the players to be found.
	 * @return			the player list with the given team ID in the specified circle.
	 */
	public Player[] getPlayersInRange(vector2D center, double range, int teamID) {
		ArrayList<Player> playersInRange = new ArrayList<Player>();
		if ( range > 0 ) {
			Integer[] teamIds = this.getTeamIDs();
			for(int i = 0; i < teamIds.length; i++) {
				for(int j = 0; j < this.getPlayers().get(teamIds[i]).length; j++) {
					if ( center.measureDistance(this.getPlayers().get(teamIds[i])[j].getPosition()) <= range && this.getPlayers().get(teamIds[i])[j].getTeamID() == teamID) {
						playersInRange.add(this.getPlayers().get(teamIds[i])[j]);
					}
				}
			}
		}
		if ( playersInRange.size() > 0 ) {
			return playersInRange.toArray(new Player[0]);
		}
		return new Player[0];
	}
	
	
	/**
	 * Gets the closest point of the given rectangle to the given point.
	 * @param rect		the rectangle.
	 * @param fromPoint	the point.
	 * @return	the closest point of the rectangle to the given point.
	 */
	public vector2D getClosestPoint(Rectangle2D.Double rect, vector2D fromPoint) {
		if ( rect != null && fromPoint != null ) {
			double closestX = 0, closestY = 0;
			if ( fromPoint.getX() < rect.getX() ) {
				closestX = rect.getX();
			}
			else if ( fromPoint.getX() > rect.getX() ) {
				closestX = rect.getX() + rect.getWidth();
			}
			else {
				closestX = fromPoint.getX();
			}
			
			if ( fromPoint.getY() < rect.getY() ) {
				closestY = rect.getY();
			}
			else if ( fromPoint.getY() > rect.getY() ) {
				closestY = rect.getY() + rect.getHeight();
			}
			else {
				closestY = fromPoint.getY();
			}
			return new vector2D(closestX, closestY);
		}
		return null;
	}
	
	/**
	 * Gets the players to the line within the given range.
	 * @param from	first endpoint of the line.
	 * @param to	second endpoint of the line.
	 * @param range	the range.
	 * @return	player list in the given range.
	 */
	public Player[] getClosestPlayersToLine(vector2D from, vector2D to, double range) {
		ArrayList<Player> players = new ArrayList<Player>();
		if ( range >= 0 ) {
			Integer[] teamIds = this.getTeamIDs();
			for(int i = 0; i < teamIds.length; i++) {
				for(int j = 0; j < this.getPlayers().get(teamIds[i]).length; j++) {
					Line2D.Double line = new Line2D.Double(from.toPoint(), to.toPoint());
					if (line.ptSegDist(this.getPlayers().get(teamIds[i])[j].getPosition().toPoint()) <= range) {
						players.add(this.getPlayers().get(teamIds[i])[j]);
					}
				}
			}
		}
		if ( players.size() > 0 ) {
			return players.toArray(new Player[0]);
		}
		return new Player[0];
	}
	
	/**
	 * Gets the players with the given team ID to the line within the given range.
	 * @param from	first endpoint of the line.
	 * @param to	second endpoint of the line.
	 * @param range	the range.
	 * @param teamID	team ID of the players to be found.
	 * @return	player list in the given range.
	 */
	public Player[] getClosestPlayersToLine(vector2D from, vector2D to, double range, int teamID) {
		ArrayList<Player> players = new ArrayList<Player>();
		if ( range >= 0 ) {
			Integer[] teamIds = this.getTeamIDs();
			for(int i = 0; i < teamIds.length; i++) {
				for(int j = 0; j < this.getPlayers().get(teamIds[i]).length; j++) {
					Line2D.Double line = new Line2D.Double(from.toPoint(), to.toPoint());
					if ( (line.ptSegDist(this.getPlayers().get(teamIds[i])[j].getPosition().toPoint()) <= range) && (teamID == this.getPlayers().get(teamIds[i])[j].getTeamID()) ) {
						players.add(this.getPlayers().get(teamIds[i])[j]);
					}
				}
			}
		}
		if ( players.size() > 0 ) {
			return players.toArray(new Player[0]);
		}
		return new Player[0];
	}

	/**
	 * Gets the player hash map.
	 * @return	player hash map.
	 */
	public HashMap<Integer, Player[]> getPlayers() {
		return players;
	}
	
	/**
	 * Gets the player list with the given team ID.
	 * @param teamID	team ID.
	 * @return	player list with the team ID.
	 */
	public Player[] getPlayerList(int teamID) {
		return players.get(teamID);
	}
	
	/**
	 * Gets the score of the team with the given team ID.
	 * @param teamID	team ID of the team.
	 * @return	score of the team with the given team ID.
	 */
	public int getScore(int teamID) {
		return scores.get(teamID).intValue();
	}
	
	/**
	 * Submits the teams to the MatchAssets object.
	 * Sets the goal post IDs of the pitch.
	 * @param team1ID	team ID of the first team.
	 * @param team2ID	team ID of the second team.
	 */
	public void submitTeams(int team1ID, int team2ID) {
		pitch.setGoalPostIDs(team1ID, team2ID);
	}
	
	/**
	 * Sets the teams and their players to the MatchObjects.
	 * @param team1ID	team ID of the first team.
	 * @param players1	player list of the first team.
	 * @param team2ID	team ID of the second team.
	 * @param players2	player list of the second team.
	 * @return	true if setting is successful. false otherwise.
	 */
	public boolean setTeams(int team1ID, Player[] players1, int team2ID, Player[] players2) {
		if ( ! players.containsKey(team1ID) && ! players.containsKey(team2ID)) {
			players.put(team1ID, players1);
			players.put(team2ID, players2);
			scores.put(team1ID, 0);
			scores.put(team2ID, 0);
			startingTeam = team1ID;
			this.initialize();
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the team ID list.
	 * @return team ID list.
	 */
	public Integer[] getTeamIDs() {
		return players.keySet().toArray(new Integer[0]);
	}

	/**
	 * Sets the player hash map.
	 * @param players	player hash map to be set.
	 */
	public void setPlayers(HashMap<Integer, Player[]> players) {
		this.players = players;
	}

	/**
	 * Gets the score hash map.
	 * @return	the score hash map.
	 */
	public HashMap<Integer, Integer> getScores() {
		return scores;
	}

	/**
	 * Sets the score hash map.
	 * @param scores	score hash map to be set.
	 */
	public void setScores(HashMap<Integer, Integer> scores) {
		this.scores = scores;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String tostring = "";
		Integer[] teamIDs = this.getTeamIDs();
		for(int i = 0; i < teamIDs.length; i++) {
			tostring += "==== Team " + teamIDs[i] + " ==== Score : " + this.getScore(teamIDs[i]) + "\n";
			for(int j = 0; j < players.get(teamIDs[i]).length; j++) {
				tostring += players.get(teamIDs[i])[j].getId() + " - " + players.get(teamIDs[i])[j].toString() + "\n";
			}
		}
		return tostring;
	}
	
	/**
	 * Increases the score of the team with the given team ID.
	 * @param teamID	team ID.
	 */
	private void increaseScore(int teamID) {
		int tempScore = this.scores.get(teamID).intValue();
		this.scores.remove(teamID);
		this.scores.put(teamID, tempScore+1);
	}
	
	/**
	 * Stops all the players in this object.
	 */
	public void stopPlayers() {
		Integer[] teamIDs = this.getTeamIDs();
		for(int i = 0; i < teamIDs.length; i++) {
			for(int j = 0; j < players.get(teamIDs[i]).length; j++) {
				players.get(teamIDs[i])[j].setSpeed(new vector2D(0,0));
			}
		}
	}
	
	/**
	 * Gets the team ID of the opponent team of the given team.
	 * @param teamID	the team ID.
	 * @return	the opponent team ID.
	 */
	public int getOtherTeamID(int teamID) {
		Integer[] teamIDs = this.getTeamIDs();
		int opponentID = -1;
		if ( teamID == teamIDs[0] ) {
			opponentID = teamIDs[1];
		}
		else if ( teamID == teamIDs[1] ) {
			opponentID = teamIDs[0];
		}
		return opponentID;
	}
	
	/**
	 * Initializes the MatchAssets object. Gets the first two players of the starting team to
	 * the kick off position.
	 */
	public void initialize() {
		this.ball.initialize(pitch);
		this.scoredTeamId = -1;
		Integer[] teamIDs = this.getTeamIDs();
		for(int i = 0; i < teamIDs.length; i++) {
			if ( teamIDs[i] == startingTeam ) {
				vector2D kickOffPos = this.pitch.getKickOffPosition(teamIDs[i]);
				players.get(teamIDs[i])[0].setPosition(kickOffPos);
				players.get(teamIDs[i])[0].setSpeed(new vector2D(0,0));
				players.get(teamIDs[i])[0].setDirectionVector(ball.getPosition().subtract(kickOffPos));
				double deltaY = kickOffPos.measureDistance(this.ball.getPosition());
				players.get(teamIDs[i])[1].setPosition(new vector2D(kickOffPos.getX(), kickOffPos.getY() - deltaY));
				players.get(teamIDs[i])[1].setSpeed(new vector2D(0,0));
				players.get(teamIDs[i])[1].setDirectionVector(ball.getPosition().subtract(new vector2D(kickOffPos.getX(), kickOffPos.getY() - deltaY)));
				
				for(int j = 2; j < players.get(teamIDs[i]).length; j++) {
					players.get(teamIDs[i])[j].setPosition(players.get(teamIDs[i])[j].getStartupPosition());
					players.get(teamIDs[i])[j].setSpeed(new vector2D(0,0));
				}
			}
			else {
				for(int j = 0; j < players.get(teamIDs[i]).length; j++) {
					players.get(teamIDs[i])[j].setPosition(players.get(teamIDs[i])[j].getStartupPosition());
					players.get(teamIDs[i])[j].setSpeed(new vector2D(0,0));
				}
			}
		}
	}

	/**
	 * Checks if a team scored.
	 * @return true if a team scored, false otherwise.
	 */
	public boolean isScored() {
		return (scoredTeamId != -1);
	}
	
}
