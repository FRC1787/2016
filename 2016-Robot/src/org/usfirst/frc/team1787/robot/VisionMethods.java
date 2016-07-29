package org.usfirst.frc.team1787.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.MeasurementType;
import com.ni.vision.NIVision.Range;
import com.ni.vision.NIVision.Rect;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.USBCamera;

/*
 * Notes to self: 
 * NIVision can perform lots of different "filters" on images.
 * In order to filter an image in NIVision, you use the "imaqParticleFilter" method.
 * (In the code, it's actually "imaqParticleFilter4". Idk where the 4 came from, or where 1-3 are. It just is that way it seems)
 * This method has a couple parameters that might be confusing at first, but I think I've deduced their purpose.
 * 
 * 1) Image dest - This is the image object where the filtered image will be placed.
 * 
 * 2) Image source - This is the image object that the filter is performed on.
 * 
 * 3) ParticleFilterCriteria2[] criteria - I guessed at this parameter's purpose based on it's structure (i.e. the ParticleFilterCritera2 Class)
 * The ParticleFilterCriteria2 Class has a few interesting variables as a part of it. These include:
 * 		a) MeasurementType parameter - The 
 * 		b) float lower
 * 		c) float upper
 * 		d) int calibrated
 * 		e) int exclude
 * 
 * 4) ParticleFilterOptions
 * 
 * 5) ROI roi = ROI stands for Region of Interest. I'd assume that if one is given, 
 * then the particle function will only examine the parts of the image that are specified by the ROI object.
 * 
 * How I think it all works:
 * 
 * The imaqParticleFilter method starts with what it has identified as the 1st particle (I'm unsure how particles are exactly identified at this point).
 * It then goes to it's list (array) of criteria to check. It performs a measurement according to the MeasurementType set in each criteria object, 
 * and then checks to see if the value obtained from that measurement is within the bounds also established in the respective criteria object 
 * (i.e. checks if (criteria.lower < measurement < criteria.upper) for each criteria in the array). The filter then alters the image accordingly.
 * A filter gives no information about an image, only alters it. Information is gotten from the measureParticle method.
 * The measureParticle method also utilizes the constants in the MeasurementType Class to specify what aspect of the particle should be measured.
 * 
 * The difference between a filter and a threshold might be that a threshold always results in a binary image, but a filter doesn't have to.
 * 
 * Other areas to look into:
 * 1) Overlays
 */

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

	Rect boundingRectangle;
	
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
		
		boundingRectangle = new Rect(20, 20, 100, 100);
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
	
	public void updateAndDrawBoundingRectangle()
	{
		if (NIVision.imaqCountParticles(binaryImg, 1) > 0)
		{
			boundingRectangle.top = (int) NIVision.imaqMeasureParticle(binaryImg, 0, 0, MeasurementType.MT_BOUNDING_RECT_TOP);
			boundingRectangle.left = (int) NIVision.imaqMeasureParticle(binaryImg, 0, 0, MeasurementType.MT_BOUNDING_RECT_LEFT);
			boundingRectangle.width = (int) NIVision.imaqMeasureParticle(binaryImg, 0, 0, MeasurementType.MT_BOUNDING_RECT_WIDTH);
			boundingRectangle.height = (int) NIVision.imaqMeasureParticle(binaryImg, 0, 0, MeasurementType.MT_BOUNDING_RECT_HEIGHT);
			NIVision.imaqDrawShapeOnImage(binaryImg, binaryImg, boundingRectangle, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 500.0f);
		}
	}
	
	public void sendProcessedImageToDashboard()
	{
		updateAndDrawBoundingRectangle();
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