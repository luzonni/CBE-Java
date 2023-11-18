package com.coffee.objects;

import java.awt.Point;

import com.coffee.main.Engine;

public class Camera implements Runnable {
	
	
	private Thread thread;
	private boolean running;
	private int X, Y;
	private Point cur;
	private Point vector;
	//TODO melhorar sistema
	public synchronized void start() {
		cur = new Point();
		vector = new Point();
		running = true;
		thread = new Thread(this, "Camera");
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			this.thread.join();
		} catch (InterruptedException e) {}
	}
	
	public int getX() {
		return cur.x;
	}
	
	public int getY() {
		return cur.y;
	}

	public void setPosition(int x, int y) {
		this.X = x;
		this.Y = y;
		this.cur.setLocation(x, y);
	}
	
	public void impact(Directions dir, int force) {
		set(dir, force);
	}
	
	private void tick() {
		moving();
	}
	
	private void set(Directions directions, int amount) {
		if(directions.equals(Directions.Up)) {
			vector.y = Math.abs(amount) * -1;
		}else if(directions.equals(Directions.Down)) {
			vector.y = Math.abs(amount);
		}else if(directions.equals(Directions.Left)) {
			vector.x = Math.abs(amount) * -1;
		}else if(directions.equals(Directions.Right)) {
			vector.x = Math.abs(amount);
		}
	}
	
	private void moving() {
		if(this.vector.x != 0) {
			this.cur.x += this.vector.x;
			if(this.vector.x > 0)
				this.vector.x --;
			else if(this.vector.x < 0)
				this.vector.x ++;
		}else {
			if(this.cur.x > this.X)
				this.cur.x -= Math.abs(this.cur.x - this.X)/2;
			else if(this.cur.x < this.X)
				this.cur.x += Math.abs(this.cur.x - this.X)/2;
		}
		if(this.vector.y != 0) {
			this.cur.y += this.vector.y;
			if(this.vector.y > 0)
				this.vector.y --;
			else if(this.vector.y < 0)
				this.vector.y ++;
		}else {
			if(this.cur.y > this.Y)
				this.cur.y -= Math.abs(this.cur.y - this.Y)/2;
			else if(this.cur.y < this.Y)
				this.cur.y += Math.abs(this.cur.y - this.Y)/2;
		}
	}

	@Override
	public void run() {
		//System values
		long lastTimeHZ = System.nanoTime();
		double amountOfHz = Engine.HZ;
		double ns_HZ = Engine.T / amountOfHz;
		double delta_HZ = 0;
		while(running) {
			if(Thread.interrupted()) 
				running = false;
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
