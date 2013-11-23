package main;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import units.*;

import buildings.*;

public class NPC extends Player{
	ArrayList<Unit>myunits=new ArrayList<Unit>();
	ArrayList<Unit>enemyunits=new ArrayList<Unit>();
	ArrayList<Building>mybuildings=new ArrayList<Building>();
	ArrayList<Building>enemybuildings=new ArrayList<Building>();
	ArrayList<Building> neededbuildings = new ArrayList<Building>();
	ArrayList<Building>willbuild=new ArrayList<Building>();
	int unittic=0;
	int numofworkers = 0;
	int difficulty=1;
	public NPC(int srace, Color scolor, World w) {
		super(srace, scolor,w);
		neededbuildings.add(new TownHall(srace, 0, 0));
		neededbuildings.add(new LumberMill(srace, 0, 0));
		neededbuildings.add(new Quarry(srace, 0, 0));
		neededbuildings.add(new Farm(srace, 0, 0));
		neededbuildings.add(new Barracks(srace, 0, 0));
		neededbuildings.add(new ArcheryRange(srace, 0, 0));
//		neededbuildings.add(new Church(srace, 0, 0));
		neededbuildings.add(new Stable(srace, 0, 0));
		neededbuildings.add(new Tower(srace, 0, 0));
	}
	@Override
	public void tic(){
		getThings();
		double closest=Double.MAX_VALUE;
		for(Unit u:myunits){
			if(u instanceof Worker&&u.busy==false){
				checkToBuild((Worker)u);
			} 
			else if(!(u instanceof Worker)&&enemybuildings.size()>0){
				Building atk=enemybuildings.get(0);
				for(Building b:enemybuildings){
					if(b.getPlayer()!=this){
						if(b.distanceFrom(u)<=closest){
							closest=b.distanceFrom(u);
							atk=b;
						}
					}
				}
				assignBuildingToUnit(u,atk);
			}
		}
		unittic++;
		if(unittic==100){
			for(Building b:mybuildings){
				if(b instanceof TownHall) {
					if(Math.random()*100>numofworkers*10) {
						b.makeUnit();
					}
				} else {
					b.makeUnit();
				}
				unittic=0;
			}
		}
	}
	public void assignBuildingToUnit(Unit u,Building b){
		unitDoAction(u,new Point(b.x(),b.y()),b);
		if(u.stopped()||u.isBeingAttacked()){
			attackSurroundingsThings(u);
		}
	}
	public void attackSurroundingsThings(Unit u){
		if(enemyunits.size()>0||enemybuildings.size()>0){
			ArrayList<Thing>things=new ArrayList<Thing>();
			things.addAll(enemyunits);
			for(Building b:enemybuildings){
				if(b instanceof Tower){
					things.add(b);
				}
			}
			Thing atk=things.get(0);
			double closest=Double.MAX_VALUE;
			for (Thing en:things){
				if(u.distanceFrom(en)<=closest&&en.collidesRange(u.getRange(),en)){
					closest=u.distanceFrom(en);
					atk=en;
				}
			}
			unitDoAction(u,new Point(atk.x,atk.y),atk);
		}
	}
	public void getThings(){
		myunits.clear();
		enemyunits.clear();
		mybuildings.clear();
		enemybuildings.clear();
		numofworkers=0;
		for(Unit u:getWorld().getUnits()){
			if(u.getPlayer().equals(this)){
				myunits.add(u);
				if(u instanceof Worker) {
					numofworkers++;
				}
			}
			else{
				enemyunits.add(u);
			}
		}
		for(Building u:getWorld().getBuildings()){
			if(u.getPlayer().equals(this)){
				for(int i=0;i<willbuild.size();i++){
					if(u.equals(willbuild.get(i))){
						willbuild.remove(i);
						i--;
					}
				}
				u.setDifficulty(difficulty);
				mybuildings.add(u);
			}
			else{
				enemybuildings.add(u);
			}
		}
	}
	public Point getBuildLocation(Building in) {
		ArrayList<Building>allb=new ArrayList<Building>();
		allb.addAll(mybuildings);
		allb.addAll(willbuild);
		for (Building b : allb) {
			int[] pos = { -200, -200, 0, -200, 200, -200, -200, 0, 200, 0,
					-200, 200, 0, 200, 200, 200 };
			for (int a = 0; a < 16; a++) {
				int tr = (int) (Math.random() * 8);
				int dx = pos[tr];
				int dy = pos[tr + 1];
				Rectangle r = new Rectangle(b.x + dx, b.y + dy, 100, 100);
				if (!myworld.doesthiscollide(r)) {
					return new Point(b.x + dx, b.y + dy);
				}
			}
		}
		for(int a=0; a<100; a++) {
			myworld.debug.add("Randoming loc");
			int xx = (int)(Math.random()*(myworld.MAXX-myworld.MINX)-myworld.MINX);
			int yy = (int)(Math.random()*(myworld.MAXY-myworld.MINY)-myworld.MINY);
			Rectangle r = new Rectangle(xx, yy, 100, 100);
			if(!myworld.doesthiscollide(r)) {
				return new Point(xx, yy);
			}
		}
		return null;
	}
	public void unitDoAction(Unit actor,Point p, Thing target){
		if(actor instanceof Worker) {
			if(target.getPlayer()==actor.getPlayer()) {
				Building b = (Building)target;
				if(!b.constructed){
					Worker w = (Worker)actor;
					int abilitynumber = w.getAbilityNumber(Action.BUILD);
					w.useAbility(abilitynumber, target);
				}
				else if(b.health()<b.maxHealth()){
					Worker w = (Worker)actor;
					int abilitynumber = w.getAbilityNumber(Action.REPAIR);
					w.useAbility(abilitynumber, target);
				}
			}
		}
		else{
//			if(target!=null){
//				int abilitynumber = actor.getAbilityNumber(Action.ATTACK);
//				actor.useAbility(abilitynumber, target);
//			} 
//			else{
				int abilitynumber = actor.getAbilityNumber(Action.ATTACKMOVE);
				actor.useAbility(abilitynumber, p.x, p.y);
//			}
		}
	}
	public void checkToBuild(Worker w){
		Building b = needToBuild();
		if(b!=null){
			Point p = getBuildLocation(b);
			b.setPosition(p.x, p.y);
			getWorld().getBuildings().add(b);
			b.setPlayer(this);
			w.busy=true;
			unitDoAction(w,p,b);
		}
	}
	public Building needToBuild(){
		ArrayList<Building> current = new ArrayList<Building>();
		current.addAll(mybuildings);
		current.addAll(willbuild);
		for(Building b:neededbuildings) {
			boolean found = false;
			for(int a=0; !found && a<current.size(); a++) {
				Building bb = current.get(a);
				if(bb.getType() == b.getType()) {
					found = true;
				}
			}
			if(!found) {
				if(b.costWood()>this.wood()) {
					willbuild.add(new LumberMill(this.race(), 0, 0));
				} else if(b.costStone()>this.stone()) {
					willbuild.add(new Quarry(this.race(), 0, 0));
				} else if(b.costGold()>this.gold()) {
					willbuild.add(new TownHall(this.race(), 0, 0));
				} else {
					willbuild.add(b);	
				}
//				myworld.debug.add("building "+b);
				return b;
			}
		}
		return null;
	}
	public void setDifficulty(int diff){
		difficulty=diff;
	}
}
