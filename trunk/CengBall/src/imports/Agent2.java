package imports;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

import rare.Pitch;
import rare.PlayerAction;
import rare.vector2D;

import common.MatchAssets;
import common.MovingObject;
import common.Player;
import common.Team;

public class Agent2 extends Team {
	private static String teamName = "Besiktas";
	private static Color teamColor1 = new Color(0, 0, 0), teamColor2 = new Color(255, 255, 255);
	private static String[] names = {"Sergen", "Metin", "Ahmet", "Feyyaz", "Pascal"};
	private Rectangle2D.Double myGoalPost, opponentGoalPost;
	private Player[] myPlayers, opponentPlayers;
	private int[] defenderIDs, midfielderIDs, attackerIDs;
	private int opponentID;
	private int PREDICTION_DISTANCE = 30, MARKING_DISTANCE = 6, SHOOT_DISTANCE = 30;
	private double SAFE_RANGE = 10, SAFE_ANGLE = Math.PI / 6; // 30 degrees
	private PlayerAction lastBallAction;
	
	public Agent2() {
		super(teamName, teamColor1, teamColor2);
	}

	@Override
	public void move() {
		myGoalPost = assets.getPitch().getGoalPost(this.getId());
		opponentGoalPost = assets.getPitch().getOtherGoalPost(this.getId());
		opponentID = assets.getOtherTeamID(this.getId());
		MatchAssets nextPercept = assets.advance(PREDICTION_DISTANCE);
		myPlayers = assets.getPlayerList(this.getId());
		opponentPlayers = assets.getPlayerList(assets.getOtherTeamID(this.getId()));
		Player owner = assets.getBall().getOwner();
		if ( owner != null ) {
			if ( owner.getTeamID() == this.getId() ) {	// I have the ball
				vector2D shootPoint = assets.getClosestPoint(opponentGoalPost, owner.getPosition());
				Player pToPass = null;
				if ( (shootPoint.measureDistance(owner.getPosition()) < SHOOT_DISTANCE) ) {
					owner.shoot(shootPoint);
					lastBallAction = owner.getIntent();
				}
				else {
					this.moveWithBall(owner);
					lastBallAction = owner.getIntent();
				}
				for(int i = 0; i < myPlayers.length; i++) {
					if ( myPlayers[i].getId() != assets.getBall().getOwner().getId() || (pToPass != null && pToPass.getId() != myPlayers[i].getId())) {
						vector2D ownerMovement = owner.getPosition().subtract(owner.getStartupPosition());
						myPlayers[i].move(myPlayers[i].getStartupPosition().add(ownerMovement));
					}
				}
			}
			else {	// Opponent has the ball
				manToManMarking(nextPercept);
			}
		}
		else {	// Noone has the ball
			Player closestPlayer = assets.getPlayerClosestToBall(myPlayers);
			if ( closestPlayer.getDistanceFrom(assets.getBall()) < assets.getBall().getTrapDistance() ) {
				closestPlayer.trap();
			}
			else {
				closestPlayer.move(nextPercept.getBall().getPosition());
			}
			
			for(int i = 0; i < myPlayers.length; i++) {
				if ( myPlayers[i].getId() != closestPlayer.getId() ) {
					mark(myPlayers[i], assets.getPlayer( nextPercept.getPlayerClosestToObject(nextPercept.getPlayerList(opponentID), myPlayers[i].getPosition()).getId()) );
				}
			}
		}
	}
	
	private Player getStopper(MovingObject target, MatchAssets newAssets) {
		if ( target != null && newAssets != null ) {
			Rectangle2D.Double betweenArea;
			if ( target.getPosition().getX() > myGoalPost.getX() ) {
				betweenArea = new Rectangle2D.Double(0, 0, Math.abs(target.getPosition().getX() - MARKING_DISTANCE), assets.getPitch().getPitchHeight());
			}
			else {
				betweenArea = new Rectangle2D.Double(target.getPosition().getX() + MARKING_DISTANCE, 0, Math.abs(myGoalPost.getX()-target.getPosition().getX()- MARKING_DISTANCE) , assets.getPitch().getPitchHeight());
			}
			Player[] betweenPlayers = newAssets.getPlayersInArea(betweenArea, this.getId());
			if ( betweenPlayers != null && betweenPlayers.length != 0 ) {
				return assets.getPlayer(newAssets.getPlayerClosestToObject(betweenPlayers, target.getPosition()).getId());
			}
			else {
				return assets.getPlayer( newAssets.getPlayerClosestToObject(newAssets.getPlayerList(this.getId()), target.getPosition()).getId() );
			}
		}
		return null;
	}
	
