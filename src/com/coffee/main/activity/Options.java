package com.coffee.main.activity;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.coffee.Inputs.Button.Button;
import com.coffee.command.Receiver;
import com.coffee.graphics.FontG;
import com.coffee.main.Engine;
import com.coffee.main.tools.Responsive;
import com.coffee.ui.UserInterface;

public class Options implements Activity, Receiver {
	
	private Responsive center;
	
	private Responsive volume;
	private Responsive colors;
	private Responsive resolution;
	private Responsive scale;
	private Responsive setter;
	
	private int PALLET;
	private int RESOLUTION;
	private int GAME_SCALE;
	
	private Map<String, Button> buttons;
	
	public Options() {
		center = Responsive.createPoint(null, 50, 50);
	}

	@Override
	public void enter() {
		PALLET = Engine.INDEX_PALLET;
		RESOLUTION = Engine.INDEX_RES;
		GAME_SCALE = Engine.GameScale;
		UserInterface.getConsole().print("if you want to help contact me!", true);
		buttons = new HashMap<String, Button>();
		volume = Responsive.createPoint(center, 0, -50*Engine.GameScale);
		buttons.put("plus_volum", new Button("->", -12*Engine.GameScale, 0, volume, 8));
		buttons.put("minus_volum", new Button("<-", 12*Engine.GameScale, 0, volume, 8));
		
		colors = Responsive.createPoint(volume, 0, 35*Engine.GameScale);
		buttons.put("plus_color", new Button("->", -12*Engine.GameScale, 0, colors, 8));
		buttons.put("minus_color", new Button("<-", 12*Engine.GameScale, 0, colors, 8));
		
		resolution = Responsive.createPoint(colors, 0, 35*Engine.GameScale);
		buttons.put("plus_resolution", new Button("->", 12*Engine.GameScale, 0, resolution, 8));
		buttons.put("minus_resolution", new Button("<-", -12*Engine.GameScale, 0, resolution, 8));
		
		scale = Responsive.createPoint(resolution, 0, 25*Engine.GameScale);
		buttons.put("plus_scale", new Button("->", 12*Engine.GameScale, 0, scale, 8));
		buttons.put("minus_scale", new Button("<-", -12*Engine.GameScale, 0, scale, 8));
		
		setter = Responsive.createPoint(scale, 0, 20*Engine.GameScale);
		buttons.put("setter", new Button("Set", 0, 0, setter, 8));
	}
	
	@Override
	public void tick() {
		if(buttons == null || buttons.isEmpty())
			return;
		if(buttons.get("plus_volum").function() && Engine.Volume + 5 <= 100)
			Engine.Volume += 5;
		if(buttons.get("minus_volum").function() && Engine.Volume - 5 >= 0)
			Engine.Volume -= 5;
		if(buttons.get("plus_color").function() && this.PALLET + 1 <= Engine.PALLET.length-1)
			this.PALLET ++;
		if(buttons.get("minus_color").function() && this.PALLET - 1 >= 0)
			this.PALLET --;
		if(buttons.get("plus_resolution").function() && this.RESOLUTION + 1 <= Engine.resolutions.length-1)
			this.RESOLUTION ++;
		if(buttons.get("minus_resolution").function() && this.RESOLUTION - 1 >= 0)
			this.RESOLUTION --;
		if(buttons.get("plus_scale").function() && this.GAME_SCALE + 1 <= 6)
			this.GAME_SCALE ++;
		if(buttons.get("minus_scale").function() && this.GAME_SCALE - 1 >= 2)
			this.GAME_SCALE --;
		if(buttons.get("setter").function()) {
			Engine.ME.setConfig(Engine.Volume, PALLET, RESOLUTION, GAME_SCALE);
			Engine.restart();
			UserInterface.getConsole().print("Restart the game for changes with (*) to be properly applied", true);
		}
	}

	@Override
	public void render(Graphics2D g) {
		buttons.forEach((name, button) -> button.render(g));
		render_volum(g);
		render_colors(g);
		render_resolution(g);
		render_scale(g);
	}
	
	private void render_volum(Graphics2D g) {
		if(!buttons.containsKey("plus_volum") || !buttons.containsKey("minus_volum"))
			return;
		String value = "Volum: " + Engine.Volume;
		Font font = FontG.font(12*Engine.GameScale);
		int wF = FontG.getWidth(value, font);
		int hF = FontG.getHeight(value, font);
		int x = volume.getPosition().x - wF/2;
		int y = volume.getPosition().y + hF/2;
		buttons.get("plus_volum").getResponsive().setRelative(wF/2 + 2*Engine.GameScale, 0);
		buttons.get("minus_volum").getResponsive().setRelative(-wF/2 - 2*Engine.GameScale, 0);
		g.setFont(font);
		g.setColor(Engine.Color_Tertiary);
		g.drawString(value, x + Engine.GameScale, y + Engine.GameScale);
		g.setColor(Engine.Color_Primary);
		g.drawString(value, x, y);
	}
	
