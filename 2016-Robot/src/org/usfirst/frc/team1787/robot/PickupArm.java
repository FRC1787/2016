package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Class for pickup arm
 * 
 * @author David Miron
 *
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
	 * Set goal position to storage position
	 */
	public void moveToStore()
	{
		toGo = POS_STORE;
	}
	
	/**
	 * Set goal position to approach position
	 */
	public void moveToApproach()
	{
		toGo = POS_APPROACH;
	}
	
	/**
	 * Set goal position to pickup position
	 */
	public void moveToPickup()
	{
		toGo = POS_PICKUP;
	}
	
	/**
	 * Move to current goal position
	 */
	public void goToGoal()
	{
		switch(toGo)
		{
		case POS_STORE:
			if(posStoreLS.get())
			{
				pickupRight.set(0);
				pickupLeft.set(0);
				int x = 2;
			}
			pickupRight.set(motorSpeed);
			pickupLeft.set(-motorSpeed);
			break;
		case POS_APPROACH:
			if (position < POS_APPROACH)
			{
				pickupRight.set(-motorSpeed);
				pickupLeft.set(motorSpeed);
			} else if (position > POS_APPROACH)
			{
				pickupRight.set(motorSpeed);
				pickupLeft.set(-motorSpeed);
			}
			break;
		case POS_PICKUP:
			pickupRight.set(-motorSpeed);
			pickupLeft.set(motorSpeed);
			break;
		}
	}
	
}
