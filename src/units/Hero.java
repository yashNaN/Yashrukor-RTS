package units;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import main.Action;
import main.Explosion;
import main.Frame;
import main.Thing;

import buildings.Building;


public class Hero extends Unit {
	String name;
	int respawntic;
	int direction;
	int origx,origy;
	int timer;
	int timerBlink;
	int timerBladesDance; 
	int timerWarCry; 
	int timerRocketBarrage;
	Frame heroframe = getWorld().getFrame();
	boolean warcrycheck;
	int mana, maxmana;
	boolean blinkrunning;
	private boolean laststand=false;
	private ArrayList<Point> blinkPoints = new ArrayList<Point>();
	private ArrayList<Point> bladePoints = new ArrayList<Point>();
	private ArrayList<Point> rocketPoints = new ArrayList<Point>();
	private ArrayList<Unit> allUnits = getWorld().getUnits();
	final int BLADESDANCEDAMAGE = 50;
	final int BLADESDANCERANGE = 100;
	final int ROCKETBARRAGEDAMAGE = 200;
	final int ROCKETBARRAGEDAMAGEAOE = 100;
	final int ROCKETBARRAGERANGE = 250;
	public Point blinkpos;
	/*
	 * 
	 * Beholder >> No abilites instead has VERY high health. But on the downside it has very low speed. This unit works
	 * 				as a tank. While it takes the damage the Player's other Units attack the enemy Units. 
	 * 				
	 * 				
	 * Slender >> Timer System: Blink: Able to teleport short distances timer.  Timer of 10 seconds
	 * 				Enable by pressing Z while a Slender is selected. 
	 * 
	 * 				Timer Blades Dance : Timer 10 seconds: Blinks to a target distance 
	 * 				and does damage in an area around the Hero. Timer of 20 seconds.
	 * 				Enable by pressing X key while Slender is selected. 
	 * 
	 * 
	 * Finneo >>  Timer System: Warcry: Moralizes nearby allies and increases there attack, move speed, and
	 * 							attack speed. enable the pressing Z key when a Finneo is selected
	 * 
	 * 							Last Stand: Passive ability that activates when on very low health. Triples attack
	 * 							and doubles attack speed. 
	 * 						
	 * Prototype >> This is the most powerful unit in the game. It has no abilities but very high stats. The prototype has really high damage,
	 * 				attack speed,and movement speed. But since it has no abilites it is made for beginner players who are just getting used to
	 * 				the game. Expert players will are encouraged to use other Heros to have a challenge and explore their abilities. 
	 * 
	 * Tarba >> No Abilites but has stat advantages. It has a very high move speed but to balance this out it has a very low attack
	 * 			This Hero would be used by the player as a scouting Unit or for raids on enemy Builders. 
	 * 
	 */
	ArrayList<String> manaHero = new ArrayList<String>();
	ArrayList<String> timerHero = new ArrayList<String>();
	ImageIcon icon=new ImageIcon("resources//images//Blank.png");
	//ArrayList<Move>moves=new ArrayList<Move>();
	public Hero(String heroname, int direction, int x, int y){
		super(2,0, direction, x, y,new ImageIcon("resources//images//Heroes/"+heroname+".gif"));
		VISIONDISTANCE = 700;
		blinkrunning = false;
		timerBlink = 0;
		timerBladesDance = 0;
		warcrycheck = false;
		timerWarCry = 0;
		timerRocketBarrage = 0; 
		origx=x;
		origy=y;
		this.direction=direction;
		name=heroname;
		int race=updateStatsAndReturnRace(heroname);
		updateSuperInfo(race);
		setSize(60,60);
		if(heroname.equals("Slender") || heroname.equals("Tarba"))
		{
			mana = 70;
			maxmana = 70;
		}
		else if(heroname.equals("Finneo") || heroname.equals("Prototype") || heroname.equals("Beholder"))
		{
			timer = 0;
		}
		if(heroname.equals("Slender"))
		{
			commands.add(Action.BLINK);
			commands.add(Action.BLADESDANCE);
		}	
		if(heroname.equals("Finneo"))
		{
			commands.add(Action.WARCRY);
			commands.add(Action.LASTSTAND);
			
		}
		if(heroname.equals("Prototype"))
		{
			commands.add(Action.ROCKETBARRAGE);
		}
	}
	public int getTimer()
	{
		return timer;
	}
	public void setTimer(int ti)
	{
		timer = ti; 
	}
	public void updateSuperInfo(int race){
		super.setRace(race);
	}
	public int getTimerWarcry()
	{
		return timerWarCry;
	}
	//Trial change
	@Override
	public void tic(){
		super.tic();
		System.out.println(getWorld().getFrame().isTutorial());
		if(getName().equals("Slender")){
			timerBlink--;
			timerBladesDance--;
		}
		if(getName().equals("Finneo")){
			timerWarCry--;
			if(timerWarCry <= 560 && warcrycheck )
			{
				warcrycheck = false;
				debuff();
			}
		}
		if(getName().equals("Prototype"))
		{
			if(timerRocketBarrage > 0)
				timerRocketBarrage--; 
		}
		if(actionqueue.size()==0) {
			return;
		}
		Action a = actionqueue.get(0);
		if(name.equals("Finneo")){
			if(health()<(maxhealth/4) && laststand == false){
				System.out.println("Running LastStand");
				lastStand();
			}
			else if(health()>=(maxhealth/4) && laststand){
				laststand=false;
				damage=damage/3;
				attackspeed=attackspeed/2;
			}
		}
		if((a.type == Action.BLINK && timer <= 0) && timerBlink <= 0){
			if(name.equals("Slender")){	
//				Point currentCamPoint = getWorld().getCamera().getPoint(x(),y());
				Point inworld = getWorld().getCamera().getPoint(getWorld().getFrame().currentMouse().x, getWorld().getFrame().currentMouse().y);
				blinkPoints.add(inworld);
				double eucliddistance = Math.sqrt((((blinkPoints.get(0).x - x())*(blinkPoints.get(0).x-x())) + ((blinkPoints.get(0).y - y()) * (blinkPoints.get(0).y-y()))));
//				
				//if((Math.abs(x()-blinkPoints.get(blinkPoints.size()-1).x)+ Math.abs(x()-blinkPoints.get(blinkPoints.size()-1).y)) <= 500)
				if(eucliddistance <= 300)
				{
						blinkrunning = false;
						//timer = 10;
//						System.out.println("In range");
//						System.out.println("Current Point: " + x() + ", " + y());
//						System.out.println("Blink Point:" + inworld.x + ", " + inworld.y );
						//this.setPosition(blinkPoints.get(blinkPoints.size()-1).x, blinkPoints.get(blinkPoints.size()-1).y);
						this.setPosition(blinkPoints.get(0).x, blinkPoints.get(0).y);
						timerBlink = 166; // sets Timer for Blink at 10 seconds
						actionqueue.remove(0);
						blinkPoints = new ArrayList<Point>();
				}
				else
				{
					blinkrunning = false;
					//timer = 0;
					//moveToward(blinkPoints.get(blinkPoints.size()-1).x - (x()-blinkPoints.get(blinkPoints.size()-1).x), blinkPoints.get(blinkPoints.size()-1).y -(y()-blinkPoints.get(blinkPoints.size()-1).y) );
					moveToward(blinkPoints.get(0).x, blinkPoints.get(0).y);
				}									
			}
		}
		else if( a.type == Action.BLADESDANCE && timerBladesDance <= 0){
			System.out.println("Running bladesdance");
			Point inworld = getWorld().getCamera().getPoint(getWorld().getFrame().currentMouse().x, getWorld().getFrame().currentMouse().y);
			bladePoints.add(inworld);
			double eucliddistance = Math.sqrt((((bladePoints.get(0).x - x())*(bladePoints.get(0).x-x())) + ((bladePoints.get(0).y - y()) * (bladePoints.get(0).y-y()))));
			if(eucliddistance <= 300)
			{
				this.setPosition(bladePoints.get(0).x,bladePoints.get(0).y);
				for( Thing t : allUnits)
				{
					//if(t.isInArea( new Point(x(),y()), t.w, t.h))
					if(t.collidesRange(100, this))
					{
						if(!(t.getPlayer().equals(getPlayer())))
						{
							//t.setHealth(getHealth() - 50);
							bladesDanceAttack(t);
						}
						//t.health -= 50;
					}
				}
				bladePoints = new ArrayList<Point>();
				timerBladesDance = 332; // timer for 20 seconds
				actionqueue.remove(0);
				
			}
			else
			{
				moveToward(bladePoints.get(0).x,bladePoints.get(0).y);
			}
		}
		else if(a.type==Action.WARCRY && timerWarCry <=0){
			warcry();
			System.out.println("Running WARCRY");
			timerWarCry = 664; // timer for 40 seconds;
			warcrycheck = true;
			actionqueue.remove(0);
		}
		else if(a.type == Action.ROCKETBARRAGE && name.equals("Prototype") && timerRocketBarrage <= 0){
			
			System.out.println("Starting rockets");
			//Point inworld = getWorld().getFrame().currentMouse();//getCamera().getPoint(getWorld().getFrame().currentMouse().x, getWorld().getFrame().currentMouse().y);
			//inworld = getWorld().getCamera().getOnScreen((int)inworld.getX(), (int)inworld.getY());
			Point inworld = new Point(a.x, a.y);
			rocketPoints.add(inworld);	
			double eucliddistance = Math.sqrt((((rocketPoints.get(0).x - x())*(rocketPoints.get(0).x-x())) + ((rocketPoints.get(0).y - y()) * (rocketPoints.get(0).y-y()))));
			if(eucliddistance < 400)
			{
				// This is real timer changed from this for testing timerRocketBarrage = 200;
				timerRocketBarrage = 50;// Need to change back to real timer
				Explosion explode = new Explosion((int)inworld.getX()-50,(int)inworld.getY()-50);
				world().getExplosions().add(explode);
				for(Thing t : allThings())
				{
					//System.out.println(t);
					if(t instanceof Unit)
						//getWorld().addDebug(t.toString());
					if(!(t.getPlayer().equals(getPlayer())))
					{
						if(t.collides(rocketPoints.get(0)))
						{
							abilityAttackNoRange(ROCKETBARRAGEDAMAGE, t);
							System.out.println("Rockets Launched");
						}
						else if(t.collidesRange(300, inworld))
						{
							abilityAttackNoRange(ROCKETBARRAGEDAMAGEAOE, t);
							System.out.println("Rockets Launched");
						}

					} 	

				}
				actionqueue.remove(0);
			
					
			}
			else
			{
				moveToward(rocketPoints.get(0).x, rocketPoints.get(0).y);
			}
		}
		else if(a.type == Action.GAURDIANSHIELD && name.equals ("Tarba") && timer <= 0){
			mana -= 20;
		}
		else if(a.type == Action.HEAL && name.equals("Tarba") && !(mana <= 0)){
			mana--; 
		}
		super.superTic();
	}
	public boolean abilityAttackNoRange( int damage, Thing target){
		if(target.damage(damage)){
			return true;
		}
		return false;
	}
	public boolean abilityAttack( int range, int damage, Thing target){
		if(this.collidesRange(range,target)){
			 if(target.damage(damage)){
				return true;
			}
		}
		return false;
	}
	public boolean bladesDanceAttack(Thing target){
		if(this.collidesRange(BLADESDANCERANGE,target)){
			 if(target.damage(BLADESDANCEDAMAGE)){
				return true;
			}
		}
		return false;
	}
	
