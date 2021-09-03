package com.example.restservice;
import java.lang.Math;

public class Calculate {

	private double id;

	public Calculate(double id) {
		this.id = id;
	}
	public Calculate() {
		this.id = 0.0001;

		for(int z = 0; z <= 1000000; z++ ){
			id += Math.sqrt(id);
			System.out.println("Calculate: " + id + " : " + z);
		}
	}

	public double getId() {
		return id;
	}

}
