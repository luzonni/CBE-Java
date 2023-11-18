package com.coffee.objects;

public enum Directions {
	
	Idle(0, 0), Up(0, -1), Right(1, 0), Down(0, 1), Left(-1, 0);
	
	int x, y;
	
	private Directions(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int[] getDir() {
		return new int[] {x, y};
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
}
