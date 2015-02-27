package common;

import java.awt.Color;


public class PlayerInfo_ {
	
	private int id;
	private String name;
	private int kitNumber;
	private int colorR, colorG, colorB;
	
	public PlayerInfo_() {
		this.id = 0;
		this.name = "I aint got no name!";
		this.colorR = this.colorG = this.colorB = 0;
		this.kitNumber = -1;
	}
	
	public PlayerInfo_(int id, String name, int kitNumber, Color color) {
		this.id = id;
		this.name = name;
		this.colorR = color.getRed();
		this.colorG = color.getGreen();
		this.colorB = color.getBlue();
		this.kitNumber = kitNumber;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getKitNumber() {
		return kitNumber;
	}
	
	public void setKitNumber(int kitNumber) {
		this.kitNumber = kitNumber;
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
