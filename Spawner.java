import java.awt.Point;
import java.util.Random;

class Spawner implements Runnable{

	Player player[] = {new Player(), new Player()};
	Zombie zombie[] = new Zombie[1024];	
	
	int round = 0;
	private int quantity;
	private volatile boolean running = true;
	int screenWidth;
	int screenHeight;

	Spawner(int screenWidth, int screenHeight){
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		for(int i = 0; i<zombie.length; i++)
			zombie[i] = new Zombie();
	}
	
	public void terminate(){
		running = false;
	}

	public void nextRound(){
		try{
			Thread.sleep(5000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		

		round++;
		quantity = getQuantity();
		int angle = 360/quantity;

		for(int i = 0; i<quantity; i++){
			Point spawnPoint = new Point(screenWidth/2 + 200, screenHeight/2 + 200);
			int x = screenWidth/2 + (int)(spawnPoint.x*Math.cos(Math.toRadians(angle*i)));
			int y = screenHeight/2 + (int)(spawnPoint.y*Math.sin(Math.toRadians(angle*i)));
			zombie[i].x = x;
			zombie[i].y = y;
			zombie[i].health = 10 + (int)1.5*round;
		}
	}
	
	public int getQuantity(){
		return 2*round;
	}

	public int getRound(){
		return round;
	}
	
	public void updatePositions(){
		int deathCounter = 0;
		for(int i = 0; i<quantity; i++){
			if(zombie[i].health <= 0){
				zombie[i].x = -100;
				zombie[i].y = -100;
				deathCounter ++;
				continue;
			}
			int nearest = -1;
			int minDistance = 99999999;
			for(int j = 0; j<2; j++){
				int dist = Math.abs(player[j].x - zombie[i].x) + Math.abs(player[j].y - zombie[i].y);
				if(dist < minDistance){
					nearest = j;
					minDistance = dist; 
				}
			}

			int xDist = Math.abs(zombie[i].x - player[nearest].x);	
			int yDist = Math.abs(zombie[i].y - player[nearest].y);
			if(zombie[i].x < player[nearest].x && xDist > 25)
				zombie[i].x += 1;
			else if(zombie[i].y < player[nearest].y && yDist > 25)
				zombie[i].y += 1;
			else if(zombie[i].x > player[nearest].x && xDist > 25)
				zombie[i].x -= 1;
			else if(zombie[i].y > player[nearest].y && yDist > 25)
				zombie[i].y -= 1;
		}
		if(deathCounter >= quantity){
			nextRound();
		}
	}

	@Override
	public void run() {
		nextRound();
		while(running){
			updatePositions();
			try {
				Thread.sleep(1000/30);
			} catch (InterruptedException e) {
				e.printStackTrace();
				running = false;
			}
		}		
	}
	
}
