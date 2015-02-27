package common;
import java.util.ArrayList;

import simulator.PlayerInfo;


public class Percept {
	
	private Ball ball;
	private int id;
	private ArrayList<PlayerInfo> playerInfoList;
	private int team1Score, team2Score;
	private Highlight perceptHighlight;
	private static int count = 0;
    
    public Percept() {
    	this.ball = new Ball();
    	this.id = count;
    	count++;
    	this.playerInfoList = new ArrayList<PlayerInfo>();
    	this.team1Score = 0;
    	this.team2Score = 0;
    }
    
    public Percept(Ball b) {
    	this.ball = b;
    }
    
    public Ball getBall() {
    	return this.ball;
    }
    
    public ArrayList<PlayerInfo> getPlayerInfoList() {
    	return this.playerInfoList;
    }
    
    public void setPlayerInfoList(ArrayList<PlayerInfo> playerInfos) {
    	this.playerInfoList = playerInfos;
    }
    
    public int getTeam1Score() {
    	return this.team1Score;
    }
    
    public int getTeam2Score() {
    	return this.team2Score;
    }

    public void setTeam1Score(int team1Score) {
    	this.team1Score = team1Score;
    }
    
    public void setTeam2Score(int team2Score) {
    	this.team2Score = team2Score;
    }
    
    public Highlight getHighlight() {
    	return this.perceptHighlight;
    }
    
    public void setHighlight(Highlight newHighlight) {
    	this.perceptHighlight = newHighlight;
    }
}
