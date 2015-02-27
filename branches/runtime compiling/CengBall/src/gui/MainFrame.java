package gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new MainPanel();
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setUndecorated(true);  
		Dimension min = new Dimension(1000,600);
		this.setMinimumSize(min);
		setContentPane(contentPane);
	}

	public MainFrame(String name) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new MainPanel();
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setUndecorated(true);  
		this.setTitle(name);
		Dimension min = new Dimension(1000,600);
		this.setMinimumSize(min);
		setContentPane(contentPane);
	}
}
