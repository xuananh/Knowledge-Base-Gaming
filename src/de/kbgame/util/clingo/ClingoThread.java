package de.kbgame.util.clingo;

import java.awt.Color;
import java.util.List;


public class ClingoThread extends Thread{
	
	public boolean shouldApplicationExit = false;
	private final ClingoGraphic graphic = new ClingoGraphic();
	private final List<PredicateASP> pres;
	private int index = 1;
	
	int x = 0, y = 0;
	private final int r1 = 50, g1 = 100, b1 = 150;
	private final int r9 = 255, g9 = 0, b9 = 0;
	private final int r0 = 150, g0 = 150, b0 = 150;
	private final int w,h;
	
	public ClingoThread() {
		String[] params = new String[3];
		params[0] = "clingo";
		params[1] = "clingo/labyrinth-23.lp";
		params[2] = "1";

		String res = Clingo.callClingo(params);

		AnswerASP a = AnswerASP.getAnswerASPfromRes(res);
		pres = a.getPreListFromString("block");
		
		w = graphic.Width / 30;
		h = graphic.Height / 10;
		
		this.start();
	}
	
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
		System.exit(0);
	}
	
	public void update(){
		if(index >= pres.size() ) {
			index = 1;
		}
		index++;
	}
	
	public void draw(){
		graphic.startDrawNewFrame();
		
		graphic.newBackground();
		for (int i = 0; i < index; i++) {
			PredicateASP p = pres.get(i);
			x = (int) p.getParameterOfIndex(0)*w;
			y = (int) p.getParameterOfIndex(1)*h;
			if ((int) p.getParameterOfIndex(2) == 0) {
				graphic.drawRectangle(x, y, w, h, new Color(r0, g0, b0));
			}
			if ((int) p.getParameterOfIndex(2) == 1) {
				graphic.drawRectangle(x, y, w, h, new Color(r1, g1, b1));
			}
			if ((int) p.getParameterOfIndex(2) == 9) {
				graphic.drawRectangle(x, y, w, h, new Color(r9, g9, b9));
			}
		}
		
		graphic.endDrawNewFrame();
	}

}
