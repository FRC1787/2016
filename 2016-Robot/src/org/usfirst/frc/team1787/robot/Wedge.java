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
	/** The Talon that controls the wedge motor. */
	private CANTalon wedgeTalon;
	
	/** Timer used for retracting and deploying wedge. */
	private Timer wedgeTimer;
	
	// Wedge Motion Info
	/** How fast the wedge will move in relation to its max speed (a value of 0.7 means 70% of max speed). */
	public static final double MOTOR_SPEED = 0.7;
	/** The value that indicates the wedge is being deployed. */
	public static final int DEPLOY = 1;
	/** The value that indicates the wedge is being retracted. */
	public static final int RETRACT = -1;
	/** The value that indicates the wedge is stationary. */
	public static final int STATIONARY = 0;
	/** The value that represents the motion of the wedge. */
	private int wedgeDirection = STATIONARY;
	/** The value that represents the position of the wedge (Wedge.DEPLOY or Wedge.RETRACT) */
	private int wedgePosition = RETRACT;
	
	// Wedge Timing
	/** The time, in seconds, that the motor will run when retracting the wedge. */
	public static final double RETRACT_TIME = 1.5;
	/** The time, in seconds, that the motor will run when deploying the wedge. */
	public static final double DEPLOY_TIME = 2;
	
	/**
	 * Constructor for the wedge.
	 * @param talonId The ID of the talon controlling the wedge's motor.
	 */
	public Wedge(int talonId)
	{
		wedgeTalon = new CANTalon(talonId);
		wedgeTimer = new Timer();
	}
	
	/**
	 * Deploys the wedge at the push of a button if it is retracted.
	 * Retracts the wedge at the push of a button if it is deployed.
	 */
	public void toggle()
	{
		if (wedgePosition == RETRACT)
			deploy();
		else if (wedgePosition == DEPLOY)
			retract();
	}
	
	/**
	 * Deploys the wedge at the push of a button.
	 */
	public void deploy()
	{
		wedgeTalon.set(-MOTOR_SPEED);
		wedgeDirection = DEPLOY;
		if (wedgeTimer.get() == 0)
			wedgeTimer.start();
	}
	
	/**
	 * Retracts the wedge at the push of a button.
	 */
	public void retract()
	{
		wedgeTalon.set(MOTOR_SPEED);
		wedgeDirection = RETRACT;
		if (wedgeTimer.get() == 0)
			wedgeTimer.start();
	}
	
	/**
	 * Stops the wedge when (a)the wedge is deploying and the wedgeTimer reads equal to or greater than the deploy time, or 
	 * (b)the wedge is retracting and the wedgeTimer reads equal to or greater than the retract time.
	 */
	public void checkWedgeTimer()
	{
		if (wedgeDirection == DEPLOY && wedgeTimer.get() >= DEPLOY_TIME)
		{
			stop();
			wedgePosition = DEPLOY;
		}
		else if (wedgeDirection == RETRACT && wedgeTimer.get() >= RETRACT_TIME)
		{
			stop();
			wedgePosition = RETRACT;
		}
	}
	
	/**
	 * Stops the wedge.
	 */
	public void stop()
	{
		wedgeTalon.set(0);
		wedgeDirection = STATIONARY;
		wedgeTimer.stop();
		wedgeTimer.reset();
	}

	/**
	 * Gets the direction of the wedge.
	 * @return The direction of the wedge.
	 */
	public int getDirection()
	{
		return wedgeDirection;
	}
	
	/**
	 * Gets the position of the wedge. Either Wedge.DEPLOY or Wedge.RETRACT
	 * @return The position of the wedge.
	 */
	public int getPosition()
	{
		return wedgePosition;
	}
}