package simulator;
import gui.SimulationPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import rare.Chance;
import rare.Highlight;
import rare.Metadata;
import rare.Percept;
import rare.Pitch;
import rare.PlayerAction;
import rare.SaveFile;
import rare.Statistic;
import rare.vector2D;
import utils.CBLogger;
import utils.CengBallException;
import utils.Enums;
import utils.Utils;

import common.Ball;
import common.CommonEnums;
import common.CommonEnums.ActionType;
import common.MatchAssets;
import common.Player;
import common.Team;


/**
 * This class is responsible for simulating the game. It is the master of the imported teams.
 * It simulates the game in a turn based manner and exports the match to a save file.
 *
 */
public class Simulator extends Thread {
	private Team a;
	private Team b;
	private MatchAssets	assets;
	private SaveFile saveFile;
	private ArrayList<Percept> perceptSequence;
	private int currentTurn, turnLimit;
	private TeamRunner threadA, threadB;
	private Metadata metadata;
	private SimulationPanel caller;
	private Highlight highlight;
	private boolean mRun;
	private boolean isFatigueSet;
	private int numberOfPlayers;
	private Statistic stats;
	
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();
	
	/**
	 * Constructor of the simulator class.
	 * @param caller	the caller GUI component.
	 * @param a			the first team.
	 * @param b			the second team.
	 * @param pitch		the pitch of the game.
	 * @param logFilePath	the file path for saving the match.
	 * @param perceptLimit	the maximum percept limit.
	 * @param numberOfPlayers	number of players for a team.
	 * @param isFatigueSet		fatigue option flag.
	 * @throws CengBallException
	 */
	public Simulator(SimulationPanel caller, Team a, Team b, Pitch pitch, String logFilePath, 
			int perceptLimit, int numberOfPlayers, boolean isFatigueSet) throws CengBallException {
		try {
			this.caller = caller;
			this.a = a;
			this.b = b;
			this.assets = new MatchAssets(new Ball(), pitch);
			this.saveFile = new SaveFile(logFilePath);
			this.currentTurn = 0;
			this.turnLimit = perceptLimit / Enums.DEFAULT_TURN_RATIO;
			this.perceptSequence = new ArrayList<Percept>();
			this.isFatigueSet = isFatigueSet;
			this.numberOfPlayers = numberOfPlayers;
			initializeTeams(numberOfPlayers);
			this.metadata = new Metadata(a.getTeamInfo(), b.getTeamInfo(), pitch, perceptLimit/ (60*Enums.DEFAULT_FPS));
			this.saveFile.setMetadata(metadata);
			initializeStats();
		} catch (CengBallException e) {
			LOGGER.log(Level.WARNING, "Cannot initialize statistics");
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Exception : " + e);
		}
	}

