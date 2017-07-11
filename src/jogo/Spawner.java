package jogo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

public class Spawner extends Thread {
	
	Vector<Data> zombies = null;
	int round = 2;
	
	Spawner(Vector<Data> zombies){
		this.zombies = zombies;
	}
	
	public void run() {
		do {
			if(zombies.size() <= 0) {
				round++;
				for(int i = 0; i<round*round; i++) {
					Data data = new Data();
					data.setPlayerData(false);
					data.setPosition(new Point(15*i, 10));
					data.setSpeed(5);
					
					zombies.add(data);
				}
			}
		}while(true);
	}

}
