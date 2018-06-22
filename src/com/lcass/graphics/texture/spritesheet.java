package com.lcass.graphics.texture;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class spritesheet {
	private Texture tex;
	private float x,y,ex,ey,width,height;
	public spritesheet(String location){
		tex = new Texture();
		tex = loadTexture(location);
		this.width = tex.width;
		this.height = tex.height;
	}
	public spritecomponent getcoords(int x, int y, int ex,int ey){
		spritecomponent temp= new spritecomponent();
		this.x = x/width;
		this.y = y/height;
		this.ex = ex/width;
		this.ey = ey/height;
		temp.set(this.x, this.y, this.ex, this.ey, tex);
		return temp;
	}
	/**
	 * Returns the texture file for the spritesheet
	 * @return
	 */
	public Texture gettexture(){
		return tex;
	}
	/**
	 * Stores the texture on the GPU
	 */
	public void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.id);
	}
	/**
	 * Removes the Texture from the GPU
	 */
	public void unbind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	/**
	 * Loads the texture file from a set spritesheet location, file needs to be PNG
	 * @param name
	 * @return
	 */
	public Texture loadTexture(String name) {

		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(Texture.class.getClassLoader()
					.getResourceAsStream(name));
		} catch (IOException e) {
			
			e.printStackTrace();
			System.out.println("Texture loading failure! " + name);

		}

		int[] pixels = new int[bimg.getWidth() * bimg.getHeight()];
		bimg.getRGB(0, 0, bimg.getWidth(), bimg.getHeight(), pixels, 0,
				bimg.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(bimg.getWidth()
				* bimg.getHeight() * 4);
		for (int y = 0; y < bimg.getHeight(); y++) {
			for (int x = 0; x < bimg.getWidth(); x++) {
				if (pixels[y * bimg.getWidth() + x] != -65281
						) {
					int pixel = pixels[y * bimg.getWidth() + x];

					buffer.put((byte) ((pixel >> 16) & 0xFF));

					buffer.put((byte) ((pixel >> 8) & 0xFF));

					buffer.put((byte) (pixel & 0xFF));

					buffer.put((byte) ((pixel >> 24) & 0xFF));
				}
				else if(pixels[y * bimg.getWidth() + x] != -1){
					buffer.put((byte)0);
					buffer.put((byte)0);
					buffer.put((byte)0);
					buffer.put((byte)0);
				}
				else if(pixels[y * bimg.getWidth() + x] == 0){
					buffer.put((byte)-1);
					buffer.put((byte)-1);
					buffer.put((byte)-1);
					buffer.put((byte)-1);
				}
				else{
					buffer.put((byte)-1);
					buffer.put((byte)-1);
					buffer.put((byte)-1);
					buffer.put((byte)-1);
				}
			}
		}
		buffer.flip();
		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, bimg.getWidth(),
				bimg.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		return new Texture(textureID, bimg.getWidth(), bimg.getHeight(),buffer);
	}
	public void bindtex(int textureID,int width , int height ,ByteBuffer buffer){
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width,
				height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	}
}
