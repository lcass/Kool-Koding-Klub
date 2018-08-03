package com.lcass.objects.floors;

import java.util.Random;

import com.lcass.game.Game;
import com.lcass.game.Planet;
import com.lcass.graphics.Vertex2d;
import com.lcass.objects.Floor;

public class grass extends Floor{
	public grass() {
		super();
	}
	public void init(Game game,Planet planet) {
		super.init(game,planet);
		Random r = new Random();
		int x = Math.abs(r.nextInt() % 4);
		int y = Math.abs(r.nextInt() % 4);
		sprite = new Vertex2d(x * 16,y * 16+16, x * 16  +16, y * 16 + 32);
		
	}

}
