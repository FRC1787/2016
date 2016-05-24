package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

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
	/** The button on stickB that will toggle the camera */
	public static final int JOYSTICK_B_CAMERA_TOGGLE = 9;
	
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
    
    /** The server through which the camera image is sent to the smart dashboard. */
    CameraServer cameraServer;
    /** The Image object that is sent to the camera server to be displayed. */
    private Image img;
    /** The camera object representing the front-mounted camera. */
    private USBCamera camFront;
    /** The name of the front camera as it is set in the roborio web interface ("roboRIO-1787-FRC.local"). */
    private static final String CAMERA_FRONT_NAME = "cam1";
    /** The camera object representing the side-mounted camera. */
    private USBCamera camSide;
    /** The name of the other camera as it is set in the roborio web interface ("roboRIO-1787-FRC.local"). */
    private static final String CAMERA_SIDE_NAME = "cam2";
    /** Boolean used for telling which camera is active. */
    private boolean frontCamActive = true;
    
    // Objects and variables used for testing functions in testPeriodic:
    
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
    
    Preferences prefs;
    private PIDOutputCalc PIDTester;
    private double desiredDegrees;
    
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
    	
    	// Set up the cameras
    	cameraServer = CameraServer.getInstance();
    	img = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
    	camFront = new USBCamera(CAMERA_FRONT_NAME);
    	camSide = new USBCamera(CAMERA_SIDE_NAME);
    	camFront.startCapture();
    	
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
    	
    	// Camera
    	if (frontCamActive)
    		camFront.getImage(img);
    	else
    		camSide.getImage(img);
		cameraServer.setImage(img);
		
    	if (stickB.getRawButton(JOYSTICK_B_CAMERA_TOGGLE)) // Toggles which camera feed is in use
    	{
    		if (frontCamActive)
    		{
    			camFront.stopCapture();
    			camSide.startCapture();
    		}
    		else
    		{
    			camSide.stopCapture();
    			camFront.startCapture();
    		}
    		frontCamActive = !frontCamActive;
    	}
    }
    
    /**
     * This function is run once when the robot enters test mode.
     */
    public void testInit()
    {
    	desiredDegrees = prefs.getDouble("Setpoint", 0);
    	PIDTester = new PIDOutputCalc(prefs.getDouble("P", 0), prefs.getDouble("I", 0), prefs.getDouble("D", 0));
    	PIDTester.setMaxOutput(0.5);
    	PIDTester.setMinOutput(-0.5);
    	PIDTester.setToleranceThreshold(prefs.getDouble("Tolerance Threshold", 0));
    	PIDTester.setIntegralThreshold(prefs.getDouble("Integral Threshold", 0));
    	testMode = 0;
    	driveControl.resetEncoders();
    	driveControl.getGyro().calibrate();
    	System.out.println("P: "+prefs.getDouble("P", 0));
    	System.out.println("I: "+prefs.getDouble("I", 0));
    	System.out.println("D: "+prefs.getDouble("D", 0));
    	System.out.println("Setpoint: "+prefs.getDouble("Setpoint", 0));
    	System.out.println("Tolerance Threshold: "+prefs.getDouble("Tolerance Threshold", 0));
    	System.out.println("Integral Threshold: "+prefs.getDouble("Integral Threshold", 0));
    	System.out.println("Test Init Complete");
    }
    
    /**
     * This function is called periodically while the robot is in test mode.
     */
    public void testPeriodic()
    {
    	PIDTester.putDataOnSmartDashboard();
    	if (stickA.getRawButton(JOYSTICK_A_PICKUP_WHEELS_BACKWARDS) && stickA.getRawButton(JOYSTICK_A_PICKUP_WHEELS_FORWARDS))
    		testMode = 1;
    	else if (stickA.getRawButton(JOYSTICK_A_PICKUP_ARM_APPROACH) && stickA.getRawButton(JOYSTICK_A_PICKUP_ARM_PICKUP))
    		testMode = 2;
    	else if (stickA.getRawButton(8))
    		testMode = 3;
    	else if (stickA.getRawButton(10))
    		testMode = 4;
    	else if (stickB.getRawButton(10))
    		testMode = 5;
    	
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
    	else if (testMode == 2) // Auto turn 360 degrees using gyro
    	{
    		System.out.println("Gyro angle: " + driveControl.getGyro().getAngle());
    		if (autoMethods.autoTurnDegrees(360, true))
    			testMode = 0;
    	}
    	else if (testMode == 3)
    	{
    		driveControl.arcadeDriveCustomValues(-stickA.getY(), -driveControl.getGyro().getAngle() * 0.03);
    		//driveControl.arcadeDrivePickupArmInFront(stickA);
    		System.out.println("Right Encoder Ticks: "+driveControl.getRightEncoder().get());
    		System.out.println("Left Encoder Ticks: "+driveControl.getLeftEncoder().get());
    		System.out.println("Right Encoder Distance: "+driveControl.getRightEncoder().getDistance());
    		System.out.println("Left Encoder Distance: "+driveControl.getLeftEncoder().getDistance());
    	}
    	else if (testMode == 4)
    	{
    		PIDTester.calculateError(desiredDegrees, driveControl.getGyro().getAngle());
    		if (!PIDTester.errorIsAcceptable())
    		{
    			driveControl.arcadeDriveCustomValues(0, PIDTester.generateOutput());
    		}
    		else
    		{
    			driveControl.stop();
    			System.out.println("Sucessfully turned "+desiredDegrees+" degrees!");
    			testTimer.delay(0.5);
    			PIDTester.reset();
    			driveControl.resetEncodersAndGyro();
    		}
    	}
    	else if (testMode == 5)
    	{
    		driveControl.arcadeDriveCustomValues(0, stickA.getX());
    		System.out.println(stickA.getX());
    	}
    }   
}