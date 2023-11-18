package com.coffee.creator;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics2D;

import com.coffee.main.Engine;
import com.coffee.objects.Objects;

public class Selected {
	
	private Objects SELECTED;
	
	private float f = 0;
	private boolean change;

	public Objects get() {
		return this.SELECTED;
	}
	
	public void set(Objects newSelected) {
		this.SELECTED = newSelected;
	}
	
	public void clear() {
		this.SELECTED = null;
	}
	
	public void tick() {
		if(change) 
			f += 0.02;
		else 
			f -= 0.02;
		if(f > 1f) {
			f = 1f;
			change = false;
		}
		if(f < 0) {
			f = 0;
			change = true;
		}
	}
	
	public void render(Graphics2D g) {
		Objects selected = get();
		if(selected != null) {
			int per = (int)(f*20);
			int x = selected.getBounds().x + per;
			int y = selected.getBounds().y + per;
			int w = selected.getBounds().width - per*2;
			int h = selected.getBounds().height - per*2;
			g.setStroke(new BasicStroke(Engine.GameScale + per));
			g.setColor(Engine.Color_Tertiary);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, f));
			g.drawRect(x, y, w, h);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}
	}
	
}
