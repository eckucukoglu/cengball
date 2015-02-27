package gui;

import imports.DynamicCompiler;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import rare.Pitch;
import utils.CBLogger;
import utils.Enums;
import utils.FileUtils;

import common.Player;
import common.Team;

public class TeamSelector extends JComponent {
	private static final long serialVersionUID = 1L;
	private String[] elements;
	private int selectedItem;
	private JLabel shownItem, teamName;
	private JList<String> playerList;
	private DefaultListModel<String> playerListModel;
	private JPanel detailPanel;
	private IconButton left, right;
	private Color backgroundColor;
	private Dimension myDimension = new Dimension(300, 500);
	private int numberOfShownPlayers = 5;
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();
	
	TeamSelector(String[] items) {
		if ( items == null ) {
			items = new String[1];
			items[0] = "No item";
		}
		this.elements = items;
		this.selectedItem = 0;
		this.backgroundColor = Enums.darkslategray;
		
		detailPanel = new JPanel();
		detailPanel.setLayout(null);
		detailPanel.setBounds(0, (int)(3 * myDimension.getHeight() / 10), (int)(myDimension.getWidth()), (int)(7 * myDimension.getHeight() / 10));
		this.add(detailPanel);
		
		teamName = new JLabel("");
		teamName.setBounds(0, 0, detailPanel.getWidth(), detailPanel.getHeight() / 5);
		teamName.setFont(FileUtils.getFont("Zekton.ttf", 36, Font.PLAIN));
		teamName.setHorizontalAlignment(SwingConstants.CENTER);
		detailPanel.setOpaque(false);
		detailPanel.add(teamName);
		
		playerListModel = new DefaultListModel<String>();
		playerList = new JList<String>(playerListModel);
		playerList.setSelectionBackground(null);
		playerList.setSelectionForeground(null);
		playerList.setFocusable(false);
		playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playerList.setLayoutOrientation(JList.VERTICAL);
		playerList.setFont(FileUtils.getFont("Zekton.ttf", (int) (18), Font.PLAIN));
		playerList.setVisibleRowCount(-1);
		playerList.setOpaque(false);
		DefaultListCellRenderer renderer =  (DefaultListCellRenderer) playerList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.CENTER); 
		detailPanel.add(playerList);
		
		left = new IconButton("arrow_left.png", "arrow_left_hovered.png", "arrow_left_pressed.png");
		left.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( selectedItem == 0 ) {
					selectedItem = elements.length - 1;
				}
				else {
					selectedItem--;
				}
				updateShownItem();
			}
		});
		left.setBackground(backgroundColor);
		left.setBounds(0, 0, (int)(myDimension.getWidth()/4), (int)(myDimension.getHeight() / 5));
		this.add(left);
		
		shownItem = new JLabel("		");
		shownItem.setFont(FileUtils.getFont("Zekton.ttf", 24, Font.PLAIN));
		shownItem.setForeground(Enums.white);
		shownItem.setBackground(backgroundColor);
		shownItem.setOpaque(true);
		shownItem.setHorizontalAlignment(SwingConstants.CENTER);
		shownItem.setBounds((int)(myDimension.getWidth()/4.0), 0, (int)(myDimension.getWidth()/2), (int)(myDimension.getHeight() / 5));
		this.add(shownItem);
		
		right = new IconButton("arrow_right.png", "arrow_right_hovered.png", "arrow_right_pressed.png");
		right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( selectedItem == elements.length - 1 ) {
					selectedItem = 0;
				}
				else {
					selectedItem++;
				}
				updateShownItem();
			}
		});
		right.setBackground(backgroundColor);
		right.setBounds((int)(3*myDimension.getWidth()/4), 0, (int)(myDimension.getWidth()/4), (int)(myDimension.getHeight() / 5));
		this.add(right);
		
		this.setSize(myDimension);
		updateShownItem();
	}
	
	public String[] getElements() {
		return elements;
	}

	public void setElements(String[] items) {
		if ( items == null ) {
			items = new String[1];
			items[0] = "No item";
		}
		this.elements = items;
		this.selectedItem = 0;
		updateShownItem();
	}
	
	private void updateShownItem() {
		shownItem.setText(elements[selectedItem]);
		double widthRatio = ( (double)(this.getWidth()) ) / (2 * shownItem.getPreferredSize().getWidth() );
		double heightRatio = ( (double)(this.getHeight()) ) / (shownItem.getPreferredSize().getHeight() );
		double ratio = Math.min(widthRatio, heightRatio);
		int fontSize = (int) (shownItem.getFont().getSize() * ratio);
		if ( fontSize < 12 ) {
			fontSize = 12;
		}
		else if ( fontSize > 24 ) {
			fontSize = 24;
		}
		shownItem.setFont(FileUtils.getFont("Zekton.ttf", fontSize, Font.PLAIN));
		if ( elements[selectedItem] != "No item" ) {
			detailPanel.setOpaque(true);
			Team team = (Team) DynamicCompiler.loadObject(elements[selectedItem]);
			detailPanel.setBackground(team.getTeamInfo().getColor1());
			teamName.setText(team.getTeamInfo().getName());
			teamName.setForeground(team.getTeamInfo().getColor2());
			playerList.setBackground(team.getTeamInfo().getColor1());
			playerList.setForeground(team.getTeamInfo().getColor2());
			playerListModel.clear();
			Player[] players = team.assembleTeam(numberOfShownPlayers, new Pitch().getPitch());
			if ( players != null ) {
				for(int i = 0; i < players.length; i++) {
					if ( players[i] != null ) {
						playerListModel.add(i, players[i].getName() + " - " + players[i].getKitNumber());
					}
				}
			}
		}
	}
	
	public String getSelectedItem() {
		return elements[selectedItem];
	}
	
	@Override
	public void setBounds(int x,int y,int width, int height) {
		super.setBounds(x, y, width, height);
		left.setBounds(0, 0, width/4, height / 5);
		shownItem.setBounds((int)((double)this.getWidth()/4.0), 0, (int)Math.ceil((double)this.getWidth()/2), this.getHeight() / 5);
		right.setBounds(3*width/4, 0, width/4, height / 5);
		detailPanel.setBounds(0, (int)(3 * height / 10), (int)(width), (int)(7 * height / 10));
		teamName.setBounds(0, 0, detailPanel.getWidth(), detailPanel.getHeight() / 5);
		playerList.setBounds(0, detailPanel.getHeight() / 5, detailPanel.getWidth(), 3 * detailPanel.getHeight() / 5);
		updateShownItem();
	}
	
    @Override
    public Dimension getPreferredSize() {
        return myDimension;
    }

    @Override
    public Dimension getMaximumSize() {
        return myDimension;
    }

    @Override
    public Dimension getMinimumSize() {
        return myDimension;
    }

	public void setSelectedItem(int selectedItem) {
		this.selectedItem = selectedItem;
		updateShownItem();
	}

}
