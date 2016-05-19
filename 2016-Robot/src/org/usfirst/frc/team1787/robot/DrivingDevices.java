package org.usfirst.frc.team1787.robot;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is a collection of all the mechanisms used for driving the robot.
 * These include the talons that control the driving motors, the solenoid that controls the gear-shifter, 
 * the encoders, and the gyro.
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
	public static final double LEFT_ENCODER_DISTANCE_PER_PULSE = 0.000114220445460;
	/** The amount of degrees that is equivalent to 1 tick on the left encoder when turning right. */
	public static final double LEFT_ENCODER_DEGREES_PER_PULSE_RIGHT_TURN = 0.007936022752788;
	/** The amount of degrees that is equivalent to 1 tick on the left encoder when turning left. */
	public static final double LEFT_ENCODER_DEGREES_PER_PULSE_LEFT_TURN = 0.0075;
	
	/** The encoder on the right side of the robot */
	private Encoder rightEncoder;
	/** The distance, in feet, that is equivalent to 1 tick on the right encoder */
	public static final double RIGHT_ENCODER_DISTANCE_PER_PULSE = 0.000113327289211;
	/** The amount of degrees that is equivalent to 1 tick on the right encoder when turning right. */
	public static final double RIGHT_ENCODER_DEGREES_PER_PULSE_RIGHT_TURN = 0.007091687581379;
	/** The amount of degrees that is equivalent to 1 tick on the right encoder when turning left. */
	public static final double RIGHT_ENCODER_DEGREES_PER_PULSE_LEFT_TURN = 0.0075;
	
	/* 
	 * Initial testing indicates 35060 encoder ticks per big wheel revolution. The big ass wheels are 15 inches in diameter. 
	 * ((15 * pi) inches/1 revolution) * (1 foot/12 inches) * (1 revolution/35060 ticks) = (15 * pi)/(12 * 35060) feet per tick.
	 * (15 * pi)/(12 * 35060) = 0.00011200772. 
	 */
	
	// Shifter
	/** The Shifter object that controls which gear the robot is in. */
	Shifter shifter;
	
	//Gyro
	/** The gyro. */
	Gyro gyro;
	
	/**
	 * Constructor for the DrivingDevices class
	 * @param talon_BR_ID The ID of the talon connected to the back-right motor.
	 * @param motor_BL_ID The ID of the talon connected to the back-left motor.
	 * @param motor_FR_ID The ID of the talon connected to the front-right motor.
	 * @param motor_FL_ID The ID of the talon connected to the front-left motor.
	 * @param sol_shifter_port The port on the PCM where the solenoid that controls the shifter is connected.
	 * @param left_encoder_port_a The DIO port on the roborio where the left encoder's A channel is connected.
	 * @param left_encoder_port_b The DIO port on the roborio where the left encoder's B channel is connected.
	 * @param right_encoder_port_a The DIO port on the roborio where the right encoder's A channel is connected.
	 * @param right_encoder_port_b The DIO port on the roborio where the right encoder's B channel is connected.
	 * @param gyro_port The Analog port on the roborio where the gyro is connected.
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
		leftEncoder.setDistancePerPulse(LEFT_ENCODER_DISTANCE_PER_PULSE);
		rightEncoder = new Encoder(right_encoder_port_a, right_encoder_port_b, true);
		rightEncoder.setDistancePerPulse(RIGHT_ENCODER_DISTANCE_PER_PULSE);
		
		shifter = new Shifter(sol_shifter_port);
		
		gyro = new AnalogGyro(gyro_port);
		gyro.calibrate();
	}
	
	/**
	 * Method used for intuitively driving the robot when the pickup arm in front using a single joystick.
	 * @param stick The joystick used to drive the robot.
	 */
	public void arcadeDrivePickupArmInFront(Joystick stick)
	{
		theRobot.arcadeDrive(stick.getY(), stick.getX() * 0.85, false);
	}
	
	/**
	 * Method used for intuitively driving the robot when the wedge is in the front using a single joystick.
	 * @param stick The joystick used to drive the robot.
	 */
	public void arcadeDriveWedgeInFront(Joystick stick)
	{
		theRobot.arcadeDrive(-stick.getY(), stick.getX() * 0.85, false);
	}
	
	/**
	 * Method that allows manual input of motion values. Used for autonomous.
	 * @param moveValue The value to use for moving forwards(positive value) or backwards(negative value)
	 * @param rotateValue The value to use for rotating right(positive value) or left(negative value)
	 */
	public void arcadeDriveCustomValues(double moveValue, double rotateValue)
	{
		theRobot.arcadeDrive(-moveValue, rotateValue);
		/* Q: Why is moveValue negative?
		 * A: Apparently, a negative moveValue will move the robot forward when using arcadeDrive().
		 * This is likely because joysticks, or at least the ones we're using, output a negative y value when pushed forward.
		 * We wanted arcadeDriveUsingValues() to move the robot forward when given a positive move value 
		 * because it seemed more intuitive, so we use -moveValue within the method to accomplish that.
		 */
	}
	
	/**
	 * Stops the robot.
	 */
	public void stop()
	{
		theRobot.arcadeDrive(0, 0);
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
	 * Used to track motion of the robot.
	 * @param distance The distance to check.
	 * A positive value indicates motion forward.
	 * A negative value indicates motion backward.
	 * @return If, according to the encoders, the robot has driven the given distance.
	 */
	public boolean hasDrivenDistance(double distance)
	{
		if (distance > 0)
			return (leftEncoder.getDistance() >= distance && rightEncoder.getDistance() >= distance);
		else if (distance < 0)
			return (leftEncoder.getDistance() <= distance && rightEncoder.getDistance() <= distance);
		else
			return true;
	}
	
	/**
	 * Gets the degree reading from the left encoder when turning right.
	 * @return
	 */
	public double getLeftEncoderDegreesRightTurn()
	{
		return (leftEncoder.get() * LEFT_ENCODER_DEGREES_PER_PULSE_RIGHT_TURN);
	}
	
	/**
	 * Gets the degree reading from the left encoder when turning left.
	 * @return
	 */
	public double getLeftEncoderDegreesLeftTurn()
	{
		return (leftEncoder.get() * LEFT_ENCODER_DEGREES_PER_PULSE_LEFT_TURN);
	}
	
	/**
	 * Gets the degree reading from the right encoder when turning right.
	 * @return
	 */
	public double getRightEncoderDegreesRightTurn()
	{
		return (rightEncoder.get() * RIGHT_ENCODER_DEGREES_PER_PULSE_RIGHT_TURN);
	}
	
	/**
	 * Gets the degree reading from the right encoder when turning left.
	 * @return
	 */
	public double getRightEncoderDegreesLeftTurn()
	{
		return (rightEncoder.get() * RIGHT_ENCODER_DEGREES_PER_PULSE_RIGHT_TURN);
	}
	
	/**
	 * Used to track turning motion.
	 * @param degrees The amount of degrees to check. Positive value for turning right, negative value for turning left.
	 * @return If the robot has turned the given amount of degrees.
	 */
	public boolean hasTurnedDegreesWithEncoders(double degrees)
	{	
		if (degrees > 0) // If turning right
			return (getLeftEncoderDegreesRightTurn() >= degrees && getRightEncoderDegreesRightTurn() <= -degrees);
		else if (degrees < 0) // If turning left
			return (getLeftEncoderDegreesLeftTurn() <= degrees && getRightEncoderDegreesLeftTurn() >= -degrees);
		else
			return true;	
	}
	
	/**
	 * Shifts the robot into high gear.
	 */
	public void setHighGear()
	{
		shifter.setHighGear();
	}
	
	/**
	 * Shifts the robot into low gear.
	 */
	public void setLowGear()
	{
		shifter.setLowGear();
	}
	
	/**
	 * Getter method for the gyro.
	 * @return The gyro.
	 */
	public Gyro getGyro()
	{
		return gyro;
	}
	
	public boolean hasTurnedDegrees(double degrees)
	{
		if (degrees > 0)
			return (gyro.getAngle() >= degrees);
		else if (degrees < 0)
			return (gyro.getAngle() <= degrees);
		else
			return true;
	}
	
	/**
	 * Resets the gyro to a heading of 0, and the encoders to a distance of 0.
	 */
	public void resetEncodersAndGyro()
	{
		resetEncoders();
		gyro.reset();
	}
	
	/**
	 * Puts data related to driving on the SmartDashboard.
	 */
	public void putDataOnSmartDashboard()
	{
		if (shifter.getCurrentGear())
			SmartDashboard.putString("Gear:", " High");
		else
			SmartDashboard.putString("Gear:", " Low");
		SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
		//SmartDashboard.putNumber("Left Encoder Ticks", leftEncoder.get());
		//SmartDashboard.putNumber("Left Encoder Distance", leftEncoder.getDistance());
		//SmartDashboard.putNumber("Left Encoder Degrees (Right Turn)", getLeftEncoderDegreesRightTurn());
		//SmartDashboard.putNumber("Left Encoder Degrees (Left Turn)", getLeftEncoderDegreesLeftTurn());
		//SmartDashboard.putNumber("Right Encoder Ticks", rightEncoder.get());
		//SmartDashboard.putNumber("Right Encoder Distance", rightEncoder.getDistance());
		//SmartDashboard.putNumber("Right Encoder Degrees (Right Turn)", getRightEncoderDegreesRightTurn());
		//SmartDashboard.putNumber("Right Encoder Degrees (Left Turn)", getRightEncoderDegreesLeftTurn());
	}
}