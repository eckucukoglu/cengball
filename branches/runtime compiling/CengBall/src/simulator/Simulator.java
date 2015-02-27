package simulator;
import common.SaveFile;

import utils.Pitch;

//
//  @ Project : CengBall
//  @ File Name : Simulator.java
//  @ Date : 11.11.2013
//  @ Author : Project Contorium
//
//


public class Simulator {
	private Team a;
	private Team b;
	private Pitch pitch;
	private SaveFile saveFile;
	
	public Simulator(Team a, Team b, Pitch pitch, String logFilePath) {
		this.a = a;
		this.b = b;
		this.pitch = pitch;
		this.saveFile = new SaveFile(logFilePath);
	}

	public void simulate() {
		
	}
}
