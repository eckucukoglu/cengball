package common;

import java.util.logging.Logger;

import rare.Pitch;
import rare.PlayerAction;
import rare.vector2D;
import utils.CBLogger;
import utils.Enums;

/**
 * This class represents the players in a match.
 *
 */
public class Player extends MovingObject {
	// static attributes
	private String name;
	private int kitNumber;
	private int teamID, id;
	private transient static int count = 0;
	private transient vector2D startupPosition;
	private transient int speed;
	private transient int shoot;
	private transient int pass;
	private transient int dribbling;
	private transient int stamina;
	private transient int tackle;
	private transient PlayerAction intent;
	
	// dynamic attributes
	private double fatigue;
	
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();
	
	/**
	 * Constructor for Player class.
	 * @param name	the name of the player
	 * @param kitnumber	the kit number of the player
	 * @param teamid	the team ID of the team that the player belongs to.
	 * @param startPos	the starting position of the player.
	 * @param speed		the speed ability of the player.
	 * @param shoot		the shoot ability of the player.
	 * @param pass		the pass ability of the player.
	 * @param dribbling	the dribbling  ability of the player.
	 * @param stamina	the stamina of the player.
	 * @param tackle	the tackle ability of the player.
	 */
	public Player (String name, int kitnumber, int teamid, 
			vector2D startPos,
			int speed, 
			int shoot, 
			int pass, 
			int dribbling, 
			int stamina, 
			int tackle) {
		super(startPos, new vector2D(0,0), Pitch.DEFAULT_PLAYER_RADIUS, Enums.ObjectType.PLAYER);
		this.name = name;
		this.kitNumber = kitnumber;
		this.teamID = teamid;
		this.startupPosition = startPos;
		this.id = count;
		count++;
		
		if (shoot > CommonEnums.MAXIMUM_ATTRIBUTE_VALUE)
			shoot = CommonEnums.MAXIMUM_ATTRIBUTE_VALUE;
		if (pass > CommonEnums.MAXIMUM_ATTRIBUTE_VALUE)
			pass = CommonEnums.MAXIMUM_ATTRIBUTE_VALUE;
		if (speed > CommonEnums.MAXIMUM_ATTRIBUTE_VALUE)
			speed = CommonEnums.MAXIMUM_ATTRIBUTE_VALUE;
		if (dribbling > CommonEnums.MAXIMUM_ATTRIBUTE_VALUE)
			dribbling = CommonEnums.MAXIMUM_ATTRIBUTE_VALUE;
		if (tackle > CommonEnums.MAXIMUM_ATTRIBUTE_VALUE)
			tackle = CommonEnums.MAXIMUM_ATTRIBUTE_VALUE;
		if (stamina > CommonEnums.MAXIMUM_ATTRIBUTE_VALUE)
			stamina = CommonEnums.MAXIMUM_ATTRIBUTE_VALUE;
		
		int totalSkillPoint = shoot + pass + speed + dribbling + tackle + stamina;
		if (totalSkillPoint > CommonEnums.MAXIMUM_TOTAL_ATTRIBUTE_PER_PLAYER) {
			shoot = 10;
			pass = 10;
			stamina = 10;
			speed = 10;
			dribbling = 10;
			tackle = 10;
		}
		
		this.shoot = shoot;
		this.pass = pass;
		this.speed = speed;
		this.dribbling = dribbling;
		this.stamina = stamina;
		this.tackle = tackle;
		this.fatigue = CommonEnums.MAXIMUM_NUMBER_FATIGUE;
		
		this.intent = new PlayerAction(CommonEnums.ActionType.NONE);
	}

