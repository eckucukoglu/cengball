package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Enums;
import utils.FileUtils;
import utils.Utils;

public class MenuPanel extends JPanel {
	private DefaultButton exitButton, kickOffButton, importButton;
	private JLabel cengBallText;
	private Image backgroundImage, backgroundOrg;

	/**
	 * Create the panel.
	 */
	public MenuPanel() {
		setBackground(Enums.black);
		setLayout(null);
		
		exitButton = new DefaultButton("EXIT");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		add(exitButton);
		
		kickOffButton = new DefaultButton("KICK OFF");
		kickOffButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("KickOff");
			}
		});
		add(kickOffButton);
		
		importButton = new DefaultButton("IMPORT TEAM");
		importButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		add(importButton);
		
		cengBallText = new JLabel("CengBall");
		cengBallText.setForeground(Enums.white);
		cengBallText.setFont(FileUtils.getFont("Zeroes.ttf", 108, Font.BOLD));
		add(cengBallText);
		
		arrangeLayout();
	}
	
	public MenuPanel getInstance() {
		return this;
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
		importButton.setBounds(widthCut, height - heightCut*2, buttonWidth, buttonHeight);
		kickOffButton.setBounds(widthCut, height - heightCut*3, buttonWidth, buttonHeight);
	}
	
	@Override
	public void paintComponent(final Graphics g) {
	    super.paintComponent(g);
	    if (backgroundImage != null)
	        g.drawImage(backgroundImage, 0, 0, null);
	}
}
