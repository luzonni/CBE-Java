package com.coffee.main.activity;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.coffee.Inputs.Button.Button;
import com.coffee.Inputs.Button.TextButton;
import com.coffee.command.Commands;
import com.coffee.creator.Commands_Boxe;
import com.coffee.creator.DrawableBox;
import com.coffee.creator.Grid;
import com.coffee.creator.Inventory;
import com.coffee.creator.Saver;
import com.coffee.creator.Selected;
import com.coffee.graphics.FontG;
import com.coffee.items.Item;
import com.coffee.level.Level;
import com.coffee.main.Engine;
import com.coffee.main.tools.Responsive;
import com.coffee.objects.Camera;
import com.coffee.objects.Objects;
import com.coffee.objects.entity.Entity;
import com.coffee.objects.entity.Flag;
import com.coffee.objects.entity.Player;
import com.coffee.objects.tiles.Air;
import com.coffee.objects.tiles.Tile;

public class Creator implements Activity {
	
	public static String PATH = Engine.ResPath + "/levels/";
	private static Responsive center;
	
	private TextButton name_sublime;
	private TextButton name_builder;;
	private TextButton[] sizes;
	private Button create_button;
	private static Selected Selected;
	private Inventory inventoryTiles;
	private Inventory inventoryEntities;
	private Inventory inventoryItems;
	private Commands_Boxe c_b;
	private DrawableBox picture;
	
	private Camera camera;
	
	private JSONObject level;
	private String NAME;
	private String BUILDER;
	private List<Commands> COMMANDS;
	private Grid MAP_TILES; 
	private Grid MAP_ENTITIES;
	private int WIDTH, HEIGHT;
	
	public Creator(JSONObject level) {
		this.level = level;
		this.camera = new Camera();
		this.camera.start();
	}
	
	private void buildInventoryTiles(int size) {
		List<Tile> tiles = new ArrayList<Tile>();
		int index = 1;
		while(true) {
			try {
				tiles.add(Tile.Factory(index, 0, 0));
			}catch (Exception e) {
				break;
			}
			index++;
		}
		Responsive res = Responsive.createPoint(Engine.UI.getMenuPosition(), -1, 5*Engine.GameScale);
		inventoryTiles = new Inventory(tiles.toArray(new Tile[0]), res, 1, 1, size);
	}
	
	private void buildInventoryEntities(int size) {
		List<Entity> entities = new ArrayList<Entity>();
		int index = 1;
		while(true) {
			try {
				Entity E = Entity.Factory(index, 0, 0);
				entities.add(E);
			}catch (Exception e) {
				break;
			}
			index++;
		}
		inventoryEntities = new Inventory(entities.toArray(new Entity[0]), inventoryTiles.getResponsive(), 4*Engine.GameScale, 0, size);
	}
	
	private void buildInventoryItems(int size) {
		List<Entity> entities = new ArrayList<Entity>();
		int index = 1;
		while(true) {
			try {
				Entity E = Item.Factory(index, 0, 0);
				entities.add(E);
			}catch (Exception e) {
				break;
			}
			index++;
		}
		inventoryItems = new Inventory(entities.toArray(new Entity[0]), inventoryEntities.getResponsive(), 4*Engine.GameScale, 0, size);
	}
	
	public static Camera getCam() {
		Activity activity = Engine.ACTIVITY;
		if(activity instanceof Creator)
			return ((Creator) activity).camera;
		throw new RuntimeException("Not in Creator");
	}
	
	@Override
	public void enter() {
		Engine.UI.addOption("Back", () -> {
			Engine.setActivity(new Menu());
		});
		Creator.getCam().setPosition(0, 0);
		Selected = new Selected();
		center = Responsive.createPoint(null, 50, 50);
		buildInventoryTiles(10);
		buildInventoryEntities(10);
		buildInventoryItems(5);
		if(level != null) {
			loader();
			addButtonsMenu();
		}else {
			sizes = new TextButton[2];
			sizes[0] = new TextButton("Width", -10*Engine.GameScale, 0, center, 8);
			sizes[1] = new TextButton("Height", 10*Engine.GameScale, 0, center, 8);
			name_sublime = new TextButton("level name", 0, -12*Engine.GameScale, center, 8);
			name_builder = new TextButton("your nick or @", 0, -6*Engine.GameScale, name_sublime.getResponsive(), 8);
			create_button = new Button("create", 0, 12*Engine.GameScale, center, 8);
			c_b = new Commands_Boxe(null);
		}
	}
	
