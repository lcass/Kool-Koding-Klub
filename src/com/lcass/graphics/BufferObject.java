package com.lcass.graphics;

import java.nio.FloatBuffer;

import com.lcass.graphics.texture.Texture;
import static org.lwjgl.system.MemoryUtil.*;

public class BufferObject {
	public FloatBuffer verticies;
	public FloatBuffer textures;
	public FloatBuffer rotations;
	public Texture texture;
	public Vertex2d translation = new Vertex2d(0,0),rotation1=new Vertex2d(0,0),rotation2=new Vertex2d(0,0);
	public int id;
	public boolean render = true;
	public int vert_count;
	public int vertex_id;
	public int texture_id;
	public int rotation_id;
	public BufferObject(FloatBuffer verticies , FloatBuffer textures ,FloatBuffer rotations, int vert_count , int vertex_id , int texture_id,int rotation_id,Texture texture,Vertex2d translation){
		this.verticies = verticies;
		this.textures = textures;
		this.rotation_id = rotation_id;
		this.rotations = rotations;
		this.texture = texture;
		this.translation = translation;
		this.vert_count = vert_count;
		this.vertex_id = vertex_id;
		this.texture_id = texture_id;
	}
	public void set_rotation1(Vertex2d coordinate,float rotation){
		rotation1 = new Vertex2d(coordinate.x,coordinate.y,rotation,0);
	}
	public void set_rotation2(Vertex2d coordinate, float rotation){
		rotation2 = new Vertex2d(coordinate.x,coordinate.y,rotation,0);
	}
	public void set_buffers(FloatBuffer verticies, FloatBuffer textures,FloatBuffer rotations){
		this.verticies = verticies;
		this.textures = textures;
		this.rotations = rotations;
	}
	public void set_translation(Vertex2d translation){
		this.translation = translation;
	}
	public void set_texture(Texture texture){
		this.texture = texture;
	}
	public void flip(){
		verticies.flip();
		textures.flip();
		rotations.flip();
	}
	public void rewind(){
		verticies.rewind();
		textures.rewind();
		rotations.rewind();
	}
	public void set_to_write(){
		verticies.position(vert_count);
		textures.position(vert_count);
		rotations.position(vert_count);
	}
	public int limit(){
		return verticies.position();//the maximum data position inside the buffer
	}
	public void clear(){
		verticies.clear();
		textures.clear();
		rotations.clear();
		vert_count = 0;
	}
	public void dispose(){
		memFree(verticies);
		memFree(rotations);
		memFree(textures);
	}
	
}
