package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import utils.Enums;
import utils.FileUtils;

public class Selector extends JComponent {
	private ArrayList<String> elements;
	private int selectedItem;
	private JLabel shownItem;
	private IconButton left, right;
	private Color backgroundColor;
	private Dimension myDimension = new Dimension(300, 100);
	
	Selector(ArrayList<String> items) {
		if ( items == null ) {
			items = new ArrayList<String>();
			items.add("No Item");
		}
		this.elements = items;
		this.selectedItem = 0;
		this.backgroundColor = Enums.darkslategray;
		
		left = new IconButton("arrow_left.png", "arrow_left_hovered.png", "arrow_left_pressed.png");
		left.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( selectedItem == 0 ) {
					selectedItem = elements.size() - 1;
				}
				else {
					selectedItem--;
				}
				updateShownItem();
			}
		});
		left.setBackground(backgroundColor);
		left.setBounds(0, 0, (int)(myDimension.getWidth()/4), (int)(myDimension.getHeight()));
		this.add(left);
		
		shownItem = new JLabel("		");
		shownItem.setFont(FileUtils.getFont("Zekton.ttf", 24, Font.PLAIN));
		shownItem.setForeground(Enums.white);
		shownItem.setBackground(backgroundColor);
		shownItem.setOpaque(true);
		shownItem.setHorizontalAlignment(SwingConstants.CENTER);
		shownItem.setBounds((int)(myDimension.getWidth()/4.0), 0, (int)(myDimension.getWidth()/2), (int)(myDimension.getHeight()));
		this.add(shownItem);
		
		right = new IconButton("arrow_right.png", "arrow_right_hovered.png", "arrow_right_pressed.png");
		right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( selectedItem == elements.size() - 1 ) {
					selectedItem = 0;
				}
				else {
					selectedItem++;
				}
				updateShownItem();
			}
		});
		right.setBackground(backgroundColor);
		right.setBounds((int)(3*myDimension.getWidth()/4), 0, (int)(myDimension.getWidth()/4), (int)(myDimension.getHeight()));
		this.add(right);
		
		this.setSize(myDimension);
		updateShownItem();
	}
	
	private void updateShownItem() {
		shownItem.setText(elements.get(selectedItem));
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
	}
	
	public String getSelectedItem() {
		return elements.get(selectedItem);
	}
	
	@Override
	public void setBounds(int x,int y,int width, int height) {
		super.setBounds(x, y, width, height);
		left.setBounds(0, 0, width/4, height);
		shownItem.setBounds((int)((double)this.getWidth()/4.0), 0, (int)Math.ceil((double)this.getWidth()/2), this.getHeight());
		right.setBounds(3*width/4, 0, width/4, height);
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
}
