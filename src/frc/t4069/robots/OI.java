package frc.t4069.robots;

import edu.wpi.first.wpilibj.Joystick;
import frc.t4069.utils.GameController;

public class OI {
	public static final int JOYSTICK1_PORT = 1;
	public static final int JOYSTICK2_PORT = 2;
	public static final boolean GAMECONTROLLER = true;

	private GameController gc;
	private Joystick j1;
	private Joystick j2;

	public OI() {
		if (GAMECONTROLLER) {
			j1 = new Joystick(JOYSTICK1_PORT);
			gc = new GameController(j1);
		} else {
			j1 = new Joystick(JOYSTICK1_PORT);
			j2 = new Joystick(JOYSTICK2_PORT);
		}
	}

	public GameController getController() {
		return gc;
	}
}
