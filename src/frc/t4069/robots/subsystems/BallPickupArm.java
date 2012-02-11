package frc.t4069.robots.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Victor;
import frc.t4069.robots.RobotMap;
import frc.t4069.utils.math.LowPassFilter;

public class BallPickupArm {

	private Relay m_motor1;
	private Relay m_motor2;
	private Victor m_roller;
	private LowPassFilter m_rollerLPF = new LowPassFilter(RC);

	private static final int RC = 250;

	public BallPickupArm() {
		this(RobotMap.PICKUP_ARM_MOTOR_1, RobotMap.PICKUP_ARM_MOTOR_2);
	}

	public BallPickupArm(int motor1channel, int motor2channel) {
		m_motor1 = new Relay(motor1channel);
		m_motor2 = new Relay(motor2channel);
		m_roller = new Victor(5);
	}

	public void runRoller(double speed) {
		speed = m_rollerLPF.calculate(speed);
		m_roller.set(speed);
	}

	public void forward() {
		m_motor1.set(Relay.Value.kForward);
		m_motor2.set(Relay.Value.kForward);
	}

	public void reverse() {
		m_motor1.set(Relay.Value.kReverse);
		m_motor2.set(Relay.Value.kReverse);
	}

	public void stop() {
		m_motor1.set(Relay.Value.kOff);
		m_motor2.set(Relay.Value.kOff);
	}

}
