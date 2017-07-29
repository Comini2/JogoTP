import java.awt.image.BufferedImage;
import java.awt.Graphics;

class Animation{

	private BufferedImage sprites[];
	private int frames;
	public float speed;
	public int frameHeight;
	public int frameWidth;
	private int spriteCounter = 0;
	public double delta = 0f;

	public Animation(BufferedImage spritesheet, int frames,int frameWidth , int frameHeight, float speed){
		this.frames = frames;
		this.speed = speed;
		this.frameHeight = frameHeight;
		this.frameWidth = frameWidth;
		sprites = new BufferedImage[frames];

		for(int i = 0; i<frames; i++)
			sprites[i] = spritesheet.getSubimage(frameWidth*i, 0, frameWidth, frameHeight);
	}

	public Animation(BufferedImage sprites[], float speed){
		this.sprites = sprites;
		this.speed = speed;
		frameWidth = sprites[0].getWidth();
		frameHeight = sprites[0].getHeight();
		frames = sprites.length;
	}

	public BufferedImage getCurrentSprite(){
		return sprites[spriteCounter];
	}

	public void nextSprite(){
		spriteCounter++;
		if(spriteCounter >= frames)
			spriteCounter = 0;
	}

	//TODO: ADICIONAR CROSS FADE ENTRE ANIMAÇÕES.

}