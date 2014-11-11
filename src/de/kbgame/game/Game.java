package de.kbgame.game;

import java.awt.Color;
import java.util.LinkedList;

import de.kbgame.grafic.Graphics;
import de.kbgame.map.Level;
import de.kbgame.util.Input;
import de.kbgame.util.SoundThread;

public class Game extends Thread{
	
	public Graphics graphic;
	public Input input;
	public Controller controller;
	public SoundThread sounds;
	
	public boolean drawEntityBorders = false; 
	public boolean shouldApplicationExit = false;
	public LinkedList<Entity> list = new LinkedList<Entity>();
	public Level level;
	
	public int x = 50, y = 50;
	public int r = 50, g = 100, b = 150;
	
	public void run() {
		shouldApplicationExit = false;
		
		while (!shouldApplicationExit){
			try{
			
				update();
				draw();
			
			}catch(Exception e){
				System.err.println("Uncaught Exception in Loop!");
				e.printStackTrace();
				shouldApplicationExit = true;
			}
			
			try {
				Thread.sleep(10);
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
		graphic.loadImages();
		controller = new Controller();
		sounds = new SoundThread();
		for (int i=0;i<20;i++) list.add((Entity)new Rectangle(50+i*15, 50+i*15,10,10));
		list.add((Entity) new Circle(300, 150, 70, 50));
		Player p = new Player(25, 25, 50, 50);
		list.add((Entity)p);
		controller.control(p);
		level = new Level(15, 5);
		
		// add 2 dummy platforms
		int bw = level.blockwidth;
		int bh = level.blockheight;
		
		Platform pf = new Platform(this, 10 * bw, 2 * bh, bw, bh, 2, 5, true);
		list.add((Entity) pf);
		pf = new Platform(this, 2 * bw, 2 * bh, bw, bh, 2, 7, false);
		list.add((Entity) pf);
		
		
		this.start();
	}
	
	public void update(){
		level.update(this);
		controller.update(this);
		for (Entity ent : list) ent.update(this);
		//clingo.exampleCall(this);
		//x = (x + 1) % graphic.Width;
		//y = (y + 2) % graphic.Height;
	
	}
	
	public void draw(){
		graphic.startDrawNewFrame();
		
		graphic.newBackground();
		level.draw(this);
		for (Entity e : list){
			e.draw(this);
			if (drawEntityBorders) e.drawBox(this);
		}
		
		//Examples
		graphic.drawText("TESSSST", 100, 500, Color.white);
		
		graphic.endDrawNewFrame();
	}
	
}
