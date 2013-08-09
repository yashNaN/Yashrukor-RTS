package main;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameMenu extends JPanel{
	int[] always={0,1,2,3,4};
	int[]bac={1,2,3,4};
	int[]zer={0};
	int[] one={1};
	int[] two={2};
	int[] thr={3};
	int[] fou={4};
	int mode=0;
	
	int diff=1;
	
	MyButton selected;
	
	//mode always
	MyButton back = new MyButton ("Back", bac, .09, .08);
	MyButton exit = new MyButton ("Exit", always, .91, .08);
	
	//mode 0
	MyButton newgame = new MyButton ("New Game", zer, .5, .3);
	//MyButton heroselection = new MyButton ("Hero Selection", zer, .5, .4);
	//MyButton difficultyselect = new MyButton ("Difficulty Selection", zer, .5, .5);
	MyButton play = new MyButton ("Resume", zer, .5, .7);
	MyButton tutorial = new MyButton ("Tutorial", zer, .5, .5);
	
	//mode 1 (singleplayer)
//	MyButton newgame = new MyButton ("NewGame", one, .5, .3);
//	MyButton savegame=new MyButton("SaveGame", one, 0.5,0.5);
//	MyButton loadgame = new MyButton("LoadGame", one, .5, .7);
	
	//mode 2
	ArrayList<MyButton> games=new ArrayList<MyButton>();
	MyButton Beholder = new MyButton ("Beholder", two, .5, .15);
	MyButton Slender = new MyButton ("Slender", two, .5, .3);
	MyButton Finneo = new MyButton ("Finneo", two, .5, .45);
	MyButton Prototype = new MyButton ("Prototype", two, .5, .6);
	MyButton Tarba = new MyButton ("Tarba", two, .5, .75);
	
	//mode 3
	MyButton Easy = new MyButton ("Easy", thr, .5, .2);
	MyButton Medium = new MyButton ("Medium", thr, .5, .4);
	MyButton Hard = new MyButton ("Hard", thr, .5, .6);
	
	//mode 4
	JTextField port2 = new JTextField();
	JTextField ip = new JTextField();
	MyButton connect2 = new MyButton ("Connect", fou, .5, .9);
	
	Frame frame;
	public GameMenu(Frame f) {
		frame = f;
		int[] x = {1, 2, 3};
		this.setLayout(null);
		initButton(back, 200, 40);
		initButton(exit, 200, 40);
		back.setAlwaysVisible();
		exit.setAlwaysVisible();
		//mode 0
		//initButton(heroselection, 200, 40);
		//initButton(difficultyselect,200,40);
		initButton(play, 200, 40);
		initButton(tutorial, 200, 40);
		//mode 1
		initButton(newgame, 200, 40);
		
		//mode 2
		initButton(Beholder, 200, 40);
		initButton(Tarba, 200, 40);
		initButton(Prototype, 200, 40);
		initButton(Slender, 200, 40);
		initButton(Finneo, 200, 40);
		selected = Beholder;
		
		initButton(Easy,200,40);
		initButton(Medium,200,40);
		initButton(Hard,200,40);
		
		activateMode(0);
	}
	public void initButton(MyButton b, int w, int h) {
		b.setSize(new Dimension(w, h));
		b.addActionListener(new ButtonListener());
		clearStyle(b);
		this.add(b);
	}
	public void refreshButtonPos(MyButton b) {
		b.setLocation((int)(this.getSize().width*b.perx-b.getSize().width/2), (int) (this.getSize().height*b.pery-b.getSize().height/2));
	}
	public void refreshPos() {
		for(Component c : this.getComponents()) {
			if(c instanceof MyButton) {
				refreshButtonPos((MyButton)c);
			}
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		refreshPos();
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		FontMetrics fm = g.getFontMetrics();
		//g2d.setFont(new Font("Arial", Font.PLAIN, 24));
		g2d.setFont(new Font("Arial", Font.ITALIC, 24));
		if(mode==0){
			Image background = Toolkit.getDefaultToolkit().getImage("Yashrukor.jpg");
			g2d.drawImage(background,0,0,getWidth(),getHeight(),null);
		}
		else if(mode==1){
			Image background = Toolkit.getDefaultToolkit().getImage("Lyzad.gif");
			g2d.drawImage(background,0,0,getWidth(),getHeight(),null);
		}
		else if(mode==2){
			Image background = Toolkit.getDefaultToolkit().getImage("Lavaroom.gif");
			g2d.drawImage(background,0,0,getWidth(),getHeight(),null);
		}
		else if(mode==3||mode==4){
			Image background = Toolkit.getDefaultToolkit().getImage("Ruins.gif");
			g2d.drawImage(background,0,0,getWidth(),getHeight(),null);
		}
		//g2d.setColor(new Color(50, 90, 71));
		//g2d.fillRect(0, 0, getWidth(), getHeight());
		

		
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, this.getWidth(), 30);
		g2d.setColor(Color.white);
		
		String title = frame.getTitle();
		int wid = fm.stringWidth(title);
		g2d.drawString(title, this.getWidth()/2-wid, 25);
		for(Component c : this.getComponents()) {
			if(c instanceof MyButton) {
				/*
				if(selected==c) {
					((MyButton)c).drawselected(g2d);
				}
				*/
				((MyButton)c).draw(g2d);
			}
		}
	}
	public void clearStyle (JButton b) {
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		b.setFont(new Font("Arial", Font.PLAIN, 16));
		b.setForeground(Color.BLACK);
	}
	private void activateMode(int newmode) {
		mode=newmode;
		for(Component c : this.getComponents()) {
			if(c instanceof MyButton) {
				((MyButton)c).refreshVisible(newmode);
			}
		}
	}
	public class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getActionCommand() == "Back") {
				if(mode==1||mode==2){
					activateMode(0);
				}
				else if(mode==3){
					activateMode(2);
				}
			}
			else if (arg0.getActionCommand() == "Exit") {
				frame.terminate();
			}
			else if (arg0.getActionCommand() == "SinglePlayer") {
				activateMode(1);
			}
			else if (arg0.getActionCommand() == "Play") {
				frame.exitMenu();
			}
			else if (arg0.getActionCommand() == "New Game") {
				//frame.createNewWorld(selected,diff, false);
				//frame.exitMenu();
				activateMode(2);
			}
			else if (arg0.getActionCommand() == "Tutorial") {
				frame.createNewWorld(selected,diff, true);
				System.out.println(frame.isTutorial());
				frame.exitMenu();
			}
