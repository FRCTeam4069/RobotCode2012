package frc.t4069.robots.subsystems;

import edu.wpi.first.wpilibj.Victor;
import frc.t4069.robots.RobotMap;

public class Shooter {
	private Victor m_shooterMotor;

	public Shooter() {
		this(RobotMap.SHOOTER_MOTOR);
	}

	public Shooter(int motorchannel) {
		m_shooterMotor = new Victor(motorchannel);
	}

	public void set(double speed) {
		m_shooterMotor.set(speed);
	}

}
