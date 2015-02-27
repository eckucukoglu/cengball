package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import utils.CBLogger;
import utils.Enums;
import utils.FileUtils;
import utils.Utils;

public class WatchPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private DefaultButton backButton, watchButton, showStatsButton, deleteButton;
	private JPanel matchPanel;
	private JLabel matchsLabel;
	private String[] saveFiles;
	private JList<String> matchList;
	private DefaultListModel<String> matchListModel;
	private JScrollPane listScrollPane;
	private Image backgroundImage, backgroundOrg;
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();

	public WatchPanel() {
		setBackground(Enums.black);
		setLayout(null);
		
		double widthRatio = Utils.getScreenWidthRatio((int) Utils.getScreenDimension().getWidth());
		
		matchPanel = new JPanel();
		matchPanel.setLayout(null);
		matchPanel.setBackground(Enums.transSgigray);
		this.add(matchPanel);
		
		matchsLabel = new JLabel("Matchs");
		matchsLabel.setForeground(Enums.white);
		matchsLabel.setBackground(null);
		matchsLabel.setFont(FileUtils.getFont("Zekton.ttf", (int) (18 * widthRatio), Font.PLAIN));
		matchPanel.add(matchsLabel);
		
		backButton = new DefaultButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				( (MainPanel) getInstance().getParent() ).show("Menu");
			}
		});
		this.add(backButton);
		
		watchButton = new DefaultButton("Watch");
		watchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( matchList.getSelectedIndex() != -1 ) {
					String saveFilePath = FileUtils.getSaveFileDirectory() + "/" + saveFiles[matchList.getSelectedIndex()];
					( (MainPanel) getInstance().getParent() ).sendSaveFilePathToMatch(saveFilePath);
					( (MainPanel) getInstance().getParent() ).show("Match");
				}
			}
		});
		this.add(watchButton);
		
		showStatsButton = new DefaultButton("Show Stats");
		showStatsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( matchList.getSelectedIndex() != -1 ) {
					String saveFilePath = FileUtils.getSaveFileDirectory() + "/" + saveFiles[matchList.getSelectedIndex()];
					( (MainPanel) getInstance().getParent() ).sendSaveFilePathToStats(saveFilePath);
					( (MainPanel) getInstance().getParent() ).show("MatchStats");
				}
			}
		});
		this.add(showStatsButton);
		
		deleteButton = new DefaultButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( matchList.getSelectedIndex() != -1 ) {
					String saveFilePath = FileUtils.getSaveFileDirectory() + "/" + saveFiles[matchList.getSelectedIndex()];
					int selectedOption = JOptionPane.showConfirmDialog(getInstance(), 
                            "Are you sure to delete this file?", 
                            "Deletion", 
                            JOptionPane.YES_NO_OPTION); 
					if (selectedOption == JOptionPane.YES_OPTION) {
						try {
							Files.delete(new File(saveFilePath).toPath());
							LOGGER.log(Level.INFO, "Deleted file : " + saveFilePath);
						} catch (IOException e1) {
							LOGGER.log(Level.WARNING, "Cannot delete file : " + saveFilePath);
						}
					}		
				
					refresh();
				}
			}
		});
		this.add(deleteButton);
		
		listScrollPane = new JScrollPane();
		matchListModel = new DefaultListModel<String>();
		matchList = new JList<String>(matchListModel);
		matchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		matchList.setLayoutOrientation(JList.VERTICAL);
		matchList.setFont(FileUtils.getFont("Zekton.ttf", (int) (18 * widthRatio), Font.PLAIN));
		matchList.setVisibleRowCount(-1);
		matchList.setBackground(Enums.white);
		DefaultListCellRenderer renderer =  (DefaultListCellRenderer) matchList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.CENTER); 
		matchList.setForeground(Enums.black);
		listScrollPane.setViewportView(matchList);
		matchPanel.add(listScrollPane);
		
		refresh();
		arrangeLayout();
	}
	
	public WatchPanel getInstance() {
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

		matchPanel.setBounds(widthCut, heightCut, panelWidth, panelHeight);
		backButton.setBounds(widthCut, panelHeight + (2*heightCut), buttonWidth, buttonHeight);
		watchButton.setBounds(width - widthCut - buttonWidth, panelHeight + (2*heightCut), buttonWidth, buttonHeight);
		showStatsButton.setBounds(width - 2*widthCut - 2*buttonWidth, panelHeight + (2*heightCut), buttonWidth, buttonHeight);
		deleteButton.setBounds(width - 3*widthCut - 3*buttonWidth, panelHeight + (2*heightCut), buttonWidth, buttonHeight);
		matchsLabel.setBounds((int) ((panelWidth - matchsLabel.getPreferredSize().getWidth())/2), heightCut/3, panelWidth, buttonHeight);
		listScrollPane.setBounds(widthCut, heightCut*4, panelWidth - 2*widthCut, panelHeight - 6*heightCut);
	}

	public void refresh() {
		matchListModel.clear();
		saveFiles = FileUtils.getSaveFiles();
		saveFiles = sortFilesAccordingToDate(saveFiles);
		String[] formattedSaveFiles = formatSaveFiles(saveFiles);
		if ( formattedSaveFiles != null ) {
			for(int i = 0; i < formattedSaveFiles.length; i++) {
				matchListModel.add(i, formattedSaveFiles[i]);
			}
		}
		repaint();
	}
	
	public static String[] sortFilesAccordingToDate(String[] saveFileNames) {
		if ( saveFileNames != null ) {
			ArrayList<String> dateList = new ArrayList<String>(Arrays.asList(saveFileNames));
			Utils.sortDates( dateList );
			Collections.reverse(dateList);
			return dateList.toArray(new String[0]);
		}
		return null;
	}
	
	public static String[] formatSaveFiles(String[] saveFileNames) {
		String[] saveFiles = null;
		if ( saveFileNames != null ) {
			saveFiles = new String[saveFileNames.length];
			Calendar calendar = Calendar.getInstance();
			for(int i = 0; i < saveFileNames.length; i++) {
				calendar.setTime(FileUtils.getSaveFileDate(FileUtils.removeExtension(saveFileNames[i]) ));
				int month = calendar.get(Calendar.MONTH) + 1;
				saveFiles[i] = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) +
						" - " + calendar.get(Calendar.DAY_OF_MONTH) + "." + month + "." + calendar.get(Calendar.YEAR);
			}
		}
		return saveFiles;
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
