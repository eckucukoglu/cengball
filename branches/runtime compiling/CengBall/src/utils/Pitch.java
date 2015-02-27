package utils;

import common.Coord;

public class Pitch {
	private Coord standard;
	
	public static final double DEFAULT_RADIUS = 9.15;
	public static final double DEFAULT_CORNER_RADIUS = 1.0;
	public static final double DEFAULT_DOT_RADIUS = 0.2;
	public static final double DEFAULT_POST_HEIGHT = 7.32;
	public static final double DEFAULT_POST_WIDTH = 2.0;
	public static final double DEFAULT_GOAL_AREA_LINE_WIDTH = 5.5;
	public static final double DEFAULT_GOAL_AREA_LINE_HEIGHT = 18.32; // standardGoalAreaLineW * 2 + standardPostH;
	public static final double DEFAULT_PENALTY_AREA_WIDTH = 16.5;
	public static final double DEFAULT_PENALTY_AREA_HEIGHT = 33.0; // standardPenaltyAreaW * 2;
	public static final double DEFAULT_DOT_DISTANCE = 11.0;
	public static final double DEFAULT_PLAYER_RADIUS = 1.0;
	public static final double DEFAULT_BALL_RADIUS = 0.5;
	
	public Pitch () {
		// Dimensions of Santiago Bernabeu
		Coord pitchCoord = new Coord (108, 72);
		standard = pitchCoord;
	}
	
	public Pitch (int w, int h) {
		Coord pitchCoord = new Coord (w, h);
		standard = pitchCoord;
	}
	
	public int getPitchWidth() {
		return standard.getW();
	}
	
	public int getPitchHeight() {
		return standard.getH();
	}
	
	public double calculateRatio (int windowWidth, int windowHeight) {
		double widthRatio = ( ((double) windowWidth - (10 + DEFAULT_POST_WIDTH*2 ) ) / getPitchWidth() );
		double heightRatio = ( ((double) windowHeight - 10) / getPitchHeight() );

		return Math.min(widthRatio, heightRatio);
	}
}
