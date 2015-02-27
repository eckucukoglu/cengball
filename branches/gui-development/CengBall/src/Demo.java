import gui.MainFrame;

import java.awt.EventQueue;
import java.util.ArrayList;

import simulator.PlayerInfo;
import utils.FileUtils;

import common.Ball;
import common.Percept;
import common.Position;
import common.SaveFile;


public class Demo {
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
