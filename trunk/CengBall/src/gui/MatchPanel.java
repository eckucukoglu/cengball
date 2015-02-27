package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import rare.Highlight;
import rare.Metadata;
import rare.SaveFile;
import rare.TeamInfo;
import utils.CBLogger;
import utils.Enums;
import utils.Enums.HighlightType;
import utils.FileUtils;
import utils.Utils;
import visualizer.Visualizer;

import common.Player;

public class MatchPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private DefaultButton ssButton, backButton, skipButton, nextButton, prevButton;
	private JLabel team1Name, team2Name, team1Score, team2Score, time, commentary;
	private JPanel buttonPanel, scorePanel, commentaryPanel;
	private Visualizer visualizer;
	private Thread visualizerThread;
	private MatchState state;
	private int fps, matchLength;
	private SaveFile saveFile;
	private Metadata metadata;
	private Highlight prevHighlight;
	private int goalCount;
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();
	
	private enum MatchState {
		READY, PLAYING, STOPPED, FINISHED
	}

	/**
	 * Create the panel.
	 */
	public MatchPanel() {
		setBackground(Enums.black);
		setLayout(null);
		
		double widthRatio = Utils.getScreenWidthRatio((int) Utils.getScreenDimension().getWidth());
		
		this.fps = Enums.DEFAULT_FPS;
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.setBackground(Enums.darkslategray);
		ssButton = new DefaultButton("Play");
		ssButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( state == MatchState.PLAYING ) {
					ssButton.setText("Resume");
					state = MatchState.STOPPED;
					nextButton.setVisible(true);
					prevButton.setVisible(true);
					visualizer.setRunning(false);
					if ( visualizerThread != null ) {
						try {
							visualizerThread.join(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					LOGGER.log(Level.INFO, "Stopped match");
				}
				else if ( state == MatchState.STOPPED  ) {
					ssButton.setText("Stop");
					state = MatchState.PLAYING;
					nextButton.setVisible(false);
					prevButton.setVisible(false);
					visualizerThread = new Thread(visualizer);
					visualizerThread.start();
					LOGGER.log(Level.INFO, "Resumed match");
				}
				else if ( state == MatchState.READY ) {
					ssButton.setText("Stop");
					state = MatchState.PLAYING;
					nextButton.setVisible(false);
					prevButton.setVisible(false);
					visualizerThread = new Thread(visualizer);
					visualizerThread.start();
					LOGGER.log(Level.INFO, "Played match");
				}
			}
		});
		buttonPanel.add(ssButton);
		backButton = new DefaultButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("Menu");
				refresh();
				LOGGER.log(Level.INFO, "Returned back to Main Menu");
			}
		});
		buttonPanel.add(backButton);
		skipButton = new DefaultButton("Skip");
		skipButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( state != MatchState.FINISHED ) {
					finishMatch();
					LOGGER.log(Level.INFO, "Skipped match");
				}
				else {
					( (MainPanel) getInstance().getParent() ).show("Menu");
					refresh();
					LOGGER.log(Level.INFO, "Returned back to Main Menu");
				}
			}
		});
		buttonPanel.add(skipButton);
		nextButton = new DefaultButton("Next");
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visualizer.forward();		
				LOGGER.log(Level.INFO, "Pressed Next");
			}
		});
		buttonPanel.add(nextButton);
		prevButton = new DefaultButton("Prev");
		prevButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visualizer.backward();	
				LOGGER.log(Level.INFO, "Pressed Prev");
			}
		});
		buttonPanel.add(prevButton);
		this.add(buttonPanel);
		
		scorePanel = new JPanel();
		scorePanel.setLayout(null);
		scorePanel.setBackground(Enums.darkslategray);
		
		commentaryPanel = new JPanel();
		commentaryPanel.setLayout(null);
		commentary = new JLabel("");
		commentary.setOpaque(true);
		commentary.setBackground(Enums.black);
		commentary.setForeground(Enums.white);
		commentary.setHorizontalAlignment(SwingConstants.CENTER);
		commentary.setFont(FileUtils.getFont("Zekton.ttf", (int) (18 * widthRatio), Font.PLAIN));
		commentaryPanel.add(commentary);
		this.add(commentaryPanel);
		
		team1Name = new JLabel("");
		team1Name.setOpaque(true);
		team1Name.setForeground(Enums.white);
		team1Name.setHorizontalAlignment(SwingConstants.CENTER);
		team1Name.setFont(FileUtils.getFont("Zekton.ttf", (int) (18 * widthRatio), Font.BOLD));
		scorePanel.add(team1Name);

		team2Name = new JLabel("");
		team2Name.setOpaque(true);
		team2Name.setForeground(Enums.white);
		team2Name.setHorizontalAlignment(SwingConstants.CENTER);
		team2Name.setFont(FileUtils.getFont("Zekton.ttf", (int) (18 * widthRatio), Font.BOLD));
		scorePanel.add(team2Name);
		
		team1Score = new JLabel("0");
		team1Score.setForeground(Enums.white);
		team1Score.setHorizontalAlignment(SwingConstants.CENTER);
		team1Score.setFont(FileUtils.getFont("Zekton.ttf", (int) (36 * widthRatio), Font.BOLD));
		scorePanel.add(team1Score);
		
		team2Score = new JLabel("0");
		team2Score.setForeground(Enums.white);
		team2Score.setHorizontalAlignment(SwingConstants.CENTER);
		team2Score.setFont(FileUtils.getFont("Zekton.ttf", (int) (36 * widthRatio), Font.BOLD));
		scorePanel.add(team2Score);
		
		time = new JLabel("00:00");
		time.setForeground(Enums.white);
		time.setHorizontalAlignment(SwingConstants.CENTER);
		time.setFont(FileUtils.getFont("Zekton.ttf", (int) (36 * widthRatio), Font.PLAIN));
		scorePanel.add(time);
		this.add(scorePanel);
		
		visualizer = new Visualizer(matchLength, fps);
		this.add(visualizer);

		refresh();
		arrangeLayout();
	}
	
	public MatchPanel getInstance() {
		return this;
	}
	
	public void setSaveFilePath(String saveFilePath) {
		loadSequenceFromFile(saveFilePath);
		TeamInfo team1 = metadata.getTeam1();
		team1Name.setText(team1.getName());
		team1Name.setBackground(team1.getColor1());
		team1Name.setForeground(team1.getColor2());

		TeamInfo team2 = metadata.getTeam2();
		team2Name.setText(team2.getName());
		team2Name.setBackground(team2.getColor1());
		team2Name.setForeground(team2.getColor2());
		visualizer.initializeVisualizer(saveFile, metadata);
	}
	
	public void updateTime(long miliseconds) {
		int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(miliseconds);
		int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(miliseconds) - TimeUnit.MINUTES.toSeconds(minutes));
		String sec = Integer.toString(seconds);
    	if ( sec.length() == 1 ) {
    		sec = "0" + sec;
    	}
    	String min = Integer.toString(minutes);
    	if ( min.length() == 1 ) {
    		min = "0" + min;
    	}
    	time.setText(min + ":" + sec);
	}
	
	public void refresh() {
		visualizer.setRunning(false);
		if ( visualizerThread != null ) {
			try {
				visualizerThread.join(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		visualizer.reset();
		state = MatchState.READY;
		ssButton.setText("Play");
		ssButton.setVisible(true);
		skipButton.setText("Skip");
		team1Score.setText("0");
		team2Score.setText("0");
		nextButton.setVisible(true);
		prevButton.setVisible(true);
		commentary.setBackground(Enums.black);
		LOGGER.log(Level.INFO, "Refreshed screen");
		repaint();
	}
	
	public void finishMatch() {
		visualizer.endMatch();
		state = MatchState.FINISHED;
		ssButton.setVisible(false);
		nextButton.setVisible(false);
		prevButton.setVisible(false);
		skipButton.setText("End Match");
		updateTime(matchLength * 60 * 1000);
		if ( visualizerThread != null ) {
			try {
				visualizerThread.join(100);
			} catch (InterruptedException e1) {
				LOGGER.log(Level.WARNING, "Cannot join thread : VisualizerThread");
			}
		}
		LOGGER.log(Level.INFO, "Finished the match");
	}
	
	private void arrangeLayout() { 
		Dimension screenDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenDimension.getWidth();
		int height = (int) screenDimension.getHeight();
		double sWRatio = Utils.getScreenWidthRatio(width);
		double sHRatio = Utils.getScreenHeightRatio(height);
		int scorePanelH = (int) (80 * sHRatio), buttonPanelH = (int) (60 * sHRatio), commentaryPanelH = (int) (60 * sHRatio);
		int delimeter = (int) (4 * sHRatio);
		int buttonH = (int) (50 * sHRatio), panelH = (int) (80 * sHRatio);
		int widthCut = (int) (10 * sWRatio);
		
		scorePanel.setBounds(0, 0, width, scorePanelH);
		buttonPanel.setBounds(0, height-buttonPanelH, width, buttonPanelH);
		commentaryPanel.setBounds(0, height-(buttonPanelH + commentaryPanelH + delimeter), width, commentaryPanelH);
		visualizer.setBounds(0, scorePanelH + delimeter, width, height - (scorePanelH + buttonPanelH + commentaryPanelH + 3*delimeter) );
		ssButton.setBounds((int)(width/2 - 80*sWRatio), (buttonPanelH - buttonH)/2, (int)(160*sWRatio), buttonH);
		backButton.setBounds(widthCut, (buttonPanelH - buttonH)/2, (int)(100*sWRatio), buttonH);
		skipButton.setBounds(width-(widthCut+(int)(200*sWRatio)), (buttonPanelH - buttonH)/2, (int)(200*sWRatio), buttonH);
		team1Name.setBounds(0, (scorePanelH - panelH)/2, (int)(240*sWRatio), panelH );
		team2Name.setBounds(width - (int)(240*sWRatio), (scorePanelH - panelH)/2, (int)(240*sWRatio), panelH );
		time.setBounds(width/2 - (int)(100*sWRatio), scorePanelH/2 - (int)(30*sHRatio), (int)(200*sWRatio), (int)(60*sHRatio) );
		team1Score.setBounds(team1Name.getWidth() + widthCut, (scorePanelH - panelH)/2, (int)(100*sWRatio), panelH );
		team2Score.setBounds(team2Name.getX() - (int)(110*sWRatio), (scorePanelH - panelH)/2, (int)(100*sWRatio), panelH );
		nextButton.setBounds(ssButton.getX() + ssButton.getWidth() + widthCut, (buttonPanelH-buttonH)/2, (int)(100*sWRatio), buttonH );
		prevButton.setBounds(ssButton.getX() - (int)(110*sWRatio), (buttonPanelH-buttonH)/2, (int)(100*sWRatio), buttonH );
		commentary.setBounds(0, 0, commentaryPanel.getWidth(), commentaryPanel.getHeight() );
	}
    
    public void loadSequenceFromFile(String filePath) {
        this.saveFile = SaveFile.getFromCompressed(filePath);
    	this.metadata = saveFile.getMetadata();
		this.matchLength = metadata.getMatchLength();
    }
    
    public void updateScore(int team1score, int team2score) {
    	team1Score.setText(Integer.toString(team1score));
    	team2Score.setText(Integer.toString(team2score));
    }
    
    public void updateCommentary(Highlight highlight) {
    	if ( highlight != null ) {
    		Player actor = highlight.getActor();
	    	if ( actor != null ) {
	    		Color color1 = metadata.getTeamInfo(actor.getTeamID()).getColor1();
	    		Color color2 = metadata.getTeamInfo(actor.getTeamID()).getColor2();
		    	String comment = "";
		    	if ( highlight.getActionType() == HighlightType.GOAL) {
		    		if ( prevHighlight.getActionType() == HighlightType.GOAL ) {
		    			goalCount++;
		    		}
		    		if ( goalCount % 18 == 0 ) {
		    			commentary.setBackground( color1 );
			    		commentary.setForeground( color2 );
		    		}
		    		else if ( goalCount % 18 == 9 ) {
		    			commentary.setBackground( color2 );
			    		commentary.setForeground( color1 );
		    		}
		    		commentary.setText("GOAL!");
		    	}
		    	else {
		    		goalCount = 0;
		    		commentary.setBackground( color1 );
		    		commentary.setForeground( color2 );
			    	if ( highlight.getActionType() == HighlightType.MOVEWITHBALL) {
			    		comment = actor.getName() + " moves with the ball";
			    		commentary.setText(comment);
			    	}
			    	else if ( highlight.getActionType() == HighlightType.PASS) {
			    		comment = actor.getName() + " passes...";
			    		commentary.setText(comment);
			    	}
			    	else if ( highlight.getActionType() == HighlightType.SHOOT) {
			    		comment = actor.getName() + " shoots...";
			    		commentary.setText(comment);
			    	}
			    	else if ( highlight.getActionType() == HighlightType.DRIBBLE) {
			    		comment = actor.getName() + " dribbles";
			    		commentary.setText(comment);
			    	}
			    	else if ( highlight.getActionType() == HighlightType.OWN) {
			    		comment = actor.getName() + " has the ball";
			    		commentary.setText(comment);
			    	}
			    	else if ( highlight.getActionType() == HighlightType.TACKLE) {
			    		comment = actor.getName() + " tackles";
			    		commentary.setText(comment);
			    	}
		    	}
	    	}
	    	else {
	    		commentary.setBackground(Enums.black);
	    		commentary.setForeground(Enums.white);
	    		if ( highlight.getActionType() == HighlightType.START) {
		    		commentary.setText("Match starts...");
		    	}
	    		else if ( highlight.getActionType() == HighlightType.END) {
	    			int score1 = Integer.parseInt(team1Score.getText());
	    			int score2 = Integer.parseInt(team2Score.getText());
	    			if ( score1 > score2 ) {
	    				commentary.setBackground(metadata.getTeam1().getColor1());
	    	    		commentary.setForeground(metadata.getTeam1().getColor2());
	    				commentary.setText(team1Name.getText() + " wins!");
	    			}
	    			else if ( score2 > score1 ) {
	    				commentary.setBackground(metadata.getTeam2().getColor1());
	    	    		commentary.setForeground(metadata.getTeam2().getColor2());
	    				commentary.setText(team2Name.getText() + " wins!");
	    			}
	    			else {
	    				commentary.setText("It is a draw");
	    			}
		    	}
	    	}
	    	prevHighlight = highlight;
    	}
    }
}
