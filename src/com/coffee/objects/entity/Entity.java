package com.coffee.objects.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import com.coffee.command.Commands;
import com.coffee.graphics.SpriteSheet;
import com.coffee.items.Item;
import com.coffee.items.Usable;
import com.coffee.main.Engine;
import com.coffee.main.Sound;
import com.coffee.main.activity.Game;
import com.coffee.objects.Directions;
import com.coffee.objects.Objects;
import com.coffee.objects.Variables;
import com.coffee.objects.particles.Kabum;
import com.coffee.objects.tiles.Tile;

public abstract class Entity extends Objects {
	
	private boolean floating;
	private Orienteering_Physics oe;
	
	public static Entity Factory(Object... values) {
		Entity entity;
		int id = (int)values[0];
		int x = (int)values[1];
		int y = (int)values[2];
		switch (id) {
		case 0: 
			return null;
		case 1: 
			entity = new Player(id, x, y);
			return entity;
		case 2: 
			entity = new Flag(id, x, y);
			return entity;
		case 3: 
			entity = new Cannon(id, x, y);
			return entity;
		case 4: 
			entity = new Crossbow(id, x, y);
			return entity;
		case 5: 
			entity = new Ant(id, x, y);
			return entity;
		case 6: 
			entity = new Button(id, x, y);
			return entity;
		case 7: 
			entity = new Computer(id, x, y);
			return entity;
		case 8: 
			entity = new Skull(id, x, y);
			return entity;
		case 9: 
			entity = new Pluuter(id, x, y);
			return entity;
		case 10: 
			entity = new Karto(id, x, y);
			return entity;
		case 11: 
			entity = new Saw(id, x, y);
			return entity;
		case 12: 
			entity = new Bomb(id, x, y);
			return entity;
		case 13: 
			entity = new Fish(id, x, y);
			return entity;
		case 14: 
			entity = new Beetle(id, x, y);
			return entity;
		case 15: 
			entity = new Fly(id, x, y);
			return entity;
		case 16: 
			entity = new Berne(id, x, y);
			return entity;
		}
		throw new RuntimeException("Tile not exist");
	}
	
	public BufferedImage[] getSprite(String name) {
		SpriteSheet spriteSheet = new SpriteSheet(Engine.ResPath+"/entity/"+name+".png", Engine.GameScale);
		spriteSheet.replaceColor(0xffffffff, Engine.Color_Primary.getRGB());
		spriteSheet.replaceColor(0xffcccccc, Engine.Color_Secondary.getRGB());
		spriteSheet.replaceColor(0xff000000, Engine.Color_Tertiary.getRGB());
		int lenght = spriteSheet.getWidth()/16;
		BufferedImage[] sprites = new BufferedImage[lenght];
		for(int i = 0; i < lenght; i++) {
			sprites[i] = spriteSheet.getSprite(i*16, 0, 16, 16);
		}
		return sprites;
	}
	
	public BufferedImage[] getSprite(String name, Color color) {
		SpriteSheet spriteSheet = new SpriteSheet(Engine.ResPath+"/entity/"+name+".png", Engine.GameScale);
		spriteSheet.replaceColor(0xffffffff, color.getRGB());
		spriteSheet.replaceColor(0xffcccccc, Engine.Color_Secondary.getRGB());
		spriteSheet.replaceColor(0xff000000, Engine.Color_Tertiary.getRGB());
		int lenght = (spriteSheet.getWidth())/16;
		BufferedImage[] sprites = new BufferedImage[lenght];
		for(int i = 0; i < lenght; i++) {
			sprites[i] = spriteSheet.getSprite(i*(16), 0, 16, 16);
		}
		return sprites;
	}
	
	public BufferedImage[] getSprite(String name, Color color, int verticalIndex) {
		SpriteSheet spriteSheet = new SpriteSheet(Engine.ResPath+"/entity/"+name+".png", Engine.GameScale);
		spriteSheet.replaceColor(0xffffffff, color.getRGB());
		spriteSheet.replaceColor(0xffcccccc, Engine.Color_Secondary.getRGB());
		spriteSheet.replaceColor(0xff000000, Engine.Color_Tertiary.getRGB());
		int lenght = (spriteSheet.getWidth())/16;
		BufferedImage[] sprites = new BufferedImage[lenght];
		for(int i = 0; i < lenght; i++)
			sprites[i] = spriteSheet.getSprite(i*16, 16*verticalIndex, 16, 16);
		return sprites;
	}
	
	public Entity(int id, int x, int y) {
		super(id);
		this.setX(x);
		this.setY(y);
		this.oe = new Orienteering_Physics(this);
	}
	
