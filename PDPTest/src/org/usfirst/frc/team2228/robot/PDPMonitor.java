package org.usfirst.frc.team2228.robot;

/*
 * Method and variable modifier notes.
 * 
 * synchronize: Tells the operating system that only one caller
 *              of this function may have access to it at any
 *              one time. If whomever called the function first
 *              will be the only one with access. All other
 *              callers will be placed in a "queue" in the
 *              order they made their call. In other words,
 *              it follows a FIFO system.
 *              
 * volatile: Forces the program to read and write to system memory.
 *           This prevents the program from using the cache on the CPU.
 *           Basically this means the value of this variable is always
 *           the most up-to-date it can be.
 *           
 * static: This means that this reference (pointer) or variable is only
 *         in one place in memory. This means that anything accessing
 *         that data will all access the same memory location.
 */

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PDPMonitor implements Runnable {

	private static volatile PDPMonitor instance = null; // null reference of the
														// PDP monitor.
	private int pdpCANID;
	private PowerDistributionPanel pdp;
	private Thread pdpThread;
	private String pdpThreadName = "PDP Thread";
	
	// Allows the thread to be "stopped"/Started.
	public static volatile boolean running = true;

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

	// What the class does while it is running.
	// it can be "paused" by setting the public variable
	// "running" to false.
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

	// Exposes and returns the CAN ID of the PDP to other systems.
	public synchronized int getPDPCANID() {
		return this.pdpCANID;
	}

	// Exposes and returns the voltage of the robot's battery to other systems.
	public synchronized double geVoltage() {
		return pdp.getVoltage();
	}

	// Allows you to get the total current flowing through the PDP.
	public synchronized double getTotalSystemCurrent() {
		return pdp.getTotalCurrent();
	}

	// Allows you to get the current for a single channel on the PDP.
	public synchronized double getDeviceCurrent(int pdpChannel) {
		return pdp.getCurrent(pdpChannel);
	}

	/*
	* If a PDPMonitor instance has not been created, create one and expose
	* it to the getInstance() caller. This will also prevent another instance
	* of the PDPMonitor from being created as all of the constructors are
    * private to this class.
	*/
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

	/* If a PDPMonitor instance has not been created, create one and expose
	* it to the getInstance() caller. This will also prevent another instance
	* of the PDPMonitor from being created as all of the constructors are
	* private to this class.
	*
	* This method signature allows the PDPMonitor to be constructed with a 
	* different CAN address than 0. 
	*/
	public static PDPMonitor getInstance(int CANAddress) {
		if (instance == null) {
			synchronized (PDPMonitor.class) {
				if (instance == null) {
					instance = new PDPMonitor(CANAddress);
				}
			}

		}
		return instance;
	}

	// Sends various pieces of data to the smart dashboard.
	private void sendDataToDashboard() {

		pdp.updateTable();

		SmartDashboard.putNumber("Total Current: ", pdp.getTotalCurrent());
		SmartDashboard.putNumber("Total Voltage: ", pdp.getVoltage());
		SmartDashboard.putNumber("Total Power: ", pdp.getTotalPower());
		SmartDashboard.putNumber("Total Energy: ", pdp.getTotalEnergy());
	}
}
