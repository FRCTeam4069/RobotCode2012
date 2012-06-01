package frc.t4069.robots;

import java.util.Date;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.t4069.robots.subsystems.BallPickupArm;
import frc.t4069.robots.subsystems.Conveyor;
import frc.t4069.robots.subsystems.DriveTrain;
import frc.t4069.robots.subsystems.Shooter;
import frc.t4069.utils.GameController;
import frc.t4069.utils.GameController.EventHandler;
import frc.t4069.utils.Logger;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class The2012Robot extends IterativeRobot {

	public static DriveTrain drivetrain;
	public static Conveyor conveyor;
	public static BallPickupArm pickupArm;
	public static Shooter shooter;
	public static GameController gc;
	public static Joystick joystick;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	public void robotInit() {
		// instantiate the command used for the autonomous period
		// Initialize all subsystems

		drivetrain = new DriveTrain();
		pickupArm = new BallPickupArm();
		shooter = new Shooter();
		conveyor = new Conveyor();
		gc = new GameController(new Joystick(RobotMap.GCPORT));
		joystick = new Joystick(RobotMap.JOYSTICKPORT);

		// For knowing whose code is on the robot.
		SmartDashboard.putString("Author", Version.author);
		SmartDashboard.putString("Version", Version.version);
		Logger.i("Robot initialized.");
	}

	public void autonomousInit() {
		m_autostarttime = new Date().getTime();
		shooter.enablePID();
	}

	private final static int MODE_SHOOT = 0;
	private final static int MODE_FEED = 1;
	private int MODE = MODE_SHOOT;

	private final static int NUMBER_OF_SHOTS = 2;

	private long m_autostarttime;
	int ballsShot = 0;
	int lastStatusSustained = 0;
	boolean lastStatus = false;
	long lastRecognized;

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		switch (MODE) {
			case MODE_SHOOT:
				shooter.setTargetSpeed(RobotMap.SHOOTER_KEY_RPM);
				if (ballsShot == NUMBER_OF_SHOTS) {
					if (new Date().getTime() - lastRecognized < 4000) {
						shooter.shoot();
						conveyor.reverse();
						SmartDashboard.putString("Autonomous",
								"Spin down in 2 seconds");
					} else {
						SmartDashboard.putString("Autonomous", "Ended");
						shooter.set(0);
						conveyor.stop();
					}

				} else if (ballsShot < NUMBER_OF_SHOTS) {
					SmartDashboard.putString("Autonomous", "In progress");
					boolean thisStatus = shooter.isBallThere();
					if (thisStatus) lastStatusSustained++;
					shooter.shoot();
					if (lastStatus && !thisStatus) {
						if (lastStatusSustained > 4) {
							ballsShot++;
							lastRecognized = new Date().getTime();
						}
						lastStatusSustained = 0;
					}
					if (shooter.isShooterReady(0.03))
						conveyor.reverse();
					else
						conveyor.stop();
					lastStatus = thisStatus;
				}

			break;

			case MODE_FEED:
				if (new Date().getTime() - m_autostarttime < 2000)
					pickupArm.setArm(0.4);
			break;

		}

		SmartDashboard.putInt("Balls Shot", ballsShot);
		SmartDashboard.putDouble("RPM", shooter.getRPM());
	}

	public void disabledInit() {
		// Everytime it's disabled.
		Logger.i("Disabled!");
		SmartDashboard.putString("Autonomous", "Ended");

		// Disables shooters
		shooter.setTargetSpeed(0);
		shooter.resetPID();
		shooter.resetShooterSustain();
	}

	public void disabledPeriodic() {

	}

	public void disabledContinuous() {

	}

	public void teleopInit() {
		SmartDashboard.putString("Autonomous", "Ended");
		shooter.enablePID();
	}

	private long m_shooterStart;
	private double m_speedlimit = 1.0;

	public void teleopPeriodic() {
		Scheduler.getInstance().run();

		gc.tick();

		EventHandler delayHandler = new EventHandler() {
			public void buttonDown() {
				m_shooterStart = new Date().getTime();
				shooter.resetPID();
				shooter.enablePID();
			}
		};

		gc.addButtonHandler(GameController.BTN_START, new EventHandler() {
			public void buttonUp() {
				if (m_speedlimit == 1)
					m_speedlimit = 0.7;
				else
					m_speedlimit = 1;
			}
		});

		gc.addButtonHandler(GameController.BTN_A, delayHandler);
		gc.addButtonHandler(GameController.BTN_B, delayHandler);
		gc.addButtonHandler(GameController.BTN_Y, delayHandler);
		gc.addButtonHandler(GameController.BTN_X, delayHandler);

		processDriveTrain();
		processArm();
		processConveyorShooter();

		SmartDashboard.putBoolean("Ball Ready To Shoot",
				shooter.isBallThere());
		SmartDashboard.putString("Shooter Status",
				shooter.isShooterReady() ? "Ready" : "Not Ready");
		SmartDashboard.putDouble("RPM", shooter.getRPM());
	}

	private void processArm() {
		double speed = joystick.getRawAxis(2);
		pickupArm.setArm(speed / (1.666666666));
	}

	private void processDriveTrain() {
		if (gc.getButton(GameController.BTN_RB)
				|| gc.getButton(GameController.BTN_LB))
			drivetrain.hardBreak();
		else
			drivetrain.arcadeDrive(m_speedlimit * gc.getTrigger(), m_speedlimit
					* gc.getLeftStick().x);
	}

	private boolean m_lastBallStatus = false;

	protected void processConveyorShooter() {
		int shooterRPM = 0;
		double shooterSpeed = 0.0;
		if (gc.getButton(GameController.BTN_X)) {
			shooterRPM = RobotMap.SHOOTER_FENDER_RPM;
		}
		else if (gc.getButton(GameController.BTN_Y))
			shooterRPM = (int) (DriverStation.getInstance().getAnalogIn(4) * 1000.0);
		else if (gc.getButton(GameController.BTN_A))
			shooterRPM = RobotMap.SHOOTER_KEY_RPM;
		else if (gc.getButton(GameController.BTN_B))
			shooterSpeed = 1.0;
		else {
			shooterSpeed = 0.0;
			shooterRPM = 0;
		}

		if (shooterRPM > 0) {
			shooter.setTargetSpeed(shooterRPM);
			shooter.shoot();
		} else if (shooterSpeed > 0.1) {
			shooter.set(-shooterSpeed);
		} else {
			shooter.set(0);
		}

		if (joystick.getRawButton(6))
			conveyor.reverse();
		else if (joystick.getRawButton(7))
			conveyor.forward();
		else if (shooterRPM > 0 && shooter.isShooterReady())
			conveyor.reverse();
		else if (shooterSpeed > 0.1
				&& new Date().getTime() - m_shooterStart > 3000) {
			boolean thisStatus = shooter.isBallThere();
			if (m_lastBallStatus && !thisStatus)
				m_shooterStart = new Date().getTime();
			m_lastBallStatus = thisStatus;
			conveyor.reverse();
		} else if (!shooter.isBallThere() && joystick.getRawButton(3))
			conveyor.reverse();
		else if (joystick.getRawButton(2)) {
			conveyor.forward();
		} else
			conveyor.stop();

	}
}
