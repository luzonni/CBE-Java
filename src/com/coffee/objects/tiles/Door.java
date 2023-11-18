package com.coffee.objects.tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.items.Item;
import com.coffee.items.Key;
import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.entity.Player;

public class Door extends Tile {

	private static BufferedImage[] sprite;
	
	public Door(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null) {
			sprite = getSprite("door", Engine.Color_Primary);
		}
		this.setSolid(true);
	}
	
	public void open() {
		this.setSolid(false);
	}
	
	@Override
	public BufferedImage getSprite() {
		return sprite[isSolid() ? 0 : 1];
	}
	
	@Override
	public void tick() {
		Player p = Game.getPlayer();
		if(this.isSolid() && p.getOE().nextTile() == this) 
			for(Item item : p.getInventory().getList())
				if(item instanceof Key) {
					p.getInventory().remove(item);
					open();
				}
	}

	@Override
	public void render(Graphics2D g) {
		renderTile(Floor.sprite[Floor.index], g);
		renderTile(getSprite(), g);

	}

}
