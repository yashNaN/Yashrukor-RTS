package buildings;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import units.Swordsman;
import units.Unit;

public class Barracks extends Building{
	//makes Swordsmen
//	private ArrayList<Swordsman>smlist=new ArrayList<Swordsman>();
	public Barracks(int race,int x,int y){
		super(6, 3000, 200, race, 60, 40, 30, Unit.SWORDSMAN, x, y);
		image=new ImageIcon("resources//images//Barracks.gif").getImage();
		this.addPossibleUnit(Unit.SWORDSMAN);
	}
	public Building initialize(int race, int x, int y) {
		return new Barracks(race, x, y);
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
	public Unit createandcollectUnit(){
		boolean found = false;
		int tempcount = 0;
		Swordsman a = null;
		while(!found && tempcount++<20) {
			double ran = Math.random();
			if(ran<.25) {
				a=new Swordsman(getPlayer().race(),0,x()-Swordsman.WIDTH,(int) (y()-Swordsman.HEIGHT+Math.random()*(h()+Swordsman.HEIGHT)));
			} else if(ran<.5) {
				a=new Swordsman(getPlayer().race(),0,x()+w()+Swordsman.WIDTH,(int) (y()-Swordsman.HEIGHT+Math.random()*(h()+Swordsman.HEIGHT)));
			} else if(ran<.75) {
				a=new Swordsman(getPlayer().race(),0,(int) (x()-Swordsman.WIDTH+Math.random()*(w()+Swordsman.WIDTH)),y()-Swordsman.HEIGHT);
			} else {
				a=new Swordsman(getPlayer().race(),0,(int) (x()-Swordsman.WIDTH+Math.random()*(w()+Swordsman.WIDTH)),y()+h()+Swordsman.HEIGHT);
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
//	public boolean hasUnits(){
//		if(smlist.size()>0){
//			return true;
//		}
//		return false;
//	}
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
