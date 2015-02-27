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

public class MenuPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private DefaultButton exitButton, kickOffButton, importButton, watchButton, settingsButton;
	private JLabel cengBallText;
	private Image backgroundImage, backgroundOrg;
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();

	/**
	 * Create the panel.
	 */
	public MenuPanel() {
		setBackground(Enums.black);
		setLayout(null);
		
		double widthRatio = Utils.getScreenWidthRatio((int) Utils.getScreenDimension().getWidth());
		
		exitButton = new DefaultButton("EXIT");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOGGER.log(Level.INFO, "Exited game");
				System.exit(0);
			}
		});
		add(exitButton);
		
		kickOffButton = new DefaultButton("KICK OFF");
		kickOffButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("KickOff");
				LOGGER.log(Level.INFO, "Button pressed : Kick Off");
			}
		});
		add(kickOffButton);
		
		importButton = new DefaultButton("IMPORT TEAM");
		importButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("Import");
				LOGGER.log(Level.INFO, "Button pressed : Import");
			}
		});
		add(importButton);
		
		watchButton = new DefaultButton("WATCH");
		watchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("Watch");
				LOGGER.log(Level.INFO, "Button pressed : Watch");
			}
		});
		add(watchButton);
		
		settingsButton = new DefaultButton("SETTINGS");
		settingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("Settings");
				LOGGER.log(Level.INFO, "Button pressed : Settings");
			}
		});
		add(settingsButton);
		
		cengBallText = new JLabel("CengBall");
		cengBallText.setForeground(Enums.white);
		cengBallText.setFont(FileUtils.getFont("Zeroes.ttf", (int) (88 * widthRatio), Font.BOLD));
		add(cengBallText);
		
		arrangeLayout();
	}
	
	public MenuPanel getInstance() {
		return this;
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
	
	private void arrangeLayout() { 
		Dimension screenDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenDimension.getWidth();
		int height = (int) screenDimension.getHeight();
		double sWRatio = Utils.getScreenWidthRatio(width);
		double sHRatio = Utils.getScreenHeightRatio(height);
		int widthCut = (int) (100 * sWRatio), heightCut = (int) (80 * sHRatio);
		int buttonHeight = (int) (Enums.DEFAULT_BUTTON_HEIGHT * sHRatio);
		int buttonWidth = (int) (Enums.DEFAULT_BUTTON_WIDTH * sWRatio);
		int panelWidth = (int) (600 * sWRatio), panelHeight = (int) (100 * sHRatio);
		
		cengBallText.setBounds(widthCut, heightCut, panelWidth, panelHeight);
		exitButton.setBounds(widthCut, height - heightCut, buttonWidth, buttonHeight);
		settingsButton.setBounds(widthCut, height - heightCut*2, buttonWidth, buttonHeight);
		importButton.setBounds(widthCut, height - heightCut*3, buttonWidth, buttonHeight);
		watchButton.setBounds(widthCut, height - heightCut*4, buttonWidth, buttonHeight);
		kickOffButton.setBounds(widthCut, height - heightCut*5, buttonWidth, buttonHeight);
	}
	
	@Override
	public void paintComponent(final Graphics g) {
	    super.paintComponent(g);
	    if (backgroundImage != null) {
	        g.drawImage(backgroundImage, 0, 0, null);
	    }
	}
}
