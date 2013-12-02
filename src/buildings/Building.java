package buildings;
import main.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import units.Unit;


public abstract class Building extends Thing{
	public static final int HPBARHEIGHT = 8;
	public static final int HPBARDIST = 8;
	private int type,buildtime,race,wood,stone,gold,spawntype;//,spawntime;
	Unit returnedspawn=null;
	public static final int FARM=1;
	public static final int LUMBERMILL=2;
	public static final int QUARRY=3;
	public static final int TOWNHALL=4;
	public static final int ARCHERYRANGE=5;
	public static final int BARRACKS=6;
	public static final int CHURCH=7;
	public static final int STABLE=8;
	public static final int TOWER=9;
	protected Image image;
	public static Image underconstruction;
	
	public static final int WIDTH = 100, HEIGHT = 100;
	

	public int construct;
	public int constructtarget;
	public boolean constructed = false;
	public int constructspeed;
	
	public boolean detectedbyplayer = false;
	
	private ArrayList<UnitButton> possibleUnits;
	

	boolean timing=false;
	/**
	 * once count reaches ticcount, a unit is spawned
	 */
	int count=0;
	int ticcount=40;
	public abstract Building initialize(int race, int x, int y);
	public Building(int type,int health,int buildtime,int race,int wood,int stone,int gold,int spawntype,int x,int y){
		super();
		possibleUnits = new ArrayList<UnitButton>();
		VISIONDISTANCE = 400;
		super.setMaxHealth(health);
		this.setHealth(this.getMaxHealth()/10);
		constructed =false;
		construct=this.getMaxHealth()/10;
		constructtarget = this.getMaxHealth();
		constructspeed = constructtarget/buildtime;
		this.setPosition(x, y);
		this.setSize(WIDTH, HEIGHT);
		this.type=type;
		this.maxhealth=health;
		this.buildtime=buildtime;//in seconds
		this.race=race;//0=orc,1=human
		this.wood=wood;
		this.stone=gold;
		this.gold=gold;
		this.spawntype=spawntype;//type of unit it spawns
//		getSpawnTimes();
	}
	/**
	 * adds unit type poss to list of possible units to be produced, ex: Unit.ARCHER, Unit.HEALER
	 * @param poss is added to list of possible units this building can make
	 */
	public void addPossibleUnit(int poss) {
		possibleUnits.add(new UnitButton(possibleUnits.size(), poss));
	}
	/**
	 * 
	 * @return true if finished constructing, false if not
	 */
	public boolean construct() {
		construct+=constructspeed;
		repair(constructspeed);
		if(construct>=constructtarget) {
			constructed = true;
			return true;
		}
		return false;
	}
	public void setFullyConstructed() {
		construct = constructtarget;
		constructed = true;
		setHealth(this.getMaxHealth());
	}
	@Override
	public boolean canSee(Thing other) {
		if(constructed) {
			return distanceFrom(other)<VISIONDISTANCE;
		} else {
			return distanceFrom(other)<200;
		}
	}
	@Override
	public boolean canSee(Point other) {
		if(constructed) {
			return distanceFrom(other)<VISIONDISTANCE;
		} else {
			return distanceFrom(other)<200;
		}
	}
	public void draw(Graphics2D g, int x, int y, int w, int h) {
		if(constructed) {
			if(image!=null)
				g.drawImage(image, x, y, w, h, null);
		} 
		else {
			if(underconstruction!=null) 
				g.drawImage(underconstruction, x, y, w, h, null);
		}
		g.setColor(Color.black);
		g.drawRect(x, y, w, h);
		g.setColor(getPlayer().getColor());
		g.drawRect(x, y-HPBARHEIGHT-HPBARDIST, w, HPBARHEIGHT);
		double ratio = (double)this.health()/this.getMaxHealth();
		g.fillRect(x+1, y-HPBARHEIGHT-HPBARDIST+1, (int)(w*ratio-1), HPBARHEIGHT-1);
	}
	public void miniDraw(Graphics2D g,int x,int y,int w,int h){
		g.setColor(getPlayer().getColor());
		g.fillRect(x,y,w,h);
	}
//	public void getSpawnTimes(){
//		if(spawntype==1){
//			spawntime=5;
//		}
//		else if(spawntype==2||spawntype==3){
//			spawntime=10;
//		}
//		else if(spawntype==4||spawntype==5){
//			spawntime=20;
//		}
//	}
	public boolean repair(int howmuch) {
		this.heal(howmuch);
		if(health()==maxHealth()){
			return true;
		}
		return false;
	}
	public int[] resources(){
		int[] ret={costGold(),costWood(),costStone()};
		return ret;
	}
	public int[] sell(){
		int[] ret={(costGold())/2,(costWood())/2,(costStone())/2};
		return ret;
	}
	public Unit createandcollectUnit(){
		return null;
	}
	public void unitTimeStart(){
	}
//	public boolean hasUnits(){
//		return false;
//	}
	public int getType(){
		return type;
	}
	public int collectResources(){
		return 0;
	}
//	public int getUnitCost(){
//		return 0;
//	}
	public int getUnitGoldCost() {
		return 0;
	}
	public int getUnitWoodCost() {
		return 0;
	}
	public int getUnitFoodCost() {
		return 0;
	}
	public int getMaxHealth(){
		return maxhealth;
	}
	public int getBuildtime(){
		return buildtime;
	}
	public int getRaceInt(){
		return race;
	}
	public String getRaceString(){
		if(race==0){
			return "ORC";
		}
		else if(race==1){
			return "HUMAN";
		}
		return null;
	}
	public int costWood(){
		return wood;
	}
	public int costGold(){
		return gold;
	}
	public int costStone(){
		return stone;
	}
	public void makeUnit(int type) {
		if(constructed){
			timing=true;
		}
	}
	public abstract void tic();
	public abstract void setDifficulty(int diff);
	public void drawbuildGUI(Graphics2D g, int x, int y, int w, int h) {
		super.drawGUI(g, x, y, w, h);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Space to make ", x+200, y+100);
		g.setFont(new Font("Arial", Font.PLAIN, 40));
		for(int a=0; a<possibleUnits.size(); a++) {
			UnitButton ub = possibleUnits.get(a);
			g.setColor(Color.red);
			g.fillRect(x+w-260+ub.bounds.x, y+h-60+ub.bounds.y, ub.bounds.width, ub.bounds.height);
			ub.drawn(x+w-260+ub.bounds.x, y+h-60+ub.bounds.y);
			g.setColor(Color.black);
			g.drawString(ub.type+"", x+w-250+ub.bounds.x, y+h-20+ub.bounds.y);
		}
	}
	/**
	 * goes through UnitButtons and handles them accordingly
	 * @param mouse the point on screen that was clicked
	 */
	public void click(Point mouse) {
		for(UnitButton ub : possibleUnits) {
			if(ub.click(mouse)) {
				tryToStartUnit(ub.type);
			}
		}
	}
	public int getPossibleUnitType() {
		if(possibleUnits.size()>0) {
			return possibleUnits.get(0).type;
		}
		return -1;
	}
	public ArrayList<UnitButton> getPossibleUnits() {
		return possibleUnits;
	}
	public void tryToStartUnit(int type) {
		if(type!=-1) {
			if(getPlayer().gold()>=getUnitGoldCost() && getPlayer().food()>=getUnitFoodCost() && getPlayer().wood()>=getUnitWoodCost()){
				makeUnit(type);
				getPlayer().addGold(-getUnitGoldCost());
				getPlayer().addFood(-getUnitFoodCost());
				getPlayer().addWood(-getUnitWoodCost());
			} else {
				myworld.addDebug("Not Enough Resources");
			}
		}
	}
	public void drawGUI(Graphics2D g, int x, int y, int w, int h) {
		super.drawGUI(g, x, y, w, h);
	}
}
