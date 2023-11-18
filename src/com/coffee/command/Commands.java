package com.coffee.command;

public enum Commands {
	
	select(1, "Used to select a object", "Just write this and click on"),
	clear(1, "Clear selected objects"), 
	move(3, "Use to move a selected object", "\"move x y\" (The values will be added to the current position)"),
	remove(1, "Use to remove a selected object"),
	shot(2, "Use to shot a arrow", "\"shot up\"  (Which side do you want to shoot)"),
	use(2, "Use to use an item on some entity", "use \"entity name\" \"item name\""),
	put(4, "Use to place an object from your inventory on the map", "put \"objct name\" X Y (position relative to you)");
	
	int length;
	String text_help;
	String command_help;
	
	Commands(int length) {
		this.length = length;
	}
	
	Commands(int length, String help) {
		this.length = length;
		this.text_help = help;
	}
	
	Commands(int length, String help, String CommandHelp) {
		this.length = length;
		this.text_help = help;
		this.command_help = CommandHelp;
	}
	
	public String getName() {
		String name = name();
		name = name.replace("_", " ");
		name = name.strip();
		return name.toLowerCase();
	}
	
	public boolean hasLength(String[] values) {
		return values.length == this.length;
	}

	public String getTextHelp() {
		if(text_help == "" || text_help == null)
			return "Maybe you don't need help";
		return text_help;
	}
	
	public String getCommandHelp() {
		if(command_help == "" || command_help == null)
			return "Maybe you don't need help";
		return command_help;
	}
	
}
