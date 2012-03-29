package frc.t4069.robots.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Victor;
import frc.t4069.robots.RobotMap;
import frc.t4069.utils.Logger;
import frc.t4069.utils.math.LowPassFilter;

public class BallPickupArm {

	private Victor m_armMotor;
	private Victor m_roller;
	private LowPassFilter m_rollerLPF = new LowPassFilter(RC);
	private DigitalInput m_limitswitch1;
	public static final double SPEED = 0.6;

	private static final int RC = 250;

	public BallPickupArm() {
		m_armMotor = new Victor(RobotMap.PICKUP_ARM_MOTOR);
		// m_roller = new Victor(RobotMap.PICKUP_ARM_ROLLER);
		m_limitswitch1 = new DigitalInput(RobotMap.ARM_LIMITSWITCH);
	}

	public void runRoller(double speed) {
		// speed = m_rollerLPF.calculate(speed);
		// m_roller.set(speed);
	}

	public void setArm(double speed) {
		if (speed < 0) {
			speed = m_limitswitch1.get() ? 0 : speed;
			if (speed == 0) Logger.i("Limit switched!");
		}
		m_armMotor.set(speed);
	}

}
