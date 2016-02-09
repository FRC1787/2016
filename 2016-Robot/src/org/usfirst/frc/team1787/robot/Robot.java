
package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.CANTalon;
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
	
	// Objects and variables used for driving the robot.
	private RobotDrive myRobot;
	private static final int FRONT_LEFT_DRIVING_TALON_ID = 3;
	private static final int BACK_LEFT_DRIVING_TALON_ID = 4;
	private static final int FRONT_RIGHT_DRIVING_TALON_ID = 1;
	private static final int BACK_RIGHT_DRIVING_TALON_ID = 2;
	
	private CANTalon t1;
	private CANTalon t2;
	private CANTalon t3;
	private CANTalon t4;
	
	// Objects and variables used for the PickupArm.
	private PickupArm arm;
	private static final int PICKUP_ARM_RIGHT_TALON_ID = 5;
	private static final int PICKUP_ARM_LEFT_TALON_ID = 6;
	private static final int PICKUP_ARM_PICKUP_WHEELS_TALON_ID = 7;
	private static final int PICKUP_ARM_REGION_0_LIMIT_SWITCH_ID =  8;
	private static final int PICKUP_ARM_REGION_2_LIMIT_SWITCH_ID = 9;
	private static final int PICKUP_ARM_REGION_4_LIMIT_SWITCH_ID = 10;
	
	// Objects and variables involving control of the robot
	private Joystick stick;
	private static final int JOYSTICK_PORT = 0;
	
	// Objects and variables used for shifting gears
	//private Solenoid gearShiftingSolenoid;
	private static final int GEAR_SHIFTING_SOLENOID_ID = 11;
	private static final int GEAR_SHIFTING_SOLENOID_PCM_PORT = 12;
	
	
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
    	
    	t1 = new CANTalon(1); //front right
    	t2 = new CANTalon(2); //back right
    	t3 = new CANTalon(3); //front left
    	t4 = new CANTalon(4); //back left
    	
    	//myRobot = new RobotDrive(FRONT_LEFT_DRIVING_TALON_ID, BACK_LEFT_DRIVING_TALON_ID, 
    	//		FRONT_RIGHT_DRIVING_TALON_ID, BACK_RIGHT_DRIVING_TALON_ID);
    	
    	myRobot = new RobotDrive(t3, t4, t1, t2);
    	
    	arm = new PickupArm(PICKUP_ARM_RIGHT_TALON_ID, PICKUP_ARM_LEFT_TALON_ID, PICKUP_ARM_PICKUP_WHEELS_TALON_ID, 
    			PICKUP_ARM_REGION_0_LIMIT_SWITCH_ID, PICKUP_ARM_REGION_2_LIMIT_SWITCH_ID, PICKUP_ARM_REGION_4_LIMIT_SWITCH_ID);
    	//arm.mo);
    	
    	stick = new Joystick(JOYSTICK_PORT);
    	
    	//gearShiftingSolenoid = new Solenoid(GEAR_SHIFTING_SOLENOID_ID, GEAR_SHIFTING_SOLENOID_PCM_PORT);
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
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
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
    	myRobot.arcadeDrive(stick);
    	DriverStation.reportError(stick.getRawAxis(0) + "\n", false);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic()
    {
    	
    }
    
}
