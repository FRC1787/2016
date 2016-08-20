package org.usfirst.frc.team1787.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.MeasurementType;
import com.ni.vision.NIVision.ParticleFilterCriteria2;
import com.ni.vision.NIVision.ParticleFilterOptions2;
import com.ni.vision.NIVision.Point;
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
 * 		Looked into Overlays, and it appears either I couldn't figure out how to use them or they just don't work.
 * 		One thing to note about the difference between overlays and drawings though. When using the NIVision draw method,
 * 		the shape gets drawn directly onto the image and becomes a part of the image as a result. An overlay is put onto an image,
 * 		but isn't a part of the image itself. Knowing this, I will put any drawing functions after everything I need to process
 * 		in an image has been processed, so the drawings don't interfere with the processing.
 * 
 * Note on connectivity8:
 * There are a couple of methods that have an int connectivity8 as a parameter. I figured out what that's for today.
 * When detecting particles, it connectivity8 is on, then pixels adjacent or diagonal to another pixel are considered to be part of the same particle.
 * The other form of connectivity, connectivity4, defines particles as groups of pixels that are only adjacent to each other, not touching diagonally.
 * Learned this from the NI Vision Concepts Help section called "Connectivity".
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
	/** A boolean indicating if the side-cam's exposure, white balance, and brightness are currently optimal for vision processing. */
	private boolean imageProcessingSettingsActive;
	
	/** The Range object which stores the acceptable range of hues for vision processing. (hue is on a scale from 0 - 360). */
	private final Range HUE = new Range(125, 145);
	/** The Range object which stores the acceptable range of saturations for vision processing. (saturation is on a scale from 0 - 255). */
	private final Range SATURATION = new Range(245, 255);
	/** The Range object which stores the acceptable range of values (as in the "v" in HSV) for vision processing. (value is on a scale from 0 - 255). */
	private final Range VALUE = new Range(30, 175);

	private Rect boundingBox = new Rect();
	private Rect centerCircle = new Rect(0, 0, 11, 11);
	
	private final int IMAGE_WIDTH_IN_PIXELS = 320;
	private final int IMAGE_HEIGHT_IN_PIXELS = 240;
	
	private Point horizontalStart = new Point(0, 120);
	private Point horizontalEnd = new Point(320, 120);
	private Point verticalStart = new Point(160, 0);
	private Point verticalEnd = new Point(160, 240);
	Point centerOfImage = new Point(160, 120);

	/** The particleID for the particle that has been confirmed to be a goal and is currently being tracked. */
	private int currentParticle = -1;
	/** The particleID for the largest particle currently in view. This particle hasn't necessarily been confirmed to be a goal. */
	private int largestParticle = -1;
	
	private final double DESIRED_AREA_TO_BOUNDING_BOX_AREA_RATIO = 0.33; // (Area of target (80 in^2) / Area of "bounding box" (240 in^2)) = 1/3
	private final double MIN_AREA_SCORE = 0.65; // determined through testing
	private final double MAX_AREA_SCORE = 1.35; // determined through testing
	
	private final double DESIRED_ASPECT_RATIO = 20; 
	/* Aspect ratio is determined using the equvalent rectangle, and is calculated as width/height. 
	The zeroes of this parabola --> 2x^2 + 2(particleArea) - (particlePerimeter)x <-- are are the side lengths of the equivalent rectangle.
	We'll call the long side the width, and the short side the height.
	Performing the calculation using the target's area (80 in^2) and the target's perimeter (84 in^2), yield a equivalent rectangle with width 40in and height 2in.
 	The desired aspect ratio is therefore 40/2 = 20. */
	private final double MIN_ASPECT_RATIO_SCORE = 0.75; // determined through testing
	private final double MAX_ASPECT_RATIO_SCORE = 1.25; // determined through testing
	
	ParticleFilterCriteria2[] filterCriteria = new ParticleFilterCriteria2[1]; // We only filter based on one criteria: area.
	ParticleFilterOptions2 filterOptions = new ParticleFilterOptions2(0,0,1,1); // Don't reject matches, don't reject the border, fill holes, and use connectivity8.
	
	private final double DEGREES_PER_PIXEL_HORIZONTAL_NI = 0.17125;
	private final double DEGREES_PER_PIXEL_HORIZONTAL_DATA_SHEET = 0.046643;
	private double DEGREES_PER_PIXEL_HORIZONTAL_GUESS = 0.15; // determined through testing. May not be super accurate, but works better than the others right now.
	
	private final double DEGREES_PER_PIXEL_VERTICAL_NI = 0.17125;
	private final double DEGREES_PER_PIXEL_VERTICAL_DATA_SHEET = 0.046643;
	private double DEGREES_PER_PIXEL_VERTICAL_GUESS = 0.15; // determined though testing. May not be super accurate, but works better than the others right now. Unsure if this being the same as the horizontal ratio is a coincidence or not.
	
	private double CONTROL_LOOP_DAMPENER = 0.83; // Value determined through testing.
	
	public VisionMethods(String camFrontName, String camSideName)
	{
		// set particle filter criteria
		filterCriteria[0] = new ParticleFilterCriteria2(MeasurementType.MT_AREA_BY_IMAGE_AREA, 0.1, 100.0, 0, 0);
		
		// set up CameraServer
		camServer = CameraServer.getInstance();
		camServer.setQuality(50);
		
		// construct the Image objects
		img = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		binaryImg = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_U8, 0);
		
		// construct cameras
		camFront = new USBCamera(camFrontName);
		camSide = new USBCamera(camSideName);
		
		// Set the exposure, white balance, and brightness of the side camera to be optimal for vision processing.
		camSide.setExposureManual(0);
		camSide.setWhiteBalanceManual(USBCamera.WhiteBalance.kFixedIndoor);
		camSide.setBrightness(100);
		camSide.updateSettings();
		imageProcessingSettingsActive = true;
		
		// Start capturing video from the front cam.
		camFront.startCapture();
		frontCamActive = true;
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
	
	public void sendRegularImageToDashboard()
	{
		camServer.setImage(getImageFromActiveCam());
	}
	
	public void sendProcessedImageToDashboard()
	{
		if (particleIsValid(currentParticle))
		{
			updateAndDrawCurrentParticleBoundingBox(binaryImg);
			updateAndDrawReticleOnCurrentParticle(binaryImg);
		}
		camServer.setImage(binaryImg);
	}
	
	public void sendHybridImageToDashboard()
	{
		if (particleIsValid(currentParticle))
		{
			updateAndDrawCurrentParticleBoundingBox(img);
			updateAndDrawReticleOnCurrentParticle(img);
		}
		camServer.setImage(img);
	}
	
	public int getNumOfParticles()
	{
		return NIVision.imaqCountParticles(binaryImg, 1);
	}
	
	public void performHSVFilter()
	{
		NIVision.imaqColorThreshold(binaryImg, getImageFromActiveCam(), 255, NIVision.ColorMode.HSV, HUE, SATURATION, VALUE);
	}
	
	public void removeSmallParticles()
	{
		NIVision.imaqParticleFilter4(binaryImg, binaryImg, filterCriteria, filterOptions, null);
	}
	
	public void updateCurrentParticleBoundingBox()
	{
		boundingBox.top = getBoundingBoxTop(currentParticle);
		boundingBox.left = getBoundingBoxLeft(currentParticle);
		boundingBox.height = getBoundingBoxHeight(currentParticle);
		boundingBox.width = getBoundingBoxWidth(currentParticle);
	}
	
	public void updateAndDrawCurrentParticleBoundingBox(Image frame)
	{
		updateCurrentParticleBoundingBox();
		NIVision.imaqDrawShapeOnImage(frame, frame, boundingBox, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 500.0f);
	}
	
	public void updateAndDrawReticleOnCurrentParticle(Image frame)
	{
		// calculate centerCircle
		centerCircle.top = getCenterOfMassY(currentParticle) - 5;
		centerCircle.left = getCenterOfMassX(currentParticle) - 5;
		
		// calculate vertical line
		verticalStart.x = getCenterOfMassX(currentParticle);
		verticalEnd.x = getCenterOfMassX(currentParticle);
		
		// calculate horizontal line
		horizontalStart.y = getCenterOfMassY(currentParticle);
		horizontalEnd.y = getCenterOfMassY(currentParticle);

		// draw circle and lines
		NIVision.imaqDrawLineOnImage(frame, frame, DrawMode.DRAW_VALUE, verticalStart, verticalEnd, 500.0f);
		NIVision.imaqDrawLineOnImage(frame, frame, DrawMode.DRAW_VALUE, horizontalStart, horizontalEnd, 500.0f);
		NIVision.imaqDrawShapeOnImage(frame, frame, centerCircle, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 500.0f);
	}
	
	public void findLargestParticle()
	{
		largestParticle = 0;
		for (int particleNumber = 0; particleNumber < getNumOfParticles(); particleNumber++)
		{
			if (getArea(particleNumber) > getArea(largestParticle))
				largestParticle = particleNumber;
		}
	}
	
	public boolean performAreaTest()
	{
		double areaToBoundingBoxAreaRatio = getRatioOfAreaToBoundingBoxArea(largestParticle);
		double areaScore = (areaToBoundingBoxAreaRatio / DESIRED_AREA_TO_BOUNDING_BOX_AREA_RATIO);
		//System.out.println("Area Score: "+areaScore);
		
		if (MIN_AREA_SCORE <= areaScore && areaScore <= MAX_AREA_SCORE)
		{
			//System.out.println("Particle #"+largestParticle+" passes the area test.");
			currentParticle = largestParticle;
			return true;
		}
		else
		{
			//System.out.println("Particle #"+largestParticle+" fails the area test.");
			return false;
		}
	}
	
	public boolean performAspectRatioTest()
	{
		double equivalentRectangleAspectRatio = getEquivalentRectangleAspectRatio(largestParticle);
		double aspectRatioScore = (equivalentRectangleAspectRatio / DESIRED_ASPECT_RATIO);
		//System.out.println("Score: "+aspectRatioScore);
		
		if (MIN_ASPECT_RATIO_SCORE <= aspectRatioScore && aspectRatioScore <= MAX_ASPECT_RATIO_SCORE)
		{
			//System.out.println("Particle #"+largestParticle+" passes the aspect ratio test.");
			currentParticle = largestParticle;
			return true;
		}
		else
		{
			//System.out.println("Particle #"+largestParticle+" fails the aspect ratio test.");
			return false;
		}
	}
	
	public int getArea(int particleID)
	{
		if (particleIsValid(particleID))
			return (int) NIVision.imaqMeasureParticle(binaryImg, particleID, 0, MeasurementType.MT_PARTICLE_AND_HOLES_AREA);
		else
			return -1;
	}
	
	public int getCenterOfMassX(int particleID)
	{
		if (particleIsValid(particleID))
			return (int) NIVision.imaqMeasureParticle(binaryImg, particleID, 0, MeasurementType.MT_CENTER_OF_MASS_X);
		else
			return -1;
	}
	
	public int getCenterOfMassY(int particleID)
	{
		if (particleIsValid(particleID))
			return (int) NIVision.imaqMeasureParticle(binaryImg, particleID, 0, MeasurementType.MT_CENTER_OF_MASS_Y);
		else
			return -1;
	}
	
	public int getBoundingBoxTop(int particleID)
	{
		if (particleIsValid(particleID))
			return (int) NIVision.imaqMeasureParticle(binaryImg, particleID, 0, MeasurementType.MT_BOUNDING_RECT_TOP);
		else
			return -1;
	}
	
	public int getBoundingBoxLeft(int particleID)
	{
		if (particleIsValid(particleID))
			return (int) NIVision.imaqMeasureParticle(binaryImg, particleID, 0, MeasurementType.MT_BOUNDING_RECT_LEFT);
		else
			return -1;
	}
	
	public int getBoundingBoxHeight(int particleID)
	{
		if (particleIsValid(particleID))
			return (int) NIVision.imaqMeasureParticle(binaryImg, particleID, 0, MeasurementType.MT_BOUNDING_RECT_HEIGHT);
		else
			return -1;
	}
	
	public int getBoundingBoxWidth(int particleID)
	{
		if (particleIsValid(particleID))
			return (int) NIVision.imaqMeasureParticle(binaryImg, particleID, 0, MeasurementType.MT_BOUNDING_RECT_WIDTH);
		else
			return -1;
	}
	
	public double getRatioOfAreaToBoundingBoxArea(int particleID)
	{
		if (particleIsValid(particleID))
			return NIVision.imaqMeasureParticle(binaryImg, particleID, 0, MeasurementType.MT_COMPACTNESS_FACTOR);
		else
			return -1;
	}
	
	public double getEquivalentRectangleAspectRatio(int particleID)
	{
		if (particleIsValid(particleID))
			return NIVision.imaqMeasureParticle(binaryImg, particleID, 0, MeasurementType.MT_RATIO_OF_EQUIVALENT_RECT_SIDES);
		else
			return -1;
	}
	
	public int getCurrentParticle()
	{
		return currentParticle;
	}
	
	public int getLargestParticle()
	{
		return largestParticle;
	}
	
	public boolean particleIsValid(int particleID)
	{
		return (0 <= particleID && particleID < getNumOfParticles());
	}
	
	public double getErrorInDegreesX(int errorInPixels)
	{
		return DEGREES_PER_PIXEL_HORIZONTAL_GUESS * errorInPixels;
	}
	
	public double getErrorInDegreesY(int errorInPixels)
	{
		return DEGREES_PER_PIXEL_VERTICAL_GUESS * errorInPixels;
	}
	
	public double getDampenedErrorInDegreesX(int errorInPixels)
	{
		return getErrorInDegreesX(errorInPixels) * CONTROL_LOOP_DAMPENER;
	}
	
	public double getDampenedErrorInDegreesY(int errorInPixels)
	{
		return getErrorInDegreesY(errorInPixels) * CONTROL_LOOP_DAMPENER;
	}
	
	public void setDegreesPerPixelVerticalGuess(double d)
	{
		DEGREES_PER_PIXEL_VERTICAL_GUESS = d;
		System.out.println("GuessY: "+DEGREES_PER_PIXEL_VERTICAL_GUESS);
	}
	
	public void setDegreesPerPixelHorizontalGuess(double d)
	{
		DEGREES_PER_PIXEL_HORIZONTAL_GUESS = d;
		System.out.println("GuessX: "+DEGREES_PER_PIXEL_HORIZONTAL_GUESS);
	}
	
	public void setControlLoopDampener(double d)
	{
		CONTROL_LOOP_DAMPENER = d;
		System.out.println("New Dampener: "+CONTROL_LOOP_DAMPENER);
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
}