
package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
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
	
	// Objects / Variables used for the PickupArm.
	private PickupArm arm;
	private static final int PICKUP_ARM_RIGHT_TALON_ID = 0;
	private static final int PICKUP_ARM_LEFT_TALON_ID = 1;
	private static final int PICKUP_ARM_PICKUP_WHEELS_TALON_ID = 2;
	private static final int PICKUP_ARM_REGION_0_LIMIT_SWITCH_ID = 3;
	private static final int PICKUP_ARM_REGION_2_LIMIT_SWITCH_ID = 4;
	private static final int PICKUP_ARM_REGION_4_LIMIT_SWITCH_ID = 5;
	
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
    	arm = new PickupArm(PICKUP_ARM_RIGHT_TALON_ID, PICKUP_ARM_LEFT_TALON_ID, PICKUP_ARM_PICKUP_WHEELS_TALON_ID, 
    			PICKUP_ARM_REGION_0_LIMIT_SWITCH_ID, PICKUP_ARM_REGION_2_LIMIT_SWITCH_ID, PICKUP_ARM_REGION_4_LIMIT_SWITCH_ID);
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

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
        arm.moveToRegion(0, 0.2);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic()
    {
    
    }
    
}
