package gui;

import java.awt.CardLayout;

import javax.swing.JPanel;

import utils.Utils;

public class MainPanel extends JPanel {
	private MenuPanel menuPanel;
	private KickOffPanel kickOffPanel;
	private MatchPanel matchPanel;
	private SplashPanel splashPanel;
	private CardLayout mainCardLayout;
	
	private Resources resources;

	public MainPanel() {
		super();
		mainCardLayout = new CardLayout(0, 0);
		setLayout(mainCardLayout);
		splashPanel = new SplashPanel();
		menuPanel = new MenuPanel();
		kickOffPanel = new KickOffPanel();
		matchPanel = new MatchPanel(120, 60);
		this.add(menuPanel, "Menu");
		this.add(matchPanel, "Match");
		this.add(kickOffPanel, "KickOff");
		this.add(splashPanel, "Splash");
		show("Splash");
		splashPanel.animate();
		resources = new Resources(this);
		resources.start();
	}
	
	public void show(String name) {
		mainCardLayout.show(this, name);
	}

	public Resources getResources() {
		return resources;
	}
	
	public void onResourcesLoaded() {
		menuPanel.setBackground(resources.getImage(0));
		kickOffPanel.setBackground(resources.getImage(1));
		if ( splashPanel.isAnimationCompleted() ) {
			show("Menu");
		}
	}
	
	public void onAnimationCompleted() {
		if ( resources.isResourcesLoaded() ) {
			show("Menu");
		}
	}
}
