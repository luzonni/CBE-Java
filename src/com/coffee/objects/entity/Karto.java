package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.Variables;

public class Karto extends Entity {
	
	private static BufferedImage[][] sprites;
	private int index;
	private boolean actived;
	private int count;
	
	public Karto(int id, int x, int y) {
		super(id, x, y);
		if(sprites == null) {
			sprites = new BufferedImage[2][];
			sprites[0] = getSprite("karto", Engine.Color_Primary, 0);
			sprites[1] = getSprite("karto", Engine.Color_Primary, 1);
		}
		setVar(Variables.Alive, true);
	}

	@Override
	public BufferedImage getSprite() {
		return sprites[actived ? 1 : 0][index];
	}

	@Override
	public void tick() {
		count++;
		if(count > 5) {
			index++;
			count = 0;
			if(index >= sprites[actived ? 1 : 0].length) {
				index = 0;
				if(actived) {
					kabum();
					die();
				}
			}
		}
		if(!actived)
			for(int i = 0; i < Game.getLevel().getEntities().size(); i++) {
				Entity e = Game.getLevel().getEntities().get(i);
				if(this.collidingWith(e) && e != this) {
					index = 0;
					actived = true;
				}
			}
	}
	
	private void kabum() {
		int length = 64;
		for(int i = 0; i < length; i++) {
			double angle = Math.toRadians((360d/(double)length)*i);
			Game.getLevel().addEntity(new Miniboll(0, getMiddle().x, getMiddle().y, angle));
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderEntity(getSprite(), g);
	}
	
	@Override
	public void dispose() {
		sprites = null;
	}

}
