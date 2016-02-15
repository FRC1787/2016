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
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot
{
	/* SendableChooser stuff:
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
    */
	
	//NOTE: MAKE SURE PORT/ID NUMBERS DON'T REPEAT FOR LIKE OBJECTS, AND THAT PORT NUMBERS DON'T GO OUT OF BOUNDS
	
	// Objects and variables used for driving the robot.
	private DrivingDevices driveControl;
	public static final int TALON_DRIVE_FL_ID = 3;
	public static final int TALON_DRIVE_BL_ID = 4;
	public static final int TALON_DRIVE_FR_ID = 5;  // This value was changed from 1 to 5 for testing the pickup arm.
	public static final int TALON_DRIVE_BR_ID = 6;  // This value was changed from 2 to 6 for testing the pickup arm.
	public static final int SOL_GEAR_SHIFTING_PCM_PORT = 0;
	public static final int LEFT_ENCODER_DIO_PORT_A = 7;
	public static final int LEFT_ENCODER_DIO_PORT_B = 8;
	public static final int RIGHT_ENCODER_DIO_PORT_A = 5;
	public static final int RIGHT_ENCODER_DIO_PORT_B = 6;
	
	// Objects and variables used for the PickupArm.
	private PickupArm arm;
	public static final int TALON_PICKUP_ARM_LEFT_ID = 1; // This value was changed from 5 to 1 for testing the pickup arm.
	public static final int TALON_PICKUP_ARM_RIGHT_ID = 2; // This value was changed from 6 to 2 for testing the pickup arm.
	public static final int TALON_PICKUP_ARM_PICKUP_WHEELS_ID = 7;
	public static final int LS_PICKUP_ARM_STORED_DIO_PORT =  0;
	public static final int LS_PICKUP_ARM_APPROACH_DIO_PORT = 2;
	public static final int LS_PICKUP_ARM_PICKUP_DIO_PORT = 4;
	public static final double PICKUP_ARM_MOTOR_SPEED = 0.2;
	//Set to 0 automatically, unless changed
	private int pickupArmDesiredRegion = 0;
	
	// Objects and variables involving control of the robot
	private Joystick stick;
	public static final int JOYSTICK_USB_PORT = 0;
	public static final int JOYSTICK_HIGH_GEAR = 6;
	public static final int JOYSTICK_LOW_GEAR = 7;
	public static final int JOYSTICK_PICKUP_ARM_STORE = 4;
	public static final int JOYSTICK_PICKUP_ARM_APPROACH = 3;
	public static final int JOYSTICK_PICKUP_ARM_PICKUP = 5;
	public static final int JOYSTICK_WEDGE_DEPLOY = 11;
	public static final int JOYSTICK_WEDGE_RETRACT = 10;
	
	// Objects and variables used for the Wedge
	private Wedge wedge;
	public static final int TALON_WEDGE_ID = 8;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
    	/* More SendableChooser stuff:
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        */
    	
    	driveControl = new DrivingDevices(TALON_DRIVE_BR_ID, TALON_DRIVE_BL_ID, TALON_DRIVE_FR_ID, TALON_DRIVE_FL_ID, 
    			SOL_GEAR_SHIFTING_PCM_PORT, LEFT_ENCODER_DIO_PORT_A, LEFT_ENCODER_DIO_PORT_B, RIGHT_ENCODER_DIO_PORT_A, LEFT_ENCODER_DIO_PORT_B);
    		
    	arm = new PickupArm(TALON_PICKUP_ARM_LEFT_ID, TALON_PICKUP_ARM_RIGHT_ID, TALON_PICKUP_ARM_PICKUP_WHEELS_ID, 
    			LS_PICKUP_ARM_STORED_DIO_PORT, LS_PICKUP_ARM_APPROACH_DIO_PORT, LS_PICKUP_ARM_PICKUP_DIO_PORT);
    	
    	stick = new Joystick(JOYSTICK_USB_PORT);
    	
    	wedge = new Wedge(TALON_WEDGE_ID);
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit()
    {
    	/* Even more SendableChooser stuff:
    	autoSelected = (String) chooser.getSelected();
    	autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		*/
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
    	/* Still even more SendableChooser stuff:
    	switch(autoSelected)
    	{
    	case customAuto:
        //Put custom auto code here   
            break;
    	case defaultAuto:
    	default:
    	//Put default auto code here
            break;
    	}
    	*/
    }
    
    public void teleopInit()
    {
    	
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
    	driveControl.driveWithJoystick(stick);
    	
    	//Set Gear
    	if (stick.getRawButton(JOYSTICK_HIGH_GEAR))
    	{
    		driveControl.setHighGear();
    	}
    	else if (stick.getRawButton(JOYSTICK_LOW_GEAR))
    	{
    		driveControl.setLowGear();
    	}
    	
    	//Set Pickup Arm Position
    	if (stick.getRawButton(JOYSTICK_PICKUP_ARM_STORE))
    	{
    		pickupArmDesiredRegion = PickupArm.REG_STORE;
    	}
    	else if (stick.getRawButton(JOYSTICK_PICKUP_ARM_APPROACH))
    	{
    		pickupArmDesiredRegion = PickupArm.REG_APPROACH;
    	}
    	else if (stick.getRawButton(JOYSTICK_PICKUP_ARM_PICKUP))
    	{
    		pickupArmDesiredRegion = PickupArm.REG_PICKUP;
    	}
    	arm.moveToRegion(pickupArmDesiredRegion, PICKUP_ARM_MOTOR_SPEED);
    	
    	//Wedge
    	if (stick.getRawButton(JOYSTICK_WEDGE_DEPLOY))
    	{
    		wedge.deploy();
    	}
    	else if (stick.getRawButton(JOYSTICK_WEDGE_RETRACT))
    	{
    		wedge.retract();
    	}
    	else
    	{
    		wedge.stop();
    	}
    }
    
    public void testInit()
    {
    	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic()
    {
    	arm.manualControl(stick);
    }   
}