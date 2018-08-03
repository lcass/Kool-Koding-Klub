package com.lcass.objects.floors;

import com.lcass.game.Game;
import com.lcass.game.Planet;
import com.lcass.objects.Floor;

public class space extends Floor{
	public space() {
		super();
		//uses an empty sprite, this is just a replacement for null incase things go wrong.
	}
	public void init(Game game,Planet planet) {
		super.init(game,planet);
	}
}