	public int updateStatsAndReturnRace(String name){
//		if(heroframe.isTutorial() == true)
//		{
//			if(name.equals("Beholder")){
//				damage= 0;
//				movespeed=0;
//				setHealth(0);	
//				return 0;
//			}
//			else if(name.equals("Slender")){
//				damage=0;
//				movespeed=0;
//				setHealth(0);
//				return 0;
//			}
//			else if(name.equals("Finneo")){
//				damage= 0;
//				//movespeed=3;
//				movespeed=0;
//				setHealth(0);
//				return 1;
//			}
//			else if(name.equals("Prototype")){	
//				damage=0;
//				//damage = 5;
//				movespeed=0;
//				setHealth(0);
//				return 1;
//			}
//			else if(name.equals("Tarba")){
//				damage=0;
//				movespeed=0;
//				setHealth(0);
//				return 1;
//			}
//		}
//		else
//		{
			if(name.equals("Beholder")){
				damage= 30;
				movespeed=3;
				setHealth(1000);	
				return 0;
			}
			else if(name.equals("Slender")){
				damage=25;
				movespeed=8;
				setHealth(500);
				return 0;
			}
			else if(name.equals("Finneo")){
				damage= 25;
				//movespeed=3;
				movespeed=7;
				setHealth(450);
				return 1;
			}
			else if(name.equals("Prototype")){	
				damage=30;
				//damage = 5;
				movespeed=8;
				setHealth(700);
				return 1;
			}
			else if(name.equals("Tarba")){
				damage=15;
				movespeed=15;
				setHealth(400);
				return 1;
			}
			return -1;
	//	}
		//return -1;
		
	}
	public void respawn(){
		getPlayer().setHero(new Hero(name,direction,origx,origy));
	}
	public void getMoves(){
		
	}
	@Override
	public int getDamage(){
		return damage;
	}
	public String getName(){
		return name;
	}
	public void warcry(){
		for(Unit u:myworld.getUnits()){
			if(u.getPlayer().equals(getPlayer())&&u.collidesRange(range, this)){
				u.buff(10);
			}
		}
	}
	public void lastStand(){
		laststand=true;
		damage=damage*3;
		attackspeed=attackspeed*2;
	}
	public void drawGUI(Graphics2D g, int x, int y, int w, int h) {
		boolean blinkActive = false;
		super.drawGUI(g, x, y, w, h);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 40));
		g.drawString(name, x+40, y+50);
		if(getName().equals("Slender"))
		{
			g.drawString("Blink(z): ", x+40, y+110);
			if(timerBlink <= 0)
			{
				timerBlink = 0;
			}
			g.drawString(": " + timerBlink/10, x+150, y +110);
			if(timerBladesDance <= 0)
			{
				timerBladesDance = 0;
			}
			g.drawString("Blades Dance(x): " + timerBladesDance/10, x + 250, y+110);
		}
		if(getName().equals("Finneo"))
		{
			if(timerWarCry <= 0)
			{
				timerWarCry = 0;
			}
			g.drawString("Warcry(z): " + timerWarCry/10, x+40, y+110 );
		}
		if(getName().equals("Prototype"))
		{
			if(timerRocketBarrage <= 0)
				timerRocketBarrage = 0;
			g.drawString("Rocket Barrage(z): " + timerRocketBarrage/10, x+40, y+110);
		}
	}
	public String toString() {
		return "Hero";
	}
}
