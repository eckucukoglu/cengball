package visualizer;
import gui.MatchPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import simulator.PlayerInfo;
import utils.Enums;
import utils.Pitch;

import common.Ball;
import common.Metadata;
import common.Percept;
import common.PlayerInfo_;
import common.Position;
import common.SaveFile;

//
//  @ Project : CengBall
//  @ File Name : Visualizer.java
//  @ Date : 11.11.2013
//  @ Author : Project Contorium
//
//


@SuppressWarnings("serial")
public class Visualizer extends JPanel implements Runnable {
	private int width, height;
	// Pitch Dimensions
	private double widthCut, heightCut;
	private double pitchW, pitchH;
	private double radius, cornerRadius, dotRadius, playerRadius, ballRadius;
	private double postHeight, postWidth, goalAreaW, goalAreaH;
	private double penaltyAreaW, penaltyAreaH;
	private double dotDistance;
	private Rectangle2D.Double mainPitch;
	private Pitch myPitch;
	private Metadata metadata;
	
	private double ratio;

	private ArrayList<Percept> smoothSequence;
	private int currentPercept;
	
	private boolean mRun;
	private SaveFile saveFile;
	private int matchLength, fps;
	
	public Visualizer(SaveFile saveFile, Metadata metadata, int matchLength, int fps) {
		super();
		setBackground( Enums.darkgreen );
		currentPercept = 0;
		this.saveFile = saveFile;
		this.metadata = metadata;
		this.matchLength = matchLength;
		this.myPitch = new Pitch(metadata.getPitchWidth(), metadata.getPitchHeight());
		this.smoothSequence = createSmoothSequence(saveFile.getPerceptList(), matchLength, fps);
		this.fps = fps;
	}
	
	private void initializePitchDimensions() {
		ratio = myPitch.calculateRatio(width, height);

		pitchW = myPitch.getPitchWidth() * ratio;
		pitchH = myPitch.getPitchHeight() * ratio;
		widthCut = (width - pitchW) / 2.0;
    	heightCut = (height - pitchH) / 2.0;
    	radius = Pitch.DEFAULT_RADIUS * ratio;
    	cornerRadius = Pitch.DEFAULT_CORNER_RADIUS*ratio;
    	dotRadius = Pitch.DEFAULT_DOT_RADIUS*ratio;
    	postHeight = Pitch.DEFAULT_POST_HEIGHT * ratio;
    	postWidth = Pitch.DEFAULT_POST_WIDTH * ratio; // To show there is a goal post
    	goalAreaW = Pitch.DEFAULT_GOAL_AREA_LINE_WIDTH * ratio;
    	goalAreaH = Pitch.DEFAULT_GOAL_AREA_LINE_HEIGHT * ratio;
    	penaltyAreaW = Pitch.DEFAULT_PENALTY_AREA_WIDTH * ratio;
    	penaltyAreaH = Pitch.DEFAULT_PENALTY_AREA_HEIGHT * ratio;
    	dotDistance = Pitch.DEFAULT_DOT_DISTANCE * ratio;
    	playerRadius = Pitch.DEFAULT_PLAYER_RADIUS * ratio;
    	ballRadius = Pitch.DEFAULT_BALL_RADIUS * ratio;
	}
	
