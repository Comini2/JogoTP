package jogo;

import java.awt.Image;
import java.awt.Point;

public class Player implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	int vida;
	int rotation;
	public int speed = 5;
	public int id;
	Point position;
	Weapon weapon;
	Image playerImage;
	
	Player(){
		position = new Point(50, 50);
	}
	
	public int getVida() {
		return vida;
	}
	public void setVida(int vida) {
		this.vida = vida;
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
