package frc.t4069.robots.subsystems;

import edu.wpi.first.wpilibj.Victor;
import frc.t4069.robots.RobotMap;
import frc.t4069.utils.math.LowPassFilter;

public class BallPickupArm {

	private Victor m_armMotor1;
	private Victor m_armMotor2;
	private Victor m_roller;
	private LowPassFilter m_rollerLPF = new LowPassFilter(RC);
	private LowPassFilter m_armLPF = new LowPassFilter(RC);

	private static final int RC = 250;

	public BallPickupArm() {
		this(RobotMap.PICKUP_ARM_MOTOR_1, RobotMap.PICKUP_ARM_MOTOR_2);
	}

	public BallPickupArm(int motor1channel, int motor2channel) {
		m_armMotor1 = new Victor(motor1channel);
		m_armMotor2 = new Victor(motor2channel);
		m_roller = new Victor(9);
	}

	public void runRoller(double speed) {
		speed = m_rollerLPF.calculate(speed);
		m_roller.set(speed);
	}

	public void forward() {
		forward(0.3);
	}

	public void forward(double speed) {
		m_armMotor1.set(speed);
		m_armMotor2.set(speed);
	}

	public void reverse() {
		reverse(-0.3);
	}

	public void reverse(double speed) {
		m_armMotor1.set(speed);
		m_armMotor2.set(speed);
	}

	public void stop() {
		m_armMotor1.set(0);
		m_armMotor2.set(0);
	}

}
