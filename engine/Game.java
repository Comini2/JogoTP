import javax.swing.JFrame;
import java.awt.geom.Point2D;

class Game{

	Game(){
		JFrame window = new JFrame("Teste");
		new Player();
		JGameObjectRenderer renderer = new JGameObjectRenderer(window);
		UpdateThread mainThread = new UpdateThread(renderer);
		mainThread.start();
	}

	public static void main(String args[]){
		new Game();
	}
}