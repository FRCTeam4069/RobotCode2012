package frc.t4069.robots.subsystems;

import java.util.Date;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.t4069.robots.RobotMap;

/**
 * RobotDrive is obviously too complicated, amirite?
 */
public class DriveTrain extends Subsystem {
	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	private Jaguar m_leftJaguar;
	private Jaguar m_rightJaguar;

	private static double RC = 400; // low pass filter constant

	private double m_limit = 1.0;

	public static double getRC() {
		return RC;
	}

	public DriveTrain() {
		this(new Jaguar(RobotMap.LEFT_MOTOR), new Jaguar(RobotMap.RIGHT_MOTOR));
	}

	public DriveTrain(Jaguar leftJaguar, Jaguar rightJaguar) {
		super("DriveTrain");
		m_leftJaguar = leftJaguar;
		m_rightJaguar = rightJaguar;

	}

	public void limitSpeed(double limit) {
		m_limit = limit;
	}

	private static double leftOld = 0;
	private static long leftLastTime = 0;
	private static double rightOld = 0;
	private static long rightLastTime = 0;

	/**
	 * Sets an RC value. Temporary solution.
	 * 
	 * @param rc If -1, no change.
	 */
	public static void setRC(double rc) {
		if (rc != -1) RC = rc;
	}

	public void hardBreak() {
		leftOld = 0;
		rightOld = 0;
		leftLastTime = new Date().getTime();
		rightLastTime = leftLastTime;
		m_leftJaguar.set(0);
		m_rightJaguar.set(0);
	}

	public void tankDrive(double leftSpeed, double rightSpeed) {
		leftSpeed *= -m_limit;
		rightSpeed *= m_limit;
		leftSpeed = lp(leftSpeed, leftOld, leftLastTime);
		rightSpeed = lp(rightSpeed, rightOld, rightLastTime);

		leftOld = leftSpeed;
		rightOld = rightSpeed;
		leftLastTime = new Date().getTime();
		rightLastTime = leftLastTime;

		m_leftJaguar.set(leftSpeed);
		m_rightJaguar.set(rightSpeed);
	}

	public static double lp(double current, double old, double lastTime) {
		return lp(current, old, lastTime, RC);
	}

	public static double lp(double current, double old, double lastTime,
			double rc) {
		if (lastTime > 0) {
			double a = new Date().getTime() - lastTime;
			a /= (a + rc);
			old = a * current + (1 - a) * old;
		}
		return old;
	}

	public void arcadeDrive(double moveValue, double rotateValue) {
		double leftMotorSpeed;
		double rightMotorSpeed;

		if (moveValue >= 0.0)
			moveValue = (moveValue * moveValue);
		else
			moveValue = -(moveValue * moveValue);
		if (rotateValue >= 0.0)
			rotateValue = (rotateValue * rotateValue);
		else
			rotateValue = -(rotateValue * rotateValue);

		if (moveValue > 0.0) {
			if (rotateValue > 0.0) {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = Math.max(moveValue, rotateValue);
			} else {
				leftMotorSpeed = Math.max(moveValue, -rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			}
		} else if (rotateValue > 0.0) {
			leftMotorSpeed = -Math.max(-moveValue, rotateValue);
			rightMotorSpeed = moveValue + rotateValue;
		} else {
			leftMotorSpeed = moveValue - rotateValue;
			rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
		}
		tankDrive(leftMotorSpeed, rightMotorSpeed);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