	@Override
	public void run() {
		mRun = true;
		long last = System.currentTimeMillis();
		highlight = new Highlight(Enums.HighlightType.START, null);
		perceptSequence.add(new Percept(assets.getBall().clone(), gatherPlayers(), assets.getScore(a.getId()), assets.getScore(b.getId()), highlight));
		while ( mRun && (currentTurn < turnLimit)) { // mRun is to stop the loop and join the thread
			if ( currentTurn % 2 == 0 ) { // Team A's turn
				updateTeamA();
				threadA = new TeamRunner(a);
				threadA.start();
				threadSleep(Enums.DEFAULT_SLEEP_TIME);
				threadJoin(threadA, Enums.DEFAULT_SLEEP_TIME);
				try {
					getIntention(a);
				} catch (CengBallException e) {
					LOGGER.log(Level.WARNING, "Cannot get intention of team : " + a.getId());
				} catch (Exception e) {
					LOGGER.log(Level.WARNING, "Exception : " + e);
				}
			}
			else { // Team B's turn
				updateTeamB();
				threadB = new TeamRunner(b);
				threadB.start();
				threadSleep(Enums.DEFAULT_SLEEP_TIME);
				threadJoin(threadB, Enums.DEFAULT_SLEEP_TIME);
				try {
					getIntention(b);
				} catch (CengBallException e) {
					LOGGER.log(Level.WARNING, "Cannot get intention of team : " + b.getId());
				} catch (Exception e) {
					LOGGER.log(Level.WARNING, "Exception : " + e);
				}
			}
			
			for(int p = 0; p < Enums.DEFAULT_TURN_RATIO; p++) {
				assets = assets.advance(1);
				if ( assets.isScored() ) {
					if ( highlight != null ) {
						highlight = new Highlight(Enums.HighlightType.GOAL, highlight.getActor().clone());
					}
					assets.stopPlayers();
					for(int d = 0; d < Enums.DEFAULT_SCORE_DELAY; d++) {
						perceptSequence.add(new Percept(assets.getBall().clone(), gatherPlayers(), assets.getScore(a.getId()), assets.getScore(b.getId()), highlight));
					}
					assets.initialize();
					break;
				}
				else {
					// Create new percept
					perceptSequence.add(new Percept(assets.getBall().clone(), gatherPlayers(), assets.getScore(a.getId()), assets.getScore(b.getId()), highlight));
				}
			}
			
			currentTurn++;
			caller.setTurn(currentTurn, turnLimit);
		}
		
		if ( currentTurn == turnLimit ) {
			highlight = new Highlight(Enums.HighlightType.END, null);
			perceptSequence.add(new Percept(assets.getBall().clone(), gatherPlayers(), assets.getScore(a.getId()), assets.getScore(b.getId()), highlight));

			saveFile.setStats(stats);
			saveFile.setPerceptList(perceptSequence);
            saveFile.saveCompressed();
			caller.onSimulationFinished();
		}
		LOGGER.log(Level.INFO, "Simulation took " + (System.currentTimeMillis() - last)/1000.0 + " seconds");
	}
	
