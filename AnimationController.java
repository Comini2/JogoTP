import java.util.LinkedList;

class AnimationController extends Thread{
	private static LinkedList<Animation> animations;

	private AnimationController(){}
	
	public static void addAnimation(Animation animation){
		animations.add(animation);
	}

}