package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class represents Team 1787's robot for the 2016 FRC: First Stronghold!
 * (add a bit more of a description here)
 * @author David Miron
 * @author Simon Wieder
 */
public class Robot extends IterativeRobot
{	
	//NOTE: MAKE SURE PORT/ID NUMBERS DON'T REPEAT FOR LIKE OBJECTS, AND THAT PORT NUMBERS DON'T GO OUT OF BOUNDS
	
	// Objects and variables used for driving the robot:
	
	// DrivingDevices
	/** The set of devices used for driving the robot */
	private DrivingDevices driveControl;
	
	// Talons
	/** The ID of the Talon that controls the front-left driving motor */
	public static final int TALON_DRIVE_FL_ID = 3;
	/** The ID of the Talon that controls the back-left driving motor */
	public static final int TALON_DRIVE_BL_ID = 4;
	/** The ID of the Talon that controls the front-right driving motor */
	public static final int TALON_DRIVE_FR_ID = 1;
	/** The ID of the Talon that controls the back-right driving motor */
	public static final int TALON_DRIVE_BR_ID = 2;
	
	// Solenoid
	/** The port on the PCM where the solenoid that controls the shifter is connected */
	public static final int SOL_GEAR_SHIFTING_PCM_PORT = 0;
	
	// Encoders
	/** The port on the DIO where the left encoder's A channel is connected */
	public static final int LEFT_ENCODER_DIO_PORT_A = 6;
	/** The port on the DIO where the left encoder's B channel is connected */
	public static final int LEFT_ENCODER_DIO_PORT_B = 7;
	/** The port on the DIO where the right encoder's A channel is connected */
	public static final int RIGHT_ENCODER_DIO_PORT_A = 8;
	/** The port on the DIO where the right encoder's B channel is connected */
	public static final int RIGHT_ENCODER_DIO_PORT_B = 9;
	
	// Gyro
	/** Port of Gyro */
	public static final int GYRO_PORT = 0;
	
	// Objects and variables used for the PickupArm:
	
	// PickupArm
	/** The pickup arm on the robot */
	private PickupArm arm;
	
	// Talons
	/** The ID of the Talon that controls the pickup arm's left motor */
	public static final int TALON_PICKUP_ARM_LEFT_ID = 8;
	/** The ID of the Talon that controls the pickup arm's right motor */
	public static final int TALON_PICKUP_ARM_RIGHT_ID = 7;
	/** The ID of the Talon that controls the wheels on the pickup arm. */
	public static final int TALON_PICKUP_ARM_PICKUP_WHEELS_ID = 6;
	
	// Limit Switches
	/** The port on the DIO where the limit switch in the pickup arm's "Stored Region" is connected */
	public static final int LS_PICKUP_ARM_STORED_DIO_PORT =  0;
	/** The port on the DIO where the limit switch in the pickup arm's "Pickup Region" is connected */
	public static final int LS_PICKUP_ARM_PICKUP_DIO_PORT = 4;
	
	// Arm Motion
	/** The region that the pickup arm will move to. During teleop, this value is set by buttons on the joystick */
	private int pickupArmDesiredRegion = 0; // Set to 0 automatically, unless changed
	
	// Objects and variables used for the Wedge:
	
	/** The wedge on the robot */
	private Wedge wedge;
	/** The ID of the Talon that controls the wedge */
	public static final int TALON_WEDGE_ID = 5;
	
	// Objects and variables involving manual control of the robot:
	
	// Joystick
	/** The joystick used to control the robot when the pickup arm is in the front */
	private Joystick stickA;
	/** The joystick used to control the robot when the wedge is in the front */
	private Joystick stickB;
	/** The USB port on the computer that stickA is connected to. USB port #'s are configured from the driver station */
	public static final int JOYSTICK_A_USB_PORT = 0;
	/** The USB port on the computer that stickB is connected to. USB port #'s are configured from the driver station */
	public static final int JOSTICK_B_USB_PORT = 1;
	
	// stickA & stickB Button Mapping
	/** The button on both joysticks that will put the robot in high gear */
	public static final int JOYSTICK_HIGH_GEAR = 6;
	/** The button on both joysticks that will put the robot in low gear */
	public static final int JOYSTICK_LOW_GEAR = 7;
	
