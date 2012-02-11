package frc.t4069.utils.math;

import java.util.Date;
import java.util.Random;

public class DateRandom {

	public DateRandom() {
		seed = new Date().getTime();
		rand = new Random(seed);
	}

	public double nextDouble(double floor, double ceiling) {
		double temp;
		temp = rand.nextDouble();
		temp *= (ceiling - floor) + floor;
		return temp;
	}


	private Random rand;
	private long seed;
}
