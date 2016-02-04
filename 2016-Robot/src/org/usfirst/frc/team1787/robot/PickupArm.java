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
	private DigitalInput region0LS;
	
	/** Limit Switch located at the arm's "approach" position (Region 2) */
	private DigitalInput region2LS;
	
	/** Limit Switch located at the arm's pickup position (Region 4) */
	private DigitalInput region4LS;
	
	/** Current position of arm */
	private int currentRegion;
	
	/** Previous position of arm */
	private int previousPosition;
	
	/** The position the arm needs to move to */
	private int desiredPosition;
	
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
	 * 
	 */
	
	
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
		region0LS = new DigitalInput(region0LSPort);
		region2LS = new DigitalInput(region2LSPort);
		region4LS = new DigitalInput(region4LSPort);
		//Add method here for moving the arm to region 0.
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
			desiredPosition = POS_STORE;
			break;
		case POS_APPROACH:
			desiredPosition = POS_APPROACH;
			break;
		case POS_PICKUP:
			desiredPosition = POS_PICKUP;
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
		switch(desiredPosition)
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
		if(!region0LS.get())
		{
			rightTalon.set(motorSpeed);
			leftTalon.set(-motorSpeed);
		} else {
			rightTalon.set(0);
			leftTalon.set(0);
		}
	}
	
	private void goToPickup()
	{
		if(!region4LS.get())
		{
			rightTalon.set(-motorSpeed);
			leftTalon.set(motorSpeed);
		} else {
			rightTalon.set(0);
			leftTalon.set(0);
		}
	}
	
	private void checkPositionChange()
	{
		 
	}
	
	//Code below here is some stuff Simon is working on / thinking about.
	
	public void moveToRegion (int desiredRegion)
	{
		determineCurrentRegion();
		if (currentRegion < desiredRegion)
		{
			//move arm forward
		}
		else if (currentRegion > desiredRegion)
		{
			//move arm backward
		}
		else
		{
			//set arm motor speed to 0
		}		
	}
	
	private void determineCurrentRegion ()
	{
		if (region0LS.get())
		{
			currentRegion = 0;
		}
		else if (region2LS.get())
		{
			currentRegion = 2;
		}
		else if (region4LS.get())
		{
			currentRegion = 4;
		}
		else if ((!region0LS.get()) && (currentRegion == 0))
		{
			currentRegion = 1;
		}
		else if ((!region4LS.get()) && (currentRegion == 4))
		{
			currentRegion = 3;
		}
		/* Need an else if condition for when the currentRegion is 2, but the limit switch in region 2 reads false.
		The arm can be in either region 1 or 3 in that case. */
	}
}
