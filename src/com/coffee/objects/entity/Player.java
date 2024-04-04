package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.Inputs.Keyboard;
import com.coffee.graphics.Flip;
import com.coffee.graphics.SpriteSheet;
import com.coffee.main.Engine;
import com.coffee.objects.Directions;
import com.coffee.objects.Variables;

public class Player extends Entity {
	
	private BufferedImage[][] sprite;
	private boolean collide;
	private int indexAnim;
	private int ticksAnim = 0;
	
	private Inventory inventory;
	
	private void buildSprites() {
		SpriteSheet sp = new SpriteSheet(Engine.ResPath+"/entity/Player.png", Engine.GameScale);
		sp.replaceColor(0xffffffff, Engine.Color_Primary.getRGB());
		sp.replaceColor(0xff000000, Engine.Color_Tertiary.getRGB());
		sprite = new BufferedImage[2][7];
		for(int i = 0; i < sprite[0].length; i++) {
			sprite[0][i] = sp.getSprite(i*16, 0);
		}
		for(int i = 0; i < sprite[1].length; i++) {
			sprite[1][i] = sp.getSprite(i*16, 16);
		}
	}
	
	public Player(int id, int x, int y) {
		super(id, x, y);
		buildSprites();
		inventory = new Inventory(3);
		getValues().addInt("speed", Engine.GameScale * 8);
		setDepth(3);
		setVar(Variables.Alive, true);
		setVar(Variables.Selectable, true);
		setVar(Variables.Movable, false);
		setVar(Variables.Removeble, false);
		getOE().setDirection(Directions.Down);
	}
	
	public void tick() {
		ticksAnim++;
		if(ticksAnim > 7) {
			ticksAnim = 0;
			indexAnim++;
		}
		if(indexAnim > sprite[getOE().colliding(getDirection()) ? 0 : 1].length - 1) {
			indexAnim = 0;
		}
		keyDirection();
		dynamics();
		getInventory().tick();
	}
	
	private void keyDirection() {
		if(!getOE().colliding(getDirection())) {
			collide = true;
			return;
		}
		if(collide) {
			collide = false;
//			Game.getCam().impact(getDirection(), 10);				
		}
		Directions new_dir = this.getDirection();
		if((Keyboard.KeyPressing("W") || Keyboard.KeyPressing("Up"))) 
			new_dir = Directions.Up;
		if((Keyboard.KeyPressing("D") || Keyboard.KeyPressing("Right"))) 
			new_dir = Directions.Right;
		if((Keyboard.KeyPressing("S") || Keyboard.KeyPressing("Down"))) 
			new_dir = Directions.Down;
		if((Keyboard.KeyPressing("A") || Keyboard.KeyPressing("Left"))) 
			new_dir = Directions.Left;
		if(!getDirection().equals(new_dir)) 
			setDirection(new_dir);
	}
	
	private void dynamics() {
		switch(getDirection()) {
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
	
	@Override
	public String giveCommand(String[] values) {
		String message = super.giveCommand(values);
		
		switch (values[0]) {
		case "speed", "Speed": 
			setSpeed(Integer.parseInt(values[1]));
			message = "Speed change to " + values[1];
			break;
		}
		return message;
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	public int getSpeed() {
		return this.getValues().getInt("speed");
	}
	
	public void setSpeed(int newSpeed) {
		this.getValues().setInt("speed", newSpeed);
	}
	
	@Override
	public BufferedImage getSprite() {
		return sprite[0][indexAnim];
	}
	
	public BufferedImage Sprite() {
		BufferedImage image = sprite[getOE().colliding(getDirection()) ? 0 : 1][indexAnim];
		switch (getDirection()) {
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
	public void render(Graphics2D g) {
		getInventory().render(g);
		renderEntity(Sprite(), g);
		
	}
	
	@Override
	public void dispose() {
		
	}
}