    private void drawPitch(Graphics2D g2d) {    	
    	int sliceW = (int)Math.ceil(pitchW / 16.0);
    	for ( int x = 0; x <= pitchW; x += sliceW) {
    		if ( (x / sliceW) % 2 == 0 ) {
    			g2d.setColor(Enums.forestgreen);
    		}
    		else if ( (x / sliceW) % 2 == 1 ) {
    			g2d.setColor(Enums.darkgreen);
    		}
    		Rectangle2D.Double slice = new Rectangle2D.Double(x + widthCut, heightCut, sliceW, pitchH);
    		g2d.draw( slice );
    		g2d.fill( slice );
    	}
    	
    	g2d.setStroke( new BasicStroke(2) );
    	g2d.setColor(Enums.white);
    	// Draw main pitch margins
    	mainPitch = new Rectangle2D.Double(widthCut, heightCut, pitchW, pitchH);
    	g2d.draw(mainPitch);
    	
    	// Draw center
    	Line2D.Double middleLine = new Line2D.Double(mainPitch.getCenterX(), mainPitch.getY(), mainPitch.getCenterX(), mainPitch.getY() + mainPitch.getHeight());
    	g2d.draw(middleLine);
    	Ellipse2D.Double centerCircle = new Ellipse2D.Double(mainPitch.getCenterX() - radius, mainPitch.getCenterY() - radius, radius*2, radius*2);
    	g2d.draw(centerCircle);
    	Ellipse2D.Double centerDot = new Ellipse2D.Double(mainPitch.getCenterX() - dotRadius, mainPitch.getCenterY() - dotRadius, dotRadius*2, dotRadius*2);
    	g2d.draw(centerDot);
    	g2d.fill(centerDot);
    	
    	// Draw corners
    	// Top Left
    	g2d.drawArc((int)(mainPitch.getX() - cornerRadius), (int)(mainPitch.getY() - cornerRadius), (int)cornerRadius*2, (int)cornerRadius*2, 270, 90);
    	// Top Right
    	g2d.drawArc((int)(mainPitch.getMaxX() - cornerRadius), (int)(mainPitch.getY() - cornerRadius), (int)cornerRadius*2, (int)cornerRadius*2, 180, 90);
    	// Bottom Left
    	g2d.drawArc((int)(mainPitch.getX() - cornerRadius), (int)(mainPitch.getMaxY() - cornerRadius), (int)cornerRadius*2, (int)cornerRadius*2, 0, 90);
    	// Bottom Right
    	g2d.drawArc((int)(mainPitch.getMaxX() - cornerRadius), (int)(mainPitch.getMaxY() - cornerRadius), (int)cornerRadius*2, (int)cornerRadius*2, 90, 90);
    
    	double angle = Math.atan( 1 / Math.tan( (penaltyAreaW - dotDistance) / radius) );
    	// Draw Left Penalty Area
    	// Left Goal Post
    	g2d.draw(new Rectangle2D.Double(mainPitch.getX() - postWidth, mainPitch.getCenterY() - (postHeight/2.0), postWidth, postHeight));
    	g2d.draw(new Rectangle2D.Double(mainPitch.getX(), mainPitch.getCenterY() - (goalAreaH/2.0), goalAreaW, goalAreaH));
    	g2d.draw(new Rectangle2D.Double(mainPitch.getX(), mainPitch.getCenterY() - (penaltyAreaH/2.0), penaltyAreaW, penaltyAreaH));
    	Ellipse2D.Double leftDot = new Ellipse2D.Double(mainPitch.getX() + dotDistance, mainPitch.getCenterY() - dotRadius, dotRadius*2, dotRadius*2);
    	g2d.draw(leftDot);
    	g2d.fill(leftDot);
    	g2d.drawArc((int)(mainPitch.getX() + dotDistance  - radius), (int)(mainPitch.getCenterY() - radius), (int)radius*2, (int)radius*2, (-1) * (int)Math.toDegrees(angle), (int)Math.toDegrees(angle) * 2);
    	
    	
    	// Draw Right Penalty Area
    	// Right Goal Post
    	g2d.draw(new Rectangle2D.Double(mainPitch.getMaxX(), mainPitch.getCenterY() - (postHeight/2.0), postWidth, postHeight));
    	g2d.draw(new Rectangle2D.Double(mainPitch.getMaxX() - goalAreaW, mainPitch.getCenterY() - (goalAreaH/2.0), goalAreaW, goalAreaH));
    	g2d.draw(new Rectangle2D.Double(mainPitch.getMaxX() - penaltyAreaW, mainPitch.getCenterY() - (penaltyAreaH/2.0), penaltyAreaW, penaltyAreaH));
    	Ellipse2D.Double rightDot = new Ellipse2D.Double(mainPitch.getMaxX() - dotDistance, mainPitch.getCenterY() - dotRadius, dotRadius*2, dotRadius*2);
    	g2d.draw(rightDot);
    	g2d.fill(rightDot);
    	g2d.drawArc((int)(mainPitch.getMaxX() - dotDistance  - radius), (int)(mainPitch.getCenterY() - radius), (int)radius*2, (int)radius*2, 180 + (-1) * (int)Math.toDegrees(angle), (int)Math.toDegrees(angle) * 2);
    	
    }
    
