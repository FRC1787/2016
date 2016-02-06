package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

/**
 * Class for pickup arm
 * 
 * @author David Miron
 * @author Simon Wieder
 *
 */

/*
 * Notes for determining the current region the arm occupies.
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
 */

public class PickupArm {

	/** Talon on the right side of the pickup arm */
	private CANTalon rightTalon;
	
	/** Talon on the left side of the pickup arm */
	private CANTalon leftTalon;
	
	/** Talon used for spinning the pickup-wheels */
	private CANTalon pickupWheels;
	
	/** Limit Switch located at the arm's "default/stored" position (Region 0) */
	private DigitalInput regStoreLS;
	
	/** Limit Switch located at the arm's "approach" position (Region 2) */
	private DigitalInput regApproachLS;
	
	/** Limit Switch located at the arm's pickup position (Region 4) */
	private DigitalInput regPickupLS;
	
	/** Current region of arm */
	private int currentRegion;
	
	/** Region number of storage position */ 
	public static final int REG_STORE = 0;
	
	/** Region number of area between Store and Approach */
	public static final int REG_STOREAPPROACH = 1;
	
	/** Region number of approach position */
	public static final int REG_APPROACH = 2;
	
	/** Region number of area between Approach and Pickup */
	public static final int REG_APPROACHPICKUP = 3;
	
	/** Region number of pickup position */
	public static final int REG_PICKUP = 4;
	
	/** Number designating the backwards motion of the arm */
	public static final int ARM_BACKWARDS = -1;
	
	/** Number designating the lack of motion of the arm */
	public static final int ARM_STATIONARY = 0;
	
	/** Number designating the forwards motion of the arm */
	public static final int ARM_FORWARDS = 1;
	
	/** Don't ask. */
	protected int farfar37;
	
	/** 
	 * Direction arm  is moving
	 * -1: Backwards
	 *  0: Not Moving
	 *  1: Forwards
	 */
	private int armDirection = 0;
	
	/**
	 * Takes IDs and port numbers, not objects
	 * @param rightTalonID    ID of the talon on the right of the arm.
	 * @param leftTalonID     ID of the talon on the left of the arm.
	 * @param pickupWheelsID  ID of the talon that controls the pickup-wheels.
	 * @param region0LSPort   The physical port that the limit switch at region 0 is plugged in to on the roboRio.
	 * @param region2LSPort   The physical port that the limit switch at region 2 is plugged in to on the roboRio.
	 * @param region4LSPort   The physical port that the limit switch at region 4 is plugged in to on the roboRio.
	 */
	public PickupArm(int rightTalonID, int leftTalonID, int pickupWheelsID, 
					 int region0LSPort, int region2LSPort, int region4LSPort)
	{
		rightTalon = new CANTalon(rightTalonID);
		leftTalon = new CANTalon(leftTalonID);
		pickupWheels = new CANTalon(pickupWheelsID);
		regStoreLS = new DigitalInput(region0LSPort);
		regApproachLS = new DigitalInput(region2LSPort);
		regPickupLS = new DigitalInput(region4LSPort);
	}
	
	/**
	 * Move to a specified region
	 * @param desiredRegion The region to move to.
	 * @param motorSpeed Desired arm speed.
	 */
	 public void moveToRegion(int desiredRegion, double motorSpeed)
	 {
  		determineCurrentRegion();
  		if (currentRegion < desiredRegion)
  			moveArm(motorSpeed);
  		else if (currentRegion > desiredRegion)
  			moveArm(-motorSpeed);
  		else
  			stopArm();
	 }
	
	public void moveArm(double motorSpeed)
	{
		rightTalon.set(motorSpeed);
		leftTalon.set(-motorSpeed);
		if (motorSpeed > 0)
			armDirection = ARM_FORWARDS;
		else if (motorSpeed < 0)
			armDirection = ARM_BACKWARDS;
		else if (motorSpeed == 0)
			armDirection = ARM_STATIONARY;
	}
	
	/**
	 * Make arm stationary
	 */
	private void stopArm()
	{
		rightTalon.set(0);
		leftTalon.set(0);
		armDirection = ARM_STATIONARY;
	}
	
	/**
	 * Updates the current region the arm occupies when called.
	 */
	private void determineCurrentRegion()
	{
		
		if (regStoreLS.get()) // If the LS @ region 0 reads true, the arm is in region 0.
		{
			currentRegion = REG_STORE;
		}
		else if (regApproachLS.get()) // If the LS @ region 2 reads true, the arm is in region 2
		{
			currentRegion = REG_APPROACH;
		}
		else if (regPickupLS.get()) // If the LS @ region 4 reads true, the arm is in region 4.
		{
			currentRegion = REG_PICKUP;
		}
		else if (currentRegion == REG_STORE && !regStoreLS.get()) // if the currentRegion is 0, but the LS in region 0 reads false, the arm is in region 1.
		{
			currentRegion = REG_STOREAPPROACH;
		}
		else if (currentRegion == REG_PICKUP && !regPickupLS.get()) // if the currentRegion is 4, but the LS in region 4 reads false, the arm is in region 3.
		{
			currentRegion = REG_APPROACHPICKUP;
		}
		else if (currentRegion == REG_PICKUP && !regApproachLS.get()) // if the currentRegion is 2, but the LS in region 2 reads false, and...
		{
			if (armDirection == ARM_FORWARDS) // the arm is moving forwards, then arm is in region 3
			{
				currentRegion = REG_APPROACHPICKUP;
			}
			else if (armDirection == ARM_BACKWARDS) // the arm is moving backwards, then the arm is in region 1
			{
				currentRegion = REG_STOREAPPROACH;
			}
		}
	}
	
	/**
	 * Spin wheels forwards (to pick up the ball)
	 * @param motorSpeed How fast the wheels spin.
	 */
	public void spinPickupWheelsForwards(double motorSpeed)
	{
		pickupWheels.set(motorSpeed);
	}
	
	/**
	 * Spin wheels backwards (to eject the ball)
	 * @param motorSpeed How fast the wheels spin.
	 */
	public void spinPickupWheelsBackwards(double motorSpeed)
	{
		pickupWheels.set(-motorSpeed);
	}
	
	/**
	 * Manually control the arm with a joystick.
	 * @param stick The joystick being used.
	 */
	public void manualControl(Joystick stick)
	{
		moveArm(stick.getY());
		determineCurrentRegion(); // This method is included to keep the currentRegion updated for when not using arm manually.
	}	 
}