package frc.t4069.robots.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.t4069.robots.RobotMap;
import frc.t4069.utils.Logger;

public class CameraMount extends Subsystem {

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
		Logger.d("Tilt: " + value);
	}

	public void setPan(double value) {
		m_pan.set(value);
		Logger.d("Pan: " + value);
	}

	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

}