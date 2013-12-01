package units;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import main.Action;

import buildings.*;


public class Worker extends Unit {
	int harvestRate=1;
	private Image image;
	static ImageIcon icon=new ImageIcon("resources//images//Worker.gif");
	public static int WIDTH=23;
	public static int HEIGHT=29;
	public Worker(int race, int direction,int x, int y){
		//super(health, healthregen, race, 1, x, y, direction);
		super(race,WORKER,direction, x, y,icon);
		setSize(23,29);
		type = WORKER;
		setHealth(250);
		movespeed = 4;
		damage = 2;
		range = 5;
		attackspeed = 1;
		goldcost = 0;
		foodcost = 10;
		woodcost = 10;
		image=icon.getImage();
		commands.add(Action.BUILD);
		commands.add(Action.REPAIR);
	}
	@Override
	public void draw(Graphics2D g, int x, int y, int w, int h) {
		super.draw(g, x, y, w, h);
		g.drawImage(image, x, y, w, h, null);
	}
	public void drawGUI(Graphics2D g, int x, int y, int w, int h) {
//		super.drawGUI(g, x, y, w, h);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 40));
		g.drawString("Worker", x+40, y+50);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("MoveSpeed:"+movespeed, x+40, y+90);
		g.drawString("Farm: F", x+200, y+50);
		g.drawString("Quarry: Q", x+200, y+80);
		g.drawString("LumberMill: L", x+200, y+110);
		g.drawString("TownHall: H", x+200, y+140);

		g.drawString("Barracks: B", x+400, y+50);
		g.drawString("ArcherRange: A", x+400, y+80);
		g.drawString("Stable: S", x+400, y+110);
		g.drawString("Church: C", x+400, y+140);

		g.drawString("Tower: T", x+600, y+50);
	}
//	@Override
//	public void miniDraw(Graphics2D g, int x, int y, int w, int h) {
//		g.setColor(Color.red);
//		g.fillRect(x,y,w,h);
//	}
//	public void harvest(){
//		
//	}
//	public void build(Building b){
//		//int d = getDirection();
//		// place building in location in direction d; 
//		
//	}
	@Override
	public int getDamage() {
		// TODO Auto-generated method stub
		return damage;
	}

}
