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
import frc.t4069.utils.Logger;
import frc.t4069.utils.networking.CommLink;
import frc.t4069.utils.networking.Value;

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
		if (CommandBase.oi.getController().getButton(GameController.BTN_A))
			try {
				Value intent = CommLink.getCommand();
				int err = intent.getError();
				if (err == 0)
					try {
						int action = intent.getInt("action");
						switch (action) {
							case 1:
								JSONObject o = new JSONObject();
								o.put("data", "value1");
								CommLink.addKV("put", o);
							break;
							case 2:
								Logger.d(CommLink.getKV("put")
										.getString("data"));
							break;
							case 3:
								CommLink.deleteKV("put");
							break;
						}

					} catch (JSONException e) {

					}
				else
					Logger.w("Commlink get command failed with status " + err);

			} catch (IOException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
