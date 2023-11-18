package com.coffee.items;

import com.coffee.command.Commands;

public class CMD extends Item {

	private Commands command;

	public CMD(Commands comman) {
		super(comman.getName());
		setSprite(comman.getName()+"_command");
		this.command = comman;
	}
	
	public Commands getCMD() {
		return this.command;
	}

}
