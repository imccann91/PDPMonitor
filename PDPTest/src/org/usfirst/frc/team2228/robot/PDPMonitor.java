package org.usfirst.frc.team2228.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PDPMonitor implements Runnable {

	private static volatile PDPMonitor instance = null;
	private int pdpCANID;
	private PowerDistributionPanel pdp;
	private Thread pdpThread;
	private String pdpThreadName = "PDP Thread";
	public static volatile boolean running = true; // Allows the thread to be
													// "stopped"/Started.

	// This constructor will assume a CAN ID for the PDP monitor to be 0;
	private PDPMonitor() {
		pdpCANID = 0;
		pdp = new PowerDistributionPanel(pdpCANID);
	}

	// This constructor will allow the PDP monitor to use a different CAN ID.
	private PDPMonitor(int id) {
		pdpCANID = id;
		pdp = new PowerDistributionPanel(pdpCANID);
	}

	@Override
	public void run() {
		while (running == true) {
			sendDataToDashboard();
		}
	}

	// Call this to start a thread from this class.
	public void start() {
		if (pdpThread == null) {
			pdpThread = new Thread(this, pdpThreadName);
			pdpThread.start();
		}
	}

	// Exposes the PDP reference to the other systems.
	public PowerDistributionPanel getPDP() {
		return this.pdp;
	}

	// Exposes and returns the CAN ID of the PDP to other systems.
	public synchronized int getPDPCANID() {
		return this.pdpCANID;
	}

	// Exposes and returns the voltage of the robot to other systems.
	public synchronized double geVoltage() {
		return pdp.getVoltage();
	}

	public synchronized double getTotalSystemCurrent() {
		return pdp.getTotalCurrent();
	}

	public synchronized double getDeviceCurrent(int pdpChannel) {
		return pdp.getCurrent(pdpChannel);
	}

	public static PDPMonitor getInstance() {
		if (instance == null) {
			synchronized (PDPMonitor.class) {
				if (instance == null) {
					instance = new PDPMonitor();
				}
			}

		}
		return instance;
	}

	private void sendDataToDashboard() {

		pdp.updateTable();

		SmartDashboard.putNumber("Total Current: ", pdp.getTotalCurrent());
		SmartDashboard.putNumber("Total Voltage: ", pdp.getVoltage());
		SmartDashboard.putNumber("Total Power: ", pdp.getTotalPower());
		SmartDashboard.putNumber("Total Energy: ", pdp.getTotalEnergy());
	}
}
