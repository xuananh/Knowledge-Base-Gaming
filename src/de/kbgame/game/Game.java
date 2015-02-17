package de.kbgame.game;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Vector;

import de.kbgame.geometry.ImageKey;
import de.kbgame.grafic.Background;
import de.kbgame.grafic.Graphics;
import de.kbgame.map.Level;
import de.kbgame.map.MapLoader;
import de.kbgame.util.Input;
import de.kbgame.util.Shot;
import de.kbgame.util.ShotCollection;
import de.kbgame.util.hud.HUD;
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
	public Player player;
	public HUD hud = new HUD();
	
	public final static boolean DEBUG = false;
	public final static boolean LOAD_CLINGO = false;
	
	public Point goal = new Point(-1, -1);
	
	public int x = 50, y = 50;
	public int r = 50, g = 100, b = 150;
	
	public double gaFactor = 1.0;
	
	Vector<ShotCollection> shots = new Vector<ShotCollection>();
	
	
	public void run() {
		shouldApplicationExit = false;
		
		long currentMillisecs, deltaMillisecs;
		long lastFrameMillisecs = System.currentTimeMillis();
		double timePerIteration = 1000 / 60; // for 60 FPS
		
		while (!shouldApplicationExit) {
			currentMillisecs = System.currentTimeMillis();
			
	        deltaMillisecs = currentMillisecs - lastFrameMillisecs;
			
	        lastFrameMillisecs = currentMillisecs;
	        gaFactor = Math.max(1.0, (double) deltaMillisecs / timePerIteration);
			
			try{
				update();
				draw();
			}catch(Exception e){
				System.err.println("Uncaught Exception in Loop!");
				e.printStackTrace();
				shouldApplicationExit = true;
			}
			
			try {
				Thread.sleep((long) Math.max(0, timePerIteration - (System.currentTimeMillis() - currentMillisecs)));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		sounds.dispose();
		graphic.dispose();
		System.exit(0);
	}
	
	public Game() {
		input = new Input(); // Init Input before Graphic, because Graphics uses Input!
		graphic = new Graphics(this);
		controller = new Controller();
		sounds = new SoundThread();
	
		int bw = Level.BLOCK_WIDTH;
		int bh = Level.BLOCK_HEIGHT;
		int playerWidth = Level.BLOCK_WIDTH - 6;
		int playerHeight = Level.BLOCK_HEIGHT;
		
		Point playerStart = new Point(1, 40);
		
		if (LOAD_CLINGO) {
			level = MapLoader.LoadMapFromClingo(this, "clingo/encoding/labyrinth-23.lp", playerStart, goal);
		} else {
			level = MapLoader.LoadMapOutOfBitmap(this, "Levels/testmap.bmp");
			
			// add 2 dummy platforms
			Platform pf1 = new Platform(this, 8 * bw + bw / 2, 30 * bh + bh / 2, bw, bh, 30, 40, true);
			platforms.add(pf1);
			
			// jump blocks
			new JumpBlock(11, 32, level);
		}
		
		backgrounds.add(new Background(ImageKey.BACKGROUND_1, .25f, 1, this));
		backgrounds.add(new Background(ImageKey.BACKGROUND_2, .5f, 1, this));

		player = new Player(playerStart.x * bw + (playerWidth / 2), playerStart.y * bh + (playerHeight / 2), playerWidth, playerHeight, hud);
		list.add((Entity) player);
		controller.control(player);
		

		ShotCollection shots = new ShotCollection(20, 5, player);
		// Schuesse aus Clingo auslesen 
		// TODO: woanders im Quellcode?
		String[] params = new String[3];
		params[0] = "clingo";
		params[1] = "clingo/encoding/schuesse-v0.1.txt";
		params[2] = "1";

		AnswerASP a = ClingoFactory.getInstance().getAnswerASP(params);
		ArrayList<PredicateASP> pres = (ArrayList) a.getPreListFromString("schuss");
		for (PredicateASP p : pres) {
			System.out.println(p.toString());
		}
	
		for (PredicateASP p : pres) {
			int y = (int) p.getParameterOfIndex(1);
			System.out.println(y);
			shots.add(new Shot(new Point(600, 2000), (int) p.getParameterOfIndex(1), (int) p.getParameterOfIndex(0), shots));
		}

	
		shots.add(new Shot(new Point(600, 2000), 1, 19, shots));
		shots.add(new Shot(new Point(600, 2000), 2, 3, shots));
		shots.add(new Shot(new Point(600, 2000), 4, 1, shots));
		shots.add(new Shot(new Point(600, 2000), 5, 10, shots));
		shots.add(new Shot(new Point(600, 2000), 9, 1, shots));
		shots.add(new Shot(new Point(600, 2000), 10, 8, shots));
		
		
		this.shots.add(shots);
		
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
		
		if (goal.x > -1) {
			if (player.getSurroundingRectangle().intersects(goal.x * Level.BLOCK_WIDTH, goal.y * Level.BLOCK_HEIGHT, Level.BLOCK_WIDTH, Level.BLOCK_HEIGHT)) {
				// TODO
				shouldApplicationExit = true;
			}
		}
		
		for (ShotCollection coll : this.shots) {
			// TODO remove list when empty
			coll.update(this);
		}
	}
	
	public void draw(){
		graphic.startDrawNewFrame(controller.viewX,controller.viewY);
		
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
		
		hud.draw(this);
		
		for (ShotCollection coll : this.shots) {
			coll.draw(this);
		}
		
		graphic.endDrawNewFrame();
	}

}
