package jogo;

public class Data implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	boolean moving = false;
	Unit unit;
	int direction;
	
	Data(){}
	
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	Data(Unit unit){
		this.unit = unit;
	}
	
	public boolean isMoving() {
		return moving;
	}
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	public String toString() {
		return "ID: " + ((Player)unit).id + " Position: " + unit.getPosition().toString();
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
}
