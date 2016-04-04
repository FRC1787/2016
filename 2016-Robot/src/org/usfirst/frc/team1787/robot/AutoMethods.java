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
	/** The set of devices used for driving the robot. */
	private DrivingDevices driveControl;
	/** The pickup arm on the robot. */
	private PickupArm arm;
	/** The wedge on the robot. */
	private Wedge wedge;
	
	// Defense Specification Values
	/** The value that specifies a given defense as the Low Bar. */
	public static final int LOW_BAR = 1;
	/** The value that specifies a given defense as the Portcullis. */
	public static final int PORTCULLIS = 2;
	/** The value that specifies a given defense as the Cheval-De-Frise. */
	public static final int CHEVAL_DE_FRISE = 3;
	/** The value that specifies a given defense as the Ramparts. */
	public static final int RAMPARTS = 4;
	/** The value that specifies a given defense as the Moat. */
	public static final int MOAT = 5;
	/** The value that specifies a given defense as the Drawbridge. */
	public static final int DRAWBRIDGE = 6;
	/** The value that specifies a given defense as the Sally Port. */
	public static final int SALLY_PORT = 7;
	/** The value that specifies a given defense as the Rock Wall. */
	public static final int ROCK_WALL = 8;
	/** The value that specifies a given defense as the Rough Terrain. */
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
	/** Timer for timing how long the wheels spin. */
	Timer pickupWheelsSpinTimer = new Timer();
	/** Time to spin pickupWheels to pick up a boulder. */
	public static final int PICKUP_TIME = 7;
	/** Time to spin pickupWheels to eject a boulder. */
	public static final int EJECT_TIME = 7;
	/** Timer for timing a sweep curve. */
	private Timer sweepCurveTimer = new Timer();
	
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
	 * This method calls the specific "autoConquer" method for the given defense.
	 * @param defense The value that specifies which defense to conquer.
	 * @return If the given defense has been conquered.
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
	 * front of the bumper lined up with the outside edge of the alignment line.
	 */
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Low Bar and end up at the alignment line.
	 * To work properly, the robot must be correctly aligned with the Low Bar.
	 * @return If the robot has finished conquering the Low Bar and has made it to the alignment line.
	 */
	public boolean autoConquerLowBar()
	{
		if (conquerDefenseStep == 1) // Move the arm to the pickup position so we can fit under the low bar with a boulder
		{
			if (autoMoveArm(PickupArm.REG_PICKUP))
				conquerDefenseStep++;
		}
		else if (conquerDefenseStep == 2) // Move under the low bar and get to the alignment line
		{
			if (autoDriveDistance(11.565, AUTO_MOVE_SPEED))
			{
				conquerDefenseStep++;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Portcullis and end up at the alignment line.
	 * To work properly, the robot must be correctly aligned with the Portcullis.
	 * @return If the robot has finished conquering the Portcullis and has made it to the alignment line.
	 */
	public boolean autoConquerPortcullis()
	{
		if (conquerDefenseStep == 1) // Deploy the wedge (starts with wedge facing portcullis)
		{
			if (autoMoveWedge(Wedge.DEPLOY))
				conquerDefenseStep++;
		}
		else if (conquerDefenseStep == 2) // Move under the portcullis
		{
			if (autoDriveDistance(-6, AUTO_MOVE_SPEED)) // A negative distance is used because the wedge is in the back, so we're technically driving backwards
				conquerDefenseStep++;
		}
		else if (conquerDefenseStep == 3)
		{
			if (autoMoveWedge(Wedge.RETRACT)) // Retract the wedge
				conquerDefenseStep++;
		}
		else if (conquerDefenseStep == 4)
		{
			if (autoTurnWithEncoders(180)) // Turn around so the pickup arm is in the front
			{
				conquerDefenseStep++;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Cheval-De-Frise and end up at the alignment line.
	 * To work properly, the robot must be correctly aligned with the Cheval-De-Frise.
	 * @return If the robot has finished conquering the Cheval-De-Frise and has made it to the alignment line.
	 */
	public boolean autoConquerChevalDeFrise()
	{
		if (conquerDefenseStep == 1) // Approach the cheval-de-frise (starts with wedge facing cheval-de-frise)
		{
			if (autoDriveDistance(-5, AUTO_MOVE_SPEED)) // A negative distance is used because the wedge is in the back, so we're technically driving backwards
				conquerDefenseStep++;
		}
		else if (conquerDefenseStep == 2) // Deploy the wedge to push one of the cheval-de-frise boards down
		{
			if (autoMoveWedge(Wedge.DEPLOY))
				conquerDefenseStep++;
		}
		else if (conquerDefenseStep == 3) // Drive over the cheval-de-frise
		{
			if (autoDriveDistance(-4, AUTO_MOVE_SPEED))
				conquerDefenseStep++;
		}
		else if (conquerDefenseStep == 4) // Retract the wedge
		{
			if (autoMoveWedge(Wedge.RETRACT))
				conquerDefenseStep++;
		}
		else if (conquerDefenseStep == 5) // Turn around so the pickup arm is in the front
		{
			if (autoTurnWithEncoders(180))
			{
				conquerDefenseStep++;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Ramparts and end up at the alignment line.
	 * To work properly, the robot must be correctly aligned with the Ramparts.
	 * @return If the robot has finished conquering the ramparts and has made it to the alignment line.
	 */
	public boolean autoConquerRamparts()
	{
		return autoDriveDistance(16, AUTO_MOVE_SPEED); // Move over the ramparts and get to the alignment line
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Moat and end up at the alignment line.
	 * To work properly, the robot must be correctly aligned with the Moat.
	 * @return If the robot has finished conquering the moat and has made it to the alignment line.
	 */
	public boolean autoConquerMoat()
	{
		return autoDriveDistance(16, AUTO_MOVE_SPEED); // Move over the moat and get to the alignment line
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Drawbridge and end up at the alignment line.
	 * To work properly, the robot must be correctly aligned with the Drawbridge.
	 * @return false. We can't conquer the drawbridge on our own in auto.
	 */
	public boolean autoConquerDrawbridge()
	{
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Sally Port and end up at the alignment line.
	 * To work properly, the robot must be correctly aligned with the Sally Port.
	 * @return false. We can't conquer the sally port on our own in auto.
	 */
	public boolean autoConquerSallyPort()
	{
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Rock Wall and end up at the alignment line.
	 * To work properly, the robot must be correctly aligned with the Rock Wall.
	 * @return If the robot has finished conquering the moat and has made it to the alignment line.
	 */
	public boolean autoConquerRockWall()
	{
		return autoDriveDistance(16, AUTO_MOVE_SPEED); // Move over the rock wall and get to the alignment line
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer the Rough Terrain and end up at the alignment line.
	 * To work properly, the robot must be correctly aligned with the Rough Terrain.
	 * @return If the robot has finished conquering the rough terrain and has made it to the alignment line.
	 */
	public boolean autoConquerRoughTerrain()
	{
		return autoDriveDistance(16, AUTO_MOVE_SPEED); // Move over the rough terrain and get to the alignment line
	}
	
	/**
	 * This method calls the specific "moveToGoal" method for the given starting position.
	 * @param startingPosition Where the robot started on the field. Possible values are 1-5 inclusive, 
	 * with 1 indicating the robot started at the left-most defense, and 5 indicating the robot started at the right-most defense.
	 * @return If the robot has finished moving to the low goal.
	 */
	public boolean autoMoveToGoal(int startingPosition)
	{
		if (startingPosition == 1)
			return moveToGoalPos1();
		else if (startingPosition == 2)
			return moveToGoalPos2();
		else if (startingPosition == 3)
			return moveToGoalPos3();
		else if (startingPosition == 4)
			return moveToGoalPos4();
		else if (startingPosition == 5)
			return moveToGoalPos5();
		else
		{
			System.out.println("Invalid Starting Position");
			return false;
		}
	}
	
	/**
	 * This method makes the robot perform a series of steps to get to the low goal 
	 * from the segment of the alignment line that is aligned with the left-most defense.
	 * @return If the robot has finished moving to the low goal.
	 */
	public boolean moveToGoalPos1()
	{
		if (moveToGoalStep == 1) // Turn right a bit so we can move away from the wall
		{
			if (autoTurnWithEncoders(10))
				moveToGoalStep++;
		}
		else if (moveToGoalStep == 2) // Move away from the wall
		{
			if (autoDriveDistance(12.25, AUTO_MOVE_SPEED))
				moveToGoalStep++;
		}
		else if (moveToGoalStep == 3) // Turn a little more to align with the ramp that leads up to the low goal
		{
			if (autoTurnWithEncoders(15))
				moveToGoalStep++;
		}
		else if (moveToGoalStep == 4) // Move up to the low goal
		{
			if (autoDriveDistance(2, AUTO_MOVE_SPEED))
			{
				moveToGoalStep++;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to get to the low goal 
	 * from the segment of the alignment line that is aligned with the defense that is 2nd from the left.
	 * @return false. This method hasn't been implemented yet.
	 */
	public boolean moveToGoalPos2()
	{
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to get to the low goal 
	 * from the segment of the alignment line that is aligned with the defense that is 3rd from the left.
	 * @return false. This method hasn't been implemented yet.
	 */
	public boolean moveToGoalPos3()
	{
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to get to the low goal 
	 * from the segment of the alignment line that is aligned with the defense that is 4th from the left.
	 * @return false. This method hasn't been implemented yet.
	 */
	public boolean moveToGoalPos4()
	{
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to get to the low goal 
	 * from the segment of the alignment line that is aligned with the defense that is 5th from the left.
	 * @return false. This method hasn't been implemented yet.
	 */
	public boolean moveToGoalPos5()
	{
		return false;
	}
	
	/**
	 * This method makes the robot perform a series of steps to shoot a boulder into the low goal.
	 * To work properly, the robot must be correctly aligned with the tower.
	 * @return If the robot has finished shooting a boulder into the low goal.
	 */
	public boolean autoShootLowGoal()
	{
		return autoSpinPickupWheels(PickupArm.WHEELS_EJECT);
	}
	
	/**
	 * This method, when called periodically, makes the robot travel a given distance at a given speed.
	 * @param distance How far, in feet, the robot should travel. Use positive values to move forward, and negative values to move backward.
	 * @param absValSpeed How fast the robot will drive as a percentage of its top speed (a value of 0.5 means 50% max speed).
	 * return If the robot has finished driving the given distance.
	 */
	public boolean autoDriveDistance(double distance, double absValSpeed)
	{
		if (!driveControl.hasDrivenDistance(distance))
		{
			if (distance > 0)
				driveControl.arcadeDriveCustomValues(absValSpeed, CURVE_CORRECTION_VALUE);
			else if (distance < 0)
				driveControl.arcadeDriveCustomValues(-absValSpeed, CURVE_CORRECTION_VALUE);
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
	 * This method, when called periodically, makes the robot turn a given amount of degrees in place.
	 * @param degrees How many degrees to turn. Use positive values to turn right, and negative values to turn left.
	 * return If the robot has finished turning the given amount of degrees.
	 */
	public boolean autoTurnDegrees(double degrees)
	{
		if (!driveControl.hasTurnedDegrees(degrees))
		{
			if (degrees > 0)
				driveControl.arcadeDriveCustomValues(0, AUTO_ROTATE_SPEED);
			else if (degrees < 0)
				driveControl.arcadeDriveCustomValues(0, -AUTO_ROTATE_SPEED);
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
	 * This method, when called periodically, makes the robot turn a given amount of degrees in place. 
	 * Encoders are used to measure the turn.
	 * @param degrees How many degrees to turn. Use positive values to turn right, and negative values to turn left.
	 * @return If the robot has finished turning the given amount of degrees.
	 */
	public boolean autoTurnWithEncoders(double degrees)
	{	
		if (!driveControl.hasTurnedDegreesWithEncoders(degrees))
		{
			if (degrees > 0) // If turning right
				driveControl.arcadeDriveCustomValues(0, AUTO_ROTATE_SPEED);
			else if (degrees < 0) // If turning left
				driveControl.arcadeDriveCustomValues(0, -AUTO_ROTATE_SPEED);
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
	 * This method, when called periodically, makes the robot move in a sweeping curve for a given amount of time.
	 * @param moveValue How fast the robot will move as a percentage of its max speed (a value of 0.5 means 50% max speed)
	 * @param curveValue How tightly the robot will curve.
	 * @param time How long, in seconds, the robot will move for.
	 * @return If the given time has passed, and the robot has therefore completed the sweep curve.
	 */
	public boolean autoTimedSweepCurve(double moveValue, double curveValue, double time)
	{
		if (sweepCurveTimer.get() == 0)
			sweepCurveTimer.start();
		
		if (sweepCurveTimer.get() < time)
			driveControl.arcadeDriveCustomValues(moveValue, curveValue);
		else
		{
			driveControl.stop();
			driveControl.resetEncodersAndGyro();
			sweepCurveTimer.stop();
			sweepCurveTimer.reset();
			return true;
		}
		return false;
	}
	
	/**
	 * This method, when called periodically, automatically moves the arm to a given region.
	 * @param region The region to move the arm to. It is recommended to use the constants in the PickupArm class for this value.
	 * @return If the arm has finished moving to the desired region.
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
	 */
	public boolean autoSpinPickupWheels(double speed)
	{
		if (pickupWheelsSpinTimer.get() == 0)
			pickupWheelsSpinTimer.start();
		
		arm.spinPickupWheels(speed);
		
		if (speed == PickupArm.WHEELS_EJECT && pickupWheelsSpinTimer.get() >= EJECT_TIME   ||
			speed == PickupArm.WHEELS_PICKUP && pickupWheelsSpinTimer.get() >= PICKUP_TIME ||
			speed == PickupArm.WHEELS_STATIONARY)
		{
			arm.stopPickupWheels();
			pickupWheelsSpinTimer.stop();
			pickupWheelsSpinTimer.reset();
			return true;
		}
		else if (speed != PickupArm.WHEELS_EJECT  || 
				 speed != PickupArm.WHEELS_PICKUP || 
				 speed != PickupArm.WHEELS_STATIONARY)
		{
			System.out.println("Invalid autoSpinPickupWheels speed. Please use PickupArm.WHEELS_EJECT, PickupArm.WHEELS_PICKUP, or PickupArm.WHEELS_STATIONARY");
		}
		return false;
	}
	
	/**
	 * Automatically deploy or retract the wedge.
	 * @param desiredDirection direction to move the wedge (Wedge.DEPLOY or Wedge.RETRACT).
	 * @return If the wedge has finished moving.
	 */
	public boolean autoMoveWedge(int desiredDirection)
	{
		if ((desiredDirection == Wedge.DEPLOY) && (wedge.getDirection() == Wedge.STATIONARY))
			wedge.deploy();
		else if ((desiredDirection == Wedge.RETRACT) && (wedge.getDirection() == Wedge.STATIONARY))
			wedge.retract();
		
		wedge.checkWedgeTimer();
		return (wedge.getDirection() == Wedge.STATIONARY);
	}
	
	/**
	 * Resets all steps to 1.
	 */
	public void resetStepCounts()
	{
		mainStep = 1;
		conquerDefenseStep = 1;
		moveToGoalStep = 1;
	}
	
	/**
	 * Adds all of the possible position options to a given sendable chooser.
	 * @param positionChooser Should only be the positionChooser.
	 */
	public void addOptionsToPositionChooser(SendableChooser positionChooser)
	{
		positionChooser.addObject("It doesn't matter cuz we're just not gonna do anything during auto trololololol", 0);
		positionChooser.addDefault("Position 1 (far left)", 1);
        positionChooser.addObject("Position 2 (second from the left)", 2);
        positionChooser.addObject("Position 3 (in the middle)", 3);
        positionChooser.addObject("Position 4 (second from the right)", 4);
        positionChooser.addObject("Position 5 (far right)", 5);
	}
	
	/**
	 * Adds all of the possible defense options to a given sendable chooser.
	 * @param defenseChooser Should only be the defenseChooser.
	 */
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
	
	/**
	 * Adds all of the possible scoring options to a given sendable chooser.
	 * @param scoreChooser Should only be the scoreChooser.
	 */
	public void addOptionsToScoreChooser(SendableChooser scoreChooser)
	{
		scoreChooser.addDefault("Don't score", false);
		scoreChooser.addObject ("Score", true);
	}
}