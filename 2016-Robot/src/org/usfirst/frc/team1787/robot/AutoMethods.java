package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

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
	
	// Defense Specification Values
	/** The value that specifies a given defense as the Low Bar */
	public static final int LOW_BAR = 1;
	/** The value that specifies a given defense as the Portcullis */
	public static final int PORTCULLIS = 2;
	/** The value that specifies a given defense as the Cheval-De-Frise */
	public static final int CHEVAL_DE_FRISE = 3;
	/** The value that specifies a given defense as the Rampart */
	public static final int RAMPARTS = 4;
	/** The value that specifies a given defense as the Moat */
	public static final int MOAT = 5;
	/** The value that specifies a given defense as the Drawbridge */
	public static final int DRAWBRIDGE = 6;
	/** The value that specifies a given defense as the Sally Port */
	public static final int SALLY_PORT = 7;
	/** The value that specifies a given defense as the Rock Wall */
	public static final int ROCK_WALL = 8;
	/** The value that specifies a given defense as the Rough Terrain */
	public static final int ROUGH_TERRAIN = 9;
	
	// Values used for auto routines
	/** How fast the robot will move during auto as a percentage of max speed (ex. 0.5 = 50% max speed). */
	private static final double AUTO_MOVE_SPEED = 0.5;
	/** How fast the robot will turn during auto as a percentage of max speed (ex. 0.5 = 50% max speed). */
	private static final double AUTO_ROTATE_SPEED = 0.5;
	/** A counter variable that keeps track of the step being performed in runAuto() */
	private int runAutoCurrentStep = 1;
	/** A counter variable that keeps track of the step being performed in a given "conquer defense" method */
	private int conquerDefenseCurrentStep = 1;
	/** A counter variable that keeps track of the step being performed in the "move to goal" method */
	private int moveToGoalCurrentStep = 1;
	
	// Angle to turn to face the tower for each position
	/** Angle to turn after going through the defense in position 1 */
	public static final int POSITION_1_ANGLE = 57;
	/** Angle to turn after going through the defense in position 2 */
	public static final int POSITION_2_ANGLE = 57;
	/** Angle to turn after going through the defense in position 3 */
	public static final int POSITION_3_ANGLE = 0;
	/** Angle to turn after going through the defense in position 4 */
	public static final int POSITION_4_ANGLE = -57;
	/** Angle to turn after going through the defense in position 5 */
	public static final int POSITION_5_ANGLE = -57 ;
	
	// Variables for spinning wheels
	/** Timer for timing how long the wheels spin */
	Timer pickupWheelsSpinTimer = new Timer();
	/** Time to spin pickupWheels to pick up a boulder */
	public static final int PICKUP_TIME = 5;
	/** Time to spin pickupWheels to eject a boulder */
	public static final int EJECT_TIME = 5;
	
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
	
	public void runAuto(int startingPosition, int defenseInStartingPosition, boolean tryToScore)
	{
		if (runAutoCurrentStep == 1)
			autoConquerDefense(defenseInStartingPosition);
		else if (runAutoCurrentStep == 2)
			autoMoveToGoal(startingPosition);
		else if (runAutoCurrentStep == 3 && tryToScore)
			autoShootLowGoal();
	}
	
	/**
	 * This method calls the "autoConquer" method associated with the given defense.
	 * @param defenseToConquer The value that specifies which defense to conquer
	 */
	public void autoConquerDefense(int defenseToConquer)
	{
		if (defenseToConquer == LOW_BAR)
			autoConquerLowBar();
		else if (defenseToConquer == PORTCULLIS)
			autoConquerPortcullis();
		else if (defenseToConquer == CHEVAL_DE_FRISE)
			autoConquerChevalDeFrise();
		else if (defenseToConquer == RAMPARTS)
			autoConquerRamparts();
		else if (defenseToConquer == MOAT)
			autoConquerMoat();
		else if (defenseToConquer == DRAWBRIDGE)
			autoConquerDrawbridge();
		else if (defenseToConquer == SALLY_PORT)
			autoConquerSallyPort();
		else if (defenseToConquer == ROCK_WALL)
			autoConquerRockWall();
		else if (defenseToConquer == ROUGH_TERRAIN)
			autoConquerRoughTerrain();
		else
			System.out.println("Invalid Defense ID");
	}
	
	/*
	 * Each of these autoConquer methods should get the
	 * back of the bumper lined up with the end of the
	 * ramp of the defense
	 */
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Low Bar.
	 * To work properly, the robot must be correctly aligned with the Low Bar.
	 */
	public void autoConquerLowBar()
	{
		if (conquerDefenseCurrentStep == 1)
			autoDriveDistance(7, conquerDefenseCurrentStep);
		else if (conquerDefenseCurrentStep == 2)
			completeStep(runAutoCurrentStep);
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Portcullis.
	 * To work properly, the robot must be correctly aligned with the Portcullis.
	 */
	public void autoConquerPortcullis()
	{
		
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Cheval-De-Frise.
	 * To work properly, the robot must be correctly aligned with the Cheval-De-Frise.
	 */
	public void autoConquerChevalDeFrise()
	{
		
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Ramparts.
	 * To work properly, the robot must be correctly aligned with the Ramparts.
	 */
	public void autoConquerRamparts()
	{
		
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Moat.
	 * To work properly, the robot must be correctly aligned with the Moat.
	 */
	public void autoConquerMoat()
	{
		
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Drawbridge.
	 * To work properly, the robot must be correctly aligned with the Drawbridge.
	 */
	public void autoConquerDrawbridge()
	{
		
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Sally Port.
	 * To work properly, the robot must be correctly aligned with the Sally Port.
	 */
	public void autoConquerSallyPort()
	{
		
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Rock Wall.
	 * To work properly, the robot must be correctly aligned with the Rock Wall.
	 */
	public void autoConquerRockWall()
	{
		
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Rough Terrain.
	 * To work properly, the robot must be correctly aligned with the Rough Terrain.
	 */
	public void autoConquerRoughTerrain()
	{
		
	}
	
	public void autoMoveToGoal(int startingPosition)
	{
		if (startingPosition == 1)
		{
			if (moveToGoalCurrentStep == 1)
				autoDriveDistance(0, moveToGoalCurrentStep);
			else if (moveToGoalCurrentStep == 2)
				autoTurnDegrees(POSITION_1_ANGLE, moveToGoalCurrentStep);
			else if (moveToGoalCurrentStep == 3)
				autoDriveDistance(0, moveToGoalCurrentStep);
		}
		else if (startingPosition == 2)
		{
			
		}
		else if (startingPosition == 3)
		{
			
		}
		else if (startingPosition == 4)
		{
			
		}
		else if (startingPosition == 5)
		{
			
		}
		else if (startingPosition == 6)
		{
			
		}
	}
	
	/**
	 * This method makes the robot perform a series of steps to shoot a boulder into the low goal.
	 * To work properly, the robot must be correctly aligned with the tower.
	 */
	public void autoShootLowGoal()
	{
		
	}
	
	/**
	 * This method, when called periodically, makes the robot travel a given distance.
	 * @param distance How far the robot should travel. Use positive values to move forward, and negative values to move backward.
	 * @param counterToIncrementWhenComplete The step counter to increment when the operation is finished.
	 */
	public void autoDriveDistance(double distance, int counter)
	{
		if (distance > 0 && driveControl.bothEncodersReadLessThan(distance))
			driveControl.arcadeDriveUsingValues(AUTO_MOVE_SPEED, 0);
		else if (distance < 0 && driveControl.bothEncodersReadGreaterThan(distance))
			driveControl.arcadeDriveUsingValues(-AUTO_MOVE_SPEED, 0);
		else
			completeStep(counter);
	}
	
	/**
	 * This method, when called periodically, makes the robot turn a given amount of degrees in place.
	 * @param degrees How many degrees to turn. Use positive values to turn right, and negative values to turn left.
	 * @param counterToIncrementWhenComplete The step counter to increment when the operation is finished.
	 */
	public void autoTurnDegrees(double degrees, int counter)
	{
		if (degrees > 0 && driveControl.getGyroAngle() < degrees)
			driveControl.arcadeDriveUsingValues(0, AUTO_ROTATE_SPEED);
		else if (degrees < 0 && driveControl.getGyroAngle() > degrees)
			driveControl.arcadeDriveUsingValues(0, -AUTO_ROTATE_SPEED);
		else
			completeStep(counter);
	}
	
	/**
	 * Automatically move the arm to a region
	 * @param region region to move the arm to; arm.[one of the regions]
	 * @param counter the counter to increment when this operation is complete
	 */
	public void autoMoveArm(int region, int counter)
	{
		if (arm.getCurrentRegion() != region)
			arm.moveToRegion(region);
		else
			completeStep(counter);
	}
	
	/**
	 * Automatically spin the pickup wheels in a direction
	 * @param direction the direction to spin the wheels; PickupArm.[one of the wheel directions]
	 * @param counter the counter to increment when this operation is complete.
	 */
	public void autoSpinWheels(int direction, int counter)
	{
		pickupWheelsSpinTimer.start();
		
		arm.spinPickupWheels(direction);
		
		if (direction == PickupArm.WHEELS_EJECT && pickupWheelsSpinTimer.get() >= EJECT_TIME ||
			direction == PickupArm.WHEELS_PICKUP && pickupWheelsSpinTimer.get() >= PICKUP_TIME ||
			direction == PickupArm.WHEELS_STATIONARY)
		{
			arm.stopPickupWheels();
			completeStep(counter);
		}
	}
	
	/**
	 * Automatically deploy or retract the wedge
	 * @param direction direction to move the wedge; wedge.DEPLOY or wedge.RETRACT
	 * @param counter the counter to increment when this operation is complete
	 */
	public void autoMoveWedge(int direction, int counter)
	{
		if ((direction == Wedge.DEPLOY) && (wedge.getDirection() == Wedge.STATIONARY))
			wedge.deploy();
		else if ((direction == Wedge.RETRACT) && (wedge.getDirection() == Wedge.STATIONARY))
			wedge.retract();
		else
		{
			wedge.checkIfWedgeMotorShouldStop();
			if (wedge.getDirection() == Wedge.STATIONARY)
				completeStep(counter);
		}
			
	}
	
	/**
	 * This method is called when the robot completes a step that is part of an autonomous routine.
	 * This method stops the robot, resets the encoders, resets the gyro, and increments the given step counter by 1.
	 * @param stepCounterToIncrement The step counter to increment.
	 */
	public void completeStep(int stepCounterToIncrement)
	{
		driveControl.stop();
		driveControl.resetEncodersAndGyro();
		stepCounterToIncrement++;
	}
	
	/**
	 * Resets currentStep to 1.
	 */
	public void resetAutoStepCounts()
	{
		runAutoCurrentStep = 1;
		conquerDefenseCurrentStep = 1;
		moveToGoalCurrentStep = 1;
	}
	
	public void addOptionsToPositionChooser(SendableChooser positionChooser)
	{
		positionChooser.addDefault("No Autonomous", 0);
		positionChooser.addObject("Position 1 (far left)", 1);
        //positionChooser.addObject("Position 2 (second from the left)", 2);
        //positionChooser.addObject("Position 3 (in the middle)", 3);
        //positionChooser.addObject("Position 4 (second from the right)", 4);
        //positionChooser.addObject("Position 5 (far right)", 5);
		//positionChooser.addObject("Just Move Forwards", 5);
	}
	
	public void addOptionsToDefenseChooser(SendableChooser defenseChooser)
	{
		defenseChooser.addDefault("Low Bar", LOW_BAR);
        //defenseChooser.addObject("Portcullis (Type A)", PORTCULLIS);
        //defenseChooser.addObject("Cheval-De-Frise (Type A)", CHEVAL_DE_FRISE);
        //defenseChooser.addObject("Ramparts (Type B)", RAMPARTS);
        //defenseChooser.addObject("Moat (Type B)", MOAT);
        //defenseChooser.addObject("Drawbridge (Type C)", DRAWBRIDGE);
        //defenseChooser.addObject("Sally Port (Type C)", SALLY_PORT);
        //defenseChooser.addObject("Rock Wall (Type D)", ROCK_WALL);
        //defenseChooser.addObject("Rough Terrain (Type D)", ROUGH_TERRAIN);
	}
	
	public void addOptionsToScoreChooser(SendableChooser scoreChooser)
	{
		scoreChooser.addDefault("Don't score", false);
		scoreChooser.addDefault("Score", true);
	}
}