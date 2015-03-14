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
import de.kbgame.geometry.ImageKey;

public class Graphics extends JFrame {

	private static final long serialVersionUID = -8990501035231822716L;
	
	private static final String GAME_TITLE = "Jump & Run";
	private static final String TIMES_NEW_ROMAN = "Times New Roman";
	private double rotation = 0;
	
	public int viewX = 0, viewY = 0;
	public final int Width = 800, Height = 600;
	public final Dimension currentWindowSize, realWindowSize;
	public final Font TimesSmall, TimesSmaller;
	public final Cursor transparentCursor, basicCursor;
	
	public final ImageLoader imageLoader = ImageLoader.getInstance();
	private BufferedImage currentScreen;
	public Graphics2D currentGrafic;
	
	public Graphics(Game game) {

		TimesSmall = new Font(TIMES_NEW_ROMAN, Font.PLAIN, 15);
		TimesSmaller = new Font(TIMES_NEW_ROMAN, Font.PLAIN, 12);
		
		setUndecorated(true);
		setIgnoreRepaint(true);
		GraphicsDevice graphicsDevice = getGraphicsConfiguration().getDevice();
		graphicsDevice.setFullScreenWindow(this);
		
		realWindowSize = getSize();
		currentWindowSize = new Dimension(800,600);
		createBufferStrategy(2);
		
		this.setTitle(GAME_TITLE);
		this.setBackground(Color.white);
		this.setForeground(Color.black);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		basicCursor = Cursor.getDefaultCursor();
		
		int[] pixels = new int[16 * 16];
		Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
		transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "invisibleCursor");
		this.setCursor(transparentCursor);
		
		setVisible(true);
		
		addKeyListener(game.input.KeyInput);
		addMouseListener(game.input.MouseInput);
		addMouseMotionListener(game.input.MouseMove);
		addMouseWheelListener(game.input.MouseWheel);
	}
	
	public void startDrawNewFrame(int viewX, int viewY) {
		this.viewX = viewX;
		this.viewY = viewY;
		currentScreen = new BufferedImage(currentWindowSize.width, currentWindowSize.height, BufferedImage.TYPE_INT_RGB);
		if (currentGrafic != null) 
			currentGrafic.dispose();
		currentGrafic = currentScreen.createGraphics();
	}
	
	public void endDrawNewFrame(){
		getBufferStrategy().getDrawGraphics().drawImage(currentScreen, 0, 0, realWindowSize.width, realWindowSize.height, 0, 0, currentWindowSize.width, currentWindowSize.height, this);
		getBufferStrategy().show();
	}
	
	public void fillBlack() {
		currentGrafic.setColor(Color.black);
		currentGrafic.fillRect(0, 0, currentWindowSize.width, currentWindowSize.height);
	}
	
	public void newBackground() {
		currentGrafic.drawImage(imageLoader.getImageByKey(ImageKey.BACKGROUND), 0, 0, currentWindowSize.width, currentWindowSize.height, this);
	}

	public void drawImage(ImageKey imageKey, int x, int y, int width, int height, float rot){
		drawImage(imageLoader.getImageByKey(imageKey), x, y, width, height, rot, true);
	}
	
	public void drawImage(BufferedImage image, int x, int y, int width, int height, float rot, boolean relative){
		if (relative){
			x -= viewX - this.Width/2;
			y -= viewY - this.Height/2;
		}
		if (x+width < 0 || x-width > this.Width || y+height < 0 || y-height > this.Width) 
			return; //Draw only if in screen
		
		if (rot != 0) 
			currentGrafic.rotate(rot, currentWindowSize.width/2, currentWindowSize.height/2);
		
		try{
			x -= width/2;
			y -= height/2;
			currentGrafic.drawImage(image, x, y, width, height, this);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if (rot != 0) 
			currentGrafic.rotate(-rot, currentWindowSize.width/2, currentWindowSize.height/2);//Nicht Vergessen!!
	}
	
	public void drawRectangle(int x, int y, int w, int h, Color c){drawRectangle(x, y, w, h, c, true);}
	public void drawRectangle(int x, int y, int w, int h, Color c, boolean relative){
		if (relative){
			x -= viewX - this.Width/2;
			y -= viewY - this.Height/2;
		}
		
		if (x+w < 0 || x-w > this.Width || y+h < 0 || y-h > this.Width) return; //Draw only if in screen
		currentGrafic.setColor(c);
		currentGrafic.fillRect(x, y, w, h);
	}

	public void drawRectangleBorder(int x, int y, int w, int h, Color c){drawRectangleBorder(x, y, w, h, c, true);}
	public void drawRectangleBorder(int x, int y, int w, int h, Color c, boolean relative){
		if (relative){
			x -= viewX - this.Width/2;
			y -= viewY - this.Height/2;
		}
		
		if (x+w < 0 || x-w > this.Width || y+h < 0 || y-h > this.Width) return; //Draw only if in screen
		currentGrafic.setColor(c);
		currentGrafic.drawRect(x, y, w, h);
	}

	public void drawOval(int x, int y, int w, int h, Color c) {drawOval(x, y, w, h, c, true);}
	public void drawOval(int x, int y, int w, int h, Color c, boolean relative) {
		if (relative){
			x -= viewX - this.Width/2;
			y -= viewY - this.Height/2;
		}
		
		if (x+w < 0 || x-w > this.Width || y+h < 0 || y-h > this.Width) return; //Draw only if in screen
		currentGrafic.setColor(c);
		currentGrafic.fillOval(x, y, w, h);
	}

	public void drawText(String st, int x, int y, Color c) {drawText(st, x, y, c, true);}
	public void drawText(String st, int x, int y, Color c, Font font) {
		drawText(st, x, y, c, true,font);
	}
	public void drawText(String st, int x, int y, Color c, boolean relative) {
		drawText(st, x, y, c, relative,TimesSmall);
	}
	public void drawText(String st, int x, int y, Color c, boolean relative, Font font) {
		if (relative){
			x -= viewX - this.Width/2;
			y -= viewY - this.Height/2;
		}
		
		//if (x+w < 0 || x-w > this.Width || y+h < 0 || y-h < this.Width) return; //Draw only if in screen
		currentGrafic.rotate(-rotation, x, y);
		currentGrafic.setColor(c);
		currentGrafic.setFont(font);
		currentGrafic.drawString(st, x, y);
		currentGrafic.rotate(rotation, x, y);
	}
	
	public void drawLine(int x, int y, int x2, int y2, Color c) {drawLine(x, y, x2, y2, c, true);}
	public void drawLine(int x, int y, int x2, int y2, Color c, boolean relative) {
		if (relative){
			x -= viewX - this.Width/2;
			y -= viewY - this.Height/2;
			x2 -= viewX - this.Width/2;
			y2 -= viewY - this.Height/2;
		}
		
		if (Math.max(x, x2) < 0 || Math.min(x, x2) > this.Width || Math.max(y, y2) < 0 || Math.min(y,y2) > this.Width) return; //Draw only if in screen
		currentGrafic.setColor(c);
		currentGrafic.drawLine(x, y, x2, y2);		
	}
}