package main;

import java.awt.Rectangle;

public interface Collides {
	public Rectangle getBounds();
	public boolean collides(Thing t);
}
