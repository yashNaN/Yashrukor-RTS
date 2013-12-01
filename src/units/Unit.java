package units;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import main.*;
import buildings.*;


public abstract class Unit extends Thing{
	public static final int AGRODISTANCE = 400;
	public static final int HPBARHEIGHT = 6;
	public static final int HPBARDIST = 6;
	public boolean attackmode; 
	protected int race;
	protected int type; // i changed this from static to non static since type shouldnt be static
	// since not all Units are of the same type 
	protected int movespeed=1;
	protected int damage=1;
	protected int range=100;
	protected int attackspeed;
	//protected int direction;
	protected int woodcost;
	protected int goldcost;
	protected int foodcost;
	private boolean buffed=false;
	private int bufftic=0;
	int buffduration=Integer.MAX_VALUE;
	// mana variables are just defined it is not used
	public static final int HERO=0;
	public static final int WORKER=1; 
	public static final int ARCHER=2;
	public static final int SWORDSMAN=3;
	public static final int KNIGHT=4;
	public static final int HEALER=5;
	public static final int ORC = 0;
	public static final int HUMAN = 1;
	public static final int NORTH = 0;
	public static final int NORTHEAST = 45;
	public static final int EAST = 90;
	public static final int SOUTHEAST = 135;
	public static final int SOUTH = 180;
	public static final int SOUTHWEST = 225;
	public static final int WEST = 270;
	public static final int NORTHWEST = 315;
	public static final int CREATIONTIME = 10;
	int moveX;
	int moveY;
	public Thing ttt=null;
	private ImageIcon icon;
	private boolean stopped=false;
	public boolean busy=false;
	public ArrayList<Action> actionqueue = new ArrayList<Action>();
	abstract public int getDamage();
	//static World W = new World();
	public Unit(int race,int type, int direction, int x, int y, ImageIcon icon){
		super();
		attackmode = false;
		VISIONDISTANCE = 500;
		commands.add(Action.MOVE);
		commands.add(Action.ATTACK);
		commands.add(Action.ATTACKMOVE);
		
		// this is a bit iffy i create a new world above how would we add everything to the same world? 
		if(x<getWorld().MINX){
			x=getWorld().MINX;
		}
		else if(x+w()>getWorld().MAXX){
			x=getWorld().MAXX-w();
		}
		if(y<getWorld().MINY){
			y=getWorld().MINY;
		}
		else if(y+h()>getWorld().MAXY){
			y=getWorld().MAXY-h();
		}
		boolean checking = true; 
		checking  = getWorld().doesthiscollide((Thing)this,0,0);
		if(!checking)
		{
			setPosition(x,y);
			moveX = x();
			moveY = y();
		}
		else
		{		
			this.x = -100000;
			this.y = - 1000000;
		}
		
		//this.maxhealth=health;
		//this.health=health;
		this.race=race;//0=orc,1=human
		this.type=type;//0=HERO,1=worker,2=archer,3=knight,4=swordsman,=5=healer
		//this.x=x;
		//this.y=y;
		//this.healthregen = healthregen;
		//this.direction = direction;
		this.icon=icon;
	}
	public void tic() {
		if(actionqueue.size()==0) {
			if(this instanceof Healer){
				Thing t = myworld.findClosestDamagedFriendlyUnit(this);
				if(t!=null && t.getPlayer()==this.getPlayer() && t.health()<t.maxHealth()) {
					if(t.distanceFrom(this)<range){
						t.heal(-this.getDamage());
					} else {
						this.moveToward(t.x()+t.w()/2, t.y()+t.h()/2);
					}
				}
			} else if(attackmode) {
				for(Thing t : allThings()) {
					if(t instanceof Unit) {
						if(this.distanceFrom(t) <= AGRODISTANCE) {
							insertAbility(Action.ATTACK, t);
						}
						else
							break;
					}
					else
						break;
				}
				
			}
			
		}
		if(actionqueue.size()==0)
			return;
		Action a = actionqueue.get(0);
		ttt=a.target;
		if(a.type==Action.MOVE && a.hascoordinates){
			if(this.moveToward(a.x, a.y)){
				actionqueue.remove(0);
			}
		}
		if(a.type==Action.ATTACKMOVE && a.hascoordinates) {
			Thing t = myworld.findClosestEnemy(this);
			if(t==null) {
				if(this.moveToward(a.x, a.y)){
					actionqueue.remove(0);
				}
			} else {
//				System.out.println("attackmoving: attack");
				this.insertAbility(this.getAbilityNumber(Action.ATTACK), t);
				a = actionqueue.get(0);
				ttt=a.target;
			}
		}
		if(!(this instanceof Healer) && a.type==Action.ATTACK && a.target!=null) {
			if(attack(a.target)){
				actionqueue.remove(0);
			} 
			else{
				if(!this.collidesRange(this.range, a.target)){
					this.moveToward(a.target.x()+a.target.w()/2, a.target.y()+a.target.h()/2);
				}
			}
		}
		if(a.type==Action.BUILD){
			if(this.collidesRange(this.range, a.target)){
				busy=true;
				Building b = (Building)a.target;
				if(b.construct()) {
					actionqueue.remove(0);
					busy=false;
				}
			} 
			else {
				this.moveToward(a.target.x()+a.target.w()/2, a.target.y()+a.target.h()/2);
				busy=true;
			}
		}
		if(a.type==Action.REPAIR){
			if(this.collidesRange(this.range, a.target)){
				busy=true;
				Building b = (Building)a.target;
				if(b.repair(b.constructspeed)) {
					actionqueue.remove(0);
					busy=false;
				}
			} 
			else {
				this.moveToward(a.target.x()+a.target.w()/2, a.target.y()+a.target.h()/2);
				busy=true;
			}
		}
		if(buffed==true){
			bufftic++;
		}
		if(bufftic>=buffduration){
			debuff();
		}
	}
	public void superTic(){
		super.tic();
	}
	public boolean moveToward(int x, int y) {
		int dx = x-this.x();
		int dy = y-this.y();
		double totaldistance = Math.sqrt(dx*dx+dy*dy);
		if(totaldistance<=this.movespeed) {//means this has arrived at target
			return true;
		}
		Point ta = alexgetpathtoward(dx, dy, 0);
		stopped=false;
		double ratio = this.movespeed/totaldistance;
		int scaledx = (int)(ta.x*ratio);
		int scaledy = (int)(ta.y*ratio);
		this.move(scaledx, scaledy);
		
//		Point ta = alexgetpathtoward2(dx, dy);
//		stopped=false;
//		if(!world().doesthiscollide(this, ta.x, ta.y))
//			this.move(ta.x, ta.y);
		

		dx = x-this.x();
		dy = y-this.y();
		totaldistance = Math.sqrt(dx*dx+dy*dy);
		if(totaldistance<=this.movespeed)
			stopped=true;
		return false;
	}
	public Point slidetoward(int dx, int dy) {
		double totaldistance = Math.sqrt(dx*dx+dy*dy);
		double ratio = this.movespeed/totaldistance;
		int scaledx = (int)(dx*ratio);
		int scaledy = (int)(dy*ratio);
		int finaldx = scaledx, finaldy = scaledy;
		if(this.world().doesthiscollide(this, scaledx, 0)) {
			if(finaldy>0) 
				finaldy+=Math.abs(finaldx);
			else
				finaldy-=Math.abs(finaldx);
//			finaldy-=finaldx;
			finaldx = 0;
		} else
		if(this.world().doesthiscollide(this, 0, scaledy)) {
			if(finaldx>0)
				finaldx+=Math.abs(finaldy);
			else
				finaldx-=Math.abs(finaldy);
//			finaldx-=finaldy;
			finaldy = 0;
		}
		return new Point(finaldx, finaldy);
	}
//	public Point alexgetpathtoward2(int dx, int dy) {// also alex work also dont touch
//		
//	}
	public Point alexgetpathtoward(int dx, int dy, int count) {// this is alex work dont touch
		if(count>200) {
//			System.out.println("reached max");
			return new Point(0, 0);
		}
		double totaldistance = Math.sqrt(dx*dx+dy*dy);
		double ratio = this.movespeed/totaldistance;
		int scaledx = (int)(dx*ratio);
		int scaledy = (int)(dy*ratio);
		if(this.world().doesthiscollide(this, (int) (scaledx*1.1), (int) (scaledy*1.1))) {
			double angle = Math.asin((double)dy/totaldistance);
			if(dx<0) {
				if(dy>0)
					angle = Math.PI-angle;
				if(dy<0)
					angle = -Math.PI-angle;
			}
			while(angle<0)
				angle+=Math.PI*2;
//			if(this instanceof Hero)
//				System.out.println("targetangle:"+(int)(angle*180/Math.PI));
			angle+=.1;
			double a=0;
			for(a=-Math.PI/2; a<angle; a+=Math.PI/4) {
			}
//			a+=Math.PI/2;
			if(this instanceof Hero)
				System.out.println("("+(int)(a*180/Math.PI)+")");
			angle = a;
//			angle+=Math.PI/16;
			int newy = (int) (totaldistance*Math.sin(angle));
			int newx = (int) (totaldistance*Math.cos(angle));
			return alexgetpathtoward(newx, newy, count+1);
		} else {
			return new Point(dx, dy);
		}
	}
	public ArrayList<Thing> allThings()
	{
		ArrayList<Thing> allThings = getWorld().getAllThings();
		return allThings;
	}
	public boolean attack(Thing target){
		if(this.collidesRange(this.getRange(),target)){
			if(this instanceof Healer){
				if(target instanceof Unit && target.getPlayer() == getPlayer()){
					if( ((Healer)this).heal(((Unit)target))/*target.damage(getDamage())*/) {
						return true;
					}
				}
			} 
			else if(target.damage(getDamage())){
				return true;
			}
		}
		return false;
		
		
		
//		if(getOneThingThatCollides(new Point(atkx,atky))!=null){
//			Thing t=getOneThingThatCollides(new Point(atkx,atky));
//			t.damage(damage);
//			allgone=false;
//		}
	}
	public void move(int dx, int dy) {
		int mx=this.x()+dx;
		int my=this.y()+dy;
		if(this.x()+dx<world().MINX){
			mx=world().MINX;
		}
		else if(this.x()+dx+w()>world().MAXX){
			mx=world().MAXX-w();
		}
		if(this.y()+dy<world().MINY){
			my=world().MINY;
		}
		else if(this.y()+dy+h()>world().MAXY){
			my=world().MAXY-h();
		}
		this.setPosition(mx,my);
	}
	public int getAbilityNumber(int action) {
		for(int a=0; a<this.commands.size(); a++) {
			if(commands.get(a)==action)
				return a;
		}
		return -1;
		
	}
	public void useAbility(int which) {
		if(which>=0 && which<commands.size()) {
			this.actionqueue.clear();
			this.actionqueue.add(new Action(commands.get(which)));
		}
	}
	public void useAbility(int which, int x, int y) {
		if(which>=0 && which<commands.size()) {
			this.actionqueue.clear();
			this.actionqueue.add(new Action(commands.get(which), x, y));
		}
	}
	public void useAbility(int which, Thing target) {
//		myworld.debug.add("using ability");
		if(which>=0 && which<commands.size()) {
//			myworld.debug.add("which is valid");
//			if(commands.get(which)==Action.ATTACK) {
				this.actionqueue.clear();
				this.actionqueue.add(new Action(commands.get(which), target));
//			}
		}
	}
	public void addAbility(int which) {
		if(which>=0 && which<commands.size()) {
			this.actionqueue.add(new Action(commands.get(which)));
		}
	}
	public void addAbility(int which, int x, int y) {
		if(which>=0 && which<commands.size()) {
			this.actionqueue.add(new Action(commands.get(which), x, y));
		}
	}
	public void addAbility(int which, Thing target) {
		if(which>=0 && which<commands.size()) {
			if(commands.get(which)==Action.ATTACK) {
				this.actionqueue.add(new Action(commands.get(which), target));
			}
		}
	}
	public void insertAbility(int which) {
		if(which>=0 && which<commands.size()) {
			this.actionqueue.add(0, new Action(commands.get(which)));
		}
	}
	public void insertAbility(int which, int x, int y) {
		if(which>=0 && which<commands.size()) {
			this.actionqueue.add(0, new Action(commands.get(which), x, y));
		}
	}
	public void insertAbility(int which, Thing target) {
		if(which>=0 && which<commands.size()) {
			if(commands.get(which)==Action.ATTACK) {
				this.actionqueue.add(0, new Action(commands.get(which), target));
			}
		}
	}
	public void setHealth(int h){
		maxhealth=h;
//		if(getTypeInt() == HERO )
//		{
//			health = h - 50;
//			super.setHealth(h - 300);
//		}
//		else
		{
			super.setHealth(h);
		}
		
	}
	public int getRange(){
		return range;
	}
	public void setIcon(ImageIcon i){
		this.icon=i;
	}
	public void setRace(int r){
		race=r;
	}
	public int getHeight(){
		return icon.getIconHeight();
	}
	public int getWidth(){
		return icon.getIconWidth();
	}
	public void draw(Graphics2D g, int x, int y, int w, int h) {
		g.drawImage(icon.getImage(),x,y,w,h,null);
		g.setColor(getPlayer().getColor());
		g.drawRect(x, y-HPBARHEIGHT-HPBARDIST, w, HPBARHEIGHT);
		double ratio = (double)this.health()/this.getMaxHealth();
		g.fillRect(x+1, y-HPBARHEIGHT-HPBARDIST+1, (int)(w*ratio-1), HPBARHEIGHT-1);
	}
	public void drawGUI(Graphics2D g, int x, int y, int w, int h) {
		super.drawGUI(g, x, y, w, h);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("MoveSpeed:"+movespeed, x+220, y+60);
		g.drawString("Damage:"+damage, x+400, y+30);
		g.drawString("Range:"+range, x+600, y+30);
		g.drawString("Attack Speed:"+attackspeed, x+400, y+60);
	}
	public void miniDraw(Graphics2D g, int x, int y, int w, int h) {
		g.setColor(getPlayer().getColor());
		g.fillRect(x,y,w,h);
	}
	public int getMaxHealth(){
		return maxhealth;
	}
//	public int getHealth(){
//		return health;
//	}
//	public int getHealthRegen()
//	{
//		return -1;
//	}
	
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
		else if(race==2){
			return "HERO";
		}
		return null;
	}
