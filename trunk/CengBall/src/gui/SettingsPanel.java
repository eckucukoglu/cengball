package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import utils.CBLogger;
import utils.Enums;
import utils.FileUtils;
import utils.UserSettings;
import utils.Utils;

public class SettingsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private DefaultButton backButton, applyButton, browseButton;
	private JPanel settingsDetailPanel;
	private JLabel settingsLabel, jdkLabel, jdkPathText, userMessage;
	private JCheckBox debugModeBox;
	private JFileChooser fileChooser;
	private Image backgroundImage, backgroundOrg;
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();
	
	public SettingsPanel() {
		setBackground(Enums.black);
		setLayout(null);
		
		double widthRatio = Utils.getScreenWidthRatio((int) Utils.getScreenDimension().getWidth());
		
		settingsDetailPanel = new JPanel();
		settingsDetailPanel.setLayout(null);
		settingsDetailPanel.setBackground(Enums.transSgigray);
		this.add(settingsDetailPanel);
		
		settingsLabel = new JLabel("Settings");
		settingsLabel.setForeground(Enums.white);
		settingsLabel.setBackground(null);
		settingsLabel.setFont(FileUtils.getFont("Zekton.ttf", (int) (18 * widthRatio), Font.PLAIN));
		settingsDetailPanel.add(settingsLabel);
		
		debugModeBox = new JCheckBox("Debug Mode");
		debugModeBox.setForeground(Enums.white);
		debugModeBox.setBackground(null);
		debugModeBox.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		debugModeBox.setSelected(false);
		debugModeBox.setFocusable(false);
		debugModeBox.setOpaque(false);
		debugModeBox.setContentAreaFilled(false);
		debugModeBox.setBorderPainted(false);
		settingsDetailPanel.add(debugModeBox);
		
		jdkLabel = new JLabel("Java Development Kit Path");
		jdkLabel.setForeground(Enums.white);
		jdkLabel.setBackground(null);
		jdkLabel.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		settingsDetailPanel.add(jdkLabel);
		
		jdkPathText = new JLabel("");
		jdkPathText.setForeground(Enums.gray83);
		jdkPathText.setBackground(null);
		jdkPathText.setFont(FileUtils.getFont("Zekton.ttf", (int) (14 * widthRatio), Font.PLAIN));
		settingsDetailPanel.add(jdkPathText);
		
		userMessage = new JLabel("", SwingConstants.RIGHT);
		userMessage.setForeground(Enums.white);
		userMessage.setBackground(null);
		userMessage.setFont(FileUtils.getFont("Zekton.ttf", (int) (12 * widthRatio), Font.PLAIN));
		settingsDetailPanel.add(userMessage);
		
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		backButton = new DefaultButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("Menu");
				refresh();
			}
		});
		this.add(backButton);
		
		applyButton = new DefaultButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserSettings.setDebugMode(debugModeBox.isSelected());
				if ( fileChooser.getSelectedFile() != null ) {
					UserSettings.setJDKPath(fileChooser.getSelectedFile().getAbsolutePath());
				}
				userMessage.setText("Settings are applied.");
				LOGGER.log(Level.INFO, "User settings are updated.");
				repaint();
			}
		});
		this.add(applyButton);
		
		browseButton = new DefaultButton("Browse");
		jdkPathText.setBackground(null);
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnValue = fileChooser.showSaveDialog(getInstance());
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					jdkPathText.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}		
				repaint();
			}
		});
		this.add(browseButton);
		
		
		refresh();
		arrangeLayout();
	}

	public SettingsPanel getInstance() {
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

		settingsDetailPanel.setBounds(widthCut, heightCut, panelWidth, panelHeight);
		backButton.setBounds(widthCut, panelHeight + (2*heightCut), buttonWidth, buttonHeight);
		applyButton.setBounds(width - (widthCut + buttonWidth ) , panelHeight + (2*heightCut), buttonWidth, buttonHeight);
		settingsLabel.setBounds((int) ((panelWidth - settingsLabel.getPreferredSize().getWidth())/2), heightCut, panelWidth, buttonHeight);
		debugModeBox.setBounds(widthCut, 4*heightCut, buttonWidth, buttonHeight);
		jdkLabel.setBounds(widthCut, 8*heightCut, buttonWidth * 3, buttonHeight);
		jdkPathText.setBounds(widthCut, 10*heightCut, buttonWidth * 3, buttonHeight);
		browseButton.setBounds(panelWidth - widthCut - buttonWidth, 10*heightCut, buttonWidth, buttonHeight / 2);
		userMessage.setBounds(panelWidth - widthCut - buttonWidth, panelHeight - heightCut - buttonHeight, buttonWidth, buttonHeight);
	}
	
	public void refresh() {
		UserSettings.loadSettings();
		debugModeBox.setSelected(UserSettings.isDebugModeOn);
		if ( UserSettings.jdkPath.equals("") ) {
			jdkPathText.setText("Not specified");
		}
		else {
			jdkPathText.setText(UserSettings.jdkPath);
		}
		userMessage.setText("");
		repaint();
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
}
