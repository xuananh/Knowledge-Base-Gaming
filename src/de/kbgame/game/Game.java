package de.kbgame.game;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import de.kbgame.geometry.ImageKey;
import de.kbgame.grafic.Background;
import de.kbgame.grafic.Graphics;
import de.kbgame.map.Level;
import de.kbgame.map.LevelBuilder;
import de.kbgame.util.FallingItem;
import de.kbgame.util.FallingItemCollection;
import de.kbgame.util.Input;
import de.kbgame.util.ShotCollection;
import de.kbgame.util.XValueObserver;
import de.kbgame.util.hud.HUD;
import de.kbgame.util.sound.SoundThread;

public class Game extends Thread {

	public final Graphics graphic;
	public final Input input;
	public final Controller controller;
	public final SoundThread sounds;

	public boolean shouldApplicationExit = false;
	public final LinkedList<Platform> platforms = new LinkedList<Platform>();
	public final LinkedList<Entity> list = new LinkedList<Entity>();
	public final LinkedList<Entity> removeFromList = new LinkedList<Entity>();
	public Level level;
	public LinkedList<Background> backgrounds = new LinkedList<Background>();
	public Player player;
	public HUD hud = new HUD();
	public FallingItem fi;
	public LinkedList<FallingItem> fallingItemList;

	public final static boolean DEBUG = false;
	public final static boolean LOAD_CLINGO = false;

	public Point goal = null;

	public int x = 50, y = 50;
	public int r = 50, g = 100, b = 150;

	public double gaFactor = 1.0;

	private TreeMap<Integer, XValueObserver> xValueObservers = new TreeMap<Integer, XValueObserver>();

	SuperEnemy superEnemy;

	Vector<ShotCollection> shots = new Vector<ShotCollection>();

	public Game() {
		input = new Input(); // Init Input before Graphic, because Graphics uses Input!
		graphic = new Graphics(this);
		controller = new Controller();
		sounds = new SoundThread();

		int bw = Level.BLOCK_WIDTH;
		int bh = Level.BLOCK_HEIGHT;
		int playerWidth = Level.BLOCK_WIDTH - 6;
		int playerHeight = Level.BLOCK_HEIGHT;

		
		Point playerStart = new Point();

		try {
			LevelBuilder builder = new LevelBuilder(new File("level_config.txt"), this, playerStart);
			level = builder.next();
			goal = level.getGoal();

			backgrounds.add(new Background(ImageKey.BACKGROUND_1, .25f, 1, this));
			backgrounds.add(new Background(ImageKey.BACKGROUND_2, .5f, 1, this));

			player = new Player(playerStart.x * bw + (playerWidth / 2), playerStart.y * bh + (playerHeight / 2), playerWidth, playerHeight, hud);
			list.add((Entity) player);
			controller.control(player);
			
			fallingItemList = new LinkedList<FallingItem>();
			
			// Test -> MapLoader
			fi = new FallingItem(player);
			
			fallingItemList.add(fi);
			fallingItemList.add(new FallingItem(300,1500,-20,7,player));
			
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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

			try {
				update();
				draw();
			} catch (Exception e) {
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

	public void update() {
		level.update(this);
		controller.update(this);

		for (Entity e : removeFromList) {
			list.remove(e);
		}
		removeFromList.clear();

		for (Platform p : platforms) {
			p.update(this);
		}

		for (Entity ent : list) {
			ent.update(this);
		}

		for (ShotCollection coll : this.shots) {
			// TODO remove list when empty
			coll.update(this);
		}
		
		for (FallingItem e : fallingItemList) {
			e.update(this);
		}
		


		checkObservers();
		if (isGoalReached()) {
			shouldApplicationExit = true;
		}
	}

	public void draw() {
		graphic.startDrawNewFrame(controller.viewX, controller.viewY);

		for (Background background : backgrounds) {
			background.draw(this);
		}

		level.draw(this);

		for (Platform p : platforms) {
			p.draw(this);
		}

		for (Entity e : list) {
			e.draw(this);
		}
		
		for (FallingItem e : fallingItemList) {
			e.draw(this);
		}
				
		hud.draw(this);

		for (ShotCollection coll : this.shots) {
			coll.draw(this);
		}

		graphic.endDrawNewFrame();
	}

	public void subscribeXPass(XValueObserver observer, Integer xValue) {
		xValueObservers.put(xValue, observer);
	}

	private void checkObservers() {
		for (Map.Entry<Integer, XValueObserver> observer : xValueObservers.entrySet()) {
			if (observer.getKey() <= player.x && observer.getValue() != null) {
				observer.getValue().notifyObserver(this);
			}
		}
	}

	private boolean isGoalReached() {
		return goal != null && player.x / Level.BLOCK_WIDTH == goal.x && player.y / Level.BLOCK_HEIGHT == goal.y;
	}
}
