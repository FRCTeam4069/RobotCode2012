package frc.t4069.robots.subsystems;

import edu.wpi.first.wpilibj.Relay;
import frc.t4069.robots.RobotMap;

public class Conveyor {
	private Relay m_relay;

	public Conveyor() {
		m_relay = new Relay(RobotMap.CONVEYER);
	}

	public void forward() { // It's flipped
		m_relay.set(Relay.Value.kForward);
	}

	public void reverse() {
		m_relay.set(Relay.Value.kReverse);
	}

	public void stop() {
		m_relay.set(Relay.Value.kOff);
	}

}
