package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 * This class represents Team 1787's robot, Hoff 9000, for the 2016 FRC game: First Stronghold!
 * @author David Miron
 * @author Simon Wieder
 */
public class Robot extends IterativeRobot
{
	//NOTE: MAKE SURE PORT/ID NUMBERS DON'T REPEAT FOR LIKE OBJECTS, AND THAT PORT NUMBERS DON'T GO OUT OF BOUNDS
	
	// Objects and variables used for driving the robot:
	
	// DrivingDevices
	/** The set of devices on the robot which are used for, or related to, driving the robot. */
	private DrivingDevices driveControl;
	
	// Talons
	/** The ID of the talon that controls the front-left driving motor. */
	public static final int TALON_DRIVE_FL_ID = 3;
	/** The ID of the talon that controls the back-left driving motor. */
	public static final int TALON_DRIVE_BL_ID = 4;
	/** The ID of the talon that controls the front-right driving motor. */
	public static final int TALON_DRIVE_FR_ID = 1;
	/** The ID of the talon that controls the back-right driving motor. */
	public static final int TALON_DRIVE_BR_ID = 2;
	
	// Solenoid
	/** The port on the PCM where the solenoid that controls the shifter is connected. */
	public static final int SOL_GEAR_SHIFTING_PCM_PORT = 0;
	
	// Encoders
	/** The DIO port on the roboRio where the left encoder's A channel is connected. */
	public static final int LEFT_ENCODER_DIO_PORT_A = 6;
	/** The DIO port on the roboRio where the left encoder's B channel is connected. */
	public static final int LEFT_ENCODER_DIO_PORT_B = 7;
	/** The DIO port on the roboRio where the right encoder's A channel is connected. */
	public static final int RIGHT_ENCODER_DIO_PORT_A = 8;
	/** The DIO port on the roboRio where the right encoder's B channel is connected. */
	public static final int RIGHT_ENCODER_DIO_PORT_B = 9;
	
	// Gyro
	/** The Analog port on the roboRio where the gyro is connected. */
	public static final int GYRO_ANALOG_PORT = 0;
	
	// Objects and variables used for the PickupArm:
	
	// PickupArm
	/** The pickup arm on the robot. */
	private PickupArm arm;
	
	// Talons
	/** The ID of the talon that controls the pickup arm's left motor. */
	public static final int TALON_PICKUP_ARM_LEFT_ID = 8;
	/** The ID of the talon that controls the pickup arm's right motor. */
	public static final int TALON_PICKUP_ARM_RIGHT_ID = 7;
	/** The ID of the talon that controls the pickup-wheels' motor. */
	public static final int TALON_PICKUP_WHEELS_ID = 6;
	
	// Limit Switches
	/** The DIO port on the roboRio where the limit switch in the pickup arm's "Stored Region" is connected. */
	public static final int LS_PICKUP_ARM_STORED_DIO_PORT =  0;
	/** The DIO port on the roboRio where the limit switch in the pickup arm's "Pickup Region" is connected. */
	public static final int LS_PICKUP_ARM_PICKUP_DIO_PORT = 4;
	
	// Arm Motion
	/** The region that the pickup arm will move to. This value is set by buttons on the joystick during teleop. */
	private int pickupArmDesiredRegion = -1; // Starts at -1 so that the arm doesn't move upon entering teleop unless it's told to.
	
	// Objects and variables used for the Wedge:
	
	/** The wedge on the robot. */
	private Wedge wedge;
	/** The ID of the talon that controls the wedge motor. */
	public static final int TALON_WEDGE_ID = 5;
	
	// Objects and variables involving manual control of the robot:
	
	// Joysticks (Note: USB port #'s are configured from the driver station)
	/** The joystick used to intuitively control the robot when the pickup arm is in the front (default orientation). */
	private Joystick stickA;
	/** The USB port on the computer that stickA is connected to. */
	public static final int JOYSTICK_A_USB_PORT = 0;
	/** The joystick used to intuitively control the robot when the wedge is in the front. */
	private Joystick stickB;
	/** The USB port on the computer that stickB is connected to. */
	public static final int JOSTICK_B_USB_PORT = 1;
	
	// stickA & stickB Button Mapping
	/** The button on both joysticks that will put the robot in high gear. */
	public static final int JOYSTICK_HIGH_GEAR = 6;
	/** The button on both joysticks that will put the robot in low gear. */
	public static final int JOYSTICK_LOW_GEAR = 7;
	
