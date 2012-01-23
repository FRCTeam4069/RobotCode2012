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

	private static double RC = 100; // low pass filter constant

	private double m_limit = 1.0;

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
		if (lastTime > 0) {
			double a = new Date().getTime() - lastTime;
			a /= (a + RC);
			old = a * current + (1 - a) * old;
		}
		return old;
	}

	public void arcadeDrive(double moveValue, double rotateValue) {
		double leftMotorSpeed;
		double rightMotorSpeed;
		if (moveValue >= 0.0) {
			moveValue = (moveValue * moveValue);
		} else {
			moveValue = -(moveValue * moveValue);
		}
		if (rotateValue >= 0.0) {
			rotateValue = (rotateValue * rotateValue);
		} else {
			rotateValue = -(rotateValue * rotateValue);
		}
		if (moveValue > 0.0) {
			if (rotateValue > 0.0) {
				rightMotorSpeed = moveValue + rotateValue; // May need to switch
															// the right and
															// left...
				leftMotorSpeed = Math.max(moveValue, rotateValue);
			} else {
				rightMotorSpeed = Math.max(moveValue, -rotateValue);
				leftMotorSpeed = moveValue + rotateValue;
			}
		} else {
			if (rotateValue > 0.0) {
				rightMotorSpeed = -Math.max(-moveValue, rotateValue);
				leftMotorSpeed = moveValue + rotateValue;
			} else {
				rightMotorSpeed = moveValue - rotateValue;
				leftMotorSpeed = -Math.max(-moveValue, -rotateValue);
			}
		}
		tankDrive(leftMotorSpeed, rightMotorSpeed);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
