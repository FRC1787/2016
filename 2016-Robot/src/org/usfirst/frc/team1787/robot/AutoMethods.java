package org.usfirst.frc.team1787.robot;

/**
 * This class is a collection of the various autonomous routines we may use.
 * This class allows us to keep all autonomous code in one place and simplifies the autonomousPeriodic() method in Robot.
 * @author David Miron
 * @author Simon Wieder
 */
public class AutoMethods
{
	// Robot Mechanisms
	/** The set of devices used for driving the robot */
	private DrivingDevices driveControl;
	/** The pickup arm on the robot */
	private PickupArm arm;
	/** The wedge on the robot */
	private Wedge wedge;
	
	private static final double AUTO_MOVEMENT_SPEED = 0.5;
	
	/**
	 * The AutoMethods constructor passes in objects representing the various subsystems of the robot we want to use in auto.
	 * @param d The set of devices used for driving the robot
	 * @param a The pickup arm on the robot
	 * @param w The wedge on the robot
	 */
	public AutoMethods(DrivingDevices d, PickupArm a, Wedge w)
	{
		driveControl = d;
		arm = a;
		wedge = w;
	}
	
	/**
	 * This is the first (and default) autonomous method.
	 * It is used to for going under the low bar
	 * Steps:
	 * 1: Move forwards 5 feet
	 * 2: Move backwards 5 feet
	 * 3: Move forwards 5 feet
	 */
	public void Auto1()
	{
		System.out.println("Running Auto1()");	
	}	
	
	/**
	 * This is the second autonomous method.
	 * It is used for _____
	 */
	public void Auto2()
	{
		System.out.println("Running Auto2()");
	}
}