package com.coffee.objects.particles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.activity.Game;

public class Trail extends Particle {
	
	private static BufferedImage[] sprites;
	private int index;
	private int count;
	
	public Trail(int x, int y) {
		super(x, y);
		if(sprites == null)
			sprites = getSprite("trail", Engine.Color_Primary);
		setSize(sprites[index].getWidth(), sprites[index].getHeight());
		setX(x - getWidth()/2);
		setY(y - getHeight()/2);	
		setDepth(-1);
	}

	@Override
	public BufferedImage getSprite() {
		return sprites[index];
	}
	
	@Override
	public void tick() {
		count++;
		if(count > 4) {
			count = 0;
			index++;
			if(index > sprites.length-1) {
				index = 0;
				Game.getLevel().getParticles().remove(this);
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderParticle(getSprite(), g);
	}

}
