package rare;


/**
 * This class is responsible for creating the chance factor in the game.
 *
 */
public class Chance {
	public static final double DEFAULT_CHANCE_THRESHOLD = 0.4;
	public static final double DEFAULT_ANGLE_COEFFICIENT = 3;
	public static final double DEFAULT_FATIGUE_COEFFICIENT = 4;
	public static final double DEFAULT_SKILL_COEFFICIENT = 5;
	public static final double MAXIMUM_ANGLE_DEVIATION = Math.PI / 6.0; 
	
	/**
	 * Calculates the state of a player with the given variables.
	 * @param fatigueRatio	the fatigue ratio of the player.
	 * @param skillRatio	the skill ratio of the player.
	 * @param angleRatio	the angle ratio of the intended motion.
	 * @return	state value in the range [-3, 9]
	 */
	public static final double calculateState(double fatigueRatio, double skillRatio, double angleRatio) {
		double angleChance = DEFAULT_ANGLE_COEFFICIENT * angleRatio;
		double fatigueChance = DEFAULT_FATIGUE_COEFFICIENT * fatigueRatio;
		double skillChance = DEFAULT_SKILL_COEFFICIENT * skillRatio;
		return skillChance + fatigueChance - angleChance;
	}
	
	/**
	 * Calculates the state of a player with the given variables.
	 * @param fatigueRatio	the fatigue ratio of the player.
	 * @param skillRatio	the skill ratio of the player.
	 * @return	state value in the range [0, 9]
	 */
	public static final double calculateState(double fatigueRatio, double skillRatio) {
		double fatigueChance = DEFAULT_FATIGUE_COEFFICIENT * fatigueRatio;
		double skillChance = DEFAULT_SKILL_COEFFICIENT * skillRatio;
		return skillChance + fatigueChance;
	}
	
	/**
	 * Applies the chance factor to the given vector with the given player state.
	 * @param v	vector of the motion.
	 * @param playerState	state of the player.
	 * @return	the vector that is affected by the chance factor.
	 */
	public static final vector2D applyChance(vector2D v, double playerState) {
		double stateRatio = playerState / (Chance.getMaximumState() - Chance.getMinimumState());
		double chance = (Math.random() + stateRatio) / 2.0;
		if ( chance < DEFAULT_CHANCE_THRESHOLD ) {
			double magnitude = v.getMagnitude();
			double angle = v.getAngle();
			chance += 0.5;	// To restore balance
			if ( Math.random() < 0.5 ) {	// Only angle deviation
				if ( Math.random() < 0.5 ) {
					angle += (1 - chance) * MAXIMUM_ANGLE_DEVIATION;
				}
				else {
					angle -= (1 - chance) * MAXIMUM_ANGLE_DEVIATION;
				}
			}
			else {	// Only power loss
				magnitude *= chance;
			}
			v = new vector2D(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
		}
		return v;
	}
	
	/**
	 * Applies the chance factor to the given player states.
	 * @param playerState1	state of the player 1.
	 * @param playerState2	state of the player 2.
	 * @return	chance difference of two players.
	 */
	public static final double applyChance(double playerState1, double playerState2) {
		double stateRatio1 = playerState1 / (Chance.getMaximumState() - Chance.getMinimumState());
		double stateRatio2 = playerState2 / (Chance.getMaximumState() - Chance.getMinimumState());
		double chance1 = (Math.random() + stateRatio1)/2.0, chance2 = (Math.random() + stateRatio2)/2.0;
		chance1 += 0.5;
		chance2 += 0.5;
		return chance1 - chance2;
	}
	
	/**
	 * Gets the maximum state value.
	 * @return	maximum state value.
	 */
	public static final double getMaximumState() {
		return DEFAULT_SKILL_COEFFICIENT + DEFAULT_FATIGUE_COEFFICIENT;
	}
	
	/**
	 * Gets the minimum state value.
	 * @return	minimum state value.
	 */
	public static final double getMinimumState() {
		return (-1) * DEFAULT_ANGLE_COEFFICIENT;
	}
}
