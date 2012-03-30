package frc.t4069.robots.subsystems;

import edu.wpi.first.wpilibj.Victor;
import frc.t4069.robots.RobotMap;

public class Conveyor {
	private Victor m_victor;

	public Conveyor() {
		m_victor = new Victor(RobotMap.CONVEYER);
	}

	public void forward() { // It's flipped
		// m_victor.set(0.6);
	}

	public void reverse() {
		// m_victor.set(-0.6);
	}

	public void stop() {
		m_victor.set(0);
	}

}
