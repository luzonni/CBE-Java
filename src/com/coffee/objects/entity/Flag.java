package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.activity.Game;

public class Flag extends Entity {
	
	private int indexAnim;
	private int counter;
	private static BufferedImage[] sprites;
	
	public Flag(int id, int x, int y) {
		super(id, x, y);
		if(sprites == null)
			sprites = getSprite("Flag", Engine.Color_Primary);
		setFloating(false);
		setDepth(0);
	}
	
	@Override
	public String giveCommand(String[] values) {
		String message = super.giveCommand(values);
		
		return message;
	}

	@Override
	public BufferedImage getSprite() {
		return sprites[indexAnim];
	}
	
	@Override
	public void tick() {
		Player p = Game.getPlayer();
		if(p.collidingWith(this)) 
			Game.end();
		counter++;
		if(counter > 10 ) {
			counter = 0;
			indexAnim++;
			if(indexAnim > sprites.length-1)
				indexAnim = 0;
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderEntity(sprites[indexAnim], g);
	}
	
	@Override
	public void dispose() {
		sprites = null;
	}

}
