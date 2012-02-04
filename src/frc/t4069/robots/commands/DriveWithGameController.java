package frc.t4069.robots.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import frc.t4069.robots.subsystems.BallPickup;
import frc.t4069.robots.subsystems.DriveTrain;
import frc.t4069.utils.GameController;
import frc.t4069.utils.math.Point;

public class DriveWithGameController extends Command {

	public DriveWithGameController() {
		requires(CommandBase.drivetrain);
		requires(CommandBase.ballpickup);
	}

	protected void initialize() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unused")
	protected void execute() {
		GameController gc = CommandBase.oi.getController();
		BallPickup bp = new BallPickup();
		Point rightStick = gc.getRightStick();
		Point leftStick = gc.getLeftStick();
		DriverStation ds = DriverStation.getInstance();
		double rc = ds.getAnalogIn(1) * 1000;
		double sensitivity = ds.getAnalogIn(2) / 5.0;
		DriveTrain.setRC(rc);
		if (gc.getButton(GameController.BTN_RB)
				|| gc.getButton(GameController.BTN_LB))
			CommandBase.drivetrain.hardBreak();
		else CommandBase.drivetrain.arcadeDrive(gc.getTrigger(), rightStick.x
				* sensitivity);
		if (gc.getButton(GameController.BTN_X)) {
			bp.Lower();
		} else {
			bp.Raise(0.5);
		}
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
