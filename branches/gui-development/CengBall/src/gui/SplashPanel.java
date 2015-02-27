package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import utils.Enums;
import utils.FileUtils;

public class SplashPanel extends JPanel {
	
	private float alpha = 0.0f; // a number between 0.0 and 1.0
	private int fade = 1;
	private boolean isCompleted;
	
	public SplashPanel() {
		setBackground(Enums.black);
		setLayout(null);
		isCompleted = false;
	}

	public void animate() {
        final int animationTime = 3;
        int framesPerSecond = 30;
        int delay = 1000 / framesPerSecond;
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
                	fade = -1;
                }
                else if ( alpha < 0.08 && fade == -1 ) {
                	alpha = 0;
                }
                repaint();
                if (elapsed >= animationTime) {
                    timer.stop();
                    isCompleted = true;
                    ( (MainPanel) getInstance().getParent() ).onAnimationCompleted();
                }
            }
        });
        timer.start();
    }
	
	public SplashPanel getInstance() {
		return this;
	}
	
	public boolean isAnimationCompleted() {
		return isCompleted;
	}
	
	@Override
	public void paintComponent(final Graphics g) {
	    super.paintComponent(g);
	    
	    // TODO Temp splash screen
	    g.setColor(new Color(65,105,225, (int)(255 * alpha)));
	    g.setFont(FileUtils.getFont("Mechanic.ttf", 48, Font.BOLD));
	    g.drawString("Project Contorium", this.getWidth()/2 - 300, this.getHeight()/2 - 20);
	}
}
