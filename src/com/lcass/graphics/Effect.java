package com.lcass.graphics;

import java.util.ArrayList;

public class Effect {
	private renderer renderer;
	private ArrayList<Vertex2d> sprites = new ArrayList<Vertex2d>();
	private int render_time = 1;
	private int render_id = -1;
	private boolean render = false;
	private Vertex2d position = new Vertex2d(0,0,0,0);
	public Effect(renderer renderer){
		this.renderer = renderer;
	}
	public void set_render_time(int time){
		render_time = time;
	}
	private int current_period = 0;
	private int current_index = 0;
	public void tick(){
		if(!render){
			return;
		}
		if(current_period >= render_time){
			current_period = 0;
			current_index ++;
			if(current_index == sprites.size()){
				current_index = 0;
			}
			renderer.edit_object(render_id, position, sprites.get(current_index), true);
		}
		current_period ++;
	}
	public void set_render(boolean render){
		if(this.render == render){
			return;
		}
		this.render = render;
		if(render){
			render_id = renderer.create_object(position, sprites.get(0), true);
		}
		else{
			renderer.remove_object(render_id);
			render_id = -1;
		}
	}
	public void set_position(Vertex2d position){
		this.position = position;
	}
	public void set_sprites(Vertex2d[] sprites){
		this.sprites.clear();
		for(Vertex2d sprite : sprites){
			this.sprites.add(sprite);
		}
	}
	

}
