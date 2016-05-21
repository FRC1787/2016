package org.usfirst.frc.team1787.robot;

/**
 * This class is for logic involving PID controllers.
 * @author Simon Wieder
 *
 */
public class PIDOutputCalc
{
	private double kp;
	private double ki;
	private double kd;
	
	private double dt = 0.05;
	
	private double error;
	private double previousError;
	private double errorRateOfChange;
	private double areaUnderErrorCurve;
	
	private double output;
	private double minOutput;
	private double maxOutput;
	
	private double toleranceThreshold = 1;
	
	public PIDOutputCalc(double p, double i, double d)
	{
		kp = p;
		ki = i;
		kd = d;
		reset();
	}
	
	public double generateOutput()
	{
		// Calculate error (done outside this method)
		
		areaUnderErrorCurve += (error * dt);
		
		errorRateOfChange = ((error - previousError) / dt);
		previousError = error;
		
		output = (error * kp) + (areaUnderErrorCurve * ki) + (errorRateOfChange * kd);
		return output;
	}
	
	public void calculateError(double desiredValue, double currentValue)
	{
		error = desiredValue - currentValue;
	}
	
	public boolean errorIsAcceptable()
	{
		return (-toleranceThreshold <= error && error <= toleranceThreshold);
	}
	
	public void setToleranceThreshold(double tt)
	{
		toleranceThreshold = tt;
	}
	
	public void reset()
	{
		error = 0;
		previousError = 0;
		errorRateOfChange = 0;
		areaUnderErrorCurve = 0;
		output = 0;
	}
}