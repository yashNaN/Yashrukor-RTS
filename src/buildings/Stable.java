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
import units.Knight;
import units.Swordsman;
import units.Unit;
import units.Worker;


public class Stable extends Building{
	private ArrayList<Knight>knightlist=new ArrayList<Knight>();
//	private Image image;
	public Stable(int race,int x,int y){
		super(8, 3000, 250, race, 60, 50, 70, 4, x, y);
		image=new ImageIcon("resources//images//Stables.gif").getImage();
		this.addPossibleUnit(Unit.KNIGHT);
	}
	public Building initialize(int race, int x, int y) {
		return new Stable(race, x, y);
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
		Knight a = null;
		while(!found && tempcount++<20) {
			double ran = Math.random();
			if(ran<.25) {
				a=new Knight(getPlayer().race(),0,x()-Worker.WIDTH,(int) (y()-Worker.HEIGHT+Math.random()*(h()+Worker.HEIGHT)));
			} else if(ran<.5) {
				a=new Knight(getPlayer().race(),0,x()+w()+Worker.WIDTH,(int) (y()-Worker.HEIGHT+Math.random()*(h()+Worker.HEIGHT)));
			} else if(ran<.75) {
				a=new Knight(getPlayer().race(),0,(int) (x()-Worker.WIDTH+Math.random()*(w()+Worker.WIDTH)),y()-Worker.HEIGHT);
			} else {
				a=new Knight(getPlayer().race(),0,(int) (x()-Worker.WIDTH+Math.random()*(w()+Worker.WIDTH)),y()+h()+Worker.HEIGHT);
			}
			found = !myworld.doesthiscollide(a, 0, 0);
		}
		if(!found)
			return null;
		if(a==null)
			return null;
		a.setPlayer(getPlayer());
//		workerlist.add(a);
		timing=false;
		count=0;
		return a;
	}
	public Unit collectUnit(){
		if(knightlist.size()>0){
			return knightlist.remove(0);
		}
		return null;
	}
	public boolean hasUnits(){
		if(knightlist.size()>0){
			return true;
		}
		return false;
	}
	public void drawGUI(Graphics2D g, int x, int y, int w, int h) {
		super.drawbuildGUI(g, x, y, w, h);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 40));
		g.drawString("Stable", x+40, y+50);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Knight", x+345, y+100);
	}
	@Override
	public void setDifficulty(int diff) {
		ticcount=(int)(40/diff);
	}
}
