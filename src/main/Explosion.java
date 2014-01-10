package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

public class Explosion extends Thing {
	private int duration;
	public static int id = 1; 
	int thisid; 
	public static Image explodeImage; 
	public Explosion(int x, int y)
	{
		//setVisionDistance(200);
		super();
		thisid = id;
		id++;
		this.setPosition(x,y);
		duration = 50;
		explodeImage = new ImageIcon("resources//images//Explosion.gif").getImage();
	}
	
	@Override
	public void tic()
	{
		if(duration <= 0)
		{
			destroySelf();
		}
		duration--;
	}
	
	public void destroySelf()
	{
		if(world().getExplosions().contains(this))
		{
			world().getExplosions().remove(this);
		}
	}
	
	@Override
	public void draw(Graphics2D g, int x, int y, int w, int h)
	{
		//world().addDebug("Drawing Explosion: " + thisid );
		//g.setColor(Color.BLACK);
		//g.drawRect(x(),y(), 100, 100);
		//g.fill3DRect(x(), y(), 100, 100, false);
		Point p = world().getCamera().getOnScreen(x(), y());
		g.drawImage(explodeImage, (int)p.getX(), (int)p.getY(),null);
	}
}
