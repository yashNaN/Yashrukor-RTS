package buildings;
import main.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.Timer;

import units.*;


public class TownHall extends Building{
	//makes workers
	private int gold=0;
	private int goldtic=0;
	private ArrayList<Worker>workerlist=new ArrayList<Worker>();
	private String name="Town Hall";
	public TownHall(int race, int x, int y){
		super(Building.TOWNHALL, 4500, 300, race, 100, 100, 300, Unit.WORKER, x, y);
		image=new ImageIcon("resources//images//TownHall.gif").getImage();
		this.addPossibleUnit(Unit.WORKER);
	}
	public Building initialize(int race, int x, int y) {
		return new TownHall(race, x, y);
	}
	@Override
	public void tic(){
		super.tic();
		if(constructed){
			goldtic++;
		}
		if(goldtic>10){
			gold+=5;
			goldtic=0;
		}
	}
	@Override
	public int collectResources(){
		int ret=gold;
		gold=0;
		return ret;
	}
	public void drawGUI(Graphics2D g, int x, int y, int w, int h) {
		super.drawbuildGUI(g, x, y, w, h);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 40));
		g.drawString("TownHall", x+40, y+50);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Worker", x+345, y+100);
	}
	@Override
	public void setDifficulty(int diff) {
		ticcount=(int)(10/diff);
	}
}
