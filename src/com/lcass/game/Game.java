package com.lcass.game;

import com.lcass.core.Core;
import com.lcass.graphics.Vertex2d;
import com.lcass.util.InputHandler;
/*
 * Make sure to document your code so we can understand what each section does, i'll try to do it as much as possible for the functions I wrote.
 * If you write a function you can use /** to add documentation
 * ctrl-shift-o automatically imports any stuff you used from the current source.
 */
public class Game {//currently just implements basic user input , ticks. 
	public Core core;
	public static final int TILE_SIZE = 32;//size of a tile in pixel
	public static final int WORLD_SIZE = 32;//size of the world in floor.
	private InputHandler input;
	private Planet planet;
	public Game(Core core) {
		this.core = core;
		input = new InputHandler(core);
		
		core.util.bind_function(core.util.obtain_method(Game.class, "tick"), this);//Add the tick function to main gameloop
		planet = new Planet(this);//planet should handle all objects, everything relating to player input goes outside of it.
	}
	public void tick() {	
		input.tick();
		planet.tick();
	}
}
