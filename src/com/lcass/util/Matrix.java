package com.lcass.util;

public class Matrix {//designed for square transformations, no weird shit here
	private float[][] data;
	/**
	 * Initializes to the identity nxn matrix
	 */
	public Matrix(int size) {
		data = new float[size][size];
		for(int i = 0; i < size;i++) {
			for(int j = 0;j< size;j++) {
				if(i==j) {
					data[i][j] = 1;
				}
			}
		}
	}
	public Matrix(float[][] starting_data) {
		this.data = data;
		if(data == null) {
			System.err.println("Warning, matrix initialized with null data");
		}
	}
	/**
	 * Performs this.data x m.data in that order.
	 * @param m
	 * @return
	 */

}
