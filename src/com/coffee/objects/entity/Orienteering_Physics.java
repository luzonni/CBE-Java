package com.coffee.objects.entity;

import java.awt.Rectangle;

import com.coffee.level.Level;
import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.Directions;
import com.coffee.objects.tiles.Tile;

public class Orienteering_Physics {

	private Directions direction;
	private Entity entity;

	public Orienteering_Physics(Entity entity) {
		this.entity = entity;
		this.direction = Directions.Idle;
	}
	
	public Directions getDirection() {
		return this.direction;
	} 

	public void setDirection(Directions newdir) {
		this.direction = newdir;
	}
	
	public boolean colliding(Directions dir) {
		boolean colliding = false;
		Level L = Game.getLevel();
		int map_Width = Game.getLevel().getBounds().width / Tile.getSize();
		int x = (entity.getMiddle().x / Tile.getSize());
		int y = (entity.getMiddle().y / Tile.getSize());
		switch(dir) {
			case Up:
				y--;
				Tile tile = L.getTile(x+y*map_Width);
				if(tile == null || outOfMapBounds()) 
					break;
				if(tile.isSolid()) {
					colliding = true;
					while(!L.getMap()[x+(((int)entity.getY() - 1) / Tile.getSize())*map_Width].isSolid()) {
						entity.setY(entity.getY() - 1);
					}
				}
				break;
			case Right:
				x++;
				tile = L.getTile(x+y*map_Width);
				if(tile == null || outOfMapBounds()) 
					break;
				if(tile.isSolid()) {
					colliding = true;
					while(!L.getMap()[(((int)entity.getX() + entity.getWidth()) / Tile.getSize())+y*map_Width].isSolid()) {
						entity.setX(entity.getX() + 1);
					}
				}
				break;
			case Down:
				y++;
				tile = L.getTile(x+y*map_Width);
				if(tile == null || outOfMapBounds()) 
					break;
				if(tile.isSolid()) {
					colliding = true;
					while(!L.getMap()[x+(((int)entity.getY() + entity.getHeight()) / Tile.getSize())*map_Width].isSolid()) {
						entity.setY(entity.getY() + 1);
					}
				}
				break;
			case Left:
				x--;
				tile = L.getTile(x+y*map_Width);
				if(tile == null || outOfMapBounds()) 
					break;
				if(L.getMap()[x+y*map_Width].isSolid()) {
					colliding = true;
					while(!L.getMap()[(((int)entity.getX() - 1) / Tile.getSize())+y*map_Width].isSolid()) {
						entity.setX(entity.getX() - 1);
					}
				}
				break;
			default:
				break;
		}
		return colliding;
	}
	
	private boolean outOfMapBounds() {
		Rectangle rec = Game.getLevel().getBounds();
		return !rec.contains(entity.getMiddle());
	} 
	
	public Tile nextTile() {
		Level level = Game.getLevel();
		Tile tile = null;
		int x = (entity.getMiddle().x) / Tile.getSize();
		int y = (entity.getMiddle().y) / Tile.getSize();
		switch(entity.getDirection()) {
			case Up:
				y--;
				tile = level.getTile(x, y);
				if(tile == null) 
					break;
				return tile;
			case Right:
				x++;
				tile = level.getTile(x, y);
				if(tile == null) 
					break;
				return tile;
			case Down:
				y++;
				tile = level.getTile(x, y);
				if(tile == null) 
					break;
				return tile;
			case Left:
				x--;
				tile = level.getTile(x, y);
				if(tile == null) 
					break;
				return tile;
			default:
				break;
		}
		return tile;
	}
	
	public Tile overTile() {
		Level level = Game.getLevel();
		Tile tile_over = null;
		int x = entity.getMiddle().x / Tile.getSize();
		int y = entity.getMiddle().y / Tile.getSize();
		switch(entity.getDirection()) {
			case Up:
				y = (entity.getMiddle().y - entity.getHeight()/2 - Engine.GameScale)  / Tile.getSize();
				tile_over = level.getTile(x, y);
				if(tile_over == null) 
					break;
				return tile_over;
			case Right:
				x = (entity.getMiddle().x + entity.getWidth()/2 + Engine.GameScale) / Tile.getSize();
				tile_over = level.getTile(x, y);
				if(tile_over == null) 
					break;
				return tile_over;
			case Down:
				y = (entity.getMiddle().y + entity.getHeight()/2 + Engine.GameScale) / Tile.getSize();
				tile_over = level.getTile(x, y);
				if(tile_over == null) 
					break;
				return tile_over;
			case Left:
				x = (entity.getMiddle().x - entity.getWidth()/2 - Engine.GameScale) / Tile.getSize();
				tile_over = level.getTile(x, y);
				if(tile_over == null) 
					break;
				return tile_over;
			default:
				break;
		}
		return tile_over;
	}
	
}
