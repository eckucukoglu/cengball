package rare;

import java.awt.Color;


/**
 * This class hold the basic information about a team.
 *
 */
public class TeamInfo {
	private int id;
	private String name;
	private Color color1, color2;
	
	/**
	 * Constructor for the TeamInfo class.
	 * @param id id of the team.
	 * @param name name of the team.
	 * @param teamColor1 color of the team.
	 * @param teamColor2 color of the team.
	 */
	public TeamInfo(int id, String name, Color teamColor1, Color teamColor2) {
		this.id = id;
		this.name = name;
		this.color1 = teamColor1;
		this.color2 = teamColor2;
	}
	
	/**
	 * Gets the name of the team.
	 * @return name of the team.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the team.
	 * @param name the name to be set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the id of the team.
	 * @return id of the team.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the id of the team.
	 * @param id the id to be set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the first color of the team
	 * @return color of the team.
	 */
	public Color getColor1() {
		return color1;
	}

	/**
	 * Sets the first color of the team.
	 * @param color1 the color to be set.
	 */
	public void setColor1(Color color1) {
		this.color1 = color1;
	}

	/**
	 * Gets the second color of the team.
	 * @return the second color of the team.
	 */
	public Color getColor2() {
		return color2;
	}

	/**
	 * Sets the second color of the team.
	 * @param color2 the color to be set.
	 */
	public void setColor2(Color color2) {
		this.color2 = color2;
	}
	
}
