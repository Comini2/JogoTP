package jogo;

import java.awt.Point;

public class Player {

	int vida;
	Point position;
	Weapon weapon;
	
	public int getVida() {
		return vida;
	}
	public void setVida(int vida) {
		this.vida = vida;
	}
	public Point getPosition() {
		return position;
	}
	public void setPostion(Point position) {
		this.position = position;
	}
	public Weapon getWeapon() {
		return weapon;
	}
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}	
	
}
