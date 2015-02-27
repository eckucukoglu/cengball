package gui;

import imports.DynamicCompiler;

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
import javax.swing.JProgressBar;

import rare.Pitch;
import simulator.Simulator;
import utils.CBLogger;
import utils.CengBallException;
import utils.Enums;
import utils.FileUtils;
import utils.Utils;

import common.Team;

public class SimulationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private DefaultButton backButton, ssButton, watchButton, showStatsButton;
	private JPanel logsPanel;
	private JLabel logsLabel;
	private JProgressBar progressBar;
	private Simulator simulator;
	private String saveFilePath;
	private MatchState state;
	private int perceptLimit, numberOfPlayers;
	private String teamName1, teamName2;
	private boolean isFatigueOn;
	private Image backgroundImage, backgroundOrg;
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();

	private enum MatchState {
		READY, PLAYING, STOPPED, FINISHED
	}

	public SimulationPanel() {
		setBackground(Enums.black);
		setLayout(null);
		
		double widthRatio = Utils.getScreenWidthRatio((int) Utils.getScreenDimension().getWidth());
		
		logsPanel = new JPanel();
		logsPanel.setLayout(null);
		logsPanel.setBackground(Enums.transSgigray);
		this.add(logsPanel);
		
		logsLabel = new JLabel("Simulating...");
		logsLabel.setForeground(Enums.white);
		logsLabel.setBackground(null);
		logsLabel.setFont(FileUtils.getFont("Zekton.ttf", (int) (18 * widthRatio), Font.PLAIN));
		logsPanel.add(logsLabel);
		
		progressBar = new JProgressBar(0, 100);
	    progressBar.setValue(0);
	    progressBar.setStringPainted(true);
	    progressBar.setFont(FileUtils.getFont("Zekton.ttf", (int) (18 * widthRatio), Font.PLAIN));
	    logsPanel.add(progressBar);
		
		backButton = new DefaultButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("Menu");
				refresh();
			}
		});
		this.add(backButton);
		
		watchButton = new DefaultButton("Watch");
		watchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).sendSaveFilePathToMatch(saveFilePath);
				( (MainPanel) getInstance().getParent() ).show("Match");
			}
		});
		this.add(watchButton);
		
		showStatsButton = new DefaultButton("Match Stats");
		showStatsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).sendSaveFilePathToStats(saveFilePath);
				( (MainPanel) getInstance().getParent() ).show("MatchStats");
			}
		});
		this.add(showStatsButton);
		
		ssButton = new DefaultButton("Start");
		ssButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( state == MatchState.READY ) {
					if ( simulator == null ) {
						ssButton.setText("Terminate");
						ssButton.setVisible(true);
						state = MatchState.PLAYING;
						
						Team team1 = (Team) DynamicCompiler.loadObject(teamName1);
						Team team2 = (Team) DynamicCompiler.loadObject(teamName2);
						
						saveFilePath = FileUtils.getSaveFilePath();
						try {
							simulator = new Simulator(getInstance(), team1, team2, new Pitch(), saveFilePath , perceptLimit, numberOfPlayers, isFatigueOn);
						} catch (CengBallException e1) {
							LOGGER.log(Level.WARNING, "Cannot create simulator");
						}
						simulator.start();
						setTurn(0, perceptLimit/Enums.DEFAULT_TURN_RATIO);
					}
				}
				else if ( state == MatchState.FINISHED ) {
					refresh();
				}
				else if ( state == MatchState.PLAYING ) {
					refresh();
				}
			}
		});
		this.add(ssButton);
		
		refresh();
		arrangeLayout();
	}
	
	public SimulationPanel getInstance() {
		return this;
	}
	
	private void arrangeLayout() { 
		Dimension screenDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenDimension.getWidth();
		int height = (int) screenDimension.getHeight();
		double sWRatio = Utils.getScreenWidthRatio(width);
		double sHRatio = Utils.getScreenHeightRatio(height);
		int widthCut = (int) (30 * sWRatio), heightCut = (int) (20 * sHRatio);
		int panelWidth = (width - (2*widthCut)), panelHeight = (int) ((height - 3*heightCut)*0.9);
		int buttonHeight = (int) ((height - 3*heightCut)*0.1);
		int buttonWidth = (int) (Enums.DEFAULT_BUTTON_WIDTH * sWRatio);
		int barWidth = width/2;

		logsPanel.setBounds(widthCut, heightCut, panelWidth, panelHeight);
		backButton.setBounds(widthCut, panelHeight + (2*heightCut), buttonWidth, buttonHeight);
		ssButton.setBounds((int)(width/2 - 80*sWRatio), panelHeight + (2*heightCut), (int)(160*sWRatio), buttonHeight);
		watchButton.setBounds(width-(widthCut+(int)(200*sWRatio)), panelHeight + (2*heightCut), (int)(200*sWRatio), buttonHeight);
		showStatsButton.setBounds(width-(2*widthCut+(int)(400*sWRatio)), panelHeight + (2*heightCut), (int)(200*sWRatio), buttonHeight);
		progressBar.setBounds((logsPanel.getWidth()-barWidth)/2, (logsPanel.getHeight()-buttonHeight)/2, barWidth, buttonHeight);
		logsLabel.setBounds((int) ((panelWidth - logsLabel.getPreferredSize().getWidth())/2), progressBar.getY() - (heightCut/3+buttonHeight), panelWidth, buttonHeight);
	}
	
	public void onSimulationFinished() {
		threadJoin(simulator, 100);
		simulator = null;
		watchButton.setVisible(true);
		showStatsButton.setVisible(true);
		state = MatchState.FINISHED;
		ssButton.setText("Reset");
		ssButton.setVisible(true);
	}

	public void refresh() {
		progressBar.setValue(0);
		state = MatchState.READY;
		ssButton.setText("Play");
		ssButton.setVisible(true);
		backButton.setVisible(true);
		watchButton.setVisible(false);
		showStatsButton.setVisible(false);
		if ( simulator != null ) {
			simulator.setmRun(false);
			threadJoin(simulator, 100);
			simulator = null;
		}
		repaint();
	}
	
	private void threadJoin(Thread thread, int miliseconds) {
		try {
			thread.join(miliseconds);
		} catch (InterruptedException e) {
			LOGGER.log(Level.WARNING, "Cannot join thread : " + thread.getName());
		}
	}
	
	public void setTurn(int currentTurn, int turnLimit) {
		int progress = (int) (( (double)currentTurn ) / ( (double)turnLimit ) * progressBar.getMaximum()) ;
		progressBar.setValue(progress);
	}
	
	public void setPerceptLimit(int perceptLimit) {
		this.perceptLimit = perceptLimit;
	}
	
	public void setTeamNames(String name1, String name2) {
		this.teamName1 = name1;
		this.teamName2 = name2;
	}
	
	public void setFatigueOption(boolean isFatigueOn) {
		this.isFatigueOn = isFatigueOn;
	}
	
	public void setNumberOfPlayers(int numOfPlayers) {
		this.numberOfPlayers = numOfPlayers;
	}
	
	@Override
	public void paintComponent(final Graphics g) {
	    super.paintComponent(g);
	    if (backgroundImage != null) {
	        g.drawImage(backgroundImage, 0, 0, null);
	    }
	}
	
	public void setBackground(Image img) {
		backgroundOrg = img;
		Dimension screenDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenDimension.getWidth();
		int height = (int) screenDimension.getHeight();
		if ( backgroundOrg != null ) {
			backgroundImage = width > 0 && height > 0 ? backgroundOrg.getScaledInstance(width, height, Image.SCALE_SMOOTH) : backgroundOrg;
		}
		repaint();
	}
}
