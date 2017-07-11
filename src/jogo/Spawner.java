package jogo;

import java.awt.Point;
import java.util.Vector;

public class Spawner extends Thread {
	
	Vector<Zombie> zombies = null;
	int round = 2;
	
	Spawner(Vector<Zombie> zombies){
		this.zombies = zombies;
	}
	
	public void run() {
		do {
			if(zombies.size() <= 0) {
				round++;
				for(int i = 0; i<round*round; i++) {
					Zombie z = new Zombie(5, new Point(15*i, 10));
					zombies.add(z);
				}
			}
		}while(true);
	}

}
