package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.coffee.graphics.Flip;
import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.main.tools.Timer;
import com.coffee.objects.Directions;
import com.coffee.objects.Variables;

public class Skull extends Entity {
	
	private static BufferedImage[] sprites;
	private int index_sprite;
	private Directions dir;
	private Timer timer;

	public Skull(int id, int x, int y) {
		super(id, x, y);
		if(sprites == null)
			sprites = getSprite("skull", Engine.Color_Primary);
		index_sprite = Engine.RAND.nextInt(sprites.length);
		getValues().addInt("speed", Engine.GameScale * 8);
		this.dir = Directions.Down;
		this.timer = new Timer(2);
		this.setDepth(1);
		this.setVar(Variables.Selectable, true);
		this.setVar(Variables.Freeze, false);
		this.setVar(Variables.Alive, true);
	}

	@Override
	public BufferedImage getSprite() {
		BufferedImage image = sprites[index_sprite];
		switch (dir) {
			case Up: 
				image = Flip.Horizontal(image);
				break;
			case Right:
				image = Flip.Rotate(image, -90);
				break;
			case Left:
				image = Flip.Rotate(image, 90);
				break;
			default:
				break;
		}
		return image;
	}

	@Override
	public void tick() {
		if(getOE().colliding(dir))
			if(this.timer.tiktak())
				setDirection();
		dynamics();
		Player p = Game.getPlayer();
		if(p.collidingWith(this))
			p.die();
	}
	
	private void setDirection() {
		List<Directions> possible_dir = new ArrayList<Directions>();
		if(!getOE().colliding(Directions.Down))
			possible_dir.add(Directions.Down);
		if(!getOE().colliding(Directions.Up))
			possible_dir.add(Directions.Up);
		if(!getOE().colliding(Directions.Left))
			possible_dir.add(Directions.Left);
		if(!getOE().colliding(Directions.Right))
			possible_dir.add(Directions.Right);
		this.dir = chose_direction(possible_dir);
	}

	private Directions chose_direction(List<Directions> possible_dir) {
		Player p = Game.getPlayer();
		if(possible_dir.contains(Directions.Down) && p.getY() > getY())
			return Directions.Down;
		if(possible_dir.contains(Directions.Up) && p.getY() < getY())
			return Directions.Up;
		if(possible_dir.contains(Directions.Left) && p.getX() < getX())
			return Directions.Left;
		if(possible_dir.contains(Directions.Right) && p.getY() > getY())
			return Directions.Right;
		return possible_dir.get(Engine.RAND.nextInt(possible_dir.size()));
	}
	
	private void dynamics() {
		switch(dir) {
			case Up:
				if(!getOE().colliding(Directions.Up)) {
					this.setY(this.getY() - this.getSpeed());
				}
				break;
			case Right:
				if(!getOE().colliding(Directions.Right)) {
					this.setX(this.getX() + this.getSpeed());
				}
				break;
			case Down:
				if(!getOE().colliding(Directions.Down)) {
					this.setY(this.getY() + this.getSpeed());
				}
				break;
			case Left:
				if(!getOE().colliding(Directions.Left)) {
					this.setX(this.getX() - this.getSpeed());
				}
				break;
			default:
				break;
		}
	}
	
	public int getSpeed() {
		return this.getValues().getInt("speed");
	}

	@Override
	public void render(Graphics2D g) {
		renderEntity(getSprite(), g);
	}
	
	@Override
	public void dispose() {
		sprites = null;
	}

}
