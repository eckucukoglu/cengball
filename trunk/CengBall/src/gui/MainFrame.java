package gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import utils.CBLogger;
import utils.Enums;
import utils.FileUtils;

public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private MainPanel mainPanel;
	private SplashPanel splashPanel;
	private Image[] images;
	private Timer timer;
	private final int timerDelay = 200;
	private static final Logger LOGGER = CBLogger.getInstance().getLogger();
	
	private SwingWorker<Image[], Void> worker = new SwingWorker<Image[], Void>() {
        @Override
        public Image[] doInBackground() {
            final Image[] innerImgs = new Image[7];
            innerImgs[0] = FileUtils.getImage("menuBackground.jpg");
            innerImgs[1] = FileUtils.getImage("kickOffBackground.jpg");
            innerImgs[2] = FileUtils.getImage("importBackground.jpg");
            innerImgs[3] = FileUtils.getImage("simulationBackground.jpg");
            innerImgs[4] = FileUtils.getImage("watchBackground.jpg");
            innerImgs[5] = FileUtils.getImage("settingsBackground.jpg");
            innerImgs[6] = FileUtils.getImage("matchStatsBackground.jpg");
            return innerImgs;
        }
 
        @Override
        public void done() {
            try {
            	images = get();
            	mainPanel.setBackgrounds(images);
            } 
            catch (InterruptedException ignore) {
            	LOGGER.log(Level.WARNING, "InterruptedException : " + ignore);
            }
            catch (java.util.concurrent.ExecutionException e) {
                String why = null;
                Throwable cause = e.getCause();
                if (cause != null) {
                    why = cause.getMessage();
                } else {
                    why = e.getMessage();
                }
                LOGGER.log(Level.WARNING, "Cannot get file : " + why);
            }
        }
    };
	
	
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setUndecorated(true);  
		Dimension min = new Dimension(1000,600);
		this.setMinimumSize(min);
		
		mainPanel = new MainPanel();
		splashPanel = new SplashPanel();
		
		setContentPane(splashPanel);
		this.pack();
		
		timer = new Timer(timerDelay, this);
		timer.setInitialDelay(Enums.DEFAULT_SPLASH_TIME);
		timer.start();
		
		worker.execute();
		splashPanel.animate();
	}

	public MainFrame(String name) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setUndecorated(true);  
		this.setTitle(name);
		Dimension min = new Dimension(1000,600);
		this.setMinimumSize(min);
		
		mainPanel = new MainPanel();
		splashPanel = new SplashPanel();
		
		setContentPane(splashPanel);
		this.pack();
		
		timer = new Timer(timerDelay, this);
		timer.setInitialDelay(Enums.DEFAULT_SPLASH_TIME);
		timer.start();
		
		worker.execute();
		splashPanel.animate();
	}
	
	public MainFrame getInstance() {
		return this;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ( !worker.isDone() || !splashPanel.isCompleted() ) {
			return;
		}
		this.setContentPane(mainPanel);
		this.revalidate();
		FileUtils.setLogger(LOGGER);
		timer.stop();
	}
}
