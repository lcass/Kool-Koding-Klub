package com.lcass.game;

import com.lcass.core.Core;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.renderer;

public class Ship {
	private renderer renderer;
	private Core core;
	private Game game;
	private Vertex2d screen_pos,acceleration = new Vertex2d(),velocity= new Vertex2d(),position = new Vertex2d();//initialize to 0 vector
	private float angle = 0,thrust = 1;
	private int ship_icon;
	public Ship(Game game) {
		this.game = game;
		this.core = game.core;
		renderer = new renderer(core);//Using placeholder for now
		ship_icon = renderer.create_object(new Vertex2d(-16,-16), new Vertex2d(0,0,16,16));//placeholder sprite aswell, sprite centered at (0,0)
		renderer.set_render(true);//we do want to render this to the screen.
		screen_pos = new Vertex2d(core.width/2,core.height/2);
		renderer.set_position(screen_pos);//We want our ship to be in the center of the screen.
		
	}
	public void tick() {
		//collision detection goes here.
		velocity.add(acceleration);
		position.add(acceleration);
		//now update rendering
	}
	public void input(boolean forward, boolean space,Vertex2d mouse_pos) {//Using space for fire and W for move forward
		float mdx = mouse_pos.x - screen_pos.x;
		float mdy = mouse_pos.y - screen_pos.y;
		if(mdx != 0 || mdy != 0) {
			angle = -(float)Math.atan2(mdx,mdy);//working in screen coordinates
		}
		renderer.set_rotation1(renderer.position, angle);//set our centre of rotation to be the center of the sprite in direction of mouse.
		if(forward) {
			acceleration.add(new Vertex2d(thrust * (float)Math.cos(angle),thrust *(float)Math.sin(angle)));
		}
		if(space) {
			fire();
		}
	}
	public void fire() {//remember to implement a delay here.
		
	}
	public Vertex2d get_position() {
		return position;
	}
	
}
