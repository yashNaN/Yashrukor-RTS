package buildings;
import main.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;


public class Farm extends Building{
	private int food=0;//amount of food the farm currently has
	private int stic=0;
//	private Image image;
	private boolean makingresources=false;
	public Farm(int race, int x, int y){
		super(1, 1500, 100, race, 50, 0, 20, 0, x, y);
		image=new ImageIcon("resources//images//Farm.gif").getImage();
	}
	public Building initialize(int race, int x, int y) {
		return new Farm(race, x, y);
	}
	public int collectResources(){
		int ret=food;
		food=0;
		return ret;
	}
	public void tic() {
		if(constructed){
			stic++;
		}
		if(stic>10){
			food+=5;
			stic=0;
		}
	}
	public void makeUnit() {
	}
	public void drawGUI(Graphics2D g, int x, int y, int w, int h) {
		super.drawGUI(g, x, y, w, h);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 40));
		g.drawString("Farm", x+40, y+50);
	}
	@Override
	public void setDifficulty(int diff) {
	}
}
