package com.coffee.main.tools;

public class Timer {
	
	private int nowTick;
	private int dis;
	private boolean pit;
	
	public Timer(int seconds) {
		this.dis = seconds*60;
	}
	
	public boolean tiktak() {
		nowTick++;
		if(nowTick >= dis) {
			nowTick = 0;
			return true;
		}
		return false;
	}
	
	public boolean pit() {
		if(tiktak()) {
			pit = !pit;
		}
		return pit;
	}
	
}
