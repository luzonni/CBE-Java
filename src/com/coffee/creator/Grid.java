package com.coffee.creator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.coffee.Inputs.Mouse;
import com.coffee.Inputs.Mouse_Button;
import com.coffee.main.Engine;
import com.coffee.main.Sound;
import com.coffee.main.activity.Creator;
import com.coffee.main.tools.Responsive;
import com.coffee.objects.Objects;
import com.coffee.objects.tiles.Tile;

public class Grid {

	private Objects[] Grid;
	private int Width, Height;

	private int lastX_mouse, lastY_mouse;
	
	public Grid(Objects[] Grid, int Width, int Height) {
		this.Grid = Grid;
		this.Width = Width;
		this.Height = Height;
	}
	
	public void tick() {
		move();
	}
	
	private void move() {
		if(Grid != null)
			if(Mouse.pressing(Mouse_Button.SCROOL)) {
				int x = lastX_mouse - Mouse.getX();
				int y = lastY_mouse - Mouse.getY();
				Creator.getCam().setPosition(x, y);
			}else {
				lastX_mouse = Mouse.getX() + Creator.getCam().getX();
				lastY_mouse = Mouse.getY() + Creator.getCam().getY();
			}
	}
	
	public void setGrid(Objects object) {
		Responsive center = Creator.getCenter();
		int X = center.getPosition().x;
		int Y = center.getPosition().y;
		int w = Width * Tile.getSize();
		int h = Height * Tile.getSize();
		int xx = X - w/2;
		int yy = Y - h/2;
		for(int y = 0; y < Height; y++) {
			for(int x = 0; x < Width; x++) {
				int px = xx + x * Tile.getSize() + Engine.GameScale;
				int py = yy + y * Tile.getSize() + Engine.GameScale;
				if(Mouse.pressingOnMap(Mouse_Button.LEFT, new Rectangle(px, py, Tile.getSize(), Tile.getSize()), Creator.getCam()))
					if(Grid[x+y*Width] == null) {
						Grid[x+y*Width] = object;
						Sound.play("place");
					}
			}
		}
	}
	
	public Rectangle getBounds() {
		Responsive center = Creator.getCenter();
		int w = Width * Tile.getSize();
		int h = Height * Tile.getSize();
		int x = center.getPosition().x - w/2;
		int y = center.getPosition().y - h/2;
		return new Rectangle(x - Creator.getCam().getX(), y - Creator.getCam().getY(), w, h);
	}
	
	public Objects[] getArray() {
		return this.Grid;
	}
	
	public boolean clearGrid() {
		Responsive center = Creator.getCenter();
		int X = center.getPosition().x;
		int Y = center.getPosition().y;
		int w = Width * Tile.getSize();
		int h = Height * Tile.getSize();
		int xx = X - w/2;
		int yy = Y - h/2;
		for(int y = 0; y < Height; y++) {
			for(int x = 0; x < Width; x++) {
				int px = xx + x * Tile.getSize() + Engine.GameScale;
				int py = yy + y * Tile.getSize() + Engine.GameScale;
					if(Grid[x+y*Width] != null) {
						if(Mouse.clickOnMap(Mouse_Button.RIGHT, new Rectangle(px, py, Tile.getSize(), Tile.getSize()), Creator.getCam())) {
							Grid[x+y*Width] = null;
							Sound.play("clear");
							return true;
						}
					}
			}
		}
		return false;
	}
	
	public void render(Graphics2D g, boolean renderGrid) {
		if(Grid == null)
			return;
		Responsive center = Creator.getCenter();
		int X = center.getPosition().x - Creator.getCam().getX();
		int Y = center.getPosition().y - Creator.getCam().getY();
		int w = Width * Tile.getSize();
		int h = Height * Tile.getSize();
		int xx = X - w/2;
		int yy = Y - h/2;
		for(int y = 0; y < Height; y++) {
			for(int x = 0; x < Width; x++) {
				g.setColor(new Color(Engine.Color_Secondary.getRed(), Engine.Color_Secondary.getGreen(), Engine.Color_Secondary.getBlue(), 60));
				int px = xx + x * Tile.getSize() + Engine.GameScale;
				int py = yy + y * Tile.getSize() + Engine.GameScale;
				if(renderGrid)
					g.fillRect(px, py, Tile.getSize() - 2*Engine.GameScale, Tile.getSize() - 2*Engine.GameScale);
				if(Grid[x+y*Width] != null)
					g.drawImage(Grid[x+y*Width].getSprite(), px , py, null);
				
			}
		}
		
	}

}
