/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frc.t4069.utils.math;

/**
 * A class meant to emulate java.awt.geom.Point2D.Double
 */
public class Point {
	public double x;
	public double y;

	public Point(double x, double y) {
		setLocation(x, y);
	}

	public static double distance(double px1, double py1, double px2, double py2) {
		double px = px2 - px1;
		double py = py2 - py1;
		return Math.sqrt(px * px + py * py);
	}

	public final double distance(Point p) {
		return distance(p.x, p.y);
	}

	public final double distance(double px, double py) {
		px -= x;
		py -= y;
		return Math.sqrt(px * px + py * py);
	}

	public final double distanceSq(Point p) {
		return distanceSq(p.x, p.y);
	}

	public final double distanceSq(double px, double py) {
		px -= x;
		py -= y;
		return (px * px + py * py);
	}

	public final void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public final void setLocation(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	public final void reverse() {
		this.x *= -1;
		this.y *= -1;
	}
}