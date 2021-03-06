package com.lcass.graphics;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.lcass.core.Core;
import com.lcass.graphics.texture.spritecomponent;
import com.lcass.util.Encapsulated_method;
import com.lcass.util.Util;

public class core_util {
	private Core core;
	private int width, height, framerate, tickrate = 0;
	private Encapsulated_method closemethod;
	private boolean running = false;

	public core_util(Core core) {
		this.core = core;
		
	}
	private ArrayList<Encapsulated_method> methods = new ArrayList<Encapsulated_method>();
	private ArrayList<Encapsulated_method> render_methods = new ArrayList<Encapsulated_method>();
	private ArrayList<Object> outputs = new ArrayList<Object>();

	public int getwidth() {
		return width;
	}

	public int getheight() {
		return height;
	}


	public void create_display(int width, int height, String name,
			int framerate, int tickrate) {
		core.graphics.create_display(width , height , name , framerate);
	
		this.width = width;
		this.height = height;
		this.framerate = framerate;
		this.tickrate = tickrate;
	}
	//defines for tick function;
	private int frames = 0;
	private int ticks = 0;
	public void kill(){
		running = false;
		cleanup();
	}
	public Object start_tick() {
		running = true;
		if (tickrate <= 0 || framerate <= 0) {
			System.out.println("invalid tick and framerate parameters");
			return null;
		}
		float delta, deltaframe = 0;
		long timeframe = 1000000000 / framerate;
		long timetick = 1000000000 / tickrate;
		long curtime = System.nanoTime();
		long lastime = System.nanoTime();
		long lasttimeframe = System.nanoTime();
		
		while (running) {
			if (GLFW.glfwWindowShouldClose(core.window)) {
				if (closemethod != null) {
					return closemethod.call_back();
				} else {
					System.out
							.println("no close method detected , performing standard cleanup");
					cleanup();
					return null;
				}
			}
			curtime = System.nanoTime();
			delta = curtime - lastime;
			if (delta >= timetick) {

				lastime = System.nanoTime();
				tick();
				ticks += 1;
			}
			if (ticks >= tickrate) {

				System.out.println("FPT:" + frames);
				frames = 0;
				ticks = 0;
			}

			deltaframe = curtime - lasttimeframe;
			if (deltaframe >= timeframe) {
				render();
				frames += 1;
				lasttimeframe = System.nanoTime();

			}

		}
		return null;
	}

	public void stop_tick() {
		running = false;
	}

	public void cleanup() {
		System.out.println("Closed correctly");
		methods.clear();
		outputs.clear();
		
	}
	

	private void tick() {
		for (int i = 0; i < methods.size(); i++) {
			methods.get(i).call();
		}

	}

