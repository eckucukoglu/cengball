package gui;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import utils.Enums;
import utils.FileUtils;


public class DefaultButton extends JButton implements MouseListener {

	DefaultButton() {
		this.setText("DefaultButton");
		this.setSize(Enums.DEFAULT_BUTTON_WIDTH, Enums.DEFAULT_BUTTON_HEIGHT);
		this.setBackground(null);
		this.setBorder(null);
		this.setFont(FileUtils.getFont("Zeroes.ttf", 32, Font.BOLD));
		this.setForeground(Enums.white);
		this.addMouseListener(this);
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setFocusable(false);
	}
	
	DefaultButton(String name) {
		this.setText(name);
		this.setSize(Enums.DEFAULT_BUTTON_WIDTH, Enums.DEFAULT_BUTTON_HEIGHT);
		this.setBackground(null);
		this.setBorder(null);
		this.setFont(FileUtils.getFont("Zeroes.ttf", 32, Font.BOLD));
		this.setForeground(Enums.white);
		this.addMouseListener(this);
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setFocusable(false);
	}
	
	DefaultButton(String name, int width, int height) {
		this.setText(name);
		this.setSize(width, height);
		this.setBackground(null);
		this.setBorder(null);
		this.setFont(FileUtils.getFont("Zeroes.ttf", 32, Font.BOLD));
		this.setForeground(Enums.white);
		this.addMouseListener(this);
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setFocusable(false);
	}
	
	public void centralize(int x, int y) {
		this.setBounds(x - this.getWidth()/2, y - this.getHeight()/2, this.getWidth(), this.getHeight());
	}
	
	public void setPosition(int x, int y) {
		this.setBounds(x, y, Enums.DEFAULT_BUTTON_WIDTH, Enums.DEFAULT_BUTTON_HEIGHT);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.setForeground(Enums.indianRed);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.setForeground(Enums.black);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.setForeground(Enums.black);
		this.setBackground(Enums.sgigray);
		this.setOpaque(true);
		this.setContentAreaFilled(true);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.setForeground(Enums.white);
		this.setBackground(null);
		this.setOpaque(false);
		this.setContentAreaFilled(false);
	}
}
