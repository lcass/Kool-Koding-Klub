package com.lcass.util;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import org.lwjgl.glfw.GLFW;

import com.lcass.core.Core;
import com.lcass.graphics.Vertex2d;

import static org.lwjgl.glfw.Callbacks.*;
public class InputHandler {

	public boolean a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t,
			u, v, w, x, y, z,space,lbrack,rbrack, shift, control,left, right, up, down, mouse1, mouse2, mouse3,esc;
	// value

	private PointerInfo mouse;
	private Point mousepos;
	private Core core;
	private Vertex2d mouseposition = new Vertex2d(0, 0);

	public InputHandler(Core core) {
		this.core = core;

	}

	public Vertex2d obtain_mouse() {
		double[] x = new double[1];
		double[] y = new double[1];
		GLFW.glfwGetCursorPos(core.window, x, y);
		y[0] = core.height - y[0];
		return new Vertex2d((int)x[0],(int)y[0]);
	}

	private boolean pressed = false;

	public void tick() {

		a = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_A) == 1;
		w = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_W) == 1;
		s = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_S) == 1;
		d = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_D) == 1;
		e= GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_E) == 1;
		q = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_Q) == 1;
		r = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_R) == 1;
		l = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_L) == 1;
		space = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_SPACE)==1;
		lbrack = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_LEFT_BRACKET) == 1;
		rbrack = GLFW.glfwGetKey(core.window,GLFW.GLFW_KEY_RIGHT_BRACKET) == 1;
		shift = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_LEFT_SHIFT) == 1;
		control = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_LEFT_CONTROL) == 1;
		right = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_RIGHT) == 1;
		left = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_LEFT) == 1;
		up = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_UP) == 1;
		down = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_DOWN) == 1;
		esc = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_ESCAPE) == 1;
		c = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_C) == 1;
		a = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_A) == 1;
		a = GLFW.glfwGetKey(core.window, GLFW.GLFW_KEY_A) == 1;
		mouse1 = GLFW.glfwGetMouseButton(core.window, GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS;
		mouse2 = GLFW.glfwGetMouseButton(core.window, GLFW.GLFW_MOUSE_BUTTON_2) == GLFW.GLFW_PRESS;
	}
}
