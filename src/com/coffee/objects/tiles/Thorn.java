package com.coffee.objects.tiles;

import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.entity.Player;

public class Thorn extends Tile {
	
	private static BufferedImage sprite;

	public Thorn(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("thorn", Engine.Color_Primary)[0];
		this.setSolid(true);
	}
	
	public void tick() {
		Player player = Game.getPlayer();
		if(player.getOE().overTile().equals(this))
			player.die();
	}

	@Override
	public BufferedImage getSprite() {
		return sprite;
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
