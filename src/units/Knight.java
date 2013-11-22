package units;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import main.Thing;

import buildings.Building;

public class Knight extends Unit {
	private Image image;
	static ImageIcon icon=new ImageIcon("resources//images//Knight.gif");
	public static int WIDTH=60;
	public static int HEIGHT=60;
	public Knight(int race, int direction, int x, int y) {
		super(race, KNIGHT, direction, x, y, icon);
		setSize(60,60);
		type = KNIGHT;
		setHealth(400);
		movespeed = 7;
		damage = 8;
		range = 50;
		attackspeed = 1;
		goldcost = 20;
		woodcost = 20;
		foodcost = 20;
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
		g.drawString("Knight", x+40, y+50);
	}
//	@Override
//	public void miniDraw(Graphics2D g, int x, int y, int w, int h) {
//		g.setColor(Color.red);
//		g.fillRect(x,y,w,h);
//	}
	//@Override
	@Override
	public int getDamage() {
		// TODO Auto-generated method stub
		return damage;
	}

}
