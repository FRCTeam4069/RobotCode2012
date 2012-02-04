package frc.t4069.robots.commands;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.AnalogChannel;

public class DoDebug extends CommandBase {

	AnalogChannel sonar;
	ADXL345_I2C accelerometer;

	protected void initialize() {
		// TODO Auto-generated method stub
		// sonar = new AnalogChannel(1);
		// accelerometer = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G);

	}

	protected void execute() {
	}

	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	protected void end() {
		// TODO Auto-generated method stub

	}

	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
