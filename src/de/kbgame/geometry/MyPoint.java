package de.kbgame.geometry;

import java.awt.geom.Point2D.Double;

@SuppressWarnings("serial")
public class MyPoint extends Double {

	public MyPoint() {
		super();
	}

	public MyPoint(double x, double y) {
		super(x, y);
	}
	
	public MyPoint(MyPoint p) {
		super();
		x = p.x;
		y = p.y;
	}

	public void add(MyPoint p2) {
		x += p2.x;
		y += p2.y;
	}

	public void add(MyPoint p1, MyPoint p2) {
		x = p1.x + p2.x;
		y = p1.y + p2.y;
	}

	public void multiply(double scalar) {
		x *= scalar;
		y *= scalar;
	}

	public void copy(MyPoint p) {
		x = p.x;
		y = p.y;
	}
	
	public String toString() {
		return "( " + x + " , " + y + " )";
	}
}
