package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Enums;
import utils.FileUtils;
import utils.Utils;

public class KickOffPanel extends JPanel {
	private JPanel team1Panel, team2Panel, settingsPanel;
	private Image backgroundImage, backgroundOrg;
	private DefaultButton backButton, startMatchButton;
	private JLabel settingsLabel, homeLabel, awayLabel, matchLengthText, commentaryText, playerNumberText;
	private Selector matchLength, homeSelector, awaySelector, commentary, playerNumber;

	public KickOffPanel() {
		setBackground(Enums.black);
		setLayout(null);

		team1Panel = new JPanel();
		team1Panel.setLayout(null);
		team1Panel.setBackground(Enums.transSgigray);
		this.add(team1Panel);
		
		homeLabel = new JLabel("Home");
		homeLabel.setForeground(Enums.blue);
		homeLabel.setBackground(null);
		homeLabel.setFont(FileUtils.getFont("Zekton.ttf", 24, Font.PLAIN));
		team1Panel.add(homeLabel);
		
		// TODO : Temp lengths
		ArrayList<String> temp1 = new ArrayList<String>();
		temp1.add("Home1");
		temp1.add("Home2");
		temp1.add("Home3");
		homeSelector = new Selector(temp1);
		team1Panel.add(homeSelector);
		
		team2Panel = new JPanel();
		team2Panel.setLayout(null);
		team2Panel.setBackground(Enums.transSgigray);
		this.add(team2Panel);
		
		awayLabel = new JLabel("Away");
		awayLabel.setForeground(Enums.blue);
		awayLabel.setBackground(null);
		awayLabel.setFont(FileUtils.getFont("Zekton.ttf", 24, Font.PLAIN));
		team2Panel.add(awayLabel);
		
		// TODO : Temp lengths
		ArrayList<String> temp2 = new ArrayList<String>();
		temp2.add("Away1");
		temp2.add("Away2");
		temp2.add("Away3");
		awaySelector = new Selector(temp2);
		team2Panel.add(awaySelector);
		
		settingsPanel = new JPanel();
		settingsPanel.setLayout(null);
		settingsPanel.setBackground(Enums.transSgigray);
		this.add(settingsPanel);
		
		settingsLabel = new JLabel("Match Settings");
		settingsLabel.setForeground(Enums.blue);
		settingsLabel.setBackground(null);
		settingsLabel.setFont(FileUtils.getFont("Zekton.ttf", 24, Font.PLAIN));
		settingsPanel.add(settingsLabel);
		
		matchLengthText = new JLabel("Match Length (minutes)");
		matchLengthText.setForeground(Enums.white);
		matchLengthText.setBackground(null);
		matchLengthText.setFont(FileUtils.getFont("Zekton.ttf", 18, Font.PLAIN));
		settingsPanel.add(matchLengthText);
		
		commentaryText = new JLabel("Commentary");
		commentaryText.setForeground(Enums.white);
		commentaryText.setBackground(null);
		commentaryText.setFont(FileUtils.getFont("Zekton.ttf", 18, Font.PLAIN));
		settingsPanel.add(commentaryText);
		
		playerNumberText = new JLabel("Number of Players (per team)");
		playerNumberText.setForeground(Enums.white);
		playerNumberText.setBackground(null);
		playerNumberText.setFont(FileUtils.getFont("Zekton.ttf", 18, Font.PLAIN));
		settingsPanel.add(playerNumberText);
		
		// TODO : Temp lengths
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("1");
		temp.add("2");
		temp.add("3");
		matchLength = new Selector(temp);
		settingsPanel.add(matchLength);
		
		// TODO : Temp lengths
		ArrayList<String> temp3 = new ArrayList<String>();
		temp3.add("ON");
		temp3.add("OFF");
		commentary = new Selector(temp3);
		settingsPanel.add(commentary);
		
		// TODO : Temp lengths
		ArrayList<String> temp4 = new ArrayList<String>();
		temp4.add("3");
		temp4.add("5");
		temp4.add("7");
		playerNumber = new Selector(temp4);
		settingsPanel.add(playerNumber);
		
		backButton = new DefaultButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("Menu");
				
			}
		});
		this.add(backButton);
		
		startMatchButton = new DefaultButton("Start Match");
		startMatchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("Match");
			}
		});
		this.add(startMatchButton);
		
		arrangeLayout();
	}

	public KickOffPanel getInstance() {
		return this;
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
		matchLength.setBounds(widthCut, heightCut*6, settingsPanelWidth - 2*widthCut, heightCut*2);
		commentaryText.setBounds((int) ((settingsPanelWidth - commentaryText.getPreferredSize().getWidth())/2), heightCut*8, settingsPanelWidth, buttonHeight);
		commentary.setBounds(widthCut, heightCut*11, settingsPanelWidth - 2*widthCut, heightCut*2);
		playerNumberText.setBounds((int) ((settingsPanelWidth - playerNumberText.getPreferredSize().getWidth())/2), heightCut*13, settingsPanelWidth, buttonHeight);
		playerNumber.setBounds(widthCut, heightCut*16, settingsPanelWidth - 2*widthCut, heightCut*2);
		homeLabel.setBounds((int) ((teamPanelWidth - homeLabel.getPreferredSize().getWidth())/2), heightCut/3, teamPanelWidth, buttonHeight);
		awayLabel.setBounds((int) ((teamPanelWidth - awayLabel.getPreferredSize().getWidth())/2), heightCut/3, teamPanelWidth, buttonHeight);
		homeSelector.setBounds(widthCut, heightCut*4, teamPanelWidth - 2*widthCut, heightCut*3);
		awaySelector.setBounds(widthCut, heightCut*4, teamPanelWidth - 2*widthCut, heightCut*3);
	}
	
	public void setBackground(Image img) {
		Dimension screenDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenDimension.getWidth();
		int height = (int) screenDimension.getHeight();
		backgroundOrg = img;
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
