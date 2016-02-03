package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;

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
 * Region 4 = The position the arm needs to be in to load the ball into the robot.
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

	/** Talon for right pickup arm */
	private CANTalon pickupRight;
	
	/** Talon for left pickup arm */
	private CANTalon pickupLeft;
	
	/** Talon for spinning pickup wheels */
	private CANTalon pickupWheels;
	
	/** Limit Switch stored position */
	private DigitalInput posStoreLS;
	
	/** Limit Switch for approach position */
	private DigitalInput posApproachLS;
	
	/** Limit Switch for pickup position */
	private DigitalInput posPickupLS;
	
	/** Position to move to */
	private int toGo;
	
	/** ID of storage position */
	public static final int POS_STORE = 0;
	
	/** ID of approach position */
	public static final int POS_APPROACH = 2;
	
	/** ID of pickup position */
	public static final int POS_PICKUP = 4;
	
	/** motor speed */
	public static double motorSpeed = 0.5;
	/**
	 * System for position:
	 * Stored: 0
	 * Stored to Approach: 1
	 * Approach: 2
	 * Approach to Pickup: 3
	 * Pickup: 4
	 */
	
	/** Current position of arm */
	private int position;
	
	
	/**
	 * Takes IDs and port numbers, not objects
	 * @param pickupRightID    ID of right pickup motor Talon
	 * @param pickupLeftID     ID of left pickup motor Talon
	 * @param pickupWheelsID   ID of pickup wheel motor Talon
	 * @param posStorePort     Analog input port of storage position limit switch
	 * @param posApproachPort  Analog input port of approach position limit switch
	 * @param posPickupPort    Analog input port of pickup position limit switch
	 */
	public PickupArm(int pickupRightID, int pickupLeftID, int pickupWheelsID, 
					 int POS_STOREPort, int POS_APPROACHPort, int POS_PICKUPPort)
	{
		pickupRight = new CANTalon(pickupRightID);
		pickupLeft = new CANTalon(pickupLeftID);
		pickupWheels = new CANTalon(pickupWheelsID);
		posStoreLS = new DigitalInput(POS_STOREPort);
		posApproachLS = new DigitalInput(POS_APPROACHPort);
		posPickupLS = new DigitalInput(POS_PICKUPPort);
		position = 0;
		
	}
	
	/**
	 * Set goal position
	 * @param positionToGo position to go
	 */
	public void setToGo(int positionToGo)
	{
		switch(positionToGo)
		{
		case POS_STORE:
			toGo = POS_STORE;
			break;
		case POS_APPROACH:
			toGo = POS_APPROACH;
			break;
		case POS_PICKUP:
			toGo = POS_PICKUP;
			break;
		}
	}
	
	/**
	 * Move to current goal position
	 * This method is called a lot every second (60 times)
	 */
	public void goToGoal()
	{
		checkPositonChange();
		switch(toGo)
		{
		case POS_STORE:
			goToStore();
			break;
			
		case POS_PICKUP:
			goToPickup();
			break;
		
		case POS_APPROACH:
			
		}
	}
	
	private void goToStore()
	{
		if(!posStoreLS.get())
		{
			pickupRight.set(motorSpeed);
			pickupLeft.set(-motorSpeed);
		} else {
			pickupRight.set(0);
			pickupLeft.set(0);
		}
	}
	
	private void goToPickup()
	{
		if(!posPickupLS.get())
		{
			pickupRight.set(-motorSpeed);
			pickupLeft.set(motorSpeed);
		} else {
			pickupRight.set(0);
			pickupLeft.set(0);
		}
	}
	
	private void checkPositionChange()
	{
		 
	}
}
