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
	/** The camera server object used to sent images to the smart dashboard. */
	private CameraServer camServer;
	/** The camera mounted on the front of the pickup arm. */
	private USBCamera camFront;
	/** The camera mounted on the side of the robot, or on the 2 axis servo base. */
	private USBCamera camSide;
	/** The image object that stores an image captured by a camera. */
	private Image img;
	/** The binary image used for vision processing. */
	private Image binaryImg;
	/** A boolean indicating if the front camera is currently active. */
	private boolean frontCamActive;
	/** A boolean indicating if methods involving vision processing are being called. */
	private boolean imageProcessingActive;
	/** A boolean indicating if the side-cam's exposure, white balance, and brightness are currently optimal for vision processing. */
	private boolean imageProcessingSettingsActive;
	
	/** The Range object which stores the acceptable range of hues for vision processing. (hue is on a scale from 0 - 360). */
	private final Range HUE;
	/** The minimum hue. */
	private final int HUE_MIN = 125;
	/** The maximum hue. */
	private final int HUE_MAX = 145;
	/** The Range object which stores the acceptable range of saturations for vision processing. (saturation is on a scale from 0 - 255). */
	private final Range SATURATION;
	/** The minimum saturation. */
	private final int SATURATION_MIN = 245;
	/** The maximum saturation. */
	private final int SATURATION_MAX = 255;
	/** The Range object which stores the acceptable range of values (as in the "v" in HSV) for vision processing. (value is on a scale from 0 - 255). */
	private final Range VALUE;
	/** The minimum value (as in the "v" in HSV). */
	private final int VALUE_MIN = 30;
	/** The maximum value (as in the "v" in HSV). */
	private final int VALUE_MAX = 175;

	/** The rectangle that completely surrounds a particle in a binary image. */
	Rect boundingRectangle;
	
	private final int IMAGE_WIDTH_IN_PIXELS = 5;
	private final int IMAGE_HEIGHT_IN_PIXELS = 5;
	
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
		
		HUE = new Range(HUE_MIN, HUE_MAX);
		SATURATION = new Range(SATURATION_MIN, SATURATION_MAX);
		VALUE = new Range(VALUE_MIN, VALUE_MAX);
		
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
		if (aParticleIsPresent())
		{
			boundingRectangle.top = (int) NIVision.imaqMeasureParticle(binaryImg, 0, 0, MeasurementType.MT_BOUNDING_RECT_TOP);
			boundingRectangle.left = (int) NIVision.imaqMeasureParticle(binaryImg, 0, 0, MeasurementType.MT_BOUNDING_RECT_LEFT);
			boundingRectangle.width = (int) NIVision.imaqMeasureParticle(binaryImg, 0, 0, MeasurementType.MT_BOUNDING_RECT_WIDTH);
			boundingRectangle.height = (int) NIVision.imaqMeasureParticle(binaryImg, 0, 0, MeasurementType.MT_BOUNDING_RECT_HEIGHT);
			NIVision.imaqDrawShapeOnImage(binaryImg, binaryImg, boundingRectangle, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 500.0f);
		}
	}
	
	public int getCenterOfMassX()
	{
		if (aParticleIsPresent())
			return (int) NIVision.imaqMeasureParticle(binaryImg, 0, 0, MeasurementType.MT_CENTER_OF_MASS_X);
		else
			return -1;
	}
	
	public int getCenterOfMassY()
	{
		if (aParticleIsPresent())
			return (int) NIVision.imaqMeasureParticle(binaryImg, 0, 0, MeasurementType.MT_CENTER_OF_MASS_Y);
		else
			return -1;
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
	
	public boolean aParticleIsPresent()
	{
		return (NIVision.imaqCountParticles(binaryImg, 1) > 0);
	}
}