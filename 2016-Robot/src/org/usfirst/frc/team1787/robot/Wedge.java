package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;

/**
 * Class representing the wedge
 * @author David Miron
 * @author Simon Wieder
 */

public class Wedge
{
	/** The Talon that controls the wedge motor */
	private CANTalon wedgeTalon;
	
	/** Timer for retracting and deploying wedge */
	private Timer wedgeTimer;
	
	// Wedge Motion Info
	/** How fast the wedge will move in relation to its max speed (a value of 0.6 means 60% of max speed) */
	public static final double MOTOR_SPEED = 0.7;
	/** The value that indicates the wedge is being deployed */
	public static final int DEPLOY = 1;
	/** The value that indicates the wedge is being retracted */
	public static final int RETRACT = -1;
	/** The value that indicates the wedge is stationary */
	public static final int STATIONARY = 0;
	/** The value that represents the motion of the wedge */
	private int wedgeDirection = STATIONARY;
	
	// Wedge Timing
	/** The time, in seconds, that the motor will run when retracting the wedge. */
	public static final double RETRACT_TIME = 1.5;
	/** The time, in seconds, that the motor will run when deploying the wedge. */
	public static final double DEPLOY_TIME = 2;
	
	/**
	 * Constructor for the wedge
	 * @param talonId ID of the Talon controlling the wedge's motor
	 */
	public Wedge(int talonId)
	{
		wedgeTalon = new CANTalon(talonId);
		wedgeTimer = new Timer();
	}
	
	/**
	 * Deploys the wedge at the push of a button.
	 */
	public void deploy()
	{
		wedgeTalon.set(-MOTOR_SPEED);
		wedgeDirection = DEPLOY;
		wedgeTimer.start();
	}
	
	/**
	 * Retracts the wedge at the push of a button.
	 */
	public void retract()
	{
		wedgeTalon.set(MOTOR_SPEED);
		wedgeDirection = RETRACT;
		wedgeTimer.start();
	}
	
	/**
	 * Stops the wedge when (a)the wedge is deploying and the wedgeTimer reads equal to or greater than the deploy time, or 
	 * (b)the wedge is retracting and the wedgeTimer reads equal to or greater than the retract time.
	 */
	public void checkIfWedgeMotorShouldStop()
	{
		if ( (wedgeDirection == DEPLOY && wedgeTimer.get() >= DEPLOY_TIME) ||
			 (wedgeDirection == RETRACT && wedgeTimer.get() >= RETRACT_TIME) )
		{
			stop();
			wedgeTimer.reset();
		}
	}
	
	/**
	 * Stops the wedge
	 */
	public void stop()
	{
		wedgeTalon.set(0);
		wedgeDirection = STATIONARY;
	}

	/**
	 * Gets the direction of the wedge.
	 * @return The direction of the wedge.
	 */
	public int getDirection()
	{
		return wedgeDirection;
	}
}