	// stickA Button Mapping
	/** The button on stickA that will set pickupArmDesiredRegion to 0. */
	public static final int JOYSTICK_A_PICKUP_ARM_STORE = 2;
	/** The button on stickA that will set pickupArmDesiredRegion to 2. */
	public static final int JOYSTICK_A_PICKUP_ARM_APPROACH = 3;
	/** The button on stickA that will set pickupArmDesiredRegion to 4. */
	public static final int JOYSTICK_A_PICKUP_ARM_PICKUP = 1;
	/** The button on stickA that will spin the pickup wheels forward. */
	public static final int JOYSTICK_A_PICKUP_WHEELS_FORWARDS = 4;
	/** The button on stickA that will spin the pickup wheels backward. */
	public static final int JOYSTICK_A_PICKUP_WHEELS_BACKWARDS = 5;
	
	// stickB Button Mapping
	/** The button on stickB that will deploy the wedge. */
	public static final int JOYSTICK_B_WEDGE_DEPLOY = 3;
	/** The button on stickB that will retract the wedge. */
	public static final int JOYSTICK_B_WEDGE_RETRACT = 2;
	/** The button on stickB that will toggle the wedge. */
	public static final int JOYSTICK_B_WEDGE_TOGGLE = 1;
	/** The button on stickB that will toggle which camera feed is sent to the driver station. */
	public static final int JOYSTICK_B_CAMERA_FEED_TOGGLE = 9;
	/** The button on stickB that will toggle image processing on and off. */
	public static final int JOYSTICK_B_IMAGE_PROCESSING_TOGGLE = 8;
	/** The button on stickB that will toggle the type of image sent to the dashboard if image processing is active. Either binary or hybrid. */
	public static final int JOYSTICK_B_IMAGE_TYPE_TOGGLE = 10;
	/** The button on stickB that will toggle the settings of the side camera between regular settings, and those optimal for image processing. */
	public static final int JOYSTICK_B_CAMERA_SETTINGS_TOGGLE = 11;
	
	// Objects and variables involving the robot's autonomous functions:
	
	// AutoMethods
	/** The collection of methods used during auto. */
	private AutoMethods autoMethods;
	
	// Used to set the starting position of the robot
	/** The SendableChooser object that allows the starting position of the robot to be set from the smart dashboard. */
    SendableChooser autonomousPositionChooser;
    /** The position where the robot starts the match. Values range from 1 to 5, with 1 being the far left, and 5 being the far right. */
    private int startingPosition;
    
    // Used to set which defense the robot must conquer
    /** The SendableChooser object that allows the specific defense the robot must conquer in auto to be set from the smart dashboard. */
    SendableChooser autonomousDefenseChooser;
    /** The value indicating the specific defense the robot needs to conquer in auto. */
    private int defenseInStartingPosition;
    
    // Used to decide whether or not to attempt a shot in the low goal
    /** The SendableChooser object that allows someone to set whether or not to attempt a low goal shot from the smart dashboard. */
    SendableChooser scoreChooser;
    /** The value indicating whether or not the robot will attempt to score in the low goal during auto. */
    private boolean tryToScore;
    
    // Objects and variables involving the camera:
    
