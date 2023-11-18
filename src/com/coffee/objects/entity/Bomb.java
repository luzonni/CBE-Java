package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.Geometry;
import com.coffee.main.Sound;
import com.coffee.main.activity.Game;
import com.coffee.objects.Variables;
import com.coffee.objects.particles.Dust;
import com.coffee.objects.particles.Kabum;
import com.coffee.objects.tiles.Tile;

public class Bomb extends Entity {

	private static BufferedImage[] sprites;
	private int[][] positions = {{-5, -5}, {-4, -6}, {-3, -6}, {-2, -7}, {-1, -7}, {0, -6}, {0, -6}, {0, -6}, {0, -6}};
	private double radius;
	private boolean activity;
	private int index;
	private int count;
	
	
	public Bomb(int id, int x, int y) {
		super(id, x, y);
		if(sprites == null) {
			sprites = getSprite("bomb", Engine.Color_Primary);
		}
		radius = Tile.getSize()*2.5;
	}

	@Override
	public BufferedImage getSprite() {
		return sprites[index];
	}

	@Override
	public void tick() {
		Player player = Game.getPlayer();
		if(Geometry.Theta(player.getMiddle(), getMiddle()) <= radius) {
			activity = true;
			Sound.loop("sss");
		}
		if(activity) {
			count++;
			Game.getLevel().addParticle(new Dust(getMiddle().x + positions[index][0]*Engine.GameScale, getMiddle().y + positions[index][1]*Engine.GameScale, -Math.PI/2));
			if(count > 15) {
				count = 0;
				index++;
				if(index == sprites.length-1) {
					Sound.stop("sss");
					explode();
				}
			}
		}
	}
	
	private void explode() {
		Entity[] entities = Game.getLevel().getEntities().toArray(new Entity[0]);
		for(int i = 0; i < entities.length; i++) {
			Entity entity = entities[i];
			if(Geometry.Theta(entity.getMiddle(), getMiddle()) <= radius && entity.getVar(Variables.Alive)) {
				entity.die();
			}
		}
		for(int i = 0; i < 1000; i++) {
			Point point = new Point((int)(getMiddle().x - radius) + Engine.RAND.nextInt((int)radius*2), (int)(getMiddle().y - radius) + Engine.RAND.nextInt((int)radius*2));
			if(Geometry.Theta(getMiddle(), point) <= radius) {
				Game.getLevel().addParticle(new Kabum(point.x, point.y));
			}
		}
		Game.getLevel().getEntities().remove(this);
		Sound.play("kabum");
	}

	@Override
	public void render(Graphics2D g) {
		renderEntity(getSprite(), g);
	}

}
