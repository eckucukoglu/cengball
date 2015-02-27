package gui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import utils.Enums;
import utils.FileUtils;

public class SplashPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private float alpha = 0.0f; // a number between 0.0 and 1.0
	private int fade = 1;
	private boolean isCompleted;
	private Image logoImage;
	private int iconWidth , iconHeight;
	private int width, height;
	
	public SplashPanel() {
		setBackground(Enums.black);
		setLayout(null);
		isCompleted = false;

		arrangeLayout();
	}

	public void animate() {
        final int animationTime = (int) (Enums.DEFAULT_SPLASH_TIME / 1000.0);
        int framesPerSecond = 30;
        int delay = (int) (1000.0 / framesPerSecond);
        final float deltaAlpha = (float) (2.0/ (float)( framesPerSecond * animationTime ));
        final long start = System.currentTimeMillis();
        final Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final long now = System.currentTimeMillis();
                final long elapsed = (now - start) / 1000;
                alpha += fade * deltaAlpha;
                if ( alpha >= 1 ) {
                	alpha = 1;
                	fade = -1;
                }
                else if ( alpha < 0.1 && fade == -1 ) {
                	alpha = 0;
                }
                repaint();
                if (elapsed >= animationTime) {
                    timer.stop();
                    isCompleted = true;
                }
            }
        });
        timer.start();
    }
	
	public SplashPanel getInstance() {
		return this;
	}
	
	public boolean isCompleted() {
		return isCompleted;
	}
	
	private void arrangeLayout() { 
		Dimension screenDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) screenDimension.getWidth();
		height = (int) screenDimension.getHeight();
		iconWidth = 496;
		iconHeight = 200;

		logoImage = FileUtils.getImage("campaing_logo_scaled.png");
	}
	
	@Override
	public void paintComponent(final Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D)g;
	    
	    int rule = AlphaComposite.SRC_OVER;
        Composite comp = AlphaComposite.getInstance(rule , alpha );
        g2d.setComposite(comp);
        
        if (logoImage != null) {
	        g.drawImage(logoImage, (width - iconWidth) / 2, (height - iconHeight) / 2, null);
	    }
	}
}