//			else if (arg0.getActionCommand() == "SaveGame") {
//				saveGame();
//				activateMode(0);
//			}
//			else if (arg0.getActionCommand() == "LoadGame") {
//				loadGame();
//				activateMode(0);
//			}
			else if (arg0.getActionCommand() == "Hero Selection") {
				activateMode(2);
			}
			else if (arg0.getActionCommand() == "Beholder") {
				selected = Beholder;
				activateMode(3);
			}
			else if (arg0.getActionCommand() == "Prototype") {
				selected = Prototype;
				activateMode(3);
			}
			else if (arg0.getActionCommand() == "Slender") {
				selected = Slender;
				activateMode(3);
			}
			else if (arg0.getActionCommand() == "Finneo") {
				selected = Finneo;
				activateMode(3);
			}
			else if (arg0.getActionCommand() == "Tarba") {
				selected = Tarba;
				activateMode(3);
			}
			else if(arg0.getActionCommand()=="Difficulty Selection"){
				activateMode(3);
			}
			else if(arg0.getActionCommand()=="Easy"){
				diff=1;
				frame.createNewWorld(selected,diff, false);
				frame.exitMenu();
			}
			else if(arg0.getActionCommand()=="Medium"){
				diff=2;
				frame.createNewWorld(selected,diff, false);
				frame.exitMenu();
			}
			else if(arg0.getActionCommand()=="Hard"){
				diff=3;
				frame.createNewWorld(selected,diff, false);
				frame.exitMenu();
			}
//			else if (arg0.getActionCommand() == "Host New Game") {
//				activateMode(3);
//			}
//			else if (arg0.getActionCommand() == "Connect To Game") {
//				getOnlineGames();
//				activateMode(4);
//			}
			else if (arg0.getActionCommand() == "Create") {
//				frame.makeServer(54321);
			}
			else if (arg0.getActionCommand() == "Connect") {
//				frame.makeClient("localhost", 54321);
//				frame.makeClient("192.168.1.100", 54321);
			}
			for(MyButton b:games){
				if(arg0.getActionCommand()==b.getActionCommand()){
					//connectToGame(b.getActionCommand());
				}
			}
		}
	}
	public void saveGame(){
		
	}
	public void loadGame(){
		
	}
}
