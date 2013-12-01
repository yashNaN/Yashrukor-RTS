package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import units.Unit;

import buildings.Building;

public class Thing implements Collides{
	protected int x,y,w,h;
	protected int health;
	protected int maxhealth;
	private boolean isbeingattacked=false;
	public static World myworld;
	private int lasth;
	private boolean destroyed=false;
	private Player myplayer;
	protected ArrayList<Integer> commands = new ArrayList<Integer>();
	public int VISIONDISTANCE;
	public Thing(){
		VISIONDISTANCE = 200;
		commands.add(Action.STOP);
	}
	public boolean isInArea(Point pos, int halfx, int halfy){
		Rectangle area = new Rectangle (pos.x, pos.y, halfx, halfy);
		if(area.intersects(getBounds()))
		{
			return true;
		}
		return false;
	}
	public void tic(){
		if(lasth>health){
			isbeingattacked=true;
		}
		else{
			isbeingattacked=false;
		}
		lasth=health;
	}
	public boolean isBeingAttacked(){
		return isbeingattacked;
	}
	public World world(){
		return myworld;
	}
	public void setPlayer(Player p) {
		myplayer = p;
	}
	public void setColor(Graphics2D g) {
		g.setColor(myplayer.getColor());
	}
	public void drawOutline(Graphics2D g, int x, int y, int w, int h) {
		setColor(g);
		g.drawRect(x+1, y+1, w-2, h-2);
	}
	public Player getPlayer() {
		return myplayer;
	}
	public int x(){
		return x;
	}
	public int y(){
		return y;
	}
	public int w(){
		return w;
	}
	public int h(){
		return h;
	}
	public void setPosition(int newx,int newy) {
		x=newx;
		y=newy;
	}
	public void setMaxHealth(int h){
		maxhealth=h;
	}
	public void setHealth(int h) {
		health=h;
	}
	public int getMaxHealth(){
		return maxhealth;
	}
	public int getHealth()
	{
		return health;
	}
	public void heal(int amount){
		health+=amount;
		if(health>maxhealth){
			health=maxhealth;
		}
	}
	public void setSize(int newwidth,int newheight) {
		if(newwidth>=0)
			w=newwidth;
		if(newheight>=0)
			h=newheight;
	}
	public Rectangle getBounds() {
		return new Rectangle(x, y, w, h);
	}
	public boolean collides(Thing t){
		Rectangle threct = getBounds();
		Rectangle trect = t.getBounds();
		return threct.intersects(trect);
	}
	public boolean collides(Rectangle r){
		Rectangle threct = getBounds();
		Rectangle trect = r;
		return threct.intersects(trect);
	}
	public boolean collidesRange(int range,Thing t){
		Rectangle r = new Rectangle(x()-range, y()-range, w()+range*2, h()+range*2);
		if(r.intersects(t.getBounds()) || r.contains(t.getBounds())){
			return true;
		}
		return false;
	}
	public boolean collidesRange(int range, Point p)
	{
		Rectangle r = new Rectangle(x() - range, y()-range, w()+range*2, h()+range*2 );
		// need to add point part here
		Rectangle pRect = new Rectangle( p.x-range, p.y-range, range*2, range*2 );
		if(pRect.intersects(getBounds())){
			return true;
		}
		return false;
	}
	public boolean damage(int dmg){
		health-=dmg;
		if(health<=0) {
			destroyed=true;
			return true;
		}
		return false;
	}
	public boolean isDestroyed(){
		return destroyed;
	}
	public int health(){
		return health;
	}
	public int maxHealth(){
		return maxhealth;
	}
	public double distanceFrom(Thing t){
		return (Math.sqrt(Math.pow((x()-t.x()),2)+Math.pow((y()-t.y()),2)));
	}
	public double distanceFrom(Point p){
		return (Math.sqrt(Math.pow((x()-p.x),2)+Math.pow((y()-p.y),2)));
	}
	public void draw(Graphics2D g, int x, int y, int w, int h) {
		g.drawString("NOIMAGE", x, y+h);
	}
	public void miniDraw(Graphics2D g, int x, int y, int w, int h) {
		g.fillRect(x, y, w, h);
	}
	public void drawGUI(Graphics2D g, int x, int y, int w, int h) {
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Health:"+health+"/"+maxhealth, x+220, y+30);
	}
	public boolean collides(Point p){
		if(p.x>x&&p.x<x+w&&p.y>y&&p.y<y+h){
			return true;
		}
		return false;
	}
	public static World getWorld(){
		return myworld;
	}
	public boolean canSee(Thing other) {
		if(World.FOW) {
			return distanceFrom(other)<VISIONDISTANCE;
		}
		return true;
	}
	public boolean canSee(Point other) {
		if(World.FOW) {
			return distanceFrom(other)<VISIONDISTANCE;
		}
		return true;
	}
}
