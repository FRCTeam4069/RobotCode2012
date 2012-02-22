package frc.t4069.robots.subsystems;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Victor;
import frc.t4069.robots.RobotMap;
import frc.t4069.utils.math.LowPassFilter;

/**
 * RobotDrive is obviously too complicated, amirite?
 */
public class DriveTrain {
	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	private Victor m_leftJaguar;
	private Jaguar m_rightJaguar;
	private LowPassFilter m_leftLP = new LowPassFilter(RC);
	private LowPassFilter m_rightLP = new LowPassFilter(RC);

	private static double RC = 250; // low pass filter constant

	private double m_limit = 1.0;

	public DriveTrain() {
		this(new Victor(RobotMap.LEFT_MOTOR), new Jaguar(RobotMap.RIGHT_MOTOR));
	}

	public DriveTrain(Victor leftJaguar, Jaguar rightJaguar) {
		m_leftJaguar = leftJaguar;
		m_rightJaguar = rightJaguar;
	}

	public void limitSpeed(double limit) {
		m_limit = limit;
	}

	public void hardBreak() {
		m_leftJaguar.set(0);
		m_rightJaguar.set(0);
	}

	public void tankDrive(double leftSpeed, double rightSpeed) {
		leftSpeed *= -m_limit;
		rightSpeed *= 0.971 * m_limit; // Rightside is more powerful than left.

		leftSpeed = m_leftLP.calculate(leftSpeed);
		rightSpeed = m_rightLP.calculate(rightSpeed);

		m_leftJaguar.set(leftSpeed);
		m_rightJaguar.set(rightSpeed);
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
}
