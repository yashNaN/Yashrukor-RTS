package main;
import buildings.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import units.*;


public class World extends JPanel {
	/**
	 * disables of enables Fog of War
	 */
	public static final boolean FOW = false;
	/**
	 * left most boundary of world
	 */
	public final int MINX = -2000;
	/**
	 * top most boundary of world
	 */
	public final int MINY = -2000;
	/**
	 * right most boundary of world
	 */
	public final int MAXX = 2000;
	/**
	 * bottom most boundary of world
	 */
	public final int MAXY = 2000;
	/**
	 * debug output on screen, also used for game updates like Player soandso was defeated, etc
	 */
	private ArrayList<String> debug = new ArrayList<String>();
	/**
	 * Time in tics until oldest debug output String is removed
	 */
	private int removestring;
	/**
	 * amount of tics for debug ouput String to be removed
	 */
	private final int DEBUGSTRINGREMOVALSPEED = 80;
	
	public static final int MAINCAMERAMOVESPEED = 20;
	private ArrayList<Key> keyboard = new ArrayList<Key>();
	private ArrayList<Unit> units = new ArrayList<Unit>();
	private ArrayList<Building> buildings = new ArrayList<Building>();
	private ArrayList<Player> players = new ArrayList<Player>();
	Color[]colors={Color.red,Color.yellow,Color.green,Color.blue,Color.pink};
	boolean addedunit = false;
	public Player thisplayer;
	/**
	 * stops the Timer's actionPerformed from doing anything while this is true
	 */
	private boolean PAUSED=true;
	private boolean won=false;
	public Rectangle select;
	public ArrayList<Thing> selected = new ArrayList<Thing>();
	public Frame frame;
	/**
	 * this is the main player camera, it starts out centered at point 1000, 1300 
	 */
	public Camera maincamera = new Camera(this, 1100, 1300, 900, 600, MAINCAMERAMOVESPEED);
	/**
	 * This is the minimap camera, it sees a wide area, and doesnt move
	 */
	Camera minimap = new Camera(this, 0, 0, 4000, 4000,  MAINCAMERAMOVESPEED);
	public boolean vselected; 
	public boolean xselected;
	
	public Building buildthis;
	public boolean isbuilding=false;
	
