package frc.t4069.robots.subsystems;

import java.util.Date;

import edu.wpi.first.wpilibj.AnalogChannel;
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
	private double lastPWMValue = 0.5;

	private final static int TICK_COUNT = 1440;
	private long lastTime = -1337;

	private final static double MAGIC = 1.9231e-04;
	private static double p = 5 * MAGIC;
	private static double i = 0.1 * MAGIC;
	private static double d = 3 * MAGIC;

	public static double[] ps = { 0, 0, 0 };
	private static double[] dp = { 1, 1, 1 }; // p, i, d

	class RPMEncoder implements PIDSource {
		private Encoder m_encoder;

		public RPMEncoder(Encoder encoder) {
			m_encoder = encoder;
		}

		public double pidGet() {
			if (lastTime == -1337) {
				lastTime = new Date().getTime();
				return 0;
			}
			long ct = new Date().getTime();
			double deltaTime = ct - lastTime;
			lastTime = ct;
			return m_encoder.get() / TICK_COUNT * deltaTime;
		}

	}

	class EncoderOutput implements PIDOutput {
		public double value = 0;

		public void pidWrite(double output) {
			output = value;
		}
	}

	public double getRPM() {
		return m_encoderpidsource.pidGet();
	}

	public Shooter() {
		m_shooterMotor = new Victor(RobotMap.SHOOTER_MOTOR);
		m_voltagesensor = new AnalogChannel(RobotMap.SHOOTER_VOLTAGE_DETECTOR);
		m_sensor = new DigitalInput(RobotMap.PHOTOELECTRIC_SENSOR);
		m_lpf = new LowPassFilter(50);
		m_encoder = new Encoder(RobotMap.ENCODER_A, RobotMap.ENCODER_B);

		m_encoderpidsource = new RPMEncoder(m_encoder);
		m_encoderoutput = new EncoderOutput();

		m_encoder.start();
		m_encoder.reset();
		m_pc = new PIDController(0, 0, 0, m_encoderpidsource, m_encoderoutput);
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

	public void setTargetSpeed(double rpm) {
		m_pc.setSetpoint(rpm);
		setrpm = rpm;
	}

	public double getRate() {
		return m_encoder.getRate();
	}

	public double getTargetSpeed() {
		return setrpm;
	}

	private double setrpm;

	private int itercount = 0;
	private final static int block = 300;
	private double avgErr = Double.POSITIVE_INFINITY;
	private double bestErr = Double.POSITIVE_INFINITY;
	private boolean collect = false;
	private int lastTwiddler = 0;
	private int mode = 0;
	private double[] errors = new double[block];
	private int lasti = 0;

	public boolean twiddle() {

		double sum = 0.0;
		for (int i = 0; i < 3; i++)
			sum += dp[i];

		if (collect) {
			errors[lasti] = getPIDOutput() - setrpm;
			lasti++;
		}

		if (itercount % (block * 2) == 0) {
			if (sum > 0.0001) {
				System.out.print("Twiddling! ");
				collect = false;
				double s = 0.0;
				for (int i = 0; i < lasti; i++)
					s += errors[i];
				avgErr = s / lasti;
				lasti = 0;
				if (avgErr < bestErr) {
					bestErr = avgErr;
					dp[lastTwiddler] *= 1.1;
				} else
					switch (mode) {
						case 0:
							ps[lastTwiddler] -= 2 * dp[lastTwiddler];
							mode = 1;
						break;
						case 1:
							dp[lastTwiddler] *= 0.9;
							ps[lastTwiddler] += dp[lastTwiddler];
							mode = 0;
						break;
					}

				lastTwiddler++;
				lastTwiddler %= 3;
				avgErr = 0;
				ps[lastTwiddler] += dp[lastTwiddler];
				m_pc.setPID(ps[0] * MAGIC, ps[1] * MAGIC, ps[2] * MAGIC);
				System.out.println("P: " + ps[0] + " I: " + ps[1] + "D: "
						+ ps[2]);
			} else {
				SmartDashboard.putDouble("p", ps[0]);
				SmartDashboard.putDouble("i", ps[1]);
				SmartDashboard.putDouble("d", ps[2]);
				return true;
			}

		} else if (itercount % block == 0) collect = true;
		itercount++;
		return false;
	}

	public double getVoltage() {
		return 2 * m_voltagesensor.getAverageVoltage();
	}

	public boolean isShooterReady() {
		return m_pc.onTarget();
	}

	public boolean isBallThere() {
		return !m_sensor.get();
	}

	private boolean problemflag = true;

	public void shoot() {
		if (!problemflag) {
			double speed = -(lastPWMValue + getPIDOutput());
			if (Math.abs(lastPWMValue - speed) > 0.4 || speed >= 0) {
				SmartDashboard.putString("SHOOTERPIDPROBLEM", "PIDout: "
						+ getPIDOutput() + " | lastPWM: " + lastPWMValue);
				problemflag = true;
			}
			lastPWMValue = -speed;
			SmartDashboard.putDouble("Speed Set", speed);
			SmartDashboard.putDouble("RPM", getRPM());
			twiddle();
		}
	}

	public void set(double speed) {
		speed = m_lpf.calculate(speed);
		m_shooterMotor.set(speed);

	}

}
