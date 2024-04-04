package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.Sound;
import com.coffee.main.activity.Game;

public class Button extends Entity {
	
	private static BufferedImage[] sprite;
	private boolean pressed;
	private int timer;

	public Button(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("button", Engine.Color_Primary);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[pressed ? 1 : 0];
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	private void click(boolean bool) {
		if(this.pressed != bool) {
			Sound.play("poft");
			this.pressed = bool;
		}
	}
	
	@Override
	public void tick() {
		for(Entity e : Game.getLevel().getEntities())
			if(e != this && e.collidingWith(this)) {
				click(true);
				return;
			}
		
		if(pressed && timer++ > 10) {
			timer = 0;
			click(false);
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		renderEntity(getSprite(), g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
