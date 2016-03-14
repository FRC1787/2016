package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class represents the pickup arm on the robot.
 * @author David Miron
 * @author Simon Wieder
 */

/* NOTE: THIS LOGIC IS OUTDATED. WE NO LONGER USE A LIMIT SWITCH FOR THE APPROACH POSITION.
 * REFER TO THE NEXT COMMENT FOR AN EXPLANATION OF THE NEW LOGIC.
 * 
 * Notes regarding the "region" the arm occupies and logic for determining that region.
 * 
 * Region 0 = The default position of the arm / Where the arm is stored when not in use.
 * Region 1 = The area between region 0 and region 2.
 * Region 2 = The position the arm needs to be in when approaching a ball.
 * Region 3 = The area between region 2 and region 4.
 * Region 4 = The position the arm needs to be in to pickup the ball / load the ball into the robot.
 * 
 * We know for a fact whether or not the arm is in regions 0, 2, or 4 because limit switches will be in those positions.
 * When the limit switch in one of those regions is pressed down, we know the arm is in the region associated with that limit switch.
 * 
 * For the regions where a limit switch is not present, we can use other available information to determine if the arm is in those regions.
 * 
 * We know the arm is in region 1 when:
 * 	A)
 * 	  1) The last region the arm occupied was region 0
 * 	  2) The arm is not currently in region 0
 * 	  3) The arm has not yet entered region 2
 *  B)
 *    1) The last region the arm occupied was region 2
 *    2) The arm is not currently in region 2, 0, or 4
 *    3) The motors are moving the arm backward
 *    
 * We know the arm is in region 3 when:
 * A)
 * 	 1) The last region the arm occupied was region 4
 * 	 2) The arm is not currently in region 4
 * 	 3) The arm has not yet entered region 2
 * B)
 *   1) The last region the arm occupied was region 2
 *   2) The arm is not currently in region 2, 0, or 4
 *   3) The motors are moving the arm forward.
 * 
 * Code Note: The limit switches we are using are "Normally Open" meaning 
 * they read true when open (not activated) and read false when closed (activated).
 * 
 * Electrical Note: Limit switches are wired from ground to signal
 */

public class PickupArm
{
	// Talons
	/** Talon that controls the motor on the left side of the pickup arm */
	private CANTalon leftTalon;
	/** Talon that controls the motor on the right side of the pickup arm */
	private CANTalon rightTalon;
	/** Talon that controls the motor which spins the pickup-wheels */
	private CANTalon pickupWheels;
	
	// Limit Switches
	/** Limit Switch located at the arm's "default/stored" position (Region 0) */
	private DigitalInput regStoreLS;
	/** Limit Switch located at the arm's "pickup" position (Region 4) */
	private DigitalInput regPickupLS;
	
	// Region Info
	/** Region number associated with the storage position */ 
	public static final int REG_STORE = 0;
	/** Region number associated with the area between the Store and Approach positions */
	public static final int REG_STOREAPPROACH = 1;
	/** Region number associated with the approach position */
	public static final int REG_APPROACH = 2;
	/** Region number associated with the area between the Approach and Pickup positions */
	public static final int REG_APPROACHPICKUP = 3;
	/** Region number associated with the pickup position */
	public static final int REG_PICKUP = 4;
	/** The value that indicates the current region the arm occupies */
	private int currentRegion;
	
	// Arm Motion Info
	/** The speed the pickup arm will move in relation to its max speed (i.e. a value of 0.2 means 20% of max speed) */
	public static final double MOTOR_SPEED = 0.66;
	/** The value designating backwards motion of the arm */
	public static final int ARM_BACKWARDS = -1;
	/** The value designating no motion of the arm */
	public static final int ARM_STATIONARY = 0;
	/** The value designating forwards motion of the arm */
	public static final int ARM_FORWARDS = 1;
	/** The value that indicates the current motion of the arm */
	private int armDirection = ARM_STATIONARY;
	
	// Pickup Wheels Motion Info
	/** The value designating that the pickup-wheels are spinning forwards. */
	public static final int WHEELS_PICKUP = -1;
	/** The value designating that the pickup-wheels are not spinning. */
	public static final int WHEELS_STATIONARY = 0;
	/** The value designating that the pickup-wheels are spinning backwards */
	public static final int WHEELS_EJECT = 1;
	/** The value that indicates the current motion of the pickup-wheels */
	private int wheelsDirection = WHEELS_STATIONARY;
		
