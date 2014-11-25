package de.kbgame.geometry;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D.Double;


@SuppressWarnings("serial")
public class Rectangle extends Double {
	
	public static final int TOP = 0;
	public static final int RIGHT = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT = 3;
	
	public Rectangle(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

	public Line2D.Double[] getLines() {
		Line2D.Double[] lines = new Line2D.Double[4];

		// top
		lines[TOP] = new Line2D.Double(x, y, x + width, y);
		
		// right
		lines[RIGHT] = new Line2D.Double(x + width, y, x + width, y + height);
		
		// bottom
		lines[BOTTOM] = new Line2D.Double(x, y + height, x + width, y + height);
		
		// left
		lines[LEFT] = new Line2D.Double(x, y, x, y + height);
		
		return lines;
	}
	
}
