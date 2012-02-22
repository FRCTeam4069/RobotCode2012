package frc.t4069.robots;

import com.sun.squawk.util.MathUtils;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.t4069.robots.commands.CommandBase;
import frc.t4069.robots.commands.DriveWithGameController;
import frc.t4069.robots.commands.DriveWithKinect;
import frc.t4069.utils.Logger;
import frc.t4069.utils.networking.CommLink;

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
	Command driveWithKinect;
	private DigitalInput sensor1;

	private DriverStation m_ds;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	public void robotInit() {
		m_ds = DriverStation.getInstance();

		// instantiate the command used for the autonomous period
		// Initialize all subsystems

		CommandBase.init();
		driveWithController = new DriveWithGameController();
		// For knowing whose code is on the robot.
		SmartDashboard.putString("Author", Version.author);
		Logger.i("Robot initialized.");
	}

	public void autonomousInit() {
		driveWithKinect = new DriveWithKinect();
		driveWithKinect.start();
	}

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public void disabledInit() {
		Logger.i("Disabled!");
		CommLink.consecutiveFailures = 0;
		driveWithController.stopRoller();
	}

	public void disabledPeriodic() {

	}

	public void disabledContinuous() {

	}

	public void teleopInit() {

		driveWithController.start();
	}

	/**
	 * This function is called periodically during operator control.
	 */

	public void teleopPeriodic() {
		Scheduler.getInstance().run();

		SmartDashboard.putBoolean("Ball Ready To Shoot",
				CommandBase.shooter.isBallThere());
		SmartDashboard.putBoolean("Roller Running",
				driveWithController.rollerRunning());
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
