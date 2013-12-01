package main;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;


public class MyButton extends JButton{
	/**
	 * The modes of this button, used to easily cycle through menus
	 */
	private int[] modes;
	/**
	 * <code>perx</code> is the percentage of the width of the <code>Frame</code> where this button should be put.
	 * For example:
	 * <code>perx</code>=.2 means that on a 1000 wide <code>Frame</code>, this button will go on 200 
	 */
	protected double perx;
	/**
	 * <code>pery</code> is the percentage of the height of the <code>Frame</code> where this button should be put.
	 * For example:
	 * <code>pery</code>=.2 means that on a 800 tall <code>Frame</code>, this button will go on 160 
	 */
	protected double pery;
	/**
	 * If <code>true</code>, will always be visible regardless of the current mode.
	 */
	private boolean alwaysvisible;
	/**
	 * The image of this button if custom graphic is desired.
	 */
	protected Image image;
	/**
	 * the color of this button, orange by default
	 */
	private Color color;
	/**
	 * basic constructor
	 * @param name the title of the button to appear on screen
	 * @param smode array of the possible modes
	 */
	public MyButton(String name, int[] smode, double perx, double pery) {
		super(name);
		modes = smode;
		this.perx = perx;
		this.pery = pery;
		alwaysvisible = false;
		color = Color.ORANGE;
	}
	/**
	 * advanced constructor if custom image is desired.
	 */
	public MyButton(String name, Image i, int[] smode, double x, double y) {
		super(name);
		image=i;
		modes = smode;
		perx = x;
		pery = y;
		alwaysvisible = false;
		color = Color.ORANGE;
	}
	/**
	 * sets color to c
	 * @param c the new Color
	 */
	public void setColor(Color c){
		color=c;
	}
	/**
	 * Sets <code>alwaysvisible</code> to <code>true</code>.
	 */
	public void setAlwaysVisible() {
		alwaysvisible = true;
	}
	
	public void setImage(ImageIcon ii){
		image=ii.getImage();
	}
	public void setImage(Image i){
		image=i;
	}
	public void removeImage(){
		image=null;
	}
	/**
	 * Sets <code>this</code> invisible if different mode and !<code>alwaysvisible</code>.
	 */
	public void refreshVisible(int newmode) {
		if(alwaysvisible) {
			this.setVisible(true);
		} else {
			for(int c=0; c<modes.length; c++) {
				if(newmode==modes[c]) {
					this.setVisible(true);
					return;
				}
			}
			this.setVisible(false);
		}
	}
	public void drawselected(Graphics2D g) {
		if(this.isVisible()) {
			g.setColor(Color.black);
			g.fillRect(this.getLocation().x-50, this.getLocation().y, this.getSize().width+100, this.getSize().height);
			g.setColor(Color.red);
			g.fillRect(this.getLocation().x-20, this.getLocation().y+10, this.getSize().width+40, this.getSize().height-20);
		}
	}
	/**
	 * Draws <code>this</code> if (<code>this.isVisible()</code>).
	 */
	public void draw(Graphics2D g) {
		if(this.isVisible()) {
			if(image==null){
				g.setColor(color);
				g.fillRect(this.getLocation().x, this.getLocation().y, this.getSize().width, this.getSize().height);
			} else if(image!=null){
				g.setColor(Color.BLACK);
				g.drawRect(this.getLocation().x, this.getLocation().y, this.getSize().width, this.getSize().height);
				g.drawImage(image,this.getLocation().x,this.getLocation().y,this.getSize().width,this.getSize().height,null);
			}
		}
	}
}
