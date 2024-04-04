package com.coffee.objects.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.Geometry;
import com.coffee.main.activity.Game;
import com.coffee.objects.Variables;
import com.coffee.objects.tiles.Tile;

public class Crossbow extends Entity {

	private int timer;
	private static BufferedImage base;
	private static BufferedImage bow;
	
	private double radians;
	
	public Crossbow(int id, int x, int y) {
		super(id, x, y);
		if(base == null)
			base = getSprite("CrossBow", Engine.Color_Tertiary)[0];
		if(bow == null)
			bow = getSprite("CrossBow", Engine.Color_Primary)[1];
		setVar(Variables.Selectable, true);
		setVar(Variables.Movable, true);
	}

	@Override
	public BufferedImage getSprite() {
		BufferedImage sprite = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = sprite.getGraphics();
		g.drawImage(base, 0, 0, null);
		g.drawImage(bow, 0, 0, null);
		g.dispose();
		return sprite;
	}

	@Override
	public void tick() {
		Player p = Game.getPlayer();
		if(Geometry.Theta(this.getMiddle(), p.getMiddle()) < Tile.getSize()*8)
			radians = Math.atan2(this.getMiddle().getY() - p.getMiddle().getY(), this.getMiddle().getX() - p.getMiddle().getX()) - Math.PI/2;
		else
			radians = 0;
		timer++;
		if(timer > 25) {
			if(isFree()) {
				timer = 0;
				double r = radians - Math.PI/2; 
				int x = (int)getMiddle().x + (int)(Math.cos(r)*Tile.getSize()/3.75);
				int y = (int)getMiddle().y + (int)(Math.sin(r)*Tile.getSize()/3.75);
				Game.getLevel().addEntity(new Arrow(x, y, r, 5*Engine.GameScale));
			}
		}
	}
	
	public boolean isFree() {
		int x = getMiddle().x;
		int y = getMiddle().y;
		double r = radians - Math.PI/2; 
		while(Game.getLevel().getBounds().contains(x, y)) {
			x += Math.cos(r) * Tile.getSize();
			y += Math.sin(r) * Tile.getSize();
			int index_x = x / Tile.getSize();
			int index_y = y / Tile.getSize();
			Tile T = Game.getLevel().getTile(index_x, index_y);
			Player P = Game.getPlayer();
			if(T.centralizedWith(P))
				return true;
			if(T.isSolid())
				return false;
		}
		return false;
	}

	@Override
	public void render(Graphics2D g) {
		renderEntity(base, g);
		g.rotate((radians - Math.PI/4), getX() - Game.getCam().getX() + getWidth()/2, getY() - Game.getCam().getY() + getHeight()/2);
		renderEntity(bow, g);
		g.rotate(-(radians - Math.PI/4), getX() - Game.getCam().getX() + getWidth()/2, getY() - Game.getCam().getY() + getHeight()/2);
	}
	
	@Override
	public void dispose() {
		base = null;
		bow = null;
	}

}
