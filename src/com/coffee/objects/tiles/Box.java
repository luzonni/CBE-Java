package com.coffee.objects.tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.Sound;
import com.coffee.main.activity.Game;
import com.coffee.main.tools.Timer;
import com.coffee.objects.entity.Entity;

public class Box extends Tile {

	private Timer timer;
	private static BufferedImage[][] sprite;

	public Box(int id, int x, int y) {
		super(id, x, y);
		timer = new Timer(1);
		if (sprite == null) {
			sprite = new BufferedImage[2][2];
			sprite[0] = getSprite("box", Engine.Color_Primary, 0);
			sprite[1] = getSprite("box", Engine.Color_Primary, 1);
		}
		this.setSolid(false);
	}

	public void change(boolean bool) {
		if(this.isSolid() != bool) {
			Sound.play("poft");
			this.setSolid(bool);
		}
	}
	
	@Override
	public BufferedImage getSprite() {
		return sprite[0][0];
	}

	@Override
	public void tick() {
		if (!this.isSolid()) {
			for(Entity e : Game.getLevel().getEntities())
				if(this.centralizedWith(e))
					change(true);
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderTile(Floor.sprite[Floor.index], g);
		renderTile(sprite[this.isSolid() ? 0 : 1][timer.pit() ? 0 : 1], g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}
	
}
