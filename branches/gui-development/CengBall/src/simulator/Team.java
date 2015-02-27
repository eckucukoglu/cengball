package simulator;
import java.util.ArrayList;

//
//  @ Project : CengBall
//  @ File Name : Team.java
//  @ Date : 11.11.2013
//  @ Author : Project Contorium
//
//

public class Team {
	
	public static final int COLOR_HOME = 1;
	public static final int COLOR_AWAY = 2;
	
	private ArrayList<Player> playerList;
	private String name;
	private int color;
	
	public Team(String name, int color) {
		this.name = name;
		this.color = color;
		this.playerList = new ArrayList<>();
	}
	
	public void distribute() {
		
	}
	
	public void move() {
		
	}
}
