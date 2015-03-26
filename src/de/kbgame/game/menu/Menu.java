package de.kbgame.game.menu;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import de.kbgame.game.Game;
import de.kbgame.grafic.ImageSprite;
import de.kbgame.util.sound.SoundKey;

public abstract class Menu {
	
	/** Haupt Menue */
	protected static final int HAUPT_MENU = 0;
	/** Submenue : About menue */
	protected static final int ABOUT_MENU = 1;
	/** Submenue: hight score Menue */
	protected static final int HIGHT_SCORE = 2;
	/** Score path File: Speichern die 3 besten Score */
	private static final String SCORE_FILE = "Store/score.txt";
	
	/** Background von Menue */
	private BufferedImage background;

	/** Sprite Image von Zeiger der Menue */
	protected final ImageSprite sprite = new ImageSprite("Images/sprite.png", 4, 3);
	/** Menuepunkt ist ausgewaehlt */
	protected int menuPunkt = 1;
	/** variable speichert welche Menue Aktiv ist. ist Hauptmenue oder Submenue */
	protected int menu;
	
	/**
	 * Konstruktor fuer Menue mit loadenden Background-Bild
	 */
	public Menu() {
		try {
			background = ImageIO.read(new File("Images/menu_background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Die Methode aktualisiert Zustand von menue. Hier werden alle 
	 * Key-Aktion abgefangen und gehandelt.
	 * 
	 * @param g Game Objekt mit Graphic und Input Instance
	 */
	protected abstract void updateMenu(Game g);
	/**
	 * Zeichnen Menue mit aktuelle Zustand durch Instance Graphic von Game.
	 * 
	 * @param g g Game Objekt mit Graphic und Input Instance
	 */
	public abstract void draw(Game g);
	
	/**
	 * Jeder Frame wird diese Methode aufgeruft,um die Zustand von Game 
	 * zu aktualisieren. Bei "ESCAPE"-button wird Game beendet.
	 * @param g Game Instance
	 */
	public void update(Game g) {
		if (g.input.getKey(KeyEvent.VK_ENTER)){
			g.sounds.sound(SoundKey.MENU);
		}
		updateMenu(g);
		if (g.input.getKey(KeyEvent.VK_ESCAPE)) 
			g.shouldApplicationExit = true;
		
		sprite.setCurrentRow(2);
		sprite.update();
		g.input.resetKeys();
	}
	
	/**
	 * Zeichnen die Logo "Jump and Run game" mit unterschiedliche Farbe und schriftart.
	 * @param xStart x-koordination von startenden Punkt zu zeichnen.
	 * @param yStart y-koordination von startenden Punkt zu zeichnen.
	 * @param g Game Instance mit Graphic Objekt.
	 * @param scale Schrift ist normal im Groesse 50! Mit diesem parameter wird Logo gemeinsam
	 * 					groesser(>1) oder kleiner(<1) gezeichnet.
	 */
	protected void gameLogo(int xStart, int yStart, Game g, float scale) {
		g.graphic.drawText("JUMP", xStart, yStart, Color.yellow, false, new Font("Comic Sans MS", Font.BOLD, (int) (50*scale)));
		g.graphic.drawText("&", xStart + (int)(150*scale), yStart + (int)(10*scale), new Color(0,191,255), false, new Font("Arial", Font.PLAIN, (int) (80*scale)));
		g.graphic.drawText("RUN", xStart + (int)(220*scale), yStart, Color.orange, false, new Font("Comic Sans MS", Font.BOLD, (int) (50*scale)));
		g.graphic.drawText("GAME", xStart +(int)(350*scale), yStart, new Color(121,205,205), false, new Font("Arial", Font.PLAIN, (int) (40*scale)));
	}
	
	/**
	 * Zeichnen Background fuer Menue
	 * @param g Game Instance 
	 */
	protected void menuBackground(Game g) {
		g.graphic.currentGrafic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
		g.graphic.currentGrafic.drawImage(background, 0, 0, g.graphic.Width, g.graphic.Height, null);
		g.graphic.currentGrafic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
	
	/**
	 * Speichern die neue Score in Score-Datei. die Neue Score wird geprueft und neue geschrieben
	 * @param score neue Score
	 */
	protected void saveHightScore(int score) {
		final List<Integer> scores = getHightScore();
		if(!scores.contains(score)){
			scores.add(score);
		}
		Collections.sort(scores);
		BufferedWriter TWrite;
		try {
			File f = new File(SCORE_FILE);
			if( !f.exists() ){
				f.createNewFile();
			}
			if( !f.canWrite() ) 
				return;
			TWrite = new BufferedWriter(new FileWriter(SCORE_FILE));
			for (int i = scores.size() - 1; i > scores.size() - 4; i--) {
				TWrite.write(scores.get(i)+"\r\n");
			}
			TWrite.close(); // Important!!
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * get die 3 besten gespeicherte Score von Datei.
	 * @return 3 besten Score
	 */
	protected List<Integer> getHightScore() {
		BufferedReader bfr = null;
		final List<Integer> score = new ArrayList<Integer>();
		int i = 0;
		try {
			FileReader fstream = new FileReader(SCORE_FILE);
			bfr = new BufferedReader(fstream);
			String line;
			while ( (line = bfr.readLine()) != null){
				if(i <= 3) {
					score.add(Integer.parseInt(line));
					i++;
				}
			}
		} catch(Exception e){ 
			e.printStackTrace();
		} finally{
			try{ 
				bfr.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return score;
	}
}
