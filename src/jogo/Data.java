package jogo;

import java.awt.Point;

public class Data implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	Point position;
	int speed;
	boolean moving = false;
	boolean playerData = true;
	int direction;
	int id;
	
	
	public boolean isPlayerData() {
		return playerData;
	}
	public void setPlayerData(boolean playerData) {
		this.playerData = playerData;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Point getPosition() {
		return position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	public boolean isMoving() {
		return moving;
	}
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public String toString() {
		return "ID: " + id + " Position: " + position.toString();
	}
	
}
