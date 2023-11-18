package com.coffee.objects.tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import com.coffee.graphics.Flip;
import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.Directions;
import com.coffee.objects.Variables;
import com.coffee.objects.entity.Entity;
import com.coffee.objects.particles.Dust;

public class Trampoline extends Tile {
	
	private static BufferedImage[] sprites;
	private boolean active;
	private int cartesian = 0;
	
	public Trampoline(int id, int x, int y) {
		super(id, x, y);
		if(sprites == null)
			sprites = getSprite("trampoline", Engine.Color_Primary);
	}
	
	private void setType() {
		int x = this.getMiddle().x / Tile.getSize();
		int y = this.getMiddle().y / Tile.getSize();
		Tile up = Game.getLevel().getTile(x, y - 1);
		Tile down = Game.getLevel().getTile(x, y + 1);
		Tile right = Game.getLevel().getTile(x + 1, y);
		Tile left = Game.getLevel().getTile(x - 1, y);
		if(up.isSolid() && right.isSolid() && !down.isSolid() && !left.isSolid())
			cartesian = 0;
		else if(up.isSolid() && !right.isSolid() && !down.isSolid() && left.isSolid())
			cartesian = 1;
		else if(!up.isSolid() && !right.isSolid() && down.isSolid() && left.isSolid())
			cartesian = 2;
		else if(!up.isSolid() && right.isSolid() && down.isSolid() && !left.isSolid())
			cartesian = 3;
	}
	
	public void tick() {
		setType();
		List<Entity> entities = Game.getLevel().getEntities();
		for(int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if(this.centralizedWith(entity) && entity.getVar(Variables.Alive)) {
				double radians = Math.PI;
				Directions direction = entity.getOE().getDirection();
				if(cartesian == 0) {
					radians = Math.PI + Math.PI/2;
					if(direction.equals(Directions.Up))
						entity.setDirection(Directions.Left);
					else if(direction.equals(Directions.Right))
						entity.setDirection(Directions.Down);
				}else if(cartesian == 1) {
					radians = Math.PI;
					if(direction.equals(Directions.Left))
						entity.setDirection(Directions.Down);
					else if(direction.equals(Directions.Up))
						entity.setDirection(Directions.Right);
				}else if(cartesian == 2) {
					radians = Math.PI/2;
					if(direction.equals(Directions.Down))
						entity.setDirection(Directions.Right);
					else if(direction.equals(Directions.Left))
						entity.setDirection(Directions.Up);
				}else if(cartesian == 3) {
					radians = 0;
					if(direction.equals(Directions.Right))
						entity.setDirection(Directions.Up);
					else if(direction.equals(Directions.Down))
						entity.setDirection(Directions.Left);
				}
				for(int ii = 0; ii < 25; ii++) {
					Game.getLevel().addParticle(new Dust(getMiddle().x, getMiddle().y, radians - (Math.PI/4 + Engine.RAND.nextDouble()*(Math.PI))));
				}
			}
		}
	}
	
	@Override
	public BufferedImage getSprite() {
		BufferedImage sprite = sprites[active ? 1 : 0];
		if(cartesian == 0)
			sprite = Flip.Invert(sprite);
		if(cartesian == 1)
			sprite = Flip.Horizontal(sprite);
		if(cartesian == 3)
			sprite = Flip.Vertical(sprite);
		return sprite;
	}
	
	@Override
	public void render(Graphics2D g) {
		renderTile(Floor.sprite[Floor.index], g);
		super.render(g);
	}

}
