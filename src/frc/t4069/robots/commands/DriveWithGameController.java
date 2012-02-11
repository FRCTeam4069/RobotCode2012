package frc.t4069.robots.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import frc.t4069.utils.GameController;
import frc.t4069.utils.math.Point;

public class DriveWithGameController extends Command {

	public DriveWithGameController() {
		requires(CommandBase.drivetrain);
		requires(CommandBase.cameraMount);
		requires(CommandBase.pickupArm);
	}

	protected void initialize() {
		// TODO Auto-generated method stub

	}

	protected void execute() {
		GameController gc = CommandBase.oi.getController();
		Point leftStick = gc.getLeftStick();
		Point rightStick = gc.getRightStick();
		DriverStation ds = DriverStation.getInstance();
		double sensitivity = ds.getAnalogIn(2) / 5.0;

		double y = (rightStick.y + 1) / 4.0 + 0.25;
		double x = (rightStick.x + 1) / 2.0;
		CommandBase.cameraMount.setTilt(y);
		CommandBase.cameraMount.setPan(x);

		if (gc.getButton(GameController.BTN_RB)
				|| gc.getButton(GameController.BTN_LB))
			CommandBase.drivetrain.hardBreak();
		else
			CommandBase.drivetrain.arcadeDrive(gc.getTrigger(), leftStick.x
					* sensitivity);

		if (gc.getButton(GameController.BTN_Y))
			CommandBase.pickupArm.forward();
		else if (gc.getButton(GameController.BTN_A))
			CommandBase.pickupArm.reverse();
		else
			CommandBase.pickupArm.stop();

	}

	protected void interrupted() {
		// TODO Auto-generated method stub

	}

	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	protected void end() {
		// TODO Auto-generated method stub

	}

}
