package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is a collection of all the mechanisms used for driving the robot.
 * These include the talons that control the driving motors, the solenoid that controls the gear-shifter, and the encoders.
 * This class was made to organize the code for these devices in one place 
 * so the Robot class can have one object that controls all aspects of driving.
 * @author David Miron
 * @author Simon Wieder
 */
public class DrivingDevices
{
	// Driving
	/** The Talon connected to the back-right motor */
	private CANTalon talon_BR;
	/** The Talon connected to the back-left motor */
	private CANTalon talon_BL;
	/** The Talon connected to the front-right motor */
	private CANTalon talon_FR;
	/** The Talon connected to the front-left motor */
	private CANTalon talon_FL;
	/** The RobotDrive object used for basic driving */
	RobotDrive theRobot;
	
	// Encoders
	/** The encoder on the left side of the robot */
	private Encoder leftEncoder;
	/** The encoder on the right side of the robot */
	private Encoder rightEncoder;
	
	// Shifter
	/** The Shifter object that controls which gear the robot is in */
	Shifter shifter;
	
	/**
	 * Constructor for the DrivingDevices class
	 * @param talon_BR_ID The ID of the Talon connected to the back-right motor
	 * @param motor_BL_ID The ID of the Talon connected to the back-left motor
	 * @param motor_FR_ID The ID of the Talon connected to the front-right motor
	 * @param motor_FL_ID The ID of the Talon connected to the front-left motor
	 * @param sol_shifter_port The port on the PCM where the solenoid that controls gear-shifting is connected
	 * @param left_encoder_port_a
	 * @param left_encoder_port_b
	 * @param right_encoder_port_a
	 * @param right_encoder_port_b
	 */
	public DrivingDevices(int talon_BR_ID, int talon_BL_ID, int talon_FR_ID, int talon_FL_ID, int sol_shifter_port, 
			int left_encoder_port_a, int left_encoder_port_b, int right_encoder_port_a, int right_encoder_port_b)
	{
		talon_BR = new CANTalon(talon_BR_ID);
		talon_BL = new CANTalon(talon_BL_ID);
		talon_FR = new CANTalon(talon_FR_ID);
		talon_FL = new CANTalon(talon_FL_ID);
		theRobot = new RobotDrive(talon_FL, talon_BL, talon_FR, talon_BR);
		
		/* Initial testing indicates 35060 encoder ticks per wheel revolution.
		Therefore, if distance is in revolutions, then distancePerPulse = 1/35060 = 0.00002852253 */
		
		/* COMMENT OUT ENCODERS WHEN THEY ARE NOT WIRED OR ELSE ROBOT WON'T DETECT CODE! */
		//leftEncoder = new Encoder(left_encoder_port_a, left_encoder_port_b);
		//rightEncoder = new Encoder(right_encoder_port_a, right_encoder_port_b);
		//leftEncoder.setDistancePerPulse(0.00002852253);
		
		shifter = new Shifter(sol_shifter_port);
	}
	
	/**
	 * Method used for driving the robot with a single joystick.
	 * @param stick The joystick used to drive the robot.
	 */
	public void arcadeDriveWithPickupArmInFront(Joystick stick)
	{
		theRobot.arcadeDrive(stick.getY(), stick.getX());
	}
	
	/**
	 * Method used for driving the robot with a single joystick.
	 * @param stick The joystick used to drive the robot.
	 */
	public void arcadeDriveWithWedgeInFront(Joystick stick)
	{
		theRobot.arcadeDrive(-stick.getY(), stick.getX());
	}
	
	public void arcadeDriveUsingValues(double moveValue, double rotateValue)
	{
		theRobot.arcadeDrive(moveValue, rotateValue);
	}
	
	public void tankDriveUsingValues(double leftValue, double rightValue)
	{
		theRobot.tankDrive(leftValue, rightValue);
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
	
	/**
	 * Getter method for the left encoder.
	 * @return The left encoder.
	 */
	public Encoder getLeftEncoder()
	{
		return leftEncoder;
	}
	
	/**
	 * Getter method for the right encoder.
	 * @return The right encoder.
	 */
	public Encoder getRightEncoder()
	{
		return rightEncoder;
	}
	
	public void putDataOnSmartDashboard()
	{
		SmartDashboard.putBoolean("Current Gear (true = high, false = low)", shifter.getCurrentGear());
		//SmartDashboard.putNumber("Left Encoder Ticks", leftEncoder.get());
		//SmartDashboard.putNumber("Right Encoder Ticks", rightEncoder.get());
	}
}