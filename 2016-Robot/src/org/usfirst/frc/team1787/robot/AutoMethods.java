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
	
	// Boolean Values
	private static final int MOST_STEPS_IN_ANY_AUTO_METHOD_PLUS_1 = 11;
	private boolean steps[] = new boolean[MOST_STEPS_IN_ANY_AUTO_METHOD_PLUS_1];
	
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
	 * It is used for _____
	 */
	public void Auto1() // An example of how auto methods may be written if called periodically.
	{
		System.out.println("Running Auto1()");
		
		if (!steps[1]) // if step 1 is not complete, work on step one.
		{
			System.out.println("Running Auto1(): Step 1");
			if (driveControl.getLeftEncoder().getDistance() < 5 && driveControl.getRightEncoder().getDistance() < 5)
				driveControl.arcadeDriveUsingValues(0.5, 0);
			else
			{
				driveControl.resetEncoders();
				steps[1] = true;
			}
		}
		else if (!steps[2]) // if step 2 is not complete, and all steps before it are complete, work on step 2.
		{
			System.out.println("Running Auto2(): Step 2");
			if (driveControl.getLeftEncoder().getDistance() > -5 && driveControl.getRightEncoder().getDistance() > -5)
				driveControl.arcadeDriveUsingValues(-0.5, 0);
			else
				steps[2] = true;
		}
			
	}
	
	/**
	 * This is the second autonomous method.
	 * It is used for _____
	 */
	public void Auto2() // An example of how auto methods may be written if called once.
	{
		System.out.println("Running Auto2()");
		driveControl.moveDistanceAtSpeed(5, 0.5);
		driveControl.resetEncoders();
		driveControl.moveDistanceAtSpeed(-5, 0.5);
	}
	
	public void resetCompletedSteps()
	{
		for (boolean s : steps)
			s = false;
	}
}