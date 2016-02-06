package org.usfirst.frc.team1787.robot;

/**
 * Class for all robot code. This class is instantiated
 * in Robot.java, and called from that file.
 * 
 * @author David Miron
 * @author Simon Wieder
 *
 */

public class Robot2016 {
	
	/** The settings for the robot */
	RobotSettings settings;
	/** The Pickup Arm */
	PickupArm pickupArm;
	
	public Robot2016(RobotSettings settings)
	{
		this.settings = settings;
	}
	/** Sets Pickup Arm to Stored Position */
	public void resetPickupArm(PickupArm pickupArm)
	{
		pickupArm.moveToRegion(0);
	}
	
}
