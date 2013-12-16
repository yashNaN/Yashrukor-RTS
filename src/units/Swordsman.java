package units;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import main.Thing;

import buildings.Building;

public class Swordsman extends Unit {
//	static ImageIcon icon=new ImageIcon("resources//images//Swordsman.gif");
	private Image image;
	public static int WIDTH=35;
	public static int HEIGHT=36;
	public Swordsman(int race, int direction, int x, int y) {
		super(race, SWORDSMAN, direction, x, y);
		Image i = Toolkit.getDefaultToolkit().createImage("resources//images//Swordsman.gif");
		this.setImage(i);
		setSize(WIDTH,HEIGHT);
		type = SWORDSMAN;
		setHealth(400);
		movespeed = 3;
		damage = 5;
		range = 20;
		attackspeed = 1;
		setGoldcost(5);
		setWoodcost(10);
		setFoodcost(10);
//		image=icon.getImage();
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
		g.drawString("Warrior", x+40, y+50);
	}
//	@Override
//	public void miniDraw(Graphics2D g, int x, int y, int w, int h) {
//		g.setColor(Color.red);
//		g.fillRect(x,y,w,h);
//	}

	@Override
	public int getDamage() {
		// TODO Auto-generated method stub
		return damage;
	}

	


}
