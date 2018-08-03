package com.lcass.game;

import com.lcass.core.Core;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.renderer;
import com.lcass.graphics.texture.spritesheet;
import com.lcass.objects.Floor;
import com.lcass.objects.floors.grass;
import com.lcass.objects.objs.wall;

public class Planet {
	private Game game;
	private Core core;
	public Floor[][] map = new Floor[Game.WORLD_SIZE][Game.WORLD_SIZE];
	private renderer renderer;//renders the floors, they don't move very much so we can have them all in one big one
	private boolean updated_renderer = false;
	private Vertex2d position = new Vertex2d();
	private boolean position_updated = false;
	public Planet(Game game) {
		this.game = game;
		this.core = game.core;
		renderer = new renderer(core,Game.WORLD_SIZE*Game.WORLD_SIZE);
		renderer.set_texture(new spritesheet("./com/textures/blocksprites.png"));
		renderer.set_render(true);
		//for now we just make the entire map out of grass.
		for(int x = 0; x< Game.WORLD_SIZE;x ++) {
			for(int y = 0; y < Game.WORLD_SIZE;y++) {
				grass grass = new grass();
				grass.init(game,this);
				grass.set_grid_pos(new Vertex2d(x,y));
				add_floor(grass);
			
			}
			
		}
		//now we can add objects as the floor is no longer null
		
		
	}
	
	//floor stuff
	public void add_floor(Floor floor) {
		if(floor == null) {
			return;
		}
		Vertex2d grid_pos = floor.get_grid_pos();
		int x = (int)grid_pos.x;
		int y = (int)grid_pos.y;
		remove_floor(x,y);
		floor.render_id = renderer.create_object_static(new Vertex2d(x*Game.TILE_SIZE, y*Game.TILE_SIZE),floor.get_sprite());
		map[x][y] = floor;
		updated_renderer = true;
	}
	public void remove_floor(Floor floor) {
		if(floor == null) {
			return;
		}
		Vertex2d grid_pos = floor.get_grid_pos();
		int x = (int)grid_pos.x;
		int y = (int)grid_pos.y;
		remove_floor(x,y);
	}
	public void remove_floor(int x, int y) {
		if(map[x][y] == null) {
			return;
		}
		Floor removed  = map[x][y];
		removed.on_remove();
		renderer.remove_object_static(removed.render_id);
		updated_renderer = true;
	}
	public Floor get_floor(Vertex2d grid_pos) {
		int x = (int)grid_pos.x;
		int y = (int)grid_pos.y;
		return map[x][y];
	}
	//tick stuff
	public void tick() {
		if(updated_renderer) {
			renderer.update_buffer();
			updated_renderer = false;
		}
		
		for(int x = 0; x < map.length;x++) {
			for(int y = 0; y < map.length;y++) {
				if(map[x][y] != null) {
					map[x][y].tick();
					if(position_updated) {
						map[x][y].set_position(position);//updates render position not grid_pos.
					}
				}
			}
		}
		if(position_updated) {
			renderer.set_position(position);
			position_updated = false;
		}
	}
	public void set_position(Vertex2d position) {
		this.position = position;
		position_updated = true;
	}
}
