package com.lcass.objects.floors;

import com.lcass.game.Game;
import com.lcass.game.Planet;
import com.lcass.graphics.Vertex2d;
import com.lcass.objects.Floor;

public class metal extends Floor{
	public metal() {
		super();
	}
	public void init(Game game,Planet planet) {
		super.init(game,planet);
		sprite = new Vertex2d(32,0,48,16);
	}
}