    /** The object which has methods to control the various camera functions / processes. */
    private VisionMethods visionMaster;
    /** The name of the front camera as it is set in the roborio web interface ("roboRIO-1787-FRC.local"). */
    private static final String CAMERA_FRONT_NAME = "cam1";
    /** The name of the other camera as it is set in the roborio web interface ("roboRIO-1787-FRC.local"). */
    private static final String CAMERA_SIDE_NAME = "cam2";
    /** A boolean indicating if methods involving vision processing should be/are being called. */
	private boolean imageProcessingActive = false;
	/** A boolean indicating if a binary image or a regular image should be drawn on and sent to the dashboard. */
	private boolean sendBinaryImage = true;
	/** The amount of pixels off center that is considered acceptable when locking on to a targed. */
	private final int ACCEPTABLE_NUM_OF_PIXELS_OFF_CENTER = 3;
	/** The PWM port on the roborio that the bottom servo is plugged in to. */
	private final int BOTTOM_SERVO_PWM_PORT = 0;
	/** The bottom servo on the camera mount. */
	private Servo bottomServo = new Servo(BOTTOM_SERVO_PWM_PORT);
	/** The PWM port on the roborio that the side servo is plugged in to. */
	private final int SIDE_SERVO_PWM_PORT = 4;
	/** The side servo on the camera mount. */
	private Servo sideServo = new Servo(SIDE_SERVO_PWM_PORT);
	/** The angle of the bottom servo. */
	private double bottomServoDesiredAngle = 95;
	/** The angle of the side servo. */
	private double sideServoDesiredAngle = 60;
	/** This is how many degrees off the bottom servo is from the angle it thinks it's at (ie setting the angle to 90 doesn't make the mount point at the front of the robot, but setting it to 95 does. It is 5 degrees off). This is only used when you want to know the actual angle of the servo. */
	private final double BOTTOM_SERVO_OFFSET = 5;
	/** This is how many degrees off the side servo is from the angle it thinks it's at (ie setting the angle to 90 doesn't make the mount level, but setting it to 85 does. It is -5 degrees off). This is only used when you want to know the actual angle of the servo. */
	private final double SIDE_SERVO_OFFSET = -5;
	/** The lowest angle the bottom servo should turn to. */
	private final double BOTTOM_SERVO_LOWER_LIMIT = 0;
	/** The greatest angle the bottom servo should turn to. */
	private final double BOTTOM_SERVO_UPPER_LIMIT = 180;
	/** The lowest angle the side servo should turn to. */
	private final double SIDE_SERVO_LOWER_LIMIT = 0;
	/** The greatest angle the side servo should turn to. */
	private final double SIDE_SERVO_UPPER_LIMIT = 130;
	/** A boolean telling whether the camera is locked on to the target in the x dimension (horizontally). */
	private boolean xLocked = false;
	/** A boolean telling whether the camera is locked on to the target in the y dimension (vertically). */
	private boolean yLocked = false;
    
    // Objects and variables used for general testing.
    
    /** Determines which functions will be available in test mode. Value can be set by buttons on the joystick. */
    private int testMode = 0;
    /** Timer for testing timer things. */
    private Timer testTimer = new Timer();
    /*
     * Timer Notes
     * 
     * testTimer.start()
     * 		Starts the timer relative to the current time.
     * 		Meaning it will always start at 0 when it is called.
     * 		Resets the timer if called while it is already running.
     * testTimier.stop()
     * 		Stops the timer from running and freezes the last value on it.
     * testTimer.reset()
     * 		resets the timer to 0.
     * 		If the timer is running, it will continue to run after being set to 0.
     * 		If the timer is stopped, it's value will be set to 0.
     */
    /** The preferences object that allows values to be grabbed from the smartdashboard. */
    Preferences prefs;
    
    private double testCounterX;
    private double testCounterY;
    
    // Objects and variables used to test specific things.
    
    // Using gyro to turn with PID control.
    private PIDOutputCalc PIDTester;
    private double desiredDegrees;
    
    // Gyro aided tracking
    boolean bothLocked = false;
    double lastGyroAngle = 0.0;
    double gyroDampener;
    
    // Miscellaneous objects and variables:
    
	/** Don't ask. */
	protected int farfar37;
	
	// Methods:
	
	
	
