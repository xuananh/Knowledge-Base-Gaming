package de.kbgame.util.clingo;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class Clingo {

	/**
	 * die Methode ruft den Befehl von Clingo in eine selbst Prozesse
	 * @param params list alle paramete von dem Befehl
	 * @return Alles von Konsole bestellt
	 */
	public String callClingo(String[] params){
		
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
	
	private static class ClThread extends Thread{
		public String result = "";
		public Process p = null;
		public ClThread(Process p){
			this.p = p;
		}
		
		/*
		 * liest jeder line von Konsole
		 * (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			String line;
			try {
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

				while ((line = input.readLine()) != null){
					result += line + "\r\n";
				}
				input.close();
			} catch (IOException e) {
				System.out.println(" procccess not read"+e);
			}
		}
	}
}
