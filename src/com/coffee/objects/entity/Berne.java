package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;

public class Berne extends Entity {
	
	private int timer;
	private int indexAnim;
	
	private static BufferedImage[] sprite;
	
	public Berne(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("berne");
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[0];
	}

	@Override
	public void tick() {
		timer++;
		if(timer > 15) {
			timer = 0;
			indexAnim++;
			this.setX(this.getX() + Engine.GameScale);
		}
		if(indexAnim > sprite.length-1) {
			indexAnim = 0;
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderEntity(sprite[indexAnim], g);
	}

}
