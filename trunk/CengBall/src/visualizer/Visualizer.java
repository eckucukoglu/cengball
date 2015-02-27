package visualizer;
import gui.MatchPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import rare.Metadata;
import rare.Percept;
import rare.Pitch;
import rare.SaveFile;
import rare.TeamInfo;
import utils.CBLogger;
import utils.Enums;
import utils.Utils;

import common.Ball;
import common.Player;

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
	private Metadata metadata;
	private long time;
	
	private double ratio;

	private ArrayList<Percept> smoothSequence;
	private int currentPercept;
	
	private boolean mRun;
	private int fps;
	
	private Font font;
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();
	
	public Visualizer(int matchLength, int fps) {
		super();
		setBackground( Enums.darkgreen );
		currentPercept = 0;
		this.fps = fps;
		this.time = 0;
	}
	
	private void initializePitchDimensions() {
		ratio = metadata.getPitch().calculateRatio(width, height);

		pitchW = metadata.getPitch().getPitchWidth() * ratio;
		pitchH = metadata.getPitch().getPitchHeight() * ratio;
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
    	int sliceW = (int)Math.ceil(pitchW / Pitch.DEFAULT_PATTERN_LINE_COUNT);
    	for(int i = 0, x1 = (int) (pitchW/2), x2 = (int) (pitchW/2); i < Pitch.DEFAULT_PATTERN_LINE_COUNT/2; i++) {
    		Color color1, color2;
    		if ( i % 2 == 0 ) {
    			color1 = Enums.forestgreen;
    			color2 = Enums.darkgreen;
    		}
    		else {
    			color1 = Enums.darkgreen;
    			color2 = Enums.forestgreen;
    		}
    		
    		if ( i == Pitch.DEFAULT_PATTERN_LINE_COUNT/2 - 1 ) {
    			sliceW = x2;
    		}
    		Rectangle2D.Double slice1 = new Rectangle2D.Double(x1 + widthCut, heightCut, sliceW, pitchH);
    		g2d.setColor(color1);
    		g2d.draw( slice1 );
    		g2d.fill( slice1 );
    		
    		Rectangle2D.Double slice2 = new Rectangle2D.Double(x2 - sliceW + widthCut, heightCut, sliceW, pitchH);
    		g2d.setColor(color2);
    		g2d.draw( slice2 );
    		g2d.fill( slice2 );
    		
    		x1 += sliceW;
    		x2 -= sliceW;
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
    
    private void drawPlayer(Graphics2D g2d, Player player, Color playerColor1, Color playerColor2, int kitNumber, String name) {
    	Ellipse2D.Double samplePlayer = new Ellipse2D.Double(mainPitch.getX() + player.getUpperLeftCornerPosition().getX() * ratio, mainPitch.getY() + player.getUpperLeftCornerPosition().getY() * ratio, playerRadius*2, playerRadius*2);
    	g2d.setColor(playerColor1);
    	g2d.setStroke(new BasicStroke(1));
		g2d.draw(samplePlayer);
    	g2d.fill(samplePlayer);
    	g2d.setColor(playerColor2);
    	g2d.setFont(font);
    	g2d.drawString(Integer.toString(kitNumber), (int)(mainPitch.getX() + player.getUpperLeftCornerPosition().getX() * ratio + playerRadius/1.7), (int)(mainPitch.getY() + player.getUpperLeftCornerPosition().getY() * ratio + 1.7*playerRadius));
    }
    
    private void drawBall(Graphics2D g2d, Ball ball) {
    	Ellipse2D.Double sampleBall = new Ellipse2D.Double(mainPitch.getX() + ball.getUpperLeftCornerPosition().getX() * ratio, mainPitch.getY() + ball.getUpperLeftCornerPosition().getY() * ratio, ballRadius*2, ballRadius*2);
		g2d.setColor(Enums.white);
		g2d.draw(sampleBall);
    	g2d.fill(sampleBall);
    	g2d.setStroke(new BasicStroke(2));
    	g2d.setColor(Enums.black);
    	g2d.draw(sampleBall);
    }
    
    public void initializeVisualizer(SaveFile saveFile, Metadata metadata) {
		this.metadata = metadata;
		this.smoothSequence = saveFile.getPerceptList();
		this.time = 0;
		( (MatchPanel) this.getParent() ).updateTime(time);
    }
    
    // When window.repaint() is called, this function is called.
    public void paintComponent( Graphics g ) {
    	super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		width = this.getWidth();
		height = this.getHeight();
		initializePitchDimensions();
		
		Font f = g2d.getFont();
    	font = new Font(f.getName(), f.getStyle(), (int) (Enums.DEFAULT_FONT_SIZE * Utils.getScreenWidthRatio(width)));
		
		drawPitch(g2d);
		if ( smoothSequence != null && (currentPercept >= 0 && currentPercept < smoothSequence.size()) ) {
			drawBall(g2d,smoothSequence.get(currentPercept).getBall());
			for(int i = 0; i < smoothSequence.get(currentPercept).getPlayers().length; i++) {
				Player player = smoothSequence.get(currentPercept).getPlayers()[i];
				TeamInfo teamInfo = metadata.getTeamInfo(player.getTeamID());
				if ( teamInfo != null ) {
					drawPlayer(g2d, player, teamInfo.getColor1(), teamInfo.getColor2(), player.getKitNumber(), player.getName());
				}
			}
		}
    }
    
    public void reset() {
    	currentPercept = 0;
    	this.time = 0;
		( (MatchPanel) this.getParent() ).updateScore(0, 0);
		( (MatchPanel) this.getParent() ).updateTime(time);
    	repaint();
    }
    
    public void endMatch() {
    	currentPercept = smoothSequence.size() - 1;
		( (MatchPanel) this.getParent() ).updateScore(smoothSequence.get(currentPercept).getTeam1Score(), smoothSequence.get(currentPercept).getTeam2Score());
		( (MatchPanel) this.getParent() ).updateTime(time);
		repaint();
    	mRun = false;
    }
    
    public void forward() {
    	if ( currentPercept != smoothSequence.size()-1) {
    		currentPercept++;
    		this.time += 1000 / fps;
    		( (MatchPanel) this.getParent() ).updateScore(smoothSequence.get(currentPercept).getTeam1Score(), smoothSequence.get(currentPercept).getTeam2Score());
			( (MatchPanel) this.getParent() ).updateCommentary(smoothSequence.get(currentPercept).getHighlight());
			( (MatchPanel) this.getParent() ).updateTime(time);
			repaint();  
    	}
    }
    
    public void backward() {
    	if ( currentPercept != 0 ) {
	    	currentPercept--;
	    	this.time -= 1000 / fps;
    		( (MatchPanel) this.getParent() ).updateScore(smoothSequence.get(currentPercept).getTeam1Score(), smoothSequence.get(currentPercept).getTeam2Score());
			( (MatchPanel) this.getParent() ).updateCommentary(smoothSequence.get(currentPercept).getHighlight());
			( (MatchPanel) this.getParent() ).updateTime(time);
			repaint();
    	}
    }

	@Override
	public void run() {
		mRun = true;
		
		int sleepTime = 1000 / fps;
		for (int i = currentPercept; i < smoothSequence.size() && mRun; i++) {  
			this.currentPercept = i;
			( (MatchPanel) this.getParent() ).updateScore(smoothSequence.get(currentPercept).getTeam1Score(), smoothSequence.get(currentPercept).getTeam2Score());
			( (MatchPanel) this.getParent() ).updateCommentary(smoothSequence.get(currentPercept).getHighlight());
			if ( smoothSequence.get(currentPercept).getHighlight().getActionType() != Enums.HighlightType.GOAL ) {
				this.time += sleepTime;
				( (MatchPanel) this.getParent() ).updateTime(time);
			}
			repaint();
			
			threadSleep(sleepTime);
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
	
	private void threadSleep(int milis) {
		 try {  
             Thread.sleep(milis);
         } catch (Exception ex) {
        	 LOGGER.log(Level.WARNING, "Exception : " + ex);
         }  
	}
}