    private void drawPlayer(Graphics2D g2d, PlayerInfo playerInfo, Color playerColor, int kitNumber, String name) {
    	Ellipse2D.Double samplePlayer = new Ellipse2D.Double(mainPitch.getX() + playerInfo.getPosition().getX() * ratio, mainPitch.getY() + playerInfo.getPosition().getY() * ratio, playerRadius*2, playerRadius*2);
		g2d.setColor(playerColor);
		g2d.draw(samplePlayer);
    	g2d.fill(samplePlayer);
    	g2d.setColor(Enums.white);
    	// TODO
    	g2d.drawString(Integer.toString(kitNumber), (int)(mainPitch.getX() + playerInfo.getPosition().getX() * ratio + playerRadius/2), (int)(mainPitch.getY() + playerInfo.getPosition().getY() * ratio + playerRadius + 5));
    }
    
    private void drawBall(Graphics2D g2d, Ball ball) {
    	Ellipse2D.Double sampleBall = new Ellipse2D.Double(mainPitch.getX() + ball.getPosition().getX() * ratio, mainPitch.getY() + ball.getPosition().getY() * ratio, ballRadius*2, ballRadius*2);
		g2d.setColor(Enums.white);
		g2d.draw(sampleBall);
    	g2d.fill(sampleBall);
    	g2d.setStroke(new BasicStroke(2));
    	g2d.setColor(new Color(0,0,0));
    	g2d.draw(sampleBall);
    }
    
    // When window.repaint() is called, this function is called.
    public void paintComponent( Graphics g ) {
    	super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		width = this.getWidth();
		height = this.getHeight();
		initializePitchDimensions();
		
		drawPitch(g2d);
		if ( smoothSequence != null && (currentPercept >= 0 && currentPercept < smoothSequence.size()) ) {
			drawBall(g2d,smoothSequence.get(currentPercept).getBall());
			for(int i = 0; i < smoothSequence.get(currentPercept).getPlayerInfoList().size(); i++) {
				PlayerInfo player = smoothSequence.get(currentPercept).getPlayerInfoList().get(i);
				PlayerInfo_ playerInfo = metadata.getPlayer(player.getID());
				if ( playerInfo != null ) {
					drawPlayer(g2d, player, new Color(playerInfo.getColorR(), playerInfo.getColorG(), playerInfo.getColorB()), playerInfo.getKitNumber(), playerInfo.getName());
				}
			}
		}
    }
    
