package main;

public class Resources implements Comparable<Resources>{
	private int wood;
	private int gold;
	private int stone;
	private int food;
	public Resources(int wood, int gold, int stone, int food) {
		this.wood = wood;
		this.gold = gold;
		this.stone = stone;
		this.food = food;
	}
	public void addWood(int wood) {
		this.wood = this.wood+wood;
	}
	public int getWood() {
		return wood;
	}
	public void addGold(int gold) {
		this.gold = this.gold+gold;
	}
	public int getGold() {
		return gold;
	}
	public void addStone(int stone) {
		this.stone = this.stone+stone;
	}
	public int getStone() {
		return stone;
	}
	public void addFood(int food) {
		this.food = this.food+food;
	}
	public int getFood() {
		return food;
	}
	@Override
	public int compareTo(Resources other) {
		if(this.getGold()>=other.getGold() && this.getWood()>=other.getWood() && this.getStone()>=other.getStone() && this.getFood()>=other.getFood()) {
			if(this.getGold()==other.getGold() && this.getWood()==other.getWood() && this.getStone()==other.getStone() && this.getFood()==other.getFood()) {
				return 0;
			}
			return 1;
			
		}
		return -1;
	}
	public void subtract(Resources ref) {
		this.addFood(-ref.getFood());
		this.addWood(-ref.getWood());
		this.addStone(-ref.getStone());
		this.addGold(-ref.getGold());
	}
	public void add(Resources ref) {
		this.addFood(ref.getFood());
		this.addWood(ref.getWood());
		this.addStone(ref.getStone());
		this.addGold(ref.getGold());
	}
}
