package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.particles.Boom;
import com.coffee.objects.tiles.Tile;

class IronBall extends Entity {

	private double radians;
	private double rotate;
	private static BufferedImage sprite;
	private int speed;

	public IronBall(int x, int y, double radians, int speed) {
		super(0, x - 5*Engine.GameScale, y - 5*Engine.GameScale);
		this.setSize(10*Engine.GameScale, 10*Engine.GameScale);
		this.radians = radians;
		this.speed = speed;
		if(sprite == null) 
			sprite = getSprite("IronBall", Engine.Color_Primary)[0];
		for(int i = 0; i < 30; i++)
			Game.getLevel().addParticle(new Boom(getMiddle().x, getMiddle().y, radians - Math.PI/2 + Engine.RAND.nextDouble()*Math.PI));
	}
	
	@Override
	public BufferedImage getSprite() {
		return sprite;
	}

	@Override
	public void tick() {
		if(colliding())
			Game.getLevel().getEntities().remove(this);
		Player p = Game.getPlayer();
		if(p.collidingWith(this)) {
			p.die();
			Game.getLevel().getEntities().remove(this);
		}
		this.setX(this.getX() + Math.cos(this.radians)*this.speed);
		this.setY(this.getY() + Math.sin(this.radians)*this.speed);
		rotate += 0.25;
	}
	
	private boolean colliding() {
		int x = getMiddle().x / Tile.getSize();
		int y = getMiddle().y / Tile.getSize();
		Tile tile = Game.getLevel().getTile(x, y);
		if(tile == null)
			return false;
		return tile.isSolid();
	}
	
	@Override
	public void render(Graphics2D g) {
		g.rotate(rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
		renderEntity(sprite, g);
		g.rotate(-rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
	}

}
