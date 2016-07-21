package org.usfirst.frc.team1787.robot;

//import org.opencv.core.Core;
//import org.opencv.core.Mat;

public class VisionMethods
{
	//private USBCamera cam;
	//VideoCapture vc;
	//Mat img;
	int imageHeight = 6;
	public VisionMethods ()
	{
		System.out.println("Trying to construct VisionMethods!");
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//vc.open(0);
		//img = new Mat();
	}
	
	public void method1()
	{
		//vc.retrieve(img);
		//imageHeight = img.height();
		System.out.println("OpenCV VideoCapture Mat imageHeight: "+imageHeight);
	}
}