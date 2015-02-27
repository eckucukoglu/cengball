package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Metadata {
	
	private TeamInfo team1;
	private TeamInfo team2;
	
	private ArrayList<PlayerInfo_> players;
	
	private int pitchWidth;
	private int pitchHeight;
	
	public Metadata() {
		this.team1 = new TeamInfo();
		this.team2 = new TeamInfo();
		this.pitchWidth = 108;
		this.pitchHeight = 72;
		this.players = new ArrayList<PlayerInfo_>();
		
		for(int i = 0; i < 5; ++i){
			players.add(new PlayerInfo_());
		}
	}
	
	public Metadata(TeamInfo team1, TeamInfo team2, ArrayList<PlayerInfo_> playerInfolist) {
		this.team1 = team1;
		this.team2 = team2;
		this.pitchWidth = 108;
		this.pitchHeight = 72;
		this.players = playerInfolist;
	}
	
	public void writeToFile(String path) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(new File(path)));
			String json = gson.toJson(this);
			writer.write(json);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public PlayerInfo_ getPlayer(int id) {
		for (PlayerInfo_ player : players) {
			if(player.getId() == id){
				return player;
			}
		}
		
		return null;
	}

	public TeamInfo getTeam1() {
		return team1;
	}

	public TeamInfo getTeam2() {
		return team2;
	}

	public int getPitchWidth() {
		return pitchWidth;
	}

	public int getPitchHeight() {
		return pitchHeight;
	}
	
}
