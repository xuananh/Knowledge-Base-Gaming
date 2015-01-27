package de.kbgame.game;

import java.util.LinkedList;

import de.kbgame.geometry.ImageKey;
import de.kbgame.grafic.Background;
import de.kbgame.grafic.Graphics;
import de.kbgame.map.Level;
import de.kbgame.map.MapLoader;
import de.kbgame.util.Input;
import de.kbgame.util.sound.SoundThread;

public class Game extends Thread{
	
	public final Graphics graphic;
	public final Input input;
	public final Controller controller;
	public final SoundThread sounds;
	
	public boolean shouldApplicationExit = false;
	public final LinkedList<Platform> platforms = new LinkedList<Platform>();
	public final LinkedList<Entity> list = new LinkedList<Entity>();
	public final LinkedList<Entity> removeFromList = new LinkedList<Entity>();
	public final Level level;
	public LinkedList<Background> backgrounds = new LinkedList<Background>();
	
	public final static boolean DEBUG = false;
	
	public int x = 50, y = 50;
	public int r = 50, g = 100, b = 150;
	
	public void run() {
		shouldApplicationExit = false;
		
		long start;
		long timePerIteration = 1000 / 60; // for 60 FPS
		
		
		while (!shouldApplicationExit) {
			start = System.currentTimeMillis();
			
			try{
				update();
				draw();
			}catch(Exception e){
				System.err.println("Uncaught Exception in Loop!");
				e.printStackTrace();
				shouldApplicationExit = true;
			}
			
			try {
				Thread.sleep(Math.max(0, timePerIteration - (System.currentTimeMillis() - start)));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		sounds.dispose();
		graphic.dispose();
		System.exit(0);
	}
	
	public Game(){
		input = new Input(); // Init Input before Graphic, because Graphics uses Input!
		graphic = new Graphics(this);
		controller = new Controller();
		sounds = new SoundThread();
	
		int bw = Level.BLOCK_WIDTH;
		int bh = Level.BLOCK_HEIGHT;
		
//		level = MapLoader.LoadMapFromClingo(this, "clingo/encoding/labyrinth-23.lp");
		level = MapLoader.LoadMapOutOfBitmap(this, "Levels/testmap.bmp");
		
		backgrounds.add(new Background(ImageKey.BACKGROUND_1, .25f, 1, this));
		backgrounds.add(new Background(ImageKey.BACKGROUND_2, .5f, 1, this));

		// player (must be instantiated after platforms)
		Player p = new Player(1 * bw, 1 * bh - 19, 50, 50);
		list.add((Entity) p);
		controller.control(p);
		
		// add 2 dummy platforms
		Platform pf1 = new Platform(this, 8 * bw + bw / 2, 30 * bh + bh / 2, bw, bh, 30, 40, true);
		platforms.add(pf1);
		
		// jump blocks
		new JumpBlock(11, 32, level);
		
		this.start();
	}
	
	public void update(){
		level.update(this);
		controller.update(this);
		
		for (Entity e: removeFromList) {
			list.remove(e);
		}
		removeFromList.clear();
		
		for (Platform p : platforms) {
			p.update(this);
		}
		
		for (Entity ent : list) {
			ent.update(this);
		}
	}
	
	public void draw(){
		graphic.startDrawNewFrame(controller.viewX,controller.viewY);
		
//		graphic.newBackground();
		
		for (Background background : backgrounds) {
			background.draw(this);
		}
		
		level.draw(this);
		
		for (Platform p : platforms) {
			p.draw(this);
		}
		
		for (Entity e : list){
			e.draw(this);
		}
		
		graphic.endDrawNewFrame();
	}
	
}
