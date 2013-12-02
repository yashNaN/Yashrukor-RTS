package buildings;

import java.awt.Point;
import java.awt.Rectangle;

public class UnitButton {
	/**
	 * x and y are counted from bottom right of gui
	 * y is usually 0
	 */
	public Rectangle bounds;
	/**
	 * the type of unit, ex: Unit.HEALER or Unit.WORKER
	 */
	public int type;
	/**
	 * stored on screen coordinates of the last place this UnitButton was drawn
	 */
	private static int drawx, drawy;
	/**
	 * initializes the UnitButton. the bounds Rectangle is created by calculating x using number
	 * @param number is the amount of UnitButtons that have already been added to the building to calculate the amount x should shift.
	 * @param type the type of unit, ex: Unit.HEALER or Unit.WORKER
	 */
	public UnitButton(int number, int type) {
		this.type = type;
		bounds = new Rectangle(number*-60, 0, 50, 50);
	}
	/**
	 * updates stored on screen coordinates of the last place this UnitButton was drawn
	 * @param newx
	 * @param newy
	 */
	public void drawn(int newx, int newy) {
		drawx = newx;
		drawy = newy;
	}
	/**
	 * check if the point mouse is on this UnitButton using stored on screen coordinates of the last place this UnitButton was drawn
	 * @param mouse
	 * @return true if mouse is contained within the Rectangle drawx, drawy, bounds.width, bounds.height
	 */
	public boolean click(Point mouse) {
		return (mouse.x>drawx && mouse.x<drawx+bounds.width && mouse.y>drawy && mouse.y<drawy+bounds.height);
	}
}
