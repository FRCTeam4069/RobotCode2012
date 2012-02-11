package frc.t4069.robots.subsystems;

import java.util.Date;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.t4069.robots.RobotMap;
import frc.t4069.utils.math.DateRandom;

public class BallPickup extends Subsystem {

	public BallPickup() {
		super("BallPickup");
		m_pickupMotor = new Relay(1);
		m_armMotor = new Jaguar(RobotMap.ARM_MOTOR);
		ballSwitch = new DigitalInput(RobotMap.BALL_SWITCH_1);
		ballSwitch2 = new DigitalInput(RobotMap.BALL_SWITCH_2);
		ballSwitch3 = new DigitalInput(RobotMap.BALL_SWITCH_3);

	}

	public void initDefaultCommand() {

	}

	public void Lower() {
		currentTime = (int) new Date().getTime() / 1000;
		if (ballSwitch.get()) {
			secsFromSwitch1 = currentTime;
		} else secsFromSwitch1 += 1;

		if (ballSwitch2.get()) {
			secsFromSwitch2 = currentTime;
		} else secsFromSwitch2 += 1;

		if (ballSwitch3.get()) {
			secsFromSwitch3 = currentTime;
		} else secsFromSwitch3 += 1;

		if (ballSwitch.get() && currentTime - secsFromSwitch2 < 3
				|| ballSwitch.get() && currentTime - secsFromSwitch3 < 3) {
			m_armMotor.set(-0.4 * rand.nextDouble(0.4, 0.5));
			m_pickupMotor.setDirection(Relay.Direction.kReverse);

		} else if (secsFromSwitch2 < 3 && secsFromSwitch3 < 4) {
			m_armMotor.set(-0.6 * rand.nextDouble(0.1, 1.2));
			m_pickupMotor.setDirection(Relay.Direction.kReverse);

		} else {
			m_armMotor.set(1.0);
			m_pickupMotor.setDirection(Relay.Direction.kForward);
		}
		m_pickupMotor.set(Relay.Value.kOn);


	}

	public void Raise(double value) {
		m_armMotor.set(-value);
	}

	private DateRandom rand = new DateRandom();
	private int secsFromSwitch1, secsFromSwitch2, secsFromSwitch3, currentTime;
	private DigitalInput ballSwitch, ballSwitch2, ballSwitch3;
	private SpeedController m_armMotor;
	private Relay m_pickupMotor;


}
