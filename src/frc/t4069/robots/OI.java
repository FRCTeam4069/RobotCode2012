package frc.t4069.robots;

import edu.wpi.first.wpilibj.Joystick;
import frc.t4069.utils.GameController;

public class OI {
	public static final int GCPORT = 1;

	private GameController gc;

	public OI() {
		gc = new GameController(new Joystick(GCPORT));
	}

	public GameController getController() {
		return gc;
	}
}
