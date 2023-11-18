package com.coffee.objects.particles;

import java.awt.Color;
import java.awt.Graphics2D;

import com.coffee.main.Engine;
import com.coffee.main.activity.Game;

public class Kabum extends Particle {

	private double radians;
	private double rotate;
	private double speed;
	private Color c;
	
	public Kabum(int x, int y) {
		super(x, y);
		this.radians = Engine.RAND.nextDouble()*(Math.PI*2);
		this.speed = Engine.RAND.nextDouble();
		c = Engine.RAND.nextBoolean() ? Engine.Color_Primary : Engine.Color_Tertiary;
	}

	@Override
	public void tick() {
		dead();
		double dirX = this.getX() + Math.cos(this.radians)*speed;
		double dirY = this.getY() + Math.sin(this.radians)*speed;
		this.setX(dirX);
		this.setY(dirY);
		rotate += 0.25;
	}

	@Override
	public void render(Graphics2D g) {
		g.rotate(rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
		renderParticle(c, g);
		g.rotate(-rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
		
	}

}
