package org.usfirst.frc.team1787.robot;
import java.util.ArrayList;

public class AutoRoutines
{
	private ArrayList routines = new ArrayList<AutoRoutine>();
	
	private AutoRoutine auto1;
	private AutoRoutine auto2;
	private AutoRoutine auto3;
	
	public AutoRoutines()
	{
		initAuto1();
		initAuto2();
		initAuto3();
	}
	
	public void initAuto1()
	{
		auto1 = new AutoRoutine();
		auto1.addStep(new AutoStep("M", 5));
		auto1.addStep(new AutoStep("M", -5));
		auto1.addStep(new AutoStep("M", 5));
		routines.add(auto1);
	}
	
	public void initAuto2()
	{
		auto2 = new AutoRoutine();
		routines.add(auto2);
	}
	
	public void initAuto3()
	{
		auto3 = new AutoRoutine();
		routines.add(auto3);
	}
	
	public AutoRoutine getRoutine(int desiredRoutine)
	{
		return (AutoRoutine) routines.get(desiredRoutine-1);
	}
}