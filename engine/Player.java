import java.awt.Point;
import java.awt.event.KeyEvent;

class Player extends SimpleConduct{

	JGameObject playerGo;
	Animation idleAnimation;
	Animation movingAnimation;

	public void start(){
		idleAnimation = new Animation(SpriteLoader.loadSpritesOnFolder("../img/soldier/idle", 20), 0.5f);
		movingAnimation = new Animation(SpriteLoader.loadSpritesOnFolder("../img/soldier/move", 20),0.5f);
		playerGo = new JGameObject(new Point(100, 100));
		playerGo.addAnimation(idleAnimation, "Idle");
		playerGo.addAnimation(movingAnimation, "Move");
	}

	public void update(){
		playerGo.playAnimation("Move");
		if(Input.isKeyPressed(KeyEvent.VK_RIGHT) && Input.isKeyPressed(KeyEvent.VK_DOWN)){
			playerGo.rotation = 45;
		}else if(Input.isKeyPressed(KeyEvent.VK_RIGHT) && Input.isKeyPressed(KeyEvent.VK_UP)){
			playerGo.rotation = -45;
		}else if(Input.isKeyPressed(KeyEvent.VK_LEFT) && Input.isKeyPressed(KeyEvent.VK_UP)){
			playerGo.rotation = -135;
		}else if(Input.isKeyPressed(KeyEvent.VK_LEFT) && Input.isKeyPressed(KeyEvent.VK_DOWN)){
			playerGo.rotation = 135;
		}else if(Input.isKeyPressed(KeyEvent.VK_RIGHT)){
			playerGo.rotation = 0;
		}else if(Input.isKeyPressed(KeyEvent.VK_LEFT)){
			playerGo.rotation = 180;
		}else if(Input.isKeyPressed(KeyEvent.VK_UP)){
			playerGo.rotation = 270;
		}else if(Input.isKeyPressed(KeyEvent.VK_DOWN)){
			playerGo.rotation = 90;
		}else {
			playerGo.playAnimation("Idle");
		}
	}
}