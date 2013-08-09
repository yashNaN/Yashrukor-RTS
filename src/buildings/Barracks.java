package buildings;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import main.*;

import units.Archer;
import units.Swordsman;
import units.Unit;

public class Barracks extends Building{
	//makes Swordsmen
	boolean timing=false;
	int count=0;
	int ticcount=10;
	private ArrayList<Swordsman>smlist=new ArrayList<Swordsman>();
	public Barracks(int race,int x,int y){
		super(6, 3000, 150, race, 60, 40, 30, Unit.SWORDSMAN, x, y);
		image=new ImageIcon("Images/Barracks.gif").getImage();
	}
//	@Override
//	public void draw(Graphics2D g, int x, int y, int w, int h) {
//		g.drawImage(image, x, y, w, h, null);
//	}
//	@Override
//	public void miniDraw(Graphics2D g, int x, int y, int w, int h) {
//		g.setColor(Color.cyan);
//		g.fillRect(x,y,w,h);
//	}
	@Override
	public void tic() {
		if(timing==true){
			count++;
		}
		if(count>=ticcount){
			Unit u = createandcollectUnit();
			if(u!=null)
				myworld.getUnits().add(u);
		}
	}
	public void makeUnit(){
		if(constructed){
			timing=true;
		}
	}
	public int getUnitGoldCost() {
		return 20;
	}
	public int getUnitWoodCost() {
		return 10;
	}
	public int getUnitFoodCost() {
		return 20;
	}
	public Unit createandcollectUnit(){
		boolean found = false;
		int tempcount = 0;
		Swordsman a = null;
		while(!found && tempcount++<20) {
			double ran = Math.random();
			if(ran<.25) {
				a=new Swordsman(getPlayer().race(),0,x()-Archer.WIDTH,(int) (y()-Archer.HEIGHT+Math.random()*(h()+Archer.HEIGHT)));
			} else if(ran<.5) {
				a=new Swordsman(getPlayer().race(),0,x()+w()+Archer.WIDTH,(int) (y()-Archer.HEIGHT+Math.random()*(h()+Archer.HEIGHT)));
			} else if(ran<.75) {
				a=new Swordsman(getPlayer().race(),0,(int) (x()-Archer.WIDTH+Math.random()*(w()+Archer.WIDTH)),y()-Archer.HEIGHT);
			} else {
				a=new Swordsman(getPlayer().race(),0,(int) (x()-Archer.WIDTH+Math.random()*(w()+Archer.WIDTH)),y()+h()+Archer.HEIGHT);
			}
			found = !myworld.doesthiscollide(a, 0, 0);
		}
		if(!found)
			return null;
		if(a==null)
			return null;
		a.setPlayer(getPlayer());
//		smlist.add(a);
		timing=false;
		count=0;
		return a;
	}
//	public Unit collectUnit(){
//		if(smlist.size()>0){
//			return smlist.remove(0);
//		}
//		return null;
//	}
	public boolean hasUnits(){
		if(smlist.size()>0){
			return true;
		}
		return false;
	}
	public void drawGUI(Graphics2D g, int x, int y, int w, int h) {
		super.drawbuildGUI(g, x, y, w, h);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 40));
		g.drawString("Barracks", x+40, y+50);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Swordsman", x+345, y+100);
	}
	@Override
	public void setDifficulty(int diff) {
		ticcount=(int)(20/diff);
	}
}
