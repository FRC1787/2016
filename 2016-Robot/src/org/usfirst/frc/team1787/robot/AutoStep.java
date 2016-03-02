package org.usfirst.frc.team1787.robot;

/**
 * This class represents any given step that may be performed as a part of an autonomous routine.
 * @author 1787Coders
 *
 */
public class AutoStep
{
	private String action;
	private double actionValue;
	
	public AutoStep(String a, double v)
	{
		action = a;
		actionValue = v;
	}
	
	public String getAction()
	{
		return action;
	}
	
	public double getActionValue()
	{
		return actionValue;
	}

}