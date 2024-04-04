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

	private Thread thread;
	public volatile boolean isRunning;
	
	private Activity activity;
	private int STEP;
	private volatile Object waiter;
	
	public Transition(Activity activity, Object waiter) {
		this.activity = activity;
		this.waiter = waiter;
		thread = new Thread(this, "Thread - Transition");
		isRunning = true;
		thread.start();
	}
	
	public static void start(Activity activity, Object waiter) {
		new Transition(activity, waiter);
		synchronized (waiter) {
			try {
				waiter.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void stop() {
		isRunning = false;
		synchronized (waiter) {
			this.waiter.notify();
		}
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
		
		Engine.Buffer.getDrawGraphics().drawImage(image, 0, 0, Engine.getWidth(), Engine.getHeight(), null);
		Engine.Buffer.show();
		
		STEP++;
		if(STEP == 10)
			transit();
		
		if(STEP == 20)
			stop();
	}
	
	private void transit() {
		Engine.ACTIVITY = activity;
		if(activity instanceof Receiver)
			UserInterface.setReceiver((Receiver)activity);
		Engine.ACTIVITY.enter();
		Engine.ACTIVITY_RUNNING = true;
		UserInterface.setReceiver(Engine.ACTIVITY);
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