	// Region 2 Timing
	/** The timer used to determine if the arm is in region 2. */
	private Timer reg2Timer;
	/** The time, in seconds, that it takes the arm to move from region 0 to region 2. */
	private static final double STORE_TO_APPROACH_TIME = 1.17;
	/** The time, in seconds, that it takes the arm to move from region 4 to region 2. */
	private static final double PICKUP_TO_APPROACH_TIME = 0.006; //From 0.007
	/** The value that indicates if the arm is currently moving towards region 2 from region 0. */
	private boolean movingTowardsApproachFromStore = false;
	/** The value that indicates if the arm is currently moving towards region 2 from region 4.  */
	private boolean movingTowardsApproachFromPickup = false;
	
	/**
	 * Constructor for the PickupArm class
	 * @param leftTalonID    ID of the talon on the right of the arm.
	 * @param rightTalonID   ID of the talon on the left of the arm.
	 * @param pickupWheelsID ID of the talon that controls the pickup-wheels.
	 * @param region0LSPort  The physical port that the limit switch at region 0 is plugged into on the roboRio.
	 * @param region4LSPort  The physical port that the limit switch at region 4 is plugged into on the roboRio.
	 */
	public PickupArm(int leftTalonID, int rightTalonID, int pickupWheelsID, 
					 int region0LSPort, int region4LSPort)
	{
		leftTalon = new CANTalon(leftTalonID);
		rightTalon = new CANTalon(rightTalonID);
		pickupWheels = new CANTalon(pickupWheelsID);
		regStoreLS = new DigitalInput(region0LSPort);
		regPickupLS = new DigitalInput(region4LSPort);
		reg2Timer = new Timer();
	}
	
	/**
	 * Moves the arm to a given region when called periodically.
	 * @param desiredRegion The region to move to.
	 */
	 public void moveToRegion(int desiredRegion)
	 {
  		determineCurrentRegion();
  		
  		if (currentRegion < desiredRegion)
  		{
  			moveArm(MOTOR_SPEED);
  			if (currentRegion == REG_STORE)
  			{
  				reg2Timer.start();
  				movingTowardsApproachFromStore = true;
  			}
  		}
  		else if (currentRegion > desiredRegion)
  		{
  			moveArm(-MOTOR_SPEED);
  			if (currentRegion == REG_PICKUP)
  			{
  				reg2Timer.start();
  				movingTowardsApproachFromPickup = true;
  			}
  		}
  		else if (currentRegion == desiredRegion)
  			stopArm();
	 }
	
	 /**
	  * Moves the arm at a desired speed when called periodically.
	  * @param motorSpeed The desired speed of the arm.
	  * Use a positive value for forwards motion.
	  * Use a negative value for backwards motion.
	  */
	private void moveArm(double motorSpeed)
	{
		if((motorSpeed > 0 && reg_Pickup_LS_Is_Activated()) || (motorSpeed < 0 && reg_Store_LS_Is_Activated()))
		{
			stopArm();
		}
		else
		{
			rightTalon.set(motorSpeed);
			leftTalon.set(motorSpeed);
			if (motorSpeed > 0)
			{
				armDirection = ARM_FORWARDS;
				movingTowardsApproachFromPickup = false;
			}
			else if (motorSpeed < 0)
			{
				armDirection = ARM_BACKWARDS;
				movingTowardsApproachFromStore = false;
			}
			else if (motorSpeed == 0)
				armDirection = ARM_STATIONARY;
		}	
	}
	
	/**
	 * Stops the arm.
	 */
	private void stopArm()
	{
		rightTalon.set(0);
		leftTalon.set(0);
		armDirection = ARM_STATIONARY;
		reg2Timer.reset();
	}
	
	/**
	 * Spins the pickup-wheels at a desired speed when called periodically.
	 * @param motorSpeed How fast the pickup-wheels spin.
	 * A negative value will spin them forwards (to pick up the ball).
	 * A positive value will spin them backwards (to eject the ball).
	 */
	public void spinPickupWheels(double motorSpeed)
	{
		pickupWheels.set(motorSpeed);
		if (motorSpeed < 0)
			wheelsDirection = WHEELS_PICKUP;
		else if (motorSpeed > 0)
			wheelsDirection = WHEELS_EJECT;
		else if (motorSpeed == 0)
			wheelsDirection = WHEELS_STATIONARY;
	}
	

