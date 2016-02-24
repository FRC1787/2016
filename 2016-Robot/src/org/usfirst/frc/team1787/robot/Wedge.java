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
	private int wedgeDirection = WEDGE_STATIONARY;
	
	public static final int WEDGE_DEPLOY = 1;
	
	public static final int WEDGE_RETRACT = -1;
	
	public static final int WEDGE_STATIONARY = 0;
	
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
		wedgeDirection = WEDGE_DEPLOY;
		wedgeTalon.set(-MOTOR_SPEED);
		wedgeTimer.start();
	}
	
	/**
	 * Retracts the wedge
	 */
	public void retract()
	{
		wedgeDirection = WEDGE_RETRACT;
		wedgeTalon.set(MOTOR_SPEED);
		wedgeTimer.start();
	}
	
	public void checkIfWedgeMotorShouldStop()
	{
		if (wedgeDirection == WEDGE_DEPLOY && wedgeTimer.get() >= DEPLOY_TIME)
		{
			stop();
			wedgeTimer.reset();
		}
		else if (wedgeDirection == WEDGE_RETRACT && wedgeTimer.get() >= RETRACT_TIME)
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
		wedgeDirection = WEDGE_STATIONARY;
	}
}