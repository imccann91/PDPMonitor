package org.usfirst.frc.team2228.robot;

public class CameraThread implements Runnable {

	private Thread cameraThread;
	private String cameraThreadName = "";
	
	@Override
	public void run() {
		//stuff you want to do here.
		
	}

	//Call this to start a thread from this class.
	public void start(){
		if(cameraThread == null){
			cameraThread = new Thread(this,cameraThreadName);
			cameraThread.start();
		}
	}
}
