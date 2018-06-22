package com.lcass.GUI;

import java.lang.reflect.Method;

import com.lcass.graphics.Vertex2d;
import com.lcass.util.Encapsulated_method;

public class button{
	protected Vertex2d bounding = new Vertex2d(0,0,16,16);
	protected Vertex2d position = new Vertex2d(0,0);
	public int overlay_id = -1;
	protected Encapsulated_method to_call;
	public int buffer_id = 0;
	public int render_id = -1;
	protected GUI gui;
	public boolean slot = false;
	public button(GUI gui){
		this.gui = gui;
	}
	public void set_bounding(Vertex2d position){
		this.bounding = position;
	}
	public void set_position(Vertex2d position){
		this.position = position;
	}
	public void set_method(Object instance,Method m){
		to_call = new Encapsulated_method(m,new Integer[]{buffer_id},instance);
	}
	public void click(){
		to_call.call(new Integer[]{buffer_id});
	}
	public Vertex2d get_position(){
		return position;
	}
	public Vertex2d get_bounding(){
		return bounding;
	}
	public boolean in_bounds(Vertex2d pos){
		Vertex2d gui_position = gui.get_position();
		Vertex2d position = pos.whole().sub(gui_position);
		if(position.x <= bounding.u + this.position.x && position.x>= this.position.x){
			if(position.y <= bounding.v + this.position.y && position.y>= this.position.y){
				return true;
			}
		}
		return false;
	}
}
