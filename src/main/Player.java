

package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import units.Hero;
import units.Worker;

public class Player {
	public static final int STARTINGGOLD = 100;
	public static final int STARTINGFOOD = 10;
	private String name;
	private int gold;
	private int wood;
	private int food;
	private int stone;
	private final int race;
	private Color color;
	protected World myworld;
	private Hero hero;
	private boolean lost=false;
	
	int ix, iy, iw, ih;

	private final Image IDLE;
	private final Image WOOD;
	private final Image FOOD;
	private final Image GOLD;
	private final Image STONE;
	
	
	boolean TUTORIAL;
	int stage = 0;
	int time = 0;
	public Player(int srace, Color c,World sworld) {
		myworld = sworld;
		race = srace;
		color=c;
		gold = STARTINGGOLD;
		wood = 100;
		food = STARTINGFOOD;
		stone = 100;
		ImageIcon ii = new ImageIcon("resources//images//idleworkerbutton.png");
		IDLE = ii.getImage();
		ii = new ImageIcon("resources//images//Wood.png");
		WOOD = ii.getImage();
		ii = new ImageIcon("resources//images//Gold.gif");
		GOLD = ii.getImage();
		ii = new ImageIcon("resources//images//Food.png");
		FOOD = ii.getImage();
		ii = new ImageIcon("resources//images//Stone.png");
		STONE = ii.getImage();
	}
	/**
	 * checks if clicked on the choose idle worker button
	 * @param e
	 * @return
	 */
	public boolean press(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(x>=30 && x<=30+40 && y>=iy+ih-210 && y<=iy+ih-210+40) {
			return chooseidleworker();
		}
		return false;
	}
	public boolean chooseidleworker() {
		Worker w = myworld.getIdleWorker(this);
		if(w!=null) {
			myworld.clearSelected();
			myworld.selected.add(w);
			myworld.maincamera.moveTo(w.x, w.y);
			return true;
		}

		return false;
	}
	public void draw(Graphics2D g, int x, int y, int w, int h) {
		ix = x; iy = y; iw = w; ih = h;
		g.setColor(Color.black);
		g.fillRect(x, y, 21, h);
		g.fillRect(x+w-20, y, 20, h);
		g.fillRect(x, y, w, 36);
		g.fillRect(x, y+h-155, w, 155);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
//		g.drawString("Gold: "+gold+", Wood: "+wood+", Stone: "+stone+", Food: "+food,10,25);
		g.drawImage(GOLD, 10, 10, 20, 20, null);
		g.drawString(gold+"", 31, 25);
		g.drawImage(WOOD, 80, 10, 20, 20, null);
		g.drawString(wood+"", 101, 25);
		g.drawImage(STONE, 150, 10, 20, 20, null);
		g.drawString(stone+"", 171, 25);
		g.drawImage(FOOD, 220, 10, 20, 20, null);
		g.drawString(food+"", 241, 25);
		
		if(myworld.selected.size()>0) {
			Thing t = myworld.selected.get(0);
			if(t!=null) {
				t.drawGUI(g, 0, y+h-155, w, 155);
			}
		}
		g.drawImage(IDLE, 30, y+h-210, 40, 40, null);
		g.setFont(new Font("Arial", Font.BOLD, 15));
		g.setColor(Color.black);
		g.drawString("F1", 50, y+h-210+38);
		
		if(lost) {
			g.setFont(new Font("Arial", Font.BOLD, 80));
			g.setColor(Color.CYAN);
			g.drawString("YOUR BASE WAS DESTROYED!", 50, y+h/2+20);
		}
		
		if(TUTORIAL) {
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.setColor(Color.RED);
			if(stage==0) {
				g.drawString("1: Left Click and drag creates a box which selects things", 50, 200);
				g.drawLine(80, 30, 100, 70);
				g.drawString("These are your resources, which are made by the TownHall(Gold), LumberMill(Wood), Quarry(Stone), and Farm(Food)", 105, 80);
			}
			if(stage==1) {
				g.drawString("2: Once you have a worker selected, you can move it by right clicking", 50, 200);
				g.drawLine(400, 650, 500, 500);
				g.drawString("This is a list of Buildings your Worker can build", 505, 510);
			}
			if(stage==2) {
				g.drawString("3: Try to build a LumberMill by pressing L and left clicking on an open spot", 50, 200);
				g.drawString("If the Worker doesnt want to build it, you may have to choose him and right click on the unfinished building", 50, 300);
			}
			if(stage==3) {
				g.drawString("4: Now build a Farm(F) and a barracks(B)", 50, 200);
				g.drawString("Once the barracks is constructed, you can choose it and press space to make a Warrior", 50, 300);
			}
			if(stage==4) {
				g.drawString("Defend yourself!", 50, 200);
				g.drawString("(Choose the warrior and right click on the enemy)", 50, 300);
			}
			if(stage==5) {
				g.drawString("6: Make another Worker out of your TownHall", 50, 200);
				g.drawString("(In case the first one dies)", 50, 300);
				g.drawString("Right click on your townhall with the worker to repair it if it is damaged", 50, 400);
				g.drawLine(700, 210, 700, 160);
				g.drawString("The Green bar is how much health the thing has left", 605, 160);
			}
			if(stage==6) {
				g.drawString("7: Another way to move is by pressing a", 50, 200);
				g.drawString("This tells your units to attack enemies on their way to the target", 50, 300);
				g.drawString("The goal is to keep your base alive and destroy all enemy bases", 50, 400);
				g.drawString("Enemies are located at the other 3 corners of the map", 50, 500);
			}
		}
		
		
	}
	public Color getColor(){
		return color;
	}
	public void setName(String n) {
		name = n;
	}
	public int race(){
		return race;
	}
	public void lose(){
		lost=true;
		System.out.println(name+" just lost.");
		myworld.addDebug(name+" just lost.");
	}
	public void win(){
		myworld.addDebug(name+" won.");
	}
	public World getWorld(){
		return myworld;
	}
	public void setHero(Hero h){
		if(!lost && getWorld().thisplayer.equals(this)){
			myworld.getUnits().remove(hero);
			hero=h;
			hero.setPlayer(this);
			myworld.getUnits().add(hero);
		}
	}
	public Hero getHero(){
		return hero;
	}
	public void tic(){

		if(TUTORIAL) {
			if(stage==0 || stage==1 || stage==2) {
				myworld.maincamera.moveTo(600, 800);
			}
			if(stage==3 || stage==4) {
				myworld.maincamera.moveTo(700, 900);
			}
			if(stage==5) {
				myworld.maincamera.moveTo(1000, 1500);
			}
			if(time++>200) {
				stage++;
				time=0;
				if(stage==3)
					time=-200;
			}
		}
	}
	public void addGold(int g){
		gold+=g;
	}
	public void addFood(int g){
		food+=g;
	}
	public void addWood(int g){
		wood+=g;
	}
	public void addStone(int g){
		stone+=g;
	}
	public int gold(){
		return gold;
	}
	public int wood(){
		return wood;
	}
	public int stone(){
		return stone;
	}
	public int food(){
		return food;
	}
	public int[] resources(){
		int[] ret={gold,wood,stone};
		return ret;
	}
}
