package de.kbgame.game;

import java.util.LinkedList;

import de.kbgame.game.Entity;
import de.kbgame.game.Platform;
import de.kbgame.game.Player;
import de.kbgame.grafic.Graphics;
import de.kbgame.map.Level;
import de.kbgame.map.MapLoader;
import de.kbgame.util.Input;
import de.kbgame.util.SoundThread;

public class Game extends Thread{
	
	public final Graphics graphic;
	public final Input input;
	public final Controller controller;
	public final SoundThread sounds;
	
	public boolean shouldApplicationExit = false;
	public final LinkedList<Platform> platforms = new LinkedList<Platform>();
	public final LinkedList<Entity> list = new LinkedList<Entity>();
	public final Level level;
	
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
		
		level = MapLoader.LoadMapOutOfBitmap(this, "Levels/testmap.bmp");

		// player (must be instantiated after platforms)
		Player p = new Player(10 * bw, 30 * bh - 19, 50, 50);
		list.add((Entity) p);
		controller.control(p);
		
		// add 2 dummy platforms
		Platform pf1 = new Platform(this, 8 * bw + bw / 2, 30 * bh + bh / 2, bw, bh, 30, 40, true);
		platforms.add(pf1);
//		Platform pf2 = new Platform(this, 2 * bw, 2 * bh, bw, bh, 2, 7, false);
//		platforms.add(pf2);

//		p.setParent(pf1);
		
		this.start();
	}
	
	public void update(){
		level.update(this);
		controller.update(this);
		
		for (Platform p : platforms) {
			p.update(this);
		}
		
		for (Entity ent : list) {
			ent.update(this);
		}
	}
	
	public void draw(){
		graphic.startDrawNewFrame(controller.viewX,controller.viewY);
		
		graphic.newBackground();
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
