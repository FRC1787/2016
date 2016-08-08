package org.usfirst.frc.team1787.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.MeasurementType;

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
	private final double DESIRED_AREA_AS_PERCENTAGE_OF_BOUNDING_BOX_AREA = 0.33;
	private double areaScore;
	
	private double aspectRatio; // aspect ratio is determined using the equivalent rectangle, and is calculated as width/height
	private final double DESIRED_ASPECT_RATIO = 1.6; // Taken from screensteps live, but not actually tested yet.
	private double aspectRatioScore;
	
	private int boundingBoxTop;
	private int boundingBoxLeft;
	private int boundingBoxHeight;
	private int boundingBoxWidth;
	
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
	
	public void determineBoundingBox(Image img)
	{
		boundingBoxTop = (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_BOUNDING_RECT_TOP);
		boundingBoxLeft = (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_BOUNDING_RECT_LEFT);
		boundingBoxHeight = (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_BOUNDING_RECT_HEIGHT);
		boundingBoxWidth = (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_BOUNDING_RECT_WIDTH);
	}
}