	/**
	 * Gets and checks the intention of the given team.
	 * @param team	team to be checked.
	 * @throws CengBallException
	 */
	private void getIntention(Team team) throws CengBallException {
		Player[] players = assets.getPlayerList(team.getId());	// Originals
		// Increase ball owner's statistic. 
		if (assets.getBall().getOwner() != null)
			stats.incHasBall(assets.getBall().getOwner().getId());
		
		for (int i = 0, size = players.length; i < size; i++) {
			Player matchingPlayer = MatchAssets.findMatchingPlayer(players[i], team.getMyPlayers());
			if (matchingPlayer != null && stats.getPassControl(matchingPlayer.getId())) {
				if (assets.getBall().getOwner() != null) {
					if (assets.getBall().getOwner().getTeamID() == matchingPlayer.getTeamID()) {
						stats.incPassSuccess(matchingPlayer.getId());
					}
					stats.setPassControl(matchingPlayer.getId(), false);
				}
			}
		}
		
		for(int i = 0, size = players.length; i < size; i++) {
			Player matchingPlayer = MatchAssets.findMatchingPlayer(players[i], team.getMyPlayers());
			
			if ( matchingPlayer != null && matchingPlayer.getIntent() != null ) {				
				switch(matchingPlayer.getIntent().getType()) {
				case NONE:
					break;
				case STOP:
					players[i].setSpeed(new vector2D(0,0));
					break;
				case PASS:
					if ( assets.getBall().checkOwner(players[i]))  { // If player want to pass, then he has to control the ball.
						double fatigueRatio = matchingPlayer.getFatigueAbility() / (double)CommonEnums.MAXIMUM_NUMBER_FATIGUE;
						if ( !isFatigueSet ) {
							fatigueRatio = 1;
						}
						double skillRatio = (double)(matchingPlayer.getPassAbility()) / (double)CommonEnums.MAXIMUM_ATTRIBUTE_VALUE;
						
						vector2D intentVector = matchingPlayer.getIntent().getIntentVector();
						if ( Double.isNaN(intentVector.getX()) ) {
							intentVector.setX(0);
						}
						
						if ( Double.isNaN(intentVector.getY()) ) {
							intentVector.setY(0);
						}
						
						vector2D passVector = intentVector.subtract(players[i].getPosition()).normalize();
																		
						passVector = passVector.multiply( (double)(Enums.MAXIMUM_BALL_SPEED * skillRatio) );						
												
						double angleDifference = Math.abs(matchingPlayer.getDirectionVector().getPosAngle() - passVector.getPosAngle());
						if ( angleDifference > Math.PI ) {
							angleDifference = 2*Math.PI - angleDifference;
						}
						
						double playerState = Chance.calculateState(fatigueRatio, skillRatio, angleDifference / (Math.PI) );
												
						passVector = Chance.applyChance(passVector, playerState);
																
						passVector = passVector.add(players[i].getSpeed());
						
						assets.getBall().setSpeed(passVector);
						assets.getBall().setOwner(null);
						highlight = new Highlight(Enums.HighlightType.PASS, players[i].clone());
						// Increase the matching players pass statistic.
						stats.incPassTry(matchingPlayer.getId());
						// Set pass control
						stats.setPassControl(matchingPlayer.getId(), true);
					}
					break;
				case SHOOT:
					if ( assets.getBall().checkOwner(players[i]))  { // If player want to shoot, then he has to control the ball.
						double fatigueRatio = matchingPlayer.getFatigueAbility() / (double)CommonEnums.MAXIMUM_NUMBER_FATIGUE;
						if ( !isFatigueSet ) {
							fatigueRatio = 1;
						}
						double skillRatio = (double)(matchingPlayer.getShootAbility()) / (double)CommonEnums.MAXIMUM_ATTRIBUTE_VALUE;
						
						vector2D intentVector = matchingPlayer.getIntent().getIntentVector();
						if ( Double.isNaN(intentVector.getX()) ) {
							intentVector.setX(0);
						}
						
						if ( Double.isNaN(intentVector.getY()) ) {
							intentVector.setY(0);
						}
						
						vector2D shootVector = intentVector.subtract(players[i].getPosition()).normalize();
						shootVector = shootVector.multiply( (double)(Enums.MAXIMUM_BALL_SPEED * skillRatio) );
							
						double angleDifference = Math.abs(matchingPlayer.getDirectionVector().getPosAngle() - shootVector.getPosAngle());
						if ( angleDifference > Math.PI ) {
							angleDifference = 2*Math.PI - angleDifference;
						}
						
						double playerState = Chance.calculateState(fatigueRatio, skillRatio, angleDifference / (Math.PI) );
						shootVector = Chance.applyChance(shootVector, playerState);
							
						shootVector = shootVector.add(players[i].getSpeed());
						assets.getBall().setSpeed(shootVector);
						assets.getBall().setOwner(null);
						highlight = new Highlight(Enums.HighlightType.SHOOT, players[i].clone());
						// Increase the matching players shoot stats.
						stats.incShootTry(matchingPlayer.getId());
					}
					break;
				case MOVE:
					vector2D intentVector = matchingPlayer.getIntent().getIntentVector();
					if ( Double.isNaN(intentVector.getX()) ) {
						intentVector.setX(0);
					}
					
					if ( Double.isNaN(intentVector.getY()) ) {
						intentVector.setY(0);
					}
					
					
					vector2D newSpeedVector = intentVector.subtract(players[i].getPosition()).normalize();
					newSpeedVector = newSpeedVector.multiply( (double)( Enums.MAXIMUM_PLAYER_SPEED * players[i].getSpeedAbility() )/(double)( CommonEnums.MAXIMUM_ATTRIBUTE_VALUE) );
					players[i].setSpeed(newSpeedVector);
					if (assets.getBall().checkOwner(players[i])) {
						highlight = new Highlight(Enums.HighlightType.MOVEWITHBALL, players[i].clone());
					}
					break;
				case TRAP:
					// if trap area will be checked, then handler needed.
					if (players[i].getDistanceFrom(assets.getBall()) <= CommonEnums.TRAP_DISTANCE) {
						// Increase the matching players trap statistic.
						stats.incTrapTry(matchingPlayer.getId());
						if ( assets.getBall().getOwner() != null ) {
							double fatigueRatio1 = matchingPlayer.getFatigueAbility() / CommonEnums.MAXIMUM_NUMBER_FATIGUE;
							if ( !isFatigueSet ) {
								fatigueRatio1 = 1;
							}
							double skillRatio1 = matchingPlayer.getTackleAbility() / CommonEnums.MAXIMUM_ATTRIBUTE_VALUE;
							double tacklerState = Chance.calculateState(fatigueRatio1, skillRatio1);
							
							double fatigueRatio2 = assets.getBall().getOwner().getFatigueAbility() / CommonEnums.MAXIMUM_NUMBER_FATIGUE;
							if ( !isFatigueSet ) {
								fatigueRatio2 = 1;
							}
							double skillRatio2 = assets.getBall().getOwner().getDribblingAbility() / CommonEnums.MAXIMUM_ATTRIBUTE_VALUE;
							double ownerState = Chance.calculateState(fatigueRatio2, skillRatio2);
							double chance = Chance.applyChance(ownerState, tacklerState);
							
							if ( chance > 0 ) {	// Owner has more chance
								highlight = new Highlight(Enums.HighlightType.DRIBBLE, assets.getBall().getOwner().clone());
							}
							else {	// Tackler has more chance
								assets.getBall().setOwner(players[i]);
								// Increase the matching players successful trap statistic.
								stats.incTrapSuccess(matchingPlayer.getId());
								players[i].setIntent(new PlayerAction(ActionType.NONE));
								highlight = new Highlight(Enums.HighlightType.TACKLE, players[i].clone());
							}
						}
						else {
							// Grab ownerless ball
							assets.getBall().setOwner(players[i]);
							// Increase the matching players successful trap statistic.
							stats.incTrapSuccess(matchingPlayer.getId());
							highlight = new Highlight(Enums.HighlightType.OWN, players[i].clone());
						}
					}
					break;
				default:
					break;
				}
				// Increase distance player covered
				stats.incTotalDistance(matchingPlayer.getId(), matchingPlayer.getPosition());
				
				if (isFatigueSet) {
					double coveredDistance = stats.getLastDistance(players[i].getId());
					double playerStamina = players[i].getStaminaAbility();
					double oldFatigue = players[i].getFatigueAbility();
					double reductionAmountForCoveredDistance = (coveredDistance/ ((double)Enums.MAXIMUM_PLAYER_SPEED * 2.0 * (double)Enums.DEFAULT_TURN_RATIO / (double)Enums.DEFAULT_FPS)) * Enums.MAXIMUM_FATIGUE_LOSS_PER_MATCH;
					double reductionAmountForStamina = Enums.MINIMUM_FATIGUE_LOSS_PER_MATCH + 
							((double)CommonEnums.MAXIMUM_ATTRIBUTE_VALUE - playerStamina) * (Enums.MAXIMUM_FATIGUE_LOSS_PER_MATCH - Enums.MINIMUM_FATIGUE_LOSS_PER_MATCH) / (double)CommonEnums.MAXIMUM_ATTRIBUTE_VALUE;
					double reductionAmount = Enums.COVERED_DISTANCE_WEIGHT_FOR_FATIGUE_CALCULATION * reductionAmountForCoveredDistance + 
											(1-Enums.COVERED_DISTANCE_WEIGHT_FOR_FATIGUE_CALCULATION) * reductionAmountForStamina;
					double reductionAmountPerTurn = reductionAmount / (double)(turnLimit);
					double newFatigue = oldFatigue - reductionAmountPerTurn;
					players[i].setFatigueAbility(newFatigue);
				}
				
				players[i].setIntent(new PlayerAction(ActionType.NONE));
			}
		}
	}
	
