package com.coffee.objects.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.Geometry;
import com.coffee.main.Sound;
import com.coffee.main.activity.Game;
import com.coffee.main.tools.Timer;
import com.coffee.objects.Variables;
import com.coffee.objects.tiles.Tile;

public class Cannon extends Entity {
	
	private Timer timer;
	private static BufferedImage base;
	private static BufferedImage cannon;
	private double radians;

	public Cannon(int id, int x, int y) {
		super(id, x, y);
		if(base == null)
			base = getSprite("Cannon", Engine.Color_Tertiary)[0];
		if(cannon == null)
			cannon = getSprite("Cannon", Engine.Color_Primary)[1];
		timer = new Timer(2 + Engine.RAND.nextInt(5));
		setVar(Variables.Selectable, true);
		setVar(Variables.Movable, true);
	}

	@Override
	public BufferedImage getSprite() {
		BufferedImage sprite = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = sprite.getGraphics();
		g.drawImage(base, 0, 0, null);
		g.drawImage(cannon, 0, 0, null);
		g.dispose();
		return sprite;
	}

	@Override
	public void tick() {
		Player p = Game.getPlayer();
		if(Geometry.Theta(this.getMiddle(), p.getMiddle()) < Tile.getSize()*12)
			radians = Math.atan2(this.getMiddle().getY() - p.getMiddle().getY(), this.getMiddle().getX() - p.getMiddle().getX()) - Math.PI/2;
		if(timer.tiktak()) {
			double r = radians - Math.PI/2; 
			int x = (int)getMiddle().x + (int)(Math.cos(r)*Tile.getSize()/1.75);
			int y = (int)getMiddle().y + (int)(Math.sin(r)*Tile.getSize()/1.75);
			Game.getLevel().addEntity(new IronBall(x, y, r, Engine.GameScale*3));
			Sound.play("poft");
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		renderEntity(base, g);
		g.rotate(radians, getX() - Game.getCam().getX() + getWidth()/2, getY() - Game.getCam().getY() + getHeight()/2);
		renderEntity(cannon, g);
		g.rotate(-radians, getX() - Game.getCam().getX() + getWidth()/2, getY() - Game.getCam().getY() + getHeight()/2);
	}

}
