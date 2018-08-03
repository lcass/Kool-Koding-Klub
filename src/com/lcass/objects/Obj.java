package com.lcass.objects;

import com.lcass.game.Game;
import com.lcass.graphics.Vertex2d;

public abstract class Obj {
	protected Vertex2d sprite = new Vertex2d(),grid_pos = new Vertex2d(), floor_pos = new Vertex2d();//grid pos is which tile is it on, floor pos is the pixel position relative to the tile.
	protected Floor floor;//the current floor we are on.
	public int render_id = -1;
	protected Game game;
	
	//all properties
	protected boolean opaque = false, dense = false;//can light pass can walk through
	protected Vertex2d hit_box = new Vertex2d(0,0,32,32);//by default it's the whole tile
	public Obj() {
	}
	public void init(Game game) {
		this.game = game;
	}
	public void tick() {
		
	}
	public void move_rel(Vertex2d rel) {
		move(rel.whole().add(grid_pos.whole().mult(Game.TILE_SIZE).add(floor_pos)));
	}
	/**
	 * in pixel coords
	 * @param target
	 */
	public void move(Vertex2d target) {
		//we have to check each step along the path to check that the hit box isn't going to collide with anything
		//we keep moving until we hit the object at which point we take that as our new position.
		Vertex2d delta = target.whole().sub(get_pixel_pos());
		float magnitude = (float)delta.get_magnitude();
		delta.div(magnitude);
		Vertex2d current_position = new Vertex2d();//relative
		for(int dist = 0; dist < magnitude;dist ++) {
			current_position.add(delta);
			if(check_collision(current_position)) {
				//we collided with something
				current_position.sub(delta);//we went too far and collided with something, this would allow edging your way into walls slowly if we didn't go back
				set_pixel_pos(current_position.add(grid_pos.whole().mult(32)));
				return;
			}
		}
		//no collisions so set us at our target position.
		set_pixel_pos(target);
		
	}
	public void set_pixel_pos(Vertex2d position) {//sets our pixel position
		//calculate the grid pos
		if(floor == null) {
			System.err.println("Not on a floor, use add obj directly");
		}
		Vertex2d grid_pos = position.whole().div(32);//will get int casted in the Planet.remove_obj methods.
		int grid_x = (int)grid_pos.x;
		int grid_y = (int)grid_pos.y;
		Floor target = floor.planet.get_floor(grid_pos);
		if(target == floor) {//we are on the same floor but we have updated our pixel position
			floor_pos = position.whole().sub(new Vertex2d(grid_x,grid_y).mult(32));
			floor.edit_object(this);
			return;
		}
		//we have moved to a new tile.
		this.grid_pos = new Vertex2d(grid_x,grid_y);
		this.floor_pos = position.whole().sub(new Vertex2d(grid_x,grid_y).mult(32));
		floor.remove_object(this);
		target.add_obj(this);
	}
	/**
	 * Checks if this objects hit box will collide given the current position.
	 * @param position
	 * @return
	 */
	public boolean check_collision(Vertex2d position) {
		if(hit_box.u == 0 || hit_box.v == 0 || floor == null){
			return false;
		}
		int min_x = Math.floorDiv((int)(hit_box.x + floor_pos.x+position.x), Game.TILE_SIZE);
		int min_y = Math.floorDiv((int)(hit_box.y + floor_pos.y+position.y), Game.TILE_SIZE);
		int max_x = (int)Math.ceil((hit_box.u + floor_pos.x+position.x)/32f);
		int max_y = (int)Math.ceil((hit_box.v + floor_pos.y+position.y)/32f);
		for(int x = min_x; x <= max_x; x++) {
			for(int y = min_y; y <= max_y; y++) {
				for(Obj o : floor.planet.map[x][y].objs) {//why does this work? We make sure that the bottom left corner of each object is on the tile below it that way we only need to check if the obj is within the tile 
					//and not that its hit box is colliding with ours, makes things much simpler.
					if(o.dense) {
						return true;//we collided with something.
					}
				}
			}
		}
		return false;
	}
	//Gets
	public Vertex2d get_floor_pos() {
		return floor_pos;
	}
	public Vertex2d get_grid_pos() {
		return grid_pos;
	}
	public Vertex2d get_sprite() {
		return sprite;
	}
	public Vertex2d get_pixel_pos() {//returns actual pixel pos.
		return grid_pos.whole().mult(game.TILE_SIZE).add(floor_pos);
	}
	public Floor get_floor() {
		return floor;
	}
	//sets
	public void set_floor(Floor floor) {
		this.floor = floor;
	}
}
