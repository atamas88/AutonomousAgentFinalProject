
import lejos.geom.Point;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

public class ExitBehave implements Behavior {
	boolean supress = false;
	private DifferentialPilot pilot;
	private Navigator nav;
	private Point destination;
	
	public ExitBehave(DifferentialPilot pilot, Navigator nav, Point destination){
		this.pilot = pilot;
		this.nav = nav;
		this.destination = destination;
	}
	
	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return nav.getPoseProvider().getPose().getX() == destination.x &&
				nav.getPoseProvider().getPose().getY() == destination.y;
	}

	@Override
	public void action() {
		pilot.stop();
		System.out.println("DESTINATION REACHED!");
	}

	@Override
	public void suppress() {
		supress = true;
	}

}
