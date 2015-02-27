package gui;

import java.awt.CardLayout;
import java.awt.Image;

import javax.swing.JPanel;

import utils.Enums;
import utils.Utils;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private MenuPanel menuPanel;
	private KickOffPanel kickOffPanel;
	private MatchPanel matchPanel;
	private WatchPanel watchPanel;
	private SimulationPanel simulationPanel;
	private ImportPanel importPanel;
	private SettingsPanel settingsPanel;
	private MatchStatsPanel matchStatsPanel;
	private CardLayout mainCardLayout;

	public MainPanel() {
		super();
		mainCardLayout = new CardLayout(0, 0);
		setLayout(mainCardLayout);
		menuPanel = new MenuPanel();
		kickOffPanel = new KickOffPanel();
		matchPanel = new MatchPanel();
		watchPanel = new WatchPanel();
		simulationPanel = new SimulationPanel();
		importPanel = new ImportPanel();
		settingsPanel = new SettingsPanel();
		matchStatsPanel = new MatchStatsPanel();
		this.add(menuPanel, "Menu");
		this.add(matchPanel, "Match");
		this.add(kickOffPanel, "KickOff");
		this.add(watchPanel, "Watch");
		this.add(simulationPanel, "Simulate");
		this.add(importPanel, "Import");
		this.add(settingsPanel, "Settings");
		this.add(matchStatsPanel, "MatchStats");
		show("Menu");
	}
	
	public void show(String name) {
		if ( name == "Watch" ) {
			watchPanel.refresh();
		}
		else if ( name == "KickOff" ) {
			kickOffPanel.refresh();
		}
		else if ( name == "Import" ) {
			importPanel.refresh();
		}
		else if ( name == "Match" ) {
			matchPanel.refresh();
		}
		else if ( name == "Simulate" ) {
			simulationPanel.refresh();
		}
		else if ( name == "Settings" ) {
			settingsPanel.refresh();
		}
		else if ( name == "MatchStats" ) {
			matchStatsPanel.refresh();
		}
		mainCardLayout.show(this, name);
	}
	
	public void sendMatchInfo(String name1, String name2, int matchLength, boolean isFatigueOn, int numOfPlayers) {
		simulationPanel.setTeamNames(name1, name2);
		simulationPanel.setPerceptLimit(Utils.minutesToFrames(matchLength, Enums.DEFAULT_FPS));
		simulationPanel.setFatigueOption(isFatigueOn);
		simulationPanel.setNumberOfPlayers(numOfPlayers);
	}
	
	public void sendSaveFilePathToMatch(String saveFilePath) {
		matchPanel.setSaveFilePath(saveFilePath);
	}
	
	public void sendSaveFilePathToStats(String saveFilePath) {
		matchStatsPanel.setSaveFilePath(saveFilePath);
	}
	
	public void setBackgrounds(Image[] images) {
		menuPanel.setBackground(images[0]);
		kickOffPanel.setBackground(images[1]);
		importPanel.setBackground(images[2]);
		simulationPanel.setBackground(images[3]);
		watchPanel.setBackground(images[4]);
		settingsPanel.setBackground(images[5]);
		matchStatsPanel.setBackground(images[6]);
	}
}
