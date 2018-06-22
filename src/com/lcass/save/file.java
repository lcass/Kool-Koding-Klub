package com.lcass.save;

import java.io.Serializable;

public class file implements Serializable{
	private Object to_save;
	private String save_target = "";
	public file(){
		
	}
	public void set_file(Object file){
		to_save = file;
	}
	public void set_target(String target){
		this.save_target=target;
	}

}
