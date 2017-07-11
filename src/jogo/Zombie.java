package jogo;

import java.awt.Point;

public class Zombie extends Unit{

	private static final long serialVersionUID = 1L;

	Zombie(){}
	
	Zombie(int speed, Point position){
		this.speed = speed;
		this.position = position;
	}
	
	
}
