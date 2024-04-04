package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.Variables;
import com.coffee.objects.particles.Dust;
import com.coffee.objects.tiles.Tile;

public class Miniboll extends Entity {

	private double radians;
	private int count;
	
	public Miniboll(int id, int x, int y, double radians) {
		super(id, x, y);
		this.radians = radians;
		this.setSize(2*Engine.GameScale, 2*Engine.GameScale);
	}

	@Override
	public BufferedImage getSprite() {
		return null;
	}

	@Override
	public void tick() {
		this.setX(getX() + Engine.GameScale*Math.cos(radians)*2);
		this.setY(getY() + Engine.GameScale*Math.sin(radians)*2);
		int x_next = (int)(getMiddle().x + Engine.GameScale*Math.cos(radians)*2) / Tile.getSize();
		int y_next = (int)(getMiddle().y + Engine.GameScale*Math.sin(radians)*2) / Tile.getSize();
		int x = (int)(getMiddle().x) / Tile.getSize();
		int y = (int)(getMiddle().y) / Tile.getSize();
		Tile vertinal_tile = Game.getLevel().getTile(x_next, y);
		Tile horizontal_tile = Game.getLevel().getTile(x, y_next);
		double ax = Math.cos(radians);
		double ay = Math.sin(radians);
		if(vertinal_tile == null || vertinal_tile.isSolid()) {
			ax *= -1;
			this.radians = Math.atan2(ay, ax);
			this.count++;
		}
		if(horizontal_tile == null || horizontal_tile.isSolid()) {
			ay *= -1;
			this.radians = Math.atan2(ay, ax);
			this.count++;
		}
		Game.getLevel().addParticle(new Dust(getMiddle().x, getMiddle().y, this.radians-Math.PI*2));
		for(int i = 0; i < Game.getLevel().getEntities().size(); i++) {
			Entity entity = Game.getLevel().getEntities().get(i);
			if(entity.getVar(Variables.Alive) && entity.collidingWith(this)) {
				entity.die();
				die();
			}
		}
		if(count > 3) {
			die();
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor((count % 2 == 0) ? Engine.Color_Primary : Engine.Color_Tertiary);
		g.fillRect((int)getX() - Game.getCam().getX(), (int)getY() - Game.getCam().getY(), getWidth(), getHeight());
	}
	
	@Override
	public void dispose() {
		
	}

}
