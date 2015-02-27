package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.CBLogger;
import utils.Enums;
import utils.FileUtils;
import utils.Utils;

public class KickOffPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel team1Panel, team2Panel, settingsPanel;
	private DefaultButton backButton, startMatchButton;
	private JLabel settingsLabel, homeLabel, awayLabel, matchLengthText, fatigueText, playerNumberText;
	private Selector matchLengthSelector,fatigueSelector, playerNumberSelector;
	private TeamSelector homeSelector, awaySelector;
	private String[] importedTeams;
	private Image backgroundImage, backgroundOrg;
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();

	public KickOffPanel() {
		setBackground(Enums.black);
		setLayout(null);
		
		double widthRatio = Utils.getScreenWidthRatio((int) Utils.getScreenDimension().getWidth());

		team1Panel = new JPanel();
		team1Panel.setLayout(null);
		team1Panel.setBackground(Enums.transSgigray);
		this.add(team1Panel);
		
		homeLabel = new JLabel("Home");
		homeLabel.setForeground(Enums.white);
		homeLabel.setBackground(null);
		homeLabel.setFont(FileUtils.getFont("Zekton.ttf", (int) (18 * widthRatio), Font.PLAIN));
		team1Panel.add(homeLabel);
		
		importedTeams = FileUtils.getImports();
		homeSelector = new TeamSelector(importedTeams);
		team1Panel.add(homeSelector);
		
		team2Panel = new JPanel();
		team2Panel.setLayout(null);
		team2Panel.setBackground(Enums.transSgigray);
		this.add(team2Panel);
		
		awayLabel = new JLabel("Away");
		awayLabel.setForeground(Enums.white);
		awayLabel.setBackground(null);
		awayLabel.setFont(FileUtils.getFont("Zekton.ttf", (int) (18 * widthRatio), Font.PLAIN));
		team2Panel.add(awayLabel);
		
		awaySelector = new TeamSelector(importedTeams);
		team2Panel.add(awaySelector);
		
		settingsPanel = new JPanel();
		settingsPanel.setLayout(null);
		settingsPanel.setBackground(Enums.transSgigray);
		this.add(settingsPanel);
		
		settingsLabel = new JLabel("Match Settings");
		settingsLabel.setForeground(Enums.white);
		settingsLabel.setBackground(null);
		settingsLabel.setFont(FileUtils.getFont("Zekton.ttf", (int) (18 * widthRatio), Font.PLAIN));
		settingsPanel.add(settingsLabel);
		
		matchLengthText = new JLabel("Match Length");
		matchLengthText.setForeground(Enums.white);
		matchLengthText.setBackground(null);
		matchLengthText.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		settingsPanel.add(matchLengthText);
		
		fatigueText = new JLabel("Player Fatigue");
		fatigueText.setForeground(Enums.white);
		fatigueText.setBackground(null);
		fatigueText.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		settingsPanel.add(fatigueText);
		
		matchLengthSelector = new Selector(Enums.DEFAULT_MATCH_LENGTHS);
		settingsPanel.add(matchLengthSelector);
		
		String[] fatigueOptions = {"ON", "OFF"};
		fatigueSelector = new Selector(fatigueOptions);
		settingsPanel.add(fatigueSelector);
		
		playerNumberText = new JLabel("Number of Players");
		playerNumberText.setForeground(Enums.white);
		playerNumberText.setBackground(null);
		playerNumberText.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		settingsPanel.add(playerNumberText);
		
		playerNumberSelector = new Selector(Enums.DEFAULT_PLAYER_NUMBERS);
		settingsPanel.add(playerNumberSelector);
		
		backButton = new DefaultButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("Menu");
				LOGGER.log(Level.INFO, "Returned back to Main Menu");
			}
		});
		this.add(backButton);
		
		startMatchButton = new DefaultButton("Start Match");
		startMatchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( (! homeSelector.getSelectedItem().equals("No item")) && (! awaySelector.getSelectedItem().equals("No item")) ) {
					( (MainPanel) getInstance().getParent() ).sendMatchInfo(homeSelector.getSelectedItem(), awaySelector.getSelectedItem(), Integer.parseInt(matchLengthSelector.getSelectedItem()), fatigueSelector.getSelectedItem() == "ON"
							,Integer.parseInt(playerNumberSelector.getSelectedItem()));
					( (MainPanel) getInstance().getParent() ).show("Simulate");
					LOGGER.log(Level.INFO, "Starting match");
				}
			}
		});
		this.add(startMatchButton);
		
		arrangeLayout();
	}

	public KickOffPanel getInstance() {
		return this;
	}
	
	public void refresh() {
		importedTeams = FileUtils.getImports();
		homeSelector.setElements(importedTeams);
		awaySelector.setElements(importedTeams);
		fatigueSelector.setSelectedItem(0);
		matchLengthSelector.setSelectedItem(0);
		playerNumberSelector.setSelectedItem(0);
		repaint();
	}
	
	private void arrangeLayout() { 
		Dimension screenDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenDimension.getWidth();
		int height = (int) screenDimension.getHeight();
		double sWRatio = Utils.getScreenWidthRatio(width);
		double sHRatio = Utils.getScreenHeightRatio(height);
		int widthCut = (int) (30 * sWRatio), heightCut = (int) (20 * sHRatio);
		int settingsPanelWidth = (width - (4*widthCut))/4;
		int panelHeight = (int) ((height - 3*heightCut)*0.9), teamPanelWidth = (int) (settingsPanelWidth*1.5);
		int buttonHeight = (int) ((height - 3*heightCut)*0.1);
		int buttonWidth = (int) (Enums.DEFAULT_BUTTON_WIDTH * sWRatio);

		team1Panel.setBounds(widthCut, heightCut, teamPanelWidth, panelHeight);
		settingsPanel.setBounds(teamPanelWidth + 2*widthCut, heightCut, settingsPanelWidth, panelHeight);
		team2Panel.setBounds(teamPanelWidth + 3*widthCut + settingsPanelWidth, heightCut, teamPanelWidth, panelHeight);
		backButton.setBounds(widthCut, panelHeight + (2*heightCut), buttonWidth, buttonHeight);
		startMatchButton.setBounds(width - widthCut - buttonWidth, panelHeight + (2*heightCut), buttonWidth, buttonHeight);
		settingsLabel.setBounds((int) ((settingsPanelWidth - settingsLabel.getPreferredSize().getWidth())/2), heightCut/3, settingsPanelWidth, buttonHeight);
		matchLengthText.setBounds((int) ((settingsPanelWidth - matchLengthText.getPreferredSize().getWidth())/2), heightCut*3, settingsPanelWidth, buttonHeight);
		matchLengthSelector.setBounds(widthCut, heightCut*6, settingsPanelWidth - 2*widthCut, heightCut*2);
		fatigueText.setBounds((int) ((settingsPanelWidth - fatigueText.getPreferredSize().getWidth())/2), heightCut*8, settingsPanelWidth, buttonHeight);
		fatigueSelector.setBounds(widthCut, heightCut*11, settingsPanelWidth - 2*widthCut, heightCut*2);
		playerNumberText.setBounds((int) ((settingsPanelWidth - playerNumberText.getPreferredSize().getWidth())/2), heightCut*13, settingsPanelWidth, buttonHeight);
		playerNumberSelector.setBounds(widthCut, heightCut*16, settingsPanelWidth - 2*widthCut, heightCut*2);
		homeLabel.setBounds((int) ((teamPanelWidth - homeLabel.getPreferredSize().getWidth())/2), heightCut/3, teamPanelWidth, buttonHeight);
		awayLabel.setBounds((int) ((teamPanelWidth - awayLabel.getPreferredSize().getWidth())/2), heightCut/3, teamPanelWidth, buttonHeight);
		homeSelector.setBounds(widthCut, heightCut*4, teamPanelWidth - 2*widthCut, panelHeight - 6*heightCut);
		awaySelector.setBounds(widthCut, heightCut*4, teamPanelWidth - 2*widthCut, panelHeight - 6*heightCut);
	}
	
	public void setBackground(Image img) {
		backgroundOrg = img;
		Dimension screenDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenDimension.getWidth();
		int height = (int) screenDimension.getHeight();
		if ( backgroundOrg != null ) {
			backgroundImage = width > 0 && height > 0 ? backgroundOrg.getScaledInstance(width, height, Image.SCALE_SMOOTH) : backgroundOrg;
		}
	}
	
	@Override
	public void paintComponent(final Graphics g) {
	    super.paintComponent(g);
	    if (backgroundImage != null)
	        g.drawImage(backgroundImage, 0, 0, null);
	}
}