	// stickA Button Mapping
	/** The button on stickA that will set pickupArmDesiredRegion to 0 */
	public static final int JOYSTICK_A_PICKUP_ARM_STORE = 2;
	/** The button on stickA that will set pickupArmDesiredRegion to 2 */
	public static final int JOYSTICK_A_PICKUP_ARM_APPROACH = 3;
	/** The button on stickA that will set pickupArmDesiredRegion to 4 */
	public static final int JOYSTICK_A_PICKUP_ARM_PICKUP = 1;
	/** The button on stickA that will spin the pickup wheels forward */
	public static final int JOYSTICK_A_PICKUP_WHEELS_FORWARDS = 5;
	/** The button on stickA that will spin the pickup wheels backward */
	public static final int JOYSTICK_A_PICKUP_WHEELS_BACKWARDS = 4;
	
	// stickB Button Mapping
	/** The button on stickB that will deploy the wedge */
	public static final int JOYSTICK_B_WEDGE_DEPLOY = 3;
	/** The button on stickB that will retract the wedge */
	public static final int JOYSTICK_B_WEDGE_RETRACT = 2;
	
	// Objects and variables involving the robot's autonomous functions:
	
	// AutoMethods
	private AutoMethods autoMethods;
	
	// Choosing an autonomous routine
	/** The SendableChooser object that allows different autonomous modes to be selected from the driver station. */
    SendableChooser autonomousChooser;
    /** The number that represents which autonomous routine is selected on the driver station */
    private int selectedAuto;
    
    // Miscellaneous objects and variables:
    
	/** Don't ask. */
	protected int farfar37;

	
	// Methods:
	
	
    /** 
     * This function is run once when the robot is first started up and should be used for any initialization code.
     */
    public void robotInit()
    {
    	// Construct the DrivingDevices
    	driveControl = new DrivingDevices(TALON_DRIVE_BR_ID, TALON_DRIVE_BL_ID, TALON_DRIVE_FR_ID, TALON_DRIVE_FL_ID, 
    			SOL_GEAR_SHIFTING_PCM_PORT, LEFT_ENCODER_DIO_PORT_A, LEFT_ENCODER_DIO_PORT_B, 
    			RIGHT_ENCODER_DIO_PORT_A, LEFT_ENCODER_DIO_PORT_B, GYRO_PORT);
    	
    	// Construct the PickupArm
    	arm = new PickupArm(TALON_PICKUP_ARM_LEFT_ID, TALON_PICKUP_ARM_RIGHT_ID, TALON_PICKUP_ARM_PICKUP_WHEELS_ID, 
    			LS_PICKUP_ARM_STORED_DIO_PORT, LS_PICKUP_ARM_PICKUP_DIO_PORT);
    	
    	// Construct the Wedge
    	wedge = new Wedge(TALON_WEDGE_ID);
    	
    	// Construct the Joysticks
    	stickA = new Joystick(JOYSTICK_A_USB_PORT);
    	stickB = new Joystick(JOSTICK_B_USB_PORT);
    	
    	// Construct the AutoRoutines
    	autoMethods = new AutoMethods(driveControl, arm, wedge);
    	
    	// Construct the autonomousChooser
    	autonomousChooser = new SendableChooser();
    	
    	// Add the different autonomous options to the chooser
        autonomousChooser.addDefault("Auto1()", 1);
        autonomousChooser.addObject("Auto2()", 2);
        
        // Put the chooser on the SmartDashboard
        SmartDashboard.putData("Which autonomous option would you like to use?", autonomousChooser);
    }
    
	/**
	 * This method is run once when the robot enters autonomous mode.
	 * This method is used to determine which autonomous routine to perform 
	 * by retrieving the selected routine from the driver station.
	 */
    public void autonomousInit()
    {
    	// Get the selected autonomous routine from the driver station
    	selectedAuto =  (int) autonomousChooser.getSelected();
		System.out.println("Preparing to run autonomous routine #" + selectedAuto + "."); // This is for testing the SendableChooser
		
		// Set the currentStep in the auto method to be executed back to 1
		autoMethods.resetCurrentStep();
		
		// Reset the encoders and the gyro
		driveControl.resetEncodersAndGyro();
    }

