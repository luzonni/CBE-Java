package com.coffee.main.ui.win;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.coffee.Inputs.Mouse;
import com.coffee.Inputs.Mouse_Button;
import com.coffee.graphics.FontG;
import com.coffee.main.Engine;

public abstract class View {
	
	private Font font;
	private Rectangle bar;
	private Rectangle showButton;
	private Rectangle boxContent;
	private boolean show;
	private boolean holding;
	private int def_x = 0;
	private int def_y = 0;
	private String name;
	
	public abstract void init();
	
	public abstract void tick();
	
	public View(String name) {
		this.name = name;
		this.font = FontG.font(8*Engine.GameScale);
		bar = new Rectangle(72*Engine.GameScale, 12*Engine.GameScale);
		showButton = new Rectangle(12*Engine.GameScale, 12*Engine.GameScale);
		boxContent = new Rectangle(84*Engine.GameScale, 0);
	}
	
	public void setLocation(int x, int y) {
		this.bar.setLocation(x, y);
	}
	
	protected void resize(int w, int h) {
		int wS = showButton.width;
		if(w < 72*Engine.GameScale)
			w = 72*Engine.GameScale;
		bar.setSize(w - wS, bar.height);
		boxContent.setSize(w, h);
	}
	
	protected boolean showing() {
		return this.show;
	}
	
	protected Font font() {
		return this.font;
	}
	
	public Rectangle bounds() {
		return new Rectangle(bar.x, bar.y + bar.height, bar.width + showButton.width, bar.height + boxContent.height);
	}
	
	private void barSystem() {
		showButton.setLocation(bar.x + bar.width, bar.y);
		boxContent.setLocation(bar.x, bar.y + bar.height);
		marginOut();
		if(bar.contains(Mouse.getX(), Mouse.getY())) 
			if(Mouse.pressing(Mouse_Button.LEFT)) 
				this.holding = true;
		else {
			this.holding = false;
			def_x = bar.x - Mouse.getX();
			def_y = bar.y - Mouse.getY();
		}
		
		if(holding) {
			bar.setLocation(Mouse.getX() + def_x, Mouse.getY() + def_y);
			showButton.setLocation(bar.x + bar.width, bar.y);
			boxContent.setLocation(bar.x, bar.y + bar.height);
		}else if(Mouse.clickOn(Mouse_Button.LEFT, showButton))
			this.show = !this.show;
	}
	
	private void marginOut() {
		if(bar.x < 0)
			bar.x = 0;
		if(bar.x + bar.width + showButton.width > Engine.getWidth())
			bar.x = Engine.getWidth() - bar.width - showButton.width;
		if(bar.y < 0)
			bar.y = 0;
		if(bar.y + bar.height > Engine.getHeight())
			bar.y = Engine.getHeight() - bar.height;
	}
	
	public void render(Graphics2D g) {
		barSystem();
		g.setStroke(new BasicStroke(Engine.GameScale));
		g.setColor(Engine.Color_Tertiary);
		g.fillRect(bar.x, bar.y, bar.width + showButton.width, bar.height + boxContent.height);
		renderBar(g);
		g.setColor(Engine.Color_Primary);
		g.drawRect(boxContent.x, boxContent.y, boxContent.width, boxContent.height);
	}
	
	private void renderBar(Graphics2D g) {
		g.setColor(Engine.Color_Primary);
		g.drawRect(bar.x, bar.y, bar.width, bar.height);
		if(!show)
			g.drawLine(showButton.x + showButton.width/2, showButton.y + showButton.height/2 - 8, showButton.x + showButton.width/2, showButton.y + showButton.height/2 + 8);
		g.drawLine(showButton.x + showButton.width/2 - 8, showButton.y + showButton.height/2, showButton.x + showButton.width/2 + 8, showButton.y + showButton.height/2);
		g.drawRect(showButton.x, showButton.y, showButton.width, showButton.height);
		g.setFont(font);
		int hF = FontG.getHeight(name, font);
		g.drawString(name, bar.x + 8, bar.y + bar.height/2 + hF/2);
	}

}
