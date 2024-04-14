package com.coffee.main.activity;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.coffee.Inputs.Mouse;
import com.coffee.Inputs.Mouse_Button;
import com.coffee.command.Receiver;
import com.coffee.level.Level;
import com.coffee.main.Engine;
import com.coffee.main.tools.Geometry;

public class LevelMap implements Activity, Receiver {
	
	private VolatileLevel holding;
	
	public static VolatileLevel[] levels;
	
	public LevelMap() {
		int amount = Engine.LEVELS.length;
		levels = new VolatileLevel[amount];
		for(int i = 0; i < amount; i++) {
			levels[i] = new VolatileLevel(new Level(Engine.LEVELS[i]));
		}
		for(int i = 0; i < Engine.INDEX_LEVEL+1; i++) {
			if(i < Engine.INDEX_LEVEL+1)
				levels[i].setBlocked(false);
		}
	}
	
	@Override
	public void enter() {
		Engine.UI.addOption("Back", () -> {
			Engine.setActivity(new Menu());
		});
	}
	
	@Override
	public String giveCommand(String[] values) {
		String message = "Nothing changed";
		if(values[0].equalsIgnoreCase("Clear") && values.length == 1) {
			List<VolatileLevel> newList = new ArrayList<VolatileLevel>();
			for(int i = 0; i < levels.length; i++) {
				VolatileLevel vl = levels[i];
				if(!vl.isBlocked()) {
					newList.add(vl);
					continue;
				}else {
					newList.add(vl);
					break;
				}
			}
			levels = newList.toArray(new VolatileLevel[0]);
			message = "clean";
		}else if(values[0].equalsIgnoreCase("help")) {
			message = "If you want to load a world that you created, just leave the file in the same place as the executable and write here \"load\" and the name of your level";
		}else if(values[0].equalsIgnoreCase("Refresh") && values.length == 1) {
			for(int i = 0; i <= levels.length; i++)
				levels[i] = new VolatileLevel(levels[i].getLevel());
			levels[0].setBlocked(false);
			levels[1].setBlocked(false);
			levels[2].setBlocked(false);
			message = "Updated";
		}else if(values[0].equals("load")) {
			String level_name = "";
			for(int i = 1; i < values.length; i++)
				level_name += values[i] + " ";
			level_name = level_name.strip().replace(" ", "_").replace("\"", "").toLowerCase().strip();
			File dir = new File(System.getProperty("user.dir")+"/"+level_name+".json");
			if(dir.exists() && Level.isLevel(dir)) {
				Engine.setActivity(new Game(new Level(dir), () -> {
					Engine.setActivity(new LevelMap());
					Engine.UI.addOption("Back", () -> {
						
					});
				}, this));
				message = "Level loaded";
			}else {
				message = "Level does not exist in current directory";
			}
		}
		return message;
	}
	
	@Override
	public void tick() {
		for(int i = 0; i < levels.length; i++) {
			VolatileLevel vl = levels[i];
			if(vl.selected())
				Game.start(vl.getLevel());
			Rectangle rec = vl.getBounds();
			int[] f;
			if(i < levels.length-1) {
				f = follow(levels[i], levels[i+1]);
			}else {
				f = new int[] {0, 0};
			}
			int nextX = (Engine.RAND.nextInt(3) - 1) + f[0];
			int nextY = (Engine.RAND.nextInt(3) - 1) + f[1];
			rec.setBounds(rec.x + nextX, rec.y + nextY, rec.width, rec.height);
			onScreen(rec);
			if(holding != null)
				holding.getBounds().setLocation(Mouse.getX() - rec.width/2, Mouse.getY() - rec.height/2);
		}
		holding();
	}
	
	private void holding() {
		for(int i = 0; i < levels.length; i++) {
			VolatileLevel vl = levels[i];
			if(Mouse.pressingOn(Mouse_Button.RIGHT, vl.getBounds())) {
				holding = vl;
				break;
			}else {
				holding = null;
			}
		}
	}
	
	private void onScreen(Rectangle bounds) {
		if(bounds.x < 0)
			bounds.setLocation(0, bounds.y);
		if(bounds.y < 0)
			bounds.setLocation(bounds.x, 0);
		if(bounds.x + bounds.width > Engine.getWidth())
			bounds.setLocation(Engine.getWidth() - bounds.width, bounds.y);
		if(bounds.y + bounds.height > Engine.getHeight())
			bounds.setLocation(bounds.x, Engine.getHeight() - bounds.height);
	}
	
	public int[] follow(VolatileLevel cur, VolatileLevel next) {
		int x = 0;
		int y = 0;
		if(Geometry.Theta(cur.getPoint(), next.getPoint()) > (cur.getBounds().getWidth()+next.getBounds().width)) {
			if(cur.getPoint().x > next.getPoint().x) {
				x = -1;
			}else if(cur.getPoint().x < next.getPoint().x) {
				x = 1;
			}
			if(cur.getPoint().y > next.getPoint().y) {
				y = -1;
			}else if(cur.getPoint().y < next.getPoint().y) {
				y = 1;
			}
		}else {
			if(cur.getPoint().x > next.getPoint().x) {
				x = 1;
			}else if(cur.getPoint().x < next.getPoint().x) {
				x = -1;
			}
			if(cur.getPoint().y > next.getPoint().y) {
				y = 1;
			}else if(cur.getPoint().y < next.getPoint().y) {
				y = -1;
			}
		}
		return new int[] {x, y};
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setStroke(new BasicStroke(Engine.GameScale*2));
		for(int i = 0; i < levels.length; i++) {
			if(i < levels.length-1) {
				Point cur = levels[i].getPoint();
				Point next = levels[i+1].getPoint();
				if(levels[i+1].isBlocked())
					g.setColor(Engine.Color_Secondary);
				else 
					g.setColor(Engine.Color_Primary);
				g.drawLine(cur.x, cur.y, next.x, next.y);
			}
			VolatileLevel vl = levels[i];
			vl.render(g);
		}
	}
	
	public void dispose() {
		Engine.UI.clearOptions();
	}

}
