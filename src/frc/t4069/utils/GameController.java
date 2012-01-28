package frc.t4069.utils;

import edu.wpi.first.wpilibj.Joystick;
import frc.t4069.utils.math.Point;

public class GameController {

	public static final int BTN_A = 1;
	public static final int BTN_B = 2;
	public static final int BTN_X = 3;
	public static final int BTN_Y = 4;
	public static final int BTN_LB = 5;
	public static final int BTN_RB = 6;
	public static final int BTN_BACK = 7;
	public static final int BTN_START = 8;
	public static final int BTN_LEFT_JOYSTICK = 9;
	public static final int BTN_RIGHT_JOYSTICK = 10;

	/**
	 * The Joystick in the back. Done like this so I don't have to override the
	 * constructors.
	 * 
	 * TODO: Override the constructor and extend from Joystick
	 */
	public Joystick joystick;

	public GameController(Joystick j) {
		joystick = j;
	}

	public double getTrigger() {
		return joystick.getRawAxis(3);
	}

	// Axis 3 is the RT and LT.. but they're on the same Axis...

	public Point getLeftStick() {
		Point p = new Point(joystick.getRawAxis(1), joystick.getRawAxis(2));
		p.y *= -1;
		return p;
	}

	public Point getRightStick() {
		Point p = new Point(joystick.getRawAxis(4), joystick.getRawAxis(5));
		p.y *= -1;
		return p;
	}

	public boolean getButton(int button) {
		return joystick.getRawButton(button);
	}
}