	private void render_colors(Graphics2D g) {
		if(!buttons.containsKey("plus_color") || !buttons.containsKey("minus_color"))
			return;
		String value = "*Colors:";
		Font font = FontG.font(12*Engine.GameScale);
		int wF = FontG.getWidth(value, font);
		int hF = FontG.getHeight(value, font);
		int xS = colors.getPosition().x - wF/2;
		int yS = colors.getPosition().y - hF;
		g.setFont(font);
		g.setColor(Engine.Color_Tertiary);
		g.drawString(value, xS + Engine.GameScale, yS + Engine.GameScale);
		g.setColor(Engine.Color_Primary);
		g.drawString(value, xS, yS);
		int size = 12*Engine.GameScale;
		int wB = Engine.PALLET[this.PALLET].length * size;
		int xB = colors.getPosition().x - wB/2;
		int yB = colors.getPosition().y - size/2;
		for(int i = 0; i < Engine.PALLET[this.PALLET].length; i++) {
			g.setColor(Engine.PALLET[this.PALLET][i]);
			g.fillRect(xB + i*size, yB, size, size);
		}
		buttons.get("plus_color").getResponsive().setRelative(wB/2 + 2*Engine.GameScale, 0);
		buttons.get("minus_color").getResponsive().setRelative(-wB/2 - 2*Engine.GameScale, 0);
	}
	
	private void render_resolution(Graphics2D g) {
		if(!buttons.containsKey("plus_resolution") || !buttons.containsKey("minus_resolution"))
			return;
		String value = "*Resolution:";
		Font font = FontG.font(12*Engine.GameScale);
		int wF = FontG.getWidth(value, font);
		int hF = FontG.getHeight(value, font);
		int xS = resolution.getPosition().x - wF/2;
		int yS = resolution.getPosition().y - hF;
		g.setFont(font);
		g.setColor(Engine.Color_Tertiary);
		g.drawString(value, xS + Engine.GameScale, yS + Engine.GameScale);
		g.setColor(Engine.Color_Primary);
		g.drawString(value, xS, yS);
		String value_res = Engine.resolutions[this.RESOLUTION][0] + " / " + Engine.resolutions[this.RESOLUTION][1];
		int wF_res = FontG.getWidth(value_res, font);
		int hF_res = FontG.getHeight(value_res, font);
		int xS_res = resolution.getPosition().x - wF_res/2;
		int yS_res = resolution.getPosition().y + hF_res/2;
		g.setFont(font);
		g.setColor(Engine.Color_Tertiary);
		g.drawString(value_res, xS_res + Engine.GameScale, yS_res + Engine.GameScale);
		g.setColor(Engine.Color_Primary);
		g.drawString(value_res, xS_res, yS_res);
		buttons.get("plus_resolution").getResponsive().setRelative(wF_res/2 + 2*Engine.GameScale, 0);
		buttons.get("minus_resolution").getResponsive().setRelative(-wF_res/2 - 2*Engine.GameScale, 0);
	}
	
	private void render_scale(Graphics2D g) {
		if(!buttons.containsKey("plus_scale") || !buttons.containsKey("minus_scale"))
			return;
		String value = "*Game Scale: " + this.GAME_SCALE;
		Font font = FontG.font(12*Engine.GameScale);
		int wF = FontG.getWidth(value, font);
		int hF = FontG.getHeight(value, font);
		int x = scale.getPosition().x - wF/2;
		int y = scale.getPosition().y + hF/2;
		buttons.get("plus_scale").getResponsive().setRelative(wF/2 + 2*Engine.GameScale, 0);
		buttons.get("minus_scale").getResponsive().setRelative(-wF/2 - 2*Engine.GameScale, 0);
		g.setFont(font);
		g.setColor(Engine.Color_Tertiary);
		g.drawString(value, x + Engine.GameScale, y + Engine.GameScale);
		g.setColor(Engine.Color_Primary);
		g.drawString(value, x, y);
	}
	
	@Override
	public String giveCommand(String[] keys) {
		String call_back = "I didn't understand";
		
		return call_back;
	}

	@Override
	public void dispose() {
		buttons.clear();
	}

}
