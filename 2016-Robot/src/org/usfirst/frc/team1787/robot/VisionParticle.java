package org.usfirst.frc.team1787.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.MeasurementType;
import com.ni.vision.NIVision.Rect;

/**
 * This class represents a particle that has been identified in a binary image and contains data about that particle,
 * which is used to determine if that particle is actually a goal.
 * @author 1787Coders
 *
 */
public class VisionParticle
{
	private int particleID;
	
	private double areaAsPercentageOfBoundingBoxArea;
	private final double DESIRED_AREA_AS_PERCENTAGE_OF_BOUNDING_BOX_AREA = 0.33; // Taken from screensteps live. Not tested, but their reasoning is sound.
	private double areaScore;
	
	private double aspectRatio; // aspect ratio is determined using the equivalent rectangle, and is calculated as width/height
	private final double DESIRED_ASPECT_RATIO = 1.6; // Taken from screensteps live, but not actually tested yet.
	private double aspectRatioScore;
	
	private Rect boundingBox = new Rect();
	private Rect circleAroundCenterOfMass = new Rect(0, 0, 11, 11);
	
	private int centerOfMassX;
	private int centerOfMassY;
	
	private boolean isGoal;
	
	public VisionParticle(int id)
	{
		particleID = id;
	}
	
	public void determineCenterOfMass(Image img)
	{
		centerOfMassX = (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_CENTER_OF_MASS_X);
		centerOfMassY = (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_CENTER_OF_MASS_Y);
	}
	
	public int getCenterOfMassX()
	{
		return centerOfMassX;
	}
	
	public int getCenterOfMassY()
	{
		return centerOfMassY;
	}
	
	public void updateBoundingBox(Image img)
	{
		boundingBox.top = (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_BOUNDING_RECT_TOP);
		boundingBox.left = (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_BOUNDING_RECT_LEFT);
		boundingBox.height = (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_BOUNDING_RECT_HEIGHT);
		boundingBox.width = (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_BOUNDING_RECT_WIDTH);
	}
	
	public Rect getBoundingBox()
	{
		return boundingBox;
	}
	
	public void calculateAreaScore()
	{
		
	}
	
	public void calculateAspectRatioScore()
	{
		
	}
}
