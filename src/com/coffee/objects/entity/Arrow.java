package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.Variables;
import com.coffee.objects.tiles.Tile;

public class Arrow extends Entity {
	
	private static BufferedImage sprite;
	private double direction;
	private int speed;
	
	public Arrow(int x, int y, double direction, int speed) {
		super(0, x, y);
		this.direction = direction;
		this.speed = speed;
		if(sprite == null)
			sprite = getSprite("Arrow", Engine.Color_Primary)[0].getSubimage(0, 0, 15*Engine.GameScale, 5*Engine.GameScale);
		this.setSize(sprite.getWidth(), sprite.getHeight());
		setDepth(1);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite;
	}

	@Override
	public void tick() {
		if(colliding())
			this.speed = 0;
		for(int i = 0; i < Game.getLevel().getEntities().size(); i++) {
			Entity e = Game.getLevel().getEntities().get(i);
			if(e.collidingWith(this) && this.speed != 0)
				if(e.getVar(Variables.Alive)) {
					e.die();
					Game.getLevel().getEntities().remove(this);
				}
		}
		this.setX(this.getX() + (Math.cos(this.direction)*this.speed));
		this.setY(this.getY() + (Math.sin(this.direction)*this.speed));
	}
	
	private boolean colliding() {
		double x = (getMiddle().x + (Math.cos(this.direction)*this.speed)) / Tile.getSize();
		double y = (getMiddle().y + (Math.sin(this.direction)*this.speed)) / Tile.getSize();
		Tile tile = Game.getLevel().getTile((int)x, (int)y);
		if(tile == null)
			return false;
		return tile.isSolid();
	}

	@Override
	public void render(Graphics2D g) {
		g.rotate(direction, getMiddle().x - Game.getCam().getX(), getMiddle().getY() - Game.getCam().getY());
		g.drawImage(sprite, (int)getX() - getWidth()/2 - Game.getCam().getX(), (int)getY() - Game.getCam().getY(), getWidth(), getHeight(), null);
		g.rotate(-direction, getMiddle().x - Game.getCam().getX(), getMiddle().getY() - Game.getCam().getY());
	}

}
