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
	
	/**
	 * Construct the wedge
	 * @param talonId ID of the talon controlling the motor
	 */
	public Wedge(int talonId)
	{
		motor = new CANTalon(talonId);
	}
	
	private void moveWedge(double speed)
	{
		motor.set(speed);
	}
	
}