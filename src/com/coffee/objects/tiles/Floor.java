package com.coffee.objects.tiles;

import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.objects.Variables;

public class Floor extends Tile {
	
	protected static BufferedImage[] sprite;
	protected static int index;
	
	public Floor(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null) {
			sprite = getSprite("floor", Engine.Color_Secondary);
			index = Engine.RAND.nextInt(sprite.length);
		}
		this.setSolid(false);
		setVar(Variables.Movable, true);
	}
	
	@Override
	public BufferedImage getSprite() {
		return sprite[index];
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
