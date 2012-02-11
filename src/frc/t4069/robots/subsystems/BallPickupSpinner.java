package frc.t4069.robots.subsystems;

import java.util.Date;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.t4069.robots.RobotMap;

public class BallPickupSpinner extends Subsystem {

	private Relay m_motor1;
	public static final String FORWARD = "forward";
	public static final String BACKWARD = "backward";
	private DigitalInput sensor1, sensor2, sensor3;
	private long lastSensor1, lastSensor2, lastSensor3, currentTime;


	public BallPickupSpinner() {
		this(RobotMap.PICKUP_SPIN_MOTOR);
	}

	public BallPickupSpinner(int motorchannel) {
		m_motor1 = new Relay(motorchannel);
		sensor1 = new DigitalInput(RobotMap.PHOTO_CHANNEL_1);
		sensor2 = new DigitalInput(RobotMap.PHOTO_CHANNEL_2);
		sensor3 = new DigitalInput(RobotMap.PHOTO_CHANNEL_3);
		lastSensor1 = 60;
		lastSensor2 = 60;
		lastSensor3 = 60;
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

	public void spin(String direction) {
		currentTime = new Date().getTime() / 1000;
		if (sensor1.get()) {
			lastSensor1 = currentTime;
		} else lastSensor1++;
		if (sensor2.get()) {
			lastSensor2 = currentTime;
		} else lastSensor2++;
		if (sensor3.get()) {
			lastSensor3 = currentTime;
		} else lastSensor3++;
		switch (direction) {
			case FORWARD: {
				if (lastSensor2 < 2 && lastSensor3 < 2)
					m_motor1.set(Relay.Value.kReverse);
				else if ((lastSensor1 < 1 && lastSensor2 < 1)
						& (!sensor1.get())) {
					m_motor1.set(Relay.Value.kOff);
				} else {
					m_motor1.set(Relay.Value.kForward);
				}
			}
		}
	}

	public void stop() {
		m_motor1.set(Relay.Value.kOff);
	}
}
