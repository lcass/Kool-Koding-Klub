package com.lcass.game;

import com.lcass.core.Core;
import com.lcass.util.InputHandler;
/*
 * Make sure to document your code so we can understand what each section does, i'll try to do it as much as possible for the functions I wrote.
 * If you write a function you can use /** to add documentation
 * ctrl-shift-o automatically imports any stuff you used from the current source.
 */
public class Game {//currently just implements basic user input , ticks. 
	public Core core;
	private InputHandler input;
	private Ship ship;
	//EG
	/**
	 * This is the Game constructor
	 * @param core pass in a core object.
	 */
	public Game(Core core) {
		this.core = core;
		input = new InputHandler(core);
		ship = new Ship(this);
		core.util.bind_function(core.util.obtain_method(Game.class, "tick"), this);//Add the tick function to main gameloop
	}
	public void tick() {
		input.tick();
		ship.input(input.w, input.space, input.obtain_mouse());
		ship.tick();
	}
}
