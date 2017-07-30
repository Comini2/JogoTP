import java.util.HashMap;

class AnimationController extends Thread{

	private JGameObject jGO;
	private HashMap<String, Animation> animations = new HashMap<>();
	Animation currAnim;

	public AnimationController(JGameObject jGO){
		this.jGO = jGO;
	}

	public void addAnimation(Animation animation, String animationName){
  		animations.put(animationName, animation);
  	}

	protected void playAnimation(String animationName, float speed){
	  currAnim = animations.get(animationName);
	  currAnim.speed = speed;
	}

	protected void playAnimation(String animationName){
	   currAnim = animations.get(animationName);
	}

	public void run(){
		long lastTime = System.nanoTime();
		final double clock = 1000000000.0 / 60.0;
		double delta = 0;
		while(true){
			long now = System.nanoTime();
			delta += (now - lastTime) / clock;
			if(currAnim != null){
	        	currAnim.delta +=(now - lastTime) / clock*currAnim.speed;
	        	while(currAnim.delta >= 1){
	        		currAnim.delta--;
	        		currAnim.nextSprite();
	        	}
        	}
	        lastTime = now;
	        while(delta >= 1){
	        	delta--;
	        }
		}
	}
}