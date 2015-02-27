package utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Utility class to simplify common operations on files
 */
public class FileUtils {
	private static Logger LOGGER;
	
	public static void setLogger(Logger logger) {
		FileUtils.LOGGER = logger;
	}
	
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
			LOGGER.log(Level.WARNING, "Exception : " + e);
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.WARNING, "IllegalArgumentException : " + e);
		}

		return formatString + "." + fileExtension;
	}
	
	public static Date getSaveFileDate(String saveFileName) {
		SimpleDateFormat dateFormat;
		Date saveFileDate = null;
		
		try {
			dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
			saveFileDate = dateFormat.parse(saveFileName);
		} catch (NullPointerException e) {
			LOGGER.log(Level.WARNING, "NullPointerException : " + e);
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.WARNING, "IllegalArgumentException : " + e);
		} catch (ParseException e) {
			LOGGER.log(Level.WARNING, "ParseException : " + e);
		}
		
		return saveFileDate;
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
			LOGGER.log(Level.WARNING, "SecurityException : " + e);
		}
		
		return fileList;
	}
	
	/**
	 * Reads an entire file and returns a String object
	 * corresponding to that file
	 * 
	 * @param path path of the file to be read
	 * @param encoding file encoding
	 * @return string object representing file contents
	 * 
	 * @throws IOException
	 */
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));

		return new String(encoded, encoding);
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
			LOGGER.log(Level.WARNING, "MalformedURLException : " + e);
		} catch (FontFormatException e) {
			LOGGER.log(Level.WARNING, "FontFormatException : " + e);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "IOException : " + e);
		}
		return font;
	}
	
	/**
	 * Gets the image from the resource file.
	 * @param imageName	the name of the image.
	 * @return	image.
	 */
	public static Image getImage(String imageName) {
		Image img = null;
		try {
			InputStream in = ClassLoader.getSystemResourceAsStream("drawables/" + imageName);
			img = ImageIO.read(in);
			in.close();
		} catch (MalformedURLException e) {
			LOGGER.log(Level.WARNING, "MalformedURLException : " + e);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "IOException : " + e);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Exception : " + e);
		}
		return img;
	}
	
	public static ImageIcon getIcon(String iconName) {
		ImageIcon img = null;
		try {
			URL url = ClassLoader.getSystemResource("drawables/" + iconName);
			img = new ImageIcon(url);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Exception : " + e);
		}
		return img;
	}

	public static String createMainDirectory() {
		String directoryPath = System.getProperty("user.home")  +  "/CEngBall";
    	File mainDirectory = new File(directoryPath);
    	if ( !mainDirectory.exists() ) {
    		mainDirectory.mkdir();
    	}
    	return directoryPath;
	}
	
	public static String getSaveFileDirectory() {
		createMainDirectory();
		String directoryPath = System.getProperty("user.home")  +  "/CEngBall/savefiles";
    	File saveFileDirectory = new File(directoryPath);
    	if ( !saveFileDirectory.exists() ) {
    		saveFileDirectory.mkdir();
    	}
    	return directoryPath;
	}
	
	public static String getImportsDirectory() {
		createMainDirectory();
		String directoryPath = System.getProperty("user.home")  +  "/CEngBall/imports";
    	File importsDirectory = new File(directoryPath);
    	if ( !importsDirectory.exists() ) {
    		importsDirectory.mkdir();
    	}
    	return directoryPath;
	}
	
	public static String getLogsDirectory() {
		createMainDirectory();
		String directoryPath = System.getProperty("user.home")  +  "/CEngBall/logs";
    	File importsDirectory = new File(directoryPath);
    	if ( !importsDirectory.exists() ) {
    		importsDirectory.mkdir();
    	}
    	return directoryPath;
	}
	
	public static final String getSaveFilePath() {
		return FileUtils.getSaveFileDirectory() + "/" +  FileUtils.generateFileName("json");
	}
	
	public static final String removeExtension(String name) {
		if (name.indexOf(".") > 0)
		    return name.substring(0, name.lastIndexOf("."));
		return name;
	}	
	
	public static String[] getSaveFiles() {
		String saveFileDir = FileUtils.getSaveFileDirectory();
		File saveFileDirectory = new File(saveFileDir);
		File[] listOfFiles = getFilteredFileList(saveFileDirectory, "json");
		String[] saveFiles = null;
		if ( listOfFiles != null && listOfFiles.length != 0 ) {
			saveFiles = new String[listOfFiles.length];
			for(int i = 0; i < listOfFiles.length; i++) {
				saveFiles[i] = listOfFiles[i].getName();
			}
		}
		return saveFiles;
	}
	
	public static String[] getImports() {
		String importDir = FileUtils.getImportsDirectory();
		File importDirectory = new File(importDir);
		File[] listOfFiles = getFilteredFileList(importDirectory, "class");
		String[] imports = null;
		if ( listOfFiles != null && listOfFiles.length != 0 ) {
			imports = new String[listOfFiles.length];
			for(int i = 0; i < listOfFiles.length; i++) {
				imports[i] = removeExtension(listOfFiles[i].getName());
			}
		}
		return imports;
	}
}
