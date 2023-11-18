package com.coffee.items;

import com.coffee.main.activity.Game;
import com.coffee.objects.entity.Entity;
import com.coffee.objects.tiles.Tile;

public class Placeable extends Item {

	private int entity_id;

	public Placeable(int entity_id) {
		super(Entity.Factory(entity_id, 0, 0).getClass().getSimpleName());
		setSprite(Entity.Factory(entity_id, 0, 0).getClass().getSimpleName());
		this.entity_id = entity_id;
	}
	
	public boolean place(String[] values) {
		String name = values[1];
		int x = (int)Game.getPlayer().getX() + Integer.parseInt(values[2])*Tile.getSize();
		int y = (int)Game.getPlayer().getY() + Integer.parseInt(values[3])*Tile.getSize();
		Entity entity = Entity.Factory(entity_id, x, y);
		if(entity == null || !entity.getClass().getSimpleName().equalsIgnoreCase(name))
			return false;
		Game.getLevel().addEntity(entity);
		Game.getPlayer().getInventory().remove(this);
		return true;
	}

}
