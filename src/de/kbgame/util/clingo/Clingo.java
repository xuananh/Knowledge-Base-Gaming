package de.kbgame.util.clingo;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import de.kbgame.game.Game;

public class Clingo {

	public static String callClingo(String[] params){
		
		if (params == null || params.length < 1) return null;

		final Process p;
		try {
			p = Runtime.getRuntime().exec(params);
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		Clingo.ClThread thread = new ClThread(p);
		int res = -1;
		try {
			thread.start();
			res = p.waitFor();
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (res != 0 && res != 10) {
			System.out.println("Process failed with status: " + res);
		}
		
		return thread.result;
	}
	
	public static class ClThread extends Thread{
		public String result = "";
		public Process p = null;
		public ClThread(Process p){
			this.p = p;
		}
		public void run() {
			String line;
			try {
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

				while ((line = input.readLine()) != null){
					//System.out.println(line);
					result += line + "\r\n";
				}
		
				input.close();
			} catch (IOException e) {
				System.out.println(" procccess not read"+e);
			}
		}
	}
	
	public static void exampleCall(Game g){
		Random r = new Random();
		int seed = r.nextInt(100000);
		//System.out.println("Seed="+seed);
		String[] params = new String[9];
		params[0] = "clingo/clingo.exe";
		params[1] = "clingo/test.txt";
		params[2] = "--seed="+seed;
		params[3] = "-c xo="+g.x;
		params[4] = "-c yo="+g.y;
		params[5] = "-c ro="+g.r;
		params[6] = "-c go="+g.g;
		params[7] = "-c bo="+g.b;
		//params[8] = "--rand-freq=0.8"; //Bullshit-Mode!
		params[8] = "--rand-freq=1"; //Bullshit-Mode!
		
		String res = callClingo(params);
		
		if (res.contains("x(") && res.contains("y(") 
				&& res.contains("r(") && res.contains("g(") && res.contains("b(")){

			g.x = extractInt(res,"x(",")",g.x);
			g.y = extractInt(res,"y(",")",g.y);
			g.r = extractInt(res,"r(",")",g.r);
			g.g = extractInt(res,"g(",")",g.g);
			g.b = extractInt(res,"b(",")",g.b);
			
		}
	}
	
	public static int extractInt(String main, String pre, String post, int def){
		int res = def;
		try{
			int p= main.indexOf(pre)+pre.length();
			String sub = main.substring(p);
			p = sub.indexOf(post);
			sub = sub.substring(0, p);
			//System.out.println("x="+sub);
			res = Integer.parseInt(sub);
		}catch(Exception e){
			res = def;
		}
		return res;
	}
}
