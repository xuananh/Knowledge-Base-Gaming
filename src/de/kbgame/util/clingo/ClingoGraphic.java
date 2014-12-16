package de.kbgame.util.clingo;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;

import javax.swing.JFrame;

public class ClingoGraphic extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public int Width = 800, Height = 600;
	public Dimension currentWindowSize, realWindowSize;

	Cursor transparentCursor, basicCursor;
	BufferedImage test;
	
	BufferedImage currentScreen;
	Graphics2D currentGrafic;

	public ClingoGraphic() {
		setUndecorated(true);
		setIgnoreRepaint(true);
		GraphicsDevice graphicsDevice = getGraphicsConfiguration().getDevice();
		graphicsDevice.setFullScreenWindow(this);
		
		realWindowSize = getSize();
		currentWindowSize = new Dimension(800,600);
		createBufferStrategy(2);
		
		this.setTitle("Game Example");
		this.setBackground(Color.white);
		this.setForeground(Color.black);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		basicCursor = Cursor.getDefaultCursor();
		
		int[] pixels = new int[16 * 16];
		Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
		transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "invisibleCursor");
		this.setCursor(transparentCursor);
		
		setVisible(true);
		

	}
	
	public void startDrawNewFrame(){
		currentScreen = new BufferedImage(currentWindowSize.width, currentWindowSize.height, BufferedImage.TYPE_INT_RGB);
		if (currentGrafic != null) currentGrafic.dispose();
		currentGrafic = currentScreen.createGraphics();
	}
	
	public void endDrawNewFrame(){
		getBufferStrategy().getDrawGraphics().drawImage(currentScreen, 0, 0, realWindowSize.width, realWindowSize.height, 0, 0, currentWindowSize.width, currentWindowSize.height, this);
		getBufferStrategy().show();
	}
	
	public void newBackground() {
		currentGrafic.setColor(Color.black);
		currentGrafic.fillRect(0, 0, currentWindowSize.width, currentWindowSize.height);
	  }
	
	public void drawRectangle(int x, int y, int w, int h, Color c){
		currentGrafic.setColor(c);
		currentGrafic.fillRect(x, y, w, h);
	}
}
