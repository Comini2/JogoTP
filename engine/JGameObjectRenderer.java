import java.util.LinkedList;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;

class JGameObjectRenderer extends Thread{

	private static JFrame mainWindow;
	private static LinkedList<JGameObject> gOs = new LinkedList<>();
	

	public JGameObjectRenderer(JFrame mainWindow){
		this.mainWindow = mainWindow;
	}

	public static void addGo(JGameObject gO){
		gOs.add(gO);
	}

	public static void prepareWindow(){
		for(JGameObject gO: gOs)
			mainWindow.add(gO);
		mainWindow.pack();
		mainWindow.addKeyListener(new Input());
		mainWindow.setSize(600, 600);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
	}


	public void run(){
		long lastTime = System.nanoTime();
		final double clock = 1000000000.0 / 60.0;
		double delta = 0;
		while(true){
			long now = System.nanoTime();
			delta += (now - lastTime) / clock;
			for(JGameObject gO: gOs){
				if(gO.currAnim != null){
		        	gO.currAnim.delta +=(now - lastTime) / clock*gO.currAnim.speed;
		        	while(gO.currAnim.delta >= 1){
		        		gO.currAnim.delta--;
		        		gO.currAnim.nextSprite();
		        	}
	        	}
	        }
	        lastTime = now;
	        while(delta >= 1){
	        	delta--;
	        	for(JGameObject gO: gOs)
	        		renderGo(gO);
	        }
		}
	}

	private void renderGo(JGameObject gO){
		gO.repaint();
	}

}