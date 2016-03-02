package org.usfirst.frc.team1787.robot;
import java.util.ArrayList;

public class AutoRoutines
{
	private ArrayList<AutoRoutine> routines = new ArrayList<AutoRoutine>();
	
	public static final int LOWBAR_KEY = 0;
	
	public static final int TO_TOWER_KEY = 1;
	
	public AutoRoutines()
	{
		routines.add(LOWBAR_KEY, initLowbar());
		routines.add(TO_TOWER_KEY, initToTower());
	}
	
	public AutoRoutine initLowbar()
	{
		AutoRoutine rout = new AutoRoutine("Lowbar");
		rout.addStep(new AutoStep("M", 5));
		rout.addStep(new AutoStep("M", -5));
		rout.addStep(new AutoStep("M", 5));
		return rout;
	}
	
	public AutoRoutine initToTower()
	{
		AutoRoutine rout = new AutoRoutine("Go To Tower");
		rout.addStep(new AutoStep("M", 5));
		rout.addStep(new AutoStep("T", 30));
		rout.addStep(new AutoStep("M", 5));
		return rout;
	}

	
	public AutoRoutine getRoutine(int desiredRoutine)
	{
		return routines.get(desiredRoutine);
	}
}