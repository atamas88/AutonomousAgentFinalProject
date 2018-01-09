package Default;

import java.awt.KeyEventPostProcessor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

import lejos.pc.comm.NXTConnector;

public class Main{
	private static MapComponent mc = new MapComponent();
	private static DataInputStream dis;
	private static DataOutputStream dos;
	private static NXTConnector conn;
	private static boolean connected;
	
	 public static void main(String[] args){
		 
		try
		{
			setUpConnection();		  
		    
			sendMessage();
		
		    Scanner sc = new Scanner(System.in);
		    boolean done = false;

		    while (! done)
		    { 
		    	String message = dis.readUTF();		
				dos.writeUTF(message.toUpperCase());
				dos.flush();
				String response = message.substring(0, 7);
				System.out.println(response);
				
				if ((response.equalsIgnoreCase("forward"))) {
					forwardResponse(message);
				}
				
				if ((response.equalsIgnoreCase("objectf"))) {
					objectFoundResponse(message);
				}
			
				if (message.equalsIgnoreCase("quit"))
				{
				    done = true;
				}
		    }
		    System.out.println("Client terminating");	    
		} 
		catch (IOException e)
		{
		    e.printStackTrace();
		}
	 }
	 
	 /*
	  * the response to what happends if the computer gets a forward response
	  */
	 private static void forwardResponse(String message)
	 {
			System.out.println();
			System.out.println(message);
			String coordinates = message.substring(7);
			 String[] coords =	coordinates.split("#");
			int x = Math.round(Float.parseFloat((coords[0])));
			int y = Math.round(Float.parseFloat((coords[1])));
			mc.addPosition(x, y);
	 }
	 
	 /*
	  * the response to whay happens if the computer gets a objectfound repsonse
	  */
	 private static void objectFoundResponse(String message){
			System.out.println();
			System.out.println(message);
			String coordinates = message.substring(7);
			 String[] coords =	coordinates.split("#");
			int x = Math.round(Float.parseFloat((coords[0])));
			int y = Math.round(Float.parseFloat((coords[1])));
			
			mc.addObject(x, y);
	 }
	 
	 /*
	  * setting up connection between computer and robot
	  */
	 private static void setUpConnection(){
		    System.out.println("Connecting to NXT...");
		    
		    conn = new NXTConnector();
		    connected = conn.connectTo("btsTpp://NoFun");
		    
		    while (! connected)
		    {
		    	System.out.println("ERROR - Unable to connect to NXT");
		    	System.exit(2);
		    }
	 }
	 
	 /*
	  * send coordinates out to robot
	  */
	 private static void sendMessage() throws IOException{
		 
		    dis = new DataInputStream(conn.getInputStream());
		    dos = new DataOutputStream(conn.getOutputStream());
		    
		    if (connected) {
		    	 System.out.println("Connection established ...");
		    	 createMap();
		    	 
		    	 Scanner sc = new Scanner(System.in);
		    	 System.out.print("Input coordinates: ");
		 		 String point = sc.nextLine();
					
		 		 int comma = point.indexOf(',');
					
		 		 String pointx = point.substring(0, comma);
		 		 String pointy = point.substring(comma + 1, point.length());
		 		 int x = Math.round(Float.parseFloat((pointx)));
				 int y = Math.round(Float.parseFloat((pointy)));
				 
		    	 mc.addDestination(x, y);
		    	 dos.writeUTF(point);
		    	 dos.flush();

			}
	 }
	 
	 /*
	  * set up and create the JFrame of the map
	  */
	 private static void createMap()
	 {
		 JFrame frame = new JFrame();
			frame.setTitle("Robot Map");
			frame.setSize(1200, 1200);
			frame.setLocationRelativeTo(null);
			
			frame.add(mc);
			frame.addKeyListener(new ControlKeys(dos));
			frame.setVisible(true);	
	 }
	 

	
	 
}
