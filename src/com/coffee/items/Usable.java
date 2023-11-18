package com.coffee.items;

import com.coffee.objects.Variables;
import com.coffee.objects.entity.Entity;

public class Usable extends Item {

	private Variables var;

	public Usable(Variables var) {
		super(var.getName());
		setSprite(var.getName());
		this.var = var;
		this.setTimer(7);
	}
	
	public boolean set(String name, Entity entity) {
		if(getName().equalsIgnoreCase(name)) {
			entity.setEffect(var, getIcon());
			return true;
		}
		return false;
	}

}
