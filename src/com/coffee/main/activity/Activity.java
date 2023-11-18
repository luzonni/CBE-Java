package com.coffee.main.activity;

import java.awt.Graphics2D;

import com.coffee.command.Receiver;

public interface Activity extends Receiver {
	
	public void enter();
	
	public void tick();
	
	public void render(Graphics2D g);
	
	public void dispose();

}
