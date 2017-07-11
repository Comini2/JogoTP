package jogo;

import java.awt.Image;
import java.awt.Point;

public class Player extends Unit{
	
	private static final long serialVersionUID = 1L;
	
	public int id;
	Point position;
	Weapon weapon;
	Image playerImage;
	
	Player(){
		position = new Point(50, 50);
	}
	
	Player(int id){
		this.id = id;
		position = new Point(50, 50);
	}
	
	public Point getPosition() {
		return position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	public Weapon getWeapon() {
		return weapon;
	}
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}	
	
}
