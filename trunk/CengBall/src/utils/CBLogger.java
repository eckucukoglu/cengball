package utils;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/*
 * To use this log class;
 * In class definition:
 * private static final Logger LOGGER = CBLogger.getInstance().getLogger();
 * In a segment wanted to log:
 * LOGGER.log(Level.INFO, "info message");
 * Level types:
 * SEVERE (highest value), WARNING, INFO, CONFIG, FINE, FINER, FINEST (lowest value) 
*/
public class CBLogger {
	private static Logger cbLogger = Logger.getLogger("CBLogger");
	private static FileHandler fileHandler;
	private static SimpleFormatter txtFormatter;
	private static CBLogger instance = null;  
	
	public static CBLogger getInstance() {  
	      if ( instance == null ) {  
	    	  try {
	    		  setup();
	    	  } catch (IOException e) {
	    		  System.out.println("Cannot create logger");
	    	  }  
	    	  instance = new CBLogger();  
	      }  
	      return instance;  
	   }  
	
	public static void setup() throws IOException {
		UserSettings.loadSettings();
		
	    // suppress the logging output to the console
	    Logger rootLogger = Logger.getLogger("");
	    Handler[] handlers = rootLogger.getHandlers();
	    if (handlers[0] instanceof ConsoleHandler) {
	    	rootLogger.removeHandler(handlers[0]);
	    }
	    if ( UserSettings.isDebugModeOn ) {
	    	cbLogger.setLevel(Level.ALL);
	    }
	    else {
	    	cbLogger.setLevel(Level.OFF);
	    }
	    fileHandler = new FileHandler(FileUtils.getLogsDirectory() + "/" + FileUtils.generateFileName("txt"));
	    // create a TXT formatter
	    txtFormatter = new SimpleFormatter();
	    fileHandler.setFormatter(txtFormatter);

	    cbLogger.addHandler(fileHandler);
	}
	
	public Logger getLogger() {
		return cbLogger;
	}
}
