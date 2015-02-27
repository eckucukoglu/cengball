package utils;

public class Utils {

	public static double getScreenWidthRatio(int width) {
		return ((double)width / (double)Enums.DEFAULT_SCREEN_WIDTH );
	}
	
	public static double getScreenHeightRatio(int height) {
		return ((double)height / (double)Enums.DEFAULT_SCREEN_HEIGHT );
	}
	
	public static void log(String str) {
		System.out.println(str);
	}
	
}
