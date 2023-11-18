package com.coffee.command;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.coffee.graphics.FontG;
import com.coffee.main.Engine;
import com.coffee.ui.UserInterface;

class Chat {
	
	private int x, y;
	private Font font;
	private List<BoxPhrase> phrases;
	
	protected Chat(Font font) {
		this.font = font;
		phrases = new ArrayList<BoxPhrase>();
	}
	
	public boolean isWriting() {
		if(phrases.isEmpty())
			return false;
		return phrases.get(0).writing;
	}
	
	protected void add(String phrase) {
		phrases.add(0, new BoxPhrase(phrase));
	}
	
	protected void clear() {
		phrases.clear();
	}
	
	protected void tick() {
		x = Engine.getWidth() - 4*Engine.GameScale;
		y = UserInterface.getConsole().textButton.getBounds().y - 5*Engine.GameScale;
		for(int i = 0; i < phrases.size(); i++)
			phrases.get(i).tick();
		for(int i = 0; i < phrases.size(); i++) {
			Rectangle rec = phrases.get(i).bounds;
			if(i > 0) {
				Rectangle rec_parent = phrases.get(i-1).bounds;
				rec.setLocation(x - rec.width, rec_parent.y - rec.height);
			} else
				rec.setLocation(x - rec.width, y - rec.height);
		}
	}
	
	protected void render(Graphics2D g) {
		for(int i = 0; i < phrases.size(); i++) {
			g.setColor(new Color(Engine.Color_Tertiary.getRed(), Engine.Color_Tertiary.getGreen(), Engine.Color_Tertiary.getBlue(), 120));
			Rectangle rec = phrases.get(i).bounds;
			g.fillRect(rec.x, rec.y, rec.width, rec.height);
		}
		for(int i = 0; i < phrases.size(); i++) {
			phrases.get(i).render(g);
			g.setColor(Engine.Color_Primary);
			//Line top
			Rectangle rec_top = phrases.get(phrases.size()-1).bounds;
			g.drawLine(rec_top.x, rec_top.y, rec_top.x + rec_top.width, rec_top.y);
			//Line down
			Rectangle rec_down = phrases.get(0).bounds;
			g.drawLine(rec_down.x, rec_down.y + rec_down.height, rec_down.x + rec_down.width, rec_down.y + rec_down.height);
			//Line Right
			Rectangle rec_right_1 = phrases.get(0).bounds;
			Rectangle rec_right_2 = phrases.get(phrases.size()-1).bounds;
			g.drawLine(rec_right_1.x + rec_right_1.width, rec_right_1.y + rec_right_1.height, rec_right_2.x + rec_right_2.width, rec_right_2.y);
			//Line Right
			if(i < phrases.size()-1) {
				Rectangle rec_left_1 = phrases.get(i).bounds;
				Rectangle rec_left_2 = phrases.get(i+1).bounds;
				g.drawLine(rec_left_1.x, rec_left_1.y + rec_left_1.height, rec_left_1.x, rec_left_1.y);
				g.drawLine(rec_left_1.x, rec_left_1.y, rec_left_2.x, rec_left_2.y + rec_left_2.height);
			}else {
				Rectangle rec_left_1 = phrases.get(i).bounds;
				g.drawLine(rec_left_1.x, rec_left_1.y + rec_left_1.height, rec_left_1.x, rec_left_1.y);		
			}
		}
	}
	
	private class BoxPhrase {
		
		private String base_write;
		private StringBuilder phrase;
		private Rectangle bounds;
		private int padding;
		private int timer;
		private final int life = 60*15;
		private boolean writing;
		private int index_writing;
		private int time_write;
		
		public BoxPhrase(String phrase) {
			if(phrase.contains("%auto/")) {
				writing = true;
				phrase = phrase.split("%auto/")[1];
				this.phrase = new StringBuilder();
				this.base_write = phrase;
			}else
				this.phrase = new StringBuilder(phrase);
			this.padding = 4 * Engine.GameScale;
		}
		
		public void tick() {
			if(writing)
				writing();
			if(!writing)
				timer++;
			if(timer >= life) {
				phrases.remove(this);
			}
			int w = FontG.getWidth(phrase.toString(), font) + padding * 2;
			int h = FontG.getHeight(phrase.toString(), font) + padding * 2;
			this.bounds = new Rectangle(w, h);
		}
		
		private void writing() {
			time_write++;
			if(time_write >= 3) {
				time_write = 0;
				phrase.append(base_write.charAt(index_writing));
				index_writing++;
				if(index_writing >= base_write.length())
					writing = false;
			}
		}
		
		public void render(Graphics2D g) {
			g.setColor(Engine.Color_Primary);
			g.setFont(font);
			float f = Math.abs((float)timer - (float)life)/(float)life;
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (f)));
			g.drawString(phrase.toString(), bounds.x + padding, bounds.y + bounds.height/2 + padding);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}
		
	}
	
}
