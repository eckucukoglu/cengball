package gui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import utils.Enums;
import utils.FileUtils;

public class IconButton extends JButton {
	private static final long serialVersionUID = 1L;
	private ImageIcon defaultIcon, hoveredIcon, pressedIcon;
	
	IconButton(String iconDefault, String iconHovered, String iconPressed) {
		this.defaultIcon = FileUtils.getIcon(iconDefault);
		this.hoveredIcon = FileUtils.getIcon(iconHovered);
		this.pressedIcon = FileUtils.getIcon(iconPressed);
		this.setIcon(defaultIcon);
		this.setRolloverIcon(hoveredIcon);
		this.setPressedIcon(pressedIcon);
		this.setSize(Enums.DEFAULT_BUTTON_WIDTH, Enums.DEFAULT_BUTTON_HEIGHT);
		this.setBackground(null);
		this.setBorder(null);
		this.setOpaque(true);
		this.setBorderPainted(false);
		this.setFocusable(false);
	}
	
	@Override
	public void setBounds(int x,int y,int width, int height) {
		if ( defaultIcon != null ) {
			defaultIcon = width > 0 && height > 0 ? new ImageIcon( defaultIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)) : defaultIcon;
		}
		if ( hoveredIcon != null ) {
			hoveredIcon = width > 0 && height > 0 ? new ImageIcon( hoveredIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)) : hoveredIcon;
		}
		if ( pressedIcon != null ) {
			pressedIcon = width > 0 && height > 0 ? new ImageIcon( pressedIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)) : pressedIcon;
		}
		this.setIcon(defaultIcon);
		this.setRolloverIcon(hoveredIcon);
		this.setPressedIcon(pressedIcon);
		super.setBounds(x, y, width, height);
	}

}
