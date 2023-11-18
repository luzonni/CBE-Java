package com.coffee.creator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.coffee.command.Commands;
import com.coffee.main.Engine;
import com.coffee.objects.Objects;
import com.coffee.objects.entity.Entity;
import com.coffee.objects.entity.EntityItem;
import com.coffee.objects.tiles.Tile;

public class Saver {
	
	private String name;
	private String builder;
	private Commands[] commands;
	private Objects[] map;
	private Objects[] entities;
	private int[] picture;
	private int width;
	private int height;

	public Saver(String Name, String builder_name, Commands[] Commands, Objects[] MAP, Objects[] Entities, int[] PICTURE, int Width, int Height) {
		this.name = Name;
		this.builder = builder_name;
		this.commands = Commands;
		this.map = MAP;
		this.entities = Entities;
		this.picture = PICTURE;
		this.width = Width;
		this.height = Height;
	}
	
	public JSONObject create() {
		JSONObject object = new JSONObject();
		object.put("NAME", this.name);
		object.put("BUILDER", this.builder);
		object.put("WIDTH", this.width);
		object.put("HEIGHT", this.height);
		JSONArray comman = new JSONArray();
		for(int i = 0; i < commands.length; i++) {
			if(commands[i] == null)
				comman.add(0);
			else
				comman.add(commands[i].name());
		}
		object.put("COMMANDS", comman);
		
		
		
		JSONArray array_tile = new JSONArray();
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int index = x+y*width;
				Tile t = (Tile) map[index];
				JSONObject tile = new JSONObject();
				tile.put("ID", t != null ? t.ID() : 0);
				tile.put("X", x);
				tile.put("Y", y);
				array_tile.add(tile);
			}
		}
		object.put("TILE", array_tile);
		
		
		
		JSONArray entity_array = new JSONArray();
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int index = x+y*width;
				Entity e = (Entity) entities[index];
				if(e != null) {
					JSONObject entity = new JSONObject();
					entity.put("TYPE", e instanceof EntityItem ? "I" : "E");
					entity.put("ID", e.ID());
					entity.put("X", x);
					entity.put("Y", y);
					entity_array.add(entity);
				}
			}
		}
		object.put("ENTITY", entity_array);
		
		
		
		JSONArray pic = new JSONArray();
		for(int i = 0; i < picture.length; i++) {
			if(picture[i] == Engine.Color_Primary.getRGB())
				pic.add(0);
			else if(picture[i] == Engine.Color_Secondary.getRGB())
				pic.add(1);
			else if(picture[i] == Engine.Color_Tertiary.getRGB())
				pic.add(2);
		}
		object.put("PICTURE", pic);
		return object;
	}
	
	public boolean save(File local, JSONObject object) {
		try {
			String l = local + "/" + name.strip().replace(' ', '_').toLowerCase() + ".json";
			FileWriter writer = new FileWriter(l);
			writer.write(object.toJSONString());
			writer.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
}
