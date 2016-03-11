package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
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
	/** The distance, in feet, that is equivalent to 1 tick on the left encoder */
	public static final double LEFT_ENCODER_DISTANCE_PER_PULSE = 0.00134409269;
	/** The encoder on the right side of the robot */
	private Encoder rightEncoder;
	/** The distance, in feet, that is equivalent to 1 tick on the right encoder */
	public static final double RIGHT_ENCODER_DISTANCE_PER_PULSE = 0.00134409269;
	
	/* 
	 * Initial testing indicates 35060 encoder ticks per wheel revolution. 1 revolution = (15 * pi) feet. 
	 * Therefore, if distance is in feet, then distancePerPulse = (feet/revolution) * (revolution/ticks).
	 * (15 * pi) / 35060 = 0.00134409269 
	 */
	
	// Shifter
	/** The Shifter object that controls which gear the robot is in */
	Shifter shifter;
	
	//Gyro
	/** The gyro */
	//Gyro gyro;
	
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
			int left_encoder_port_a, int left_encoder_port_b, int right_encoder_port_a, int right_encoder_port_b, int gyro_port)
	{
		talon_BR = new CANTalon(talon_BR_ID);
		talon_BL = new CANTalon(talon_BL_ID);
		talon_FR = new CANTalon(talon_FR_ID);
		talon_FL = new CANTalon(talon_FL_ID);
		theRobot = new RobotDrive(talon_FL, talon_BL, talon_FR, talon_BR);
		
		/* COMMENT OUT ENCODERS WHEN THEY ARE NOT WIRED OR ELSE ROBOT WON'T DETECT CODE! */
		leftEncoder = new Encoder(left_encoder_port_a, left_encoder_port_b, false);
		//leftEncoder.setDistancePerPulse(LEFT_ENCODER_DISTANCE_PER_PULSE);
		rightEncoder = new Encoder(right_encoder_port_a, right_encoder_port_b, false);
		//rightEncoder.setDistancePerPulse(RIGHT_ENCODER_DISTANCE_PER_PULSE);
		
		shifter = new Shifter(sol_shifter_port);
		
		//gyro = new AnalogGyro(gyro_port);
		//resetGyro();
	}
	
	/**
	 * Method used for intuitively driving the robot when the pickup arm in front using a single joystick.
	 * @param stick The joystick used to drive the robot.
	 */
	public void arcadeDriveWithPickupArmInFront(Joystick stick)
	{
		theRobot.arcadeDrive(stick.getY(), stick.getX());
	}
	
	/**
	 * Method used for intuitively driving the robot when the wedge is in the front using a single joystick.
	 * @param stick The joystick used to drive the robot.
	 */
	public void arcadeDriveWithWedgeInFront(Joystick stick)
	{
		theRobot.arcadeDrive(-stick.getY(), stick.getX());
	}
	
	/**
	 * Method that allows manual input of motion values. Used for autonomous.
	 * @param moveValue The value to use for moving forwards(positive value) or backwards(negative value)
	 * @param rotateValue The value to use for rotating right(positive value) or left(negative value)
	 */
	public void arcadeDriveUsingValues(double moveValue, double rotateValue)
	{
		theRobot.arcadeDrive(moveValue, rotateValue);
	}
	
	/**
	 * Stops the robot
	 */
	public void stop()
	{
		theRobot.arcadeDrive(0, 0);
	}
	
	/**
	 * Shifts the robot into high gear
	 */
	public void setHighGear()
	{
		shifter.setHighGear();
	}
	
	/**
	 * Shifts the robot into low gear
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
	
	/**
	 * Resets both encoders.
	 */
	public void resetEncoders()
	{
		leftEncoder.reset();
		rightEncoder.reset();
	}
	
	/** 
	 * Used to track forward motion.
	 * @param distance The distance to check
	 * @return If both encoders read greater than the given distance.
	 */
	public boolean bothEncodersReadGreaterThan(double distance)
	{
		return (leftEncoder.getDistance() > distance && rightEncoder.getDistance() > distance);
	}
	
	/**
	 * Used to track backward motion.
	 * @param distance The distance to check
	 * @return If both encoders read less than the given distance.
	 */
	public boolean bothEncodersReadLessThan(double distance)
	{
		return (leftEncoder.getDistance() < distance && rightEncoder.getDistance() < distance);
	}
	/*
	/**
	 * Getter method for the gyro angle.
	 * @return The gyro angle.
	 *
	public double getGyroAngle()
	{
		return gyro.getAngle();
	}
	
	/**
	 * Resets the gyro to a heading of 0
	 *
	public void resetGyro()
	{
		gyro.reset();
	}*/
	
	/**
	 * Resets the gyro to a heading of 0, and the encoders to a distance of 0.
	 */
	public void resetEncodersAndGyro()
	{
		resetEncoders();
		//resetGyro();
	}
	
	/**
	 * Puts data related to driving on the SmartDashboard.
	 */
	public void putDataOnSmartDashboard()
	{
		SmartDashboard.putBoolean("Current Gear (true = high, false = low)", shifter.getCurrentGear());
		//SmartDashboard.putNumber("Gyro Angle", getGyroAngle());
		//SmartDashboard.putNumber("Left Encoder Ticks", leftEncoder.get());
		//SmartDashboard.putNumber("Left Encoder Distance", leftEncoder.getDistance());
		//SmartDashboard.putNumber("Right Encoder Ticks", rightEncoder.get());
		//SmartDashboard.putNumber("Right Encoder Distance", rightEncoder.getDistance());
	}
}