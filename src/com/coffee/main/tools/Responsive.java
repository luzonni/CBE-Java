package com.coffee.main.tools;

import java.awt.Point;
import java.awt.Rectangle;

import com.coffee.main.Engine;

public class Responsive {
	
	private float Vertical, Horizontal;
	private Rectangle bounds;
	private Responsive parent;
	
	public static Responsive createPoint(Responsive parent, float dinstance_x, float dinstance_y) {
		return new Responsive(parent, dinstance_x, dinstance_y, new Rectangle());
	}
	
	public static Responsive createRectangle(Responsive parent, Rectangle bounds, float dinstance_x, float dinstance_y) {
		return new Responsive(parent, dinstance_x, dinstance_y, bounds);
	}
	
	private Responsive(Responsive parent, float dinstance_x, float dinstance_y, Rectangle bounds) {
		this.parent = parent;
		this.Vertical = dinstance_x;
		this.Horizontal = dinstance_y;
		this.bounds = new Rectangle();
		this.bounds.setSize(bounds.getSize());
		setPosition();
	}

	public void setRelative(Responsive parent, int dinstance_x, int dinstance_y) {
		this.parent = parent;
		this.Vertical = dinstance_x;
		this.Horizontal = dinstance_y;
		setPosition();
	}
	
	public void setRelative(int dinstance_x, int dinstance_y) {
		this.Vertical = dinstance_x;
		this.Horizontal = dinstance_y;
		setPosition();
	}
	
	public void setSize(int w, int h) {
		this.bounds.setSize(w, h);
	}
	
	public void setPosition() {
		float[] values;
		if(this.parent == null) {
			values = getWithPercent();
		}else {
			values = getWithParent();
		}
		this.bounds.setLocation((int)values[0] - this.bounds.width/2, (int)values[1] - this.bounds.height/2);
	}
	
	private float[] getWithPercent() {
		float Width = Engine.getWidth();
		float Height = Engine.getHeight();
		return new float[] {Width*(Vertical/100f), Height*(Horizontal/100f)};
	}
	
	private float[] getWithParent() {
		int side_x = (Vertical == 0) ? 0 : (Vertical > 0) ? 1 : -1;
		int side_y = (Horizontal == 0) ? 0 : (Horizontal > 0) ? 1 : -1;
		float x = parent.getPosition().x + side_x*(parent.bounds.width/2 + this.bounds.width/2) + Vertical;
		float y = parent.getPosition().y + side_y*(parent.bounds.height/2 + this.bounds.height/2) + Horizontal;
		return new float[] {x, y};
	}
	
	private Point getCenter(Rectangle bounds) {
		return new Point(bounds.x + bounds.width/2, bounds.y + bounds.height/2);
	}
	
	public Rectangle getBounds() {
		setPosition();
		return this.bounds;
	}
	
	public Point getPosition() {
		setPosition();
		return getCenter(bounds);
	}

}
