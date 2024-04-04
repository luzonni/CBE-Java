package com.coffee.objects.tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.entity.Button;
import com.coffee.objects.entity.Entity;

public class Reforced_Door extends Tile {

private static BufferedImage[] sprite;
	
	public Reforced_Door(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null) {
			sprite = getSprite("reforced_door", Engine.Color_Primary);
		}
		this.setSolid(true);
	}
	
	private boolean open() {
		boolean has = false;
		for(Entity e : Game.getLevel().getEntities())
			if(e instanceof Button) {
				has = true;
				if(!((Button) e).isPressed())
					return false;
			}
		return has;
	}
	
	@Override
	public BufferedImage getSprite() {
		return sprite[isSolid() ? 0 : 1];
	}
	
	@Override
	public void tick() {
		setSolid(!open());
	}

	@Override
	public void render(Graphics2D g) {
		renderTile(Floor.sprite[Floor.index], g);
		renderTile(getSprite(), g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
