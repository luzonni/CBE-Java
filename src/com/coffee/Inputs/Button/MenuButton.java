package com.coffee.Inputs.Button;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.graphics.SpriteSheet;
import com.coffee.main.Engine;
import com.coffee.main.tools.Responsive;

public class MenuButton extends Button {
	
	private BufferedImage icon;
	
	public MenuButton(int x_per, int y_per, Responsive parent, int size) {
		super("Menu", x_per, y_per, parent, size);
		SpriteSheet sp = new SpriteSheet(Engine.ResPath+"/ui/menubutton.png", Engine.GameScale);
		sp.replaceColor(Engine.PRIMARY, Engine.Color_Primary.getRGB());
		sp.replaceColor(Engine.SECUNDATY, Engine.Color_Secondary.getRGB());
		sp.replaceColor(Engine.TERTIARY, Engine.Color_Tertiary.getRGB());
		icon = sp.getImage();
		getResponsive().setSize(icon.getWidth(), icon.getHeight());
	}
	
	@Override
	public void render(Graphics2D g) {
		g.drawImage(icon, getBounds().x, getBounds().y, null);
	}
	
}