	private void moveWithBall(Player owner) {
		Player[] closestOpponents = assets.getPlayersInRange(owner.getPosition(), SAFE_RANGE, opponentID);
		double goalAngle = assets.getClosestPoint(opponentGoalPost, owner.getPosition()).subtract(owner.getPosition()).getAngle();
		double moveAngle = goalAngle;
		if ( closestOpponents!= null && closestOpponents.length > 0 ) {
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
	
	
	private void mark(Player myPlayer, Player opponent) {
		Rectangle2D.Double myGoalPost = assets.getPitch().getGoalPost(this.getId());
		vector2D mgpCenter = new vector2D(myGoalPost.getCenterX(), myGoalPost.getCenterY());
		double goalAngle = opponent.getPosition().subtract(mgpCenter).getAngle();
		vector2D movePoint = new vector2D(opponent.getPosition().getX() - MARKING_DISTANCE * Math.cos(goalAngle), opponent.getPosition().getY() - 10 * Math.sin(goalAngle));
		myPlayer.move(movePoint);
	}
	
	private void manToManMarking(MatchAssets nextPercept) {
		ArrayList<Player> opponents = new ArrayList<Player>(Arrays.asList(opponentPlayers));
		Player closestPlayer = getStopper(assets.getBall(), nextPercept);
		if ( closestPlayer != null ) {
			if ( closestPlayer.getDistanceFrom(assets.getBall()) < assets.getBall().getTrapDistance() ) {
				assets.getPlayer(closestPlayer.getId()).trap();
			}
			else {
				closestPlayer.move(assets.getBall().getPosition());
			}
			opponents.remove(assets.getBall().getOwner());
		}		
		
		for(int i = 0; i < myPlayers.length; i++) {
			if ( myPlayers[i].getId() != closestPlayer.getId() ) {
				Player closestOpponent = assets.getPlayer( nextPercept.getPlayerClosestToObject(opponents.toArray(new Player[0]), myPlayers[i].getPosition()).getId() );
				if ( closestOpponent != null && assets.getPitch().getHalfCourt(this.getId()).contains(closestOpponent.getPosition().toPoint()) ) {
					mark(myPlayers[i], closestOpponent);
					opponents.remove(closestOpponent);
				}
				else {
					myPlayers[i].move(assets.getBall().getPosition());
				}
			}
		}
	}

	@Override
	public Player[] assembleTeam(int numberOfPlayers, Rectangle2D.Double halfCourt) {
		Player[] myPlayers = new Player[numberOfPlayers];
		double pDistance1 = (halfCourt.getHeight() - (2*numberOfPlayers/5) * Pitch.DEFAULT_PLAYER_RADIUS * 2) / ((2*numberOfPlayers/5) + 1 );
		int remainingPlayers = numberOfPlayers - 2*(2*numberOfPlayers/5);
		double pDistance2 = (halfCourt.getHeight() - (remainingPlayers) * Pitch.DEFAULT_PLAYER_RADIUS * 2) / (remainingPlayers + 1 );
		double pDistance3 = (halfCourt.getWidth() - numberOfPlayers*Pitch.DEFAULT_PLAYER_RADIUS * 2) / (numberOfPlayers+1);
		int playerIndex = 0;
		if ( halfCourt.getX() == 0 ) {
			for (int i = 0; i < (2*numberOfPlayers/5); i++) {
				myPlayers[playerIndex] = new Player(names[playerIndex%5], playerIndex+1, this.getId(), new vector2D(halfCourt.getWidth() - (pDistance3),pDistance1*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						(15),	// speed
						(10),	// shoot
						(10),	// pass
						(10),	// dribbling
						(5),		// stamina
						(10));	// tackle
				playerIndex++;
			}
			for (int i = 0; i < (2*numberOfPlayers/5); i++) {
				myPlayers[playerIndex] = new Player(names[playerIndex%5], playerIndex+1, this.getId(), new vector2D(halfCourt.getWidth() - (pDistance3*3),pDistance1*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						(10),
						(5),
						(10),
						(10),
						(15),
						(10));
				playerIndex++;
			}
			for (int i = 0; i < remainingPlayers; i++) {
				myPlayers[playerIndex] = new Player(names[playerIndex%5], playerIndex+1, this.getId(), new vector2D(halfCourt.getWidth() - (pDistance3*2),pDistance2*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						(10),
						(10),
						(5),
						(10),
						(20),
						(5));
				playerIndex++;
			}
		}
		else {
			for (int i = 0; i < (2*numberOfPlayers/5); i++) {
				myPlayers[playerIndex] = new Player(names[playerIndex%5], playerIndex+1, this.getId(), new vector2D(halfCourt.getX() + (pDistance3),pDistance1*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						(15),
						(10),
						(10),
						(10),
						(5),
						(10));
				playerIndex++;
			}
			for (int i = 0; i < (2*numberOfPlayers/5); i++) {
				myPlayers[playerIndex] = new Player(names[playerIndex%5], playerIndex+1, this.getId(), new vector2D(halfCourt.getX() + (pDistance3*3),pDistance1*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						(10),
						(10),
						(10),
						(10),
						(10),
						(10));
				playerIndex++;
			}
			for (int i = 0; i < remainingPlayers; i++) {
				myPlayers[playerIndex] = new Player(names[playerIndex%5], playerIndex+1, this.getId(), new vector2D(halfCourt.getX() + (pDistance3*2),pDistance2*(i+1) + Pitch.DEFAULT_PLAYER_RADIUS), 
						(5),
						(10),
						(10),
						(10),
						(20),
						(5));
				playerIndex++;
			}
		}
				
		return myPlayers;
	}
}
