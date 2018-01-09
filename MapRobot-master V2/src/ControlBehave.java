import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

public class ControlBehave implements Behavior {

	boolean supress = false;
	DifferentialPilot pilot;
	DataInputStream dis;
	Navigator nav;
	String input;
	DataOutputStream dos;
	
	public ControlBehave(DifferentialPilot pilot, DataInputStream dis, DataOutputStream dos, Navigator nav) {
		this.pilot = pilot;
		this.dis = dis;
		this.dos = dos;
		this.nav = nav;
	}

	@Override
	public boolean takeControl() {

		try {
			if (dis.available() > 0) {
				if (!Main.isControlBehaveOn) {
					Main.isControlBehaveOn = dis.readUTF().equalsIgnoreCase("enter");
				}

				return Main.isControlBehaveOn;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void action() {
			//pilot.stop();
			try {
				inputAction();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			float x = nav.getPoseProvider().getPose().getX();
			float y = nav.getPoseProvider().getPose().getY();
			sendPos(x, y);
	}

	private void inputAction() throws IOException{
		if (dis.available() > 0) {
			input = dis.readUTF();
			
			if (input.equalsIgnoreCase("Up")) {
				pilot.travel(5);
				System.out.println("Up");
			}
			if (input.equalsIgnoreCase("Down")) {
				pilot.travel(-5);
				System.out.println("down");
			}
			if (input.equalsIgnoreCase("Left")) {
				pilot.rotate(10);
				System.out.println("left");
			}
			if (input.equalsIgnoreCase("Right")) {
				pilot.rotate(-10);
				System.out.println("right");
			}
			if (input.equalsIgnoreCase("space")) {
				System.out.println("spacess");
				Main.isControlBehaveOn = false;
			}
		}
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
	
	@Override
	public void suppress() {
		supress = true;
	}

}
