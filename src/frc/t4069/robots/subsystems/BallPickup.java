package frc.t4069.robots.subsystems;

import java.util.Date;
import java.util.Random;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.t4069.robots.RobotMap;

public class BallPickup extends Subsystem {

	public BallPickup() {
		super("BallPickup");
		m_pickupMotor = new Jaguar(3);
		m_armMotor = new Jaguar(4);
	}

	public void initDefaultCommand() {

	}

	public void Lower() {
		currentTime = (int) new Date().getTime();
		if (ballSwitch.get())
			secsFromSwitch1 = (int) new Date().getTime();
		else secsFromSwitch1++;

		if (ballSwitch2.get())
			secsFromSwitch2 = (int) new Date().getTime();
		else secsFromSwitch2++;

		if (ballSwitch3.get())
			secsFromSwitch3 = (int) new Date().getTime();
		else secsFromSwitch3++;

		if (currentTime - secsFromSwitch1 < 5
				&& currentTime - secsFromSwitch2 < 5
				&& currentTime - secsFromSwitch3 < 5) {

			if (ballSwitch.get() && currentTime - secsFromSwitch2 < 3
					|| ballSwitch.get() && currentTime - secsFromSwitch3 < 3) {
				m_pickupMotor.set(0.6);
				m_armMotor.set(-0.4 * rand.nextDouble());

			} else if (ballSwitch2.get() && ballSwitch3.get()) {
				m_armMotor.set(-0.6 * rand.nextDouble());
			}
		} else {
			m_armMotor.set(1.0);
			m_pickupMotor.set(1.0);
		}

	}

	private Random rand = new Random(new Date().getTime());
	private int secsFromSwitch1, secsFromSwitch2, secsFromSwitch3, currentTime;
	private DigitalInput ballSwitch = new DigitalInput(RobotMap.BALL_SWITCH_1);
	private DigitalInput ballSwitch2 = new DigitalInput(RobotMap.BALL_SWITCH_2);
	private DigitalInput ballSwitch3 = new DigitalInput(RobotMap.BALL_SWITCH_3);
	private SpeedController m_armMotor = new Jaguar(RobotMap.ARM_MOTOR);
	private SpeedController m_pickupMotor = new Jaguar(RobotMap.PICKUP_MOTOR);

}
