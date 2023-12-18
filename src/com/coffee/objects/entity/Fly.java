package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Fly extends Entity {
	
	private static BufferedImage[] sprite;

	public Fly(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("fly");
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[0];
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(Graphics2D g) {
		renderEntity(getSprite(), g);
	}

}
