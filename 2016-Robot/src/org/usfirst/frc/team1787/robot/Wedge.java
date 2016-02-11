package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.CANTalon;

/**
 * Class for controlling wedge
 * @author David Miron
 * @author Simon Weeder
 *
 */

public class Wedge {
	
	/** Talon for the motor controlling the wedge */
	private CANTalon motor;
	
	/** Speed of motor */
	public static final double MOTOR_SPEED = 0.6;
	
	/**
	 * Construct the wedge
	 * @param talonId ID of the talon controlling the motor
	 */
	public Wedge(int talonId)
	{
		motor = new CANTalon(talonId);
	}
	
	/**
	 * Deploy the wedge
	 */
	public void deploy()
	{
		motor.set(MOTOR_SPEED);
	}
	
	/**
	 * Retract the wedge
	 */
	public void retract()
	{
		motor.set(-MOTOR_SPEED);
	}
	
	/**
	 * Stop the wedge
	 */
	public void stop()
	{
		motor.set(0);
	}
}