	private void loader() {
		this.NAME = (String)level.get("NAME");
		this.BUILDER = (String)level.get("BUILDER");
		this.WIDTH = ((Number)level.get("WIDTH")).intValue();
		this.HEIGHT = ((Number)level.get("HEIGHT")).intValue();
		
		JSONArray tiles = (JSONArray) level.get("TILE");
		Tile[] array_tile = new Tile[this.WIDTH * this.HEIGHT];
		for(int i = 0; i < array_tile.length; i++) {
			JSONObject tile = (JSONObject) tiles.get(i);
			int id = ((Number)tile.get("ID")).intValue();
			Tile t = Tile.Factory(id, 0, 0);
			if(t instanceof Air)
				continue;
			array_tile[i] = t;
		}
		MAP_TILES = new Grid(array_tile, this.WIDTH, this.HEIGHT);
		
		
		JSONArray entities = (JSONArray) level.get("ENTITY");
		Entity[] array_item = new Entity[this.WIDTH * this.HEIGHT];
		for(int i = 0; i < entities.size(); i++) {
			JSONObject entity = (JSONObject) entities.get(i);
			String type = (String)entity.get("TYPE");
			int id = ((Number)entity.get("ID")).intValue();
			int x = ((Number)entity.get("X")).intValue();
			int y = ((Number)entity.get("Y")).intValue();
			if(type.equals("E"))
				array_item[x+y*this.WIDTH] = Entity.Factory(id, x, y);
			else if(type.equals("I"))
				array_item[x+y*this.WIDTH] = Item.Factory(id, x, y);
		}
		MAP_ENTITIES = new Grid(array_item, this.WIDTH, this.HEIGHT);
		
		JSONArray commands = (JSONArray) level.get("COMMANDS");
		COMMANDS = new ArrayList<Commands>();
		for(int i = 0; i < commands.size(); i++) {
			String name = (String)commands.get(i);
			COMMANDS.add(Commands.valueOf(name));
		}
		c_b = new Commands_Boxe(COMMANDS);
		
		picture = new DrawableBox(center, new Rectangle(this.WIDTH, this.HEIGHT));
		JSONArray list_pixels = (JSONArray)level.get("PICTURE");
		int[] pixels = new int[list_pixels.size()];
		for(int i = 0; i < list_pixels.size(); i++)
			pixels[i] = ((Number)list_pixels.get(i)).intValue();
		picture.setPixels(DrawableBox.convertPixels(pixels));
	}
	
	@Override
	public String giveCommand(String[] keys) {
		String message = "";
		if(keys[0].equalsIgnoreCase("load")) {
			String level_name = "";
			for(int i = 1; i < keys.length; i++)
				level_name += keys[i] + " ";
			level_name = level_name.strip().replace(" ", "_").replace("\"", "").toLowerCase().strip();
			File dir = new File(System.getProperty("user.dir")+"/"+level_name+".json");
			if(dir.exists() && Level.isLevel(dir)) {
				JSONObject level = Level.getLevel(dir);
				Engine.setActivity(new Creator(level));
			}else {
				message = "Level not found";
			}
		}
		return message;
	}
	
	private void getLevel() {
		if(sizes == null)
			return;
		char[] cs = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		NAME = name_sublime.readText(null);
		BUILDER = name_builder.readText(null);
		String string_w = sizes[0].readText(cs);
		String string_h = sizes[1].readText(cs);
		if(string_w == "" || string_h == "")
			return;
		int w = Integer.parseInt(string_w);
		int h = Integer.parseInt(string_h);
		if(w < 3 || h < 3 || NAME == "" || w > 48 || h > 48)
			return;
		if(create_button.function()) {
			addButtonsMenu();
			MAP_TILES = new Grid(new Tile[w*h], w, h);
			MAP_ENTITIES = new Grid(new Entity[w*h], w, h);
			picture = new DrawableBox(center, new Rectangle(w, h));
			WIDTH = w;
			HEIGHT = h;
			sizes = null;
		}
	}
	
