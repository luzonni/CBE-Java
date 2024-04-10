package com.coffee.main.ui.win;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.coffee.Inputs.Mouse;
import com.coffee.Inputs.Mouse_Button;
import com.coffee.command.Commands;
import com.coffee.graphics.FontG;
import com.coffee.main.Engine;

public class Helper extends View {
	
	
	private List<Commands> commands;
	private List<Comman> list;
	
	public Helper(List<Commands> commands) {
		super("Commands");
		setCommands(commands);
	}
	
	public void setCommands(List<Commands> commands) {
		this.commands = commands;
	}
	
	public void init() {
		list = new ArrayList<Comman>();
	}
	
	public void tick() {
		list.clear();
		for(Commands c : commands)
			list.add(new Comman(c));
		
		int h = 0;
		int w = bounds().width;
		if(showing()) {
			for(Comman c : list)
				h += c.bounds.height;
			for(Comman c : list)
				if(w < c.bounds.width)
					w = c.bounds.width;
		}else {
			h = 0;
		}
		resize(w, h);
		selectHelp();
	}
	
	public void selectHelp() {
		if(!showing())
			return;
		int x = bounds().x;
		int y = bounds().y;
		for(int i = 0; i < list.size(); i++) {
			Rectangle rec = list.get(i).bounds;
			if(Mouse.clickOn(Mouse_Button.LEFT, new Rectangle(x, y + i*rec.height, rec.width, rec.height))) {
				Engine.UI.getConsole().print(list.get(i).c.getTextHelp(), true);
			}
			if(Mouse.clickOn(Mouse_Button.RIGHT, new Rectangle(x, y + i*rec.height, rec.width, rec.height))) {
				Engine.UI.getConsole().print(list.get(i).c.getCommandHelp(), true);
			}
		}
	}
	
	public void render(Graphics2D g) {
		super.render(g);
		if(!showing())
			return;
		int x = bounds().x;
		int y = bounds().y;
		for(int i = 0; i < list.size(); i++) {
			Rectangle rec = list.get(i).bounds;
			boolean on = false;
			if(Mouse.On_Mouse(x, y + i*rec.height, rec.width, rec.height)) {
				on = true;
			}
			list.get(i).render(x, y + i*list.get(i).bounds.height, on, g);
		}
	}
	
	
	private class Comman {
		
		private Rectangle bounds;
		private Commands c;
		private int padding = 5*Engine.GameScale;
		
		public Comman(Commands c) {
			this.c = c;
			int w = FontG.getWidth(c.getName(), font()) + padding*2;
			int h = FontG.getHeight(c.getName(), font()) + padding*2;
			this.bounds = new Rectangle(w, h);
		}
		
		public void render(int x, int y, boolean on, Graphics2D g) {
			String name = c.getName();
			g.setColor(Engine.Color_Primary);
			int wF = FontG.getWidth(name, font());
			int hF = FontG.getHeight(name, font());
			g.drawString(name, padding + x, padding + y + hF);
			if(on) {
				g.drawLine(x + padding, y + hF + 2*Engine.GameScale + padding, x + wF + padding, y + hF + 2*Engine.GameScale + padding);
			}
		}
		
	}
	
}
