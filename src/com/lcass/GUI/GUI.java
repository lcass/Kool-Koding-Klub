package com.lcass.GUI;

import java.lang.reflect.Method;
import java.util.ArrayList;

import com.lcass.core.Core;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.renderer;

public class GUI {
	private renderer gui_renderer;
	public Core core;
	private Vertex2d position= new Vertex2d(0,0,0,0);
	private ArrayList<button> buttons = new ArrayList<button>();
	private int current_id = 0;
	private boolean render = true;
	public GUI(Core core){
		this.core = core;
		gui_renderer = new renderer(core);
	}
	public boolean check_click(Vertex2d location){
		if(!render){
			return false;
		}
		for(button b: buttons){
			if(b.in_bounds(location)){
				b.click();
				return true;
			}
		}
		return false;
	}
	public void top_stack(){
		gui_renderer.push_top();
	}
	public int create_button(Vertex2d position,Vertex2d bounding,Method m,Object method_instance){
		button new_button = new button(this);
		new_button.set_position(position);
		new_button.set_bounding(bounding);
		new_button.set_method(method_instance,m);
		new_button.buffer_id = current_id;
		current_id++;
		buttons.add(new_button);
		return current_id-1;
	}
	public button get_button(int button_id){
		for(button b : buttons){
			if(b.buffer_id == button_id){
				return b;
			}
		}
		return null;
	}
	public void set_render(boolean render){
		gui_renderer.set_render(render);
		this.render = render;
		gui_renderer.push_top();
	}
	public void set_position(Vertex2d position){
		this.position = position;
		gui_renderer.set_position(position);
	}
	public int add_overlay(Vertex2d position, Vertex2d sprite){
		return gui_renderer.create_object(position,sprite,true);
	}
	public void remove_artifical_overlay(int id){
		gui_renderer.remove_object(id);
	}
	public Vertex2d get_position(){
		return position;
	}
	public void edit_artificial_overlay(int id,Vertex2d position,Vertex2d sprite){
		gui_renderer.edit_object(id, position, sprite, true);
	}
	public void set_overlay(int button_id, Vertex2d sprite){
		button b = get_button(button_id);
		b.overlay_id = gui_renderer.create_object(b.get_position(), sprite);
	}
	public void remove_overlay(int button_id){
		button b = get_button(button_id);
		gui_renderer.remove_object(b.overlay_id);
	}
	public renderer get_renderer(){
		return gui_renderer;
	}
	public void remove_button(int button_id){
		button remove = null;
		for(button b : buttons){
			if(b.buffer_id == button_id){
				remove = b;
				break;
			}
		}
		buttons.remove(remove);
		
	}
	public void delete(){
		gui_renderer.clear();
		gui_renderer.delete_buffer();
	}
}
