package frc.t4069.robots.commands;

public class AutonomousShooter extends CommandBase {

	int ballsShot = 0;
	int lastStatusSustained = 0;
	boolean lastStatus = false;

	protected void initialize() {
		// TODO Auto-generated method stub

	}

	protected void execute() {
		shooter.set(1);
		conveyor.reverse();

	}

	protected boolean isFinished() {
		boolean thisStatus = shooter.isBallThere();
		if (thisStatus) lastStatusSustained++;
		if (lastStatus && !thisStatus) {
			if (lastStatusSustained > 4) ballsShot++;
			lastStatusSustained = 0;
		}
		lastStatus = thisStatus;
		return ballsShot == 2;
	}

	protected void end() {
		// TODO Auto-generated method stub

	}

	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