//	public void setMoveX(int x){
//		if(x<1){
//			x=1;
//		}
//		moveX = x;
//	}
//	public void setMoveY(int y){
//		if(y<1){
//			y=1;
//		}
//		moveY = y;
//	}
	public int getMoveX(){
		return moveX;
	}
	public int getMoveY(){
		return moveY;
	}
	public void buff(int duration){
		buffduration=duration*100;
		buffed=true;
		bufftic=0;
		movespeed=movespeed*2;
		attackspeed=attackspeed*2;
		damage=damage*2;
	}
	public void debuff(){
		buffed=false;
		buffduration=Integer.MAX_VALUE;
		movespeed=movespeed/2;
		attackspeed=attackspeed/2;
		damage=damage/2;
	}
	public String getTypeString(){
		if(type==WORKER){
			return "WORKER";
		}
		else if(type==ARCHER){
			return "ARCHER";
		}
		else if(type==KNIGHT){
			return "KNIGHT";
		}
		else if(type==SWORDSMAN){
			return "SWORDSMAN";
		}
		else if(type==HEALER){
			return "HEALER";
		}
		return null;
	}
	public void getLocation(){
		  
	}
	public boolean stopped(){
		return stopped;
	}
	public String toString() {
		return "Unit "+getClass()+". x:"+x()+", y:"+y()+", w:"+w()+", h:"+h();
	}
//	public boolean feelsLikeAttacking(Thing other) {
//		return (this.distanceFrom(other)<=AGRODISTANCE);
//	}
	
}
