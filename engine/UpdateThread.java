import java.util.LinkedList;

class UpdateThread extends Thread{

	private static LinkedList<SimpleConduct> sCs = new LinkedList<>();
	private static Screen screen;

	UpdateThread(Screen screen){
		this.screen = screen;
	}

	public void run() {
		Conduct[] components;
		for(SimpleConduct sC : sCs){
			sC.start();
		}
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
	        screen.repaint();
	    }
	}

	public static void addSimpleConduct(SimpleConduct sc){
		sCs.add(sc);
	}
}