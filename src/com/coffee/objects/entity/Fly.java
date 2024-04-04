package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.coffee.main.Engine;
import com.coffee.main.Geometry;
import com.coffee.main.activity.Game;
import com.coffee.main.tools.Timer;
import com.coffee.objects.Variables;
import com.coffee.objects.ai.Path;
import com.coffee.objects.particles.Interrogation;
import com.coffee.objects.tiles.Tile;

public class Fly extends Entity {
	
	private Path ai;
	private Timer timer;
	private List<Tile> tiles_no_acess;
	
	private int tt, indexAnim;
	private static BufferedImage[] sprite;

	public Fly(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("fly");
		ai = new Path();
		tiles_no_acess = new ArrayList<Tile>();
		timer = new Timer(1);
		setDepth(1);
		setVar(Variables.Alive, true);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[0];
	}

	@Override
	public void tick() {
		if(!ai.follow(ai.direction())) {
			indexAnim = 0;
			if(timer.tiktak()) {
				Tile end_point_tile = getFarTile();
				if(Game.getLevel().getTile((int)(getMiddle().x/Engine.GameScale), (int)(getMiddle().y/Engine.GameScale)) != end_point_tile) {
					Game.getLevel().addParticle(new Interrogation(end_point_tile.getMiddle().x, end_point_tile.getMiddle().y));
					ai.buildPath(this, end_point_tile.getMiddle(), 20);
					if(!ai.follow(ai.direction())) {
						//TODO problema em tiles não acessiveis
						tiles_no_acess.add(end_point_tile);
					}
				}
			}
		}else {
			tt++;
			if(tt > 5) {
				tt = 0;
				indexAnim++;
			}	
			if(indexAnim > sprite.length-1)
				indexAnim = 1;
		}
		
	}
	
	private Tile getFarTile() {
		Tile[] tiles = Game.getLevel().getMap();
		Tile far_tile = null;
		for(int i = 0; i < tiles.length; i++) {
			Tile tile = tiles[i];
			if(!tile.isSolid()) {
				if(far_tile == null) {
					far_tile = tile;
					continue;
				}
				double current_dis = Geometry.Theta(tile.getMiddle(), Game.getPlayer().getMiddle());
				double far_tile_dis = Geometry.Theta(far_tile.getMiddle(), Game.getPlayer().getMiddle());
				if(current_dis >= far_tile_dis && !tiles_no_acess.contains(tile)) {
					far_tile = tile;
				}
			}
		}
		return far_tile;
	}

	@Override
	public void render(Graphics2D g) {
		ai.render(g);
		renderEntity(sprite[indexAnim], g);
	}

}
