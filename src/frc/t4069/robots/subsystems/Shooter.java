package frc.t4069.robots.subsystems;

import java.util.Date;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
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
	private PIDController m_pc;
	private RPMEncoder m_encoderpidsource;
	private EncoderOutput m_encoderoutput;

	// PID Magic Constants
	private final static double MAGIC = 0.1;
	private final static int MAX_SPEED = 5000;

	private static double p = 24.0 * MAGIC;
	private static double i = 1.0 * MAGIC;
	private static double d = 6.0 * MAGIC;

	class RPMEncoder implements PIDSource {
		private Encoder m_encoder;
		private long lastTime = -1337;
		private int lastValue = 0;
		private double currentValue = 0.0;

		private LowPassFilter m_lpf = new LowPassFilter(25);

		public RPMEncoder(Encoder encoder) {
			m_encoder = encoder;
		}

		public double pidGet() {
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
			rev = rev / (deltaTime / 60000.0) / MAX_SPEED;

			currentValue = m_lpf.calculate(rev);
			return currentValue;
		}
	}

	class EncoderOutput implements PIDOutput {
		public double value = 0;

		public void pidWrite(double output) {
			value = output;

		}
	}

	public double getRPM() {
		return m_encoderpidsource.currentValue * MAX_SPEED;
	}

	public Encoder getEncoder() {
		return m_encoder;
	}

	public Shooter() {
		m_shooterMotor = new Victor(RobotMap.SHOOTER_MOTOR);
		m_voltagesensor = new AnalogChannel(RobotMap.SHOOTER_VOLTAGE_DETECTOR);
		m_sensor = new DigitalInput(RobotMap.PHOTOELECTRIC_SENSOR);
		m_lpf = new LowPassFilter(50);
		m_encoder = new Encoder(RobotMap.ENCODER_A, RobotMap.ENCODER_B, false,
				CounterBase.EncodingType.k1X);

		m_encoderpidsource = new RPMEncoder(m_encoder);
		m_encoderoutput = new EncoderOutput();

		m_encoder.start();
		m_encoder.reset();
		m_pc = new PIDController(p, i, d, m_encoderpidsource, m_encoderoutput);
		m_pc.setOutputRange(-1, 1);
		m_pc.setTolerance(1.5);

	}

	public void resetEncoder() {
		m_encoder.reset();
	}

	public double getPIDOutput() {
		return m_encoderoutput.value;
	}

	public void enablePID() {
		m_pc.enable();
	}

	public void disablePID() {
		m_pc.disable();
	}

	public void resetPID() {
		m_pc.reset();
	}

	public void setTargetSpeed(double rpm) {
		m_pc.setSetpoint(rpm / MAX_SPEED);
		setrpm = rpm;
	}

	public double getRate() {
		return m_encoder.getRate();
	}

	public double getTargetSpeed() {
		return setrpm;
	}

	private double setrpm;

	public double getVoltage() {
		return 2 * m_voltagesensor.getAverageVoltage();
	}

	boolean lastShooterStatus = false;
	int shooterSustained = 0;
	int shooterNotSustained = 0;

	public boolean isShooterReady() {
		return isShooterReady(0.03);
	}

	public boolean isShooterReady(double tolerance) {
		boolean thisStatus = Math.abs(setrpm - m_encoderpidsource.currentValue
				* MAX_SPEED)
				/ setrpm < tolerance;
		if (thisStatus) {
			shooterSustained++;
			if (shooterSustained > 15) {
				shooterNotSustained = 0;
				return true;
			}
		}
		lastShooterStatus = thisStatus;
		if (!lastShooterStatus) {
			shooterNotSustained++;
			if (shooterNotSustained > 15) {
				shooterSustained = 0;
				return false;
			}
		}
		return false;
	}

	public void resetShooterSustain() {
		shooterSustained = 0;
		shooterNotSustained = 0;
	}

	public boolean isBallThere() {
		return !m_sensor.get();
	}

	public void shoot() {
		SmartDashboard.putDouble("Target RPM", m_pc.getSetpoint());
		// double speed = -(lastPWMValue + getPIDOutput());
		double speed = -getPIDOutput();
		m_shooterMotor.set(speed);

	}

	public void set(double speed) {
		speed = m_lpf.calculate(speed);
		m_shooterMotor.set(speed);
	}

}
