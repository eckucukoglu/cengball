package gui;

import imports.DynamicCompiler;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import utils.CBLogger;
import utils.Enums;
import utils.FileUtils;
import utils.UserSettings;
import utils.Utils;

public class ImportPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private DefaultButton cancelButton, backButton, forwardButton;
	private JPanel importPanel;
	private JLabel textArea;
	private JFileChooser fileChooser;
	private ImportState state;
	private Image backgroundImage, backgroundOrg;
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();
	
	enum ImportState {
		READY, BROWSED, FINISHED
	}

	public ImportPanel() {
		setBackground(Enums.black);
		setLayout(null);
		
		double widthRatio = Utils.getScreenWidthRatio((int) Utils.getScreenDimension().getWidth());
		
		state = ImportState.READY;
		
		importPanel = new JPanel();
		importPanel.setLayout(null);
		importPanel.setBackground(Enums.transSgigray);
		this.add(importPanel);
		
		textArea = new JLabel("Browse the team code to be imported");
		textArea.setFont(FileUtils.getFont("Zekton.ttf", (int) (24 * widthRatio), Font.PLAIN));
		textArea.setHorizontalAlignment(SwingConstants.CENTER);
		textArea.setBackground(null);
		importPanel.add(textArea);
		
		fileChooser = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Java File","java");
		fileChooser.setFileFilter(filter);
		
		cancelButton = new DefaultButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("Menu");
				LOGGER.log(Level.INFO, "Cancelled code importing");
			}
		});
		this.add(cancelButton);
		
		backButton = new DefaultButton("Back");
		backButton.setVisible(false);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( state == ImportState.BROWSED ) {
					state = ImportState.READY;
					textArea.setText("Browse the team code to be imported");
					backButton.setVisible(false);
					forwardButton.setText("Browse");
					LOGGER.log(Level.INFO, "Backed to browsing state");
				}
			}
		});
		this.add(backButton);
		
		forwardButton = new DefaultButton("Browse");
		forwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( state == ImportState.READY ) {
					int returnValue = fileChooser.showOpenDialog(getInstance());
					if (returnValue == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						if ( file.getAbsolutePath().endsWith(".java") ) {
							backButton.setVisible(true);
							state = ImportState.BROWSED;
							textArea.setText(file.getName() + " is browsed");
							forwardButton.setText("Check");
							LOGGER.log(Level.INFO, "Browsed successfully");
						}
						else {
							textArea.setText(file.getName() + " cannot be browsed");
							LOGGER.log(Level.WARNING, "Cannot browse file : " + file.getName());
						}
					}
				}
				else if ( state == ImportState.BROWSED ) {
					DynamicCompiler compiler = new DynamicCompiler(fileChooser.getSelectedFile().getAbsolutePath());

					if ( compiler.compile() ) {
						textArea.setText(fileChooser.getSelectedFile().getName() + " is compiled");
						LOGGER.log(Level.INFO, "Compiled file : " + fileChooser.getSelectedFile().getName());
						state = ImportState.FINISHED;
						backButton.setVisible(false);			
						cancelButton.setVisible(false);
						forwardButton.setText("Finish");
					}
					else {
						textArea.setText(fileChooser.getSelectedFile().getName() + " cannot be compiled");
						LOGGER.log(Level.WARNING, "Cannot compile file : " + fileChooser.getSelectedFile().getName());
					}
				}
				else if ( state == ImportState.FINISHED ) {
					( (MainPanel) getInstance().getParent() ).show("Menu");
					LOGGER.log(Level.INFO, "Returned back to Main Menu");
				}
			}
		});
		this.add(forwardButton);
		
		arrangeLayout();
		refresh();
	}

	public ImportPanel getInstance() {
		return this;
	}
	
	public void refresh() {
		state = ImportState.READY;
		textArea.setText("Browse the team code to be imported");
		backButton.setVisible(false);
		cancelButton.setVisible(true);
		forwardButton.setText("Browse");
		UserSettings.loadSettings();
		if (UserSettings.jdkPath.equals("")) {
			textArea.setText("JDK Path is not specified. Please set your JDK path in Settings");
			forwardButton.setVisible(false);
			LOGGER.log(Level.WARNING, "Not specified : JDK Path");
		}
		else {
			forwardButton.setVisible(true);
		}
		LOGGER.log(Level.INFO, "Refreshed screen");
		repaint();
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

		importPanel.setBounds(widthCut, heightCut, panelWidth, panelHeight);
		cancelButton.setBounds(widthCut, panelHeight + (2*heightCut), buttonWidth, buttonHeight);
		forwardButton.setBounds(width - (widthCut + buttonWidth), panelHeight + (2*heightCut), buttonWidth, buttonHeight);
		backButton.setBounds(width - 2*(widthCut + buttonWidth), panelHeight + (2*heightCut), buttonWidth, buttonHeight);
		textArea.setBounds(widthCut, heightCut * 3, panelWidth - 2*widthCut, panelHeight - 6*heightCut);
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
	
	@Override
	public void paintComponent(final Graphics g) {
	    super.paintComponent(g);
	    if (backgroundImage != null) {
	        g.drawImage(backgroundImage, 0, 0, null);
	    }
	}
}
