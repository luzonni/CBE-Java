package com.coffee.objects.tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.objects.Variables;

public class Crate extends Tile {
	
	private static BufferedImage[] sprite;
	private static int index;
	
	public Crate(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null) {
			sprite = this.getSprite("crate", Engine.Color_Primary);
			index = Engine.RAND.nextInt(sprite.length);
		}
		this.setSolid(true);
		setVar(Variables.Selectable, true);
		setVar(Variables.Movable, true);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[index];
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics2D g) {
		renderTile(Floor.sprite[Floor.index], g);
		renderTile(getSprite(), g);
	}

}
