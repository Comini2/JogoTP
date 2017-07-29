import java.util.LinkedList;

class UpdateThread extends Thread{

	private static LinkedList<SimpleConduct> sCs = new LinkedList<>();
	private static JGameObjectRenderer renderer;

	public UpdateThread(JGameObjectRenderer renderer){
		this.renderer = renderer;
	}

	public void run() {
		for(SimpleConduct sC : sCs)
	    		sC.start();

	    renderer.prepareWindow();
	    renderer.start();

	    long lastTime = System.nanoTime();
	    final double clock = 1000000000.0 / 60.0;
	    double delta = 0;

	    while (true) {
	        long now = System.nanoTime();
	        delta +=(now - lastTime) / clock;
	        lastTime = now;
	        while (delta >= 1){
	            delta--;
	            for(SimpleConduct sC : sCs)
	    			sC.update();
	        }
	    }
	}

	public static void addConduct(SimpleConduct sc){
		sCs.add(sc);
	}
}