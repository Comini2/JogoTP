package jogo;

public class Data implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	Player player;
	boolean moving = false;
	
	int direction;
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public boolean isMoving() {
		return moving;
	}
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}	
	
}
