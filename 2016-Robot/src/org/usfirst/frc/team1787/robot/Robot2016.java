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
	public Robot2016(RobotSettings settings)
	{
		this.settings = settings;
	}
	/** Resets Pickup Arm to Stored Position */
	PickupArm pickupArm;
	public RobotPickupArm(PickupArm pickupArm)
	{
		this.moveToRegion(0);
	}
}
