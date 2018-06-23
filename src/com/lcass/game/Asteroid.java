package com.lcass.game;

import com.lcass.graphics.Vertex2d;

public class Asteroid {
	public Vertex2d position;
	public Vertex2d velocity;
	public int render_id = -1;// null id for renderer
	public float mass = 1;
	public Vertex2d sprite = new Vertex2d(0,0,16,16);
	public Asteroid() {
		velocity = position = new Vertex2d();
		
	}
	public Asteroid(float mass, Vertex2d position) {
		this.position = new Vertex2d(position.x,position.y);
		velocity = new Vertex2d(position.u,position.v);
		this.mass = mass;
	}
	public void move() {
		position.add(velocity);
	}
	/**
	 * returns a copy of position.
	 * @return
	 */
	public Vertex2d get_position() {
		return position.whole();//whole creates a copy of the vector, so we dont edit the original.
	}
}
