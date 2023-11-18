package com.coffee.creator;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.coffee.Inputs.Mouse;
import com.coffee.Inputs.Mouse_Button;
import com.coffee.main.Engine;
import com.coffee.main.activity.Creator;
import com.coffee.main.tools.Responsive;
import com.coffee.objects.Objects;
import com.coffee.objects.tiles.Tile;

public class Inventory {
	
	private Objects[] default_inventory; 
	private Objects[] inventory;
	private int page;
	private Responsive bounds;
	
	public Inventory(Objects[] inventory, Responsive ref, int x, int y) {
		this.default_inventory = inventory;
		this.bounds = Responsive.createRectangle(ref, new Rectangle(Tile.getSize(), Tile.getSize()), x, y);
	}
	
	private void setList(int page_size) {
		this.getResponsive().setSize(Tile.getSize(), Tile.getSize()*page_size);
		if(Mouse.On_Mouse(getResponsive().getBounds())) {
			int scrool = Mouse.Scrool();
			int p = page + scrool;
			if(p >= 0 && p + page_size <= default_inventory.length)
				page = p;
		}
		inventory = new Objects[page_size];
		int index = 0;
		for(int i = page; i < page + page_size; i++) {
			Objects o = default_inventory[i];
			o.setX(getResponsive().getBounds().x);
			o.setY(getResponsive().getBounds().y + index*Tile.getSize());
			inventory[index] = o;
			index++;
		}
	}

	public boolean itemOnSlots(Objects objects) {
		if(this.inventory != null)
			for(Objects o : this.inventory)
				if(o.equals(objects))
					return true;
		return false;
	}
	
	public Objects getItem() {
		setList(5);
		for(int i = 0; i < inventory.length; i++) {
			Objects o = inventory[i];
			if(Mouse.clickOn(Mouse_Button.LEFT, o.getBounds())) {
				return o;
			}
		}
		return null;
	}

	public Responsive getResponsive() {
		return this.bounds;
	}
	
	public void render(Graphics2D g) {
		if(inventory == null)
			return;
		g.setColor(new Color(Engine.Color_Primary.getRed(), Engine.Color_Primary.getGreen(), Engine.Color_Primary.getBlue(), 60));
		g.fillRect(getResponsive().getBounds().x - Engine.GameScale, getResponsive().getBounds().y - Engine.GameScale, getResponsive().getBounds().width + Engine.GameScale*3, getResponsive().getBounds().height + Engine.GameScale*2);
		for(int i = 0; i < inventory.length; i++) {
			Objects t = inventory[i];
			int x = (int)t.getX();
			int y = (int)t.getY();
			g.setColor(new Color(Engine.Color_Secondary.getRed(), Engine.Color_Secondary.getGreen(), Engine.Color_Secondary.getBlue(), 60));
			g.fillRect(x + 2*Engine.GameScale, y + 2*Engine.GameScale, Tile.getSize() - 4*Engine.GameScale, Tile.getSize() - 4*Engine.GameScale);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, t == Creator.getSelected() ? 1f : 0.5f));
			g.drawImage(t.getSprite(), x , y, Tile.getSize(), Tile.getSize(), null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}
		g.setColor(Engine.Color_Tertiary);
		int size = (int)(((double)inventory.length / (double)default_inventory.length)*getResponsive().getBounds().height);
		double a = ((double)size * ((double)(page) / (double)(inventory.length)));
		g.fillRect(getResponsive().getBounds().x + getResponsive().getBounds().width, (int)(getResponsive().getBounds().y + a), Engine.GameScale, size);
	}

}
