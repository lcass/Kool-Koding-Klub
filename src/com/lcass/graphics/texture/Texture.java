package com.lcass.graphics.texture;

import java.nio.ByteBuffer;
//Many thanks to SHC for the tutorial on this
public class Texture {
	public int id;

	public int width;

	public int height;

	public ByteBuffer buffer;
	public Texture(int id, int width, int height, ByteBuffer buffer) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.buffer = buffer;
	}
	public Texture(){
		
	}

	
}
