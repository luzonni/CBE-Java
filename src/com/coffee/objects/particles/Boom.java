package com.coffee.objects.particles;

import java.awt.Graphics2D;

import com.coffee.main.Engine;
import com.coffee.main.activity.Game;

public class Boom extends Particle {
	
	private double radians;
	private double rotate;
	private double speed;
	
	public Boom(int x, int y, double radians) {
		super(x, y);
		this.radians = radians;
		this.speed = Engine.RAND.nextDouble();
	}

	@Override
	public void tick() {
		dead();
		double dirX = this.getX() + Math.cos(this.radians)*speed;
		double dirY = this.getY() + Math.sin(this.radians)*speed;
		this.setX(dirX);
		this.setY(dirY);
		rotate += 0.15;
	}

	@Override
	public void render(Graphics2D g) {
		g.rotate(rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
		renderParticle(Engine.Color_Tertiary, g);
		g.rotate(-rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
		
	}

}
