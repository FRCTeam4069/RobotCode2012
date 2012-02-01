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
		double returnval;
		temp = rand.nextDouble();
		temp = temp * (ceiling - floor) + floor;
		returnval = temp;
		return temp;
	}


	private Random rand;
	private long seed;
}
