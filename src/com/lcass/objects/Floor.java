package com.lcass.objects;

import java.util.ArrayList;

import com.lcass.game.Game;
import com.lcass.game.Planet;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.renderer;
import com.lcass.graphics.texture.spritesheet;

public abstract class Floor {//these 
	protected ArrayList<Obj> objs = new ArrayList<Obj>();
	protected renderer renderer;
	public int render_id = -1;
	protected Vertex2d sprite = new Vertex2d(), grid_pos = new Vertex2d();
	protected boolean renderer_updated = false;
	protected Game game;
	protected Planet planet;//the current planet that we are on.
	
	public Floor() {
		
	}
	
	public void init(Game game,Planet planet) {
		this.game = game;
		this.planet = planet;
		renderer = new renderer(game.core,20);//we shouldn't expect more than maybe 20 objects on a tile, we dynamically reallocate this if that is the case.
		renderer.set_render(true);
		renderer.set_texture(new spritesheet("./com/textures/items.png"));
	}
	
	public void add_obj(Obj obj) {
		if(objs.contains(obj) || obj == null) {
			return;
		}
		if(objs.size() == 20) {
			expand_renderer();
		}
		obj.set_floor(this);
		objs.add(obj);
		obj.render_id = renderer.create_object_static(obj.get_pixel_pos(), obj.get_sprite());
		renderer_updated = true;
	}
	
	public void remove_object(Obj obj) {
		if(obj == null) {
			return;
		}
		objs.remove(obj);
		//we don't set the floor as that is handled internally by obj.
		renderer.remove_object_static(obj.render_id);
		renderer_updated = true;
	}
	
	public void edit_object(Obj obj) {//update the obj's render stuff before sending it to this.
		renderer.edit_object_static(obj.render_id, obj.get_pixel_pos(), obj.get_sprite());
		renderer_updated = true;
	}
	
	
	
	public void tick() {
		if(renderer_updated) {
			renderer.update_buffer();
			renderer_updated = false;
		}
		for(Obj obj: objs) {
			obj.tick();
		}
	}
	//ingame
	public void on_remove() {
		
	}
	//Gets
	public Vertex2d get_grid_pos() {
		return grid_pos;
	}
	public Vertex2d get_sprite(){
		return sprite;
		
	}
	//Sets
	public void set_position(Vertex2d position) {
		renderer.set_position(position);
	}
	public void set_grid_pos(Vertex2d grid_pos) {
		this.grid_pos = grid_pos.whole();
	}
	public void expand_renderer() {//should add some safe guards as this could cause really bad memory issues if exploited.
		renderer new_renderer = new renderer(game.core);//no limit buffer created, if this gets overfilled then somebody has fucked something up
		for(Obj o : objs) {
			o.render_id = new_renderer.create_object_static(o.get_pixel_pos(), o.get_sprite());//move everything over to the new buffer
		}
		new_renderer.update_buffer();
		new_renderer.set_render(true);
		new_renderer.set_position(renderer.position);
		renderer.delete_buffer();
		renderer = new_renderer;
	}
	public ArrayList<Obj> get_objs(){
		return objs;
	}
}