    /** 
     * This function is run once when the robot is first started up and is used for initialization code.
     */
    public void robotInit()
    {	
    	// Construct the DrivingDevices
    	driveControl = new DrivingDevices(TALON_DRIVE_BR_ID, TALON_DRIVE_BL_ID, TALON_DRIVE_FR_ID, TALON_DRIVE_FL_ID, 
    			SOL_GEAR_SHIFTING_PCM_PORT, LEFT_ENCODER_DIO_PORT_A, LEFT_ENCODER_DIO_PORT_B, 
    			RIGHT_ENCODER_DIO_PORT_A, RIGHT_ENCODER_DIO_PORT_B, GYRO_ANALOG_PORT);
    	
    	// Construct the PickupArm
    	arm = new PickupArm(TALON_PICKUP_ARM_LEFT_ID, TALON_PICKUP_ARM_RIGHT_ID, TALON_PICKUP_WHEELS_ID, 
    			LS_PICKUP_ARM_STORED_DIO_PORT, LS_PICKUP_ARM_PICKUP_DIO_PORT);
    	
    	// Construct the Wedge
    	wedge = new Wedge(TALON_WEDGE_ID);
    	
    	// Construct the Joysticks
    	stickA = new Joystick(JOYSTICK_A_USB_PORT);
    	stickB = new Joystick(JOSTICK_B_USB_PORT);
    	
    	// Construct the VisionMethods
    	visionMaster = new VisionMethods(CAMERA_FRONT_NAME, CAMERA_SIDE_NAME);
    	
    	// Construct the AutoMethods
    	autoMethods = new AutoMethods(driveControl, arm, wedge);
    	
    	// Construct the position chooser and add all options to it
    	autonomousPositionChooser = new SendableChooser();
        autoMethods.addOptionsToPositionChooser(autonomousPositionChooser);
        
        // Construct the defense chooser and add all options to it
        autonomousDefenseChooser = new SendableChooser();
        autoMethods.addOptionsToDefenseChooser(autonomousDefenseChooser); 
        
        // Construct the score chooser and all all options to it
        scoreChooser = new SendableChooser();
        autoMethods.addOptionsToScoreChooser(scoreChooser);
        
        // Put the choosers on the SmartDashboard
        SmartDashboard.putData("In which position will the robot start the match?", autonomousPositionChooser);
        SmartDashboard.putData("What defense is in that position?", autonomousDefenseChooser);
        SmartDashboard.putData("Try to score in the low goal during auto?", scoreChooser);
        
        // Construct preferences
        prefs = Preferences.getInstance();
    }
    
	/**
	 * This method is run once upon the robot entering autonomous mode, 
	 * and is used to determine which autonomous routine to perform 
	 * by retrieving the selected routine from the smart dashboard.
	 */
    public void autonomousInit()
    {
    	// Get the selected starting position from the smart dashboard
    	startingPosition =  (int) autonomousPositionChooser.getSelected();
    	
		// Get the selected defense to conquer from the smart dashboard
		defenseInStartingPosition = (int) autonomousDefenseChooser.getSelected();
		
		// Get whether or not to attempt a shot in the low goal from the smart dashboard
		tryToScore = (boolean) scoreChooser.getSelected();
		
		// Put the robot in low gear for better control while going over defenses
    	driveControl.setLowGear();
		
		// Make sure that all auto processes will execute step 1 when initially called
		autoMethods.resetStepCounts();
		
		// Reset the encoders and the gyro
		driveControl.resetEncodersAndGyro();
    }
    
    /** 
     * This function is called periodically while the robot is in autonomous mode.
     */
    public void autonomousPeriodic()
    {
    	if (startingPosition != 0) // A startingPosition of 0 indicates the driver chose "No Autonomous" from the smart dashboard
    		autoMethods.runAuto(startingPosition, defenseInStartingPosition, tryToScore);
    }
    
    /**
     * This method is run once when the robot enters teleop mode.
     */
    public void teleopInit()
    {
    	driveControl.setLowGear();
    	driveControl.resetEncodersAndGyro();
    	pickupArmDesiredRegion = -1; // Ensures the pickup arm only begins to move when we tell it to.
    	bottomServo.setAngle(bottomServoDesiredAngle); // Start with the camera looking up so it can find a goal.
    	sideServo.setAngle(sideServoDesiredAngle); // Start with the camera looking up so it can find a goal.
    	/*
    	visionMaster.setHSVThreshold(
    			prefs.getInt("HMin", 0), prefs.getInt("HMax", 360), 
    			prefs.getInt("SMin", 0), prefs.getInt("SMax", 255), 
    			prefs.getInt("VMin", 0), prefs.getInt("VMax", 255));
    	*/
    	//visionMaster.setControlLoopDampener(prefs.getDouble("vision dampener", 0.83));
    	xLocked = false;
    	yLocked = false;
    	SmartDashboard.putBoolean("X Locked", xLocked);
    	SmartDashboard.putBoolean("Y Locked", yLocked);
    	/*
    	gyroDampener = prefs.getDouble("gyroDampener", 0.5);
    	System.out.println("Gyro Dampener: "+gyroDampener);
    	bothLocked = false;
    	lastGyroAngle = 0.0; */
    }
    
