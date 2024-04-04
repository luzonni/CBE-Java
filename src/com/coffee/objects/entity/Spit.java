package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.Directions;
import com.coffee.objects.tiles.Tile;

class Spit extends Entity {

	private Directions dir;

	public Spit(int x, int y, Directions dir) {
		super(-1, x, y);
		this.dir = dir;
		setSize(2*Engine.GameScale, 2*Engine.GameScale);
	}

	@Override
	public BufferedImage getSprite() {
		return null;
	}

	@Override
	public void tick() {
		if(colliding())
			Game.getLevel().getEntities().remove(this);
		if(this.dir.equals(Directions.Left))
			setX(getX() - 2*Engine.GameScale);
		else
			setX(getX() + 2*Engine.GameScale);
		Player p = Game.getPlayer();
		if(this.collidingWith(p))
			p.die();
	}
	
	private boolean colliding() {
		double x = getMiddle().x / Tile.getSize();
		double y = getMiddle().y / Tile.getSize();
		Tile tile = Game.getLevel().getTile((int)x, (int)y);
		if(tile == null)
			return false;
		return tile.isSolid();
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(Engine.Color_Primary);
		g.fillRect((int)getX() - Game.getCam().getX(), (int)getY() - Game.getCam().getY(), getWidth(), getHeight());
	}
	
	@Override
	public void dispose() {
		
	}

}
