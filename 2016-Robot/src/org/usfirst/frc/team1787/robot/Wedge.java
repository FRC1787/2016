package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.CANTalon;

/**
 * Class representing the wedge
 * @author David Miron
 * @author Simon Wieder
 *
 */

public class Wedge {
	
	/** Talon for the motor controlling the wedge */
	private CANTalon motor;
	
	/** Speed of motor */
	public static final double MOTOR_SPEED = 0.6;
	
	/**
	 * Constructor for the wedge
	 * @param talonId ID of the talon controlling the wedge's motor
	 */
	public Wedge(int talonId)
	{
		motor = new CANTalon(talonId);
	}
	
	/**
	 * Deploys the wedge
	 */
	public void deploy()
	{
		motor.set(-MOTOR_SPEED);
	}
	
	/**
	 * Retracts the wedge
	 */
	public void retract()
	{
		motor.set(MOTOR_SPEED);
	}
	
	/**
	 * Stops the wedge
	 */
	public void stop()
	{
		motor.set(0);
	}
}