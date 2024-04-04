package com.coffee.objects;

import java.awt.image.BufferedImage;

import com.coffee.graphics.SpriteSheet;
import com.coffee.main.Engine;

public enum Variables {
	
	Selectable, Movable, Removeble, Duplicable, Pullable, Ollarable, Generable, Modifiable, Breakable, Freeze, Alive, Armored;
	
	public String getName() {
		return this.name().toLowerCase();
	}
	
	public BufferedImage getIcon() {
		String path = Engine.ResPath+"/effects/"+getName()+".png";
		return new SpriteSheet(path, Engine.GameScale).getImage();
	}
	
}
