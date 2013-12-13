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
	/**
	 * reference to the world this camera is representing
	 */
	private World world;
	/**
	 * boundaries of the place on the world this camera looks at
	 */
	private Rectangle worldcoordinates;
	/**
	 * coordinates of the place on screen this camera draws
	 */
	private Rectangle draw;
	/**
	 * in pixels
	 */
	public int movespeed;
	/**
	 * image for dark fog of war
	 */
	private final Image FOG;
	/**
	 * image for light fog of war
	 */
	private final Image LFOG;
	public Camera(World sworld, int worldx, int worldy, int worldw, int worldh, int smovespeed) {
		worldcoordinates = new Rectangle(worldx, worldy, worldw, worldh);
		draw = new Rectangle();
		world = sworld;
		movespeed = smovespeed;
		ImageIcon ii = new ImageIcon("resources//images//fog.png");
		FOG = ii.getImage();
		ii = new ImageIcon("resources//images//lightfog.png");
		LFOG = ii.getImage();
	}
	public void setBounds(int x, int y, int w, int h) {
		draw = new Rectangle(x, y, w, h);
	}
	/**
	 * for x and y in the world
	 */
	public boolean inCamera(int x,int y){
		Point p = getOnScreen(x, y);
		if(p.x>draw.x&&p.x<(draw.x+draw.width)&&p.y>draw.y&&p.y<draw.y+draw.height){
			return true;
		}
		return false;
	}
	/**
	 * for x and y in on the screen
	 */
	public boolean inCamera(Point onscreen){
		Point p = onscreen;
		if(p.x>draw.x&&p.x<(draw.x+draw.width)&&p.y>draw.y&&p.y<draw.y+draw.height){
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
		if(w<1)
			w=1;
		if(h<1)
			h=1;
		return new Rectangle(x, y, w, h);
	}
	public void move(int dx, int dy) {
		worldcoordinates.x=(int)(worldcoordinates.x+dx*movespeed*.01);
		if(worldcoordinates.x-world.maincamera.worldcoordinates.width/2<world.MINX){
			worldcoordinates.x=world.MINX+world.maincamera.worldcoordinates.width/2;
		}
		else if(worldcoordinates.x+world.maincamera.worldcoordinates.width/2>world.MAXX){
			worldcoordinates.x=world.MAXX-world.maincamera.worldcoordinates.width/2;
		}
		worldcoordinates.y=(int)(worldcoordinates.y+dy*movespeed*.01);
		if(worldcoordinates.y-world.maincamera.worldcoordinates.height/2<world.MINY){
			worldcoordinates.y=world.MINY+world.maincamera.worldcoordinates.height/2;
		}
		else if(worldcoordinates.y+world.maincamera.worldcoordinates.height/2>world.MAXY){
			worldcoordinates.y=world.MAXY-world.maincamera.worldcoordinates.height/2;
		}
	}
	public void moveTo(int x,int y){
		worldcoordinates.x=x;
		worldcoordinates.y=y;
	}
	public int getX(){
		return worldcoordinates.x;
	}
	public int getY(){
		return worldcoordinates.y;
	}
	public int getW(){
		return worldcoordinates.width;
	}
	public int getH(){
		return worldcoordinates.height;
	}
	public Point getPoint(Point p) {
		return getPoint(p.x, p.y);
	}
	public Point getPoint(int x, int y) {
		double rw = (double)draw.width/this.worldcoordinates.width;
		double rh = (double)draw.height/(double)this.worldcoordinates.height;
		int onscreenx = (int) ((x-(draw.x+draw.width/2))/rw+this.worldcoordinates.x);
		int onscreeny = (int) ((y-(draw.y+draw.height/2))/rh+this.worldcoordinates.y);
		return new Point(onscreenx, onscreeny);
	}
	public Rectangle convertToOnScreen(Rectangle r) {
		double rw = (double)draw.width/this.worldcoordinates.width;
		double rh = (double)draw.height/this.worldcoordinates.height;
		Point newfocus = new Point((int)(draw.x+draw.width/2), (int)(draw.y+draw.height/2));
		int dx = r.x-this.worldcoordinates.x;
		int dy = r.y-this.worldcoordinates.y;
		int newdx = (int) (dx*rw);
		int newdy = (int) (dy*rh);
		int neww = (int) (r.width*rw);
		int newh = (int) (r.height*rh);
		return Camera.fixRectangle(new Rectangle(newdx+newfocus.x, newdy+newfocus.y, neww, newh));
		
	}
	public Point getOnScreen(int x, int y) {
		double rw = (double)draw.width/this.worldcoordinates.width;
		double rh = (double)draw.height/this.worldcoordinates.height;
		int dx = x-this.worldcoordinates.x;
		int dy = y-this.worldcoordinates.y;
		int newdx = (int) (dx*rw);
		int newdy = (int) (dy*rh);
		Point newfocus = new Point((int)(draw.x+draw.width/2), (int)(draw.y+draw.height/2));
		return new Point(newdx+newfocus.x, newdy+newfocus.y);
	}
	public void paint(Graphics2D g){
		g.setColor(new Color(182,207,182));
		g.fillRect(draw.x, draw.y, draw.width, draw.height);
		g.setColor(Color.black);
		g.drawRect(draw.x, draw.y, draw.width, draw.height);
		
		for(Thing t : world.getAllThings()) {
			Rectangle r = convertToOnScreen(t.getBounds());
			if(world.doesPlayerHaveVision(world.thisplayer, t)) {
				t.draw(g, r.x, r.y, r.width, r.height);
				if(world.selected.contains(t)) {
					t.drawOutline(g, r.x, r.y, r.width, r.height);
				}
			}
		}
		for(int x=-this.worldcoordinates.width; x<this.worldcoordinates.width; x+=50) {
			for(int y=-this.worldcoordinates.height; y<this.worldcoordinates.height; y+=50) {
				if(!world.doesPlayerHaveVision(world.thisplayer, new Point(this.worldcoordinates.x+x, this.worldcoordinates.y+y))) {
					Rectangle r = convertToOnScreen(new Rectangle(this.worldcoordinates.x+x, this.worldcoordinates.y+y, 51, 51));
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
		g.drawRect(draw.x, draw.y, draw.width, draw.height);
		
	}
	public void minimapPaint(Graphics2D g){
		g.setColor(Color.white);
		g.fillRect(draw.x, draw.y, draw.width, draw.height);
		g.setColor(Color.black);
		g.drawRect(draw.x, draw.y, draw.width, draw.height);
		double rw = (double)draw.width/this.worldcoordinates.width;
		double rh = (double)draw.height/this.worldcoordinates.height;
		Point newfocus = new Point((int)(draw.x+draw.width/2), (int)(draw.y+draw.height/2));
		for(Thing t : world.getAllThings()) {
			int dx = t.x()-this.worldcoordinates.x;
			int dy = t.y()-this.worldcoordinates.y;
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
//			int dx = b.x()-this.worldcoordinates.x;
//			int dy = b.y()-this.worldcoordinates.y;
//			int newdx = (int) (dx*rw);
//			int newdy = (int) (dy*rh);
//			int neww = (int) (b.w()*rw);
//			int newh = (int) (b.h()*rh);
//			b.miniDraw(g, newdx+newfocus.x, newdy+newfocus.y, neww, newh);
//		}
//		for(Unit u : world.getUnits()) {
//			int dx = u.x()-this.worldcoordinates.x;
//			int dy = u.y()-this.worldcoordinates.y;
//			int newdx = (int) (dx*rw);
//			int newdy = (int) (dy*rh);
//			int neww = (int) (u.w()*rw);
//			int newh = (int) (u.h()*rh);
//			u.miniDraw(g, newdx+newfocus.x, newdy+newfocus.y, neww, newh);
//		}
		for(int x=-this.worldcoordinates.width/2; x<this.worldcoordinates.width/2; x+=200) {
			for(int y=-this.worldcoordinates.height/2; y<this.worldcoordinates.height/2; y+=200) {
				if(!world.doesPlayerHaveVision(world.thisplayer, new Point(this.worldcoordinates.x+x, this.worldcoordinates.y+y))) {
					Rectangle r = convertToOnScreen(new Rectangle(this.worldcoordinates.x+x, this.worldcoordinates.y+y, 201, 201));
					g.drawImage(FOG, r.x, r.y, r.width, r.height, null);
				}
			}
		}
		g.setColor(Color.black);
		Rectangle r = convertToOnScreen(new Rectangle(this.world.MINX, this.world.MINY, this.world.MAXX-this.world.MINX, this.world.MAXY-this.world.MINY));
		g.drawRect(r.x, r.y, r.width, r.height);
		
		g.setColor(Color.black);
		r = convertToOnScreen(new Rectangle(this.world.maincamera.worldcoordinates.x-this.world.maincamera.worldcoordinates.width/2, this.world.maincamera.worldcoordinates.y-this.world.maincamera.worldcoordinates.height/2, this.world.maincamera.worldcoordinates.width, this.world.maincamera.worldcoordinates.height));
		g.draw(r);
		//g.drawRect(draw.x+draw.width/2, draw.y+draw.height/2, 1, 1);
	}
}
