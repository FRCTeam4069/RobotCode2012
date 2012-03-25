package frc.t4069.utils;

import java.util.Hashtable;

import edu.wpi.first.wpilibj.Joystick;
import frc.t4069.utils.math.Point;

public class GameController {

	public static class EventHandler {
		public void buttonDown() {

		}

		public void buttonUp() {

		}

		public void buttonHeld() {

		}
	}

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

	private Hashtable m_lastButtonStatus = new Hashtable();
	private Hashtable m_thisButtonStatus = new Hashtable();
	private Hashtable m_handlers = new Hashtable();

	/**
	 * The Joystick in the back. Done like this so I don't have to override the
	 * constructors.
	 * 
	 * TODO: Override the constructor and extend from Joystick
	 */
	public Joystick joystick;

	public GameController(Joystick j) {
		joystick = j;
		EventHandler placeholder = new EventHandler();
		for (int i = 1; i <= 10; i++) {
			m_thisButtonStatus.put(new Integer(i), new Boolean(getButton(i)));
			m_handlers.put(new Integer(i), placeholder);
		}
		tick();
	}

	public double getTrigger() {
		return joystick.getRawAxis(3);
	}

	public void addButtonHandler(int button, EventHandler handler) {
		m_handlers.put(new Integer(button), handler);
	}

	public void tick() {
		for (int i = 1; i <= 10; i++) {
			boolean lastStatus = ((Boolean) m_thisButtonStatus.get(new Integer(
					i))).booleanValue();
			boolean thisStatus = getButton(i);
			m_lastButtonStatus.put(new Integer(i), new Boolean(lastStatus));
			m_thisButtonStatus.put(new Integer(i), new Boolean(thisStatus));
			if (thisStatus)
				((EventHandler) m_handlers.get(new Integer(i))).buttonHeld();

			if (lastStatus && !thisStatus)
				((EventHandler) m_handlers.get(new Integer(i))).buttonUp();
			if (!lastStatus && thisStatus)
				((EventHandler) m_handlers.get(new Integer(i))).buttonDown();
		}
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
