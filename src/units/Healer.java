package units;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public class Healer extends Unit {
	private Image image;
	static ImageIcon icon=new ImageIcon("resources//images//Healer.gif");
	public static int WIDTH=55;
	public static int HEIGHT=55;
	public Healer(int race, int direction, int x, int y) {
		super(race, HEALER, direction, x, y,icon);
		setSize(WIDTH,HEIGHT);
		type = HEALER;
		setHealth(350);
		movespeed = 5;
		damage = -10;
		range = 250;
		attackspeed = 1;
		setGoldcost(15);
		setWoodcost(15);
		setFoodcost(15);
		image=icon.getImage();
	}
	@Override
	public void draw(Graphics2D g, int x, int y, int w, int h) {
		super.draw(g, x, y, w, h);
		g.drawImage(image, x, y, w, h, null);
	}
	public void drawGUI(Graphics2D g, int x, int y, int w, int h) {
		super.drawGUI(g, x, y, w, h);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 40));
		g.drawString("Healer", x+40, y+50);
	}
	public boolean heal(Unit u){
		/*
		int x =0;
		while(u.health <= u.maxhealth){
			if(x < 4 ){	
				x++;
				u.health++;
				if( x == 4)
					return true; 
			}
			else
				break; 
		}
		*/
		return false;
	}
	@Override
	public int getDamage() {
		return damage;
	}

}
