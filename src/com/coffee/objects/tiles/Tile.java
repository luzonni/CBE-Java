package com.coffee.objects.tiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.coffee.command.Commands;
import com.coffee.graphics.SpriteSheet;
import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.Objects;
import com.coffee.objects.Variables;
import com.coffee.objects.entity.Entity;

public abstract class Tile extends Objects {
	
	private int xF, yF;
	
	private boolean solid;
	private boolean floating;
	
	public static int getSize() {
		return Engine.GameScale*16;
	}
	
	public static Tile Factory(Object... values) {
		Tile tile = null;
		int id = (int)values[0];
		int x = (int)values[1];
		int y = (int)values[2];
		switch (id) {
		case 0: 
			tile = new Air(id, x, y);
			return tile;
		case 1: 
			tile = new Wall(id, x, y);
			return tile;
		case 2: 
			tile = new Floor(id, x, y);
			return tile;
		case 3:
			tile = new Door(id, x, y);
			return tile;
		case 4:
			tile = new Reforced_Door(id, x, y);
			return tile;
		case 5:
			tile = new Box(id, x, y);
			return tile;
		case 6:
			tile = new Crate(id, x, y);
			return tile;
		case 7:
			tile = new Spine(id, x, y);
			return tile;
		case 8:
			tile = new Vase(id, x, y);
			return tile;
		case 9:
			tile = new Thorn(id, x, y);
			return tile;
		case 10:
			tile = new Trampoline(id, x, y);
			return tile;
		case 11: 
			tile = new Repellent(id, x, y);
			return tile;
		case 12: 
			tile = new Fake_Wall(id, x, y);
			return tile;
		}
		throw new RuntimeException("Tile not exist");
	}
	
	public Tile(int id, int x, int y) {
		super(id);
		this.setX(x);
		this.setY(y);
		this.setSize(getSize(), getSize());
	}
	
	public BufferedImage[] getSprite(String name, Color color) {
		SpriteSheet spriteSheet = new SpriteSheet(Engine.ResPath+"/tiles/"+name+".png", Engine.GameScale);
		spriteSheet.replaceColor(0xffffffff, color.getRGB());
		spriteSheet.replaceColor(0xff000000, Engine.Color_Tertiary.getRGB());
		int lenght = spriteSheet.getWidth()/16;
		BufferedImage[] sprites = new BufferedImage[lenght];
		for(int i = 0; i < lenght; i++) {
			sprites[i] = spriteSheet.getSprite(i*(16), 0, 16, 16);
		}
		return sprites;
	}
	
	public BufferedImage[] getSprite(String name, Color color, int verticalIndex) {
		SpriteSheet spriteSheet = new SpriteSheet(Engine.ResPath+"/tiles/"+name+".png", Engine.GameScale);
		spriteSheet.replaceColor(0xffffffff, color.getRGB());
		spriteSheet.replaceColor(0xff000000, Engine.Color_Tertiary.getRGB());
		int lenght = (spriteSheet.getWidth())/16;
		BufferedImage[] sprites = new BufferedImage[lenght];
		for(int i = 0; i < lenght; i++) {
			sprites[i] = spriteSheet.getSprite(i*(16), 16*verticalIndex, 16, 16);
		}
		return sprites;
	}
	
	public boolean isSolid() {
		return this.solid;
	}
	
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
	
	public boolean centralizedWith(Entity o) {
		Point p1 = new Point((int)o.getX() + 1, (int)o.getY() + 1);
		Point p2 = new Point((int)o.getX() + o.getWidth() - 1, (int)o.getY() + 1);
		Point p3 = new Point((int)o.getX() + o.getWidth() - 1, (int)o.getY() + o.getHeight() - 1);
		Point p4 = new Point((int)o.getX() + 1, (int)o.getY() + o.getHeight() - 1);
		if((this.getBounds().contains(p1) && this.getBounds().contains(p2) && this.getBounds().contains(p3) && this.getBounds().contains(p4)) && !o.isFloating()) {
			return true;
		}
		return false;
	}
	
	@Override
	public String giveCommand(String[] keys) {
		String message = "Command no access";
		if(take(keys, Commands.move)) {
			Tile[] map = Game.getLevel().getMap();
			int w = Game.getLevel().getBounds().width / Tile.getSize();
			int h = Game.getLevel().getBounds().height / Tile.getSize();
			int x_next = Integer.parseInt(keys[1]);
			int y_next = Integer.parseInt(keys[2]);
			Tile curTile = this;
			int xIndex = (int)curTile.getX() / Tile.getSize();
			int yIndex = (int)curTile.getY() / Tile.getSize();
			if(xIndex+x_next < 0 || xIndex+x_next > w || yIndex+y_next < 0 || yIndex+y_next > h)
				return "Out position";
			Tile nextTile = map[(xIndex+x_next)+(yIndex+y_next)*w];
			if(!curTile.getVar(Variables.Movable) || !nextTile.getVar(Variables.Movable)) {
				message = "The box could not be moved to this position";
				return message;
			}
			for(Entity e : Game.getLevel().getEntities())
				if(nextTile.centralizedWith(e)) {
					message = "The position has an entity";
					return message;
				}
			curTile.setX(curTile.getX()+x_next*Tile.getSize());
			curTile.setY(curTile.getY()+y_next*Tile.getSize());
			nextTile.setX(nextTile.getX()-x_next*Tile.getSize());
			nextTile.setY(nextTile.getY()-y_next*Tile.getSize());
			Game.getLevel().getMap()[xIndex+yIndex*w] = nextTile;
			Game.getLevel().getMap()[(xIndex+x_next)+(yIndex+y_next)*w] = curTile;
			message = "";
			used(Commands.move);
			return message;
		}
		return message;
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics2D g) {
		renderTile(getSprite(), g);
	}
	
	public void renderTile(BufferedImage Sprite, Graphics2D g) {
		int x = (int)getX() + xF;
		int y = (int)getY() + yF;
		if(floating && Engine.RAND.nextInt(1000) < 5) {
			xF = (Engine.RAND.nextInt(2) - 1)*Engine.GameScale;
			yF = (Engine.RAND.nextInt(2) - 1)*Engine.GameScale;
		}
		g.drawImage(Sprite, x - Game.getCam().getX(), y - Game.getCam().getY(), null);
	}

}
