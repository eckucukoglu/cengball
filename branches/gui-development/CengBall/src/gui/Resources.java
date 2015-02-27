package gui;

import java.awt.Image;
import java.util.ArrayList;

import utils.FileUtils;
import utils.Utils;

public class Resources extends Thread {
	private MainPanel parent;
	private ArrayList<Image> images;
	private boolean isLoaded;
	
	Resources(MainPanel parentPanel) {
		parent = parentPanel;
		isLoaded = false;
	}
	
	@Override
	public void run() {
		try {
			images = new ArrayList<Image>();
			images.add( FileUtils.getImage("menuBackground.jpg") ) ;
			images.add( FileUtils.getImage("kickOffBackground.jpg") ) ;
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			isLoaded = true;
			parent.onResourcesLoaded();
		}
	}
	
	public Image getImage(int i) {
		return images.get(i);
	}
	
	public boolean isResourcesLoaded() {
		return isLoaded;
	}
}
