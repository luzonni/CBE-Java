package com.coffee.objects.ai;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import com.coffee.exceptions.NoWayFound;
import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.Directions;
import com.coffee.objects.entity.Entity;
import com.coffee.objects.tiles.Tile;

public class Path implements Runnable {

	private int range;
	private A_Star aStar;
	private Entity entity;
	private Point end;
	
	public void buildPath(Entity entity, Point End, int range) {
		this.entity = entity;
		this.end = End;
		this.range = range;
		new Thread(this).start();
	}
	
	public boolean follow(Directions dir) {
		if(aStar == null || aStar.getPath() == null || aStar.getPath().isEmpty() || entity == null)
			return false;
		if(colliding(currentTile()))
			aStar.getPath().clear();
		int x = (int)entity.getX();
		int y = (int)entity.getY();
		if(dir.equals(Directions.Left)) {
			x -= Engine.GameScale;
			entity.setX(x);
		}else if(dir.equals(Directions.Right)) {
			x += Engine.GameScale;
			entity.setX(x);
		}else if(dir.equals(Directions.Up)) {
			y -= Engine.GameScale;
			entity.setY(y);
		}else if(dir.equals(Directions.Down)) {
			y += Engine.GameScale;
			entity.setY(y);
		}
		return true;
	}
	
	public Directions direction() {
		Node node = currentTile();
		if(node != null) {
			int x = node.x * Tile.getSize();
			int y = node.y * Tile.getSize();
			if(entity.getX() > x) {
				return Directions.Left;
			}else if(entity.getX() < x) {
				return Directions.Right;
			}else if(entity.getY() > y) {
				return Directions.Up;
			}else if(entity.getY() < y) {
				return Directions.Down;
			}else {
				arrived();
			}
		}
		return Directions.Idle;
	}
	
	private boolean colliding(Node node) {
		Tile tile = Game.getLevel().getTile(node.x, node.y);
		if(tile == null)
			return true;
		return tile.isSolid();
	}
	
	private Node currentTile() {
		if(aStar != null && aStar.getPath() != null && !aStar.getPath().isEmpty()) {
			Node target = aStar.getPath().get(aStar.getPath().size() - 1);
			return target;
		}
		return null;
	}
	
	private void arrived() {
		aStar.getPath().remove(aStar.getPath().size() - 1);
	}

	public boolean isEmpty() {
		return (aStar == null || aStar.getPath() == null || aStar.getPath().isEmpty());
	}

	public void render(Graphics g) {
		if(aStar != null && aStar.getPath() != null)
			for(Node node : aStar.getPath()) {
				int xi = node.x*Tile.getSize() + Tile.getSize()/2;
				int yi = node.y*Tile.getSize() + Tile.getSize()/2;
				int xF = xi + Tile.getSize()/2;
				int yF = yi + Tile.getSize()/2;
				if(node.parent != null) {
					xF = node.parent.x*Tile.getSize() + Tile.getSize()/2;
					yF = node.parent.y*Tile.getSize() + Tile.getSize()/2;
				}
				Graphics2D g2 = (Graphics2D)g;
				g2.setStroke(new BasicStroke(Engine.GameScale));
				g2.setColor(Color.red);
				g2.drawLine(xi - Game.getCam().getX(), yi - Game.getCam().getY(), xF - Game.getCam().getX(), yF - Game.getCam().getY());
			}
	}

	@Override
	public void run() {
		try {
			aStar = new A_Star(entity.getMiddle(), end, range);
		} catch (NoWayFound e) {
			return;
		}
	}

}