	/**
	 * Constructor for Player class with a given ID.
	 * @param id	the id of the player.
	 * @param name	the name of the player
	 * @param kitnumber	the kit number of the player
	 * @param teamid	the team ID of the team that the player belongs to.
	 * @param startPos	the starting position of the player.
	 * @param speed		the speed ability of the player.
	 * @param shoot		the shoot ability of the player.
	 * @param pass		the pass ability of the player.
	 * @param dribbling	the dribbling ability of the player.
	 * @param stamina	the stamina of the player.
	 * @param tackle	the tackle ability of the player.
	 */
	public Player (int id, String name, int kitnumber, int teamid, 
			vector2D startPos,
			int speed, 
			int shoot, 
			int pass,
			int dribbling, 
			int stamina, 
			int tackle) {
		super(startPos, new vector2D(0,0), Pitch.DEFAULT_PLAYER_RADIUS, Enums.ObjectType.PLAYER);
		this.name = name;
		this.kitNumber = kitnumber;
		this.teamID = teamid;
		this.startupPosition = startPos;
		this.id = id;
		
		this.shoot = shoot;
		this.pass = pass;
		this.speed = speed;
		this.dribbling = dribbling;
		this.stamina = stamina;
		this.tackle = tackle;
		this.fatigue = CommonEnums.MAXIMUM_NUMBER_FATIGUE;
		
		this.intent = new PlayerAction(CommonEnums.ActionType.NONE);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public final Player clone() {
		String newName = new String(this.name);
		vector2D newStartupPosition = this.startupPosition.clone();
		PlayerAction newIntent = this.intent.clone();
		vector2D newPosition = this.positionVector.clone();
		vector2D newSpeedVector = this.speedVector.clone();
		
		int speedSp = this.getSpeedAbility();
		int shootSp = this.getShootAbility();
		int passSp = this.getPassAbility();
		int dribblingSp = this.getDribblingAbility();
		int staminaSp = this.getStaminaAbility();
		int tackleSp = this.getTackleAbility();
		
		Player newPlayer = new Player(this.id, newName, this.kitNumber, this.teamID, newStartupPosition, 
				speedSp, shootSp, passSp, dribblingSp, staminaSp, tackleSp);
		
		newPlayer.setFatigueAbility(this.fatigue);
		newPlayer.setIntent(newIntent);
		newPlayer.setPosition(newPosition);
		newPlayer.setSpeed(newSpeedVector);
		
		return newPlayer;
	}

	/**
	 * Gets the intention of the player.
	 * @return	intention of the player.
	 */
	public PlayerAction getIntent() {
		return intent;
	}

	/**
	 * Sets the intention of the player.
	 * @param intent	intention to be set.
	 */
	public void setIntent(PlayerAction intent) {
		this.intent = intent;
	}

	/**
	 * Gets the team id of the player.
	 * @return teamID id of the player.
	 */
	public int getTeamID() {
		return teamID;
	}

	/**
	 * Sets the team id of the player.
	 * @param teamID team id to be set.
	 */
	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	/**
	 * Gets the name of the player.
	 * @return name of the player.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the players.
	 * @param name the name to be set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the kit number of the player.
	 * @return the kit number of the player.
	 */
	public int getKitNumber() {
		return kitNumber;
	}

	/**
	 * Sets the kit number of the player.
	 * @param kitNumber the kit number to be set.
	 */
	public void setKitNumber(int kitNumber) {
		this.kitNumber = kitNumber;
	}
	
	
	/**
	 * Clones the given player list.
	 * @param players	player list to be cloned.
	 * @return	cloned player list.
	 */
	public static Player[] clonePlayers(Player[] players) {
		if ( players != null ) {
			Player[] clonePlayers = new Player[players.length];
			for(int i = 0; i < players.length; i++) {
				clonePlayers[i] = players[i].clone();
			}
			return clonePlayers;
		}
		return null;
	}
	
	/** 
	 * Gets the speed ability of the player.
	 * @return the speed ability of the player.
	 */
	public int getSpeedAbility() {
		return speed;
	}

	/**
	 * Gets the pass ability of the player.
	 * @return the pass ability of the player.
	 */
	public int getPassAbility() {
		return pass;
	}

	/**
	 * Gets the shoot ability of the player.
	 * @return the shoot ability of the player.
	 */
	public int getShootAbility() {
		return shoot;
	}

	/**
	 * Gets the dribbling ability of the player.
	 * @return the dribbling ability of the player.
	 */
	public int getDribblingAbility() {
		return dribbling;
	}

	/**
	 * Gets the stamina ability of the player.
	 * @return the stamina ability of the player.
	 */
	public int getStaminaAbility() {
		return stamina;
	}
	
	/**
	 * Gets the tackle ability of the player.
	 * @return the tackle ability of the player.
	 */
	public int getTackleAbility() {
		return tackle;
	}

	/**
	 * Gets the fatigue ability of the player.
	 * @return the fatigue ability of the player.
	 */
	public double getFatigueAbility() {
		return fatigue;
	}

	/**
	 * Sets the fatigue ability of the player.
	 * @param fatigue the fatigue ability to be set. Fatigue can't be higher then a previously determined value.
	 */
	public void setFatigueAbility(double fatigue) {
		this.fatigue = fatigue;
	}
	
	
	/**
	 * Gets the distance from a moving object to the player.
	 * @param object the object, which the distance between the player to be calculated.
	 * @return the distance between the object and the player.
	 */
	public double getDistanceFrom (MovingObject object) {
		double distance = this.getPosition().measureDistance(object.getPosition());
		return distance;
	}
	
	/**
	 * Sets the player's intention to pass.
	 * @param where a vector which determines where the ball will be passed towards.
	 */
	public void pass(vector2D where) {
		PlayerAction intent = new PlayerAction(CommonEnums.ActionType.PASS, where);
		this.setIntent(intent);
	}
	
	/**
	 * Sets the player's intention to shoot.
	 * @param where a vector which determines where the ball will be shot towards.
	 */
	public void shoot(vector2D where) {
		PlayerAction intent = new PlayerAction(CommonEnums.ActionType.SHOOT, where);
		this.setIntent(intent);
	}
	
	/**
	 * Sets the player's intention to move.
	 * @param where a vector where the player will move towards.
	 */
	public void move(vector2D where) {
		PlayerAction intent = new PlayerAction(CommonEnums.ActionType.MOVE, where);
		this.setIntent(intent);
	}
	
	/**
	 * Sets the player's intention to trap the ball.(Capture the ball.)
	 * If there is a ball near the player, the player is going to try to capture the  ball.
	 */
	public void trap() {
		PlayerAction intent = new PlayerAction(CommonEnums.ActionType.TRAP);
		this.setIntent(intent);
	}
	
	/**
	 * Sets the player's intention to stop.
	 * Player does nothing when it's intention is stop.
	 */
	public void stop() {
		PlayerAction intent = new PlayerAction(CommonEnums.ActionType.STOP);
		this.setIntent(intent);
	}

	/**
	 * Gets the player's id.
	 * @return id of the player.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Creates id for the player.
	 * @return the id that is created.
	 */
	public static int createId() {
		int newID = count;
		count++;
		return newID;		
	}

	/**
	 * Gets the startup position of the player.
	 * @return the startup postion of the player.
	 */
	public vector2D getStartupPosition() {
		return startupPosition;
	}

	/**
	 * Sets the startup position of the player. Start up position determines where the player will be when the game starts and after each goal. 
	 * @param startupPosition	the starting position of the player
	 */
	public void setStartupPosition(vector2D startupPosition) {
		this.startupPosition = startupPosition;
	}

	
	/**
	 * Sets the id of the player.
	 * @param id the id to be set.
	 */
	public void setId(int id) {
		this.id = id;
	}
}
	