package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 * This class is a collection of all the mechanisms used for driving the robot.
 * These include the talons that control the driving motors, and the solenoid that controls the gear-shifter.
 * This class was made so that the Robot class can have one object that controls all aspects of driving.
 * @author David Miron
 * @author Simon Weider
 */
public class DrivingDevices
{
	/** The talon connected to the back-right motor */
	private CANTalon talon_BR;
	/** The talon connected to the back-left motor */
	private CANTalon talon_BL;
	/** The talon connected to the front-right motor */
	private CANTalon talon_FR;
	/** The talon connected to the front-left motor */
	private CANTalon talon_FL;
	
	/** The encoder on the left side of the robot */
	private Encoder leftEncoder;
	/** The encoder on the right side of the robot */
	private Encoder rightEncoder;
	
	/** The RobotDrive object that controls the driving of the robot */
	RobotDrive theRobot;
	
	/** The Shifter object that controls which gear the robot is in */
	Shifter shifter;
	
	/**
	 * Constructor for the DrivingDevices class
	 * @param talon_BR_ID The ID of the talon connected to the back-right motor
	 * @param motor_BL_ID The ID of the talon connected to the back-left motor
	 * @param motor_FR_ID The ID of the talon connected to the front-right motor
	 * @param motor_FL_ID The ID of the talon connected to the front-left motor
	 * @param sol_shifter_port The port where the solenoid that controls gear-shifting is plugged in to on the PCM
	 */
	public DrivingDevices(int talon_BR_ID, int talon_BL_ID, int talon_FR_ID, int talon_FL_ID, int sol_shifter_port, 
			int left_encoder_port_a, int left_encoder_port_b, int right_encoder_port_a, int right_encoder_port_b)
	{
		talon_BR = new CANTalon(talon_BR_ID);
		talon_BL = new CANTalon(talon_BL_ID);
		talon_FR = new CANTalon(talon_FR_ID);
		talon_FL = new CANTalon(talon_FL_ID);
		
		leftEncoder = new Encoder(left_encoder_port_a, left_encoder_port_b);
		rightEncoder = new Encoder(right_encoder_port_a, right_encoder_port_b);
		
		theRobot = new RobotDrive(talon_FL, talon_BL, talon_FR, talon_BR);
		
		shifter = new Shifter(sol_shifter_port);
	}
	
	/**
	 * Method used for driving the robot with a single joystick.
	 * @param stick The joystick used to drive the robot.
	 */
	public void driveWithJoystick(Joystick stick)
	{
		theRobot.arcadeDrive(stick);
	}
	
	/**
	 * Method that shifts the robot into high gear
	 */
	public void setHighGear()
	{
		shifter.setHighGear();
	}
	
	/**
	 * Method that shifts the robot into low gear
	 */
	public void setLowGear()
	{
		shifter.setLowGear();
	}
	
	public Encoder getLeftEncoder()
	{
		return leftEncoder;
	}
	
	public Encoder getRightEncoder()
	{
		return rightEncoder;
	}
}