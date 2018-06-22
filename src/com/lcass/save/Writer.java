package com.lcass.save;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Writer {
	public Writer(){
		
	}
	public void save_obj(Object o,String address){
		file f = new file();
		f.set_file(o);
		f.set_target(address);
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			fout = new FileOutputStream(address);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(f);

			System.out.println("Done");

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {

			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}
}
