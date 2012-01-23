package frc.t4069.robots;

import java.io.IOException;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.t4069.robots.commands.CommandBase;
import frc.t4069.robots.commands.DriveWithGameController;
import frc.t4069.utils.GameController;
import frc.t4069.utils.networking.CommLink;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class The2012Robot extends IterativeRobot {

	Command driveWithController;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	public void robotInit() {
		// instantiate the command used for the autonomous period
		driveWithController = new DriveWithGameController();

		// Initialize all subsystems
		CommandBase.init();

	}

	public void autonomousInit() {
		// schedule the autonomous command (example)
		// autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		driveWithController.start();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		if (CommandBase.oi.getController().getButton(GameController.BTN_A)) {
			JSONObject jo = new JSONObject();
			try {
				jo.put("test", "1");
				jo.put("test2", "2");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				CommLink.putData(jo);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
