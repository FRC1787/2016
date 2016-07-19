package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.vision.USBCamera;
import org.opencv.*;

public class VisionMethods
{
	private USBCamera cam;
	
	public VisionMethods (String camName)
	{
		cam = new USBCamera(camName);
		cam.openCamera();
		cam.startCapture();
	}
	
	public void method1()
	{
		
	}
}