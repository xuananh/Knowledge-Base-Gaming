package de.kbgame.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;

public class Input {
	
	public KeyAdapter KeyInput;
	public MouseAdapter MouseInput, MouseMove, MouseWheel;
	
	public int x=-1,y=-1,lastx=-1,lasty=-1,clickx=-1,clicky=-1;
	public boolean mouse1=false,mouse2=false,mouse3=false,mouse1old=false,mouse2old=false,mouse3old=false;
	public int newclick=-1, scrollWheel=0;

	public LinkedList<Integer> keydowns = new LinkedList<Integer>();
	public LinkedList<Integer> waitforkeyuptillpressagain = new LinkedList<Integer>();
	
	public Input() {
		KeyInput = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				
				int kc = e.getKeyCode();
				
				if (!waitforkeyuptillpressagain.contains(kc) && !keydowns.contains(kc)) {
					keydowns.add(kc);
				}
			}
		 
			public void keyReleased(KeyEvent e) {
				
				int kc = e.getKeyCode();
			
				if (keydowns.contains(kc)) {
					keydowns.removeFirstOccurrence(kc);
				}
				if (waitforkeyuptillpressagain.contains(kc)){
					waitforkeyuptillpressagain.removeFirstOccurrence(kc);				
				}
			}
		};
	
		MouseInput = new MouseAdapter() {
			public void mousePressed(MouseEvent e){
				clickx=x;
				clicky=y;
				switch (e.getButton()){
				case MouseEvent.NOBUTTON:{System.out.println("Warning: Pressed Button, but not NOBUTTON recieved.");break;}
				case MouseEvent.BUTTON1:{mouse1=true;newclick=1;break;}
				case MouseEvent.BUTTON2:{mouse2=true;newclick=2;break;}
				case MouseEvent.BUTTON3:{mouse3=true;newclick=3;break;}
				}
			}
			public void mouseReleased(MouseEvent e){
				switch (e.getButton()){
				case MouseEvent.NOBUTTON:{System.out.println("Warning: Pressed Button, but not NOBUTTON recieved.");break;}
				case MouseEvent.BUTTON1:{mouse1old=mouse1;mouse1=false;break;}
				case MouseEvent.BUTTON2:{mouse2old=mouse2;mouse2=false;break;}
				case MouseEvent.BUTTON3:{mouse3old=mouse3;mouse3=false;break;}
				}
			}
		};
		
		MouseMove = new MouseAdapter() {
			public void mouseMoved(MouseEvent e){
				lastx=x;
				lasty=y;
				x=e.getX();
				y=e.getY();
				}
			public void mouseDragged(MouseEvent e){
				lastx=x;
				lasty=y;
				x=e.getX();
				y=e.getY();
				}
		};
		
		MouseWheel = new MouseAdapter(){
			public void mouseWheelMoved(MouseWheelEvent e) {
				scrollWheel = e.getWheelRotation();//e.getScrollAmount();
			}
		};
	}
	
	public boolean getKey(int c){
		try{
			return keydowns.contains(c);
		}catch(Exception e){
			if (keydowns == null) System.err.print("Pressed Keys Error: This should not happen... Keydowns == null ");
			else System.err.print("Pressed Keys Error: This should not happen... LinkedList.contains(x) => Nullpointer ");
			return false;
		}
	}
	
	public void setKeyDown(int c){ 
		if (!keydowns.contains(c)) keydowns.add(c);
	}
	
	public void setKeyUp(int c){ 
		if (keydowns.contains(c)) keydowns.removeFirstOccurrence(c);
	}
	
	public void dontAlertTillKeyUp(int c){ 
		if (keydowns.contains(c)) keydowns.removeFirstOccurrence(c);
		if (!waitforkeyuptillpressagain.contains(c)) waitforkeyuptillpressagain.add(c);
	}
	
	public void resetKeys(){
		keydowns = new LinkedList<Integer>();
		waitforkeyuptillpressagain = new LinkedList<Integer>();
	}
	
	public void resetMouseButtons(){
		mouse1=false;mouse2=false;mouse3=false;
		mouse1old=false;mouse2old=false;mouse3old=false;
	}
	
	public boolean getMouse(int button){
		switch (button){
		case 1:{return mouse1;}
		case 2:{return mouse2;}
		case 3:{return mouse3;}
		}
		return false;
	}
	
	public boolean releasedMouse(int button){
		switch (button){
		case 1:{return !mouse1&&mouse1old;}
		case 2:{return !mouse2&&mouse2old;}
		case 3:{return !mouse3&&mouse3old;}
		}
		return false;
	}
	
}
