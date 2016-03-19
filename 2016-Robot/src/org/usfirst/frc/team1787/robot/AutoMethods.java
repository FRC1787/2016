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
	/** How fast the robot will move during auto as a percentage of max speed (ex. 0.8 = 80% max speed). */
	private static final double AUTO_MOVE_SPEED = 0.7;
	/** How fast the robot will turn during auto as a percentage of max speed (ex. 0.5 = 50% max speed). */
	private static final double AUTO_ROTATE_SPEED = 0.5;
	/** Tested value to turn while moving to move in a straight line */
	private static final double CURVE_CORRECTION_VALUE = 0.085;
	/** A counter variable that keeps track of the step being performed in runAuto() */
	private int mainStep = 1;
	/** A counter variable that keeps track of the step being performed in a given "conquer defense" method */
	private int conquerDefenseStep = 1;
	/** A counter variable that keeps track of the step being performed in the "move to goal" method */
	private int moveToGoalStep = 1;
	
	// Variables for spinning wheels
	/** Timer for timing how long the wheels spin */
	Timer pickupWheelsSpinTimer = new Timer();
	/** Time to spin pickupWheels to pick up a boulder */
	public static final int PICKUP_TIME = 7;
	/** Time to spin pickupWheels to eject a boulder */
	public static final int EJECT_TIME = 7;
	
	// Methods:
	
	
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
		if (mainStep == 1)
		{
			if (autoConquerDefense(defenseInStartingPosition))
				mainStep++;
		}
		else if (mainStep == 2 && tryToScore)
		{
			if (autoMoveToGoal(startingPosition))
				mainStep++;
		}
		else if (mainStep == 3 && tryToScore)
		{
			if (autoShootLowGoal())
				mainStep++;
		}
	}
	
	/**
	 * This method calls the "autoConquer" method associated with the given defense.
	 * @param defense The value that specifies which defense to conquer
	 */
	public boolean autoConquerDefense(int defense)
	{
		if (defense == LOW_BAR)
			return autoConquerLowBar();
		else if (defense == PORTCULLIS)
			return autoConquerPortcullis();
		else if (defense == CHEVAL_DE_FRISE)
			return autoConquerChevalDeFrise();
		else if (defense == RAMPARTS)
			return autoConquerRamparts();
		else if (defense == MOAT)
			return autoConquerMoat();
		else if (defense == DRAWBRIDGE)
			return autoConquerDrawbridge();
		else if (defense == SALLY_PORT)
			return autoConquerSallyPort();
		else if (defense == ROCK_WALL)
			return autoConquerRockWall();
		else if (defense == ROUGH_TERRAIN)
			return autoConquerRoughTerrain();
		else
		{
			System.out.println("Invalid Defense ID");
			return false;
		}
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
	public boolean autoConquerLowBar()
	{
		if (conquerDefenseStep == 1)
		{
			if (autoMoveArm(PickupArm.REG_PICKUP))
				conquerDefenseStep++;
			return false;
		}
		else if (conquerDefenseStep == 2)
		{
			if (autoDriveDistance(10, AUTO_MOVE_SPEED)) // F 12.7, F 3.5, T 35deg, F 13
			{
				conquerDefenseStep++;
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Portcullis.
	 * To work properly, the robot must be correctly aligned with the Portcullis.
	 */
	public boolean autoConquerPortcullis()
	{
		if (conquerDefenseStep == 1)
		{
			if (autoMoveWedge(Wedge.DEPLOY))
				conquerDefenseStep++;
			return false;
		}
		else if (conquerDefenseStep == 2)
		{
			if (autoDriveDistance(-6, AUTO_MOVE_SPEED))
				conquerDefenseStep++;
			return false;
		}
		else if (conquerDefenseStep == 3)
		{
			if (autoMoveWedge(Wedge.RETRACT))
				conquerDefenseStep++;
			return false;
		}
		else if (conquerDefenseStep == 4)
		{
			if (autoTurnWithEncoders(180))
			{
				conquerDefenseStep++;
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Cheval-De-Frise.
	 * To work properly, the robot must be correctly aligned with the Cheval-De-Frise.
	 */
	public boolean autoConquerChevalDeFrise()
	{
		if (conquerDefenseStep == 1)
		{
			if (autoDriveDistance(-5, AUTO_MOVE_SPEED))
				conquerDefenseStep++;
			return false;
		}
		else if (conquerDefenseStep == 2)
		{
			if (autoMoveWedge(Wedge.DEPLOY))
				conquerDefenseStep++;
			return false;
		}
		else if (conquerDefenseStep == 3)
		{
			if (autoDriveDistance(-4, AUTO_MOVE_SPEED))
				conquerDefenseStep++;
			return false;
		}
		else if (conquerDefenseStep == 4)
		{
			if (autoMoveWedge(Wedge.RETRACT))
				conquerDefenseStep++;
			return false;
		}
		else if (conquerDefenseStep == 5)
		{
			if (autoTurnWithEncoders(180))
			{
				conquerDefenseStep++;
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Ramparts.
	 * To work properly, the robot must be correctly aligned with the Ramparts.
	 */
	public boolean autoConquerRamparts()
	{
		return autoDriveDistance(16, AUTO_MOVE_SPEED);
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Moat.
	 * To work properly, the robot must be correctly aligned with the Moat.
	 */
	public boolean autoConquerMoat()
	{
		return autoDriveDistance(16, AUTO_MOVE_SPEED);
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Drawbridge.
	 * To work properly, the robot must be correctly aligned with the Drawbridge.
	 */
	public boolean autoConquerDrawbridge()
	{
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Sally Port.
	 * To work properly, the robot must be correctly aligned with the Sally Port.
	 */
	public boolean autoConquerSallyPort()
	{
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Rock Wall.
	 * To work properly, the robot must be correctly aligned with the Rock Wall.
	 */
	public boolean autoConquerRockWall()
	{
		return autoDriveDistance(16, AUTO_MOVE_SPEED);
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Rough Terrain.
	 * To work properly, the robot must be correctly aligned with the Rough Terrain.
	 */
	public boolean autoConquerRoughTerrain()
	{
		return autoDriveDistance(16, AUTO_MOVE_SPEED);
	}
	
	public boolean autoMoveToGoal(int startingPosition)
	{
		if (startingPosition == 1)
		{
			if (moveToGoalStep == 1)
			{
				if(autoTurnWithEncoders(10))
					moveToGoalStep++;
				return false;
			}
			else if (moveToGoalStep == 2)
			{
				if (autoDriveDistance(14.5, AUTO_MOVE_SPEED))
					moveToGoalStep++;
				return false;
			}
			else if (moveToGoalStep == 3)
			{
				if (autoTurnWithEncoders(20))
					moveToGoalStep++;
				return false;
			}
			else if (moveToGoalStep == 4)
			{
				if (autoDriveDistance(6, AUTO_MOVE_SPEED))
				{
					moveToGoalStep++;
					return true;
				}
				return false;
			}
		}
		else if (startingPosition == 2)
		{
			return false;
		}
		else if (startingPosition == 3)
		{
			return false;
		}
		else if (startingPosition == 4)
		{
			return false;
		}
		else if (startingPosition == 5)
		{
			return false;
		} // <- Putting an else here for the following statements doesn't work. Idk why tho.
		System.out.println("Invalid Starting Position");
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to shoot a boulder into the low goal.
	 * To work properly, the robot must be correctly aligned with the tower.
	 */
	public boolean autoShootLowGoal()
	{
		return autoSpinWheels(PickupArm.WHEELS_EJECT);
	}
	
	/**
	 * This method, when called periodically, makes the robot travel a given distance.
	 * @param distance How far the robot should travel. Use positive values to move forward, and negative values to move backward.
	 * @param counterToIncrementWhenComplete The step counter to increment when the operation is finished.
	 */
	public boolean autoDriveDistance(double distance, double absValSpeed)
	{
		if (distance > 0 && driveControl.bothEncodersReadLessThan(distance))
		{
			driveControl.arcadeDriveUsingValues(absValSpeed, CURVE_CORRECTION_VALUE);
			return false;
		}
		else if (distance < 0 && driveControl.bothEncodersReadGreaterThan(distance))
		{
			driveControl.arcadeDriveUsingValues(-absValSpeed, CURVE_CORRECTION_VALUE);
			return false;
		}
		else
		{
			driveControl.stop();
			driveControl.resetEncodersAndGyro();
			return true;
		}
	}
	
	/*
	/**
	 * This method, when called periodically, makes the robot turn a given amount of degrees in place.
	 * @param degrees How many degrees to turn. Use positive values to turn right, and negative values to turn left.
	 * @param counterToIncrementWhenComplete The step counter to increment when the operation is finished.
	 *
	public boolean autoTurnDegrees(double degrees)
	{
		if (degrees > 0 && driveControl.getGyroAngle() < degrees)
		{
			driveControl.arcadeDriveUsingValues(0, AUTO_ROTATE_SPEED);
			return false;
		}
		else if (degrees < 0 && driveControl.getGyroAngle() > degrees)
		{
			driveControl.arcadeDriveUsingValues(0, -AUTO_ROTATE_SPEED);
			return false;
		}
		else
		{
			driveControl.stop();
			driveControl.resetEncodersAndGyro();
			return true;
		}
	}*/
	
	/**
	 * This method, when called periodically, makes the robot turn a given amount of degrees in place. 
	 * Encoders are used to measure the turn.
	 * @param degrees How many degrees to turn. Use positive values to turn right, and negative values to turn left.
	 * @return If the robot has turned degrees
	 */
	public boolean autoTurnWithEncoders(double degrees)
	{	
		if (!driveControl.hasTurnedDegrees(degrees))
		{
			if (degrees > 0) // If turning right
				driveControl.arcadeDriveUsingValues(0, AUTO_ROTATE_SPEED);
			else if (degrees < 0) // If turning left
				driveControl.arcadeDriveUsingValues(0, -AUTO_ROTATE_SPEED);
			return false;
		}
		else
		{
			driveControl.stop();
			driveControl.resetEncodersAndGyro();
			return true;
		}
	}
	
	/**
	 * Automatically move the arm to a region
	 * @param region region to move the arm to; arm.[one of the regions]
	 * @param counter the counter to increment when this operation is complete
	 */
	public boolean autoMoveArm(int region)
	{
		arm.moveToRegion(region);		
		return (arm.getCurrentRegion() == region);
	}
	
	/**
	 * Automatically spin the pickup wheels at a desired speed for a set amount of time. 
	 * The amount of time they spin is determined by the direction they spin.
	 * @param speed How fast the pickup-wheels spin.
	 * A positive value will spin them forwards (to pick up the ball).
	 * A negative value will spin them backwards (to eject the ball).
	 * @param counter The step counter to increment when this operation is complete.
	 */
	public boolean autoSpinWheels(double speed)
	{
		pickupWheelsSpinTimer.start();
		
		arm.spinPickupWheels(speed);
		
		if (speed == PickupArm.WHEELS_EJECT && pickupWheelsSpinTimer.get() >= EJECT_TIME ||
			speed == PickupArm.WHEELS_PICKUP && pickupWheelsSpinTimer.get() >= PICKUP_TIME ||
			speed == PickupArm.WHEELS_STATIONARY)
		{
			arm.stopPickupWheels();
			pickupWheelsSpinTimer.reset();
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Automatically deploy or retract the wedge
	 * @param desiredDirection direction to move the wedge; wedge.DEPLOY or wedge.RETRACT
	 * @param counter the counter to increment when this operation is complete
	 */
	public boolean autoMoveWedge(int desiredDirection)
	{
		if ((desiredDirection == Wedge.DEPLOY) && (wedge.getDirection() == Wedge.STATIONARY))
		{
			wedge.deploy();
			return false;
		}
		else if ((desiredDirection == Wedge.RETRACT) && (wedge.getDirection() == Wedge.STATIONARY))
		{
			wedge.retract();
			return false;
		}
		else
		{
			wedge.checkIfWedgeMotorShouldStop();
			return (wedge.getDirection() == Wedge.STATIONARY);
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
	 * Resets all steps to 1.
	 */
	public void resetAutoStepCounts()
	{
		mainStep = 1;
		conquerDefenseStep = 1;
		moveToGoalStep = 1;
	}
	
	public void addOptionsToPositionChooser(SendableChooser positionChooser)
	{
		positionChooser.addObject("It doesn't matter cuz we're just not gonna do anything during auto trololololol", 0);
		positionChooser.addDefault("Position 1 (far left)", 1);
        positionChooser.addObject("Position 2 (second from the left)", 2);
        positionChooser.addObject("Position 3 (in the middle)", 3);
        positionChooser.addObject("Position 4 (second from the right)", 4);
        positionChooser.addObject("Position 5 (far right)", 5);
	}
	
	public void addOptionsToDefenseChooser(SendableChooser defenseChooser)
	{
		defenseChooser.addDefault("Low Bar", LOW_BAR);
        defenseChooser.addObject("Portcullis (Type A)", PORTCULLIS);
        defenseChooser.addObject("Cheval-De-Frise (Type A)", CHEVAL_DE_FRISE);
        defenseChooser.addObject("Ramparts (Type B)", RAMPARTS);
        defenseChooser.addObject("Moat (Type B)", MOAT);
        //defenseChooser.addObject("Drawbridge (Type C)", DRAWBRIDGE);
        //defenseChooser.addObject("Sally Port (Type C)", SALLY_PORT);
        defenseChooser.addObject("Rock Wall (Type D)", ROCK_WALL);
        defenseChooser.addObject("Rough Terrain (Type D)", ROUGH_TERRAIN);
	}
	
	public void addOptionsToScoreChooser(SendableChooser scoreChooser)
	{
		scoreChooser.addDefault("Don't score", false);
		scoreChooser.addObject ("Score", true);
	}
}