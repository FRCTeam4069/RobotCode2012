package frc.t4069.robots;

import edu.wpi.first.wpilibj.Joystick;
import frc.t4069.utils.GameController;

public class OI {
	public static final int GCPORT = 1;
	public static final int LEFTJOYSTICKPORT = 2;
	public static final int RIGHTJOYSTICKPORT = 3;

	private GameController gc;
	private Joystick m_joystickRight;
	private Joystick m_joystickLeft;

	public OI() {
		gc = new GameController(new Joystick(GCPORT));
		m_joystickLeft = new Joystick(LEFTJOYSTICKPORT);
		m_joystickRight = new Joystick(RIGHTJOYSTICKPORT);
	}

	public GameController getController() {
		return gc;
	}

	public Joystick getLeftJoystick() {
		return m_joystickLeft;
	}

	public Joystick getRightJoystick() {
		return m_joystickRight;
	}
}
