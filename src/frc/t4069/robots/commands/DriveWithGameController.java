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
	private DriverStation m_ds;
	private double m_speedlimit = 1;

	private final static double LOW = 0.36;
	private final static double MEDIUM = 0.55;
	private final static double HIGH = 0.75;

	public static String log = "";

	public DriveWithGameController() {

	}

	protected void initialize() {
		// TODO Auto-generated method stub
		m_gc = oi.getController();
		m_joystickLeft = oi.getLeftJoystick();
		m_joystickRight = oi.getRightJoystick();
		m_ds = DriverStation.getInstance();
	}

	private long m_shooterStart;

	protected void execute() {
		double sensitivity = m_ds.getAnalogIn(2) / 5.0;
		m_gc.tick();

		EventHandler delayHandler = new EventHandler() {
			public void buttonDown() {
				m_shooterStart = new Date().getTime();
			}
		};

		m_gc.addButtonHandler(GameController.BTN_X, delayHandler);
		m_gc.addButtonHandler(GameController.BTN_Y, delayHandler);
		m_gc.addButtonHandler(GameController.BTN_A, delayHandler);
		m_gc.addButtonHandler(GameController.BTN_B, delayHandler);

		m_gc.addButtonHandler(GameController.BTN_START, new EventHandler() {
			public void buttonUp() {
				if (m_speedlimit == 1)
					m_speedlimit = 0.65;
				else
					m_speedlimit = 1;
			}
		});

		SmartDashboard.putBoolean("RightButton3",
				m_joystickRight.getRawButton(3));

		processCamera(m_gc);
		processRoller();
		processDriveTrain(m_gc, sensitivity);
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

	protected void processDriveTrain(GameController gc, double turnSensitivity) {
		if (gc.getButton(GameController.BTN_RB)
				|| gc.getButton(GameController.BTN_LB))
			drivetrain.hardBreak();
		else
			drivetrain.arcadeDrive(m_speedlimit * gc.getTrigger(), m_speedlimit
					* gc.getLeftStick().x * turnSensitivity);
	}

	protected void processCamera(GameController gc) {
		// TODO: Sensititivty..g
		double y = (m_joystickRight.getRawAxis(2) + 1) / 4.0; // 0 - 0.5
		double x = (m_joystickRight.getRawAxis(1) + 1) / 2.88; // 0 - 0.7
		cameraMount.setTilt(y);
		cameraMount.setPan(x);
	}

	protected void processConveyorShooter() {
		double shooterSpeed = 0;
		if (m_gc.getButton(GameController.BTN_X))
			shooterSpeed = LOW;
		else if (m_gc.getButton(GameController.BTN_A))
			shooterSpeed = MEDIUM;
		else if (m_gc.getButton(GameController.BTN_B))
			shooterSpeed = HIGH;
		else if (m_gc.getButton(GameController.BTN_Y))
			shooterSpeed = m_ds.getAnalogIn(3) / 5.0;

		shooter.set(-shooterSpeed);

		if (shooterSpeed > 0.1 && shooter.isShooterReady()
				&& new Date().getTime() - m_shooterStart > 3000)
			conveyor.reverse();
		else if (!shooter.isBallThere())
			conveyor.reverse();
		else
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
