package frc.t4069.robots.subsystems;

import java.util.Date;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.t4069.robots.RobotMap;
import frc.t4069.utils.math.LowPassFilter;

public class Shooter {
	private DigitalInput m_sensor;
	private Victor m_shooterMotor;
	private AnalogChannel m_voltagesensor;
	private LowPassFilter m_lpf;
	private Encoder m_encoder;

	private boolean m_shootingInProgress = false;

	public Shooter() {
		m_shooterMotor = new Victor(RobotMap.SHOOTER_MOTOR);
		m_voltagesensor = new AnalogChannel(RobotMap.SHOOTER_VOLTAGE_DETECTOR);
		m_sensor = new DigitalInput(RobotMap.PHOTOELECTRIC_SENSOR);
		m_lpf = new LowPassFilter(30);
		m_encoder = new Encoder(RobotMap.ENCODER_A, RobotMap.ENCODER_B);
		m_encoder.start();
	}

	long lastTime = -1337;
	int lastValue = 0;

	public double getRPM() {
		if (lastTime == -1337) {
			lastTime = new Date().getTime();
			lastValue = 0;
			return 0;
		}
		long ct = new Date().getTime();
		double deltaTime = ct - lastTime;
		lastTime = ct;
		int thisValue = m_encoder.get();
		int deltaValue = thisValue - lastValue;
		lastValue = thisValue;
		if (lastValue > 2000000000) {
			m_encoder.reset();
			lastValue = 0;
		}
		double rev = deltaValue / 250.0;
		return rev / (deltaTime / 60000.0);
	}

	public double getVoltage() {
		return 2 * m_voltagesensor.getAverageVoltage();
	}

	public boolean isShooterReady() {
		return (getVoltage() > (DriverStation.getInstance().getBatteryVoltage() * 0.8));
	}

	private final static double TARGET_TOLERANCE = 0.01;

	public boolean isShooterReady(int targetRPM) {
		return (targetRPM * (1 - TARGET_TOLERANCE) >= getRPM());
	}

	public boolean isBallThere() {
		return !m_sensor.get();
	}

	public void set(double speed) {
		speed *= (0.5 / (6.5 / DriverStation.getInstance().getBatteryVoltage()));
		SmartDashboard.putDouble("Shooter Speed", speed);
		speed = m_lpf.calculate(speed);
		m_shooterMotor.set(speed);
	}

	public void shoot() {

	}

	public boolean isShooting() {
		boolean ballThere = isBallThere();
		if (m_shootingInProgress && ballThere)
			return true;
		else if (m_shootingInProgress && !ballThere)
			m_shootingInProgress = false;
		return false;
	}

	public boolean shootingInProgress() {
		return m_shootingInProgress;
	}

}
