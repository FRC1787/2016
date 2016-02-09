package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * Class for shifting gears
 * @author David Miron
 * @author Simon Weider
 *
 */

public class Shifter {
	
	private int sol_shifter_id;
	private Solenoid sol_shifter;
	
	/**
	 * Create Shifter class
	 * @param sol_shifter_id
	 */
	public Shifter(int sol_shifter_id)
	{
		this.sol_shifter_id = sol_shifter_id;
		sol_shifter = new Solenoid(this.sol_shifter_id);
	}
	
	/**
	 * Set shifter to high gear
	 */
	public void setHighGear()
	{
		sol_shifter.set(true);
	}
	
	/**
	 * Set shifter to low gear
	 */
	public void setLowGear()
	{
		sol_shifter.set(false);
	}
	
}
