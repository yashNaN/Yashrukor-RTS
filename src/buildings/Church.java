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
import units.Healer;
import units.Swordsman;
import units.Unit;

public class Church extends Building{
	//makes Healers
	boolean timing=false;
	int count=0;
	int ticcount=50;
	private ArrayList<Healer>clist=new ArrayList<Healer>();
//	private Image image;
	public Church(int race,int x,int y){
		super(7, 2500, 210, race, 80, 40, 50, Unit.HEALER, x, y);
		image=new ImageIcon("resources//images//Church.gif").getImage();
	}
	public Building initialize(int race, int x, int y) {
		return new Church(race, x, y);
	}
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
	public int getUnitCost(){
		return 15;
	}
	public Unit createandcollectUnit(){
		boolean found = false;
		int tempcount = 0;
		Healer a = null;
		while(!found && tempcount++<20) {
			double ran = Math.random();
			if(ran<.25) {
				a=new Healer(getPlayer().race(),0,x()-Healer.WIDTH,(int) (y()-Healer.HEIGHT+Math.random()*(h()+Healer.HEIGHT)));
			} else if(ran<.5) {
				a=new Healer(getPlayer().race(),0,x()+w()+Healer.WIDTH,(int) (y()-Healer.HEIGHT+Math.random()*(h()+Healer.HEIGHT)));
			} else if(ran<.75) {
				a=new Healer(getPlayer().race(),0,(int) (x()-Healer.WIDTH+Math.random()*(w()+Healer.WIDTH)),y()-Healer.HEIGHT);
			} else {
				a=new Healer(getPlayer().race(),0,(int) (x()-Healer.WIDTH+Math.random()*(w()+Healer.WIDTH)),y()+h()+Healer.HEIGHT);
			}
			found = !myworld.doesthiscollide(a, 0, 0);
		}
		if(!found)
			return null;
		if(a==null)
			return null;
		a.setPlayer(getPlayer());
		clist.add(a);
		timing=false;
		count=0;
		return a;
	}
//	public Unit collectUnit(){
//		if(clist.size()>0){
//			return clist.remove(0);
//		}
//		return null;
//	}
	public boolean hasUnits(){
		if(clist.size()>0){
			return true;
		}
		return false;
	}
	public void drawGUI(Graphics2D g, int x, int y, int w, int h) {
		super.drawbuildGUI(g, x, y, w, h);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 40));
		g.drawString("Church", x+40, y+50);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Healer", x+345, y+100);
	}
	@Override
	public void setDifficulty(int diff) {
		ticcount=(int)(30/diff);
	}
}
