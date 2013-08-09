package main;

public class Action {
	public static final int MOVE = 1000;
	public static final int STOP = 1001;
	public static final int ATTACKMOVE = 1002;
	public static final int ATTACK = 1003;
	public static final int STUN = 1004; //STUN AND FREEZE ARE THE SAME
	public static final int BURN = 1005;
	public static final int SLUDGE = 1006; // for Beholder ** input coordinates
	public static final int BLINK = 1007; // for Slender  **input coordinates
	public static final int BLADESDANCE = 1008; // for Slender **input coordinates
	public static final int WARCRY = 1009; // for Finneo **input self
	public static final int LASTSTAND = 1010; // for Finneo  **input self
	public static final int ROCKETBARRAGE = 1011; // for Prototype ** input target
	public static final int GAURDIANSHIELD = 1012; // for Tarba  ** input self gaurdianshield around self
	public static final int HEAL = 1013; // for Tarba  ** input target
	public static final int BUILD = 1014;
	public static final int REPAIR = 1015;
	
	public int type;
	public int x;
	public int y;
	public boolean hascoordinates;
	public Thing target;
	public Action(int typ) {
		type = typ;
	}
	public Action(int typ, Thing starget) {
		type = typ;
		target = starget;
	}
	public Action(int typ, int xx, int yy) {
		type = typ;
		x = xx;
		y=yy;
		hascoordinates=true;
	}
}
