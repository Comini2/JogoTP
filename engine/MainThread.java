import java.util.LinkedList;

class MainThread extends Thread{

	private LinkedList<JGameObject> gOs = new LinkedList<>();

	public void run() {
	    long lastTime = System.nanoTime();
	    final double clock = 1000000000.0 / 60.0;
	    double delta = 0;

	    while (true) {
	        long now = System.nanoTime();
	        delta +=(now - lastTime) / clock;
	        lastTime = now;
	        while (delta >= 1){
	            delta--;
	        }
	    }
	}

	//TODO: IMPLEMENTAR THREAD PRINCIPAL QUE MANTERA O LIFECICLE DA ENGINE.

}