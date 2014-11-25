package de.kbgame.geometry;

import java.awt.geom.Point2D.Double;

@SuppressWarnings("serial")
public class Point extends Double {
	
	public Point() {
		super();
	}
	
	public Point(double x, double y) {
		super(x, y);
	}

	public void add(Point p2) {
		x += p2.x;
		y += p2.y;
	}
	
	public void add(Point p1, Point p2) {
		x = p1.x + p2.x;
		y = p1.y + p2.y;
	}
}
