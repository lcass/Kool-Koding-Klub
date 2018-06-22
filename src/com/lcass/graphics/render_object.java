package com.lcass.graphics;

public class render_object {
	public Vertex2d overlay = new Vertex2d(0,0,0,0);
	public Vertex2d position = new Vertex2d(0,0,0,0);
	public Vertex2d rotation = new Vertex2d(0,0,0,0);
	public int size = 32;
	public boolean custom = false;
	public render_object(Vertex2d position , Vertex2d sprite){
		this.position = position;
		size = (int)Math.abs(position.x - position.u);
		overlay = sprite;
	}
	public render_object(Vertex2d position , Vertex2d sprite,boolean custom){
		this.position = position;
		this.custom = custom;
		size = (int)Math.abs(position.x - position.u);
		overlay = sprite;
	}
}
