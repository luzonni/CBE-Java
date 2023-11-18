package com.coffee.command;

import com.coffee.main.Engine;
import com.coffee.main.activity.Game;

public interface Receiver {
	
	public String giveCommand (String[] keys);

	public default boolean take(String[] keys, Commands command) {
		ginomu : for(Commands k : Game.getLevel().getKeys()) {
			if(k.equals(command) && k.hasLength(keys)) {
				String[] cur_key = k.name().split("_");
				for(int i = 0; i < cur_key.length; i++)
					if(!cur_key[i].equalsIgnoreCase(keys[i]))
						break ginomu;
				return true;
			}
		}
		return false;
	}
	
	public default boolean used(Commands command) {
		if(Engine.ACTIVITY instanceof Game) {
			Game.getLevel().getKeys().remove(command);
			return true;
		}
		return false;
	}
	
}
