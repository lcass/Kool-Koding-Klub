package com.lcass.graphics;

import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import com.lcass.core.Core;
import com.lcass.graphics.texture.Texture;
/*
 * This class basically stores all the VBO's and vbo data in permagen floatbuffers that are cleared on exit , this helps with efficiency and they are all handled manually.
 * For floatbuffers to be created simply use stacks in the classes requiring them however the overlayhandler will be renamed and repurposed to handle all of the rendering without much overhead
 */
public class Graphics_handler {
	private Core core;
	private CoreVBO vbo_core;
	public int width = 800;
	public int height = 600;
	private ArrayList<BufferObject> buffer_objects = new ArrayList<BufferObject>();
	private int current_index = 0;
	public Graphics_handler(Core core){
		this.core = core;
		
	}
	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		int index = 0;
		for(index = 0; index < buffer_objects.size();index++){
			
			BufferObject render_object = buffer_objects.get(index);
			if(!render_object.render){
				continue;
			}
			vbo_core.bind_shader();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, render_object.texture.id);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, render_object.vertex_id);
			GL20.glEnableVertexAttribArray(vbo_core.locationattribvertex);
			GL20.glEnableVertexAttribArray(vbo_core.locationattribtex);
			GL20.glEnableVertexAttribArray(vbo_core.locationattribrot);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,render_object.vertex_id);
			GL20.glVertexAttribPointer(vbo_core.locationattribvertex, 2,
					GL11.GL_FLOAT, false, 0, 0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, render_object.texture_id);
			GL20.glVertexAttribPointer(vbo_core.locationattribtex, 2,
					GL11.GL_FLOAT, false, 0, 0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, render_object.rotation_id);
			GL20.glVertexAttribPointer(vbo_core.locationattribrot, 3, GL11.GL_FLOAT, false, 0, 0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
			// Draw the textured rectangle
			GL20.glUniform2f(vbo_core.locationdimension, width, height);
			GL20.glUniform2f(vbo_core.locationtransform, render_object.translation.x,
					render_object.translation.y);
			GL20.glUniform2f(vbo_core.locationrotatepos, render_object.rotation1.x, render_object.rotation1.y);
			GL20.glUniform1f(vbo_core.locationrotate,render_object.rotation1.u);
			GL20.glUniform3f(vbo_core.locationrotatepos_2, render_object.rotation2.x, render_object.rotation2.y, render_object.rotation2.u);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, render_object.vert_count);
			
			GL20.glDisableVertexAttribArray(vbo_core.locationattribvertex);
			GL20.glDisableVertexAttribArray(vbo_core.locationattribtex);
			GL20.glDisableVertexAttribArray(vbo_core.locationattribrot);
			GL11.glEnable(GL11.GL_BLEND);
			vbo_core.un_bind_shader();
		}
		GLFW.glfwSwapBuffers(core.window);
		GLFW.glfwPollEvents();
		
	}
	public BufferObject get_buffer(int id){
		for(BufferObject buffer : buffer_objects){
			if(buffer.id==id){
				return buffer;
			}
		}
		return null;
	}
	public int create_buffer(int size,Texture t){//texture must be set even if it's null
		
		BufferObject buffer = new BufferObject(memAllocFloat(size * 12),memAllocFloat(size*12),memAllocFloat(size*18),0,GL15.glGenBuffers(),GL15.glGenBuffers(),GL15.glGenBuffers(),t,new Vertex2d(0,0,0,0));
		buffer_objects.add(buffer);//designed for dealing with squares as they make up the majority of the game , if other objects are used i'll put in custom methods for them.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.vertex_id);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.verticies, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.texture_id);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER,	buffer.textures, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.rotation_id);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.rotations, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		buffer.render=false;
		current_index++;
		buffer.id = current_index-1;
		return current_index-1;//the index is the current position of the item in the arraylist , NEVER DELETE VBO'S ONLY MODIFY
	}
	public FloatBuffer[] get_floatbuffer(int id){
		BufferObject buffer_obj = get_buffer(id);
		return new FloatBuffer[]{buffer_obj.verticies,buffer_obj.textures};
	}
	public void set_render(int index , boolean render){
		get_buffer(index).render=render;
	}
	
	/**
	 * 
	 * @param size the maximum size of the vbo , probably larger than the inital data
	 * @param data the data being parsed , data has the form of x & y as position of 1 vertex and u & v being the texture coordinates of that same vertex
	 * @return returns the id of the buffer
	 */
	public int create_buffer(int size , Vertex2d[] data,Texture t){
		int index = create_buffer(size,t);
		BufferObject buffer = get_buffer(index);
		for(Vertex2d vertex :data){
			buffer.verticies.put(vertex.x);
			buffer.textures.put(vertex.u);
			buffer.verticies.put(vertex.y);
			buffer.textures.put(vertex.v);
			buffer.rotations.put(0);
			buffer.rotations.put(0);
			buffer.rotations.put(0);
			buffer.vert_count+=2;
		}
		buffer_objects.set(index,buffer);
		buffer.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.vertex_id);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER,0, buffer.verticies);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.texture_id);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER,0,	buffer.textures);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.rotation_id);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer.rotations);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return index;
	}
	/**
	 * Extends the indexed buffer and then modifies the underlying data , use extend_buffer_static to extend the buffer without modifying the graphics data and instead do it in bulk afterwards
	 * @param index the index of the data
	 * @param data the vertex2d[] array to be appened , should be a complete square
	 */
	public void extend_buffer(int index , Vertex2d[] data){
		BufferObject buffer = get_buffer(index);
		buffer.set_to_write();
		for(Vertex2d vertex :data){
			buffer.verticies.put(vertex.x);
			buffer.textures.put(vertex.u);
			buffer.verticies.put(vertex.y);
			buffer.textures.put(vertex.v);
			buffer.vert_count += 2;
			buffer.rotations.put(0);
			buffer.rotations.put(0);
			buffer.rotations.put(0);
		}
		buffer_objects.set(index,buffer);
		buffer.rewind();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.vertex_id);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER,0, buffer.verticies);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.texture_id);
	    GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER,	0, buffer.textures);
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.rotation_id);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer.rotations);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);		
	}
	/**
	 * Extends the indexed buffer without modifying the gpu data
	 * @param index the index of the data
	 * @param data the vertex2d[] array to be appened , should be a complete square
	 */
	public void extend_buffer_static(int index,Vertex2d[] data){
		BufferObject buffer = get_buffer(index);
		buffer.set_to_write();
		for(Vertex2d vertex :data){
			buffer.verticies.put(vertex.x);
			buffer.textures.put(vertex.u);
			buffer.verticies.put(vertex.y);
			buffer.textures.put(vertex.v);
			buffer.vert_count+=2;
			buffer.rotations.put(0);
			buffer.rotations.put(0);
			buffer.rotations.put(0);
		}
		buffer_objects.set(index,buffer);
		
	}
	/**
	 * Modify the specific index of the data in the buffer and then update the stored memory
	 * @param index the index of the buffer
	 * @param buffer_index the starting index of the data inside the buffer
	 * @param data the data to overwrite the currently stored data , if this is larger than the previous object expect visual artefacts
	 */
	public void edit_buffer_index(int index , int buffer_index,Vertex2d[] data){
		BufferObject buffer = get_buffer(index);
		int pos = 0;
		for(Vertex2d vertex :data){
			buffer.verticies.put(pos + buffer_index,vertex.x);
			
			buffer.textures.put(pos + buffer_index,vertex.u);
			pos++;
			buffer.verticies.put(pos + buffer_index,vertex.y);
			buffer.textures.put(pos + buffer_index,vertex.v);
			pos++;
		}
		buffer_objects.set(index,buffer);
		buffer.rewind();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.vertex_id);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER,0, buffer.verticies);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.texture_id);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0,buffer.textures);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
	}
	public void edit_rotation_index(int index, int buffer_index, Vertex2d[] data){
		BufferObject buffer = get_buffer(index);
		int pos = 0;
		for(Vertex2d rot_vert : data){
			buffer.rotations.put(pos + buffer_index,rot_vert.x);
			pos++;
			buffer.rotations.put(pos + buffer_index,rot_vert.y);
			pos++;
			buffer.rotations.put(pos + buffer_index,rot_vert.u);
			pos++;
		}
		buffer_objects.set(index,buffer);
		buffer.rewind();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.rotation_id);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER,0, buffer.rotations);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	public void edit_rotation_index_static(int index, int buffer_index, Vertex2d[] data){
		BufferObject buffer = get_buffer(index);
		int pos = 0;
		
		for(Vertex2d rot_vert : data){
			buffer.rotations.put(pos + buffer_index,rot_vert.x);
			pos++;
			buffer.rotations.put(pos + buffer_index,rot_vert.y);
			pos++;
			buffer.rotations.put(pos + buffer_index,rot_vert.u);
			pos++;
		}
		buffer_objects.set(index,buffer);
		buffer.rewind();
	}
	/**
	 * Modify the specific index of the data in the buffer , this does not update the stored memory use update_buffer(index) for this
	 * @param index the index of the buffer
	 * @param buffer_index the index of the data inside the referenced buffer
	 * @param data the data to overwrite the currently stored data , if this is larger than the previous object within here expect visual artefacts
	 */
	public void edit_buffer_index_static(int index , int buffer_index , Vertex2d[] data){
		BufferObject buffer = get_buffer(index);
		int pos = 0;
		for(Vertex2d vertex :data){
			buffer.verticies.put(pos+buffer_index,vertex.x);
			buffer.textures.put(pos+buffer_index,vertex.u);
			pos++;
			buffer.verticies.put(pos+buffer_index,vertex.y);
			buffer.textures.put(pos+buffer_index,vertex.v);
			pos++;
		}
		buffer_objects.set(index,buffer);
	}
	/**
	 * updates the specified buffers gpu data with currently stored data , used after extend_buffer_static
	 * @param index the index of the data
	 */
	public void update_buffer(int index){
		BufferObject buffer = get_buffer(index);
		buffer.rewind();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.vertex_id);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER,0, buffer.verticies);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.texture_id);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0,buffer.textures);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.rotation_id);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0,buffer.rotations);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
	}
	/**
	 * Clears the stored buffer data and overwrites it with new data and then updates the GPU memory
	 * @param index the index of the buffer to be rewritten
	 * @param data the data to be written into the buffer
	 */
	public void edit_buffer(int index , Vertex2d[] data){//wiping an entire buffer and refilling it with this data
		BufferObject buffer = get_buffer(index);
		buffer.clear();
		int vertex_count  = 0;
		for(Vertex2d vertex :data){
			buffer.verticies.put(vertex.x);
			buffer.textures.put(vertex.u);
			buffer.verticies.put(vertex.y);
			buffer.textures.put(vertex.v);
			vertex_count+=2;
		}
		buffer.vert_count = vertex_count;
		buffer_objects.set(index, buffer);
		buffer.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.vertex_id);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER,0, buffer.verticies);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.texture_id);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer.textures);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	/**
	 * set the buffer screen coordinates , input pixel coordinates
	 * @param index the index of the buffer
	 * @param translation the Vertex2d of translation , only x and y are used.
	 */
	public void set_position(int index , Vertex2d translation){
		BufferObject buffer = get_buffer(index);
		buffer.translation = core.util.convert_coordinates(translation);
		buffer_objects.set(index,buffer);
	}
	public void set_texture(int index , Texture t){
		get_buffer(index).set_texture(t);
	}
	public void clear_buffer(int index){
		get_buffer(index).clear();
		
	}
	public void set_rotation1(Vertex2d coordinate,float rotation, int buffer_id){
		BufferObject buffer = get_buffer(buffer_id);
		buffer.set_rotation1(coordinate, rotation);
	}
	public void set_rotation2(Vertex2d coordinate,float rotation, int buffer_id){
		BufferObject buffer = get_buffer(buffer_id);
		buffer.set_rotation2(coordinate,rotation);
	}
	public void push_top(int buffer_id){
		BufferObject new_top = get_buffer(buffer_id);
		buffer_objects.remove(new_top);
		buffer_objects.add(new_top);
	}
	/**
	 * destroy all currently stored buffers and destroy the shader object.
	 * If a new graphics_handler is being created this must be called and then must be recreated through new Graphics_handler();
	 */
	public void dispose(){
		for(BufferObject buffer : buffer_objects){
			buffer.dispose();
			GL15.glDeleteBuffers(buffer.vertex_id);
			GL15.glDeleteBuffers(buffer.texture_id);
			GL15.glDeleteBuffers(buffer.rotation_id);
		}
		vbo_core.dispose();
	}
	public void delete_buffer(int id){
		BufferObject buffer = get_buffer(id);
		buffer.dispose();
		GL15.glDeleteBuffers(buffer.vertex_id);
		GL15.glDeleteBuffers(buffer.texture_id);
		GL15.glDeleteBuffers(buffer.rotation_id);
		
	}
	

	public void create_display(int width, int height, String name, int framerate) {

		GLFW.glfwInit();
		core.window = GLFW.glfwCreateWindow(width, height,
				name, 0, 0);
		this.width = width;
		this.height = height;
		GLFW.glfwMakeContextCurrent(core.window);
		GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_FALSE);
		GLFW.glfwSwapInterval(1);
		GLFW.glfwShowWindow(core.window);
		GL.createCapabilities();
		vbo_core = new CoreVBO();
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

	}

}
