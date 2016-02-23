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
	private CANTalon motor;
	
	/** Timer for retracting and deploying wedge */
	private Timer timer;
	
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
	public static final double RETRACT_TIME = 0.4;
	
	/** Time to run motor for deployment (seconds) */
	public static final double DEPLOY_TIME = 0.4;
	
	/**
	 * Constructor for the wedge
	 * @param talonId ID of the talon controlling the wedge's motor
	 */
	public Wedge(int talonId)
	{
		motor = new CANTalon(talonId);
		timer = new Timer();
	}
	
	/**
	 * Deploys the wedge
	 */
	public void deploy()
	{
		wedgeDirection = WEDGE_DEPLOY;
		motor.set(-MOTOR_SPEED);
		timer.start();
	}
	
	/**
	 * Retracts the wedge
	 */
	public void retract()
	{
		wedgeDirection = WEDGE_RETRACT;
		motor.set(MOTOR_SPEED);
		timer.start();
	}
	
	public void update()
	{
		if (wedgeDirection == WEDGE_DEPLOY && timer.get() == DEPLOY_TIME)
		{
			stop();
			timer.reset();
		}
		else if (wedgeDirection == WEDGE_RETRACT && timer.get() == RETRACT_TIME)
		{
			stop();
			timer.reset();
		}
	}
	
	/**
	 * Stops the wedge
	 */
	public void stop()
	{
		motor.set(0);
		wedgeDirection = 0;
	}
}