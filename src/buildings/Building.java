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

import units.Healer;
import units.Unit;


public abstract class Building extends Thing{
	public static final int HPBARHEIGHT = 8;
	public static final int HPBARDIST = 8;
	private int type,buildtime,race;
	private Resources cost;
	private Resources refund;
	Unit returnedspawn=null;
	public static final int UNDERCONSTRUCTIONVISIONDISTANCE = 150;
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
	private ArrayList<Unit> queue;
	

//	boolean timing=false;
	/**
	 * once count reaches ticcount, a unit is spawned
	 */
	int count=0;
	int ticcount=40;
	public abstract Building initialize(int race, int x, int y);
	public Building(int type,int health,int buildtime,int race,int woodcost,int stonecost,int goldcost,int spawntype,int x,int y){
		super();
		possibleUnits = new ArrayList<UnitButton>();
		queue = new ArrayList<Unit>();
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
		this.cost = new Resources(woodcost, goldcost, stonecost, 0);
		this.refund = new Resources(woodcost, goldcost, stonecost, 0);
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
		refund.addGold(-1);
		refund.addWood(-1);
		refund.addStone(-1);
		repair(constructspeed);
		if(construct>=constructtarget) {
			constructed = true;
			refund = new Resources(0, 0, 0, 0);
			return true;
		}
		return false;
	}
	/**
	 * instantly constructs the building fully, and sets it to full health
	 */
	public void setFullyConstructed() {
		construct = constructtarget;
		constructed = true;
		refund = new Resources(0, 0, 0, 0);
		setHealth(this.getMaxHealth());
	}
	/**
	 * @return true if Thing other is in VISIONDISTANCE of this, false if otherwise
	 */
	@Override
	public boolean canSee(Thing other) {
		if(constructed) {
			return distanceFrom(other)<VISIONDISTANCE;
		} else {
			return distanceFrom(other)<UNDERCONSTRUCTIONVISIONDISTANCE;
		}
	}
	/**
	 * @return true if Point other is in VISIONDISTANCE of this, false if otherwise
	 */
	@Override
	public boolean canSee(Point other) {
		if(constructed) {
			return distanceFrom(other)<VISIONDISTANCE;
		} else {
			return distanceFrom(other)<UNDERCONSTRUCTIONVISIONDISTANCE;
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
	public boolean repair(int howmuch) {
		this.heal(howmuch);
		if(health()==maxHealth()){
			return true;
		}
		return false;
	}
	public Resources getCost() {
		return cost;
	}
	public Resources sell() {
		if(refund.getGold()<0) {
			refund.addGold(-refund.getGold());
		}
		if(refund.getWood()<0) {
			refund.addWood(-refund.getWood());
		}
		if(refund.getStone()<0) {
			refund.addStone(-refund.getStone());
		}
		return refund;
	}
	public int getType(){
		return type;
	}
	public int collectResources(){
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
	public Unit makeUnit(int type) {
		if(constructed) {
			Unit newu = UnitFactory.createUnit(type);
			queue.add(newu);
			Building.myworld.addDebug("CREATED UNIT:"+type);
			return newu;
		}
		return null;
	}
	public void tic() {
		if(queue.size()>0){
			count++;
		}
		if(count>=ticcount && queue.size()>0){
			Unit newunit = queue.remove(0);
			initializeUnit(newunit);
//			Unit u = createandcollectUnit();
			if(newunit!=null)
				myworld.getUnits().add(newunit);
			count = 0;
		}
	}
	public void initializeUnit(Unit newunit) {
		boolean found = false;
		int tempcount = 0;
		while(!found) {
			tempcount++;
			double ran = Math.random();
			if(ran<.25) {
				newunit.setPosition(x()-newunit.getWidth()-(int)(Math.random()*tempcount), (int) (y()-newunit.getHeight()+Math.random()*(h()+newunit.getHeight())));
			} else if(ran<.5) {
				newunit.setPosition(x()+w()+5+(int)(Math.random()*tempcount), (int) (y()-newunit.getHeight()+Math.random()*(h()+newunit.getHeight())));
			} else if(ran<.75) {
				newunit.setPosition((int) (x()-newunit.getWidth()+Math.random()*(w()+newunit.getWidth())), y()-newunit.getHeight()-(int)(Math.random()*tempcount));
			} else {
				newunit.setPosition((int) (x()-newunit.getWidth()+Math.random()*(w()+newunit.getWidth())), y()+h()+5+(int)(Math.random()*tempcount));
			}
			found = !myworld.doesthiscollide(newunit, 0, 0);
		}
		newunit.setPlayer(getPlayer());
	}
	public abstract void setDifficulty(int diff);
	public void drawbuildGUI(Graphics2D g, int x, int y, int w, int h) {
		super.drawGUI(g, x, y, w, h);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Space to make ", x+200, y+100);
		g.setFont(new Font("Arial", Font.PLAIN, 40));
		for(int a=0; a<possibleUnits.size(); a++) {
			UnitButton ub = possibleUnits.get(a);
			g.setColor(Camera.BACKGROUND);
			g.fillRect(x+w-260+ub.bounds.x, y+h-60+ub.bounds.y, ub.bounds.width, ub.bounds.height);
			ub.drawn(x+w-260+ub.bounds.x, y+h-60+ub.bounds.y);
			g.setColor(Color.black);
			g.drawString(ub.type+"", x+w-250+ub.bounds.x, y+h-20+ub.bounds.y);
			g.drawImage(ub.image, x+w-260+ub.bounds.x, y+h-60+ub.bounds.y, ub.bounds.width, ub.bounds.height, null);
		}
		for(int a=0; a<queue.size(); a++) {
			g.setColor(Camera.BACKGROUND);
			g.fillRect(x+w-260-a*40, y+20, 30, 30);
			g.drawImage(queue.get(a).getImage(), x+w-260-a*40, y+20, 30, 30, null);
		}
		g.setColor(Color.white);
		g.drawString(""+queue.size(), x+200, y+150);
	}
	/**
	 * goes through UnitButtons and handles them accordingly
	 * @param mouse the point on screen that was clicked
	 */
	public void click(Point mouse) {
		if(this.constructed) {
			for(UnitButton ub : possibleUnits) {
				if(ub.click(mouse)) {
					tryToStartUnit(ub.type);
				}
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
			Unit newu = makeUnit(type);
			if(newu!=null && getPlayer().getResources().getGold()>=newu.getGoldcost() && getPlayer().getResources().getFood()>=newu.getFoodcost() && getPlayer().getResources().getWood()>=newu.getWoodcost()){
					getPlayer().addGold(-newu.getGoldcost());
					getPlayer().addFood(-newu.getFoodcost());
					getPlayer().addWood(-newu.getWoodcost());
			} else {
				myworld.addDebug("Not Enough Resources");
			}
		}
	}
	public void drawGUI(Graphics2D g, int x, int y, int w, int h) {
		super.drawGUI(g, x, y, w, h);
	}
}
