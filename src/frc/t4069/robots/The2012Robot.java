package frc.t4069.robots;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.t4069.robots.commands.CommandBase;
import frc.t4069.robots.commands.DoDebug;
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

	Command driveWithController;
	Command doDebugInfo;

	private DriverStation m_ds;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	public void robotInit() {
		m_ds = DriverStation.getInstance();

		// instantiate the command used for the autonomous period
		driveWithController = new DriveWithGameController();
		doDebugInfo = new DoDebug();
		// Initialize all subsystems

		CommandBase.init();
		doDebugInfo.start();

		// For knowing whose code is on the robot.
		SmartDashboard.putString("Author", Version.author);
		Logger.i("Robot initialized.");
	}

	public void autonomousInit() {
		// schedule the autonomous command (example)
		// autonomousCommand.start();
	}

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public void disabledInit() {

	}

	public void disabledPeriodic() {

	}

	public void disabledContinous() {

	}

	public void teleopInit() {
		driveWithController.start();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();

	}
}
