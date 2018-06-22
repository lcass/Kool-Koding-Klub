package com.lcass.GUI;

import java.lang.reflect.Method;
import java.util.ArrayList;

import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.renderer;

public class widget {
	protected Vertex2d position = new Vertex2d(0, 0);
	private Vertex2d edge_sprite = new Vertex2d(16, 144, 17, 145);
	private ArrayList<Integer> buttons = new ArrayList<Integer>();
	private int edge_id = -1;
	private int overlay_id = -1;
	private int overlay_size = 0;
	protected GUI gui;
	protected String data;

	public widget() {
		
	}
	public void set_gui(GUI gui){
		this.gui = gui;
	}

	public void create() {
		delete();
		renderer gui_render = gui.get_renderer();
		gui_render.remove_object_static(edge_id);
		edge_id = gui_render.create_object(position, edge_sprite, true);
		if (data != null) {
			update_data();
		}
	}
	public void set_bounds(Vertex2d position){
		this.position = position;
	}
	public Vertex2d get_bounds(){
		return position.whole();
	}

	public void set_data(String data) {
		if (this.data == data) {
			return;
		}
		this.data = data;
	}

	public void update_data() {
		clear_overlays();
		if(data == null){
			return;
		}
		for (String widget_part : data.split(";")) {
			String[] widget_info = widget_part.split("\\#");
			Vertex2d sprite = null;
			Vertex2d position = null;
			String[] util_string;
			boolean overlay = true;
			switch (widget_info[0]) {
			case "obj":
				util_string = widget_info[2].split(",");// sprite
				sprite = new Vertex2d(Float.valueOf(util_string[0]),
						Float.valueOf(util_string[1]),
						Float.valueOf(util_string[0]) + 16,
						Float.valueOf(util_string[1]) + 16);
				util_string = widget_info[1].split(",");
				position = new Vertex2d(Float.valueOf(util_string[0]),
						Float.valueOf(util_string[1]), 32, 32);
				break;
			case "image":
				util_string = widget_info[2].split(",");// sprite
				sprite = new Vertex2d(Float.valueOf(util_string[0]),
						Float.valueOf(util_string[1]),
						Float.valueOf(util_string[2]),
						Float.valueOf(util_string[3]));
				util_string = widget_info[1].split(",");
				position = new Vertex2d(Float.valueOf(util_string[0]),
						Float.valueOf(util_string[1]),
						Float.valueOf(util_string[2]),
						Float.valueOf(util_string[3]));
				break;
			case "bar":
				util_string = widget_info[2].split(",");// sprite
				sprite = new Vertex2d(Float.valueOf(util_string[0]),
						Float.valueOf(util_string[1]),
						Float.valueOf(util_string[0]),
						Float.valueOf(util_string[1]));
				util_string = widget_info[1].split(",");
				position = new Vertex2d(Float.valueOf(util_string[0]),
						Float.valueOf(util_string[1]),
						Float.valueOf(util_string[2]),
						Float.valueOf(util_string[3]));
				break;
			}

			add_overlay(position, sprite);

		}

	}
	public void add_button(Vertex2d button,Vertex2d button_sprite, Method button_call,Object instace){
		int id = gui.create_button(button,button_sprite.whole().sub(new Vertex2d(button_sprite.x,button_sprite.y,button_sprite.x,button_sprite.y)), button_call, instace);
		buttons.add(id);
		gui.set_overlay(id, button_sprite);
		
	}
	public void clear_buttons(){
		for(int i : buttons){
			gui.remove_button(i);
		}
	}
	public Vertex2d get_position() {
		return position;
	}

	public void add_overlay(Vertex2d position, Vertex2d sprite) {
		if (position == null || sprite == null) {
			return;
		}
		int id = gui.get_renderer().create_object(
				position.whole().add(this.position.xy()), sprite, true);
		if (overlay_id == -1) {
			overlay_id = id;
		} else {
			overlay_size++;
		}
	}

	public void clear_overlays() {
		for (int i = 0; i <= overlay_size; i++) {
			gui.get_renderer().remove_object_static(i + overlay_id);
		}
		overlay_id = -1;
		overlay_size = 0;
		gui.get_renderer().update_buffer();
	}

	public void delete() {
		clear_overlays();
		gui.get_renderer().remove_object(edge_id);
	}

}
