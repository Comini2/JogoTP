import java.util.LinkedList;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;

class JGameObjectRenderer extends Thread{
	private static LinkedList<JGameObject> gOs = new LinkedList<>();
	
	public static void addGo(JGameObject gO){
		gOs.add(gO);
	}

	//TODO: IMPLEMENTAR VELOCIDADES DIFERENTES PARA AS ANIMAÇÕES

	public void run(){
		while(true){
			try{
				sleep(1000/30);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			for(JGameObject gO: gOs)
				drawGo(gO);
		}
	}

	private void drawGo(JGameObject gO){
		gO.repaint();
	}

}