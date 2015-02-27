package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.color.ColorSpace;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();
	
	public static Dimension getScreenDimension() {
		return java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	}

	public static double getScreenWidthRatio(int width) {
		return ((double)width / (double)Enums.DEFAULT_SCREEN_WIDTH );
	}
	
	public static double getScreenHeightRatio(int height) {
		return ((double)height / (double)Enums.DEFAULT_SCREEN_HEIGHT );
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	
	public static int minutesToFrames(int minutes, int fps) {
		return minutes * 60 * fps;
	}
	
	public static float[] rgbToLab(Color rgbColor) {
		ColorSpace CIEXYZ = ColorSpace.getInstance(ColorSpace.CS_CIEXYZ);
		float[] rgba = new float[3];
		rgbColor.getRGBColorComponents(rgba);
		float[] xyz = CIEXYZ.fromRGB(rgba);
		double l = f(xyz[1]);
        double L = 116.0 * l - 16.0;
        double a = 500.0 * (f(xyz[0]) - l);
        double b = 200.0 * (l - f(xyz[2]));
        return new float[] {(float) L, (float) a, (float) b};
	}
	
	private static double f(double x) {
        if (x > 216.0 / 24389.0) {
            return Math.cbrt(x);
        } else {
            return (841.0 / 108.0) * x + 4.0 / 29.0;
        }
    }
	
	public static float distance3D(float[] point1, float[] point2) {
		if ( point1.length >= 3 && point2.length >= 3 ) {
			double term1 = Math.pow(point1[0] - point2[0], 2);
			double term2 = Math.pow(point1[1] - point2[1], 2);
			double term3 = Math.pow(point1[2] - point2[2], 2);
			return (float) Math.sqrt( term1 + term2 + term3 );
		}
		return 0;
	}
	
	public static boolean isSimilar(Color color1, Color color2) {
		try {
			if ( Utils.distance3D( Utils.rgbToLab(color1), Utils.rgbToLab(color2)) < Enums.NOTICABLE_COLOR_DISTANCE )  {
				return true;
			}
		} catch ( Exception e ) {
			LOGGER.log(Level.WARNING, "Cannot check team colors");
		}
		return false;
	}
	
	public static void sortDates(ArrayList<String> dates) {
		Collections.sort(dates, new Comparator<String>(){
			 
            @Override
            public int compare(String o1, String o2) {
            	Date date1 = FileUtils.getSaveFileDate(FileUtils.removeExtension(o1) );
            	Date date2 = FileUtils.getSaveFileDate(FileUtils.removeExtension(o2) );
                return date1.compareTo(date2);
            }
        });
	}
}
