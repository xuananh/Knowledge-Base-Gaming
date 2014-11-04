package de.kbgame.game;

import java.awt.Color;
import java.awt.event.KeyEvent;

import de.kbgame.grafic.Graphics;
import de.kbgame.util.Input;

public class Game extends Thread{
	
	public Graphics graphic;
	public Input input;
	
	public boolean shouldApplicationExit = false;
	
	public int x = 0, y = 0;
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
		
		graphic.dispose();
		System.exit(0);
	}
	
	public Game(){
		input = new Input(); // Init Input before Graphic, because Graphics uses Input!
		graphic = new Graphics(this);
		
		this.start();
	}
	
	public void update(){
		if (input.getKey(KeyEvent.VK_ESCAPE)) shouldApplicationExit = true;
		
//		clingo.exampleCall(this);
		//x = (x + 1) % graphic.Width;
		//y = (y + 2) % graphic.Height;
	
	}
	
	public void draw(){
		graphic.startDrawNewFrame();
		
		graphic.newBackground();
		graphic.drawRectangle(x-25, y-25, 500, 500, new Color(r,g,b));
		
		graphic.endDrawNewFrame();
	}
	
}
