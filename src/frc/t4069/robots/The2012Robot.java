package frc.t4069.robots;

import java.util.Date;

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
		m_autostarttime = new Date().getTime();
		CommandBase.shooter.enablePID();
	}

	int ballsShot = 0;
	int lastStatusSustained = 0;
	boolean lastStatus = false;
	long lastRecognized;

	private final static int KEY = 1900;

	private long m_autostarttime;

	private final static int MODE_SHOOT = 0;
	private final static int MODE_FEED = 1;
	private int MODE = MODE_SHOOT;
	private final static int DELAY = 0;

	private final static int NUMBER_OF_SHOTS = 2;

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		switch (MODE) {
			case MODE_SHOOT:
				CommandBase.shooter.setTargetSpeed(KEY);
				if (ballsShot == NUMBER_OF_SHOTS) {
					if (new Date().getTime() - lastRecognized < 4000) {
						CommandBase.shooter.shoot();
						CommandBase.conveyor.reverse();
						SmartDashboard.putString("Autonomous",
								"Spin down in 2 seconds");
					} else {
						SmartDashboard.putString("Autonomous", "Ended");
						CommandBase.shooter.set(0);
						CommandBase.conveyor.stop();
					}

				} else if (ballsShot < NUMBER_OF_SHOTS) {
					SmartDashboard.putString("Autonomous", "In progress");
					boolean thisStatus = CommandBase.shooter.isBallThere();
					if (thisStatus) lastStatusSustained++;
					CommandBase.shooter.shoot();
					if (lastStatus && !thisStatus) {
						if (lastStatusSustained > 4) {
							ballsShot++;
							lastRecognized = new Date().getTime();
						}
						lastStatusSustained = 0;
					}
					if (CommandBase.shooter.isShooterReady(0.03))
						CommandBase.conveyor.reverse();
					else
						CommandBase.conveyor.stop();
					lastStatus = thisStatus;
				}

			break;

			case MODE_FEED:
				if (new Date().getTime() - m_autostarttime < 2000)
					CommandBase.pickupArm.setArm(0.4);
			break;

		}
		SmartDashboard.putDouble("RPM", CommandBase.shooter.getRPM());

		SmartDashboard.putInt("Balls Shot", ballsShot);
		SmartDashboard.putDouble("RPM", CommandBase.shooter.getRPM());
	}

	public void disabledInit() {
		Logger.i("Disabled!");
		SmartDashboard.putString("Autonomous", "Ended");
		CommandBase.shooter.setTargetSpeed(0);
		CommandBase.shooter.resetPID();
		CommandBase.shooter.resetShooterSustain();
	}

	public void disabledPeriodic() {

	}

	public void disabledContinuous() {

	}

	public void teleopInit() {
		SmartDashboard.putString("Autonomous", "Ended");
		driveWithController.start();
		CommandBase.shooter.enablePID();
		// CommandBase.shooter.setPD(DriverStation.getInstance().getAnalogIn(1),
		// DriverStation.getInstance().getAnalogIn(3),
		// DriverStation.getInstance().getAnalogIn(2));
	}

	/**
	 * This function is called periodically during operator control.
	 */

	public void teleopPeriodic() {
		Scheduler.getInstance().run();

		SmartDashboard.putBoolean("Ball Ready To Shoot",
				CommandBase.shooter.isBallThere());

		if (CommandBase.shooter.isShooterReady())
			SmartDashboard.putString("Shooter Status", "Ready");
		else
			SmartDashboard.putString("Shooter Status", "Not Ready");

		SmartDashboard.putDouble("RPM", CommandBase.shooter.getRPM());
	}
}
