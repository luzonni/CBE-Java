package com.coffee.objects.tiles;

import java.awt.image.BufferedImage;

import com.coffee.objects.Variables;

public class Air extends Tile {
	
	
	public Air(int id, int x, int y) {
		super(id, x, y);
		this.setVar(Variables.Movable, true);
		this.setSolid(false);
	}
	
	@Override
	public BufferedImage getSprite() {
		return null;
	}
	
}
