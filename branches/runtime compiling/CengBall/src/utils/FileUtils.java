package utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class to simplify common operations on files
 */
public class FileUtils {
	
	/**
	 * Returns a string containing current date and time with supplied extension
	 * 
	 * @param fileExtension extension of the output file
	 * @return name string with a given extension
	 */
	public static String generateFileName(String fileExtension) {
		String formatString = "";
		
		try {
			formatString = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return formatString + "." + fileExtension;
	}
	
	/**
	 * Traverses a directory and returns a list of files with
	 * given extension
	 * 
	 * @param dir directory to traverse
	 * @param extension files will be filtered with this extension
	 * @return list of files in given directory with given extension
	 */
	public static File [] getFilteredFileList(File dir, final String extension) {
		File []fileList = null;
		
		try {
			fileList = dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith("." + extension);
				}
			});
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		return fileList;
	}
	
	public static final Font getFont(String fontName, int fontSize, int style) {
		Font font = new Font("GentiumAlt", Font.BOLD, 32);
		try {
			InputStream in = Enums.class.getClassLoader().getResourceAsStream("fonts/" + fontName);
			font = Font.createFont(Font.TRUETYPE_FONT, in);
			font = font.deriveFont(style,fontSize);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(font);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return font;
	}

}
