package org.usfirst.frc.team1787.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Range;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class VisionMethods
{
	private CameraServer camServer;
	private USBCamera cam;
	private Image img;
	private Image binaryImg;
	
	Range HUE = new Range(73, 162);
	Range SATURATION = new Range(67, 255);
	Range VALUE = new Range(0, 184);

	public VisionMethods (String camName)
	{
		camServer = CameraServer.getInstance();
		cam = new USBCamera(camName);
		cam.startCapture();
		img = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		binaryImg = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_U8, 0);
	}
	
	public void method1()
	{
		cam.getImage(img);
		
		NIVision.imaqColorThreshold(binaryImg, img, 255, NIVision.ColorMode.HSV, HUE, SATURATION, VALUE);
		
		camServer.setImage(binaryImg);
	}
}