	public void render() {
		core.graphics.render();
		for(int i = 0; i < render_methods.size();i++){//manual rendering , must be done through the graphics handler
			render_methods.get(i).call();
		}
	}
	public Vertex2d revert_coordinates(Vertex2d coordinates){
		Vertex2d temp = new Vertex2d(coordinates.x * width, coordinates.y * height);
		temp.x = (temp.x )/2;
		temp.y = (temp.y)/2;
		return temp;
		
	}
	public int get_tick(){
		return ticks;
	}
	public Method obtain_method(Class instance, String name) {
		try {
			Method value = instance.getMethod(name, null);

			return value;
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public Method obtain_method(Class instance, String name,Class[] parameters) {
		try {
			Method value = instance.getMethod(name, parameters);

			return value;
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void bind_function(Method method, Object[] parameters) {// must be
																	// static!
		methods.add(new Encapsulated_method(method, parameters, core));
	}
	public void bind_render_function(Method method,Object instance){
		render_methods.add(new Encapsulated_method(method,null,instance));
	}

	public void bind_function(Method method) {// no parameters!
		methods.add(new Encapsulated_method(method, null, core));
	}
	public void bind_function(Method method,Object instance){
		methods.add(new Encapsulated_method(method,null,instance));
	}
	public void bind_close_function(Method method) {
		closemethod = new Encapsulated_method(method, null, core);
	}

	public void clear_function() {
		methods.clear();
	}

	public Vertex2d obtain_dimensions() {
		return new Vertex2d(width, height);
	}

	private Object myobject;

	public void bind(Object obj) {
		myobject = obj;
	}

	public Object obtain() {
		return myobject;// when this is returned is it myobject ie a c++
						// reference style object or a copy
	}

	public Vertex2d convert_coordinates(int x, int y) {
		return new Vertex2d((float) x / width, (float) y / height);
	}

	public float convert_coordinate(int i, int parameter) {
		return (float) i / (float) parameter;
	}

	public Vertex2d[] square(int x, int y, int sizelen,
			spritecomponent s) {
		return rectangle(x,y,sizelen,sizelen,s);
//		float sizex = convert_coordinate(sizelen * 2, width);
//		float sizey = convert_coordinate(sizelen * 2, height);
//		x = x * 2;
//		y = y * 2;
//		x = x - (width);
//		y = y - (height - sizelen);
//		
//		Vertex2d new_coords = convert_coordinates(x, y);
//		float xp = new_coords.x;
//		float yp = new_coords.y;
//
//	    Vertex2d[] data = new Vertex2d[] { new Vertex2d(xp, yp, s.x, s.y),
//				new Vertex2d(xp + sizex, yp, s.ex, s.y),
//				new Vertex2d(xp, yp - sizey, s.x, s.ey),
//				new Vertex2d(xp + sizex, yp - sizey, s.ex, s.ey),
//				new Vertex2d(xp, yp - sizey, s.x, s.ey),
//				new Vertex2d(xp + sizex, yp, s.ex, s.y) };
//		return data;
	}

	public Vertex2d[] rectangle(int x, int y, int width, int height,
			spritecomponent s) {
		float sizex = convert_coordinate(width *2, this.width);
		float sizey = convert_coordinate(height * 2, this.height);
		x = x * 2;
		y = y * 2;
		x = x - (this.width);
		y = y - (this.height - (height * 2));
		Vertex2d new_coords = convert_coordinates(x, y);
		float xp = new_coords.x;
		float yp = new_coords.y;
		
		Vertex2d[] data = new Vertex2d[] { new Vertex2d(xp, yp, s.x, s.y),
				new Vertex2d(xp + sizex, yp, s.ex, s.y),
				new Vertex2d(xp, yp - sizey, s.x, s.ey),
				new Vertex2d(xp + sizex, yp - sizey, s.ex, s.ey),
				new Vertex2d(xp, yp - sizey, s.x, s.ey),
				new Vertex2d(xp + sizex, yp, s.ex, s.y) };
		return data;
	}
	public Vertex2d[] square_vert(int x, int y, int sizelen,
			spritecomponent s) {
		float sizex = convert_coordinate(sizelen * 2, width);
		float sizey = convert_coordinate(sizelen * 2, height);
		x = x * 2;
		y = y * 2;
		x = x - (width);
		y = y - (height - sizelen);
		
		Vertex2d new_coords = convert_coordinates(x, y);
		float xp = new_coords.x;
		float yp = new_coords.y;

		Vertex2d[] data = new Vertex2d[] { new Vertex2d(xp, yp, s.x, s.y),
				new Vertex2d(xp + sizex, yp, s.ex, s.y),
				new Vertex2d(xp, yp - sizey, s.x, s.ey),
				new Vertex2d(xp + sizex, yp - sizey, s.ex, s.ey),
				new Vertex2d(xp, yp - sizey, s.x, s.ey),
				new Vertex2d(xp + sizex, yp, s.ex, s.y) };
		return data;
	}
	
	public Vertex2d[] rectangle_vert(int x, int y, int width, int height,
			spritecomponent s) {
		float sizex = convert_coordinate(width *2, this.width);
		float sizey = convert_coordinate(height * 2, this.height);
		x = x * 2;
		y = y * 2;
		x = x - (this.width);
		y = y - (this.height - (height * 2));
		Vertex2d new_coords = convert_coordinates(x, y);
		float xp = new_coords.x;
		float yp = new_coords.y;
		
		Vertex2d[] data = new Vertex2d[] { new Vertex2d(xp, yp, s.x, s.y),
				new Vertex2d(xp + sizex, yp, s.ex, s.y),
				new Vertex2d(xp, yp - sizey, s.x, s.ey),
				new Vertex2d(xp + sizex, yp - sizey, s.ex, s.ey),
				new Vertex2d(xp, yp - sizey, s.x, s.ey),
				new Vertex2d(xp + sizex, yp, s.ex, s.y) };
		return data;
	}
	public Vertex2d[] rectangle_vert(int x, int y, int width, int height,
			spritecomponent s,Vertex2d rotation) {
		
		float sizex = convert_coordinate(width *2, this.width);
		float sizey = convert_coordinate(height * 2, this.height);
		x = x * 2;
		y = y * 2;
		x = x - (this.width);
		y = y - (this.height - (height * 2));
		Vertex2d new_coords = convert_coordinates(x, y);
		float xp = new_coords.x;
		float yp = new_coords.y;
		Vertex2d new_rotation = convert_coordinates(rotation.whole().mult(2).sub(new Vertex2d(this.width,this.height - 2* height)));
		Vertex2d[] data = new Vertex2d[] { new Vertex2d(xp, yp, s.x, s.y),
				new Vertex2d(xp + sizex, yp, s.ex, s.y),
				new Vertex2d(xp, yp - sizey, s.x, s.ey),
				new Vertex2d(xp + sizex, yp - sizey, s.ex, s.ey),
				new Vertex2d(xp, yp - sizey, s.x, s.ey),
				new Vertex2d(xp + sizex, yp, s.ex, s.y) };
		return Util.rotate_xy(data, new_rotation, 0.5f);
	}
	public Vertex2d convert_coordinates(Vertex2d coordinates){
		Vertex2d temp = new Vertex2d(coordinates.x * 2, coordinates.y * 2);
		temp.x = (temp.x )/width;
		temp.y = (temp.y)/height;
		temp.u = coordinates.u;
		temp.v = coordinates.v;
		return temp;
	}
	public Vertex2d[] convert_coordinates(Vertex2d coordinates[]){
		Vertex2d[] to_return = new Vertex2d[coordinates.length];
		for(int i = 0; i < coordinates.length;i++){
			to_return[i] = convert_coordinates(coordinates[i]);
		}
		return to_return;
		
	}
	
	/**
	 * generate a vertex2d array of two triangles forming a square
	 * @param x 
	 * @param y
	 * @param sizelen
	 * @param s
	 * @return
	 */
	public Vertex2d[] square_vertex(int x, int y, int sizelen, spritecomponent s) {
		sizelen = sizelen * 2;
		float sizex = convert_coordinate(sizelen, width);
		float sizey = convert_coordinate(sizelen, height);
		x = x * 2;
		y = y * 2;
		x = x - (width);
		y = y - (height - sizelen);
		Vertex2d new_coords = convert_coordinates(x, y);

		float xp = new_coords.x;
		float yp = new_coords.y;
		Vertex2d[] data = new Vertex2d[] { new Vertex2d(xp, yp, s.x, s.y),
				new Vertex2d(xp + sizex, yp, s.ex, s.y),
				new Vertex2d(xp, yp - sizey, s.x, s.ey),
				new Vertex2d(xp + sizex, yp - sizey, s.ex, s.ey),
				new Vertex2d(xp, yp - sizey, s.x, s.ey),
				new Vertex2d(xp + sizex, yp, s.ex, s.y) };
		return data;
	}

	/**
	 * 
	 * Fix float values to within a reasonable position
	 * @param a
	 * 
	 */
	public float make_accurate(float a){//fixes floats
		int b = (int) (a * 1000000);
		
		return (float)(b)/1000000f;
		
	}

	public Vertex2d centre_point(Vertex2d a, Vertex2d b){//x = 5, y = 2 ,x = 2,y = -4
		float mid_x = Math.min(a.x, b.x) - Math.max(a.x, a.x);
		float mid_y = Math.min(a.y, b.y) - Math.max(b.y, b.y);
		return new Vertex2d(a.x + (mid_x/2),a.y + (mid_y/1));
	}
}
