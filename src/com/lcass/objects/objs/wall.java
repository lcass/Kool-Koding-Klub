package com.lcass.objects.objs;

import com.lcass.game.Game;
import com.lcass.graphics.Vertex2d;
import com.lcass.objects.Obj;

public class wall extends Obj{
	public wall() {
		super();
	}
	public void init(Game game){
		super.init(game);
		opaque = true;
		dense = true;
		hit_box = new Vertex2d(0,0,32,32);//we take up a whole tile.
		sprite = new Vertex2d( 5 * 16 , 0, 6 * 16, 16);
	}
}
