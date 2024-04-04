package com.coffee.objects.particles;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.graphics.SpriteSheet;
import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.Objects;

public abstract class Particle extends Objects {
	
	private float f = 1;
	
	public Particle(int x, int y) {
		super(-1);
		int size = (3 + Engine.RAND.nextInt(3)) * Engine.GameScale;
		this.setSize(size, size);
		this.setX(x - size/2);
		this.setY(y - size/2);
	}
	
	@Override
	public String giveCommand(String[] keys) {
		return null;
	}
	
	@Override
	public BufferedImage getSprite() {
		return null;
	}
	
	protected BufferedImage[] getSprite(String name, Color color) {
		SpriteSheet spriteSheet = new SpriteSheet(Engine.ResPath+"/particles/"+name+".png", Engine.GameScale);
		spriteSheet.replaceColor(0xffffffff, color.getRGB());
		spriteSheet.replaceColor(0xff000000, Engine.Color_Tertiary.getRGB());
		int lenght = (spriteSheet.getWidth())/16;
		BufferedImage[] sprites = new BufferedImage[lenght];
		for(int i = 0; i < lenght; i++) {
			sprites[i] = spriteSheet.getSprite(i*(16), 0, 16, 16);
		}
		return sprites;
	}
	
	public void dead() {
		f -= 0.02;
		if(f < 0) {
			f = 0;
			Game.getLevel().getParticles().remove(this);
		}
	}
	
	public void renderParticle(BufferedImage image, Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, f));
		g.drawImage(image, (int)getX() - Game.getCam().getX(), (int)getY() - Game.getCam().getY(), getWidth(), getHeight(), null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
	
	public void renderParticle(Color color, Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, f));
		g.setColor(color);
		g.fillRect((int)getX() - Game.getCam().getX(), (int)getY() - Game.getCam().getY(), getWidth(), getHeight());
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
	
	@Override
	public void dispose() {
		
	}

}
