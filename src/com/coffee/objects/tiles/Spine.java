package com.coffee.objects.tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.entity.Player;

public class Spine extends Tile {

	private static BufferedImage[] sprite;
	private int indexAnim;
	private boolean actived = false;
	private int timer;
	
	public Spine(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = this.getSprite("spine", Engine.Color_Primary);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[indexAnim];
	}

	@Override
	public void tick() {
		Player p = Game.getPlayer();
		if(this.centralizedWith(p)) {
			actived = true;
			if(indexAnim == sprite.length-1) {
				p.die();
			}
		}
		if(actived) {
			timer++;
			if(timer > 5) {
				timer = 0;
				indexAnim++;
			}
			if(indexAnim >= sprite.length-1) {
				indexAnim = sprite.length-1;
				
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderTile(Floor.sprite[Floor.index], g);
		renderTile(getSprite(), g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
