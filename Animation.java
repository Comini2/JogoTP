import java.awt.image.BufferedImage;

class Animation{

	private BufferedImage spritesheet;
	private int frames;
	private float speed;
	private int frameHeight;
	private int frameWidth;

	public Animation(BufferedImage spritesheet, int frames,int frameHeight, int frameWidth, float speed, JGameObject gO){
		this.spritesheet = spritesheet;
		this.frames = frames;
		this.speed = speed;
		this.frameHeight = frameHeight;
		this.frameWidth = frameWidth;
		AnimationController.addAnimation(this);
	}
}