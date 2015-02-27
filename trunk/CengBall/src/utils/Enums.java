package utils;

import java.awt.Color;

public class Enums {
	public static final int DEFAULT_SCREEN_WIDTH = 1024;
	public static final int DEFAULT_SCREEN_HEIGHT = 768;
	public static final int DEFAULT_BUTTON_WIDTH = 200;
	public static final int DEFAULT_BUTTON_HEIGHT = 60;
	public static final Color white = new Color(255,255,255);
	public static final Color black = new Color(0,0,0);
	public static final Color indianRed = new Color(205,85,85);
	public static final Color brown = new Color(139,35,35);
	public static final Color sgigray = new Color(142,142,142);
	public static final Color gray83 = new Color(212,212,212);
	public static final Color darkslategray = new Color(47,79,79);
	public static final Color transSgigray = new Color(142,142,142, 200);
	public static final Color red = new Color(255,0,0);
	public static final Color blue = new Color(0,0,255);
	public static final Color green = new Color(0,255,0);
	public static final Color darkgreen = new Color(0,100,0);
	public static final Color forestgreen = new Color(34, 139, 34);
	
	public static enum HighlightType {
		NONE, START, END, SHOOT, PASS, GOAL, TACKLE, DRIBBLE, MOVEWITHBALL, OWN
	}
	
	public enum ObjectType {
		BALL,
		PLAYER
	}
	
	public static final double NOTICABLE_COLOR_DISTANCE = 20.0;
	
	public static final double DEFAULT_INELASTIC_COLLISION_LOSS = 0.7;
	
	public static final int DEFAULT_FONT_SIZE = 9;
	
	public static final int DEFAULT_SPLASH_TIME = 6000;
	public static final int DEFAULT_SCORE_DELAY = 200;
	
	public static final int DEFAULT_SLEEP_TIME = 50;	// In miliseconds
	public static final int DEFAULT_FPS = 60;
	public static final int DEFAULT_TURN_RATIO = 10;
	public static final String[] DEFAULT_MATCH_LENGTHS = {"2","3","5"};	// In minutes
	public static final String[] DEFAULT_PLAYER_NUMBERS = {"5","6","7"};
	public static final int MAXIMUM_PLAYER_SPEED = 10;	// meters per second
	public static final int MAXIMUM_BALL_SPEED = 30;	// meters per second
	
	// Minimum number for fatigue loss per match for player which has best stamina attribute
	public static final double MINIMUM_FATIGUE_LOSS_PER_MATCH = 20.0;
	// Maximum number for fatigue loss per match for player which has worst stamina attribute
	public static final double MAXIMUM_FATIGUE_LOSS_PER_MATCH = 60.0;
	
	public static final double COVERED_DISTANCE_WEIGHT_FOR_FATIGUE_CALCULATION = 0.5;
	
	public static enum ExceptionType {
		MAXIMUM_ATTRIBUTE_EXCEED,
		MAXIMUM_SKILL_POINT_PER_TEAM_EXCEED,
		MAXIMUM_FATIGUE_EXCEED,
		NOT_OWNER_OF_BALL,
		NOT_IN_TRAP_AREA,
		NOT_IN_VIEW,
		STATISTIC_ERROR,
	}
}
