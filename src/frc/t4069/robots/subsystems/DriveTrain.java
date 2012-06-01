package frc.t4069.robots.subsystems;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.SpeedController;
import frc.t4069.robots.RobotMap;
import frc.t4069.utils.math.LowPassFilter;

/**
 * RobotDrive is obviously too complicated, amirite? This class includes a low
 * pass filter.
 * 
 * Recommended setup is tank drive based robot, with 2 or 4 Jaguars (not
 * victors). If 4 jags are used, use a PWM splitter to split the PWM signal.
 * Jaguars should be on brake mode.
 * 
 * Controller (Logitech Game Controller) should be mapped to trigger to be
 * forward/reverse, and left joystick's x to be left and right. These values are
 * passed to arcadeDrive as the move and turn value respectively.
 * 
 * @author Shuhao
 */
public class DriveTrain {

	private SpeedController m_leftJaguar;
	private SpeedController m_rightJaguar;
	private LowPassFilter m_leftLP;
	private LowPassFilter m_rightLP;

	private double m_limit = 1.0;
	private double m_leftLimit = 1.0;
	private double m_rightLimit = 0.98; // Right side is less powerful than the
											// left.

	/**
	 * Initializes a new drive object with the RC value of 250.
	 */
	public DriveTrain() {
		this(250); // Good constant for this drive train
	}

	/**
	 * Initializes a new drive object with a custom RC value.
	 * 
	 * @param RC The RC value used for the drive train.
	 */
	public DriveTrain(double RC) {
		this(new Jaguar(RobotMap.LEFT_MOTOR), new Jaguar(RobotMap.RIGHT_MOTOR),
				RC);
	}

	/**
	 * Initializes a new drive train with all custom stuff. Recommend to use
	 * Jaguars as oppose to Victors. Victors didn't seem to like the Low Pass
	 * Filter as much...
	 * 
	 * If there's 4 motors, use a PWM splitter, if you need to control all 4
	 * separately, this class is not for you.
	 * 
	 * @param leftJaguar Left Jaguar/SpeedController object
	 * @param rightJaguar Right Jaguar/SpeedController object
	 * @param RC RC Constant for Low Pass Filter
	 */
	public DriveTrain(SpeedController leftJaguar, SpeedController rightJaguar,
			double RC) {
		m_leftJaguar = leftJaguar;
		m_rightJaguar = rightJaguar;
		m_leftLP = new LowPassFilter(RC);
		m_rightLP = new LowPassFilter(RC);
	}

	/**
	 * Sets a RC value. Used when tuning with the analog controls.
	 * 
	 * @param RC the RC value
	 */
	public void setRC(double RC) {
		m_leftLP.setRC(RC);
		m_rightLP.setRC(RC);
	}

	/**
	 * Gets the current RC value;
	 * 
	 * @return The current RC value set.
	 */
	public double getRC() {
		return m_leftLP.getRC();
	}

	/**
	 * Limit the max speed. Used for precision mode. You can also use this to
	 * reverse directions (negatives)
	 * 
	 * @param limit A double between -1 - 1, as a percentage
	 */
	public void limitSpeed(double limit) {
		m_limit = limit;
	}

	/**
	 * Limit the left side, used for if one side is more powerful. You can use
	 * this to reverse directions (negatives)
	 * 
	 * @param limit A double between -1 - 1, as a percentage
	 */
	public void limitLeft(double limit) {
		m_leftLimit = limit;
	}

	/**
	 * Limit the left side, used for if one side is more powerful. You can use
	 * this to reverse directions (negatives)
	 * 
	 * @param limit A double between -1 - 1, as a percentage
	 */
	public void limitRight(double limit) {
		m_rightLimit = limit;
	}

	/**
	 * Stops robot by setting the speed of the controller to 0 (remember that
	 * the Jag should be on brake mode)
	 */
	public void hardBreak() {
		m_leftJaguar.set(0);
		m_rightJaguar.set(0);
		m_leftLP.reset(); // TODO: This was not present during the competition.
							// Is it required?
		m_rightLP.reset();
	}

	/**
	 * Tank drive. Controls the left and right speed. Not used usually.
	 * 
	 * @param leftSpeed Left speed between -1 - 1
	 * @param rightSpeed Right speed between -1 - 1
	 */
	public void tankDrive(double leftSpeed, double rightSpeed) {
		leftSpeed *= m_leftLimit * -m_limit;
		rightSpeed *= m_rightLimit * m_limit;

		leftSpeed = m_leftLP.calculate(leftSpeed);
		rightSpeed = m_rightLP.calculate(rightSpeed);

		m_leftJaguar.set(leftSpeed);
		m_rightJaguar.set(rightSpeed);
	}

	/**
	 * Arcade drive. It calculates the left and right speed.
	 * 
	 * @param moveValue Value between -1 - 1
	 * @param rotateValue Value between -1 - 1
	 */
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
