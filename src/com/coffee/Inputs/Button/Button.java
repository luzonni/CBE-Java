package com.coffee.Inputs.Button;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.coffee.Inputs.Mouse;
import com.coffee.Inputs.Mouse_Button;
import com.coffee.graphics.FontG;
import com.coffee.main.Engine;
import com.coffee.main.Sound;
import com.coffee.main.tools.Responsive;

public class Button {
	
	public BufferedImage[] sprites;
	
	protected String Name;
	protected Responsive P;
	public Font font;
	protected int margin;
	
	private void addSprite() {
		int wF = FontG.getWidth(this.Name, this.font) + margin;
		int hF = FontG.getHeight(this.Name, this.font) + margin;
		BasicStroke stroke = new BasicStroke(margin/3);
		sprites = new BufferedImage[] {new BufferedImage(wF, hF, BufferedImage.TYPE_INT_ARGB), new BufferedImage(wF, hF, BufferedImage.TYPE_INT_ARGB)};
		int width = sprites[0].getWidth();
		int height = sprites[0].getHeight();
		Graphics2D g = (Graphics2D) sprites[0].getGraphics();
		g.setFont(this.font);
		g.setColor(Engine.Color_Tertiary);
		g.fillRect(0, 0, width, height);
		g.setColor(Engine.Color_Primary);
		g.setStroke(stroke);
		g.drawRect(0, 0, width, height);
		g.setColor(Engine.Color_Primary);
		g.drawString(this.Name, width/2 - wF/2 + margin/2, height/2 + hF/4);
		g = (Graphics2D) sprites[1].getGraphics();
		g.setFont(this.font);
		g.setColor(Engine.Color_Secondary);
		g.fillRect(0, 0, width, height);
		g.setColor(Engine.Color_Primary);
		g.setStroke(stroke);
		g.drawRect(0, 0, width, height);
		g.setColor(Engine.Color_Primary);
		g.drawString(this.Name, width/2 - wF/2 + margin/2, height/2 + hF/4);
	}

	public Button(String Name, int x_per, int y_per, Responsive parent, int size) {
		this.font = FontG.font(Engine.GameScale*size);
		this.Name = Name;
		margin = size*3;
		addSprite();
		BufferedImage sprite = this.sprites[0];
		Rectangle rec = new Rectangle(sprite.getWidth(), sprite.getHeight());
		this.P = Responsive.createRectangle(parent, rec, x_per, y_per);
	}
	
	public Responsive getResponsive() {
		return this.P;
	}

	public Rectangle getBounds() {
		return this.P.getBounds();
	}
	
	public boolean function() {
		if(Mouse.clickOn(Mouse_Button.LEFT, getBounds())) {
			Sound.play("click");
			return true;
		}
		return false;
	}
	
	public void render(Graphics2D g) {
		if(this.P == null)
			return;
		this.getResponsive().setPosition();
		int over = Mouse.On_Mouse(getBounds()) ? 1 : 0;
		g.drawImage(this.sprites[over], getBounds().x, getBounds().y, null);
	}

}
