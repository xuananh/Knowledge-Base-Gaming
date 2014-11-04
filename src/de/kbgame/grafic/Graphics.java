package de.kbgame.grafic;



import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;

import javax.swing.JFrame;

import de.kbgame.game.Game;

public class Graphics extends JFrame {

	private static final long serialVersionUID = -8990501035231822716L;

	public int Width = 800, Height = 600;
	public Dimension currentWindowSize, realWindowSize;

	Font TimesSmall, TimesSmaller;
	Cursor transparentCursor, basicCursor;
	BufferedImage test;
	
	BufferedImage currentScreen;
	Graphics2D currentGrafic;
	
	public Graphics(Game game) {
		
		TimesSmall = new Font("Times New Roman", Font.PLAIN, 15);
		TimesSmaller = new Font("Times New Roman", Font.PLAIN, 12);
		
		setUndecorated(true);
		setIgnoreRepaint(true);
		GraphicsDevice graphicsDevice = getGraphicsConfiguration().getDevice();
		graphicsDevice.setFullScreenWindow(this);
//		graphicsDevice.setDisplayMode(new DisplayMode(Width, Height, 32, 60));
		
		//this.setAlwaysOnTop(true);
		//this.setLocationByPlatform(true);
		//this.setSize(Width, Height);
		
		realWindowSize = getSize();
		currentWindowSize = new Dimension(800,600);
		createBufferStrategy(2);
		
		this.setTitle("Game Example");
		this.setBackground(Color.white);
		this.setForeground(Color.black);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//BufferedImage img = null;
		//try {
		//	img = ImageIO.read(new File("Icon.jpg"));
		//	this.setIconImage(img); 
		//} catch (IOException e) {
		//	e.printStackTrace();
		//} 
		
		//this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		basicCursor = Cursor.getDefaultCursor();
		
		int[] pixels = new int[16 * 16];
		Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
		transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "invisibleCursor");
		this.setCursor(transparentCursor);
		
		
		//try {
		//	test = ImageIO.read(new File("Grafik/Test.png"));
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}
		
		setVisible(true);
		
		addKeyListener(game.input.KeyInput);
		addMouseListener(game.input.MouseInput);
		addMouseMotionListener(game.input.MouseMove);
		addMouseWheelListener(game.input.MouseWheel);
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
