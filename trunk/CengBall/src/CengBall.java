import gui.MainFrame;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.CBLogger;
import utils.FileUtils;


public class CengBall {
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();
	
	public static void main(String[] args) {
		try {
			FileUtils.createMainDirectory();
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					try {
						MainFrame frame = new MainFrame();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			LOGGER.log(Level.SEVERE, "GUI cannot be created");
		}
	}
}
