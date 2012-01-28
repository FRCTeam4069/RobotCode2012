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

	private Jaguar m_leftRearJaguar;
	private Jaguar m_rightRearJaguar;
	private Jaguar m_leftForwardJaguar;
	private Jaguar m_rightForwardJaguar;

	private static double RC = 400; // low pass filter constant

	private double m_limit = 1.0;

	public static double getRC() {
		return RC;
	}

	public DriveTrain() {
		this(new Jaguar(RobotMap.LEFT_BACK_MOTOR), new Jaguar(
				RobotMap.RIGHT_BACK_MOTOR), new Jaguar(
				RobotMap.LEFT_FORWARD_MOTOR), new Jaguar(
				RobotMap.RIGHT_FORWARD_MOTOR));
	}

	public DriveTrain(Jaguar leftRearJaguar, Jaguar rightRearJaguar,
			Jaguar leftForwardJaguar, Jaguar rightForwardJaguar) {
		super("DriveTrain");
		m_leftRearJaguar = leftRearJaguar;
		m_rightRearJaguar = rightRearJaguar;
		m_leftForwardJaguar = leftForwardJaguar;
		m_rightForwardJaguar = rightForwardJaguar;
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
		m_leftRearJaguar.set(0);
		m_leftForwardJaguar.set(0);
		m_rightRearJaguar.set(0);
		m_rightForwardJaguar.set(0);
	}

	public void tankDrive(double leftSpeed, double rightSpeed) {
		leftSpeed *= m_limit;
		rightSpeed *= m_limit;
		leftSpeed = lp(leftSpeed, leftOld, leftLastTime);
		rightSpeed = lp(rightSpeed, rightOld, rightLastTime);

		leftOld = leftSpeed;
		rightOld = rightSpeed;
		leftLastTime = new Date().getTime();
		rightLastTime = leftLastTime;

		m_leftRearJaguar.set(leftSpeed);
		m_leftForwardJaguar.set(leftSpeed);
		m_rightRearJaguar.set(rightSpeed);
		m_rightForwardJaguar.set(rightSpeed);
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
				rightMotorSpeed = moveValue - rotateValue;
				leftMotorSpeed = Math.max(moveValue, rotateValue);
			} else {
				rightMotorSpeed = Math.max(moveValue, -rotateValue);
				leftMotorSpeed = moveValue + rotateValue;
			}
		} else if (rotateValue > 0.0) {
			rightMotorSpeed = -Math.max(-moveValue, rotateValue);
			leftMotorSpeed = moveValue + rotateValue;
		} else {
			rightMotorSpeed = moveValue - rotateValue;
			leftMotorSpeed = -Math.max(-moveValue, -rotateValue);
		}
		tankDrive(leftMotorSpeed, -1 * rightMotorSpeed);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
