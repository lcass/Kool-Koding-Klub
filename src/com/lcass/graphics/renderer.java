package com.lcass.graphics;

import java.util.ArrayList;

import com.lcass.core.Core;
import com.lcass.graphics.texture.spritesheet;
import com.lcass.util.Util;
/*
 * As with the VBO info in Graphics_handler , renderer's should never be deleted instead their core content should be modified and deletion will occur automatically on program exit
 */
public class renderer {//render objects are handled entirely inside the renderer as to make the overhead as small as possible during use
	private render_object[] render_objects;
	private ArrayList<Integer> free_objects = new ArrayList<Integer>();//when adding a new object free_objects.get(0) is checked to see if there are currently free positions
	public Vertex2d position;
	private spritesheet overlay_sprite_data = new spritesheet(
			"com/textures/items.png");
	public Core core;
	private int buffer_id;
	public Graphics_handler graphics;
	private int objects_used = 0;
	private Util util;
	private static final int OVERLAY_MAX = 10000;
	private int overlay_number = OVERLAY_MAX;
	/**
	 * the GUI init version , requires init() to be called afterwards in order to create the other objects
	 * @param core
	 * @param sprites
	 */
	public renderer(){
		position = new Vertex2d(0,0,0,0);
		util = new Util();
	}
	/**
	 * preload a sprite sheet
	 * @param core
	 * @param sprites a spritesheet, preloads texture
	 */
	public renderer(Core core , spritesheet sprites){
		this.core = core;
		position = new Vertex2d(0,0,0,0);
		this.graphics = core.graphics;
		render_objects = new render_object[OVERLAY_MAX];
		buffer_id = graphics.create_buffer(OVERLAY_MAX, sprites.gettexture());
		this.overlay_sprite_data = sprites;
		util = new Util();
	}
	/**
	 * creates a renderer set with the default sprite sheet
	 * @param core
	 */
	public renderer(Core core){
		this.core = core;
		position = new Vertex2d(0,0,0,0);
		this.graphics = core.graphics;
		render_objects = new render_object[OVERLAY_MAX];
		buffer_id = graphics.create_buffer(OVERLAY_MAX, overlay_sprite_data.gettexture());
		util = new Util();
		//add to core.tick hence no tick call is required in the method.
	}
	/**
	 * Load with a custom amount of render objects instead of the standard maximum.
	 * @param core world core.
	 * @param max_render_objects max number of render objects.
	 */
	public renderer(Core core, int max_render_objects) {
		this.core = core;
		position = new Vertex2d();
		this.graphics = core.graphics;
		render_objects = new render_object[max_render_objects];
		buffer_id = graphics.create_buffer(max_render_objects,overlay_sprite_data.gettexture());
		util = new Util();
		overlay_number = max_render_objects;
	}
	public void set_render(boolean render){
		graphics.set_render(buffer_id, render);
	}
	/**
	 * sets up the renderer , do not call if renderer(core) was used.
	 */
	public void init(Core core , spritesheet sprite){
		this.graphics = core.graphics;
		this.overlay_sprite_data = sprite;
		this.core = core;
		render_objects = new render_object[OVERLAY_MAX];
		buffer_id = graphics.create_buffer(OVERLAY_MAX, overlay_sprite_data.gettexture());
		util = new Util();
	}
	public Vertex2d[] render_to_array(render_object render){
		if(render.custom){
			return rect_to_array(render);
		}
		return core.util.square((int)render.position.x, (int)render.position.y, 32, overlay_sprite_data.getcoords((int)render.overlay.x,(int)render.overlay.y,(int)render.overlay.u,(int)render.overlay.v));
	}
	public Vertex2d[] rect_to_array(render_object render){
		return core.util.rectangle((int)render.position.x, (int)render.position.y, (int)render.position.u,(int)render.position.v, overlay_sprite_data.getcoords((int)render.overlay.x,(int)render.overlay.y,(int)render.overlay.u,(int)render.overlay.v));
	}
	public Vertex2d[] render_to_empty(render_object render){
		return core.util.square(0, 0, 0 , overlay_sprite_data.getcoords(0,0,0,0));
	}
	/**
	 * Create a new render object , set custom to true if this is a custom sprite
	 * @param position the position of it
	 * @param sprite the sprite of the data
	 * @return the index of the newly created object
	 */
	public int create_object(Vertex2d position , Vertex2d sprite,boolean custom){//get(0) was causing issues , couldn't decide if it was a position or value so it would reassign new data to those indecies.
		if(free_objects.size() >0){
			int index = free_objects.get(0);
			free_objects.remove(0);
			edit_object(index,position,sprite,custom);
			return index;
		}
		int index = objects_used;
		
		render_objects[index] = new render_object(position.whole(),sprite.whole(),custom);
		graphics.extend_buffer(buffer_id, render_to_array(render_objects[index]));
		objects_used++;
		return index;
		
	}
	/**
	 * Create a new render object , set custom to true if this is a custom sprite
	 * @param position the position of it
	 * @param sprite the sprite of the data
	 * @return the index of the newly created object
	 */
	public int create_object_static(Vertex2d position , Vertex2d sprite,boolean custom){//get(0) was causing issues , couldn't decide if it was a position or value so it would reassign new data to those indecies.
		if(free_objects.size() >0){
			int index = free_objects.get(0);
			free_objects.remove(0);
			edit_object(index,position,sprite,custom);
			
			return index;
			
		}
		int index = objects_used;
		render_objects[index] = new render_object(position.whole(),sprite.whole(),custom);
		graphics.extend_buffer_static(buffer_id, render_to_array(render_objects[index]));
		objects_used++;
		return index;
		
	}
	/**
	 * Create a new object with a square sprite of default 32 x 32 size.
	 * @param position
	 * @param sprite
	 * @return
	 */
	public int create_object(Vertex2d position , Vertex2d sprite){
		return create_object(position,sprite,false);
	}
	/**
	 * Same as create_object() but doesn't update the screen buffer.
	 * @param position
	 * @param sprite
	 * @return
	 */
	public int create_object_static(Vertex2d position, Vertex2d sprite) {
		return create_object_static(position,sprite,false);
	}
	/**
	 * update the stored render list and updates the screen data , should not be called with rebuild_buffer();
	 * @param index the index id of the data
	 * @param position the pixel coordinate of the data
	 * @param sprite the sprite coordinates of the data
	 */
	public void edit_object(int index,Vertex2d position, Vertex2d sprite, boolean custom){
		if(index == -1){
			return;
		}
		render_objects[index] = new render_object(position,sprite,custom);
		graphics.edit_buffer_index(buffer_id,index * 12, render_to_array(render_objects[index]));
	}
	/**
	 * Rotate an objects coordinates directly and update the screen data.
	 * @param index
	 * @param rotation_centre
	 * @param rotation
	 */
	public void rotate(int index,Vertex2d rotation_centre, float rotation){
		if(index == -1){
			return;
		}
		render_object r = render_objects[index];
		float delta_rot = rotation - r.rotation.u;
		r.rotation.u += delta_rot;
		graphics.edit_rotation_index(buffer_id, index*18, new Vertex2d[]{new Vertex2d(rotation_centre.x,rotation_centre.y,rotation,0),new Vertex2d(rotation_centre.x,rotation_centre.y,rotation,0),new Vertex2d(rotation_centre.x,rotation_centre.y,rotation,0),new Vertex2d(rotation_centre.x,rotation_centre.y,rotation,0),new Vertex2d(rotation_centre.x,rotation_centre.y,rotation,0),new Vertex2d(rotation_centre.x,rotation_centre.y,rotation,0)});
	}
	/**
	 * Rotate an objects coordinates directly, use for batch calls only.
	 * @param index
	 * @param rotation_centre
	 * @param rotation
	 */
	public void rotate_static(int index,Vertex2d rotation_centre, float rotation){
		if(index == -1){
			return;
		}
		render_object r = render_objects[index];
		float delta_rot = rotation - r.rotation.u;
		r.rotation.u += delta_rot;
		graphics.edit_rotation_index_static(buffer_id, index*18, new Vertex2d[]{new Vertex2d(rotation_centre.x,rotation_centre.y,rotation,0),new Vertex2d(rotation_centre.x,rotation_centre.y,rotation,0),new Vertex2d(rotation_centre.x,rotation_centre.y,rotation,0),new Vertex2d(rotation_centre.x,rotation_centre.y,rotation,0),new Vertex2d(rotation_centre.x,rotation_centre.y,rotation,0),new Vertex2d(rotation_centre.x,rotation_centre.y,rotation,0)});
	}
	/**
	 * Edit the objects sprite and position directly.
	 * @param index
	 * @param position
	 * @param sprite
	 */
	public void edit_object(int index, Vertex2d position,Vertex2d sprite){
		if(index == -1){
			return;
		}
		edit_object(index,position,sprite,false);
	}
	/**
	 * Translate the specified object's vertex data directly.
	 * @param index
	 * @param position
	 */
	public void translate_object(int index , Vertex2d position){
		if(index == -1){
			return;
		}
		render_objects[index].position = position;
		graphics.edit_buffer_index(buffer_id,index*12,render_to_array(render_objects[index]));
	}
	/**
	 * translate an object without updating the screen.
	 * @param index
	 * @param position
	 */
	public void translate_object_static(int index, Vertex2d position) {
		if(index == -1) {
			return;
		}
		render_objects[index].position = position;
		graphics.edit_buffer_index_static(buffer_id, index*12, render_to_array(render_objects[index]));
	}
	/**
	 * updates the stored render list but does not update the GPU memory or current screen data , once all objects have been placed call rebuild_buffer()
	 * @param index the id of the data
	 * @param position the pixel coordinate of the data
	 * @param sprite the pixel coordinates of the sprite data
	 */
	public void edit_object_static(int index , Vertex2d position, Vertex2d sprite,boolean custom){
		if(index == -1){
			return;
		}
		render_objects[index] = new render_object(position,sprite,custom);
		graphics.edit_buffer_index_static(buffer_id,index * 12, render_to_array(render_objects[index]));
	}
	public void edit_object_static(int index , Vertex2d position, Vertex2d sprite){
		edit_object_static(index,position,sprite,false);
	}
	/**
	 * remove an object from the stored buffer and screen data 
	 * @param index
	 */
	public void remove_object(int index){
		if(index == -1){
			return;
		}
		graphics.edit_buffer_index(buffer_id,index * 12, render_to_empty(render_objects[index]));
	    free_objects.add(index);
	    render_objects[index] = new render_object(new Vertex2d(0,0,0,0),new Vertex2d(0,0,0,0));
	}
	/**
	 * Removes an object without updating screen data, Use for batch calls
	 * @param index
	 */
	public void remove_object_static(int index){
		if(index == -1){
			return;
		}
		graphics.edit_buffer_index_static(buffer_id,index * 12, render_to_empty(render_objects[index]));
	    free_objects.add(index);
	    render_objects[index] = new render_object(new Vertex2d(0,0,0,0),new Vertex2d(0,0,0,0));
	}
	/**
	 * Rebuild the current buffer , extremely innefficient to use use the other methods whenever possible unless batch objects are being modified.
	 */
	public void rebuild_buffer(){
		graphics.clear_buffer(buffer_id);
		for(int i = 0; i < overlay_number;i++){
			if(render_objects[i] == null){
				continue;
			}
			graphics.extend_buffer_static(buffer_id, render_to_array(render_objects[i]));
		}
		graphics.update_buffer(buffer_id);
	}
	public void update_buffer(){
		graphics.update_buffer(buffer_id);
	}
	
