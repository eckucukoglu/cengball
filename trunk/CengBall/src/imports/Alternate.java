package imports;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

import rare.Pitch;
import rare.PlayerAction;
import rare.vector2D;

import common.Ball;
import common.CommonEnums;
import common.MatchAssets;
import common.Player;
import common.Team;


public class Alternate extends Team {
	private static String teamName = "Rockets";
	private static Color teamColor1 = new Color(30, 30, 30), teamColor2 = new Color(205, 205, 0);
	private static String[] names = {"Burhan", "Ali", "Hakan", "Bulent", "Hagi"};
	
	private int PREDICTION_DISTANCE = 20, MARKING_DISTANCE = 6, SHOOT_DISTANCE = 30, SAFE_DISTANCE = 15, SAFE_RANGE = 20;
	private double SAFE_ANGLE = Math.PI / 6.0, FORMATION_DISTANCE = 30, DEFENCE_ZONE = 25;
	private ArrayList<vector2D> supportPoints;
	
	// Environmental Variables
	private PlayerAction lastBallAction;
	private Player[] myPlayers, opponentPlayers;
	private Rectangle2D.Double myGoalPost, opponentGoalPost;
	private int opponentID;
	private int tactic;
	private Player targetPlayer;

	public Alternate() {
		super(teamName, teamColor1, teamColor2);
		
		lastBallAction = null;
	}
	
