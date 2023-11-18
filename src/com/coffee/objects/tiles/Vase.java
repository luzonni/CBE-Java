package com.coffee.objects.tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.Variables;
import com.coffee.objects.entity.Player;

public class Vase extends Tile {
	
	protected static BufferedImage[] sprite;
	private int durability;
	
	public Vase(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("vase", Engine.Color_Primary);
		setVar(Variables.Selectable, true);
		setVar(Variables.Movable, true);
		this.setSolid(true);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[durability];
	}

	@Override
	public void tick() {
		Player P = Game.getPlayer();
		if(P.getOE().nextTile() == this) {
			durability = 1;
		}else if(durability == 1){
			durability = 2;
		}
		if(durability >= sprite.length-1) {
			this.setSolid(false);
			durability = sprite.length-1;
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		renderTile(Floor.sprite[Floor.index], g);
		renderTile(getSprite(), g);
	}

}
