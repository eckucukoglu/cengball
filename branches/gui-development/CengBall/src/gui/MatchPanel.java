package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import simulator.PlayerInfo;
import utils.Enums;
import utils.Enums.ActionType;
import utils.FileUtils;
import utils.Utils;
import visualizer.Visualizer;

import common.Ball;
import common.Highlight;
import common.Metadata;
import common.Percept;
import common.PlayerAction;
import common.PlayerInfo_;
import common.Position;
import common.SaveFile;
import common.TeamInfo;

public class MatchPanel extends JPanel {
	private DefaultButton ssButton, backButton, skipButton, nextButton, prevButton;
	private JLabel team1Name, team2Name, team1Score, team2Score, time, commentary;
	private JPanel buttonPanel, scorePanel, commentaryPanel;
	private Visualizer visualizer;
	private Thread visualizerThread;
	private MatchState state;
	private Timer timer;
	private long miliseconds;
	private int fps, matchLength;
	private static String demoFilePath;
	private SaveFile saveFile;
	private Metadata metadata;
	
	private enum MatchState {
		READY, PLAYING, STOPPED, FINISHED
	};

	/**
	 * Create the panel.
	 */
	public MatchPanel(final int matchLength, final int fps) {
		setBackground(Enums.black);
		setLayout(null);
		
		this.matchLength = matchLength;
		this.fps = fps;
		createRandomBallSequence();
		loadSequenceFromFile(demoFilePath);
		
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
					timer.stop();
					if ( visualizerThread != null ) {
						try {
							visualizerThread.join(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
				else if ( state == MatchState.STOPPED  ) {
					ssButton.setText("Stop");
					state = MatchState.PLAYING;
					nextButton.setVisible(false);
					prevButton.setVisible(false);
					visualizerThread = new Thread(visualizer);
					visualizerThread.start();
					timer.start();
				}
				else if ( state == MatchState.READY ) {
					ssButton.setText("Stop");
					state = MatchState.PLAYING;
					nextButton.setVisible(false);
					prevButton.setVisible(false);
					visualizerThread = new Thread(visualizer);
					visualizerThread.start();
					miliseconds = 0;
					timer.start();
				}
			}
		});
		buttonPanel.add(ssButton);
		backButton = new DefaultButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("KickOff");
				resetScreen();
				
			}
		});
		buttonPanel.add(backButton);
		skipButton = new DefaultButton("Skip");
		skipButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( state != MatchState.FINISHED ) {
					finishMatch();
				}
				else {
					( (MainPanel) getInstance().getParent() ).show("Menu");
					resetScreen();
				}
			}
		});
		buttonPanel.add(skipButton);
		nextButton = new DefaultButton("Next");
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visualizer.forward();		
				changeTime(miliseconds + 1000/fps);
			}
		});
		buttonPanel.add(nextButton);
		prevButton = new DefaultButton("Prev");
		prevButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visualizer.backward();	
				changeTime(miliseconds - 1000/fps);
			}
		});
		buttonPanel.add(prevButton);
		this.add(buttonPanel);
		
		scorePanel = new JPanel();
		scorePanel.setLayout(null);
		scorePanel.setBackground(Enums.darkslategray);
		
		commentaryPanel = new JPanel();
		commentaryPanel.setLayout(null);
		commentary = new JLabel("Temp");
		commentary.setOpaque(true);
		commentary.setBackground(Enums.black);
		commentary.setForeground(Enums.white);
		commentary.setHorizontalAlignment(SwingConstants.CENTER);
		commentary.setFont(FileUtils.getFont("Zekton.ttf", 24, Font.PLAIN));
		commentaryPanel.add(commentary);
		this.add(commentaryPanel);
		
		TeamInfo team1 = metadata.getTeam1();
		team1Name = new JLabel(team1.getName());
		team1Name.setOpaque(true);
		team1Name.setBackground(new Color(team1.getColorR(), team1.getColorG(), team1.getColorB()) );
		team1Name.setForeground(Enums.white);
		team1Name.setHorizontalAlignment(SwingConstants.CENTER);
		team1Name.setFont(FileUtils.getFont("Zekton.ttf", 24, Font.BOLD));
		scorePanel.add(team1Name);

		TeamInfo team2 = metadata.getTeam2();
		team2Name = new JLabel(team2.getName());
		team2Name.setOpaque(true);
		team2Name.setBackground(new Color(team2.getColorR(), team2.getColorG(), team2.getColorB()) );
		team2Name.setForeground(Enums.white);
		team2Name.setHorizontalAlignment(SwingConstants.CENTER);
		team2Name.setFont(FileUtils.getFont("Zekton.ttf", 24, Font.BOLD));
		scorePanel.add(team2Name);
		
		team1Score = new JLabel("0");
		team1Score.setForeground(Enums.white);
		team1Score.setHorizontalAlignment(SwingConstants.CENTER);
		team1Score.setFont(FileUtils.getFont("Zekton.ttf", 48, Font.BOLD));
		scorePanel.add(team1Score);
		
		team2Score = new JLabel("0");
		team2Score.setForeground(Enums.white);
		team2Score.setHorizontalAlignment(SwingConstants.CENTER);
		team2Score.setFont(FileUtils.getFont("Zekton.ttf", 48, Font.BOLD));
		scorePanel.add(team2Score);
		
		time = new JLabel("00:00");
		time.setForeground(Enums.white);
		time.setHorizontalAlignment(SwingConstants.CENTER);
		time.setFont(FileUtils.getFont("Zekton.ttf", 48, Font.PLAIN));
		scorePanel.add(time);
		this.add(scorePanel);
		
		visualizer = new Visualizer(saveFile, metadata, matchLength, fps);
		this.add(visualizer);
		
		timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	changeTime(miliseconds + 1000);
            }
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);

		resetScreen();
		arrangeLayout();
	}
	
	public MatchPanel getInstance() {
		return this;
	}
	
	private void changeTime(long newTime) {
		miliseconds = newTime;
		int seconds = (int) (miliseconds % 60000) / 1000;
		int minutes = (int) (miliseconds / 60000); 
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
	
	private void resetScreen() {
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
		time.setText("00:00");
		commentary.setText("Match starts...");
		commentary.setBackground(Enums.black);
		timer.stop();
	}
	
	public void finishMatch() {
		visualizer.endMatch();
		state = MatchState.FINISHED;
		ssButton.setVisible(false);
		nextButton.setVisible(false);
		prevButton.setVisible(false);
		skipButton.setText("End Match");
		commentary.setText("Match ends...");
		timer.stop();
		changeTime(matchLength * 1000);
		if ( visualizerThread != null ) {
			try {
				visualizerThread.join(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
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
	
	public static void createRandomBallSequence() {
    	ArrayList<Percept> perceptSequence = new ArrayList<Percept>();
    	int team1Score = 0, team2Score = 0;
    	for(int i = 0; i < 40; i++) {
    		int x = (int) (int)(Math.random() * ((108) + 1));
    		int y = (int) (int)(Math.random() * ((72) + 1));
    		Percept newPercept = new Percept( new Ball(new Position(x, y) ) );
    		ArrayList<PlayerInfo> playerInfos = new ArrayList<PlayerInfo>();
    		for(int j = 0; j < 10; j++) {
    			x = (int) (int)(Math.random() * ((108) + 1));
    			y = (int) (int)(Math.random() * ((72) + 1));
    			playerInfos.add(new PlayerInfo(j, new Position(x, y)));
    		}
    		int randomPlayer = (int) (int)(Math.random() * (10));
    		if ( (i == 7) || (i == 23) || (i == 29)) {
    			team1Score++;
    			newPercept.setHighlight(new Highlight(new PlayerAction(ActionType.GOAL, 0,0), playerInfos.get(randomPlayer), null));
    		}
    		else if ( (i == 19) || (i == 31) || (i == 37)) {
    			team2Score++;
    			newPercept.setHighlight(new Highlight(new PlayerAction(ActionType.GOAL, 0,0), playerInfos.get(randomPlayer), null));
    		}
    		else {
    			PlayerInfo playerInfo = playerInfos.get(randomPlayer);
    			ActionType type = ActionType.MOVE;
    			if ( i % 3 == 0 ) {
    				type = ActionType.PASS;
    			}
    			else if ( i % 5 == 0 ) {
    				type = ActionType.TACKLE;
    			}
    			if ( i % 7 == 0 ) {
    				type = ActionType.SHOOT;
    			}
    			PlayerAction playerAction = new PlayerAction(type, x,y);
    			Highlight currentHighlight = new Highlight(playerAction, playerInfo, null);
    			newPercept.setHighlight(currentHighlight);
    		}
    		newPercept.setTeam1Score(team1Score);
			newPercept.setTeam2Score(team2Score);
    		newPercept.setPlayerInfoList(playerInfos);
    		perceptSequence.add( newPercept ); // Random ball movement
    	}
    	
    	ArrayList<PlayerInfo_> playerInfoList = new ArrayList<PlayerInfo_>();
    	Percept samplePercept = perceptSequence.get(0);
    	Color team1Color = new Color(255,0,0);
    	Color team2Color = new Color(0,0,255);
    	TeamInfo team1 = new TeamInfo(1, "Team 1", team1Color);
    	TeamInfo team2 = new TeamInfo(2, "Team 2", team2Color);
    	for(int i = 0, size = samplePercept.getPlayerInfoList().size(); i < size; i++) {
    		String playerName = "Player " + i;
    		if ( i < size/2 ) {
    			playerInfoList.add(new PlayerInfo_(samplePercept.getPlayerInfoList().get(i).getID(), playerName, i, team1Color));
    		}
    		else {
    			playerInfoList.add(new PlayerInfo_(samplePercept.getPlayerInfoList().get(i).getID(), playerName, i, team2Color));
    		}
    	}
    	
    	String directoryPath = System.getProperty("user.home")  +  "/CEngBall" ;
    	File saveFileDirectory = new File(directoryPath);
    	if ( !saveFileDirectory.exists() ) {
    		saveFileDirectory.mkdir();
    	}
    	demoFilePath = System.getProperty("user.home")  +  "/CEngBall/" +  FileUtils.generateFileName("json");
    	SaveFile savefile = new SaveFile(demoFilePath, perceptSequence, new Metadata(team1, team2, playerInfoList));
    	savefile.saveToFile();
    }
	

    
    public void loadSequenceFromFile(String filePath) {
    	saveFile = SaveFile.getFromFile(filePath);
		metadata = saveFile.getMetadata();
    }
    
    public void updateScore(int team1score, int team2score) {
    	team1Score.setText(Integer.toString(team1score));
    	team2Score.setText(Integer.toString(team2score));
    }
    
    public void updateCommentary(Highlight highlight) {
    	if ( highlight != null ) {
	    	PlayerInfo_ actorInfo = metadata.getPlayer(highlight.getActor1().getID());
	    	if ( actorInfo != null ) {
	    		commentary.setBackground(new Color(actorInfo.getColorR(), actorInfo.getColorG(), actorInfo.getColorB()) );
		    	String comment = "";
		    	if ( highlight.getAction().getType() == ActionType.MOVE) {
		    		Position actorPosition = highlight.getActor1().getPosition();
		    		comment = actorInfo.getName() + " moves to [" + actorPosition.getX() + "," + actorPosition.getY() + "]";
		    		commentary.setText(comment);
		    	}
		    	else if ( highlight.getAction().getType() == ActionType.PASS) {
		    		Position actorPosition = highlight.getActor1().getPosition();
		    		comment = actorInfo.getName() + " passes the ball to [" + actorPosition.getX() + "," + actorPosition.getY() + "]";
		    		commentary.setText(comment);
		    	}
		    	else if ( highlight.getAction().getType() == ActionType.SHOOT) {
		    		Position actorPosition = highlight.getActor1().getPosition();
		    		comment = actorInfo.getName() + " shoots the ball to [" + actorPosition.getX() + "," + actorPosition.getY() + "]";
		    		commentary.setText(comment);
		    	}
		    	else if ( highlight.getAction().getType() == ActionType.TACKLE) {
		    		comment = actorInfo.getName() + " tackles the ball";
		    		commentary.setText(comment);
		    	}
		    	else if ( highlight.getAction().getType() == ActionType.GOAL) {
		    		comment = actorInfo.getName() + " scores!";
		    		commentary.setText(comment);
		    	}
	    	}
    	}
    }
}
