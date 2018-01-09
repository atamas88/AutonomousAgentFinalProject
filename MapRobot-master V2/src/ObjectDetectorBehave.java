
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.geom.Point;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.FeatureListener;
import lejos.robotics.subsumption.Behavior;

public class ObjectDetectorBehave implements Behavior, FeatureListener {
    boolean supress = false;
	private UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
	private final int securityDistance = 30;
	boolean objectDetected = false;

	DifferentialPilot pilot;
	Navigator nav;
	DataInputStream dis;
	DataOutputStream dos;
	public ObjectDetectorBehave(DifferentialPilot pilot, DataInputStream dis,DataOutputStream dos, Navigator nav){
		this.pilot = pilot;
		this.dis = dis;
		this.dos =dos;
		this.nav = nav;
	}
	@Override
	public boolean takeControl() {
	 
		return us.getDistance()< securityDistance && !Main.isControlBehaveOn;
	}

	@Override
	public void action() {
	
		System.out.println("fdsfds");
				pilot.stop();
				sendRespond();
				
				do
				{
					pilot.rotate(12);
					//float x = nav.getPoseProvider().getPose().getX();
					//float y = nav.getPoseProvider().getPose().getY();
					float z = nav.getPoseProvider().getPose().getHeading();
					
					float dis = us.getDistance();
					
					Point obj = nav.getPoseProvider().getPose().pointAt(dis, z);
					
					
					try {
						dos.writeUTF("objectf" + obj.x + "#" + obj.y);
					
						dos.flush();
					} catch (IOException e) {
						e.printStackTrace();
						
					}
				}while(us.getDistance() < securityDistance);
				
				pilot.travel(10);
				
				objectDetected = false;		
	}
	
	public void sendRespond(){
		try {
			dos.writeUTF("Object Found!");
			dos.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void suppress() {
		supress = true;// TODO Auto-generated method stub

	}
	
	@Override
	public void featureDetected(Feature feature, FeatureDetector detector) {
		objectDetected = true;// TODO Auto-generated method stub
		
	}

}
