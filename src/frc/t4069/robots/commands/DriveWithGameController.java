package frc.t4069.robots.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.t4069.utils.math.Point;

public class DriveWithGameController extends Command {

	public DriveWithGameController() {
		requires(CommandBase.drivetrain);
	}

	protected void initialize() {
		// TODO Auto-generated method stub

	}

	protected void execute() {
		Point rightStick = CommandBase.oi.getController().getRightStick();
		CommandBase.drivetrain.arcadeDrive(rightStick.y, rightStick.x);
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
