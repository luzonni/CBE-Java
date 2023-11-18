package com.coffee.objects.entity;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.coffee.Inputs.Mouse;
import com.coffee.graphics.FontG;
import com.coffee.items.Item;
import com.coffee.main.Engine;

public class Inventory {
	
	private Item[] inventory;
	
	public Inventory(int size) {
		inventory = new Item[size];
	}
	
	public boolean put(Item item) {
		if(haveSpace()) {
			for(int i = 0; i < inventory.length; i++) {
				if(inventory[i] == null) {
					inventory[i] = item;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean remove(Item item) {
		for(int i = inventory.length-1; i >= 0; i--) {
			if(inventory[i] == item) {
				inventory[i] = null;
				return true;
			}
		}
		return false;
	}
	
	public boolean haveSpace() {
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] == null) {
				return true;
			}
		}
		return false;
	}
	
	public Item[] getList() {
		List<Item> list = new ArrayList<Item>();
		for(Item item : this.inventory)
			if(item != null)
				list.add(item);
		return list.toArray(new Item[0]);
	}
	
	public boolean isEmpty() {
		boolean isEmpty = true;
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] != null) {
				isEmpty = false;
			}
		}
		return isEmpty;
	}
	
	public void tick() {
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] != null)
				inventory[i].tick();
		}
	}
	
	public void render(Graphics2D g) {
		if(isEmpty())
			return;
		Graphics2D g2 = (Graphics2D)g;
		int size = inventory.length;
		int width = 16*Engine.GameScale*size;
		int x = Engine.getWidth()/2 - width/2;
		int y = Engine.getHeight()/24;
		for(int i = 0; i < size; i++) {
			g2.setColor(Engine.Color_Secondary);
			g2.fillRect(x + i*16*Engine.GameScale, y, 16*Engine.GameScale, 16*Engine.GameScale);
			g2.setStroke(new BasicStroke(Engine.GameScale));
			g2.setColor(Engine.Color_Primary);
			g2.drawRect(x + i*16*Engine.GameScale, y, 16*Engine.GameScale, 16*Engine.GameScale);
			if(inventory[i] != null)
				g2.drawImage(inventory[i].getSprite(), x + i*16*Engine.GameScale, y, 16*Engine.GameScale, 16*Engine.GameScale, null);
			
		}
		for(int i = 0; i < size; i++) 
			if(inventory[i] != null)
				if(Mouse.On_Mouse(new Rectangle(x + i*16*Engine.GameScale, y, 16*Engine.GameScale, 16*Engine.GameScale))) 
					renderName(inventory[i], g);
	}
	
	private void renderName(Item item, Graphics2D g) {
		int x = Mouse.getX();
		int y = Mouse.getY();
		String name = item.getName();
		Font f = FontG.font(12*Engine.GameScale);
		int wF = FontG.getWidth(name, f);
		int hF = FontG.getHeight(name, f);
		g.setFont(f);
		g.setColor(Engine.Color_Tertiary);
		g.fillRect(x + 8*Engine.GameScale, y + 17*Engine.GameScale - hF, wF + 2*Engine.GameScale, hF);
		g.setColor(Engine.Color_Secondary);
		g.fillRect(x + 7*Engine.GameScale, y + 16*Engine.GameScale - hF, wF + 2*Engine.GameScale, hF);
		g.setColor(Engine.Color_Tertiary);
		g.drawString(name, x + 9*Engine.GameScale, y + 15*Engine.GameScale);
		g.setColor(Engine.Color_Primary);
		g.drawString(name, x + 8*Engine.GameScale, y + 14*Engine.GameScale);
	}
	
}
