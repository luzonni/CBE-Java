package com.coffee.creator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.coffee.Inputs.Mouse;
import com.coffee.Inputs.Mouse_Button;
import com.coffee.command.Commands;
import com.coffee.graphics.FontG;
import com.coffee.main.Engine;
import com.coffee.main.tools.Responsive;

public class Commands_Boxe {
	
	private Rectangle box_list;
	private Rectangle box_default_list;
	private C[] list;
	private int page;
	private List<C> default_list;
	
	public Commands_Boxe(List<Commands> commands) {
		this.box_list = new Rectangle();
		this.box_default_list = new Rectangle();
		this.default_list = new ArrayList<C>();
		if(commands != null)
			for(Commands c : commands)
				this.default_list.add(new C(c));
	}
	
	public List<Commands> tick() {
		createList(4);
		setDimensions();
		for(int i = 0; i < list.length; i++)
			if(Mouse.clickOn(Mouse_Button.LEFT, list[i].bounds)) {
				C new_c = new C(list[i].command);
				new_c.bounds.setLocation(list[i].bounds.x, list[i].bounds.y);
				default_list.add(new_c);
			}
		for(int i = 0; i < default_list.size(); i++) 
			if(Mouse.clickOn(Mouse_Button.LEFT, default_list.get(i).bounds)) 
				default_list.remove(default_list.get(i));
		List<Commands> L = new ArrayList<Commands>();
		for(int i = 0; i < default_list.size(); i++)
			L.add(default_list.get(i).command);
		return L;
	}
	
	private void createList(int list_Size) {
		Commands[] l = Commands.values();
		if(Mouse.On_Mouse(box_list)) {
			int scrool = Mouse.Scrool();
			int p = page + scrool;
			if(p >= 0 && p + list_Size <= l.length)
				page = p;
		}
		list = new C[list_Size];
		int index = 0;
		for(int i = page; i < page + list_Size; i++) {
			list[index] = new C(l[i]);
			index++;
		}
	}
	
	private void setDimensions() {
		Responsive R_1 = Responsive.createPoint(null, 85, 5);
		Responsive R_2 = Responsive.createPoint(null, 98, 5);
		int w = R_2.getPosition().x - R_1.getPosition().x;
		int padding = 5*Engine.GameScale;
		
		int h_box_list = 0;
		for(int i = 0; i < list.length; i++) {
			h_box_list += list[i].bounds.height;
			C C = list[i];
			C.bounds.setLocation(R_1.getPosition().x + box_list.width/2 - C.bounds.width/2 - padding, R_1.getPosition().y + i*C.bounds.height);
		}
		box_list.setBounds(R_1.getPosition().x - padding, R_1.getPosition().y - padding, w + padding*2, h_box_list + padding*2);
		
		int h_box_default_list = 0;
		for(int i = 0; i < default_list.size(); i++) {
			h_box_default_list += default_list.get(i).bounds.height;
			C c = default_list.get(i);
			c.bounds.setLocation(box_default_list.x + box_default_list.width/2 - c.bounds.width/2, box_default_list.y + padding + i*c.bounds.height);
		}
		box_default_list.setBounds(R_1.getPosition().x - padding, R_1.getPosition().y + h_box_list + padding*2, w + padding*2, h_box_default_list + padding*2);
	}
	
	public void render(Graphics2D g) {
		if(list == null || default_list == null)
			return;
		g.setColor(new Color(Engine.Color_Primary.getRed(), Engine.Color_Primary.getGreen(), Engine.Color_Primary.getBlue(), 60));
		g.fillRect(box_list.x - Engine.GameScale, box_list.y - Engine.GameScale, box_list.width + Engine.GameScale*2, box_list.height + Engine.GameScale*2);
		g.setColor(Engine.Color_Tertiary);
		int size = (int)(((double)list.length / (double)Commands.values().length)*box_list.height);
		double a = ((double)size * ((double)(page) / (double)(list.length)));
		g.fillRect(box_list.x + box_list.width - Engine.GameScale, (int)(box_list.y + a), Engine.GameScale, size);
		g.setColor(new Color(Engine.Color_Primary.getRed(), Engine.Color_Primary.getGreen(), Engine.Color_Primary.getBlue(), 60));
		if(!default_list.isEmpty())
			g.fillRect(box_default_list.x, box_default_list.y, box_default_list.width, box_default_list.height);
		for(C c : list)
			c.render(g);
		for(C c : default_list) 
			c.render(g);
	}
	
	private class C {
		
		private Font font = FontG.font(8*Engine.GameScale);
		private Commands command;
		private Rectangle bounds;
		
		public C(Commands command) {
			this.command = command;
			String value = this.command.getName();
			int wF = FontG.getWidth(value, font);
			int hF = FontG.getHeight(value, font);
			this.bounds = new Rectangle(wF, hF);
		}
		
		public void render(Graphics2D g) {
			if(Mouse.On_Mouse(bounds))
				g.setColor(new Color(Engine.Color_Primary.getRed(), Engine.Color_Primary.getGreen(), Engine.Color_Primary.getBlue(), 100));
			else
				g.setColor(Engine.Color_Primary);
			g.setFont(font);
			String value = command.getName();
			int hF = FontG.getHeight(value, font);
			g.drawString(value, this.bounds.x, this.bounds.y + hF);
		}
		
	}

}
