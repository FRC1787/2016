package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;

/**
 * Class representing the wedge
 * @author David Miron
 * @author Simon Wieder
 *
 */

public class Wedge {
	
	/** Talon for the motor controlling the wedge */
	private CANTalon wedgeTalon;
	
	/** Timer for retracting and deploying wedge */
	private Timer wedgeTimer;
	
	/** Direction of the wedge
	 *  1 = deploying
	 *  0 = stationary
	 * -1 = retracting
	 */
	private int wedgeDirection = STATIONARY;
	
	public static final int DEPLOY = 1;
	
	public static final int RETRACT = -1;
	
	public static final int STATIONARY = 0;
	
	/** Speed of motor */
	public static final double MOTOR_SPEED = 0.6;
	
	/** Time to run motor for retraction (seconds) */
	public static final double RETRACT_TIME = 0.75;
	
	/** Time to run motor for deployment (seconds) */
	public static final double DEPLOY_TIME = 0.5;
	
	/**
	 * Constructor for the wedge
	 * @param talonId ID of the talon controlling the wedge's motor
	 */
	public Wedge(int talonId)
	{
		wedgeTalon = new CANTalon(talonId);
		wedgeTimer = new Timer();
	}
	
	/**
	 * Deploys the wedge
	 */
	public void deploy()
	{
		wedgeDirection = DEPLOY;
		wedgeTalon.set(-MOTOR_SPEED);
		wedgeTimer.start();
	}
	
	/**
	 * Retracts the wedge
	 */
	public void retract()
	{
		wedgeDirection = RETRACT;
		wedgeTalon.set(MOTOR_SPEED);
		wedgeTimer.start();
	}
	
	public void checkIfWedgeMotorShouldStop()
	{
		if (wedgeDirection == DEPLOY && wedgeTimer.get() >= DEPLOY_TIME)
		{
			stop();
			wedgeTimer.reset();
		}
		else if (wedgeDirection == RETRACT && wedgeTimer.get() >= RETRACT_TIME)
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
	
	public int getDirection()
	{
		return wedgeDirection;
	}
}