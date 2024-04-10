package com.coffee.Inputs.Button;

import com.coffee.main.tools.ActionBack;
import com.coffee.main.tools.Responsive;

public class ActionButton extends Button {

	private ActionBack actionBack;

	public ActionButton(String Name, int x_per, int y_per, Responsive parent, int size, ActionBack actionBack) {
		super(Name, x_per, y_per, parent, size);
		this.actionBack = actionBack;
	}
	
	public void action() {
		actionBack.function();
	}


}
