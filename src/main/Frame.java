package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import units.*;

import buildings.*;


public class Frame extends JFrame implements ActionListener{
	private World world;
	private Timer timer;
	private GameMenu newgamemenu;
	private Point lastpoint;
	private Point currentmousepos = new Point(0, 0);
	private int w,h;
	private boolean leftmousedown;
	boolean shiftdown;
	boolean TUTORIAL;
	public Frame(){
    	this.setLayout(null);
    	this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
		shiftdown = false;
		w=ss.width;
		h=ss.height;
		this.setSize(new Dimension(w,h));		
		this.addKeyListener(new GameListener());
		this.addMouseListener(new GameListener());
		this.addMouseMotionListener(new GameListener());
        this.setLocationRelativeTo(null);
        this.setTitle("Yashrukor: Rise of the Son of Han");
        this.setResizable(false);
		this.setFocusable(true);
		this.validate();
		this.repaint();
        this.setVisible(true);
		this.requestFocus();
    	world=new World(this);
    	Thing.myworld = world;
		this.enterMainMenu();
		timer=new Timer(50, this);
        timer.start();
	}
	public Point currentMouse() {
		return currentmousepos;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		world.tic();
		if(currentmousepos.x<=10) {
			world.moveAllCameras(-180, 0);
		}
		if(currentmousepos.x>=getWidth()-10) {
			world.moveAllCameras(180, 0);
		}
		if(currentmousepos.y<=10) {
			world.moveAllCameras(0, -180);
		}
		if(currentmousepos.y>=getHeight()-10) {
			world.moveAllCameras(0, 180);
		}
		repaint();
	}
	/**
	 * Removes the <code>World</code> from the frame and adds and initializes 
	 * a <code>NewGameMenu</code>.
	 */
	public void enterMainMenu(){
    	this.remove(world);
    	newgamemenu=new GameMenu(this);
    	newgamemenu.setLocation(new Point(0, 0));
    	newgamemenu.setSize(Frame.this.getSize());
    	newgamemenu.setFocusable(false);
    	newgamemenu.setVisible(true);
    	newgamemenu.setBackground(Color.BLACK);
		this.add(newgamemenu);
    }
	/**
	 * Removes the <code>NewGameMenu</code> from the <code>Frame</code> and adds
	 * the <code>World</code> to the <code>Frame</code>.
	 */
	public void exitMenu(){
		this.remove(newgamemenu);
		newgamemenu=null;
		world.unpause();
		world.setLocation(new Point(0, 0));
		world.setSize(Frame.this.getSize());
    	world.setFocusable(false);
		world.setVisible(true);
		this.add(world);
	}
	/**
	 * Initializes new <code>World</code>. Which currently
	 * consists of just running its constructor.
	 */
	public void createNewWorld(MyButton m, int difficulty, boolean tutorial) {
		world=new World(this);
		TUTORIAL = tutorial;
		world.thisplayer.TUTORIAL = tutorial;
		if (m.getActionCommand() == "Beholder") {
			world.thisplayer.setHero(new Hero("Beholder", 0, 1000, 1000));
		}else if (m.getActionCommand() == "Prototype") {
			world.thisplayer.setHero(new Hero("Prototype", 0, 1000, 1000));
		}else if (m.getActionCommand() == "Slender") {
			world.thisplayer.setHero(new Hero("Slender", 0, 1000, 1000));
		}else if (m.getActionCommand() == "Finneo") {
			world.thisplayer.setHero(new Hero("Finneo", 0, 1000, 1000));
		}else if (m.getActionCommand() == "Tarba") {
			world.thisplayer.setHero(new Hero("Tarba", 0, 1000, 1000));
		}
		for(Player p:world.getPlayers()){
			if(p instanceof NPC){
				((NPC)p).setDifficulty(difficulty);
			}
		}
	}
	public boolean canBuild(int[] world,int[] building){
		if(world[0]>=building[0]&&world[1]>=building[1]&&world[2]>=building[2]){
			return true;
		}
		return false;
	}
	public void unitsDoAction(Point p, Thing target){
		if(target!=null) {
			for(Thing t : world.selected){
				if(t instanceof Worker) {
					if(target instanceof Building) {
						if(target.getPlayer()==t.getPlayer()) {
							Building b = (Building)target;
							if(!b.constructed){
								Worker w = (Worker)t;
								int abilitynumber = w.getAbilityNumber(Action.BUILD);
								if(shiftdown) {
									w.addAbility(abilitynumber, target);
								} else {
									w.useAbility(abilitynumber, target);
								}
							}
							else if(b.health()<b.maxHealth()){
								Worker w = (Worker)t;
								int abilitynumber = w.getAbilityNumber(Action.REPAIR);
								if(shiftdown) {
									w.addAbility(abilitynumber, target);
								} else {
									w.useAbility(abilitynumber, target);
								}
							}
						}
					}
				} 
				else if(t instanceof Unit && t.getPlayer()!=target.getPlayer()){
					Unit u = (Unit)t;
					int abilitynumber = u.getAbilityNumber(Action.ATTACK);
					if(shiftdown) {
						u.addAbility(abilitynumber, target);
					} else {
						u.useAbility(abilitynumber, target);
					}
				}
			}
		} 
		else{
			for(Thing t : world.selected){
				Point tar = new Point(p.x-t.w()/2, p.y-t.h()/2);
				if(t instanceof Unit){
					Unit u = (Unit)t;
					int abilitynumber = u.getAbilityNumber(Action.MOVE);
					if(shiftdown) {
						u.addAbility(abilitynumber, tar.x, tar.y);
					} else {
						u.useAbility(abilitynumber, tar.x, tar.y);
					}
				}
			}
		}
	}
	public class GameListener implements KeyListener, MouseListener, MouseMotionListener {
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_SHIFT)
				shiftdown = true;
			if(world!=null) {
				world.keyPressed(e.getKeyCode());
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_SHIFT)
				shiftdown = false;
			if(world!=null) {
				world.keyReleased(e.getKeyCode());
			}
		}
		@Override
		public void keyTyped(KeyEvent e) {
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			currentmousepos.x = e.getX();
			currentmousepos.y = e.getY();
		}
		@Override
		public void mouseExited(MouseEvent e) {
			currentmousepos.x = e.getX();
			currentmousepos.y = e.getY();
		}
		@Override
		public void mousePressed(MouseEvent e) {
			currentmousepos.x = e.getX();
			currentmousepos.y = e.getY();
			if(newgamemenu==null){
				if(e.getButton()==MouseEvent.BUTTON1) {
					lastpoint=world.getMainCamera().getPoint(e.getX(), e.getY());
					leftmousedown = true;
					if(world.whatIsHere(lastpoint)!=null){
						world.addDebug(world.whatIsHere(lastpoint).toString());
					}
				}
				if(world.vselected==true)
				{
					for(Thing t : world.selected){
						if(t instanceof Hero &&((Hero)t).getName().equals("Slender")){
							world.vselected = false;
							((Hero)t).blinkpos = lastpoint;
							if(shiftdown) {
								((Hero)t).addAbility((((Hero)t).getAbilityNumber(Action.BLINK)));
							} else {
								((Hero)t).useAbility(((Hero)t).getAbilityNumber(Action.BLINK));
							}
						}
					}
				}
				if(world.xselected == true)
				{
					for(Thing t: world.selected)
					{
						if(t instanceof Hero && ((Hero)t).getName().equals("Slender"))
						{
							world.xselected = false;
							if(shiftdown) {
								((Hero)t).addAbility(((Hero)t).getAbilityNumber(Action.BLADESDANCE), currentmousepos.x, currentmousepos.y);
							} else {
								((Hero)t).useAbility(((Hero)t).getAbilityNumber(Action.BLADESDANCE), currentmousepos.x, currentmousepos.y);
							}
						}	
					}
				}
				if(world.isbuilding==true&&canBuild(world.thisplayer.resources(),world.buildthis.resources())){
					boolean overlaps=false;
					Building f;
					f = world.buildthis.initialize(world.thisplayer.race(),lastpoint.x-Building.WIDTH/2,lastpoint.y-Building.HEIGHT/2);
					for(Building b:world.getBuildings()){
						if(b.collides(f)){
							overlaps=true;
						}
					}
					if(overlaps==false){
						for(Unit u:world.getUnits()){
							if(u.collides(f)){
								overlaps=true;
							}
						}
					}
					if(f.x()<world.MINX+10 || f.y()<world.MINY+10 || f.x()+f.w()>world.MAXX-10 || f.y()+f.h()>world.MAXY-10) {
						overlaps = true;
					}
					if(overlaps==false&&lastpoint.x>world.MINX&&lastpoint.x<(world.MAXX-100)&&lastpoint.y>world.MINY&&lastpoint.y<(world.MAXY-100)){
						world.isbuilding=false;
						world.buildthis = null;
						f.setPlayer(world.thisplayer);
						world.removeCosts(f,f.getPlayer());
						world.getBuildings().add(f);
						Worker w = world.getaselectedworker();
						if(w!=null) {
							int abilitynumber = w.getAbilityNumber(Action.BUILD);
							if(shiftdown) {
								w.addAbility(abilitynumber, f);
							} else {
								w.useAbility(abilitynumber, f);
							}
						}
					}
				}
//				if(world.getMainCamera().inCamera(x,y)&&world.addedunit==false&&SwingUtilities.isRightMouseButton(e)==true){
//					for(Thing b:world.selected){
//						((Building)b).makeUnit();
//					}
//				}
				
				/*
				else if(world.getMiniCamera().inCamera(x,y)){
					//MAKE BOTH CAMERAS MOVE TO X,Y
					//DOES NOT WORK
					Point p=world.getMiniCamera().getOnScreen(x, y);
					x=p.x;
					y=p.y;
					world.moveAllCameras(x, y);
				}
				*/
			}
		}
		public void bunchUnitsAround(Point p,ArrayList<Thing>units){
			int size=units.size();
			int addnexth=0;
			int addnextw=0;
			for(Thing u:units){
				u.setPosition(p.x+addnextw,p.y+addnexth);
				//((Unit) u).move(p.x, p.y);
				addnexth=addnexth+(((Unit)u).getHeight()/2);
				addnextw=addnextw+(((Unit)u).getWidth()/2);
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			currentmousepos.x = e.getX();
			currentmousepos.y = e.getY();
			if(newgamemenu==null){
				if(SwingUtilities.isLeftMouseButton(e)==true && !world.getMiniCamera().inCamera(currentmousepos)) {

					if(world.thisplayer.press(e))
						return;
					world.clearSelected();
					Point p = world.getMainCamera().getPoint(e.getX(), e.getY());
					world.select = Camera.fixRectangle(lastpoint.x, lastpoint.y, p.x-lastpoint.x, p.y-lastpoint.y);
					world.chooseThings();
					leftmousedown = false;
				}
//				int x=lastpoint.x;
//				int y=lastpoint.y;
				if(SwingUtilities.isRightMouseButton(e)==true && world.getMainCamera().inCamera(currentmousepos) && world.addedunit==true){
					Point p=world.getMainCamera().getPoint(e.getX(),e.getY());
					Thing target = world.getOneThingThatCollides(p);
					unitsDoAction(p,target);
				}
				if(SwingUtilities.isLeftMouseButton(e)==true && world.getMiniCamera().inCamera(currentmousepos)){
					Point p=world.getMiniCamera().getPoint(e.getX(),e.getY());
					world.maincamera.moveTo(p.x,p.y);
				}
				if(SwingUtilities.isRightMouseButton(e)==true && world.getMiniCamera().inCamera(currentmousepos) && world.addedunit==true){
					Point p=world.getMiniCamera().getPoint(e.getX(),e.getY());
					Thing target = world.getOneThingThatCollides(p);
					unitsDoAction(p,target);
				}
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			currentmousepos.x = e.getX();
			currentmousepos.y = e.getY();
			if(SwingUtilities.isLeftMouseButton(e)==true) {
				if(world.getMiniCamera().inCamera(currentmousepos)){
					Point p=world.getMiniCamera().getPoint(e.getX(),e.getY());
					world.maincamera.moveTo(p.x,p.y);
				} else {
					if(newgamemenu==null && leftmousedown){
						Point p = world.getMainCamera().getPoint(e.getX(), e.getY());
						world.select = Camera.fixRectangle(lastpoint.x, lastpoint.y, p.x-lastpoint.x, p.y-lastpoint.y);
					}
				}
			}
			
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			currentmousepos.x = e.getX();
			currentmousepos.y = e.getY();
		}
	}
	public void terminate() {
		System.exit(0);
	}
	
	public boolean isTutorial()
	{
		return TUTORIAL;
	}
	
}
