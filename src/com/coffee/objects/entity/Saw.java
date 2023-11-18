package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.level.Level;
import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.Directions;
import com.coffee.objects.Variables;
import com.coffee.objects.tiles.Tile;

public class Saw extends Entity {

	private static BufferedImage sprite;
	private double rotate;
	private Directions dir;

	public Saw(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("saw", Engine.Color_Primary)[0];
		this.dir = Directions.Idle;
		setDepth(4);
	}


	private void system() {
		Directions[] dirs = {Directions.Down, Directions.Left, Directions.Up, Directions.Right};
		int x = getMiddle().x / Tile.getSize();
		int y = getMiddle().y / Tile.getSize();
		Level level = Game.getLevel();
		Tile tile = level.getTile(x, y);
		if(this.dir.equals(Directions.Idle)) {
			for(int i = 0; i < dirs.length; i++) {
				if(level.getTile((int)tile.getX()/Tile.getSize() + dirs[i].getX(), (int)getY()/Tile.getSize() + dirs[i].getY()).isSolid()) {
					int index = i + 1;
					if(index > dirs.length-1)
						index = 0;
					this.dir = dirs[index];
					this.setX(tile.getX() + (tile.getWidth()/2) * dirs[i].getX());
					this.setY(tile.getY() + (tile.getHeight()/2) * dirs[i].getY());
					break;
				}
			}
		}
		if(tile.isSolid() || tile == null) {
			die();
			return;
		}
		int botton_index = 0;
		int index = 0;
		for(int i = 0; i < dirs.length; i++)
			if(this.dir.equals(dirs[i])) {
				index = i;
				botton_index = i - 1;
				if(botton_index < 0)
					botton_index = dirs.length-1;
			}
		Tile botton_tile = level.getTile((getMiddle().x + dirs[botton_index].getX()*Engine.GameScale) / Tile.getSize(), (getMiddle().y + dirs[botton_index].getY()*Engine.GameScale) / Tile.getSize());
		Tile next_tile = level.getTile((getMiddle().x + dirs[index].getX()*Engine.GameScale) / Tile.getSize(), (getMiddle().y + dirs[index].getY()*Engine.GameScale) / Tile.getSize());
		if(next_tile.isSolid() && botton_tile.isSolid()) {
			int next = index + 1;
			if(next > dirs.length-1)
				next = 0;
			this.dir = dirs[next];
		}else if(!next_tile.isSolid() && !botton_tile.isSolid()) {
			int next = index - 1;
			if(next < 0)
				next = dirs.length-1;
			this.dir = dirs[next];
		}
		moving(dir);
	}

	private void moving(Directions dir) {
		int speed = Engine.GameScale;
		if(dir.equals(Directions.Left)) {
			this.setX(this.getX() - speed);
		}else if(dir.equals(Directions.Up)) {
			this.setY(this.getY() - speed);
		}else if(dir.equals(Directions.Right)) {
			this.setX(this.getX() + speed);
		}else if(dir.equals(Directions.Down)) {
			this.setY(this.getY() + speed);
		}
	}

	@Override
	public BufferedImage getSprite() {
		return sprite;
	}

	@Override
	public void tick() {
		Level level = Game.getLevel();
		system();
		boolean colliding = false;
		for(int i = 0; i < level.getMap().length; i++) {
			Tile t = level.getMap()[i];
			if(this.getBounds().intersects(t.getBounds()) && t.isSolid()) {
				colliding = true;
			}
		}
		if(!colliding) {
			die();
			return;
		}
		for(int i = 0; i < Game.getLevel().getEntities().size(); i++) {
			Entity entity = Game.getLevel().getEntities().get(i);
			if(entity.getVar(Variables.Alive) && entity.collidingWith(this)) {
				entity.die();
			}
		}
		this.rotate -= 0.15;
	}

	@Override
	public void render(Graphics2D g) {
		g.rotate(rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
		renderEntity(sprite, g);
		g.rotate(-rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
	}

}
