package frc.t4069.robots.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.t4069.robots.OI;
import frc.t4069.robots.subsystems.BallPickupArm;
import frc.t4069.robots.subsystems.CameraMount;
import frc.t4069.robots.subsystems.Conveyor;
import frc.t4069.robots.subsystems.DriveTrain;
import frc.t4069.robots.subsystems.Shooter;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use
 * CommandBase.exampleSubsystem
 */
public abstract class CommandBase extends Command {

	public static OI oi;
	public static DriveTrain drivetrain;
	public static Conveyor conveyor;
	public static CameraMount cameraMount;
	public static BallPickupArm pickupArm;
	public static Shooter shooter;

	public static void init() {
		// This MUST be here. If the OI creates Commands (which it very likely
		// will), constructing it during the construction of CommandBase (from
		// which commands extend), subsystems are not guaranteed to be
		// yet. Thus, their requires() statements may grab null pointers. Bad
		// news. Don't move it.
		oi = new OI();
		drivetrain = new DriveTrain();
		cameraMount = new CameraMount();
		pickupArm = new BallPickupArm();
		shooter = new Shooter();
		conveyor = new Conveyor();
	}

	public CommandBase(String name) {
		super(name);
	}

	public CommandBase() {
		super();
	}
}
