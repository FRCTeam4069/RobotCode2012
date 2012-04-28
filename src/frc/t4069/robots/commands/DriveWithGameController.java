package frc.t4069.robots.commands;

import java.util.Date;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.t4069.utils.GameController;
import frc.t4069.utils.GameController.EventHandler;

public class DriveWithGameController extends CommandBase {

	private GameController m_gc;

	private Joystick m_joystickRight;
	private Joystick m_joystickLeft;
	private double m_speedlimit = 1;

	private final static int KEY = 1900;
	private final static int FENDER = 1500;

	public static String log = "";

	public DriveWithGameController() {

	}

	protected void initialize() {
		// TODO Auto-generated method stub
		m_gc = oi.getController();
		m_joystickLeft = oi.getLeftJoystick();
		m_joystickRight = oi.getRightJoystick();
	}

	private long m_shooterStart;

	protected void execute() {
		m_gc.tick();

		EventHandler delayHandler = new EventHandler() {
			public void buttonDown() {
				m_shooterStart = new Date().getTime();
				shooter.resetPID();
				shooter.enablePID();
			}
		};

		m_gc.addButtonHandler(GameController.BTN_START, new EventHandler() {
			public void buttonUp() {
				if (m_speedlimit == 1)
					m_speedlimit = 0.7;
				else
					m_speedlimit = 1;
			}
		});

		m_gc.addButtonHandler(GameController.BTN_A, delayHandler);
		m_gc.addButtonHandler(GameController.BTN_B, delayHandler);
		m_gc.addButtonHandler(GameController.BTN_Y, delayHandler);
		m_gc.addButtonHandler(GameController.BTN_X, delayHandler);

		SmartDashboard.putBoolean("RightButton3",
				m_joystickRight.getRawButton(3));

		processCamera(m_gc);
		processRoller();
		processDriveTrain(m_gc);
		processArm(m_gc);
		processConveyorShooter();
	}

	protected void processRoller() {
		if (m_joystickLeft.getRawButton(1))
			pickupArm.runRoller(0.6);
		else
			pickupArm.runRoller(0);
	}

	protected void processArm(GameController gc) {
		double speed = m_joystickLeft.getRawAxis(2);
		pickupArm.setArm(speed / (1.666666666));
	}

	protected void processDriveTrain(GameController gc) {
		if (gc.getButton(GameController.BTN_RB)
				|| gc.getButton(GameController.BTN_LB))
			drivetrain.hardBreak();
		else
			drivetrain.arcadeDrive(m_speedlimit * gc.getTrigger(), m_speedlimit
					* gc.getLeftStick().x);
	}

	protected void processCamera(GameController gc) {
		// TODO: Sensititivty..g
		double y = (m_joystickRight.getRawAxis(2) + 1) / 4.0; // 0 - 0.5
		double x = (m_joystickRight.getRawAxis(1) + 1) / 2.88; // 0 - 0.7
		cameraMount.setTilt(y);
		cameraMount.setPan(x);
	}

	private boolean m_lastBallStatus = false;

	protected void processConveyorShooter() {
		int shooterRPM = 0;
		double shooterSpeed = 0.0;
		if (m_gc.getButton(GameController.BTN_X)) {
			shooterRPM = FENDER;
		}
		else if (m_gc.getButton(GameController.BTN_Y))
			shooterRPM = (int) (DriverStation.getInstance().getAnalogIn(4) * 1000.0);
		else if (m_gc.getButton(GameController.BTN_A))
			shooterRPM = KEY;
		else if (m_gc.getButton(GameController.BTN_B))
			shooterSpeed = 1.0;
		else {
			shooterSpeed = 0.0;
			shooterRPM = 0;
		}

		System.out.println("RPM: " + shooterRPM);
		if (shooterRPM > 0) {
			shooter.setTargetSpeed(shooterRPM);
			shooter.shoot();
		} else if (shooterSpeed > 0.1) {
			shooter.set(-shooterSpeed);
		} else {
			shooter.set(0);
		}

		if (m_joystickLeft.getRawButton(6))
			conveyor.reverse();
		else if (m_joystickLeft.getRawButton(7))
			conveyor.forward();
		else if (shooterRPM > 0 && shooter.isShooterReady())
			conveyor.reverse();
		else if (shooterSpeed > 0.1
				&& new Date().getTime() - m_shooterStart > 3000) {
			boolean thisStatus = shooter.isBallThere();
			if (m_lastBallStatus && !thisStatus)
				m_shooterStart = new Date().getTime();
			m_lastBallStatus = thisStatus;
			conveyor.reverse();
		} else if (!shooter.isBallThere() && m_joystickLeft.getRawButton(3))
			conveyor.reverse();
		else if (m_joystickLeft.getRawButton(2)) {
			conveyor.forward();
		} else
			conveyor.stop();

	}

	protected void interrupted() {

	}

	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	protected void end() {
		// TODO Auto-generated method stub

	}

}
