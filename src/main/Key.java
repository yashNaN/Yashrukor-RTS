package main;

public class Key {
	/**
	 * whether or not this key is currently pressed
	 */
	private boolean pressed;
	/**
	 * used to find this key
	 */
	private String name;
	/**
	 * the KeyEvent id of this
	 * ex: KeyEvent.VK_UP
	 */
	private int id;
	/**
	 * used to know if you already checked this keypress
	 * ex: escape opens and closes menu, but if you hold down
	 * you dont want to continuously do the action
	 */
	public boolean checked;
	/**
	 * 
	 * @param sname ex: "w"
	 * @param sid ex: KeyEvent.VK_W
	 */
	public Key(String sname, int sid) {
		name = sname;
		id = sid;
		checked = false;
		pressed = false;
	}
	/**
	 * check if <code>this</code> is a key using <code>name</code>.
	 * @param other String to be checked
	 */
	public boolean is(String other) {
		return name.equals(other);
	}
	/**
	 * sets <code>pressed</code> to true and
	 * <code>checked</code> to false
	 */
	public void press() {
		pressed = true;
		uncheck();
	}
	/**
	 * sets <code>pressed</code> to false and
	 * <code>checked</code> to false
	 */
	public void release() {
		pressed = false;
		uncheck();
	}
	/**
	 * @return <code>pressed</code>
	 */
	public boolean pressed() {
		return pressed;
	}
	/**
	 * sets <code>checked</code> to true
	 */
	public void checked() {
		checked = true;
	}
	/**
	 * sets <code>checked</code> to false
	 */
	public void uncheck() {
		checked = false;
	}
	/**
	 * @return <code>name</code>
	 */
	public String name() {
		return name;
	}
	/** 
	 * (ex: KeyEvent.VK_S)
	 * @return <code>id</code> 
	 */
	public int id() {
		return id;
	}
}
