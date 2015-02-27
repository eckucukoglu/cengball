package common;

public class CommonEnums {
	public static final int DEFAULT_VIEW_ANGLE = 160; 
	// Maximum number for each attribute can be
	public static final int MAXIMUM_ATTRIBUTE_VALUE = 20;
	// Maximum number for any player's total attributes
	public static final int MAXIMUM_TOTAL_ATTRIBUTE_PER_PLAYER = 60;
	// Maximum number for player's fatigue attribute
	public static final double MAXIMUM_NUMBER_FATIGUE = 100.0;
	public static final int TRAP_DISTANCE = 3;
	
	public static enum ActionType {
		NONE, PASS, SHOOT, MOVE, TRAP, STOP
	}
}