    /**
     * This function is called periodically while the robot is in teleop mode.
     */
    public void teleopPeriodic()
    {
    	// Note: Try to keep this to simply method calls triggered by buttons. Doing so will maintain readability.
    	
    	// Driving
    	if (stickA.getMagnitude() > stickB.getMagnitude()) // Lets only one stick be used to drive at a time.
    		driveControl.arcadeDrivePickupArmInFront(stickA);
    	else
    		driveControl.arcadeDriveWedgeInFront(stickB); 
    	
    	// Shifting Gears
    	if (stickA.getRawButton(JOYSTICK_HIGH_GEAR) || stickB.getRawButton(JOYSTICK_HIGH_GEAR))
    		driveControl.setHighGear();
    	else if (stickA.getRawButton(JOYSTICK_LOW_GEAR) || stickB.getRawButton(JOYSTICK_LOW_GEAR))
    		driveControl.setLowGear();
    	
    	// Driving Data
    	driveControl.putDataOnSmartDashboard();
    	
    	// Functions specific to Joystick A
    	
    	// Pickup Arm
		if (stickA.getRawButton(JOYSTICK_A_PICKUP_ARM_STORE))
			pickupArmDesiredRegion = PickupArm.REG_STORE;
		else if (stickA.getRawButton(JOYSTICK_A_PICKUP_ARM_APPROACH))
			pickupArmDesiredRegion = PickupArm.REG_APPROACH;
		else if (stickA.getRawButton(JOYSTICK_A_PICKUP_ARM_PICKUP))
			pickupArmDesiredRegion = PickupArm.REG_PICKUP;
		
		if (pickupArmDesiredRegion != arm.getCurrentRegion() && pickupArmDesiredRegion != -1) // if the arm is moving, or hasn't moved yet, move the camera mount out of the way.
		{
			bottomServo.setAngle(95);
			sideServo.setAngle(0);
		}
		else // othewise, go back to where you were before
		{
			bottomServo.setAngle(bottomServoDesiredAngle);
			sideServo.setAngle(sideServoDesiredAngle);
		}
		
		if (pickupArmDesiredRegion >= 0) // Used to see if an arm region button has been pressed in teleop yet.
			arm.moveToRegion(pickupArmDesiredRegion);
    	
    	// Pickup Wheels
    	if (stickA.getRawButton(JOYSTICK_A_PICKUP_WHEELS_FORWARDS) || (arm.getCurrentRegion() == 4 && stickA.getRawButton(JOYSTICK_A_PICKUP_ARM_PICKUP)))
    		arm.spinPickupWheels(PickupArm.WHEELS_PICKUP); 
    	else if (stickA.getRawButton(JOYSTICK_A_PICKUP_WHEELS_BACKWARDS))
    		arm.spinPickupWheels(PickupArm.WHEELS_EJECT);
    	else
    		arm.stopPickupWheels();
    	    	
    	// Arm Data
    	// arm.putDataOnSmartDashboard(); // <- Used for testing arm stuff
    	
    	// Functions specific to Joystick B
    	
    	// Wedge
    	if (stickB.getRawButton(JOYSTICK_B_WEDGE_DEPLOY))
    		wedge.deploy();
    	else if (stickB.getRawButton(JOYSTICK_B_WEDGE_RETRACT))
    		wedge.retract();
    	else if (stickB.getRawButton(JOYSTICK_B_WEDGE_TOGGLE))
    		wedge.toggle();
    	wedge.checkWedgeTimer();
    	
    	// Cameras
    	if (stickB.getRawButton(JOYSTICK_B_CAMERA_FEED_TOGGLE)) // Toggles which camera feed is in use
    		visionMaster.toggleActiveCamFeed();
    	if (stickB.getRawButton(JOYSTICK_B_IMAGE_PROCESSING_TOGGLE))
    	{
    		imageProcessingActive = !imageProcessingActive;
    		xLocked = false;
    		yLocked = false;
    		SmartDashboard.putBoolean("X Locked", xLocked);
    		SmartDashboard.putBoolean("Y Locked", yLocked);
    		testTimer.delay(1); // Prevent imageProcessingActive from switching super quickly if the button is held down
    	}
    	if (stickB.getRawButton(JOYSTICK_B_IMAGE_TYPE_TOGGLE))
    		sendBinaryImage = ! sendBinaryImage;
    	if (stickB.getRawButton(JOYSTICK_B_CAMERA_SETTINGS_TOGGLE))
    	{
    		visionMaster.toggleCamSettings();
    		testTimer.delay(2); // Gives the camera time to update settings.
    	}
    	
    	if (imageProcessingActive && (pickupArmDesiredRegion == -1 || pickupArmDesiredRegion == arm.getCurrentRegion()))
    	{
    		/*
    		if (bothLocked)
    		{
    			double gyroAngleDifference = lastGyroAngle - driveControl.getGyro().getAngle();
    			testCounterX += (gyroAngleDifference * gyroDampener);
    			bottomServo.setAngle(testCounterX);
    			lastGyroAngle = driveControl.getGyro().getAngle();
    		} */
    		visionMaster.performHSVFilter();
    		visionMaster.removeSmallParticles();
    		if (visionMaster.getNumOfParticles() > 0) // if there is at least 1 particle, look for the biggest one.
    		{
    			visionMaster.findLargestParticle();
				if (visionMaster.performAspectRatioTest()) // if the biggest particle is a goal, track it.
				{
					// Horizontal
					if (visionMaster.getCenterOfMassX(visionMaster.getCurrentParticle()) < visionMaster.centerOfImage.x - ACCEPTABLE_NUM_OF_PIXELS_OFF_CENTER ||
						visionMaster.getCenterOfMassX(visionMaster.getCurrentParticle()) > visionMaster.centerOfImage.x + ACCEPTABLE_NUM_OF_PIXELS_OFF_CENTER) // if the goal is to the left or right of center, turn appropriately.
					{
						int errorInPixels = visionMaster.getCenterOfMassX(visionMaster.getCurrentParticle()) - visionMaster.centerOfImage.x;
						double errorInDegrees = visionMaster.getDampenedErrorInDegreesX(errorInPixels);
						bottomServoDesiredAngle += errorInDegrees;
						
						if (bottomServoDesiredAngle < BOTTOM_SERVO_LOWER_LIMIT)
							bottomServoDesiredAngle = BOTTOM_SERVO_LOWER_LIMIT;
						else if (bottomServoDesiredAngle > BOTTOM_SERVO_UPPER_LIMIT)
							bottomServoDesiredAngle = BOTTOM_SERVO_UPPER_LIMIT;
					}
					else
						xLocked = true;
						
					// Vertical
					if (visionMaster.getCenterOfMassY(visionMaster.getCurrentParticle()) < visionMaster.centerOfImage.y - ACCEPTABLE_NUM_OF_PIXELS_OFF_CENTER ||
						visionMaster.getCenterOfMassY(visionMaster.getCurrentParticle()) > visionMaster.centerOfImage.y + ACCEPTABLE_NUM_OF_PIXELS_OFF_CENTER) // if the goal is too high or too low, turn accordingly.
					{
						int errorInPixels = visionMaster.getCenterOfMassY(visionMaster.getCurrentParticle()) - visionMaster.centerOfImage.y;
						double errorInDegrees = visionMaster.getDampenedErrorInDegreesY(errorInPixels);
						sideServoDesiredAngle += errorInDegrees;
						
						if (sideServoDesiredAngle < SIDE_SERVO_LOWER_LIMIT)
							sideServoDesiredAngle = SIDE_SERVO_LOWER_LIMIT;
						else if (sideServoDesiredAngle > SIDE_SERVO_UPPER_LIMIT)
							sideServoDesiredAngle = SIDE_SERVO_UPPER_LIMIT;
					}
					else
						yLocked = true;
					
					SmartDashboard.putBoolean("X Locked", xLocked);
					SmartDashboard.putBoolean("Y Locked", yLocked);
					/*
					if (xLocked && yLocked)
					{
						if (!bothLocked)
							driveControl.resetEncodersAndGyro();
						bothLocked = true;
					} */
					xLocked = false;
					yLocked = false;
					
					bottomServo.setAngle(bottomServoDesiredAngle);
		    		sideServo.setAngle(sideServoDesiredAngle);
				}
    		}
    		if (sendBinaryImage)
    			visionMaster.sendProcessedImageToDashboard();
    		else
    			visionMaster.sendHybridImageToDashboard();
    	}
    	else if (!imageProcessingActive)
    		visionMaster.sendRegularImageToDashboard();	
    }
    
