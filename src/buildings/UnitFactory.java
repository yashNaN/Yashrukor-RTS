package buildings;

import units.*;

public class UnitFactory {
	public static Unit createUnit(int type) {
		Unit u = null;
		if(type==Unit.ARCHER) {
			u = new Archer(Unit.ORC, 0, 0, 0);
		}
		if(type==Unit.HEALER) {
			u = new Healer(Unit.ORC, 0, 0, 0);
		}
		if(type==Unit.KNIGHT) {
			u = new Knight(Unit.ORC, 0, 0, 0);
		}
		if(type==Unit.SWORDSMAN) {
			u = new Swordsman(Unit.ORC, 0, 0, 0);
		}
		if(type==Unit.WORKER) {
			u = new Worker(Unit.ORC, 0, 0, 0);
		}
		return u;
	}
}
