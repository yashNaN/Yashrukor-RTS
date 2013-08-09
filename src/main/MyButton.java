package main;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;


public class MyButton extends JButton{
	/**
	 * The mode of this button, used to easily cycle through menus
	 */
	//private int mode;
	private int[] modes;
	/**
	 * <code>perx</code> is the percentage of the width of the <code>Frame</code> where this button should be put.
	 * For example:
	 * <code>perx</code>=.2 means this button will go left of the center of the <code>Frame</code>
	 */
	protected double perx;
	/**
	 * <code>pery</code> is the percentage of the height of the <code>Frame</code> where this button should be put.
	 * For example:
	 * <code>pery</code>=.2 means this button will go above of the center of the <code>Frame</code>
	 */
	protected double pery;
	/**
	 * If <code>true</code>, will always be visible, even if the <code>mode</code> is switched.
	 */
	private boolean alwaysvisible;
	protected Image image;
	private Color color=Color.orange;
	//image=new ImageIcon("Images/Farm.gif").getImage();
	public MyButton(String name, int[] smode, double x, double y) {
		super(name);
		modes = smode;
		perx = x;
		pery = y;
		alwaysvisible = false;
	}
	public MyButton(String name,Image i, int[] smode, double x, double y) {
		super(name);
		image=i;
		modes = smode;
		perx = x;
		pery = y;
		alwaysvisible = false;
	}
	/**
	 * Sets <code>alwaysvisible</code> to <code>true</code>.
	 */
	public void setColor(Color c){
		color=c;
	}
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
//		if(alwaysvisible) {
//			this.setVisible(true);
//		} else {
			for(int c=0; c<modes.length; c++) {
				if(newmode==modes[c]) {
					this.setVisible(true);
					return;
				}
			}
			this.setVisible(false);
//		}
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
			}
			else if(image!=null){
				g.setColor(Color.RED);
				g.drawRect(this.getLocation().x, this.getLocation().y, this.getSize().width, this.getSize().height);
				g.drawImage(image,this.getLocation().x,this.getLocation().y,this.getSize().width,this.getSize().height,null);
			}
		}
	}
}