	int[]yolo={0};
	public World(Frame frame){
		this.frame = frame;
		vselected = false;
		Thing.myworld = this;
		initializeImages();
		
		thisplayer = new Player(Unit.HUMAN, colors[2], this);
		thisplayer.setName("SirSwagalot");
		NPC enemy=new NPC(Unit.ORC,colors[0], this);
		enemy.setName("Badguy");
		NPC enemy2=new NPC(Unit.ORC,colors[3], this);
		enemy2.setName("Mr.Hanson");
		NPC enemy3=new NPC(Unit.ORC,colors[1], this);
		enemy3.setName("Mr. Little");
		players.add(enemy);
		players.add(thisplayer);
		players.add(enemy3);
		players.add(enemy2);
		//PAUSED = false;
		keyboard.add(new Key("w",KeyEvent.VK_W));//BARACKS
		keyboard.add(new Key("a",KeyEvent.VK_A));//ARCHERY RANGE
		keyboard.add(new Key("s",KeyEvent.VK_S));//STABLE
		keyboard.add(new Key("d",KeyEvent.VK_D));
		keyboard.add(new Key("b",KeyEvent.VK_B));//BARRACKS
		keyboard.add(new Key("c",KeyEvent.VK_C));//CHURCH
		keyboard.add(new Key("l",KeyEvent.VK_L));//LUMBERMILL
		keyboard.add(new Key("q",KeyEvent.VK_Q));//QUARRY
		keyboard.add(new Key("f",KeyEvent.VK_F));//FARM
		keyboard.add(new Key("t",KeyEvent.VK_T));//TOWER
		keyboard.add(new Key("h",KeyEvent.VK_H));//TOWNHALL
		keyboard.add(new Key("v",KeyEvent.VK_V));//BLINK
		keyboard.add(new Key("x",KeyEvent.VK_X));//BLADESDANCE
		keyboard.add(new Key("z",KeyEvent.VK_Z)); // Ability Button
		keyboard.add(new Key("e",KeyEvent.VK_E)); //toggle attack mode
		keyboard.add(new Key("space",KeyEvent.VK_SPACE));//SELECTED BUILDINGS BUILD
		keyboard.add(new Key("up",KeyEvent.VK_UP));//Camera MOVEMENT
		keyboard.add(new Key("left",KeyEvent.VK_LEFT));//Camera MOVEMENT
		keyboard.add(new Key("down",KeyEvent.VK_DOWN));//Camera MOVEMENT
		keyboard.add(new Key("right",KeyEvent.VK_RIGHT));//Camera MOVEMENT
		keyboard.add(new Key("esc",KeyEvent.VK_ESCAPE));//CANCEL
		keyboard.add(new Key("F10",KeyEvent.VK_F10));//Open MENU
		keyboard.add(new Key("del",KeyEvent.VK_DELETE));//SELL BUILDING
		this.setLayout(null);
		this.setBackground(Color.white);
		
		Worker work=new Worker(thisplayer.race(),0,500,850);
		work.setPlayer(thisplayer);
		units.add(work);
		
		Worker enwork=new Worker(enemy.race(),0,-400,-850);
		enwork.setPlayer(enemy);
		units.add(enwork);
		
		Worker enwork2=new Worker(enemy2.race(),0,400,-850);
		enwork2.setPlayer(enemy2);
		units.add(enwork2);
		
		Worker enwork3=new Worker(enemy3.race(),0,-400,850);
		enwork3.setPlayer(enemy3);
		units.add(enwork3);
		
//		Worker enwork2=new Worker(enemy.race(),0,-200,-850);
//		enwork2.setPlayer(enemy);
//		units.add(enwork2);
//		
//		Worker enwork3=new Worker(enemy.race(),0,-850,-500);
//		enwork3.setPlayer(enemy);
//		units.add(enwork3);


		TownHall th=new TownHall(1,MAXX-1000,MAXY-600);
		LumberMill lm = new LumberMill(1, MAXX - 600 , MAXY - 600);
		Farm fm = new Farm(1,MAXX - 800, MAXY-600);
		Quarry qu = new Quarry( 1,MAXX - 400, MAXY-600);
		qu.setFullyConstructed();
		qu.setPlayer(thisplayer);
		buildings.add(qu);
		fm.setFullyConstructed();
		fm.setPlayer(thisplayer);
		buildings.add(fm);
		lm.setFullyConstructed();
		lm.setPlayer(thisplayer);
		buildings.add(lm);
		th.setFullyConstructed();
		th.setPlayer(thisplayer);
		buildings.add(th);
		
		TownHall enemytw=new TownHall(0,MINX+400,MINY+400);
		enemytw.setFullyConstructed();
		enemytw.setPlayer(enemy);
		buildings.add(enemytw);
		
		TownHall enemytw2=new TownHall(0,MAXX-400,MINY+400);
		enemytw2.setFullyConstructed();
		enemytw2.setPlayer(enemy2);
		buildings.add(enemytw2);
		
		TownHall enemytw3=new TownHall(0,MINX+400,MAXY-400);
		enemytw3.setFullyConstructed();
	  	enemytw3.setPlayer(enemy3);
		buildings.add(enemytw3);
		
//		th.makeUnit(); 
		
		thisplayer.setHero(new Hero("Slender",0,1000,1000));
		
//		Healer heal=new Healer(thisplayer.race(), 45, 300, 500);
//		heal.setPlayer(thisplayer);
//		units.add(heal);
		
		
//		Archer enemya=new Archer(0,0,-300,-600);
//		enemya.setPlayer(enemy);
//		units.add(enemya);
		if(getFrame().isTutorial() == false)
		{
			enemy.setHero(new Hero("Slender",0,-1000,-1000));
			
			enemy2.setHero(new Hero("Beholder",0,1000,-1000));
			
			enemy3.setHero(new Hero("Finneo",0,-1000,1000));
		}
		
	}
	public void initializeImages() {
		Building.underconstruction = new ImageIcon("resources//images//underconstruction.png").getImage();
	}
	public Thing findClosestEnemy(Unit u) {
		Thing closest = null;
		int distofclosest = 0;
		for(Thing thing : getAllThings()) {
			if(thing.getPlayer()!=u.getPlayer()) {//check if it is an enemy
				if(u.canSee(thing)) {// check if Unit u can see thing
					int dist = (int) u.distanceFrom(thing);
					if(closest==null || dist<distofclosest) {
						closest = thing;
						distofclosest = dist;
					}
				}
			}
		}
		return closest;
	}
	/**
	 * 
	 * @return the first enemy inside AGRODISTANCE of Unit u
	 */
	public Thing findEnemy(Unit u) {
		for(Thing t : getAllThings()) {
			if(t.getPlayer()!=u.getPlayer()){
				if(u.canSee(t)) {
					int distance = (int) u.distanceFrom(t);
					if(distance<=Unit.AGRODISTANCE) {
						return t;
					}
				}
			}
		}
		return null;
	}
	/**
	 * @param u
	 * @return the closest Unit to u of the same Player
	 */
	public Unit findClosestFriendlyUnit(Unit u) {
		int mindistance = 0;
		Unit closest = null;
		for(Thing t : getAllThings()) {
			if(t instanceof Unit && t.getPlayer()==u.getPlayer()){
				int distance = (int) u.distanceFrom(t);
				if(closest == null || distance<mindistance) {
					closest = (Unit)t;
					mindistance = distance;
				}
			}
		}
		return closest;
	}
	/**
	 * used for the Healer to find what to heal
	 * @param u
	 * @return the closest Unit to u of the same Player or null of none exist
	 */
	public Unit findClosestDamagedFriendlyUnit(Unit u) {
		int mindistance = 0;
		Unit closest = null;
		for(Thing t : getAllThings()) {
			if(t instanceof Unit && t.getPlayer()==u.getPlayer() && ((Unit)t).health()<((Unit)t).maxHealth()){
				int distance = (int) u.distanceFrom(t);
				if(closest == null || distance<mindistance) {
					closest = (Unit)t;
					mindistance = distance;
				}
			}
		}
		return closest;
	}
	public Thing getOneThingThatCollides(Point p){
		for(Unit u:getUnits()){
			if(u.collides(new Point(p))){
				return u;
			}
		}
		for(Building b:getBuildings()){
			if(b.collides(p)){
				return b;
			}
		}
		return null;
	}
	/**
	 * returns true if even one of <code>Player</code> p's <code>Thing</code>s can see <code>Thing</code> t, 
	 * otherwise returns false.
	 */
	public boolean doesPlayerHaveVision(Player p, Thing t) {
		if(t instanceof Building) {
			if(((Building)t).detectedbyplayer) {
				return true;
			}
		}
		if(t.getPlayer()==p) {
			if(t instanceof Building)
				((Building)t).detectedbyplayer = true;
			return true;
		}
		for(Thing th : getPlayerThings(p)) {
			if(th.canSee(t)) {
				if(t instanceof Building)
					((Building)t).detectedbyplayer = true;
				return true;
			}
		}
		return false;
	}
	/**
	 * returns true if even one of <code>Player</code> p's <code>Thing</code>s can see <code>Point</code> t, 
	 * otherwise returns false.
	 */
	public boolean doesPlayerHaveVision(Player p, Point t) {
		for(Thing th : getPlayerThings(p)) {
			if(th.canSee(t)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * @return all <code>Thing</code>s that belong to <code>Player</code> <code>p</code>.
	 */
	public ArrayList<Thing> getPlayerThings(Player p) {
		ArrayList<Thing> everything = this.getAllThings();
		for(int a=everything.size()-1; a>=0; a--) {
			if(everything.get(a).getPlayer() == p) {
			} else {
				everything.remove(a);
			}
		}
		return everything;
	}
	public Frame getFrame()
	{
		return frame;
	}
	public Camera getCamera()
	{
		return maincamera;
	}
	public void refund(int[] b,Player p){
		p.addGold(b[0]);
		p.addWood(b[1]);
		p.addStone(b[2]);
	}
	public void refreshButtonPos(MyButton b){
		b.setLocation((int)(frame.getSize().width*b.perx-b.getSize().width/2), (int) (frame.getSize().height*b.pery-b.getSize().height/2));
	}
	public void collectResources() {
		for(Building b:buildings){
			if(b instanceof Farm){
				b.getPlayer().addFood(b.collectResources());
			}
			else if(b instanceof LumberMill){
				b.getPlayer().addWood(b.collectResources());
			}
			else if(b instanceof Quarry){
				b.getPlayer().addStone(b.collectResources());
			}
			else if(b instanceof TownHall){
				b.getPlayer().addGold(b.collectResources());
//				if(b.hasUnits()){
//					units.add(b.collectUnit());
//				}
			}
			else if(b.getType()==Building.ARCHERYRANGE||b.getType()==Building.BARRACKS||b.getType()==Building.CHURCH||b.getType()==Building.STABLE){
//				if(b.hasUnits()){
//					units.add(b.collectUnit());
//				}
			}
		}
	}
	public void tic() {
		if(!PAUSED) {
			this.collectResources();
			this.checkKeyPresses();
			for(int a=0; a<units.size(); a++ ){
				Unit t=units.get(a);
				if(t==null) {
					units.remove(a--);
				} else
				t.tic();
			}
			for(int a=0; a<buildings.size(); a++){
				Building b=buildings.get(a);
				if(b==null) {
					buildings.remove(a--);
				} else
				b.tic();
			}
			for(Player p:players){
				p.tic();
			}
			removeDestroyedThings();
			checkDeadPlayers();
		}
		removestring+=debug.size();
		if(debug.size()>=20)
			removestring+=DEBUGSTRINGREMOVALSPEED/10;
		if(removestring>DEBUGSTRINGREMOVALSPEED) {
			if(debug.size()!=0) {
				debug.remove(0);
				removestring=0;
			}
			removestring=0;
		}
	}
	/**
	 * adds String s to debug output list
	 * @param s is added to the end of Arraylist<String> debug
	 */
	public void addDebug(String s) {
		debug.add(s);
	}
	public void removeDestroyedThings() {
		if(this.getBuildings().size()>0) {
			for(int a=0; a<this.getBuildings().size(); a++) {
				Building b = this.getBuildings().get(a);
				if(b.isDestroyed()){
					this.getBuildings().remove(a);
					a--;
				}
			}
		}
		if(this.getUnits().size()>0) {
			for(int a=0;a<this.getUnits().size();a++) {
				Unit b = this.getUnits().get(a);
				if(!(b instanceof Hero)&&b.isDestroyed()) {
					this.getUnits().remove(a);
					a--;
				}
				else if(b instanceof Hero && b.isDestroyed()){
					((Hero)b).respawn();
				}
			}
		}
	}
	public void moveAllCameras(int x, int y) {
		maincamera.move(x, y);
		//minimap.move(x, y);
	}
	public Camera getMainCamera(){
		return maincamera;
	}
	public Camera getMiniCamera(){
		return minimap;
	}
	public void pause() {
		PAUSED = true;
	}
	public void unpause() {
		PAUSED = false;
	}
	public void openMenu() {
		pause();
		frame.enterMainMenu();
	}
	public void checkKeyPresses(){
		for(Key k : keyboard) {
			if(k.pressed()) {
				if(k.is("F10")){
					this.openMenu();
				} else if(k.is("esc")){
					selected.clear();
					isbuilding=false;
					buildthis = null;
				}
				else if(k.is("up")){
					moveAllCameras(0, -100);
				}
				else if(k.is("left")){
					moveAllCameras(-100, 0);
				}
				else if(k.is("down")){
					moveAllCameras(0, 100);
				}
				else if(k.is("right")){
					moveAllCameras(100, 0);
				}
				
				
				
				if(workerselected()){
					if(k.is("a")){
						isbuilding=true;
						buildthis=new ArcheryRange(0,0,0);
						buildthis.setPlayer(thisplayer);
					}
					else if(k.is("b")){
						isbuilding=true;
						buildthis=new Barracks(0,0,0);
						buildthis.setPlayer(thisplayer);
					}
					else if(k.is("c")){
						isbuilding=true;
						buildthis=new Church(0,0,0);
						buildthis.setPlayer(thisplayer);
					}
					else if(k.is("f")){
						isbuilding=true;
						buildthis=new Farm(0,0,0);
						buildthis.setPlayer(thisplayer);
					}
					else if(k.is("l")){
						isbuilding=true;
						buildthis=new LumberMill(0,0,0);
						buildthis.setPlayer(thisplayer);
					}
					else if(k.is("q")){
						isbuilding=true;
						buildthis=new Quarry(0,0,0);
						buildthis.setPlayer(thisplayer);
					}
					else if(k.is("s")){
						isbuilding=true;
						buildthis=new Stable(0,0,0);
						buildthis.setPlayer(thisplayer);
					}
					else if(k.is("t")){
						isbuilding=true;
						buildthis=new Tower(0,0,0);
						buildthis.setPlayer(thisplayer);
					}
					else if(k.is("h")){
						isbuilding=true;
						buildthis=new TownHall(0,0,0);
						buildthis.setPlayer(thisplayer);
					}
				}
				if(k.is("del")){
					for(Thing t:selected){
						if(t instanceof Building){
							Building b = (Building)t;
							if(!b.constructed) {
								b.getPlayer().refund(b.sell());
//								refund(((Building)t).sell(),t.getPlayer());
							}
						}
						t.damage(t.maxHealth());
					}
					selected.clear();
				}
				else if(k.is("z")){
					System.out.println("Z selected");
					for(Thing t : selected){
						if(t instanceof Hero){
							Hero h=(Hero)t;
							if(h.getName().equals("Slender")){
								vselected = false;
								if(frame.shiftdown) {
									h.addAbility(h.getAbilityNumber(Action.BLINK));
								} else {
									h.useAbility(h.getAbilityNumber(Action.BLINK));
								}
							}
							else if(h.getName().equals("Finneo")){
								if(frame.shiftdown) {
									h.addAbility(h.getAbilityNumber(Action.WARCRY));
								} else {
									h.useAbility(h.getAbilityNumber(Action.WARCRY));
								}
							}
							else if(h.getName().equals("Prototype"))
							{
								if(frame.shiftdown) {
									h.addAbility(h.getAbilityNumber(Action.ROCKETBARRAGE)); 
								} else {
									h.useAbility(h.getAbilityNumber(Action.ROCKETBARRAGE)); 
								}
							}
						}
					}
				}
				else if( k.is("x"))
				{
					for(Thing t : selected)
					{
						if(t instanceof Hero)
						{
							if(((Hero)t).getName().equals("Slender"))
							{
								((Hero)t).useAbility(((Hero)t).getAbilityNumber(Action.BLADESDANCE));
							}
						}
					}
				}
			}
			k.checked = true;
		}
	}
	public void keyPressed(int id) {
		for(Key k : keyboard) {
			if(k.getKeyCode()==id) {
				k.press();
			}
		}
	}
	public void keyReleased(int id) {
		for(Key k : keyboard) {
			if(k.getKeyCode()==id) {
				k.release();
				if(k.is("space")) {
					if(selected.size()>0&&addedunit==false){
						for(Thing t:selected){
							if(t instanceof Building) {
								Building b = (Building)t;
								b.tryToStartUnit(b.getPossibleUnitType());
							}
						}
					}
				}
				if(k.is("a")) {
					for(Thing t : selected){
						Point tar = null;
						if(this.getMiniCamera().inCamera(frame.currentMouse())){
							tar=this.getMiniCamera().getPoint(frame.currentMouse());
						} else if(this.getMainCamera().inCamera(frame.currentMouse())){
							tar=this.getMainCamera().getPoint(frame.currentMouse());
						}
						if(tar != null && t instanceof Unit){
							Unit u = (Unit)t;
							int abilitynumber = u.getAbilityNumber(Action.ATTACKMOVE);
							if(frame.shiftdown) {
								u.addAbility(abilitynumber, tar.x, tar.y);
							} else {
								u.useAbility(abilitynumber, tar.x, tar.y);
							}
						}
					}
				}
				if(k.is("e"))
				{
					for(Thing t : selected)
					{
						if(t instanceof Unit)
						{
							Unit u = (Unit)t;
							if(u.attackmode)
								u.attackmode = false;
							else
								u.attackmode = true; 
						}
					}
				}
			}
		}
		if(id==KeyEvent.VK_F1) {
			thisplayer.chooseidleworker();
		}
	}
//	public void removeCosts(Building b, Player p){
//		p.addWood(-b.costWood());
//		p.addGold(-b.costGold());
//		p.addStone(-b.costStone());
//	}
	/**
	 * 
	 * @return true if at least one worker is currently selected
	 */
	public boolean workerselected() {
		for(Thing t : selected) {
			if(t instanceof Worker) 
				return true;
		}
		return false;
	}
	public Worker getaselectedworker() {
		for(Thing t : selected) {
			if(t instanceof Worker) {
				return (Worker)t;
			}
		}
		return null;
	}
	public Worker getIdleWorker(Player p) { 
		for(Unit u : getUnits()) {
			if(u instanceof Worker && u.getPlayer()==p) {
				Worker w = (Worker)u;
				if(!w.busy) {
					return w;
				}
			}
		}
		return null;
	}
	public Worker getaworker(Player p) {
		for(Unit u : getUnits()) {
			if(u instanceof Worker && u.getPlayer()==p) {
				return (Worker)u;
			}
		}
		return null;
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		g.setColor(Color.white);
		g.fillRect(500, 500, 50, 50);
		maincamera.setBounds(20, 35, this.getWidth()-40,this.getHeight()-190);
		maincamera.paint((Graphics2D)g);
		
		if(select!=null) {
			select = Camera.fixRectangle(select);
			maincamera.drawSelect(g2d, select);
		}
		
		g.setColor(Color.black);
		thisplayer.draw(g2d, 0, 0, frame.getWidth(), frame.getHeight());
		
		//g.drawString("Gold: "+goldcount+", Wood: "+woodcount+", Stone: "+stonecount+", Food: "+foodcount+", Total Buildings: "+buildings.size()+", Total Units: "+units.size()+", Selected Units: "+selsize, 10,25);
		
//		for(MyButton b:buttons){
//			b.draw(g2d);
//		}1
		
//		g.setColor(Color.black);
//		g.fillRect(0, this.getHeight()-200, this.getWidth(), 200);
//		g.fillRect(0, 0, 20, this.getHeight());
//		g.fillRect(0, 0, this.getWidth(), 40);
//		g.fillRect(this.getWidth()-20, 0, 20, this.getHeight());
//		g.setColor(Color.white);
//		g.drawString("HUD", 50, this.getHeight()-150);
		
		minimap.setBounds(this.getWidth()-190, this.getHeight()-190, 180, 180);
		minimap.minimapPaint((Graphics2D)g);
		for(int a=0; a<debug.size(); a++) {
			g2d.setFont(new Font("Arial", 50, 30));
			g2d.setColor(Color.black);
			g2d.drawString(debug.get(a), getWidth()/2-100, getHeight()-200-a*32);
		}
		
	}
//	public boolean checkCollision(Rectangle r) {
//	}
	public class getResources implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			collectResources();
		}
	}
	public void clearSelected(){
		selected.clear();
	}
	public void chooseThings() {
		Rectangle r = select;
		if(r!=null) {
			for(Building b : buildings) {
				//if(b.getPlayer()==thisplayer&&(r.intersects(b.getBounds()) || r.contains(b.getBounds()))){
				if((r.intersects(b.getBounds()) || r.contains(b.getBounds()))){
					selected.add(b);
				}
			}
			addedunit = false;
			for(Unit u : units) {
				if(u.getPlayer()==thisplayer&&(r.intersects(u.getBounds()) || r.contains(u.getBounds()))){
					if(!addedunit && selected.size()>0) {
						selected.clear();
					}
					addedunit = true;
					selected.add(u);
				}
			}
		}
		select = null;
	}
	public ArrayList<Building> getBuildings() {
		return buildings;
	}
	public ArrayList<Unit> getUnits() {
		return units;
	}
	public ArrayList<Thing> getAllThings(){
		ArrayList<Thing> allThings = new ArrayList<Thing>();
		allThings.addAll(units);
		allThings.addAll(buildings);
		return allThings; 
	}
	
	public boolean doesthiscollide(Thing t, int dx, int dy) {
		Rectangle re = new Rectangle(t.getBounds().x+dx, t.getBounds().y+dy, t.getBounds().width, t.getBounds().height);
		
		for(Unit u : getUnits()) {
			if(u!=t && u!=null) {
				if(u.getBounds().intersects(re)) {
					return true;
				}
			}
		}
		for(Building b : getBuildings()) {
			if(b!=t) {
				if(b.getBounds().intersects(re)) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean doesthiscollide(Rectangle r) {
		Rectangle re = r;
		if(!(r.x<=MAXX && r.x+r.width>=MINX && r.y<=MAXY && r.y+r.height>=MINY)) {
			return true;
		}
		for(Unit u : getUnits()) {
			if(u.getBounds().intersects(re)) {
				return true;
			}
		}
		for(Building b : getBuildings()) {
			if(b.getBounds().intersects(re)) {
				return true;
			}
		}
		return false;
	}
	public Thing whatIsHere(Point p){
		for(Thing t:getAllThings()){
			if(t.collides(p)){
				return t;
			}
		}
		return null;
	}
	public Thing whatIsHere(Rectangle r, Thing notthis){
		for(Thing t:getAllThings()){
			if(t.collides(r)){
				if(t!=notthis)
					return t;
			}
		}
		return null;
	}
	public ArrayList<Player>getPlayers(){
		return players;
	}
	public void checkDeadPlayers(){
		ArrayList<Player>remove=new ArrayList<Player>();
		for(Player p: players){
			boolean ded=true;
			for(Building b:buildings){
				if(b.getPlayer().equals(p)){
					ded=false;
				}
			}
			if(ded==true){
				p.lose();
				remove.add(p);
			}
		}
		ArrayList<Unit>rem=new ArrayList<Unit>();
		for(Player p:remove){
			players.remove(p);
			for(Unit u:units){
				if(u.getPlayer().equals(p)){
					rem.add(u);
				}
			}
		}
		for(Unit u:rem){
			units.remove(u);
		}
		if(players.size()==1&&!won){
			won=true;
			players.get(0).win();
		}
	}
}
