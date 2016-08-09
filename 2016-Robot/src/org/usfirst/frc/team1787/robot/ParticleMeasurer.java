package org.usfirst.frc.team1787.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.MeasurementType;
import com.ni.vision.NIVision.Rect;

/**
 * This class can return info about a given particle. It's probably not necessary and will likely be merged with VisionMethods soon.
 * @author 1787Coders
 *
 */
public class ParticleMeasurer
{
	private static int currentParticle = 0;
	
	private double areaAsPercentageOfBoundingBoxArea;
	private final double DESIRED_AREA_AS_PERCENTAGE_OF_BOUNDING_BOX_AREA = 0.33; // Taken from screensteps live. Not tested, but their reasoning is sound.
	private double areaScore;
	
	private double aspectRatio; // aspect ratio is determined using the equivalent rectangle, and is calculated as width/height
	private final double DESIRED_ASPECT_RATIO = 1.6; // Taken from screensteps live, but not actually tested yet.
	private double aspectRatioScore;
	
	private boolean isGoal;
	
	private static Image img;
	
	public ParticleMeasurer(Image i)
	{
		img = i;
	}
	
	public static int getCenterOfMassX(int particleID)
	{
		if (particleID < getNumOfParticles())
			return (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_CENTER_OF_MASS_X);
		else
			return -1;
	}
	
	public static int getCenterOfMassX()
	{
		return getCenterOfMassX(currentParticle);
	}
	
	public static int getCenterOfMassY(int particleID)
	{
		if (particleID < getNumOfParticles())
			return (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_CENTER_OF_MASS_Y);
		else
			return -1;
	}
	
	public static int getCenterOfMassY()
	{
		return getCenterOfMassY(currentParticle);
	}
	
	public static int getArea(int particleID)
	{
		if (particleID < getNumOfParticles())
			return (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_AREA);
		else
			return -1;
	}
	
	public static Rect getBoundingBox(int particleID)
	{
		if (particleID < getNumOfParticles())
		{
			Rect boundingBox = new Rect();
			boundingBox.top = (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_BOUNDING_RECT_TOP);
			boundingBox.left = (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_BOUNDING_RECT_LEFT);
			boundingBox.height = (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_BOUNDING_RECT_HEIGHT);
			boundingBox.width = (int) NIVision.imaqMeasureParticle(img, particleID, 0, MeasurementType.MT_BOUNDING_RECT_WIDTH);
			return boundingBox;
		}
		else
			return null;
	}
	
	public static void calculateAreaScore(int particleID)
	{
		if (particleID < getNumOfParticles())
		{
			
		}
	}
	
	public static void calculateAspectRatioScore(int particleID)
	{
		if (particleID < getNumOfParticles())
		{
			
		}
	}
	
	public static int getNumOfParticles()
	{
		return (int) NIVision.imaqCountParticles(img, 1);
	}
	
	public static void setCurrentParticle(int current)
	{
		currentParticle = current;
	}
}
