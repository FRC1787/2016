package org.usfirst.frc.team1787.robot;

/**
 * This class contains a set of settings to
 * be passed into the constructor of a Robot2016
 * class
 * 
 * This class is to be instantiated with an empty
 * constructor, and for each system's settings to
 * be input separately
 * 
 * @author David Miron
 *
 */

public class RobotSettings {
	
	// PickupArm Settings
	
	public int pickupArmRightTalonID;
	public int pickupArmLeftTalonID;
	public int pickupArmWheelsTalonID;
	public int storedLSPort;
	public int approachLSPort;
	public int pickupLSPort;
	public int pickupArmMotorSpeed;
	public int pickupWheelsMotorSpeed;
	
	/**
	 * Default constructor
	 */
	public RobotSettings()
	{
		
	}
	
	public void setPickupArmSettings(int pickupArmRightTalonID, int pickupArmLeftTalonID, int pickupArmWheelsTalonID,
								int storedLSPort, int approachLSPort, int pickupLSPort, int pickupArmMotorSpeed,
								int pickupWheelsMotorSpeed)
	{
		this.pickupArmRightTalonID = pickupArmRightTalonID;
		this.pickupArmLeftTalonID = pickupArmLeftTalonID;
		this.pickupArmWheelsTalonID = pickupArmWheelsTalonID;
		this.storedLSPort = storedLSPort;
		this.approachLSPort = approachLSPort;
		this.pickupLSPort = pickupLSPort;
		this.pickupArmMotorSpeed = pickupArmMotorSpeed;
		this.pickupWheelsMotorSpeed = pickupWheelsMotorSpeed;
	}
	
	
}
