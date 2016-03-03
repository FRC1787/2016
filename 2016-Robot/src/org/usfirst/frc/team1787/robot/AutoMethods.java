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
	
	// Values used for auto routines
	private static final double AUTO_MOVE_SPEED = 0.5;
	private static final double AUTO_ROTATE_SPEED = 0.5;
	private int currentStep = 1;
	
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
	 * It is used to drive in a square.
	 */
	public void Auto1()
	{
		System.out.println("Running Auto1()");
		
		if ( (currentStep % 2) == 1)
			autoDriveDistance(5);
		else if ( (currentStep % 2) == 0)
			autoTurnDegrees(90);		
	}	
	
	/**
	 * This is the second autonomous method.
	 * It is used for _____
	 */
	public void Auto2()
	{
		System.out.println("Running Auto2()");
	}
	
	public void autoDriveDistance(double distance)
	{
		if (distance > 0 && driveControl.bothEncodersReadLessThan(distance))
			driveControl.arcadeDriveUsingValues(AUTO_MOVE_SPEED, 0);
		else if (distance < 0 && driveControl.bothEncodersReadGreaterThan(distance))
			driveControl.arcadeDriveUsingValues(-AUTO_MOVE_SPEED, 0);
		else
		{
			driveControl.stop();
			driveControl.resetEncodersAndGyro();
			currentStep++;
		}
	}
	
	public void autoTurnDegrees(double degrees)
	{
		if (degrees > 0 && driveControl.getGyroAngle() < degrees)
			driveControl.arcadeDriveUsingValues(0, AUTO_ROTATE_SPEED);
		else if (degrees < 0 && driveControl.getGyroAngle() > degrees)
			driveControl.arcadeDriveUsingValues(0, -AUTO_ROTATE_SPEED);
		else
		{
			driveControl.stop();
			driveControl.resetEncodersAndGyro();
			currentStep++;
		}
	}
	
	public void resetCurrentStep()
	{
		currentStep = 1;
	}
}