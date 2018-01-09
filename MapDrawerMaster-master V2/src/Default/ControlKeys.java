package Default;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ControlKeys implements KeyListener {

	DataOutputStream dos;
	
	public ControlKeys(DataOutputStream dos)
	{
		this.dos = dos;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {

		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		try {
			 switch(e.getKeyCode()){
			 case KeyEvent.VK_ENTER:
			        System.out.println("Controll taken!!!");
					dos.writeUTF("enter");
				 break;
			 case KeyEvent.VK_LEFT:
			        System.out.println("Left");
					dos.writeUTF("Left");
			        break;
			 case KeyEvent.VK_RIGHT:
			        System.out.println("Right");
					dos.writeUTF("Right");
			        break;
			 case KeyEvent.VK_UP:
			        System.out.println("Up");
					dos.writeUTF("Up");
			        break;
			 case KeyEvent.VK_DOWN:
			        System.out.println("Down");
					dos.writeUTF("Down");
			        break;
			 case KeyEvent.VK_SPACE:
			        System.out.println("space");
					dos.writeUTF("space");
			        break; 
			 }
				dos.flush();
				
			}
			 catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
			 }	
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
