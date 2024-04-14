package com.coffee.Inputs.Button;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.coffee.Inputs.Mouse;
import com.coffee.graphics.SpriteSheet;
import com.coffee.main.Engine;
import com.coffee.main.tools.ActionBack;
import com.coffee.main.tools.Responsive;

public class MenuButton extends Button {
	
	private List<ActionButton> actionButtons;
	private boolean showing;
	private BufferedImage icon[];
	private int indexAnim;
	private int size;
	
	public MenuButton(int x_per, int y_per, Responsive parent, int size) {
		super("Menu", x_per, y_per, parent, size);
		this.size = size;
		icon = buildIcon();
		getResponsive().setSize(icon[0].getWidth(), icon[0].getHeight());
		actionButtons = new ArrayList<ActionButton>();
	}
	
	private BufferedImage[] buildIcon() {
		SpriteSheet sp = new SpriteSheet(Engine.ResPath+"/ui/menubutton.png", Engine.GameScale);
		sp.replaceColor(Engine.PRIMARY, Engine.Color_Primary.getRGB());
		sp.replaceColor(Engine.SECUNDATY, Engine.Color_Secondary.getRGB());
		sp.replaceColor(Engine.TERTIARY, Engine.Color_Tertiary.getRGB());
		BufferedImage[] icons = new BufferedImage[sp.getWidth()/10];
		for(int i = 0; i < icons.length; i++) {
			icons[i] = sp.getSprite(i*10, 0, 10, 10);
		}
		return icons;
	}
	
	public void addOption(String name, ActionBack action) {
		Responsive res = getResponsive();
		if(!actionButtons.isEmpty()) 
			res = actionButtons.get(actionButtons.size()-1).getResponsive();
		ActionButton acb = new ActionButton(name, Engine.GameScale*3, 0, res, Engine.GameScale * size, action);
		actionButtons.add(acb);
	}

	public void clearOption() {
		this.actionButtons.clear();
	}
	
	public boolean overMenu() {
		if(Mouse.On_Mouse(getBounds()))
			return true;
		if(this.showing)
			for(int i = 0; i < actionButtons.size(); i++)
				if(Mouse.On_Mouse(actionButtons.get(i).getBounds()))
					return true;
		return false;
	}

	public void hide() {
		this.showing = false;
	}
	
	@Override
	public boolean function() {
		anim();
		if(showing) {
			for(int i = 0; i < actionButtons.size(); i++) {
				ActionButton acb = actionButtons.get(i);
				if(acb.function()) {
					acb.action();
					hide();
				}
			}
		}
		if(super.function()) {
			this.showing = !this.showing;
		}
		return super.function();
	}
	
	private void anim() {
		if(showing) {
			if(indexAnim < icon.length-1)
				indexAnim++;
		}else {
			if(indexAnim > 0)
				indexAnim--;
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		g.drawImage(icon[indexAnim], getBounds().x, getBounds().y, null);
		if(showing) {
			for(int i = 0; i < actionButtons.size(); i++) {
				actionButtons.get(i).render(g);
			}
		}
		
	}
	
}
