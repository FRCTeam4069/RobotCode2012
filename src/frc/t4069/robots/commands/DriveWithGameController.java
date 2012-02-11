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
		Point rightStick = gc.getRightStick();
		DriverStation ds = DriverStation.getInstance();
		double sensitivity = ds.getAnalogIn(2) / 5.0;

		processCamera(rightStick);
		processDriveTrain(gc, sensitivity);
		processArm(gc);

		if (gc.getButton(GameController.BTN_X))
			CommandBase.pickupArm.runRoller(ds.getAnalogIn(1) / 5.0);
		else
			CommandBase.pickupArm.runRoller(0);

	}

	protected void processArm(GameController gc) {
		if (gc.getButton(GameController.BTN_Y))
			CommandBase.pickupArm.forward();
		else if (gc.getButton(GameController.BTN_A))
			CommandBase.pickupArm.reverse();
		else
			CommandBase.pickupArm.stop();
	}

	protected void processDriveTrain(GameController gc, double sensitivity) {
		if (gc.getButton(GameController.BTN_RB)
				|| gc.getButton(GameController.BTN_LB))
			CommandBase.drivetrain.hardBreak();
		else
			CommandBase.drivetrain.arcadeDrive(gc.getTrigger(),
					gc.getLeftStick().x * sensitivity);
	}

	protected void processCamera(Point stick) {
		double y = (stick.y + 1) / 4.0 + 0.25;
		double x = (stick.x + 1) / 2.0;
		CommandBase.cameraMount.setTilt(y);
		CommandBase.cameraMount.setPan(x);
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
