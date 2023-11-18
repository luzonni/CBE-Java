package com.coffee.items;

import java.awt.image.BufferedImage;

import com.coffee.command.Commands;
import com.coffee.graphics.SpriteSheet;
import com.coffee.main.Engine;
import com.coffee.objects.Variables;
import com.coffee.objects.entity.EntityItem;

public abstract class Item {
	
	private String name;
	private BufferedImage[] sprite;
	
	private int indexAnim;
	private int counter;
	private int timer;
	
	public static EntityItem Factory(Object... values) {
		EntityItem item;
		int id = (int)values[0];
		int x = (int)values[1];
		int y = (int)values[2];
		switch (id) {
		case 0: 
			return null;
		case 1: 
			item = new EntityItem(id, new Key(), x, y);
			return item;
		case 2: 
			item = new EntityItem(id, new CMD(Commands.select), x, y);
			return item;
		case 3: 
			item = new EntityItem(id, new CMD(Commands.move), x, y);
			return item;
		case 4: 
			item = new EntityItem(id, new Bow(), x, y);
			return item;
		case 5: 
			item = new EntityItem(id, new Placeable(12), x, y);
			return item;
		case 6: 
			item = new EntityItem(id, new Usable(Variables.Armored), x, y);
			return item;
		}
		throw new RuntimeException("Tile not exist");
	}
	
	public Item(String name) {
		this.name = name.toLowerCase();
		this.timer = 15;
	}
	
	protected void setTimer(int newtimer) {
		this.timer = newtimer;
	}
	
	protected void setSprite(String sprite) {
		this.sprite = getSprite(sprite.toLowerCase());
	}
	
	protected void setSprite(BufferedImage[] sprite) {
		this.sprite = sprite;
	}
	
	public String getName() {
		return this.name;
	}

	public BufferedImage getSprite() {
		return this.sprite[indexAnim];
	}
	
	public BufferedImage getIcon() {
		return this.sprite[0];
	}
	
	public static BufferedImage[] getSprite(String name) {
		SpriteSheet spriteSheet = new SpriteSheet(Engine.ResPath+"/items/"+name+".png", Engine.GameScale);
		spriteSheet.replaceColor(0xffffffff, Engine.Color_Primary.getRGB());
		spriteSheet.replaceColor(0xff000000, Engine.Color_Tertiary.getRGB());
		int lenght = (spriteSheet.getWidth())/16;
		BufferedImage[] sprites = new BufferedImage[lenght];
		for(int i = 0; i < lenght; i++) {
			sprites[i] = spriteSheet.getSprite(i*(16), 0, 16, 16);
		}
		return sprites;
	}
	
	public void tick() {
		counter++;
		if(counter > timer) {
			counter = 0;
			indexAnim++;
			if(indexAnim > sprite.length-1)
				indexAnim = 0;
		}
	}
	
	public void fun() {
		
	}
	
}
