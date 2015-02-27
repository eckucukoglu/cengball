package utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class Enums {
	public static final int DEFAULT_SCREEN_WIDTH = 1024;
	public static final int DEFAULT_SCREEN_HEIGHT = 768;
	public static final int DEFAULT_BUTTON_WIDTH = 200;
	public static final int DEFAULT_BUTTON_HEIGHT = 60;
	public static final Color white = new Color(255,255,255);
	public static final Color black = new Color(0,0,0);
	public static final Color indianRed = new Color(205,85,85);
	public static final Color brown = new Color(139,35,35);
	public static final Color sgigray = new Color(142,142,142);
	public static final Color transSgigray = new Color(142,142,142, 200);
	public static final Color darkslategray = new Color(47,79,79);
	public static final Color red = new Color(255,0,0);
	public static final Color blue = new Color(0,0,255);
	public static final Color green = new Color(0,255,0);
	public static final Color darkgreen = new Color(0,100,0);
	public static final Color forestgreen = new Color(34, 139, 34);
	
	
	
	public static enum ActionType {
		GOAL, PASS, SHOOT, TACKLE, MOVE
	}
}
