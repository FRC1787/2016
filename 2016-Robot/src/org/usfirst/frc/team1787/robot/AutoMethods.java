package org.usfirst.frc.team1787.robot;

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
	
	// Defense count variables
	/** Count variable for the low bar */
	private int count_lowbar = 1;
	/** Count variable for the portcullis */
	private int count_portcullis = 1;
	/** Count variable for the cheval de frise */
	private int count_chevaldefrise = 1;
	/** Count variable for the ramparts */
	private int count_ramparts = 1;
	/** Count variable for the moat */
	private int count_moat = 1;
	/** Count variable for the drawbridge */
	private int count_drawbridge = 1;
	/** Count variable for the sally port */
	private int count_sallyport = 1;
	/** Count variable for the rock wall */
	private int count_rockwall = 1;
	/** Count variable for the rough terrain */
	private int count_roughterrain = 1;
	
	// Values used for auto routines
	/** How fast the robot will move during auto as a percentage of max speed (ex. 0.5 = 50% max speed). */
	private static final double AUTO_MOVE_SPEED = 0.5;
	/** How fast the robot will turn during auto as a percentage of max speed (ex. 0.5 = 50% max speed). */
	private static final double AUTO_ROTATE_SPEED = 0.5;
	/** The current step being performed in an autonomous method */
	private int currentStep = 1;
	
	// Angles for each position to turn towards the tower
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
	
	/*public void runAuto(int startingPosition, int defenseInStartingPosition, boolean tryToScore)
	{
    	if (startingPosition == 1)
    		conquerDefenseInPosition1(defenseInStartingPosition, tryToScore);
    	else if (startingPosition == 2)
    		conquerDefenseInPosition2(defenseInStartingPosition, tryToScore);
    	else if (startingPosition == 3)
    		conquerDefenseInPosition3(defenseInStartingPosition, tryToScore);
    	else if (startingPosition == 4)
    		conquerDefenseInPosition4(defenseInStartingPosition, tryToScore);
    	else if (startingPosition == 5)
    		conquerDefenseInPosition5(defenseInStartingPosition, tryToScore);
    	else if (startingPosition == 6)
    		justMoveForwards();
	}*/
	
	//public void runAuto()
	
	/**
	 * This method makes the robot perform a series of steps to conquer a given defense in position 1, 
	 * approach the tower, and shoot a boulder into the low goal during autonomous.
	 * To work properly, the robot must be correctly aligned with the defense in position 1.
	 * @param defenseInPosition1 The value indicating the specific type of defense to conquer.
	 */
	public void conquerDefenseInPosition1(int defenseInPosition1, boolean tryToScore)
	{
		System.out.println("Automatically conquering the defense in position 1");
		if (currentStep == 1)
		{
			autoConquerDefense(defenseInPosition1);
		}
		else if (currentStep == 2)
		{
			
		}
	}	
	/*
	/**
	 * This method makes the robot perform a series of steps to conquer a given defense in position 2, 
	 * approach the tower, and shoot a boulder into the low goal during autonomous.
	 * To work properly, the robot must be correctly aligned with the defense in position 2.
	 * @param defenseInPosition2 The value indicating the specific type of defense to conquer.
	 */
	public void conquerDefenseInPosition2(int defenseInPosition2, boolean tryToScore)
	{
		System.out.println("Automatically conquering the defense in position 2");
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer a given defense in position 3, 
	 * approach the tower, and shoot a boulder into the low goal during autonomous.
	 * To work properly, the robot must be correctly aligned with the defense in position 3.
	 * @param defenseInPosition3 The value indicating the specific type of defense to conquer.
	 */
	public void conquerDefenseInPosition3(int defenseInPosition3, boolean tryToScore)
	{
		System.out.println("Automatically conquering the defense in position 3");
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer a given defense in position 4, 
	 * approach the tower, and shoot a boulder into the low goal during autonomous.
	 * To work properly, the robot must be correctly aligned with the defense in position 4.
	 * @param defenseInPosition4 The value indicating the specific type of defense to conquer.
	 */
	public void conquerDefenseInPosition4(int defenseInPosition4, boolean tryToScore)
	{
		System.out.println("Automatically conquering the defense in position 4");
	}
	
	/**
	 * This method makes the robot perform a series of steps to conquer a given defense in position 5, 
	 * approach the tower, and shoot a boulder into the low goal during autonomous.
	 * To work properly, the robot must be correctly aligned with the defense in position 5.
	 * @param defenseInPosition5 The value indicating the specific type of defense to conquer.
	 */
	public void conquerDefenseInPosition5(int defenseInPosition5, boolean tryToScore)
	{
		System.out.println("Automatically conquering the defense in position 5");
	}*/
	
	/**
	 * This method just makes the robot drive forwards
	 */
	public void justMoveForwards()
	{
		if (currentStep == 1)
			autoDriveDistance(12);
	}
	
	/**
	 * This method is called when the robot completes a step that is part of an autonomous routine.
	 * This method stops the robot, resets the encoders, resets the gyro, and advances the currentStep by 1.
	 */
	public void completeStep()
	{
		driveControl.stop();
		driveControl.resetEncodersAndGyro();
		currentStep++;
	}
	
	/**
	 * This method, when called periodically, makes the robot travel a given distance.
	 * @param distance How far the robot should travel. 
	 * Use positive values to move forward, and negative values to move backward.
	 */
	public void autoDriveDistance(double distance)
	{
		if (distance > 0 && driveControl.bothEncodersReadLessThan(distance))
			driveControl.arcadeDriveUsingValues(AUTO_MOVE_SPEED, 0);
		else if (distance < 0 && driveControl.bothEncodersReadGreaterThan(distance))
			driveControl.arcadeDriveUsingValues(-AUTO_MOVE_SPEED, 0);
		else
			completeStep();
	}
	
	/**
	 * This method, when called periodically, makes the robot turn a given amount of degrees in place.
	 * @param degrees How many degrees to turn. Use positive values to turn right, and negative values to turn left.
	 */
	public void autoTurnDegrees(double degrees)
	{
		if (degrees > 0 && driveControl.getGyroAngle() < degrees)
			driveControl.arcadeDriveUsingValues(0, AUTO_ROTATE_SPEED);
		else if (degrees < 0 && driveControl.getGyroAngle() > degrees)
			driveControl.arcadeDriveUsingValues(0, -AUTO_ROTATE_SPEED);
		else
			completeStep();
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
		autoDriveDistance(7);
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
	
	/**
	 * This method makes the robot perform a series of steps to shoot a boulder into the low goal.
	 * To work properly, the robot must be correctly aligned with the tower.
	 */
	public void autoShootLowGoal()
	{
		
	}
	
	/**
	 * Resets currentStep to 1.
	 */
	public void resetCurrentStep()
	{
		currentStep = 1;
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