	/**
	 * returns the render object at this index
	 * @param index the index of the render object
	 * @return the render object at this index
	 */
	public render_object get_object(int index){
		return render_objects[index];
	}
	/**
	 * Set the texture of this renderer
	 * @param sprites the new spritesheet 
	 */
	public void set_texture(spritesheet sprites){//change the current texture of the renderer
		overlay_sprite_data = sprites;
		graphics.set_texture(buffer_id, sprites.gettexture());
	}
	/**
	 * Set the screen position of the buffer
	 * @param position the pixel coordinates to place the buffer
	 */
	
	public void push_top(){
		core.graphics.push_top(buffer_id);
	}
	public void set_position(Vertex2d position){
		graphics.set_position(buffer_id, position);
		
	}
	public void batch_remove(ArrayList<Integer> ids){
		for(int i : ids){
			remove_object_static(i);
		}
		update_buffer();
	}
	public void batch_remove(int[] ids){
		for(int i : ids){
			remove_object_static(i);
		}
		update_buffer();
	}
	/**
	 * clear all render data stored within this renderer and clear all stored render objects , all index's related to this renderer must also be cleared when this is called
	 */
	public void clear(){
		for(int i = 0; i < objects_used;i++){
			remove_object(i);
		}
		rebuild_buffer();
	}
	public void set_rotation1(Vertex2d coordinate, float rotation){
		graphics.set_rotation1(coordinate, rotation, buffer_id);
	}
	public void set_rotation2(Vertex2d coordinate,float rotation){
		graphics.set_rotation2(coordinate,rotation,buffer_id);
	}
	public void delete_buffer(){
		core.graphics.delete_buffer(buffer_id);
	}
	
}
