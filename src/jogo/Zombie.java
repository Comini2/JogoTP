package jogo;

import java.awt.Point;

public class Zombie {
	int vida;
	int speed;
	int damage;
	int rotation;
	Point position;
	
	Zombie(){}
	
	Zombie(int vida, int speed, Point position){
		this.vida = vida;
		this.speed = speed;
		this.position = position;
	}
	
	public int getVida() {
		return vida;
	}
	public void setVida(int vida) {
		this.vida = vida;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public int getRotation() {
		return rotation;
	}
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	public Point getPosition() {
		return position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
}
