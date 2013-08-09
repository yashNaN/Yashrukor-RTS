package main;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import units.Unit;

import buildings.Building;


public class Camera{
	private World world;
	private int worldx;
	private int worldy;
	private int worldw;
	private int worldh;
	private int drawx;
	private int drawy;
	private int draww;
	private int drawh;
	/**
	 * in pixels
	 */
	public int movespeed;
	private final Image FOG;
	private final Image LFOG;
	public Camera(World sworld, int sx, int sy, int sw, int sh, int smovespeed) {
		worldx = sx;
		worldy = sy;
		worldw = sw;
		worldh = sh;
		world = sworld;
		movespeed = smovespeed;
		ImageIcon ii = new ImageIcon("Images/fog.png");
		FOG = ii.getImage();
		ii = new ImageIcon("Images/lightfog.png");
		LFOG = ii.getImage();
	}
	public void setBounds(int x, int y, int w, int h) {
		drawx = x;
		drawy = y;
		draww = w;
		drawh = h;
	}
	/**
	 * for x and y in the world
	 */
	public boolean inCamera(int x,int y){
		Point p = getOnScreen(x, y);
		if(p.x>drawx&&p.x<(drawx+draww)&&p.y>drawy&&p.y<drawy+drawh){
			return true;
		}
		return false;
	}
	/**
	 * for x and y in on the screen
	 */
	public boolean inCamera(Point onscreen){
		Point p = onscreen;
		if(p.x>drawx&&p.x<(drawx+draww)&&p.y>drawy&&p.y<drawy+drawh){
			return true;
		}
		return false;
	}
	public void drawSelect(Graphics2D g, Rectangle r) {
		g.setColor(Color.green);
		Point p = getOnScreen(r.x, r.y);//top left point to be drawn on screen
		Point botright = getOnScreen(r.x+r.width, r.y+r.height);// bottom right point, only to be used for finding width and height
		Point dim = new Point(botright.x-p.x, botright.y-p.y);// botright point - topleft point = width, height
		g.drawRect(p.x, p.y, dim.x, dim.y);
	}
	public static Rectangle fixRectangle(Rectangle r) {
		return fixRectangle(r.x, r.y, r.width, r.height);
	}
	public static Rectangle fixRectangle( int x, int y, int w, int h) {
		if(w<0) {
			x=x+w;
			w=w*-1;
		}
		if(h<0) {
			y=y+h;
			h=h*-1;
		}
		return new Rectangle(x, y, w, h);
	}
	public void move(int dx, int dy) {
		worldx=(int)(worldx+dx*movespeed*.01);
		if(worldx-world.maincamera.worldw/2<world.MINX){
			worldx=world.MINX+world.maincamera.worldw/2;
		}
		else if(worldx+world.maincamera.worldw/2>world.MAXX){
			worldx=world.MAXX-world.maincamera.worldw/2;
		}
		worldy=(int)(worldy+dy*movespeed*.01);
		if(worldy-world.maincamera.worldh/2<world.MINY){
			worldy=world.MINY+world.maincamera.worldh/2;
		}
		else if(worldy+world.maincamera.worldh/2>world.MAXY){
			worldy=world.MAXY-world.maincamera.worldh/2;
		}
	}
	public void moveTo(int x,int y){
		worldx=x;
		worldy=y;
	}
	public int getX(){
		return worldx;
	}
	public int getY(){
		return worldy;
	}
	public int getW(){
		return worldw;
	}
	public int getH(){
		return worldh;
	}
	public Point getPoint(int x, int y) {
		double rw = (double)draww/this.worldw;
		double rh = (double)drawh/(double)this.worldh;
		int onscreenx = (int) ((x-(drawx+draww/2))/rw+this.worldx);
		int onscreeny = (int) ((y-(drawy+drawh/2))/rh+this.worldy);
		return new Point(onscreenx, onscreeny);
	}
	public Rectangle convertToOnScreen(Rectangle r) {
		double rw = (double)draww/this.worldw;
		double rh = (double)drawh/this.worldh;
		Point newfocus = new Point((int)(drawx+draww/2), (int)(drawy+drawh/2));
		int dx = r.x-this.worldx;
		int dy = r.y-this.worldy;
		int newdx = (int) (dx*rw);
		int newdy = (int) (dy*rh);
		int neww = (int) (r.width*rw);
		int newh = (int) (r.height*rh);
		//System.out.println(newp.x+", "+newp.y+", "+newp2.x+", "+newp2.y);
		return Camera.fixRectangle(new Rectangle(newdx+newfocus.x, newdy+newfocus.y, neww, newh));
		
	}
	public Point getOnScreen(int x, int y) {
		double rw = (double)draww/this.worldw;
		double rh = (double)drawh/this.worldh;
		int dx = x-this.worldx;
		int dy = y-this.worldy;
		int newdx = (int) (dx*rw);
		int newdy = (int) (dy*rh);
		Point newfocus = new Point((int)(drawx+draww/2), (int)(drawy+drawh/2));
		return new Point(newdx+newfocus.x, newdy+newfocus.y);
	}
	public void paint(Graphics2D g){
		g.setColor(new Color(182,207,182));
		g.fillRect(drawx, drawy, draww, drawh);
		g.setColor(Color.black);
		g.drawRect(drawx, drawy, draww, drawh);
		
		for(Thing t : world.getAllThings()) {
			Rectangle r = convertToOnScreen(t.getBounds());
			if(world.doesPlayerHaveVision(world.thisplayer, t)) {
				t.draw(g, r.x, r.y, r.width, r.height);
				if(world.selected.contains(t)) {
					t.drawOutline(g, r.x, r.y, r.width, r.height);
				}
			}
		}
		for(int x=-this.worldw; x<this.worldw; x+=50) {
			for(int y=-this.worldh; y<this.worldh; y+=50) {
				if(!world.doesPlayerHaveVision(world.thisplayer, new Point(this.worldx+x, this.worldy+y))) {
					Rectangle r = convertToOnScreen(new Rectangle(this.worldx+x, this.worldy+y, 51, 51));
					g.drawImage(FOG, r.x, r.y, r.width, r.height, null);
				}
			}
		}

		if(world.isbuilding) {
			Point p =world.frame.currentMouse();
			p = this.getPoint(p.x, p.y);
			world.buildthis.setPosition(p.x-Building.WIDTH/2, p.y-Building.HEIGHT/2);
			Rectangle r = convertToOnScreen(world.buildthis.getBounds());
			world.buildthis.draw(g, r.x, r.y, r.width, r.height);
			g.drawImage(LFOG, r.x, r.y, r.width, r.height, null);
//			g.setColor(Color.green);
//			g.fillRect(r.x, r.y, r.width, r.height);
		}
		//draw boundaries
		g.setColor(Color.black);
		Rectangle r = convertToOnScreen(new Rectangle(this.world.MINX, this.world.MINY, this.world.MAXX-this.world.MINX, this.world.MAXY-this.world.MINY));
		g.drawRect(r.x, r.y, r.width, r.height);
		
		g.setColor(Color.red);
		g.drawRect(drawx, drawy, draww, drawh);
		
	}
	public void minimapPaint(Graphics2D g){
		g.setColor(Color.white);
		g.fillRect(drawx, drawy, draww, drawh);
		g.setColor(Color.black);
		g.drawRect(drawx, drawy, draww, drawh);
		double rw = (double)draww/this.worldw;
		double rh = (double)drawh/this.worldh;
		Point newfocus = new Point((int)(drawx+draww/2), (int)(drawy+drawh/2));
		for(Thing t : world.getAllThings()) {
			int dx = t.x()-this.worldx;
			int dy = t.y()-this.worldy;
			int newdx = (int) (dx*rw);
			int newdy = (int) (dy*rh);
			int neww = (int) (t.w()*rw);
			if(neww<4)
				neww=4;
			int newh = (int) (t.h()*rh);
			if(newh<4)
				newh=4;
			if(world.doesPlayerHaveVision(world.thisplayer, t)) {
				t.miniDraw(g, newdx+newfocus.x, newdy+newfocus.y, neww, newh);
			}
		}
//		for(Building b : world.getBuildings()) {
//			int dx = b.x()-this.worldx;
//			int dy = b.y()-this.worldy;
//			int newdx = (int) (dx*rw);
//			int newdy = (int) (dy*rh);
//			int neww = (int) (b.w()*rw);
//			int newh = (int) (b.h()*rh);
//			b.miniDraw(g, newdx+newfocus.x, newdy+newfocus.y, neww, newh);
//		}
//		for(Unit u : world.getUnits()) {
//			int dx = u.x()-this.worldx;
//			int dy = u.y()-this.worldy;
//			int newdx = (int) (dx*rw);
//			int newdy = (int) (dy*rh);
//			int neww = (int) (u.w()*rw);
//			int newh = (int) (u.h()*rh);
//			u.miniDraw(g, newdx+newfocus.x, newdy+newfocus.y, neww, newh);
//		}
		for(int x=-this.worldw/2; x<this.worldw/2; x+=200) {
			for(int y=-this.worldh/2; y<this.worldh/2; y+=200) {
				if(!world.doesPlayerHaveVision(world.thisplayer, new Point(this.worldx+x, this.worldy+y))) {
					Rectangle r = convertToOnScreen(new Rectangle(this.worldx+x, this.worldy+y, 201, 201));
					g.drawImage(FOG, r.x, r.y, r.width, r.height, null);
				}
			}
		}
		g.setColor(Color.black);
		Rectangle r = convertToOnScreen(new Rectangle(this.world.MINX, this.world.MINY, this.world.MAXX-this.world.MINX, this.world.MAXY-this.world.MINY));
		g.drawRect(r.x, r.y, r.width, r.height);
		
		g.setColor(Color.black);
		r = convertToOnScreen(new Rectangle(this.world.maincamera.worldx-this.world.maincamera.worldw/2, this.world.maincamera.worldy-this.world.maincamera.worldh/2, this.world.maincamera.worldw, this.world.maincamera.worldh));
		g.draw(r);
		//g.drawRect(drawx+draww/2, drawy+drawh/2, 1, 1);
	}
}
