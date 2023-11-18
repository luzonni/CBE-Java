package com.coffee.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.coffee.command.Receiver;
import com.coffee.graphics.FontG;
import com.coffee.main.activity.Activity;
import com.coffee.ui.UserInterface;

class Transition implements Runnable {

	private static Thread thread;
	private static boolean isRunning;
	
	private BufferedImage image;
	private Activity activity;
	private int STEP;
	
	public Transition(Activity activity) {
		this.activity = activity;
	}
	
	public synchronized void Start() {
		thread = new Thread(this, "Thread - Game");
		isRunning = true;
		thread.start();
	}
	
	private void Stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		isRunning = false;
	}
	
	private void tick() {
		int W = Engine.getWidth()/Engine.GameScale;
		int H = Engine.getHeight()/Engine.GameScale;
		BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
		int[] rgb = new int[W*H];
		for(int y = 0; y < H; y++)
			for(int x = 0; x < W; x++) {
				if(Engine.RAND.nextBoolean())
					rgb[x+y*W] = Engine.Color_Tertiary.getRGB();
				else if(Engine.RAND.nextBoolean())
					rgb[x+y*W] = Engine.Color_Primary.getRGB();
				else {
					rgb[x+y*W] = new Color(Engine.Color_Primary.getRed(), Engine.Color_Primary.getGreen(), Engine.Color_Primary.getBlue(), Engine.RAND.nextInt(255)).getRGB();
				}
			}
		image.setRGB(0, 0, W, H, rgb, 0, W);
		Graphics g = image.getGraphics();
		g.setColor(new Color(Engine.Color_Secondary.getRed(), Engine.Color_Secondary.getGreen(), Engine.Color_Secondary.getBlue(), 180));
		g.fillRect(0, Engine.RAND.nextInt(Engine.getHeight()), W, H/6);
		
		if(Engine.ACTIVITY != null) {
			Font font = FontG.font(Engine.GameScale*8);
			String value = "Channel: " + Engine.ACTIVITY.getClass().getSimpleName();
			int wF = FontG.getWidth(value, font);
			int hF = FontG.getHeight(value, font);
			int padding = Engine.GameScale*3;
			g.setFont(font);
			g.setColor(Engine.Color_Tertiary);
			g.drawString(value, W - wF - padding + 1, hF + padding + 1);
			g.setColor(Engine.Color_Primary);
			g.drawString(value, W - wF - padding, hF + padding);
		}
		
		g.dispose();
		
		STEP++;
		if(STEP == 10)
			transit();
		
		setImage(image);
		if(STEP == 20)
			finish();
	}
	
	private void transit() {
		Engine.ACTIVITY = activity;
		if(activity instanceof Receiver)
			UserInterface.setReceiver((Receiver)activity);
		Engine.ACTIVITY.enter();
		Engine.ACTIVITY_RUNNING = true;
		UserInterface.setReceiver(Engine.ACTIVITY);
	}
	
	private void setImage(BufferedImage image) {
		this.image = image;
	}
	
	private void finish() {
		this.image = null;
		Stop();
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
	
	@Override
	public void run() {
		//System values
		long lastTimeHZ = System.nanoTime();
		double amountOfHz = Engine.HZ;
		double ns_HZ = Engine.T / amountOfHz;
		double delta_HZ = 0;
		while(isRunning) {
			try {
				long nowHZ = System.nanoTime();
				delta_HZ += (nowHZ - lastTimeHZ) / ns_HZ;
				lastTimeHZ = nowHZ;
				if(delta_HZ >= 1) {
					tick();
					delta_HZ--;
				}
				
			}catch(Exception e) {
				System.out.println("ERROR!");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	

}
