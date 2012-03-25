package frc.t4069.robots.commands;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.sun.squawk.util.MathUtils;

import edu.wpi.first.wpilibj.Kinect;
import edu.wpi.first.wpilibj.Skeleton;
import edu.wpi.first.wpilibj.Skeleton.Joint;
import edu.wpi.first.wpilibj.command.Command;
import frc.t4069.utils.networking.CommLink;

public class DriveWithKinect extends Command {

	private Kinect m_kinect;

	protected void initialize() {
		// TODO Auto-generated method stub
		m_kinect = Kinect.getInstance();

	}

	protected void execute() {
		Skeleton skeleton = m_kinect.getSkeleton();
		Joint leftWrist = skeleton.GetWristLeft();
		Joint leftShoulder = skeleton.GetShoulderLeft();
		double dx = Math.abs(leftWrist.getX() - leftShoulder.getX());
		double dy = leftWrist.getY() - leftShoulder.getY();
		double angle = MathUtils.atan2(dy, dx);
		angle /= (Math.PI / 2);
		double dz = leftShoulder.getZ() - leftWrist.getZ();
		CommandBase.pickupArm.runRoller(angle);
		JSONObject jo = new JSONObject();
		try {
			jo.put("Motor Speed", "" + angle);
			jo.put("Left dz", "" + dz);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		CommLink.addKV("Kinect", jo);
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
