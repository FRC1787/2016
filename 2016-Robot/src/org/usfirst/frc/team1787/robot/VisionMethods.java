package org.usfirst.frc.team1787.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Range;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class VisionMethods
{
	private CameraServer camServer;
	private USBCamera camFront;
	private USBCamera camSide;
	private Image img;
	private Image binaryImg;
	private boolean frontCamActive;
	private boolean imageProcessingActive;
	
	Range HUE = new Range(90, 120);
	Range SATURATION = new Range(0, 255);
	Range VALUE = new Range(0, 255);

	public VisionMethods (String camFrontName, String camSideName)
	{
		camServer = CameraServer.getInstance();
		camServer.setQuality(100);
		
		camFront = new USBCamera(camFrontName);
		camSide = new USBCamera(camSideName);
		
		camSide.setExposureManual(0);
		camSide.setExposureHoldCurrent();
		camSide.setBrightness(0);
		camSide.updateSettings();
		
		camFront.startCapture();
		frontCamActive = true;
		
		img = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		binaryImg = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_U8, 0);
		
		imageProcessingActive = false;
	}
	
	public Image getImageFromActiveCam()
	{
		if (frontCamActive)
			camFront.getImage(img);
		else if (!frontCamActive)
			camSide.getImage(img);
		return img;
	}
	
	public void toggleActiveCamFeed()
	{
		if (frontCamActive)
		{
			camFront.stopCapture();
			camSide.startCapture();
		}
		else if (!frontCamActive)
		{
			camSide.stopCapture();
			camFront.startCapture();
		}
		
		frontCamActive = !frontCamActive;
	}
	
	public void sendRegularImageToDashboard()
	{
		camServer.setImage(getImageFromActiveCam());
	}
	
	public void toggleImageProcessing()
	{
		imageProcessingActive = !imageProcessingActive;
	}
	
	public void performHSVFilter()
	{
		NIVision.imaqColorThreshold(binaryImg, getImageFromActiveCam(), 255, NIVision.ColorMode.HSV, HUE, SATURATION, VALUE);
	}
	
	public void sendProcessedImageToDashboard()
	{
		camServer.setImage(binaryImg);
	}
	
	public void setHueRange(int min, int max)
	{
		HUE.minValue = min;
		HUE.maxValue = max;
	}
	
	public void setSaturationRange(int min, int max)
	{
		SATURATION.minValue = min;
		SATURATION.maxValue = max;
	}
	
	public void setValueRange(int min, int max)
	{
		VALUE.minValue = min;
		VALUE.maxValue = max;
	}
	
	public boolean imageProcessingIsActive()
	{
		return imageProcessingActive;
	}
}