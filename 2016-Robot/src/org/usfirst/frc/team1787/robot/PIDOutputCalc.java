package org.usfirst.frc.team1787.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	
	private double pTerm;
	private double iTerm;
	private double dTerm;
	
	private double dt = 0.05;
	
	private double error;
	private double previousError;
	private double errorRateOfChange;
	private double areaUnderErrorCurve;
	
	private double output;
	private double minOutput;
	private double maxOutput;
	
	private double toleranceThreshold;
	private double integralThreshold;
	
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
		
		if (-integralThreshold < error && error < integralThreshold)
		areaUnderErrorCurve += (error * dt);
		
		errorRateOfChange = ((error - previousError) / dt);
		previousError = error;
		
		pTerm = (error * kp);
		iTerm = (areaUnderErrorCurve * ki);
		dTerm = (errorRateOfChange * kd);
		
		//System.out.println("P term: "+pTerm);
		//System.out.println("I term : "+iTerm);
		//System.out.println("D term: "+dTerm);
		
		output = pTerm + iTerm + dTerm;
		
		if (output > maxOutput)
			output = maxOutput;
		else if (output < minOutput)
			output = minOutput;
		
		System.out.println("Output: "+output);
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
	
	public void setIntegralThreshold(double it)
	{
		integralThreshold = it;
	}
	
	public void setMaxOutput(double max)
	{
		maxOutput = max;
	}
	
	public void setMinOutput(double min)
	{
		minOutput = min;
	}
	
	public void reset()
	{
		error = 0;
		previousError = 0;
		errorRateOfChange = 0;
		areaUnderErrorCurve = 0;
		output = 0;
	}
	
	public void putDataOnSmartDashboard()
	{
		SmartDashboard.putNumber("Error", error);
		SmartDashboard.putNumber("P Term", pTerm);
		SmartDashboard.putNumber("I Term", iTerm);
		SmartDashboard.putNumber("D Term", dTerm);
		SmartDashboard.putNumber("Output", output);
	}
}