package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * This class controls which gear the robot is in. We created this class for better read-ability in the Robot class.
 * @author David Miron
 * @author Simon Wieder
 */
public class Shifter
{
	/** the solenoid that controls which gear the robot is in */
	private Solenoid sol_shifter;
	
	/**
	 * Constructor for the Shifter class
	 * @param sol_shifter_port The port on the PCM that the gear-shifting solenoid is connected to.
	 */
	public Shifter(int sol_shifter_port)
	{
		sol_shifter = new Solenoid(sol_shifter_port);
		/* Note that by using the Solenoid constructor that only takes the port number as a parameter, it is assumed that the
		 * ID of the solenoid is 0. We make the assumption because there are no other pneumatics systems on the robot currently.
		 */
	}
	
	/**
	 * This method puts the robot in high gear.
	 */
	public void setHighGear()
	{
		sol_shifter.set(true);
	}
	
	/**
	 * This method puts the robot in low gear.
	 */
	public void setLowGear()
	{
		sol_shifter.set(false);
	}
	
	/**
	 * Returns a boolean value indicating the current gear of the robot. 
	 * True indicates the robot is in high gear. False indicates the robot is in low gear.
	 * @return
	 */
	public boolean getCurrentGear()
	{
		return sol_shifter.get();
	}
}