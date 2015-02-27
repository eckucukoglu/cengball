package utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * This class is responsible for saving and loading user settings.
 *
 */
public class UserSettings {
	public static boolean isDebugModeOn = false;
	public static String jdkPath = "";
	private static Preferences prefs;
	
	private static String debugModeId = "DebugMode";
	private static String jdkPathId = "jdkPath";
	
	public static void loadSettings() {
		prefs = Preferences.userRoot().node(UserSettings.class.getName());
		isDebugModeOn = prefs.getBoolean(debugModeId, false);
		jdkPath = prefs.get(jdkPathId, "");
	}
	
	public static void setDebugMode(boolean debugModeOn) {
		if ( prefs == null ) {
			prefs = Preferences.userRoot().node(UserSettings.class.getName());
		}
		isDebugModeOn = debugModeOn;
		prefs.putBoolean(debugModeId, debugModeOn);
	}
	
	public static void setJDKPath(String JDKPath) {
		if ( prefs == null ) {
			prefs = Preferences.userRoot().node(UserSettings.class.getName());
		}
		jdkPath = JDKPath;
		prefs.put(jdkPathId, JDKPath);
	}
	
	public static void clearSettings() {
		try {
			if ( prefs != null ) {
				prefs.clear();
			}
		} catch (BackingStoreException e) {
			Logger LOGGER = CBLogger.getInstance().getLogger();
			LOGGER.log(Level.WARNING, "BackingStoreException : " + e);
		}
	}
}
