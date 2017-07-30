import java.awt.geom.Rectangle2D;
import java.awt.event.KeyEvent;

class PlayerScript extends SimpleConduct{

	JGameObject playerGo;
	JGameObject playerGo2;
	Animation idleAnimation;
	Animation movingAnimation;

	public void start(){
		playerGo = new JGameObject(new Rectangle2D.Double(0, 0, 50, 50));
	}
	public void update(){
		if(Input.isKeyPressed(KeyEvent.VK_RIGHT)){
			playerGo.move(3, 0);
		}
	}
}
