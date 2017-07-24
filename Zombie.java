import java.util.Random;

class Zombie{
	int x;
	int y;
	int direction;
	boolean attacking = false;
	boolean hit = false;
	private int sprite;
	private int attackSprite = 0;
	float attackRate = 1000f;
	float attackCooldown = 0f;
	int health;

	Zombie(){
		Random random = new Random();
		sprite = random.nextInt(16);
		x = -100;
		y = -100;
	}

	public int getNextSpriteIndex(){
		sprite++;
		if(sprite > 15)
			sprite = 0;
		return sprite;
	}

	public int getSpriteIndex(){
		return sprite;
	}

	public int getAttackSpriteIndex(){
		return attackSprite;
	}

	public int getNextAttackSpriteIndex(){
		attackSprite++;
		if(attackSprite > 8){
			attackSprite = 0;
			attacking = false;
		}
		return attackSprite;
	}
}