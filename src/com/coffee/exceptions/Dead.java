package com.coffee.exceptions;

import com.coffee.objects.entity.Entity;

public class Dead extends RuntimeException {

	private static final long serialVersionUID = 7L;
	private Entity entity;

	public Dead(String message, Entity entity) {
		super(message);
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return this.entity;
	}

}