    private ArrayList<Percept> createSmoothSequence(ArrayList<Percept> percepts, int matchLength, int fps) {
    	ArrayList<Percept> resultSequence = null;
    	
    	if ( percepts != null ) {
    		resultSequence = new ArrayList<Percept>();
    		int numberOfSlice = ((matchLength * fps) - percepts.size() ) / (percepts.size() - 1);
    		for(int i = 0; i < percepts.size(); i++) {
    			resultSequence.add(percepts.get(i));
    			if ( i+1 == percepts.size() ) {
    				break;
    			}
    			int numOfPlayers = percepts.get(i).getPlayerInfoList().size();
    			double[] playerDeltaXs = new double[numOfPlayers];
    			double[] playerDeltaYs = new double[numOfPlayers];
    			for(int j = 0; j < numOfPlayers; j++) {
    				playerDeltaXs[j] = (percepts.get(i+1).getPlayerInfoList().get(j).getPosition().getX() - percepts.get(i).getPlayerInfoList().get(j).getPosition().getX())/ (double)numberOfSlice;
    				playerDeltaYs[j] = (percepts.get(i+1).getPlayerInfoList().get(j).getPosition().getY() - percepts.get(i).getPlayerInfoList().get(j).getPosition().getY())/ (double)numberOfSlice;
    			}
    			double ballDeltaX = (percepts.get(i+1).getBall().getPosition().getX() - percepts.get(i).getBall().getPosition().getX()) / (double)numberOfSlice;
    			double ballDeltaY = (percepts.get(i+1).getBall().getPosition().getY() - percepts.get(i).getBall().getPosition().getY()) / (double)numberOfSlice;
    			for(int k = 1; k < numberOfSlice + 1; k++) {
    				Position ballPosition = new Position(percepts.get(i).getBall().getPosition().getX() + k*ballDeltaX, percepts.get(i).getBall().getPosition().getY() + k*ballDeltaY);
    				Percept newPercept = new Percept(new Ball(ballPosition));
    				ArrayList<PlayerInfo> playerInfos = new ArrayList<PlayerInfo>();
    				for(int j = 0; j < numOfPlayers; j++) {
    					Position playerPos = new Position(percepts.get(i).getPlayerInfoList().get(j).getPosition().getX() + k*playerDeltaXs[j], percepts.get(i).getPlayerInfoList().get(j).getPosition().getY() + k*playerDeltaYs[j]);
    					playerInfos.add(new PlayerInfo(percepts.get(i).getPlayerInfoList().get(j).getID(), playerPos));
    				}
    				newPercept.setPlayerInfoList(playerInfos);
    				newPercept.setTeam1Score(percepts.get(i).getTeam1Score());
    				newPercept.setTeam2Score(percepts.get(i).getTeam2Score());
    				resultSequence.add(newPercept);
    			}
    		}
    	}
    	
    	return resultSequence;
    }
    
    public void reset() {
    	currentPercept = 0;
		( (MatchPanel) this.getParent() ).updateScore(0, 0);
    	repaint();
    }
    
    public void endMatch() {
    	currentPercept = smoothSequence.size() - 1;
		( (MatchPanel) this.getParent() ).updateScore(smoothSequence.get(currentPercept).getTeam1Score(), smoothSequence.get(currentPercept).getTeam2Score());
    	repaint();
    	mRun = false;
    }
    
    public void forward() {
    	if ( currentPercept != smoothSequence.size()-1) {
    		currentPercept++;
    		( (MatchPanel) this.getParent() ).updateScore(smoothSequence.get(currentPercept).getTeam1Score(), smoothSequence.get(currentPercept).getTeam2Score());
			( (MatchPanel) this.getParent() ).updateCommentary(smoothSequence.get(currentPercept).getHighlight());
    		repaint();  
    	}
    }
    
    public void backward() {
    	if ( currentPercept != 0 ) {
	    	currentPercept--;
    		( (MatchPanel) this.getParent() ).updateScore(smoothSequence.get(currentPercept).getTeam1Score(), smoothSequence.get(currentPercept).getTeam2Score());
			( (MatchPanel) this.getParent() ).updateCommentary(smoothSequence.get(currentPercept).getHighlight());
    		repaint();
    	}
    }

	@Override
	public void run() {
		mRun = true;
		
		int sleepTime = 1000 / fps;
		for (int i = currentPercept; i < smoothSequence.size() && mRun; i++) {  
			currentPercept = i;
			( (MatchPanel) this.getParent() ).updateScore(smoothSequence.get(currentPercept).getTeam1Score(), smoothSequence.get(currentPercept).getTeam2Score());
			( (MatchPanel) this.getParent() ).updateCommentary(smoothSequence.get(currentPercept).getHighlight());
			repaint();
   
            try {  
                Thread.sleep(sleepTime);
            } catch (Exception ex) {}  
        }
		if ( currentPercept == smoothSequence.size()-1 ) {
			( (MatchPanel) this.getParent() ).finishMatch();
		}
	}
	
	public void setRunning(boolean mRun) {
		this.mRun = mRun;
	}
	
	public boolean getRunning() {
		return this.mRun;
	}
}
