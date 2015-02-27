package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import rare.Metadata;
import rare.SaveFile;
import utils.CBLogger;
import utils.Enums;
import utils.FileUtils;
import utils.Utils;

import common.CommonEnums;
import common.Player;

public class MatchStatsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private DefaultButton backButton;
	private JPanel team1Panel, team2Panel, statTypesPanel;
	private JLabel statsLabel, passText, shootText, trapText, hasBallText, distanceText, fatigueText;
	private JLabel pass1Text, shoot1Text, trap1Text, hasBall1Text, distance1Text, fatigue1Text;
	private JLabel pass2Text, shoot2Text, trap2Text, hasBall2Text, distance2Text, fatigue2Text;
	private DefaultComboBoxModel<String> box1Model, box2Model;
	private JComboBox<String> team1Box, team2Box;
	private Image backgroundImage, backgroundOrg;
	
	private SaveFile saveFile;
	private Metadata metadata;
	
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();

	public MatchStatsPanel() {
		setBackground(Enums.black);
		setLayout(null);
		
		double widthRatio = Utils.getScreenWidthRatio((int) Utils.getScreenDimension().getWidth());
		
		team1Panel = new JPanel();
		team1Panel.setLayout(null);
		team1Panel.setBackground(Enums.transSgigray);
		this.add(team1Panel);
		
		box1Model = new DefaultComboBoxModel<String>();
		team1Box = new JComboBox<String>(box1Model);
		team1Box.setBackground(null);
		team1Box.setFont(FileUtils.getFont("Zekton.ttf", (int) (18 * widthRatio), Font.PLAIN));
		((JLabel)team1Box.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		team1Box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( box1Model.getSize() > 0 ) {
					updateStats1();
					arrangeLayout();
					repaint();
				}
			}
		});
		team1Panel.add(team1Box);
		
		
		pass1Text = new JLabel("0 / 0");
		pass1Text.setForeground(Enums.white);
		pass1Text.setBackground(null);
		pass1Text.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		team1Panel.add(pass1Text);
		
		shoot1Text = new JLabel("0");
		shoot1Text.setForeground(Enums.white);
		shoot1Text.setBackground(null);
		shoot1Text.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		team1Panel.add(shoot1Text);
		
		trap1Text = new JLabel("0 / 0");
		trap1Text.setForeground(Enums.white);
		trap1Text.setBackground(null);
		trap1Text.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		team1Panel.add(trap1Text);
		
		hasBall1Text = new JLabel("0");
		hasBall1Text.setForeground(Enums.white);
		hasBall1Text.setBackground(null);
		hasBall1Text.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		team1Panel.add(hasBall1Text);
		
		distance1Text = new JLabel("0");
		distance1Text.setForeground(Enums.white);
		distance1Text.setBackground(null);
		distance1Text.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		team1Panel.add(distance1Text);
		
		fatigue1Text = new JLabel("%0");
		fatigue1Text.setForeground(Enums.white);
		fatigue1Text.setBackground(null);
		fatigue1Text.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		team1Panel.add(fatigue1Text);
		
		
		team2Panel = new JPanel();
		team2Panel.setLayout(null);
		team2Panel.setBackground(Enums.transSgigray);
		this.add(team2Panel);
		
		box2Model = new DefaultComboBoxModel<String>();
		team2Box = new JComboBox<String>(box2Model);
		team2Box.setBackground(null);
		team2Box.setFont(FileUtils.getFont("Zekton.ttf", (int) (18 * widthRatio), Font.PLAIN));
		((JLabel)team2Box.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		team2Box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( box2Model.getSize() > 0 ) {
					updateStats2();
					arrangeLayout();
					repaint();
				}
			}
		});
		team2Panel.add(team2Box);
		
		pass2Text = new JLabel("0 / 0");
		pass2Text.setForeground(Enums.white);
		pass2Text.setBackground(null);
		pass2Text.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		team2Panel.add(pass2Text);
		
		shoot2Text = new JLabel("0");
		shoot2Text.setForeground(Enums.white);
		shoot2Text.setBackground(null);
		shoot2Text.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		team2Panel.add(shoot2Text);
		
		trap2Text = new JLabel("0 / 0");
		trap2Text.setForeground(Enums.white);
		trap2Text.setBackground(null);
		trap2Text.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		team2Panel.add(trap2Text);
		
		hasBall2Text = new JLabel("0");
		hasBall2Text.setForeground(Enums.white);
		hasBall2Text.setBackground(null);
		hasBall2Text.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		team2Panel.add(hasBall2Text);
		
		distance2Text = new JLabel("0");
		distance2Text.setForeground(Enums.white);
		distance2Text.setBackground(null);
		distance2Text.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		team2Panel.add(distance2Text);
		
		fatigue2Text = new JLabel("%0");
		fatigue2Text.setForeground(Enums.white);
		fatigue2Text.setBackground(null);
		fatigue2Text.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		team2Panel.add(fatigue2Text);
		
		statTypesPanel = new JPanel();
		statTypesPanel.setLayout(null);
		statTypesPanel.setBackground(Enums.transSgigray);
		this.add(statTypesPanel);
		
		statsLabel = new JLabel("Match Statistics");
		statsLabel.setForeground(Enums.white);
		statsLabel.setBackground(null);
		statsLabel.setFont(FileUtils.getFont("Zekton.ttf", (int) (18 * widthRatio), Font.PLAIN));
		statTypesPanel.add(statsLabel);
		
		passText = new JLabel("Pass");
		passText.setForeground(Enums.white);
		passText.setBackground(null);
		passText.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		statTypesPanel.add(passText);
		
		shootText = new JLabel("Shoot");
		shootText.setForeground(Enums.white);
		shootText.setBackground(null);
		shootText.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		statTypesPanel.add(shootText);
		
		trapText = new JLabel("Trap");
		trapText.setForeground(Enums.white);
		trapText.setBackground(null);
		trapText.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		statTypesPanel.add(trapText);
		
		hasBallText = new JLabel("Ball Control");
		hasBallText.setForeground(Enums.white);
		hasBallText.setBackground(null);
		hasBallText.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		statTypesPanel.add(hasBallText);
		
		distanceText = new JLabel("Distance Travelled (m)");
		distanceText.setForeground(Enums.white);
		distanceText.setBackground(null);
		distanceText.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		statTypesPanel.add(distanceText);
		
		fatigueText = new JLabel("Fatigue");
		fatigueText.setForeground(Enums.white);
		fatigueText.setBackground(null);
		fatigueText.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		statTypesPanel.add(fatigueText);
		
		backButton = new DefaultButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("Menu");
				LOGGER.log(Level.INFO, "Returned back to Main Menu");
			}
		});
		this.add(backButton);
		
		
		refresh();
	}
	
	public MatchStatsPanel getInstance() {
		return this;
	}

	public void refresh() {
		if ( box1Model.getSize() > 0 ) {
			team1Box.setSelectedIndex(0);
	    	updateStats1();
		}
		if ( box2Model.getSize() > 0 ) {
			team2Box.setSelectedIndex(0);
			updateStats2();
		}
		arrangeLayout();
		repaint();
		LOGGER.log(Level.INFO, "Refreshed screen");
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
		statTypesPanel.setBounds(teamPanelWidth + 2*widthCut, heightCut, settingsPanelWidth, panelHeight);
		team2Panel.setBounds(teamPanelWidth + 3*widthCut + settingsPanelWidth, heightCut, teamPanelWidth, panelHeight);
		
		backButton.setBounds(widthCut, panelHeight + (2*heightCut), buttonWidth, buttonHeight);
		
		team1Box.setBounds(widthCut, heightCut/3, teamPanelWidth - 2*widthCut, buttonHeight);
		team2Box.setBounds(widthCut, heightCut/3, teamPanelWidth - 2*widthCut, buttonHeight);
		statsLabel.setBounds((int) ((settingsPanelWidth - statsLabel.getPreferredSize().getWidth())/2), heightCut/3, settingsPanelWidth, buttonHeight);
		passText.setBounds((int) ((settingsPanelWidth - passText.getPreferredSize().getWidth())/2), heightCut*2 + buttonHeight, settingsPanelWidth, buttonHeight);
		shootText.setBounds((int) ((settingsPanelWidth - shootText.getPreferredSize().getWidth())/2), heightCut*3 + buttonHeight*2, settingsPanelWidth, buttonHeight);
		trapText.setBounds((int) ((settingsPanelWidth - trapText.getPreferredSize().getWidth())/2), heightCut*4 + buttonHeight*3, settingsPanelWidth, buttonHeight);
		hasBallText.setBounds((int) ((settingsPanelWidth - hasBallText.getPreferredSize().getWidth())/2), heightCut*5 + buttonHeight*4, settingsPanelWidth, buttonHeight);
		distanceText.setBounds((int) ((settingsPanelWidth - distanceText.getPreferredSize().getWidth())/2), heightCut*6 + buttonHeight*5, settingsPanelWidth, buttonHeight);
		fatigueText.setBounds((int) ((settingsPanelWidth - fatigueText.getPreferredSize().getWidth())/2), heightCut*7 + buttonHeight*6, settingsPanelWidth, buttonHeight);
	
		pass1Text.setBounds((int) ((teamPanelWidth - pass1Text.getPreferredSize().getWidth())/2), heightCut*2 + buttonHeight, teamPanelWidth, buttonHeight);
		shoot1Text.setBounds((int) ((teamPanelWidth - shoot1Text.getPreferredSize().getWidth())/2), heightCut*3 + buttonHeight*2, teamPanelWidth, buttonHeight);
		trap1Text.setBounds((int) ((teamPanelWidth - trap1Text.getPreferredSize().getWidth())/2), heightCut*4 + buttonHeight*3, teamPanelWidth, buttonHeight);
		hasBall1Text.setBounds((int) ((teamPanelWidth - hasBall1Text.getPreferredSize().getWidth())/2), heightCut*5 + buttonHeight*4, teamPanelWidth, buttonHeight);
		distance1Text.setBounds((int) ((teamPanelWidth - distance1Text.getPreferredSize().getWidth())/2), heightCut*6 + buttonHeight*5, teamPanelWidth, buttonHeight);
		fatigue1Text.setBounds((int) ((teamPanelWidth - fatigue1Text.getPreferredSize().getWidth())/2), heightCut*7 + buttonHeight*6, teamPanelWidth, buttonHeight);
	
		pass2Text.setBounds((int) ((teamPanelWidth - pass2Text.getPreferredSize().getWidth())/2), heightCut*2 + buttonHeight, teamPanelWidth, buttonHeight);
		shoot2Text.setBounds((int) ((teamPanelWidth - shoot2Text.getPreferredSize().getWidth())/2), heightCut*3 + buttonHeight*2, teamPanelWidth, buttonHeight);
		trap2Text.setBounds((int) ((teamPanelWidth - trap2Text.getPreferredSize().getWidth())/2), heightCut*4 + buttonHeight*3, teamPanelWidth, buttonHeight);
		hasBall2Text.setBounds((int) ((teamPanelWidth - hasBall2Text.getPreferredSize().getWidth())/2), heightCut*5 + buttonHeight*4, teamPanelWidth, buttonHeight);
		distance2Text.setBounds((int) ((teamPanelWidth - distance2Text.getPreferredSize().getWidth())/2), heightCut*6 + buttonHeight*5, teamPanelWidth, buttonHeight);
		fatigue2Text.setBounds((int) ((teamPanelWidth - fatigue2Text.getPreferredSize().getWidth())/2), heightCut*7 + buttonHeight*6, teamPanelWidth, buttonHeight);
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
	
	public void setSaveFilePath(String saveFilePath) {
		this.saveFile = SaveFile.getFromCompressed(saveFilePath);
    	this.metadata = saveFile.getMetadata();
    	
    	if ( box1Model.getSize() != 0 ) {
    		box1Model.removeAllElements();
    	}
    	if ( box2Model.getSize() != 0 ) {
    		box2Model.removeAllElements();
    	}
    	
    	box1Model.addElement(metadata.getTeam1().getName() + " - " + metadata.getTeam1().getId());
    	box2Model.addElement(metadata.getTeam1().getName() + " - " + metadata.getTeam1().getId());
    	box1Model.addElement(metadata.getTeam2().getName() + " - " + metadata.getTeam2().getId());
    	box2Model.addElement(metadata.getTeam2().getName() + " - " + metadata.getTeam2().getId());
    	
    	for(int i = 0, size = saveFile.getPerceptList().get(0).getPlayers().length; i < size; i++) {
    		box1Model.addElement(saveFile.getPerceptList().get(0).getPlayers()[i].getName() + " - " + saveFile.getPerceptList().get(0).getPlayers()[i].getId());
        	box2Model.addElement(saveFile.getPerceptList().get(0).getPlayers()[i].getName() + " - " + saveFile.getPerceptList().get(0).getPlayers()[i].getId());
    	}
    	
	}
	
	private void updateStats1() {
		Player[] playerList = saveFile.getPerceptList().get(saveFile.getPerceptList().size()-1).getPlayers();
		String selectedItem = box1Model.getSelectedItem().toString();
		String selectedName = selectedItem.substring(0, selectedItem.lastIndexOf("-")).trim();
		int selectedId = Integer.parseInt( selectedItem.substring(selectedItem.lastIndexOf("-")+1).trim() );
		
		if ( selectedName.equals(metadata.getTeam1().getName()) && selectedId == metadata.getTeam1().getId() ) {
			int successPass = 0, tryPass = 0, successTrap = 0, tryTrap = 0, shoot = 0, ballControl = 0;
			
			for(int i = 0; i < playerList.length; i++) {
				if ( playerList[i].getTeamID() == metadata.getTeam1().getId() ) {
					successPass += saveFile.getStats().getPassSuccess(playerList[i].getId());
					tryPass += saveFile.getStats().getPassTry(playerList[i].getId());
					successTrap += saveFile.getStats().getTrapSuccess(playerList[i].getId());
					tryTrap += saveFile.getStats().getTrapTry(playerList[i].getId());
					shoot += saveFile.getStats().getShootTry(playerList[i].getId());
					ballControl += saveFile.getStats().getHasBall(playerList[i].getId());
				}
			}
			
			pass1Text.setText(successPass + " / " + tryPass);
			shoot1Text.setText( Integer.toString(shoot) );
			trap1Text.setText(successTrap + " / " + tryTrap);
			hasBall1Text.setText( Integer.toString(ballControl) );
			distance1Text.setText( "-" );
			fatigue1Text.setText( "-");
			
		}
		else if ( selectedName.equals(metadata.getTeam2().getName()) && selectedId == metadata.getTeam2().getId() ) {
			int successPass = 0, tryPass = 0, successTrap = 0, tryTrap = 0, shoot = 0, ballControl = 0;
			
			for(int i = 0; i < playerList.length; i++) {
				if ( playerList[i].getTeamID() == metadata.getTeam2().getId() ) {
					successPass += saveFile.getStats().getPassSuccess(playerList[i].getId());
					tryPass += saveFile.getStats().getPassTry(playerList[i].getId());
					successTrap += saveFile.getStats().getTrapSuccess(playerList[i].getId());
					tryTrap += saveFile.getStats().getTrapTry(playerList[i].getId());
					shoot += saveFile.getStats().getShootTry(playerList[i].getId());
					ballControl += saveFile.getStats().getHasBall(playerList[i].getId());
				}
			}
			
			pass1Text.setText(successPass + " / " + tryPass);
			shoot1Text.setText( Integer.toString(shoot) );
			trap1Text.setText(successTrap + " / " + tryTrap);
			hasBall1Text.setText( Integer.toString(ballControl) );
			distance1Text.setText( "-" );
			fatigue1Text.setText( "-");
		}
		else {
			for(int i = 0; i < playerList.length; i++) {
				if ( playerList[i].getName().equals(selectedName) && playerList[i].getId() == selectedId ) {
					pass1Text.setText(saveFile.getStats().getPassSuccess(playerList[i].getId()) + " / " + saveFile.getStats().getPassTry(playerList[i].getId()));
					shoot1Text.setText( Integer.toString(saveFile.getStats().getShootTry(playerList[i].getId())) );
					trap1Text.setText(saveFile.getStats().getTrapSuccess(playerList[i].getId()) + " / " + saveFile.getStats().getTrapTry(playerList[i].getId()));
					hasBall1Text.setText( Integer.toString(saveFile.getStats().getHasBall(playerList[i].getId())) );
					distance1Text.setText( Double.toString( Utils.round(saveFile.getStats().getTotalDistance(playerList[i].getId()), 2) ) );
					fatigue1Text.setText( "% " + Utils.round(100 * (playerList[i].getFatigueAbility() / CommonEnums.MAXIMUM_NUMBER_FATIGUE) , 2) );
					break;
				}
			}
		}
	}
	
	private void updateStats2() {
		Player[] playerList = saveFile.getPerceptList().get(saveFile.getPerceptList().size()-1).getPlayers();
		String selectedItem = box2Model.getSelectedItem().toString();
		String selectedName = selectedItem.substring(0, selectedItem.lastIndexOf("-")).trim();
		int selectedId = Integer.parseInt( selectedItem.substring(selectedItem.lastIndexOf("-")+1).trim() );
		
		if ( selectedName.equals(metadata.getTeam1().getName()) && selectedId == metadata.getTeam1().getId() ) {
			int successPass = 0, tryPass = 0, successTrap = 0, tryTrap = 0, shoot = 0, ballControl = 0;
			
			for(int i = 0; i < playerList.length; i++) {
				if ( playerList[i].getTeamID() == metadata.getTeam1().getId() ) {
					successPass += saveFile.getStats().getPassSuccess(playerList[i].getId());
					tryPass += saveFile.getStats().getPassTry(playerList[i].getId());
					successTrap += saveFile.getStats().getTrapSuccess(playerList[i].getId());
					tryTrap += saveFile.getStats().getTrapTry(playerList[i].getId());
					shoot += saveFile.getStats().getShootTry(playerList[i].getId());
					ballControl += saveFile.getStats().getHasBall(playerList[i].getId());
				}
			}
			
			pass2Text.setText(successPass + " / " + tryPass);
			shoot2Text.setText( Integer.toString(shoot) );
			trap2Text.setText(successTrap + " / " + tryTrap);
			hasBall2Text.setText( Integer.toString(ballControl) );
			distance2Text.setText( "-" );
			fatigue2Text.setText( "-");
			
		}
		else if ( selectedName.equals(metadata.getTeam2().getName()) && selectedId == metadata.getTeam2().getId() ) {
			int successPass = 0, tryPass = 0, successTrap = 0, tryTrap = 0, shoot = 0, ballControl = 0;
			
			for(int i = 0; i < playerList.length; i++) {
				if ( playerList[i].getTeamID() == metadata.getTeam2().getId() ) {
					successPass += saveFile.getStats().getPassSuccess(playerList[i].getId());
					tryPass += saveFile.getStats().getPassTry(playerList[i].getId());
					successTrap += saveFile.getStats().getTrapSuccess(playerList[i].getId());
					tryTrap += saveFile.getStats().getTrapTry(playerList[i].getId());
					shoot += saveFile.getStats().getShootTry(playerList[i].getId());
					ballControl += saveFile.getStats().getHasBall(playerList[i].getId());
				}
			}
			
			pass2Text.setText(successPass + " / " + tryPass);
			shoot2Text.setText( Integer.toString(shoot) );
			trap2Text.setText(successTrap + " / " + tryTrap);
			hasBall2Text.setText( Integer.toString(ballControl) );
			distance2Text.setText( "-" );
			fatigue2Text.setText( "-");
		}
		else {
			for(int i = 0; i < playerList.length; i++) {
				if ( playerList[i].getName().equals(selectedName) && playerList[i].getId() == selectedId ) {
					pass2Text.setText(saveFile.getStats().getPassSuccess(playerList[i].getId()) + " / " + saveFile.getStats().getPassTry(playerList[i].getId()));
					shoot2Text.setText( Integer.toString(saveFile.getStats().getShootTry(playerList[i].getId())) );
					trap2Text.setText(saveFile.getStats().getTrapSuccess(playerList[i].getId()) + " / " + saveFile.getStats().getTrapTry(playerList[i].getId()));
					hasBall2Text.setText( Integer.toString(saveFile.getStats().getHasBall(playerList[i].getId())) );
					distance2Text.setText( Double.toString( Utils.round(saveFile.getStats().getTotalDistance(playerList[i].getId()), 2) ) );
					fatigue2Text.setText( "% " + Utils.round(100 * (playerList[i].getFatigueAbility() / CommonEnums.MAXIMUM_NUMBER_FATIGUE) , 2) );
					break;
				}
			}
		}
	}
}
