package com.lcass.graphics;

import org.lwjgl.opengl.GL20;

public class CoreVBO {
	public float fps;
	public float particle_sim_rate = 300;
	public static int locationtime,locationbloom,locationtransform, locationzoom, locationrotatepos,locationdimension,locationrotate;
	public static int shader_id,vert_id,tex_id,particle_shader,particle_vert,particle_tex,particle_geom;
	public static int locationattribtex, locationattribvertex,locationattribrot;
	public static int locationcolor;
	public static int locationrotatepos_2;
	public static int particle_transform,particle_dimension,particle_time,particleattribvert,particleattribtex,particle_spawn,particle_decay,particle_alpha,particle_size,particle_velocity;
	private Shaderclass shaderclass,line_shader_class,particle_shader_class;
	public CoreVBO(){
		shaderclass = new Shaderclass();
		shaderclass.attachFragmentShader("./src/com/shaders/default_frag.frag");
		shaderclass.attachVertexShader("./src/com/shaders/default_vert.vert");
		shaderclass.link();
		
		
		shader_id = shaderclass.programID;
		vert_id = shaderclass.vertexShaderID;
		tex_id = shaderclass.fragmentShaderID;
		locationrotatepos = GL20.glGetUniformLocation(shader_id, "rotpos");
		locationrotatepos_2 = GL20.glGetUniformLocation(shader_id, "rotpos_2");
		locationtime = GL20.glGetUniformLocation(shader_id, "timein");
		locationbloom = GL20.glGetUniformLocation(shader_id, "bloomval");
		locationtransform = GL20.glGetUniformLocation(shader_id, "transform");
		locationzoom = GL20.glGetUniformLocation(shader_id,"zoom");
		locationcolor = GL20.glGetUniformLocation(shader_id,"color");
		
		locationdimension = GL20.glGetUniformLocation(shader_id,"dimension");
		locationrotate = GL20.glGetUniformLocation(shader_id,"rotation");
		locationattribtex = GL20.glGetAttribLocation(shader_id,"texin");
		locationattribvertex = GL20.glGetAttribLocation(shader_id, "position");
		locationattribrot = GL20.glGetAttribLocation(shader_id, "rot_attrib");

		
	}
	public void bind_shader(){
		shaderclass.bind();
	}
	public void un_bind_shader(){
		shaderclass.unbind();
	}
	public void bind_particle(){
		particle_shader_class.bind();
	}
	public void un_bind_particle(){
		particle_shader_class.unbind();
	}
	public void dispose(){
		particle_shader_class.dispose();
		line_shader_class.dispose();
		shaderclass.dispose();
	}
	public void set_fps(int fps){
		this.fps = fps;
	}
	public float fps_mod(){
		return fps/particle_sim_rate;
		
	}
}