	/**
	 * Stops the pickup wheels.
	 */
	public void stopPickupWheels()
	{
		pickupWheels.set(0);
		wheelsDirection = WHEELS_STATIONARY;
	}
	
	/**
	 * Allows for manual control of the arm using a joystick when called periodically.
	 * @param stick The joystick to use.
	 */
	public void manualControl(Joystick stick)
	{
		moveArm(-stick.getY() * 0.66);
		determineCurrentRegion(); // This method is included to keep the currentRegion updated for when not using arm manually.
	}
	
	/**
	 * Updates the current region the arm occupies.
	 */
	private void determineCurrentRegion()
	{
		
		if (reg_Store_LS_Is_Activated()) // If the LS @ region 0 is activated, the arm is in region 0.
			currentRegion = REG_STORE;
		else if (reg_Pickup_LS_Is_Activated()) // If the LS @ region 4 is activated, the arm is in region 4.
			currentRegion = REG_PICKUP;		
		else if ( (movingTowardsApproachFromStore && reg2Timer.get() >= STORE_TO_APPROACH_TIME) || (movingTowardsApproachFromPickup && reg2Timer.get() >= PICKUP_TO_APPROACH_TIME))
		{
			currentRegion = REG_APPROACH;
			movingTowardsApproachFromStore = false;
			movingTowardsApproachFromPickup = false;
			reg2Timer.reset();
		}
		else if ( (movingTowardsApproachFromStore && reg2Timer.get() < STORE_TO_APPROACH_TIME) || (currentRegion == REG_STORE && !reg_Store_LS_Is_Activated()) ) // if movingToApproachFromStore, but the time it takes to move from region 0 to region 2 hasn't passed, the arm is in region 1.
			currentRegion = REG_STOREAPPROACH;
		else if ( (movingTowardsApproachFromPickup && reg2Timer.get() < PICKUP_TO_APPROACH_TIME) || (currentRegion == REG_PICKUP && !reg_Pickup_LS_Is_Activated())) // if movingToApproachFromPickup, but the time it takes to move from region 4 to region 2 hasn't passed, the arm is in region 3.
			currentRegion = REG_APPROACHPICKUP;
		
		
		else if (currentRegion == REG_APPROACH) // if the currentRegion is 2 and...
		{
			if (armDirection == ARM_FORWARDS) // the arm is moving forwards, then arm is in region 3
				currentRegion = REG_APPROACHPICKUP;
			else if (armDirection == ARM_BACKWARDS) // the arm is moving backwards, then the arm is in region 1
				currentRegion = REG_STOREAPPROACH;
		}
	}
	
	/**
	 * Tells if the region 0 limit switch is activated.
	 * @return If the region 0 limit switch is activated.
	 */
	public boolean reg_Store_LS_Is_Activated()
	{
		return regStoreLS.get(); // This switch is normally closed
	}
	
	/**
	 * Tells if the region 4 limit switch is activated.
	 * @return If the region 4 limit switch is activated.
	 */
	public boolean reg_Pickup_LS_Is_Activated()
	{
		return regPickupLS.get(); // This switch is normally closed
	}
	
	/**
	 * Gets the current region the arm occupies.
	 * @return The current region the arm occupies.
	 */
	public int getCurrentRegion()
	{
		return currentRegion;
	}
	
	/**
	 * Puts data related to the pickup arm on the SmartDashboard.
	 */
	public void putDataOnSmartDashboard()
	{
		//SmartDashboard.putNumber("Current Region", currentRegion);
		//SmartDashboard.putBoolean("Region 0 LS Reading", reg_Store_LS_Is_Activated());
		//SmartDashboard.putBoolean("Region 4 LS Reading", reg_Pickup_LS_Is_Activated());
		// SmartDashboard.putBoolean("movingTowardsApproachFromStore", movingTowardsApproachFromStore);
		// SmartDashboard.putBoolean("movingTowardsApproachFromPickup", movingTowardsApproachFromPickup);
	}
}