	public static Responsive getCenter() {
		return Creator.center;
	}
	
	public static Objects getSelected() {
		return Creator.Selected.get();
	}

	@Override
	public void tick() {
		Selected.tick();
		getLevel();
		grids();
		if(picture != null && !picture.isDrawing()) {
			Tile t = (Tile) inventoryTiles.getItem();
			if(t != null)
				Selected.set(t);
			Entity e = (Entity) inventoryEntities.getItem();
			if(e != null)
				Selected.set(e);
			Entity i = (Entity) inventoryItems.getItem();
			if(i != null)
				Selected.set(i);
			this.COMMANDS = c_b.tick();
		}
	}
	
	private void grids() {
		if(MAP_TILES == null || MAP_ENTITIES == null)
			return;
		MAP_TILES.tick();
		MAP_ENTITIES.tick();
		if(picture != null && !picture.isDrawing()) {
			if(getSelected() instanceof Tile) {
				MAP_TILES.setGrid(getSelected());
			}
			if(getSelected() instanceof Entity) {
				Entity entity = (Entity) getSelected();
				boolean can_place = true;
				if(entity instanceof Player)
					for(Entity e : (Entity[])MAP_ENTITIES.getArray())
						if((e instanceof Player))
							can_place = false;
				if(entity instanceof Flag)
					for(Entity e : (Entity[])MAP_ENTITIES.getArray())
						if((e instanceof Flag))
							can_place = false;
				if(can_place)
					MAP_ENTITIES.setGrid(getSelected());
			}
			MAP_ENTITIES.clearGrid();
			MAP_TILES.clearGrid();
		}
	}
	
	private void testeAndSaveLevel() {
		Saver saver = new Saver(NAME, BUILDER, COMMANDS.toArray(new Commands[0]), MAP_TILES.getArray(), MAP_ENTITIES.getArray(), picture.getPixels(), WIDTH, HEIGHT);
		JSONObject level_file = saver.create();
		Engine.setActivity(new Game(new Level(level_file), () -> {
			File curPath = new File(System.getProperty("user.dir"));
			boolean wasSaved = saver.save(curPath, level_file);
			if(wasSaved)
				Engine.UI.getConsole().print("Saved in directory: " + curPath.getAbsolutePath(), true);
			else
				Engine.UI.getConsole().print("Unable to save your level", true);
			Engine.setActivity(new Creator(level_file));
		}, new Creator(level_file)));
	}
	
	private void addButtonsMenu() {
		Engine.UI.addOption("new", () -> {
			Engine.setActivity(new Creator(null));
		});
		Engine.UI.addOption("draw", ()-> {
			picture.setDrawnable(!picture.isDrawing());
		});
		Engine.UI.addOption("try", () -> {
			testeAndSaveLevel();
		});
	}
	
	@Override
	public void render(Graphics2D g) {
		if(picture != null)
			picture.render(g);
		renderLCreator(g);
		if(MAP_TILES != null)
			MAP_TILES.render(g, true);
		if(MAP_ENTITIES != null)
			MAP_ENTITIES.render(g, false);
		if(picture != null && !picture.isDrawing()) {
			inventoryTiles.render(g);
			inventoryEntities.render(g);
			inventoryItems.render(g);
			if(inventoryTiles.itemOnSlots(Selected.get()) || inventoryEntities.itemOnSlots(Selected.get()) || inventoryItems.itemOnSlots(Selected.get()))
				Selected.render(g);
			c_b.render(g);
		}
	}
	
	private void renderLCreator(Graphics2D g) {
		if(sizes == null)
			return;
		for(TextButton t : sizes)
			t.render(g);
		name_sublime.render(g);
		name_builder.render(g);
		create_button.render(g);
		String value = "/";
		Font f = FontG.font(12*Engine.GameScale);
		g.setColor(Engine.Color_Primary);
		g.setFont(f);
		int x = Engine.getWidth()/2 - FontG.getWidth(value, f)/2 + Engine.GameScale;
		int y = Engine.getHeight()/2 + FontG.getHeight(value, f)/2;
		g.drawString(value, x, y);
	}
	
	public void dispose() {
		Engine.UI.clearOptions();
		if(this.picture != null)
			this.picture.stop();
		if(this.camera != null)
			this.camera.stop();
	}
	
}
