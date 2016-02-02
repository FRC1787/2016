package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;

public class PickupArm {

	private CANTalon pickupRight;
	private CANTalon pickupLeft;
	private CANTalon pickupWheels;
	private DigitalInput posStore;
	private DigitalInput posApproach;
	private DigitalInput posPickup;
	private String position;
	
	public PickupArm(int pickupRightID, int pickupLeftID, int pickupWheelsID, 
					 int posStorePort, int posApproachPort, int posPickupPort)
		{
			pickupRight = new CANTalon(pickupRightID);
			pickupLeft = new CANTalon(pickupLeftID);
			pickupWheels = new CANTalon(pickupWheelsID);
			posStore = new DigitalInput(posStorePort);
			posApproach = new DigitalInput(posApproachPort);
			posPickup = new DigitalInput(posPickupPort);
			position = "stored";
		}
}
