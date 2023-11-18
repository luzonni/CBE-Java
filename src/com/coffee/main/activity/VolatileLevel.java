package com.coffee.main.activity;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import com.coffee.Inputs.Mouse;
import com.coffee.Inputs.Mouse_Button;
import com.coffee.graphics.FontG;
import com.coffee.level.Level;
import com.coffee.main.Engine;

class VolatileLevel {
	
	private Level level;
	private boolean blocked;
	private Rectangle bounds;
	
	private String Name;
	private Font font;

	public VolatileLevel(Level level) {
		this.level = level;
		this.bounds = new Rectangle(Engine.RAND.nextInt(Engine.getWidth()), Engine.RAND.nextInt(Engine.getHeight()), 0, 0);
		this.font = FontG.font(14*Engine.GameScale);
		this.Name = level.getName();
		this.blocked = true;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public Level getLevel() {
		return level;
	}

	public boolean isBlocked() {
		return this.blocked;
	}
	
	public Point getPoint() {
		return new Point(bounds.x + bounds.width/2, bounds.y + bounds.height/2);
	}
	
	public Rectangle getBounds() {
		return this.bounds;
	}
	
	private void setBounds() {
		int w = fontWidth();
		int h = fontHeight();
		bounds.setSize(w + Engine.GameScale*3, h + Engine.GameScale*2);
	}
	
	public int fontWidth() {
		return FontG.getWidth(Name, font);
	}
	
	public int fontHeight() {
		return FontG.getHeight(Name, font);
	}
	
	public boolean selected() {
		return Mouse.clickOn(Mouse_Button.LEFT, bounds) && !this.blocked;
	}
	
	@Override
	public boolean equals(Object obj) {
		return level.getName().equalsIgnoreCase(((VolatileLevel)obj).getLevel().getName());
	}
	
	public void render(Graphics2D g) {
		setBounds();
		if(bounds.contains(Mouse.getX(), Mouse.getY()) && !isBlocked())
			g.setColor(Engine.Color_Secondary);
		else
			g.setColor(Engine.Color_Tertiary);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		if(blocked)
			g.setColor(Engine.Color_Secondary);
		else
			g.setColor(Engine.Color_Primary);
		g.setFont(font);
		g.drawString(Name, bounds.x + Engine.GameScale*2, bounds.y + fontHeight());
		g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
}
