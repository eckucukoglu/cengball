package common;

import java.awt.Color;


public class TeamInfo {
	
	private int id;
	private String name;
	private int colorR, colorG, colorB;
	
	public TeamInfo() {
		this.id = 0;
		this.name = "NoName bitches!!11!!bir";
	}
	
	public TeamInfo(int id, String name, Color teamColor) {
		this.id = id;
		this.name = name;
		this.colorR = teamColor.getRed();
		this.colorG = teamColor.getGreen();
		this.colorB = teamColor.getBlue();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getColorR() {
		return colorR;
	}

	public void setColorR(int colorR) {
		this.colorR = colorR;
	}

	public int getColorG() {
		return colorG;
	}

	public void setColorG(int colorG) {
		this.colorG = colorG;
	}

	public int getColorB() {
		return colorB;
	}

	public void setColorB(int colorB) {
		this.colorB = colorB;
	}

}
