package com.lcass.core;

import java.lang.reflect.Method;

import org.lwjgl.glfw.GLFW;

import com.lcass.game.Game;
import com.lcass.graphics.Graphics_handler;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.core_util;
import com.lcass.util.InputHandler;

public class Core{
	public static int width = 1600;
	public static int height = (width /16) * 9;
	public core_util util;
	public long window;
	public InputHandler ih;
	public Graphics_handler graphics;
	public Core(){	
	}
	public static void main(String[] args){
		new Core().run();
	}
	public void cleanup(){	
		util.cleanup();
		graphics.dispose();
		GLFW.glfwTerminate();
		
	}
	public static Vertex2d get_dimensions(){
		return new Vertex2d(width,height);
	}
	public Vertex2d get_cam_mod(){
		int xmod = width/64;
		int ymod = height/64;
		return new Vertex2d(xmod,ymod);
	}
	public void run() {
		util = new core_util(this);
		graphics = new Graphics_handler(this);
		util.create_display(width, height, "Spess battaru",100, 60);
		Method close = util.obtain_method(this.getClass(), "cleanup");
		util.bind_close_function(close);
		//bind graphics handler tick
		Method render = util.obtain_method(graphics.getClass(), "render");
		util.bind_render_function(render, graphics);
		//Create Game
		new Game(this);
		//int texture
		//setup background
		//do the top layers*/
		util.start_tick();
	}

}
