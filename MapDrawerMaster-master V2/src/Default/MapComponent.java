package Default;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;



public class MapComponent extends JComponent{

	
    private final List<Point> points = new ArrayList<>(); 
    private final List<Point> objects = new ArrayList<>();
    private Point destination = new Point(0,0);
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
        g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
        for (Point p : points)
        {
        	g2d.setColor(Color.BLACK);
            g2d.fillOval(getWidth() / 2 + p.x, getHeight() / 2 - p.y, 3, 3);
        }
        for (Point p : objects)
        {
        	g2d.setColor(Color.RED);
        	g2d.fillOval(getWidth() / 2 + p.x, getHeight() / 2 - p.y, 3, 3);
        }
        g2d.setColor(Color.GREEN);
    	g2d.fillOval(getWidth() / 2 + destination.x, getHeight() / 2 - destination.y, 10, 10);
    }
    
    public void addPosition(int x, int y)
    {
		points.add(new Point(x, y));
		repaint();
    }
    
    public void addObject(int x, int y)
    {
    	objects.add(new Point(x, y));
    	repaint();
    }
    
    public void addDestination(int x, int y){
    	destination = new Point(x,y);
    	repaint();
    }
	
}