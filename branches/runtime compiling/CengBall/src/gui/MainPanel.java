package gui;

import javax.swing.JPanel;
import java.awt.CardLayout;

public class MainPanel extends JPanel {
	private MenuPanel menuPanel;
	private MatchPanel matchPanel;
	private CardLayout mainCardLayout;

	/**
	 * Create the panel.
	 */
	public MainPanel() {
		super();
		mainCardLayout = new CardLayout(0, 0);
		setLayout(mainCardLayout);
		menuPanel = new MenuPanel();
		matchPanel = new MatchPanel(120, 60);
		this.add(menuPanel, "Menu");
		this.add(matchPanel, "Match");
		show("Menu");
	}
	
	public void show(String name) {
		mainCardLayout.show(this, name);
	}

}
