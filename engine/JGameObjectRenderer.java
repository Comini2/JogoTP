import java.util.LinkedList;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;

class JGameObjectRenderer extends Thread{
	private static LinkedList<JGameObject> gOs = new LinkedList<>();
	
	public static void addGo(JGameObject gO){
		gOs.add(gO);
	}

	long lastTime = System.nanoTime();
	final double clock = 1000000000.0 / 60.0;
	double delta = 0;

	public void run(){
		while(true){
			long now = System.nanoTime();
			delta += (now - lastTime) / clock;
			for(JGameObject gO: gOs)
	        	gO.currAnim.delta +=(now - lastTime) / clock*gO.currAnim.speed;
			lastTime = now;
			for(JGameObject gO: gOs)
	        	while(gO.currAnim.delta >= 1){
	        		gO.currAnim.delta--;
	        		gO.currAnim.nextSprite();
	        	}
	        while(delta >= 1){
	        	delta--;
	        	for(JGameObject gO: gOs)
	        		drawGo(gO);
	        }
		}
	}

	private void drawGo(JGameObject gO){
		gO.repaint();
	}

}