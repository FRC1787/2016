package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 * Class for driving
 * @author David Miron
 * @author Simon Weider
 *
 */

public class Drive {
	
	private int motor_BR_ID;
	private int motor_BL_ID;
	private int motor_FR_ID;
	private int motor_FL_ID;
	
	private CANTalon talon_BR;
	private CANTalon talon_BL;
	private CANTalon talon_FR;
	private CANTalon talon_FL;
	
	RobotDrive theRobot;
	
	Shifter shifter;
	
	/**
	 * Constructor for Drive class
	 * @param motor_BR_ID
	 * @param motor_BL_ID
	 * @param motor_FR_ID
	 * @param motor_FL_ID
	 * @param sol_shifter_port
	 */
	public Drive(int motor_BR_ID, int motor_BL_ID, int motor_FR_ID, int motor_FL_ID, int sol_shifter_port)
	{
		this.motor_BR_ID = motor_BR_ID;
		this.motor_BL_ID = motor_BL_ID;
		this.motor_FR_ID = motor_FR_ID;
		this.motor_FL_ID = motor_FL_ID;
		
		talon_BR = new CANTalon(this.motor_BR_ID);
		talon_BL = new CANTalon(this.motor_BL_ID);
		talon_FR = new CANTalon(this.motor_FR_ID);
		talon_FL = new CANTalon(this.motor_FL_ID);
		
		theRobot = new RobotDrive(talon_FL, talon_BL, talon_FR, talon_BR);
		
		shifter = new Shifter(sol_shifter_port);
	}
	
	/**
	 * To drive the robot
	 * @param stick The joystick
	 */
	public void drive(Joystick stick)
	{
		theRobot.arcadeDrive(stick);
	}
	
	/**
	 * Set the robot to the gear that is high
	 */
	public void setHighGear()
	{
		shifter.setHighGear();
	}
	
	/**
	 * Set the robot to the gear that is low
	 */
	public void setLowGear()
	{
		shifter.setLowGear();
	}
}
