package com.coffee.objects;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;

import com.coffee.command.Receiver;
import com.coffee.objects.tiles.Tile;

public abstract class Objects implements Receiver {
	
	private Values values;
	
	public Objects(int ID) {
		this.values = new Values();
		this.values.addInt("id", ID);
		this.values.addDouble("x", 0);
		this.values.addDouble("y", 0);
		this.values.addInt("width", Tile.getSize());
		this.values.addInt("height", Tile.getSize());
		this.values.addInt("depth", 0);

		for(Variables type : Variables.values())
				this.values.addBoolean(type.getName(), false);
	}

	public abstract BufferedImage getSprite();
	
	public int ID() {
		return getValues().getInt("id");
	}
	
	public Values getValues() {
		return this.values;
	}
	
	public boolean getVar(Variables type) {
		return this.getValues().getBoolean(type.getName());
	}
	
	public boolean collidingWith(Objects o) {
		return this.getBounds().intersects(o.getBounds());
	}
	
	public void setVar(Variables type, boolean bool) {
		this.getValues().setBoolean(type.getName(), bool);
	}
	
	protected void setDepth(int newDepth) {
		this.values.setInt("depth", newDepth);
	}
	
	public void setX(double X) {
		this.getValues().setDouble("x", X);
	}
	
	public void setY(double Y) {
		this.getValues().setDouble("y", Y);
	}
	
	public double getX() {
		return this.getValues().getDouble("x");
	}
	
	public double getY() {
		return this.getValues().getDouble("y");
	}
	
	public int getWidth() {
		return this.getValues().getInt("width");
	}
	
	public int getHeight() {
		return this.getValues().getInt("height");
	}

	public void setSize(int Width, int Height) {
		this.getValues().setInt("width", Width);
		this.getValues().setInt("height", Height);
	}
	
	public Point getMiddle() {
		return new Point((int)this.getX() + this.getHeight()/2, (int)this.getY() + this.getHeight()/2);
	}
	
	public static Comparator<Objects> Depth = new Comparator<Objects>() {
		@Override
		public int compare(Objects n0, Objects n1) {
			return Integer.compare(n0.values.getInt("depth"), n1.values.getInt("depth"));
		}
	};
	
	public abstract void tick();
	
	public abstract void render(Graphics2D g);
	
	public Rectangle getBounds() {
		return new Rectangle((int)getX(), (int)getY(), getWidth(), getHeight());
	}
	
}
