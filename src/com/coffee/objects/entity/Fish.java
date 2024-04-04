package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.graphics.Flip;
import com.coffee.main.activity.Game;
import com.coffee.objects.tiles.Tile;

public class Fish extends Entity {
	
	private static BufferedImage[] sprite;
	private int indexAnim, counter;
	private int side = -1;
	private double speed;
	
	public Fish(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("fish");
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[0];
	}

	@Override
	public void tick() {
		counter++;
		if(counter > 10 ) {
			counter = 0;
			indexAnim++;
			if(indexAnim == 1)
				speed = 5;
			if(indexAnim > sprite.length-1)
				indexAnim = 0;
		}
		swim();
		if(Game.getPlayer().collidingWith(this))
			Game.getPlayer().die();
	}
	
	private void swim() {
		int x = (getMiddle().x + side*getWidth()/2) / Tile.getSize();
		int y = (getMiddle().y) / Tile.getSize();
		Tile t = Game.getLevel().getTile(x, y);
		if(t != null && t.isSolid()) {
			speed = 0;
			side *=-1;
			indexAnim = 0;
		}
		double currentSpeed = side * speed;
		setX(getX() + currentSpeed);
		if(speed > 0)
			speed -= 0.1;
	}

	@Override
	public void render(Graphics2D g) {
		BufferedImage currentSprite = sprite[indexAnim];
		if(side > 0)
			currentSprite = Flip.Vertical(currentSprite);
		renderEntity(currentSprite, g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
