package frc.t4069.robots.commands;

import frc.t4069.utils.GameController;
import frc.t4069.utils.math.Point;

public class DoDebug extends CommandBase {

	protected void initialize() {
		// TODO Auto-generated method stub

	}

	protected void execute() {
		GameController gc = CommandBase.oi.getController();
		Point leftStick = gc.getLeftStick();
		// TODO Auto-generated method stub
		double y = (leftStick.y + 1) / 2.0;
		double x = (leftStick.x + 1) / 2.0;
		CommandBase.cameraMount.setTilt(y);
		CommandBase.cameraMount.setPan(x);
	}

	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	protected void end() {
		// TODO Auto-generated method stub

	}

	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
