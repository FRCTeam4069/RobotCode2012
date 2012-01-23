package frc.t4069.robots;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.t4069.robots.subsystems.DriveTrain;
import frc.t4069.utils.GameController;
import frc.t4069.utils.math.Point;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class The2012Robot extends IterativeRobot {

	Command autonomousCommand;
	GameController gc = new GameController(new Joystick(1));
	DriveTrain drive;
	Jaguar leftRearJaguar;
	Jaguar rightRearJaguar;
	Jaguar leftForwardJaguar;
	Jaguar rightForwardJaguar;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	public void robotInit() {
		// instantiate the command used for the autonomous period
		// autonomousCommand = new ExampleCommand();

		// Initialize all subsystems
		// CommandBase.init();
		leftRearJaguar = new Jaguar(RobotMap.LEFT_BACK_MOTOR);
		leftForwardJaguar = new Jaguar(RobotMap.LEFT_FORWARD_MOTOR);
		rightRearJaguar = new Jaguar(RobotMap.RIGHT_BACK_MOTOR);
		rightForwardJaguar = new Jaguar(RobotMap.RIGHT_FORWARD_MOTOR);
		drive = new DriveTrain(leftRearJaguar, rightRearJaguar,
				leftForwardJaguar, rightForwardJaguar);
	}

	public void autonomousInit() {
		// schedule the autonomous command (example)
		// autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		// autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		Point rightStick = gc.getRightStick();
		drive.arcadeDrive(rightStick.y, rightStick.x);
	}
}
