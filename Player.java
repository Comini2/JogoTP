import java.util.Random;

class Player{
	int x;
	int y;
	int direction;
	boolean idle = true;
	boolean shooting = false;
	private int sprite;
	private int shootSprite = 0;
	int health;
	int killCount = 0;

	Player(){
		Random random = new Random();
		sprite = random.nextInt(20);
	}

	public int getNextSpriteIndex(){
		sprite++;
		if(sprite > 19)
			sprite = 0;
		return sprite;
	}

	public int getNextShootSpriteIndex(){
		shootSprite++;
		if(shootSprite > 2){
			shootSprite = 0;
			shooting = false;
		}
		return shootSprite;
	}

	public int getSpriteIndex(){
		return sprite;
	}
	public int getShootSpriteIndex(){
		return shootSprite;
	}
}