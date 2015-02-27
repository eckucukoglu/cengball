package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Enums;
import utils.FileUtils;

public class MenuPanel extends JPanel {
	private DefaultButton exitButton, singlePlayerButton, multiPlayerButton, demoButton, settingsButton;
	private JLabel cengBallText;

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
		
		settingsButton = new DefaultButton("SETTINGS");
		settingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		add(settingsButton);
		
		multiPlayerButton = new DefaultButton("MULTIPLAYER");
		multiPlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		add(multiPlayerButton);
		
		singlePlayerButton = new DefaultButton("SINGLE PLAYER");
		singlePlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		add(singlePlayerButton);
		
		demoButton = new DefaultButton("DEMO");
		demoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("Match");
			}
		});
		add(demoButton);
		
		cengBallText = new JLabel("CengBall");
		cengBallText.setForeground(Color.WHITE);
		cengBallText.setBackground(Color.WHITE);
		cengBallText.setFont(FileUtils.getFont("Zeroes.ttf", 108, Font.BOLD));
		add(cengBallText);

		arrangeLayout();
	}
	
	public MenuPanel getInstance() {
		return this;
	}
	
	private void arrangeLayout() { 
		Dimension screenDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		// TODO arrange it according to the size of the screen
		int width = (int) screenDimension.getWidth();
		int height = (int) screenDimension.getHeight();
		
		cengBallText.setBounds(100, 40, 600, 100);
		exitButton.setBounds(100, height - 100, 300, 60);
		settingsButton.setBounds(100, height - 200, 300, 60);
		multiPlayerButton.setBounds(100, height - 300, 300, 60);
		singlePlayerButton.setBounds(100, height - 400, 300, 60);
		demoButton.setBounds(100, height - 500, 300, 60);
	}
}