	/**
	 * Synchronizes the team a.
	 */
	private void updateTeamA() {
		MatchAssets clone = assets.clone(a.getId());
		a.sync(clone);
	}
	
	/**
	 * Synchronizes the team b.
	 */
	private void updateTeamB() {
		MatchAssets clone = assets.clone(b.getId());
		b.sync(clone);
	}
	
	private void threadSleep(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			LOGGER.log(Level.WARNING, "InterruptedException : " + e);
		}
	}
	
	private void threadJoin(Thread thread, int miliseconds) {
		try {
			thread.join(miliseconds);
		} catch (InterruptedException e) {
			LOGGER.log(Level.WARNING, "InterruptedException : " + e);
		}
	}
	
	private void initializeTeams(int numberOfPlayers) {
		checkTeams(a,b);
		assets.submitTeams(a.getId(), b.getId());
		HashMap<Integer, Integer> idMap = new HashMap<Integer, Integer>();
		Player[] teamAPlayers = a.assembleTeam(numberOfPlayers, assets.getPitch().getHalfCourt(a.getId()));
		Player[] teamBPlayers = b.assembleTeam(numberOfPlayers, assets.getPitch().getHalfCourt(b.getId()));
		
		// Check team A
		if ( teamAPlayers == null ) {
			Team tempTeam = new Team();
			teamAPlayers = tempTeam.assembleTeam(numberOfPlayers, assets.getPitch().getHalfCourt(a.getId()));
			for(int i = 0; i < teamAPlayers.length; i++) {
				teamAPlayers[i].setTeamID(a.getId());
			}
			LOGGER.log(Level.WARNING, "Team " + a.getId() + " - " + a.getTeamInfo().getName() 
						+ " cannot assemble team, creating defaults");
		}
		else if ( teamAPlayers.length < numberOfPlayers ) {
			Player[] enoughPlayers = new Player[numberOfPlayers];
			Player[] remainingPlayers = new Team().assembleTeam(numberOfPlayers - teamAPlayers.length, assets.getPitch().getHalfCourt(a.getId()));
			for(int i = 0; i < (numberOfPlayers - teamAPlayers.length); i++) {
				remainingPlayers[i].setTeamID(a.getId());
			}
			System.arraycopy(teamAPlayers, 0, enoughPlayers, 0, teamAPlayers.length);
			System.arraycopy(remainingPlayers, 0, enoughPlayers, teamAPlayers.length, remainingPlayers.length);
			teamAPlayers = enoughPlayers;
			LOGGER.log(Level.WARNING, "Team " + a.getId() + " - " + a.getTeamInfo().getName() 
					+ " cannot assemble enough players, creating defaults");
		}
		else if ( teamAPlayers.length > numberOfPlayers ) { 
			Player[] enoughPlayers = new Player[numberOfPlayers];
			System.arraycopy(teamAPlayers, 0, enoughPlayers, 0, numberOfPlayers);
			teamAPlayers = enoughPlayers;
			LOGGER.log(Level.WARNING, "Team " + a.getId() + " - " + a.getTeamInfo().getName() 
					+ " assembled more than required players, taking enough of them");
		}
		
		// Check team B
		if (teamBPlayers == null ) {
			Team tempTeam = new Team();
			teamBPlayers = tempTeam.assembleTeam(numberOfPlayers, assets.getPitch().getHalfCourt(b.getId()));
			for(int i = 0; i < teamBPlayers.length; i++) {
				teamBPlayers[i].setTeamID(b.getId());
			}
			LOGGER.log(Level.WARNING, "Team " + b.getId() + " - " + b.getTeamInfo().getName() 
					+ " cannot assemble team, creating defaults");
		}
		else if ( teamBPlayers.length < numberOfPlayers ) {
			Player[] enoughPlayers = new Player[numberOfPlayers];
			Player[] remainingPlayers = new Team().assembleTeam(numberOfPlayers - teamBPlayers.length, assets.getPitch().getHalfCourt(b.getId()));
			for(int i = 0; i < (numberOfPlayers - teamBPlayers.length); i++) {
				remainingPlayers[i].setTeamID(b.getId());
			}
			System.arraycopy(teamBPlayers, 0, enoughPlayers, 0, teamBPlayers.length);
			System.arraycopy(remainingPlayers, 0, enoughPlayers, teamBPlayers.length, remainingPlayers.length);
			teamBPlayers = enoughPlayers;
			LOGGER.log(Level.WARNING, "Team " + b.getId() + " - " + b.getTeamInfo().getName() 
					+ " cannot assemble enough players, creating defaults");
		}
		else if ( teamBPlayers.length > numberOfPlayers ) { 
			Player[] enoughPlayers = new Player[numberOfPlayers];
			System.arraycopy(teamBPlayers, 0, enoughPlayers, 0, numberOfPlayers);
			teamBPlayers = enoughPlayers;
			LOGGER.log(Level.WARNING, "Team " + b.getId() + " - " + b.getTeamInfo().getName() 
					+ " assembled more than required players, taking enough of them");
		}
		assets.setTeams(a.getId(), Player.clonePlayers( checkPlayers(a.getId(), teamAPlayers, idMap) ), b.getId(), Player.clonePlayers( checkPlayers(b.getId(), teamBPlayers, idMap) ));
		// Send MatchAssets object to the teams
		MatchAssets cloneForA = assets.clone(a.getId());
		a.sync(cloneForA);
		MatchAssets cloneForB = assets.clone(b.getId());
		b.sync(cloneForB);
	}
	
	private void initializeStats() throws CengBallException {
		int numberOfTotalPlayers = numberOfPlayers * 2;
		this.stats = new Statistic();
		int[] IdDatas = new int[numberOfTotalPlayers];
		vector2D[] positionDatas = new vector2D[numberOfTotalPlayers];
		Player[] players = gatherPlayers();
		
		for (int i = 0; i < numberOfTotalPlayers; ++i) {
			IdDatas[i] = players[i].getId();
			positionDatas[i] = players[i].getPosition().clone();
		}
		
		if (!this.stats.init(IdDatas, positionDatas))
			throw new CengBallException(Enums.ExceptionType.STATISTIC_ERROR);
	}
	
	private void checkTeams(Team team1, Team team2) {
		if ( Utils.isSimilar(team1.getTeamInfo().getColor1(), team2.getTeamInfo().getColor1()) )  {
			team2.swapColors();
		}
	}
	
	// Checking players that are assembled by one team
	private Player[] checkPlayers(int teamID, Player[] players, HashMap<Integer, Integer> idMap) {
		for(int i = 0; i < players.length; i++) {
			int playerID = 0;
			vector2D startPos = new vector2D(0,0);
			
			if ( players[i] != null ) {
				playerID = players[i].getId();
				startPos = players[i].getStartupPosition();
				// If the ID is invalid, create new one
				if ( idMap.containsKey( playerID ) ) {
					playerID = Player.createId();
				}
				// If the startup position is invalid, place it in the center of its halfcourt
				if ( !assets.getPitch().getHalfCourt(teamID).contains(players[i].getStartupPosition().toPoint())) {
					double newX = assets.getPitch().getHalfCourt(teamID).getCenterX();
					double newY = assets.getPitch().getHalfCourt(teamID).getCenterY();
					startPos = new vector2D(newX, newY);
				}
			}
			else {
				double newX = assets.getPitch().getHalfCourt(teamID).getCenterX();
				double newY = assets.getPitch().getHalfCourt(teamID).getCenterY();
				startPos = new vector2D(newX, newY);
				players[i] = new Player("Player " + i, i+1, teamID, startPos, 
						10,10,10,10,10,10);
			}
			
			
			// Correct variables
			players[i].setId(playerID);
			players[i].setTeamID(teamID);	// Correct team ID
			players[i].setStartupPosition(startPos);
			idMap.put(playerID, 1);
			
			
		}
		return players;
	}
	
	private Player[] gatherPlayers() {
		Player[] allPlayers = new Player[assets.getPlayerList(a.getId()).length + assets.getPlayerList(b.getId()).length];
		System.arraycopy(Player.clonePlayers(assets.getPlayerList(a.getId())), 0, allPlayers, 0, assets.getPlayerList(a.getId()).length);
		System.arraycopy(Player.clonePlayers(assets.getPlayerList(b.getId())), 0, allPlayers, assets.getPlayerList(a.getId()).length, assets.getPlayerList(b.getId()).length);
		return allPlayers;
	}

	public boolean ismRun() {
		return mRun;
	}

	public void setmRun(boolean mRun) {
		this.mRun = mRun;
	}
	
}