	@Override
	public Player[] assembleTeam(int numberOfPlayers, Rectangle2D.Double halfCourt) {
		supportPoints = initializeSupportPoints();
		
		Player[] myPlayers = new Player[numberOfPlayers];
		double pDistance1 = (halfCourt.getHeight() - (2*numberOfPlayers/5) * Pitch.DEFAULT_PLAYER_RADIUS * 2) / ((2*numberOfPlayers/5) + 1 );
		int remainingPlayers = numberOfPlayers - 2*(2*numberOfPlayers/5);
		double pDistance2 = (halfCourt.getHeight() - (remainingPlayers) * Pitch.DEFAULT_PLAYER_RADIUS * 2) / (remainingPlayers + 1 );
		double pDistance3 = (halfCourt.getWidth() - numberOfPlayers*Pitch.DEFAULT_PLAYER_RADIUS * 2) / (numberOfPlayers+1);
		int playerIndex = 0;
		if ( halfCourt.getX() == 0 ) {
			// Attackers
			for (int i = 0; i < (2*numberOfPlayers/5); i++) {
				myPlayers[playerIndex] = new Player(names[playerIndex%5], playerIndex+1, this.getId(), new vector2D(halfCourt.getWidth() - (pDistance3),pDistance1*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						(15),		// speed
						(20),		// shoot
						(5),		// pass
						(10),		// dribbling
						(9),		// stamina
						(1));		// tackle
				playerIndex++;
			}
			// Defenders
			for (int i = 0; i < (2*numberOfPlayers/5); i++) {
				myPlayers[playerIndex] = new Player(names[playerIndex%5], playerIndex+1, this.getId(), new vector2D(halfCourt.getWidth() - (pDistance3*3),pDistance1*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						(10),		// speed
						(1),		// shoot
						(14),		// pass
						(5),		// dribbling
						(10),		// stamina
						(20));		// tackle
				playerIndex++;
			}
			// Midfielders
			for (int i = 0; i < remainingPlayers; i++) {
				myPlayers[playerIndex] = new Player(names[playerIndex%5], playerIndex+1, this.getId(), new vector2D(halfCourt.getWidth() - (pDistance3*2),pDistance2*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						(12),		// speed
						(10),		// shoot
						(11),		// pass
						(10),		// dribbling
						(12),		// stamina
						(5));		// tackle
				playerIndex++;
			}
		}
		else {
			// Attackers
			for (int i = 0; i < (2*numberOfPlayers/5); i++) {
				myPlayers[playerIndex] = new Player(names[playerIndex%5], playerIndex+1, this.getId(), new vector2D(halfCourt.getX() + (pDistance3),pDistance1*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						(15),		// speed
						(20),		// shoot
						(5),		// pass
						(10),		// dribbling
						(9),		// stamina
						(1));		// tackle
				playerIndex++;
			}
			// Defenders
			for (int i = 0; i < (2*numberOfPlayers/5); i++) {
				myPlayers[playerIndex] = new Player(names[playerIndex%5], playerIndex+1, this.getId(), new vector2D(halfCourt.getX() + (pDistance3*3),pDistance1*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						(10),		// speed
						(1),		// shoot
						(14),		// pass
						(5),		// dribbling
						(10),		// stamina
						(20));		// tackle
				playerIndex++;
			}
			// Midfielders
			for (int i = 0; i < remainingPlayers; i++) {
				myPlayers[playerIndex] = new Player(names[playerIndex%5], playerIndex+1, this.getId(), new vector2D(halfCourt.getX() + (pDistance3*2),pDistance2*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						(12),		// speed
						(10),		// shoot
						(11),		// pass
						(10),		// dribbling
						(12),		// stamina
						(5));		// tackle
				playerIndex++;
			}
		}
			
		return myPlayers;
	}
	
	@Override
	public void move() {
		opponentID = assets.getOtherTeamID(this.getId());
		myPlayers = assets.getPlayerList(this.getId());
		opponentPlayers = assets.getPlayerList(opponentID);
		myGoalPost = assets.getPitch().getGoalPost(this.getId());
		opponentGoalPost = assets.getPitch().getGoalPost(opponentID);
		MatchAssets nextAssets = assets.advance(PREDICTION_DISTANCE);
		decideTactic();
		
		Player owner = assets.getBall().getOwner();
		if ( owner == null ) {		// Ball is free
			if ( lastBallAction == null ) {
				Player myClosestPlayer = assets.getPlayerClosestToBall(myPlayers);
				getFreeBall(assets.getPlayer(myClosestPlayer.getId()), nextAssets);
			}
			else {
				if ( targetPlayer != null ) {
					getFreeBall(assets.getPlayer(targetPlayer.getId()), nextAssets);
				}
				else {
					Player myClosestPlayer = assets.getPlayerClosestToBall(myPlayers);
					getFreeBall(assets.getPlayer(myClosestPlayer.getId()), nextAssets);
				}
			}
		}
		else {
			if ( owner.getTeamID() == this.getId() ) {		// I have the ball
				double ownerState = getPlayerState(owner);
				
				Player pToPass = myPlayers[0];
				double playerState = getPlayerState(pToPass);
				double passState = getPassState(owner.getPosition(), pToPass.getPosition());
				double totalBestState = playerState + passState * 0.5;
				for(int i = 1; i < myPlayers.length; i++) {
					double playerState1 = getPlayerState(myPlayers[i]);
					double passState1 = getPassState(owner.getPosition(), myPlayers[i].getPosition());
					if ( totalBestState < playerState1 + passState1 * 0.5) {
						totalBestState = playerState1;
						pToPass = myPlayers[i];
					}
				}
				
				if ( pToPass.getId() == owner.getId() || ownerState > totalBestState ) {	// Owner will have the ball
					vector2D shootPoint = new vector2D(opponentGoalPost.getCenterX(), opponentGoalPost.getCenterY());
					double ownerAngle = owner.getDirectionVector().getAngle();
					double shootAngle = shootPoint.subtract(owner.getPosition()).getAngle();
					if ( (shootPoint.measureDistance(owner.getPosition()) < SHOOT_DISTANCE && Math.abs(ownerAngle - shootAngle) < SAFE_ANGLE) ) {
						owner.shoot(shootPoint);
						ArrayList<vector2D> currentPoints = getSupportPoints();
						for(int i = 0; i < myPlayers.length; i++) {
							if ( myPlayers[i].getId() != owner.getId() ) {
								support(assets.getPlayer(pToPass.getId()), myPlayers[i], currentPoints);
							}
						}
					}
					else {
						moveWithBall(owner);
						ArrayList<vector2D> currentPoints = getSupportPoints();
						for(int i = 0; i < myPlayers.length; i++) {
							if ( myPlayers[i].getId() != owner.getId() ) {
								support(assets.getPlayer(pToPass.getId()), myPlayers[i], currentPoints);
							}
						}
					}
				}
				else {	// The ball should be passed
					double ownerAngle = owner.getDirectionVector().getAngle();
					double passAngle = pToPass.getPosition().subtract(owner.getPosition()).getAngle();
					if ( Math.abs(ownerAngle - passAngle) < SAFE_ANGLE ) {
						owner.pass(pToPass.getPosition());
						pToPass.stop();
						targetPlayer = pToPass;
						lastBallAction = owner.getIntent();
						
						ArrayList<vector2D> currentPoints = getSupportPoints();
						for(int i = 0; i < myPlayers.length; i++) {
							if ( myPlayers[i].getId() != owner.getId() && myPlayers[i].getId() != pToPass.getId() ) {
								support(assets.getPlayer(pToPass.getId()), myPlayers[i], currentPoints);
							}
						}
					}
					else {
						owner.move(pToPass.getPosition());
						pToPass.stop();
						
						ArrayList<vector2D> currentPoints = getSupportPoints();
						for(int i = 0; i < myPlayers.length; i++) {
							if ( myPlayers[i].getId() != owner.getId() && myPlayers[i].getId() != pToPass.getId() ) {
								support(assets.getPlayer(owner.getId()), myPlayers[i], currentPoints);
							}
						}
					}
				}
			}
			else {		// Opponent has the ball
				lastBallAction = null;
				targetPlayer = null;
				zoneMarking(nextAssets);
			}
		}
	}
	
	private double getPassState(vector2D from, vector2D to) {
		double distance = from.measureDistance(to);
		return (-1) * ((double)assets.getClosestPlayersToLine(from, to, SAFE_RANGE * (distance / FORMATION_DISTANCE) , opponentID).length / (double)opponentPlayers.length );
	}
	
	private double getPlayerState(Player p) {
		double numberOfPlayersAround = ( (double)assets.getPlayersInRange(p.getPosition(), SAFE_RANGE, opponentID).length) / ( (double)opponentPlayers.length );
		vector2D ocp = new vector2D(opponentGoalPost.getCenterX(), opponentGoalPost.getCenterY());
		double goalPostDistance = p.getPosition().measureDistance(ocp) / SHOOT_DISTANCE;
		if ( goalPostDistance > 1 ) {
			goalPostDistance = 1;
		}
		double shootRatio = (double)p.getShootAbility() / (double)CommonEnums.MAXIMUM_ATTRIBUTE_VALUE;
		double dribblingRatio = (double)p.getDribblingAbility() / (double)CommonEnums.MAXIMUM_ATTRIBUTE_VALUE;
		double speedRatio = (double)p.getSpeedAbility() / (double)CommonEnums.MAXIMUM_ATTRIBUTE_VALUE;
		double fatigueRatio = (double)p.getFatigueAbility() / (double)CommonEnums.MAXIMUM_NUMBER_FATIGUE;
		
		return (dribblingRatio * 6 + shootRatio * 4 + speedRatio * 5 - numberOfPlayersAround * 15 - goalPostDistance * 2 + fatigueRatio + 17 ) / 33.0;
	}
	
	private void getFreeBall(Player playerToAct, MatchAssets nextAssets) {
		Player opClosestPlayer = assets.getPlayerClosestToBall(opponentPlayers);
		double myDistance = playerToAct.getDistanceFrom(assets.getBall());
		double opDistance = opClosestPlayer.getDistanceFrom(assets.getBall());
		boolean isTrapped = false;
		if ( myDistance < assets.getBall().getTrapDistance() ) {
			playerToAct.trap();
			targetPlayer = null;
			lastBallAction = null;
			isTrapped = true;
		}
		else {
			if ( myDistance <= opDistance ) {
				goToBall(playerToAct, nextAssets);
			}
			else {
				if ( opDistance <= SAFE_DISTANCE ) {
					mark(playerToAct, opClosestPlayer );
				}
				else {
					goToBall(playerToAct, nextAssets);
				}
			}
			
		}
		
		ArrayList<vector2D> currentPoints = getSupportPoints();
		for(int i = 0; i < myPlayers.length; i++) {
			if ( myPlayers[i].getId() != playerToAct.getId() ) {
				if ( isTrapped ) {
					support(playerToAct, myPlayers[i], currentPoints);
				}
				else {
					Player playerToMark = assets.getPlayer( nextAssets.getPlayerClosestToObject(nextAssets.getPlayerList(opponentID), myPlayers[i].getPosition()).getId());
					// Mark players only if they are in my half court
					if ( playerToMark != null && assets.getPitch().getHalfCourt(this.getId()).contains(playerToMark.getPosition().toPoint())) {
						mark(myPlayers[i], playerToMark);
					}
					else {
						double distanceToBall = myPlayers[i].getPosition().measureDistance(assets.getBall().getPosition());
						if ( distanceToBall < SAFE_DISTANCE) {
							goToBall(myPlayers[i], nextAssets);
						}
						else {
							vector2D movementVector = assets.getBall().getPosition().subtract(new vector2D(assets.getPitch().getPitchWidth()/2.0, assets.getPitch().getPitchHeight()/2.0));
							double deltaX = movementVector.getX() / 6.0;
							double deltaY = movementVector.getY() / 4.0;
							vector2D goToPosition = myPlayers[i].getStartupPosition();
							if ( assets.getPitch().getHalfCourt(this.getId()).getX() == 0 ) {
								goToPosition = new vector2D(myPlayers[i].getStartupPosition().getX() - deltaX, myPlayers[i].getStartupPosition().getY() - deltaY);
							}
							else {
								goToPosition = new vector2D(myPlayers[i].getStartupPosition().getX() + deltaX, myPlayers[i].getStartupPosition().getY() - deltaY);	
							}
							if ( assets.getPitch().contains(goToPosition) ) {
								myPlayers[i].move(goToPosition);
							}
							else {
								myPlayers[i].stop();
							}
						}
					}
				}
			}
		}
	}
	
	private void moveWithBall(Player owner) {
		Player[] closestOpponents = assets.getPlayersInRange(owner.getPosition(), SAFE_RANGE, opponentID);
		double goalAngle = assets.getClosestPoint(opponentGoalPost, owner.getPosition()).subtract(owner.getPosition()).getAngle();
		double moveAngle = goalAngle;
		if ( closestOpponents != null && closestOpponents.length > 0 ) {
			double[] anglesWithOwner = new double[closestOpponents.length];
			for(int i = 0; i < closestOpponents.length; i++) {
				anglesWithOwner[i] = vector2D.calculateAngle(closestOpponents[i].getPosition(), owner.getPosition());
			}
			Arrays.sort(anglesWithOwner);
			moveAngle = goalAngle;
			for(int i = 0; i < anglesWithOwner.length; i++) {
				if ( (anglesWithOwner[i] - SAFE_ANGLE < goalAngle) && (anglesWithOwner[i] + SAFE_ANGLE > goalAngle)  ) {					
					if ( Math.abs(goalAngle - (anglesWithOwner[i] - SAFE_ANGLE)) < Math.abs(goalAngle - (anglesWithOwner[i] + SAFE_ANGLE)) ) {
						moveAngle = anglesWithOwner[i] - SAFE_ANGLE;
					}
					else {
						moveAngle = anglesWithOwner[i] + SAFE_ANGLE;
					}
				}
			}
		}
		vector2D direction = new vector2D(Math.cos(moveAngle), Math.sin(moveAngle));
		owner.move( owner.getPosition().add(direction.multiply(owner.getSpeedAbility())) ) ;
	}
	
	private vector2D findIntersectionPoint(Ball	ball, Player player) {
		double d = Math.abs(ball.getPosition().measureDistance(player.getPosition()));
		double vP = 10;
		double vB = ball.getSpeed().getMagnitude();
		if ( vB == 0 ) {
			return ball.getPosition();
		}
		
		double angle = player.getPosition().subtract(ball.getPosition()).getAngle() - ball.getSpeedAngle();
		double a = ( vP / vB ) * ( vP / vB ) - 1;
		double b = 2 * d * Math.cos(angle);
		double c = (-1) * d * d;
		double deltaR = 0;
		if ( a != 0 ) {
			double delta = b * b - 4 * a * c;
			double r1 = ((-1) * b + Math.sqrt(delta)) / (2 * a);
			double r2 = ((-1) * b - Math.sqrt(delta)) / (2 * a);
			deltaR = Math.max(r1, r2);
		}
		else {
			deltaR = c / b;
		}
		return new vector2D(ball.getPosition().getX() + deltaR * Math.cos(ball.getSpeedAngle()), ball.getPosition().getY() + deltaR * Math.sin(ball.getSpeedAngle()));
	}
	
	private void goToBall(Player p, MatchAssets nextPercept) {
		p.move(findIntersectionPoint(assets.getBall(), p));
	}
	
	private Player getStopper(MatchAssets newAssets) {
		if ( newAssets != null ) {
			Rectangle2D.Double betweenArea;
			if ( newAssets.getBall().getPosition().getX() > myGoalPost.getX() ) {
				betweenArea = new Rectangle2D.Double(0, 0, Math.abs(newAssets.getBall().getPosition().getX() - MARKING_DISTANCE), assets.getPitch().getPitchHeight());
			}
			else {
				betweenArea = new Rectangle2D.Double(newAssets.getBall().getPosition().getX() + MARKING_DISTANCE, 0, Math.abs(myGoalPost.getX()-newAssets.getBall().getPosition().getX()- MARKING_DISTANCE) , assets.getPitch().getPitchHeight());
			}
			Player[] betweenPlayers = assets.getPlayersInArea(betweenArea, this.getId());
			if ( betweenPlayers != null && betweenPlayers.length != 0 ) {
				return assets.getPlayer(assets.getPlayerClosestToObject(betweenPlayers, newAssets.getBall().getPosition()).getId());
			}
			else {
				return assets.getPlayer(assets.getPlayerClosestToObject(newAssets.getPlayerList(this.getId()), newAssets.getBall().getPosition()).getId() );
			}
		}
		return null;
	}
	
	private void zoneMarking(MatchAssets nextPercept) {
		ArrayList<Player> opponents = new ArrayList<Player>(Arrays.asList(opponentPlayers));
		
		Player currentClosest = assets.getPlayerClosestToObject(myPlayers, assets.getBall().getPosition());
		if ( currentClosest != null && currentClosest.getPosition().measureDistance(assets.getBall().getPosition()) < assets.getBall().getTrapDistance()) {
			assets.getPlayer(currentClosest.getId()).trap();
			
			ArrayList<vector2D> currentPoints = getSupportPoints();
			for(int i = 0; i < myPlayers.length; i++) {
				if ( myPlayers[i].getId() != currentClosest.getId() ) {
					support(assets.getPlayer(currentClosest.getId()), myPlayers[i], currentPoints);
				}
			}
		}
		else {
			Player stopper = getStopper(nextPercept);
			goToBall(stopper, nextPercept);
			
			opponents.remove(assets.getBall().getOwner());
			
			for(int i = 0; i < myPlayers.length; i++) {
				if ( myPlayers[i].getId() != stopper.getId() ) {
					Player closestOpponent = nextPercept.getPlayerClosestToObject(opponents.toArray(new Player[0]), myPlayers[i].getPosition());
					if ( closestOpponent != null && assets.getPitch().getHalfCourt(this.getId()).contains(closestOpponent.getPosition().toPoint()) ) {
						mark(myPlayers[i], closestOpponent);
						opponents.remove(closestOpponent);
					}
					else {
						goToBall(myPlayers[i], nextPercept);
					}
				}
			}
		}
	}
	
	private ArrayList<vector2D> initializeSupportPoints() {
		ArrayList<vector2D> points = new ArrayList<vector2D>();
		double deltaX = new Pitch().getPitchWidth() / 12;
		double deltaY = new Pitch().getPitchHeight() / 12;
		
		for(int i = 1; i < 11; i++) {
			for(int j = 1; j < 11; j++) {
				points.add(new vector2D(i*deltaX, j*deltaY));
			}
		}
		
		return points;
	}
	
	
	private ArrayList<vector2D> getSupportPoints() {
		ArrayList<vector2D> points = new ArrayList<vector2D>();
		
		double furthestX = assets.getPlayerClosestToObject(myPlayers, 
				new vector2D(myGoalPost.getCenterX(), myGoalPost.getCenterY())).getPosition().getX();
		Rectangle2D.Double supportArea = new Rectangle2D.Double(Math.min(opponentGoalPost.getX(), furthestX), 
				0, Math.abs(opponentGoalPost.getX() - furthestX), assets.getPitch().getPitchHeight());

		for(int i = 0; i < supportPoints.size(); i++) {
			if ( supportArea.contains(supportPoints.get(i).toPoint()) ) {
				points.add(supportPoints.get(i));
			}
		}
		
		return points;
	}
	
	private void decideTactic() {
		tactic = assets.getScore(this.getId()) - assets.getScore(opponentID);
	}
	
	private void mark(Player myPlayer, Player opponent) {
		if ( myPlayer != null && opponent != null ) {
			Rectangle2D.Double myGoalPost = assets.getPitch().getGoalPost(this.getId());
			vector2D mgpCenter = new vector2D(myGoalPost.getCenterX(), myGoalPost.getCenterY());
			double goalAngle = opponent.getPosition().subtract(mgpCenter).getAngle();
			vector2D movePoint = new vector2D(opponent.getPosition().getX() - MARKING_DISTANCE * Math.cos(goalAngle), opponent.getPosition().getY() - 10 * Math.sin(goalAngle));
			myPlayer.move(movePoint);
		}
	}
	
	private int getPlayerType(Player p) {
		return p.getShootAbility() - p.getTackleAbility();
	}
	
	private void support(Player owner, Player supporter, ArrayList<vector2D> supportPoints) {
		if ( owner != null && supporter != null ) {
			if ( supportPoints == null || supportPoints.size() == 0 ) {
				supporter.stop();
			}
			else {
				vector2D formationPoint = supporter.getStartupPosition().add(owner.getPosition().subtract(owner.getStartupPosition()));
				vector2D supportPoint = supportPoints.get(0);
				if ( getPlayerType(supporter) > 0 ) {	// Attacker
					if ( tactic < 0 ) {
						for(int i = 1; i < supportPoints.size(); i++) {
							int numberOfPlayersAround = assets.getPlayersInRange(supportPoint, SAFE_RANGE, opponentID).length;
							int numberOfPlayersOnLine = assets.getClosestPlayersToLine(supportPoint, owner.getPosition(), SAFE_RANGE, opponentID).length;
							vector2D ocp = new vector2D(opponentGoalPost.getCenterX(), opponentGoalPost.getCenterY());
							double postDistance1 = supporter.getPosition().measureDistance(ocp);
							int playersAround = assets.getPlayersInRange(supportPoints.get(i), SAFE_RANGE, opponentID).length;
							int playersOnLine = assets.getClosestPlayersToLine(supportPoints.get(i), owner.getPosition(), SAFE_RANGE, opponentID).length;
							double postDistance2 = supportPoints.get(i).measureDistance(ocp);
							double distance1 = supportPoint.measureDistance(formationPoint);
							double distance2 = supportPoints.get(i).measureDistance(formationPoint);
							if ( 17*(postDistance2-postDistance1) + 11 * (distance2 - distance1) + 5*(playersAround - numberOfPlayersAround) + 7*(playersOnLine - numberOfPlayersOnLine) < 0 ) {
								supportPoint = supportPoints.get(i);
							}
						}
					}
					else {
						for(int i = 1; i < supportPoints.size(); i++) {
							int numberOfPlayersAround = assets.getPlayersInRange(supportPoint, SAFE_RANGE, opponentID).length;
							int numberOfPlayersOnLine = assets.getClosestPlayersToLine(supportPoint, owner.getPosition(), SAFE_RANGE, opponentID).length;
							vector2D ocp = new vector2D(opponentGoalPost.getCenterX(), opponentGoalPost.getCenterY());
							double postDistance1 = supporter.getPosition().measureDistance(ocp);
							int playersAround = assets.getPlayersInRange(supportPoints.get(i), SAFE_RANGE, opponentID).length;
							int playersOnLine = assets.getClosestPlayersToLine(supportPoints.get(i), owner.getPosition(), SAFE_RANGE, opponentID).length;
							double postDistance2 = supportPoints.get(i).measureDistance(ocp);
							double distance1 = supportPoint.measureDistance(formationPoint);
							double distance2 = supportPoints.get(i).measureDistance(formationPoint);
							if ( 17*(postDistance2-postDistance1) + 11 * (distance2 - distance1) + 9*(playersAround - numberOfPlayersAround) + 7*(playersOnLine - numberOfPlayersOnLine) < 0 ) {
								supportPoint = supportPoints.get(i);
							}
						}
					}
				}
				else {									// Defender
					if ( tactic > 0 ) {
						for(int i = 1; i < supportPoints.size(); i++) {
							int numberOfPlayersAround = assets.getPlayersInRange(supportPoint, SAFE_RANGE, opponentID).length;
							int numberOfPlayersOnLine = assets.getClosestPlayersToLine(supportPoint, owner.getPosition(), SAFE_RANGE, opponentID).length;
							vector2D ocp = new vector2D(opponentGoalPost.getCenterX(), opponentGoalPost.getCenterY());
							double postDistance1 = supporter.getPosition().measureDistance(ocp);
							int playersAround = assets.getPlayersInRange(supportPoints.get(i), SAFE_RANGE, opponentID).length;
							int playersOnLine = assets.getClosestPlayersToLine(supportPoints.get(i), owner.getPosition(), SAFE_RANGE, opponentID).length;
							double postDistance2 = supportPoints.get(i).measureDistance(ocp);
							double distance1 = supportPoint.measureDistance(formationPoint);
							double distance2 = supportPoints.get(i).measureDistance(formationPoint);
							if ( 17*(postDistance2-postDistance1) + 11 * (distance2 - distance1) + 9*(playersAround - numberOfPlayersAround) + 7*(playersOnLine - numberOfPlayersOnLine) < 0 ) {
								supportPoint = supportPoints.get(i);
							}
						}
					}
					else {
						for(int i = 1; i < supportPoints.size(); i++) {
							int numberOfPlayersAround = assets.getPlayersInRange(supportPoint, SAFE_RANGE, opponentID).length;
							int numberOfPlayersOnLine = assets.getClosestPlayersToLine(supportPoint, owner.getPosition(), SAFE_RANGE, opponentID).length;
							vector2D ocp = new vector2D(opponentGoalPost.getCenterX(), opponentGoalPost.getCenterY());
							double postDistance1 = supporter.getPosition().measureDistance(ocp);
							int playersAround = assets.getPlayersInRange(supportPoints.get(i), SAFE_RANGE, opponentID).length;
							int playersOnLine = assets.getClosestPlayersToLine(supportPoints.get(i), owner.getPosition(), SAFE_RANGE, opponentID).length;
							double postDistance2 = supportPoints.get(i).measureDistance(ocp);
							double distance1 = supportPoint.measureDistance(formationPoint);
							double distance2 = supportPoints.get(i).measureDistance(formationPoint);
							if ( 17*(postDistance2-postDistance1) + 11 * (distance2 - distance1) + 7*(playersAround - numberOfPlayersAround) + 9*(playersOnLine - numberOfPlayersOnLine) < 0 ) {
								supportPoint = supportPoints.get(i);
							}
						}
					}
				}
				supporter.move(supportPoint);
				supportPoints.remove(supportPoint);
			}
		}
	}
}
