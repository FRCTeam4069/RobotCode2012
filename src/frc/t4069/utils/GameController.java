package frc.t4069.utils;

import edu.wpi.first.wpilibj.Joystick;
import frc.t4069.utils.math.Point;

public class GameController {

	public static final int BTN_A = 1;
	public static final int BTN_B = 2;
	public static final int BTN_X = 3;
	public static final int BTN_Y = 4;

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
