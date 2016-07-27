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
	private boolean imageProcessingSettingsActive;
	
	Range HUE = new Range(125, 145);
	Range SATURATION = new Range(245, 255);
	Range VALUE = new Range(30, 175);

	public VisionMethods (String camFrontName, String camSideName)
	{
		camServer = CameraServer.getInstance();
		camServer.setQuality(50);
		
		camFront = new USBCamera(camFrontName);
		camSide = new USBCamera(camSideName);
		
		camSide.setExposureManual(0);
		camSide.setWhiteBalanceManual(USBCamera.WhiteBalance.kFixedIndoor);
		camSide.setBrightness(100);
		camSide.updateSettings();
		imageProcessingSettingsActive = true;
		
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
	
	public void setHSVThreshold(int hMin, int hMax, int sMin, int sMax, int vMin, int vMax)
	{
		HUE.minValue = hMin;
		HUE.maxValue = hMax;
		SATURATION.minValue = sMin;
		SATURATION.maxValue = sMax;
		VALUE.minValue = vMin;
		VALUE.maxValue = vMax;
	}
	
	public boolean imageProcessingIsActive()
	{
		return imageProcessingActive;
	}
	
	public void toggleCamSettings()
	{
		if (imageProcessingSettingsActive)
		{
			camSide.setExposureAuto();
			camSide.setWhiteBalanceAuto();
			camSide.setBrightness(50);
		}
		else if (!imageProcessingSettingsActive)
		{
			camSide.setExposureManual(0);
			camSide.setWhiteBalanceManual(USBCamera.WhiteBalance.kFixedIndoor);
			camSide.setBrightness(100);
		}
		camSide.updateSettings();
		imageProcessingSettingsActive = !imageProcessingSettingsActive;
	}
}