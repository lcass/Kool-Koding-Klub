package com.lcass.graphics;

public class Vertex2d {//2d for texture and for vert coords
	public float x,y,u,v,val;//val is used for some lighting stuff
	public Vertex2d(float x, float y){
		this.x = x;
		this.y = y;
		u = 0;
		v = 0;
	}
	public Vertex2d(float x,float y , float u , float v){
		this.x = x;
		this.y = y;
		this.u = u;
		this.v = v;
	}
	public Vertex2d() {
		x = y = u = v = 0;
	}
	
	
	
	public Vertex2d add(Vertex2d ad){
		this.x += ad.x;
		this.y += ad.y;
		this.u += ad.u;
		this.v += ad.v;
		return this;
	}
	public Vertex2d sub(Vertex2d ad){
		this.x -= ad.x;
		this.y -= ad.y;
		this.u -= ad.u;
		this.v -= ad.v;
		return this;
	}
	
	public Vertex2d mult(float a){
		this.x *= a;
		this.y *= a;
		this.u *= a;
		this.v *= a;
		return this;
	}
	public void div(Vertex2d ad){
		
	}
	public Vertex2d div(float a){
		this.x /= a;
		this.y /= a;
		this.u /= a;
		this.v /= a;
		return this;
	}
	public Vertex2d floor(){
		return new Vertex2d((float)Math.floor(x),(float)Math.floor(y),(float)Math.floor(u),(float)Math.floor(v));
	}
	public Vertex2d xy(){
		return new Vertex2d(x,y);
	}
	public Vertex2d whole(){
		return new Vertex2d(x,y,u,v);
	}
	public boolean int_equal(Vertex2d input){
		if((int)x == (int)input.x && (int)y == (int)input.y){
			return true;
		}
		return false;
	}
	public Vertex2d uv(){
		return new Vertex2d(u,v,0,0);
	}
	public Vertex2d rotate(int rot){
		Vertex2d output = new Vertex2d(0,0,0,0);
		switch(rot){
		case 1: output=new Vertex2d(v,x,y,u); break;
		case 2: output=new Vertex2d(u,v,x,y); break;
		case 3: output=new Vertex2d(y,u,v,x); break;
		default: return this;
		}
		x = output.x;
		y = output.y;
		u = output.u;
		v = output.v;
		return this;
	}
	public double get_magnitude(){
		return Math.sqrt(x*x + y * y);
	}
	
}
