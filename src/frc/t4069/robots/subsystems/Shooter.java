package frc.t4069.robots.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.t4069.robots.RobotMap;
import frc.t4069.utils.math.LowPassFilter;

public class Shooter {
	private DigitalInput m_sensor;
	private Victor m_shooterMotor;
	private AnalogChannel m_voltagesensor;
	private LowPassFilter m_lpf;

	private boolean m_shootingInProgress = false;

	public Shooter() {
		m_shooterMotor = new Victor(RobotMap.SHOOTER_MOTOR);
		m_voltagesensor = new AnalogChannel(RobotMap.SHOOTER_VOLTAGE_DETECTOR);
		m_sensor = new DigitalInput(RobotMap.PHOTOELECTRIC_SENSOR);
		m_lpf = new LowPassFilter(30);
	}

	public double getVoltage() {
		return 2 * m_voltagesensor.getAverageVoltage();
	}

	public boolean isShooterReady() {
		return (getVoltage() > (DriverStation.getInstance().getBatteryVoltage() * 0.8));
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
		if (isBallThere()) m_shootingInProgress = true;
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