    /**
     * This function is run once when the robot enters test mode.
     */
    public void testInit()
    {
    	testMode = 0;
    	driveControl.resetEncoders();
    	driveControl.getGyro().calibrate();
    	
    	desiredDegrees = prefs.getDouble("Setpoint", 0);
    	PIDTester = new PIDOutputCalc(prefs.getDouble("P", 0), prefs.getDouble("I", 0), prefs.getDouble("D", 0));
    	System.out.println("P: "+prefs.getDouble("P", 0));
    	System.out.println("I: "+prefs.getDouble("I", 0));
    	System.out.println("D: "+prefs.getDouble("D", 0));
    	
    	desiredDegrees = prefs.getDouble("Setpoint", 0);
    	System.out.println("Setpoint: "+prefs.getDouble("Setpoint", 0));
    	
    	PIDTester.setIntegralThreshold(prefs.getDouble("Integral Threshold", 0));
    	System.out.println("Integral Threshold: "+prefs.getDouble("Integral Threshold", 0));
    	
    	PIDTester.setToleranceThreshold(prefs.getDouble("Tolerance Threshold", 0));
    	System.out.println("Tolerance Threshold: "+prefs.getDouble("Tolerance Threshold", 0));
    	
    	PIDTester.setMinOutput(-0.5);
    	PIDTester.setMaxOutput(0.5);
    	
    	System.out.println("Test Init Complete");
    }
    