    /** 
     * This function is called periodically while the robot is in autonomous mode.
     */
    public void autonomousPeriodic()
    {
    	if (selectedAuto == 1)
    		autoMethods.Auto1();
    	else if (selectedAuto == 2)
    		autoMethods.Auto2();
    }
    
    /**
     * This method is run once when the robot enters teleop mode.
     */
    public void teleopInit()
    {
    	
    }

    /**
     * This function is called periodically while the robot is in teleop mode.
     */
    public void teleopPeriodic()
    {
    	// Let's try to keep this to simply method calls triggered by buttons.
    	// Remember to have any mildly complicated operation occur in the class the operation is associated with.
    	
    	// Driving
    	if (stickB.getX() == 0 && stickB.getY() == 0) // Lets stickA be used only if stickB is in the neutral position.
    		driveControl.arcadeDriveWithPickupArmInFront(stickA);
    	if (stickA.getX() == 0 && stickA.getY() == 0) // Lets stickB be used only if stickA is in the neutral position.
    		driveControl.arcadeDriveWithWedgeInFront(stickB);
    	
    	// Shifting Gears
    	if (stickA.getRawButton(JOYSTICK_HIGH_GEAR) || stickB.getRawButton(JOYSTICK_HIGH_GEAR))
    		driveControl.setHighGear();
    	else if (stickA.getRawButton(JOYSTICK_LOW_GEAR) || stickB.getRawButton(JOYSTICK_LOW_GEAR))
    		driveControl.setLowGear();
    	
    	// Driving Data
    	driveControl.putDataOnSmartDashboard();
    	
    	// Forwards Joystick
    	
    	// Pickup Arm
    	if (stickA.getRawButton(JOYSTICK_A_PICKUP_ARM_STORE))
    		pickupArmDesiredRegion = PickupArm.REG_STORE;
    	else if (stickA.getRawButton(JOYSTICK_A_PICKUP_ARM_APPROACH))
    		pickupArmDesiredRegion = PickupArm.REG_APPROACH;
    	else if (stickA.getRawButton(JOYSTICK_A_PICKUP_ARM_PICKUP))
    	{
    		arm.moveToRegion(PickupArm.REG_PICKUP);
    		pickupArmDesiredRegion = PickupArm.REG_APPROACH;
    	}
    	if (!stickA.getRawButton(JOYSTICK_A_PICKUP_ARM_PICKUP))
    		arm.moveToRegion(pickupArmDesiredRegion);    	
    	
    	// Pickup Wheels
    	if (stickA.getRawButton(JOYSTICK_A_PICKUP_WHEELS_BACKWARDS) || arm.getCurrentRegion() == 1)
    		arm.spinPickupWheels(1);
    	else if (stickA.getRawButton(JOYSTICK_A_PICKUP_WHEELS_FORWARDS) || arm.getCurrentRegion() > 2)
			arm.spinPickupWheels(-1);
		else
    		arm.stopPickupWheels();
    	
    	// Arm Data
    	arm.putDataOnSmartDashboard();
    	
    	// Backwards Joystick
    	
    	// Wedge
    	if (stickB.getRawButton(JOYSTICK_B_WEDGE_DEPLOY))
    		wedge.deploy();
    	else if (stickB.getRawButton(JOYSTICK_B_WEDGE_RETRACT))
    		wedge.retract();
    	wedge.checkIfWedgeMotorShouldStop();
    }
    
    /**
     * This function is run once when the robot enters test mode.
     */
    public void testInit()
    {
    	
    }
    
    /**
     * This function is called periodically while the robot is in test mode.
     */
    public void testPeriodic()
    {
    	arm.manualControl(stickA); // This is for testing the pickup arm
    	
    	if (stickA.getRawButton(JOYSTICK_A_PICKUP_WHEELS_FORWARDS)) // This is for testing the pickup arm
    		arm.spinPickupWheels(-1);
    	else if (stickA.getRawButton(JOYSTICK_A_PICKUP_WHEELS_BACKWARDS)) // This is for testing the pickup arm
    		arm.spinPickupWheels(1);
    	else
    		arm.stopPickupWheels();
    }   
}