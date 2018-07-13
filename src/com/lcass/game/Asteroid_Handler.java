package com.lcass.game;

import java.util.ArrayList;

import com.lcass.core.Core;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.renderer;

public class Asteroid_Handler extends Thread{
	private boolean finished = true;
	private ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	private renderer renderer;
	private Game game;
	private float gravity_strength = 1;
	public Asteroid_Handler(Game game) {
		this.game = game;
		renderer = new renderer(game.core);
		
	}
	/**
	 * Add an asteroid.
	 * @param pos Take form (x_pos,y_pos,x_vel,y_vel)
	 */
	public void add_asteroid(Vertex2d pos,float mass) {
		Asteroid a = new Asteroid(mass,pos);
		a.render_id = renderer.create_object_static(pos, a.sprite);//need to do static, we will be adding a lot of asteroids at once
		asteroids.add(a);
	}
	public void remove_asteroid(Asteroid a) {
		renderer.remove_object_static(a.render_id);
		asteroids.remove(a);
	}
	@Override
	public void run() {//Enacts gravity from the asteroids onto the ship
		while(true) {//avoid writing your threads like this, I had already written all of this and didn't want to rework it.
		finished = false;
		//We cant remove elements of asteroids while reading it so build a delete queue instead.
		ArrayList<Asteroid> delete_queue = new ArrayList<Asteroid>();
		for(Asteroid a:asteroids) {
			if(!game.core.util.on_screen(a.position)) {//don't process asteroids we can't see thats just mean. This might have to change as otherwise there might be sudden jumps when asteroids leave screen
				delete_queue.add(a);
				continue;
			}
			//Compute gravity
			Vertex2d delta = a.position.sub(game.ship.get_position());//Delta in position.
			float dist_to_asteroid = delta.x * delta.x + delta.y * delta.y;//calc r^2
			delta.div((float)Math.sqrt(dist_to_asteroid)).div(dist_to_asteroid).mult(gravity_strength*a.mass);//convert to unit vector then apply gravity
			game.ship.accelerate(delta);//seems inefficient computing movement for each force, why not calc resultant for all asteroids and then move
			a.move();
			//update the locations of the asteroids on the screen.
			renderer.edit_object_static(a.render_id, a.get_position(), a.sprite);
		}
		for(Asteroid a: delete_queue) {
			remove_asteroid(a);
		}
		
		renderer.update_buffer();//We moved objects using static so update the buffer.
		
		finished = true;//let the main thread know we are finished and to wake up if it was held up.
		game.notify();
		try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public boolean finished() {
		return finished;
	}
}
