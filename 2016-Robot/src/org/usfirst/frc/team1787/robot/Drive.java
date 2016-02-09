package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.CANTalon;

public class Drive {
	
	private int motor_BR_ID;
	private int motor_BL_ID;
	private int motor_FR_ID;
	private int motor_FL_ID;
	
	private CANTalon drive_BR;
	private CANTalon drive_BL;
	private CANTalon drive_FR;
	private CANTalon drive_FL;
	
	public Drive(int motor_BR_ID, int motor_BL_ID, int motor_FR_ID, int motor_FL_ID)
	{
		this.motor_BR_ID = motor_BR_ID;
		this.motor_BL_ID = motor_BL_ID;
		this.motor_FR_ID = motor_FR_ID;
		this.motor_FL_ID = motor_FL_ID;
		
		drive_BR = new CANTalon(this.motor_BR_ID);
		drive_BL = new CANTalon(this.motor_BL_ID);
		drive_FR = new CANTalon(this.motor_FR_ID);
		drive_FL = new CANTalon(this.motor_FL_ID);
	}
	
}
