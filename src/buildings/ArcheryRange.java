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
import units.Unit;
import units.Worker;


public class ArcheryRange extends Building{
	private ArrayList<Archer>archerlist=new ArrayList<Archer>();
	public ArcheryRange(int race,int x,int y){
		super(5, 3000, 220, race, 100, 30, 50, Unit.ARCHER, x, y);
		image=new ImageIcon("resources//images//ArcheryRange.png").getImage();
		this.addPossibleUnit(Unit.ARCHER);
	}
	public Building initialize(int race, int x, int y) {
		return new ArcheryRange(race, x, y);
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
	public int getUnitGoldCost() {
		return 20;
	}
	public int getUnitWoodCost() {
		return 10;
	}
	public int getUnitFoodCost() {
		return 20;
	}
//	public int getUnitCost(){
//		return 15;
//	}
	public Unit createandcollectUnit(){
		boolean found = false;
		int tempcount = 0;
		Archer a = null;
		while(!found && tempcount++<20) {
			double ran = Math.random();
			if(ran<.25) {
				a=new Archer(getPlayer().race(),0,x()-Archer.WIDTH,(int) (y()-Archer.HEIGHT+Math.random()*(h()+Archer.HEIGHT)));
			} else if(ran<.5) {
				a=new Archer(getPlayer().race(),0,x()+w()+Archer.WIDTH,(int) (y()-Archer.HEIGHT+Math.random()*(h()+Archer.HEIGHT)));
			} else if(ran<.75) {
				a=new Archer(getPlayer().race(),0,(int) (x()-Archer.WIDTH+Math.random()*(w()+Archer.WIDTH)),y()-Archer.HEIGHT);
			} else {
				a=new Archer(getPlayer().race(),0,(int) (x()-Archer.WIDTH+Math.random()*(w()+Archer.WIDTH)),y()+h()+Archer.HEIGHT);
			}
			found = !myworld.doesthiscollide(a, 0, 0);
		}
		if(!found)
			return null;
		if(a==null)
			return null;
		a.setPlayer(getPlayer());
		timing=false;
		count=0;
		return a;
	}
//	public Unit collectUnit(){
//		if(archerlist.size()>0){
//			return archerlist.remove(0);
//		}
//		return null;
//	}
//	public boolean hasUnits(){
//		if(archerlist.size()>0){
//			return true;
//		}
//		return false;
//	}
	public void drawGUI(Graphics2D g, int x, int y, int w, int h) {
		super.drawbuildGUI(g, x, y, w, h);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 40));
		g.drawString("ArcherRange", x+40, y+70);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Archer", x+345, y+100);
	}
	@Override
	public void setDifficulty(int diff) {
		ticcount=(int)(20/diff);
	}
}