	@Override
	public String giveCommand(String[] keys) {
		Objects object = Game.getSelect();
		String message = "Command no access";
		if(take(keys, Commands.remove)) {
			message = "Was removed";
			Game.getLevel().getEntities().remove(object);
			Game.clearSelect();
			used(Commands.remove);
		}
		if(take(keys, Commands.move)) {
			//TODO see later
			System.out.println("Olhar esse sistema! (Sistema de mover das entidades)");
			Entity nextEntity = null;
			int x = (int)getX() / Tile.getSize();
			int y = (int)getY() / Tile.getSize();
			int x_next = Integer.parseInt(keys[1]);
			int y_next = Integer.parseInt(keys[2]);
			List<Entity> list = Game.getLevel().getEntities();
			for(int i = 0; i < list.size(); i++) {
				if(Game.getLevel().getTile(x+x_next, y+y_next).centralizedWith(list.get(i))) {
					nextEntity = list.get(i);
				}
			}
			if(nextEntity != null && nextEntity.getVar(Variables.Movable)) {
				nextEntity.setX(x * Tile.getSize());
				nextEntity.setY(y * Tile.getSize());
			}
			if(this.getVar(Variables.Movable)) {
				setX((x + x_next) * Tile.getSize());
				setY((y + y_next) * Tile.getSize());
			}
		}

		if(take(keys, Commands.use)) {
			Item[] items = Game.getPlayer().getInventory().getList();
			for(int i = 0; i < items.length; i++) {
				if(items[i] instanceof Usable) {
					if(((Usable)items[i]).set(keys[1], this)) {
						Game.getPlayer().getInventory().remove(items[i]);
						used(Commands.use);
						Game.getLevel().clearSelect();
						message = "";
					}else
						message = "Item not found";
				}else {
					message = "Item not found";
				}
			}
		}
		return message;
	}
	public Directions getDirection() {
		return this.oe.getDirection();
	} 
	
	public Orienteering_Physics getOE() {
		return this.oe;
	}

	public void setDirection(Directions newdir) {
		this.oe.setDirection(newdir);
	} 
	
	public void setFloating(boolean floating) {
		this.floating = floating;
	}
	
	public void setEffect(Variables var) {
		this.setVar(var, true);
	}
	
	public boolean isFloating() {
		return this.floating;
	}
	
	public void die() {
		if(!this.getVar(Variables.Armored)) {
			for(int i = 0; i < 40; i++)
				Game.getLevel().addParticle(new Kabum(getMiddle().x, getMiddle().y));
			Game.getLevel().getEntities().remove(this);
			Sound.play("die");
		}else {
			this.setVar(Variables.Armored, false);
		}
	}
	
	public boolean collidingWith(Entity o) {
		int xMe = ((int)getX() + getWidth()/2) / Tile.getSize();
		int yMe = ((int)getY() + getHeight()/2) / Tile.getSize();
		Tile meTile = Game.getLevel().getTile(xMe, yMe);
		int xO = ((int)o.getX() + o.getWidth()/2) / Tile.getSize();
		int yO = ((int)o.getY() + o.getHeight()/2) / Tile.getSize();
		Tile oTile = Game.getLevel().getTile(xO, yO);
		if(getBounds().intersects(o.getBounds()) && meTile == oTile && !o.isFloating() && !this.isFloating()) {
			return true;
		}
		return false;
	}
	
	public void renderEntity(BufferedImage sprite, Graphics2D g) {
		int x = (int)getX();
		int y = (int)getY();
		if(floating && Engine.RAND.nextInt(10) < 5) {
			x += (Engine.RAND.nextInt(3*Engine.GameScale) - 1*Engine.GameScale);
			y += (Engine.RAND.nextInt(3*Engine.GameScale) - 1*Engine.GameScale);
		}
		g.drawImage(sprite, x - Game.getCam().getX(), y - Game.getCam().getY(), getWidth(), getHeight(), null);
		//TODO fix effect shower
//		renderEffect(x, y, g);
	}
	
//	private void renderEffect(int x, int y, Graphics2D g) {
//		if(getVar(Variables.Armored)) {
//			BufferedImage image = Usable.getIcon();
//			switch (getDirection()) {
//			case Up: 
//				image = Flip.Horizontal(image);
//				break;
//			case Right:
//				image = Flip.Rotate(image, -90);
//				break;
//			case Left:
//				image = Flip.Rotate(image, 90);
//				break;
//			default:
//				break;
//			}
//			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
//			g.drawImage(image, x - Game.getCam().getX(), y - Game.getCam().getY(), getWidth(), getHeight(), null);
//			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
//		}
//	}
	
	
}
