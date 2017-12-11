package mainServer;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;

public class Robot {
	
	private RemoteEV3 ev3;
	private RMIRegulatedMotor B, C;
	
	
	public Robot(String ip) throws RemoteException, MalformedURLException, NotBoundException {
		
		ev3 = new RemoteEV3(ip);
		
		ev3 = (RemoteEV3) BrickFinder.getDefault();
		ev3.setDefault();
		
		LCD.drawString("Sie sehen mich", 0,4);
		LCD.drawString("rollen", 0,5);
		LCD.drawString("Sie hassen", 0, 6);
		

		B = ev3.createRegulatedMotor("B", 'L');
		C = ev3.createRegulatedMotor("C", 'L');
		
	}
	
	public void readinput(int[] input) throws RemoteException {

		if (input[1] < 100) {
			if (input[0] > 100 && input[2] > 100 && input[3] > 100 && input[4] > 100) {
				input[1] = (100-input[1])*7;
				B.setSpeed(input[1]);
				C.setSpeed(input[1]);
				B.backward();
				C.backward();
			}
		}
		
		else {
			B.setSpeed(0);
			C.setSpeed(0);
		}
	}
	
	public void close() throws RemoteException	{
		System.out.println("Closing robot");
		B.stop(false);
		C.stop(false);
		B.close();
		C.close();
		System.out.println("done");
	}
	
	
}