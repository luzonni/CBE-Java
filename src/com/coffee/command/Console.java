package com.coffee.command;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.coffee.Inputs.Keyboard;
import com.coffee.Inputs.Button.TextButton;
import com.coffee.graphics.FontG;
import com.coffee.main.Engine;
import com.coffee.ui.UserInterface;

public class Console {
	
	private Chat chat;
	private List<String[]> lastCommands;
	
	protected TextButton textButton;
	
	public Console() {
		chat = new Chat(FontG.font(10 * Engine.GameScale));
		lastCommands = new ArrayList<String[]>();
		textButton = new TextButton("Command", 50, 95, null, 10);
	}
	
	public void print(String value, boolean write) {
		if(write)
			value = "%auto/"+value;
		chat.add(value);
		chat.tick();
	}
	
	public void tick() {
		Keyboard.Writing(textButton.isSelect());
		String[] commands = textButton.getText(null);
		if(commands != null) {
			String Callback = "Wrong command";
			try {
				Callback = UserInterface.getReceiver().giveCommand(commands);
			}catch (Exception e) {
				e.printStackTrace();
			}
			if(!Callback.equals("")) {
				print(Callback, true);
				lastCommands.add(commands);
			}
		}
		chat.tick();
	}
	
	public void render(Graphics2D g) {
		textButton.render(g);
		chat.render(g);
	}

	public void clearChat() {
		chat.clear();
	}

	public boolean isWriting() {
		return chat.isWriting();
	}

}
