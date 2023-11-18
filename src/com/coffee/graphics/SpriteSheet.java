package com.coffee.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class SpriteSheet {

	private BufferedImage spritesheet;
	
	protected int Scale = 1;
	
	public SpriteSheet(String path){
		try {
			spritesheet = ImageIO.read(getClass().getResource(path));
			
		} catch (Exception e) {
			spritesheet = sadImage();
		}
	}
	
	public SpriteSheet(String path, int Scale){
		try {
			spritesheet = ImageIO.read(getClass().getResource(path));
		} catch (Exception e) {
			spritesheet = sadImage();
		}
		if(Scale > 1) {
			this.Scale = Scale;
			spritesheet = setScale(spritesheet, spritesheet.getWidth()*Scale, spritesheet.getHeight()*Scale);
		}
	}
	
	public BufferedImage replaceColor(int c1, int c2) {
		int w = spritesheet.getWidth();
		int h = spritesheet.getHeight();
		int rgb[] = spritesheet.getRGB(0, 0, w, h, null, 0, w);
		for(int i = 0; i < rgb.length; i++)
			if(rgb[i] == c1)
				rgb[i] = c2;
//		BufferedImage newSprite = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		spritesheet.setRGB(0, 0, w, h, rgb, 0, w);
		return spritesheet;
	}
	
	public BufferedImage getSprite(int x,int y,int width,int height){
		return spritesheet.getSubimage(x*Scale, y*Scale, width*Scale, height*Scale);
	}
	
	public BufferedImage getSprite(int x,int y){
		return spritesheet.getSubimage(x*Scale, y*Scale, 16*Scale, 16*Scale);
	}
	
	private static BufferedImage setScale(BufferedImage img, int ScaleX, int ScaleY) {
		BufferedImage NewImg = new BufferedImage(ScaleX,ScaleY,BufferedImage.TYPE_INT_ARGB);
		NewImg.getGraphics().drawImage(img, 0, 0, ScaleX, ScaleY, null);
		return NewImg;
	}

	private BufferedImage sadImage() {
		BufferedImage img = new BufferedImage(256*4,256*4,BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 256, 256);
		for(int y = 0; y <= img.getWidth()/16; y++)
			for(int x = 0; x <= img.getHeight()/16; x++) {
				g.setColor(Color.magenta);
				g.fillRect(x*16, y*16, 8, 8);
			}
		for(int y = 1; y <= img.getWidth()/16; y++)
			for(int x = 1; x <= img.getHeight()/16; x++) {
				g.setColor(Color.magenta);
				g.fillRect(x*16 - 8, y*16 - 8, 8, 8);
			}
				
		return img;
	}
	
	public int getScale() {
		return this.Scale;
	}
	
	public int getWidth() {
		return spritesheet.getWidth()/Scale;
	}
	
	public int getHeight() {
		return spritesheet.getHeight()/Scale;
	}
	
	public int getLength(String vector) {
		int v = (vector.equalsIgnoreCase("x")) ? spritesheet.getWidth()/(16*Scale) : spritesheet.getHeight()/(16*Scale);
		return v;
	}

	public BufferedImage getImage() {
		return spritesheet;
	}
	
}
