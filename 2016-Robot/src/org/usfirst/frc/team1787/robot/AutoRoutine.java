package org.usfirst.frc.team1787.robot;
import java.util.ArrayList;

/**
 * This class represents any given autonomous routine.
 * An autonomous routine is a collection of autoSteps to be completed.
 * @author 1787Coders
 *
 */
public class AutoRoutine
{
	private ArrayList<AutoStep> steps = new ArrayList<AutoStep>();
	private String name;
	
	public AutoRoutine(String n)
	{
		name = n;
	}
	
	public void addStep(AutoStep s)
	{
		steps.add(s);
	}
	
	public AutoStep getStep(int desiredStep)
	{
		return steps.get(desiredStep);
	}
	
	public String getName()
	{
		return name;
	}

	public int getLength() {
		return steps.size();
	}
}