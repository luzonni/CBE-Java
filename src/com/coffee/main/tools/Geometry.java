package com.coffee.main.tools;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.awt.Point;

public class Geometry {
	
	public static double Theta(double x1, double y1, double x2, double y2) {
		return sqrt(pow(x1 - x2, 2) + pow(y1 - y2, 2));
	}
	
	public static double Theta(Point p1, Point p2) {
		return sqrt(pow(p2.x - p1.x, 2) + pow(p2.y - p1.y, 2));
	}

}
