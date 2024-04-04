package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.command.Commands;
import com.coffee.items.CMD;
import com.coffee.items.Item;
import com.coffee.main.Engine;
import com.coffee.main.activity.Game;

public class Computer extends Entity {
	
	private static BufferedImage[] sprite;
	private int index;
	
	public Computer(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("server", Engine.Color_Primary);
		this.index = Engine.RAND.nextInt(sprite.length);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[index];
	}

	@Override
	public void tick() {
		Player player = Game.getPlayer();
		if(player.collidingWith(this)) {
			Item[] items = player.getInventory().getList();
			for(Item item : items) {
				if(item instanceof CMD) {
					Commands c = ((CMD)item).getCMD();
					Game.getLevel().addKey(c);
					player.getInventory().remove(item);
				}
			}
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
