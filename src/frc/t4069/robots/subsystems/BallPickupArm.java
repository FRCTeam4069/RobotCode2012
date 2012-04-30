package frc.t4069.robots.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Victor;
import frc.t4069.robots.RobotMap;
import frc.t4069.utils.Logger;

public class BallPickupArm {

	private Victor m_armMotor;
	private DigitalInput m_limitswitch1;
	public static final double SPEED = 0.6;

	public BallPickupArm() {
		m_armMotor = new Victor(RobotMap.ARM_MOTOR);
		m_limitswitch1 = new DigitalInput(RobotMap.ARM_LIMITSWITCH);
	}

	public void setArm(double speed) {
		if (speed < 0) {
			speed = m_limitswitch1.get() ? 0 : speed;
			if (speed == 0) Logger.i("Limit switched!");
		}
		m_armMotor.set(speed);
	}

}
