import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.geom.Point;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

public class ForwardBehave implements Behavior {
	boolean supress = false;

	private Navigator nav;
	private DifferentialPilot pilot;
	private DataOutputStream dos;
	private Point destination;
	
	public ForwardBehave(DifferentialPilot pilot, DataOutputStream dos, Navigator nav, Point destination) {
		this.pilot = pilot;
		this.nav = nav;
		this.dos = dos;
		this.destination = destination;
	}

	@Override
	public boolean takeControl() {
		
		if(Main.isControlBehaveOn)
		{
			return false;
		}
		else
		{
			return true;
		}	
	}

	@Override
	public void action() {
		supress = false;
		float x = nav.getPoseProvider().getPose().getX();
		float y = nav.getPoseProvider().getPose().getY();

		goTo(destination.x, destination.y);
		try {
			
			dos.writeUTF("forward" + x + "#" + y);
			dos.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		supress = true;
	}

	//we made this because the goto method of nav was broken
	public void goTo(float x, float y) {
		float dx = x - nav.getPoseProvider().getPose().getX();
		float dy = y - nav.getPoseProvider().getPose().getY();
		float angle = (float) Math.toDegrees(Math.atan2(dy, dx));

		nav.rotateTo(angle);
		pilot.travel(nav.getPoseProvider().getPose().distanceTo(destination), true);
		while (!supress) {
			sendPos(nav.getPoseProvider().getPose().getX(), nav.getPoseProvider().getPose().getY());

			Thread.yield();

		}
		pilot.stop();

	}

	private void sendPos(float x, float y) {
		try {
			dos.writeUTF("forward" + x + "#" + y);

			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