    /**
     * This function is called periodically while the robot is in test mode.
     */
    public void testPeriodic()
    {
    	if (stickB.getRawButton(1))
    		testMode = 1;
    	else if (stickB.getRawButton(2))
    		testMode = 2;
    	else if (stickB.getRawButton(3))
    		testMode = 3;
    	else if (stickB.getRawButton(4))
    		testMode = 4;
    	else if (stickB.getRawButton(5))
    		testMode = 5;
    	else if (stickB.getRawButton(6))
    		testMode = 6;
    	else if (stickB.getRawButton(7))
    		testMode = 7;
    	else if (stickB.getRawButton(8))
    		testMode = 8;
    	
    	if (testMode == 1) // Manually control pickup arm
    	{
    		arm.manualControl(stickA);
    		
        	if (stickA.getRawButton(JOYSTICK_A_PICKUP_WHEELS_FORWARDS))
        		arm.spinPickupWheels(PickupArm.WHEELS_PICKUP);
        	else if (stickA.getRawButton(JOYSTICK_A_PICKUP_WHEELS_BACKWARDS))
        		arm.spinPickupWheels(PickupArm.WHEELS_EJECT);
        	else
        		arm.stopPickupWheels();
    	}
    	else if (testMode == 2) // Test PID turning.
    	{
    		PIDTester.putDataOnSmartDashboard();
        	SmartDashboard.putNumber("Gyro Angle (Test)", driveControl.getGyro().getAngle());
        	
    		PIDTester.calculateError(desiredDegrees, driveControl.getGyro().getAngle());
    		if (!PIDTester.errorIsAcceptable())
    		{
    			driveControl.arcadeDriveCustomValues(0, PIDTester.generateOutput());
    		}
    		else
    		{
    			driveControl.stop();
    			System.out.println("Sucessfully turned "+desiredDegrees+" degrees!");
    			testTimer.delay(1);
    			PIDTester.reset();
    			driveControl.resetEncodersAndGyro();
    			testTimer.delay(1);
    		}
    	}
    	else if (testMode == 3) // Manually control servos.
    	{
    		if (stickA.getX() > 0.2 && bottomServoDesiredAngle < BOTTOM_SERVO_UPPER_LIMIT) // look right
    			testCounterX += 1;
    		else if (stickA.getX() < -0.2 && bottomServoDesiredAngle > BOTTOM_SERVO_LOWER_LIMIT) // look left
    			testCounterX -= 1;
    		
    		if (-stickA.getY() > 0.2 && sideServoDesiredAngle < SIDE_SERVO_UPPER_LIMIT) // look down
    			testCounterY += 1;
    		else if (-stickA.getY() < -0.2 && sideServoDesiredAngle > SIDE_SERVO_LOWER_LIMIT) // look up
    			testCounterY -= 1;
    		
    		if (stickA.getRawButton(10)) // reset to neutral position
    		{
    			testCounterX = 90 + BOTTOM_SERVO_OFFSET;
    			testCounterY = 90 + SIDE_SERVO_OFFSET;
    		}
    		
    		System.out.println("X: "+testCounterX);
    		System.out.println("Y: "+testCounterY);
    		
    		bottomServo.setAngle(testCounterX);
    		sideServo.setAngle(testCounterY);
    	}
    	else if (testMode == 4) // Tank drive the robot, just for fun.
    	{
    		driveControl.theRobot.tankDrive(stickB, stickA);
    	}
    }   
}