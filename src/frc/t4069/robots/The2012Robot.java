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
		m_autostarttime = new Date().getTime();
	}

	int ballsShot = 0;
	int lastStatusSustained = 0;
	boolean lastStatus = false;
	long lastRecognized;
	private static double AUTOSPEED = 0.30;
	private static double CLOSEAUTOSPEED = 0.10;
	private final static int MODE_SHOOT = 0;
	private final static int MODE_FEED = 1;
	private final static int MODE_CLOSESHOOT = 2;
	private int MODE = MODE_SHOOT;
	private long m_autostarttime;

	public void autonomousPeriodic() {
		switch (MODE) {
			case MODE_SHOOT:
				long currentTime = new Date().getTime();
				if (ballsShot == 2) {
					if (currentTime - lastRecognized < 2000) {

						CommandBase.shooter.set(-AUTOSPEED);
						CommandBase.conveyor.reverse();
						SmartDashboard.putString("Autonomous",
								"Spin down in 2 seconds");
					} else {
						SmartDashboard.putString("Autonomous", "Ended");
						CommandBase.shooter.set(0);
					}
				} else if (ballsShot < 2) {
					SmartDashboard.putString("Autonomous", "In progress");
					boolean thisStatus = CommandBase.shooter.isBallThere();
					if (thisStatus) {
						lastStatusSustained++;
						if (currentTime - m_autostarttime > 3000)
							CommandBase.conveyor.reverse();

					} else if (currentTime - m_autostarttime > 3000)
						CommandBase.conveyor.reverse();

					CommandBase.shooter.set(-AUTOSPEED);

					if (lastStatus && !thisStatus) {
						if (lastStatusSustained > 4) ballsShot++;
						lastStatusSustained = 0;
						if (ballsShot == 2) {
							lastRecognized = new Date().getTime();
							m_autostarttime = lastRecognized;
						}
					}
					lastStatus = thisStatus;
				}
			break;
			case MODE_FEED:
				if (new Date().getTime() - m_autostarttime < 2000)
					CommandBase.pickupArm.setArm(0.5);
			break;
			case MODE_CLOSESHOOT:
				long time = new Date().getTime();
				CommandBase.shooter.set(-CLOSEAUTOSPEED);
				if (time - m_autostarttime < 3500)
					CommandBase.drivetrain.arcadeDrive(1.0, 0);
				if (time - m_autostarttime > 4000) {
					m_autostarttime = time;
					MODE = MODE_SHOOT;
					AUTOSPEED = CLOSEAUTOSPEED;
				}

			break;
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
				.getVoltage() * 100) / 12;
		SmartDashboard.putDouble("Shooter Voltage", shooterVoltage);
	}
}
