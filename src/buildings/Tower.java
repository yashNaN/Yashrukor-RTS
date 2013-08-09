package buildings;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import units.Unit;

import main.*;

public class Tower extends Building{
//	private Image image;
	private int range=250;
	private int damage=5;
	public Tower(int race, int x, int y){
		super(9, 3000, 200, race, 100, 100, 40, 0, x, y);
		VISIONDISTANCE = 1000;
		image=new ImageIcon("Images/Tower.gif").getImage();
	}
//	@Override
//	public void draw(Graphics2D g, int x, int y, int w, int h) {
//		g.drawImage(image, x, y, w, h, null);
//	}
//	@Override
//	public void miniDraw(Graphics2D g, int x, int y, int w, int h) {
//		g.setColor(Color.gray);
//		g.fillRect(x, y, w, h);
//	}
	public void makeUnit() {
	}
	@Override
	public void tic() {
		if(constructed){
			checkForAttacks();
		}
	}
	public boolean checkForAttacks(){
		for(Unit u:myworld.getUnits()){
			if(u !=null && u.getPlayer()!=getPlayer()&&collidesRange(range,u)){
				u.damage(damage);
				return true;
			}
		}
		return false;
	}
	public void drawGUI(Graphics2D g, int x, int y, int w, int h) {
		super.drawGUI(g, x, y, w, h);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 40));
		g.drawString("Tower", x+40, y+50);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Damage:"+damage, x+400, y+30);
		g.drawString("Range:"+range, x+600, y+30);
//		g.drawString("Attack Speed:"+attackspeed, x+400, y+60);
	}
	@Override
	public void setDifficulty(int diff) {
	}
}
