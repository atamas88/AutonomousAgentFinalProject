
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.geom.Point;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Main {
	private static DifferentialPilot pilot = new DifferentialPilot(DifferentialPilot.WHEEL_SIZE_NXT2, 14.0, Motor.B,
			Motor.C);
	private static Navigator nav = new Navigator(pilot);
	private static Point destination;
	private static String points;
	private static DataInputStream dis;
	private static DataOutputStream dos;
	public static boolean isControlBehaveOn;

	public static void main(String[] args) {
		
		isControlBehaveOn = false;
		setUpConnection();
		// destination = new Point(0, 0); seems unneccesary
		setUpStartPoint();
		
		pilot.setTravelSpeed(17);
		
		setUpBehaviors();
	}

	private static void setUpConnection() {
		waitForKeyPressWithDelay(500);
		LCD.clear();
		LCD.drawString("Waiting for connection...", 0, 0);

		BTConnection btc = Bluetooth.waitForConnection();

		LCD.drawString("Connected", 0, 0);

		dis = new DataInputStream(btc.openDataInputStream());
		dos = new DataOutputStream(btc.openDataOutputStream());
	}

	private static void setUpStartPoint() {
		try {
			points = dis.readUTF();
			System.out.println(points);
			int comma = points.indexOf(',');
			String pointx = points.substring(0, comma);
			String pointy = points.substring(comma + 1, points.length());

			int x = Math.round(Float.parseFloat((pointx)));
			int y = Math.round(Float.parseFloat((pointy)));
			destination = new Point(x, y);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void setUpBehaviors(){
		System.out.println("Program Started");
		Behavior forward = new ForwardBehave(pilot, dos, nav, destination);
		Behavior detect = new ObjectDetectorBehave(pilot, dis, dos, nav);
		Behavior control = new ControlBehave(pilot, dis, dos, nav);
		Behavior stop = new ExitBehave(pilot, nav, destination);
		Behavior[] behave = { forward, detect, control, stop };

		
		Arbitrator arb = new Arbitrator(behave);
		arb.start();
	}
	
	public static void waitForKeyPressWithDelay(int delayTime) {
		System.out.println("Press any key to continue...");
		Button.waitForAnyPress();
		delay(delayTime);
	}

	public static void delay(int delayTime)

	{
		try {
			Thread.sleep(delayTime);
		} catch (InterruptedException e) {
			// just wake up
		}
	}
}
