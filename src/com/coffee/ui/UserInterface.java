package com.coffee.ui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coffee.Inputs.Button.Button;
import com.coffee.command.Console;
import com.coffee.command.Receiver;
import com.coffee.main.Engine;
import com.coffee.main.activity.Menu;
import com.coffee.ui.win.View;

public class UserInterface {
	
	private static Receiver RECEIVER;
	private static Console console;
	private static Map<String, Button> buttons;
	private static ActionBack action_back;
	
	public static List<View> views;

	public UserInterface() {
		buttons = new HashMap<String, Button>();
		buttons.put("back", new Button("Back", 5, 5, null, 10));
		console = new Console();
		views = new ArrayList<View>();
	}

	public static void setActionBack(ActionBack action) {
		UserInterface.action_back = action;
	}
	
	//TODO melhorar sistema de views
	public static void addView(View newView) {
		if(views.contains(newView))
			return;
		newView.init();
		int x = Engine.getWidth() - newView.bounds().width - 4*Engine.GameScale;
		int y = 4*Engine.GameScale;
		newView.setLocation(x, y + (newView.bounds().height + 4*Engine.GameScale) * (views.size()));
		views.add(newView);
		
	}
	
	public static void clearViews() {
		views.clear();
	}
	
	public static void setReceiver(Receiver receiver) {
		RECEIVER = receiver;
	}

	public static Receiver getReceiver() {
		return RECEIVER;
	}
	
	public static Console getConsole() {
		return UserInterface.console;
	}
	
	public static Map<String, Button> getButtons(){
		return UserInterface.buttons;
	}
	
	public synchronized void tick() {
		console.tick();
		if(!(Engine.ACTIVITY instanceof Menu))
			if(buttons.get("back").function()) {
				if(action_back != null) {
					action_back.function();
					action_back = null;
				}else
					Engine.setActivity(new Menu());
			}
		for(int i = 0; i < views.size(); i++)
			views.get(i).tick();
	}
	
	public void render(Graphics2D g) {
		console.render(g);
		if(!(Engine.ACTIVITY instanceof Menu)) {
			String[] names = buttons.keySet().toArray(new String[0]);
			for(int i = 0; i < names.length; i++) {
				buttons.get(names[i]).render(g);
			}
		}
		for(int i = 0; i < views.size(); i++) {
			views.get(i).render(g);
		}
	}
	
}
