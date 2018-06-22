package com.lcass.game;

import com.lcass.GUI.GUI;
import com.lcass.core.Core;
import com.lcass.graphics.Vertex2d;
import com.lcass.graphics.renderer;
import com.lcass.graphics.texture.spritesheet;
import com.lcass.util.InputHandler;

/*
 * This class is not loaded into the game, it just give examples of how to use different parts of the engine.
 */
public class Examples {
	private renderer renderer;
	public Examples(Core core) {
		renderer = new renderer(core);//initialise a new renderer
		/*
		 * Renderers are like trays with objects on them, you can either move the objects individually or you can move the entire tray, so for instance.
		 */
		renderer.set_rotation1(new Vertex2d(), 0);//used to set the rotation of the entire renderer
		int object_id = renderer.create_object(new Vertex2d(), new Vertex2d(0,0,16,16));//setup a new object, each object is stored with a unique id
		renderer.rotate(object_id, new Vertex2d(16,16), 0);//rotate the object and update the screen
		renderer.rotate_static(object_id, new Vertex2d(16,16), 0);//rotate the object but don't update the screen, use this for batch calls
		renderer.update_buffer();//update the screen.
		//similarly for translations
		renderer.set_position(new Vertex2d());//translate the tray
		renderer.translate_object(object_id, new Vertex2d());//translate a single object.
		renderer.translate_object_static(object_id, new Vertex2d());
		//We can also remove objects 
		renderer.remove_object_static(object_id);
		//We can either remove objects like this or batch remove an array list of them
		renderer.batch_remove(new int[] {object_id});//removes all objects then updates the screen.
		//we can also setup new objects that aren't just squares.
		object_id = renderer.create_object(new Vertex2d(0,0,16,32), new Vertex2d(0,0,16,16), true);//here the position variable has arguements (x_pos,y_pos,width,height)
		//We can clear everything from the renderer.
		renderer.clear();
		//We can also manipulate the shape of an object and change it's sprite on the go.
		renderer.edit_object(object_id, new Vertex2d(0,0,16,72), new Vertex2d(0,0,16,16),true);
		//If we want our renderer to be on top (after all other renderers have been created) We can use the push_top function.
		renderer.push_top();
		//We can also decide if the renderer actually renders to the screen at all.
		renderer.set_render(true);
		//Now if for some reason you need to edit render_objects directly and need to update the renderer you use this
		renderer.rebuild_buffer();
		//avoid doing this as it's very inefficient.
		
		//We can also take user input
		InputHandler input = new InputHandler(core);
		//We can obtain the key state of an object in the following way
		input.tick();//update the input handler
		boolean a = input.a;
		//We can obtain the mouse coordinates
		Vertex2d mouse = input.obtain_mouse();
		//We can check if the mouse has been clicked
		boolean clicked = input.mouse1;
		//however in order to do this we need to call input.tick() each tick so that we actually get the input information, we can also just call it when we need to check 
		//if a key has been pressed.
		
		//Here we bind tick to the master controller
		core.util.bind_function(core.util.obtain_method(this.getClass(), "tick"), this);
		
		//We can also load sprite sheets
		spritesheet sprites = new spritesheet("./com/textures/blocksprites.png");
		//We can then bind this set of sprites to our renderer using the following
		renderer.set_texture(sprites);
		//All of our sprites for current objects are taken from this new sprite sheet.
		//For GUI's we can add buttons and other fun things
		GUI gui = new GUI(core);
		int button = gui.create_button(new Vertex2d(0,0), new Vertex2d(0,0,16,16), core.util.obtain_method(this.getClass(), "button_click"),this);
		int overlay = gui.add_overlay(new Vertex2d(), new Vertex2d(0,0,16,16));//Basically just an image but stored in the GUI renderer
		//similar translation functions hold for the GUI aswell.
	}
	public void button_click(int[] data) {//called when the button is clicked, data[0] stores the ID of the button that clicked it so all button calls can be handled by a single function.
		
	}
	public void tick() {//if we wish to call something each tick (60tps) then we need to bind tick to the master controller
		//if we want to check the input every single tick we simply call it each tick
		//input.tick();
	}
}
