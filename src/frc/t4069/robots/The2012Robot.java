package frc.t4069.robots;

import java.util.Date;

import com.sun.squawk.util.MathUtils;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.t4069.robots.commands.CommandBase;
import frc.t4069.robots.commands.DriveWithGameController;
import frc.t4069.utils.Logger;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class The2012Robot extends IterativeRobot {

	DriveWithGameController driveWithController;
	Command doDebugInfo;
	Command autoShooter;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	public void robotInit() {
		// instantiate the command used for the autonomous period
		// Initialize all subsystems

		CommandBase.init();
		driveWithController = new DriveWithGameController();
		// For knowing whose code is on the robot.
		SmartDashboard.putString("Author", Version.author);
		Logger.i("Robot initialized.");
	}

	public void autonomousInit() {

	}

	int ballsShot = 0;
	int lastStatusSustained = 0;
	boolean lastStatus = false;
	long lastRecognized;

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		if (ballsShot == 2) {
			if (new Date().getTime() - lastRecognized < 2000) {
				CommandBase.shooter.set(-0.55);
				CommandBase.conveyor.reverse();
				SmartDashboard
						.putString("Autonomous", "Spin down in 2 seconds");
			} else {
				SmartDashboard.putString("Autonomous", "Ended");
				CommandBase.shooter.set(0);
			}
		} else if (ballsShot < 2) {
			SmartDashboard.putString("Autonomous", "In progress");
			CommandBase.shooter.set(-0.55);
			CommandBase.conveyor.reverse();
			boolean thisStatus = CommandBase.shooter.isBallThere();
			if (thisStatus) lastStatusSustained++;
			if (lastStatus && !thisStatus) {
				if (lastStatusSustained > 4) ballsShot++;
				lastStatusSustained = 0;
				if (ballsShot == 2) lastRecognized = new Date().getTime();
			}
			lastStatus = thisStatus;
		}

	}

	public void disabledInit() {
		Logger.i("Disabled!");
		SmartDashboard.putString("Autonomous", "Ended");
	}

	public void disabledPeriodic() {

	}

	public void disabledContinuous() {

	}

	public void teleopInit() {
		SmartDashboard.putString("Autonomous", "Ended");
		driveWithController.start();
	}

	/**
	 * This function is called periodically during operator control.
	 */

	public void teleopPeriodic() {
		Scheduler.getInstance().run();

		SmartDashboard.putBoolean("Ball Ready To Shoot",
				CommandBase.shooter.isBallThere());
		SmartDashboard.putBoolean("Shooting In Progress",
				CommandBase.shooter.shootingInProgress());

		if (CommandBase.shooter.isShooterReady())
			SmartDashboard.putString("Shooter Status", "Ready");
		else
			SmartDashboard.putString("Shooter Status", "Not Ready");
		double shooterVoltage = MathUtils.round(CommandBase.shooter
				.getVoltage() * 100) / 100.0;
		SmartDashboard.putDouble("Shooter Voltage", shooterVoltage);
	}
}
