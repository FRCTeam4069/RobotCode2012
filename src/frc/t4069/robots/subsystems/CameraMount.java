package frc.t4069.robots.subsystems;

import edu.wpi.first.wpilibj.Servo;
import frc.t4069.robots.RobotMap;

public class CameraMount {

	private Servo m_tilt;
	private Servo m_pan;

	public CameraMount() {
		this(new Servo(RobotMap.CAMERA_TILT_SERVO), new Servo(
				RobotMap.CAMERA_PAN_SERVO));
	}

	public CameraMount(Servo tilt, Servo pan) {
		m_tilt = tilt;
		m_pan = pan;
	}

	public void setTilt(double value) {
		m_tilt.set(value);
	}

	public void setPan(double value) {
		m_pan.set(value);
	}

}
