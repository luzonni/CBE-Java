package com.coffee.main.activity;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics2D;

import com.coffee.Inputs.Mouse;
import com.coffee.Inputs.Mouse_Button;
import com.coffee.Inputs.Button.Button;
import com.coffee.command.Receiver;
import com.coffee.exceptions.Dead;
import com.coffee.level.Level;
import com.coffee.level.Next;
import com.coffee.main.Engine;
import com.coffee.main.tools.Responsive;
import com.coffee.objects.Camera;
import com.coffee.objects.Objects;
import com.coffee.objects.entity.Entity;
import com.coffee.objects.entity.Player;
import com.coffee.ui.UserInterface;
import com.coffee.ui.win.Helper;

public class Game implements Activity, Receiver {
	
	private Camera camera;
	private Level level;
	private Next next;
	public static Responsive middle = Responsive.createPoint(null, 50, 50);
	
	public static Helper helper;
	
	private int lastX_mouse, lastY_mouse;
	private float f = 0;
	private boolean change;

	public Game(Level level, Next next) {
		this.level = level;
		this.next = next;
		camera = new Camera();
		camera.start();
	}

	@Override
	public void enter() {
		this.level.selected = null;
		this.level.open();
		if(helper == null)
			helper = new Helper(level.getKeys());
		else
			helper.setCommands(level.getKeys());
		UserInterface.getButtons().put("restart", new Button("Restart", 2*Engine.GameScale, 0, UserInterface.getButtons().get("back").getResponsive(), 10));
		UserInterface.addView(helper);
	}
	
	@Override
	public String giveCommand(String[] keys) {	
		if(keys.length == 2 && keys[0].equalsIgnoreCase("clear") && keys[1].equalsIgnoreCase("chat")) {
			UserInterface.getConsole().clearChat();
			return "";
		}
		return level.giveCommand(keys);
	}
	
	public static void clearSelect() {
		getLevel().selected = null;
	}
	
	public static Player getPlayer() {
		for(Entity e : Game.getLevel().getEntities())
			if(e instanceof Player)
				return (Player)e;
		throw new Dead("Player is dead", null);
	}
	
	public static Objects getSelect() {
		return getLevel().selected;
	}
	
	public static Level getLevel() {
		Activity activity = Engine.ACTIVITY;
		if(activity instanceof Game)
			return ((Game) activity).level;
		throw new RuntimeException("Not in game");
	}
	
	public static Camera getCam() {
		Activity activity = Engine.ACTIVITY;
		if(activity instanceof Game)
			return ((Game) activity).camera;
		throw new RuntimeException("Not in game");
	}
	
	public void tick() {
		if(UserInterface.getButtons().get("restart").function()) {
			Game.restart();
		}
		level.tick();
		if(Mouse.pressing(Mouse_Button.SCROOL)) {
			int x = lastX_mouse - Mouse.getX();
			int y = lastY_mouse - Mouse.getY();
			getCam().setPosition(x, y);
		}else {
			lastX_mouse = Mouse.getX() + getCam().getX();
			lastY_mouse = Mouse.getY() + getCam().getY();
		}
		f();
	}
	
	public static void start(Level level) {
		Engine.setActivity(new Game(level, () -> {
			Level nextLevel = null;
			for(int i = 0; i < Engine.LEVELS.length; i++) 
				if(Engine.LEVELS[i].equalsIgnoreCase(level.getName()) && i + 1 < Engine.LEVELS.length) {
					nextLevel = new Level(Engine.LEVELS[i+1]);
					Engine.INDEX_LEVEL = i+1;
					break;
				}
			if(nextLevel != null) 
				Game.start(nextLevel);
			else 
				Engine.setActivity(new LevelMap(), () -> {
					Engine.setActivity(new Menu());
				});
		}), () -> {
			Engine.setActivity(new LevelMap(), () -> {
				Engine.setActivity(new Menu());
			});
		});
	}
	
	public static void restart() {
		if(!(Engine.ACTIVITY instanceof Game))
			throw new RuntimeException("Not in game");
		Game game = ((Game)Engine.ACTIVITY);
		Engine.setActivity(new Game(new Level(game.level.getLevel()), game.next));
	}
	
	public static void end() {
		Activity activity = Engine.ACTIVITY;
		if(!(activity instanceof Game)) 
			throw new RuntimeException("Not in game");
		((Game)activity).next.next();
	}
	
	private void f() {
		if(change) {
			f += 0.02;
		}else {
			f -= 0.02;
		}
		
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
		level.render(g);
		if(this.level.selected != null) {
			int per = (int)(f*20);
			int x = this.level.selected.getBounds().x - getCam().getX() + per;
			int y = this.level.selected.getBounds().y  - getCam().getY() + per;
			int w = this.level.selected.getBounds().width - per*2;
			int h = this.level.selected.getBounds().height - per*2;
			g.setStroke(new BasicStroke(Engine.GameScale + per));
			g.setColor(Engine.Color_Tertiary);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, f));
			g.drawRect(x, y, w, h);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}
	}
	
	public void dispose() {
		this.level.selected = null;
		this.camera.stop();
		this.level.dispose();
		UserInterface.clearViews();
		UserInterface.getButtons().remove("restart");
	}

}
