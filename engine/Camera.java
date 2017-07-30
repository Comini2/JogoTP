import java.awt.geom.*;

class Camera{

	public Camera(){}

	private static Point2D position = new Point2D.Double(0, 0);

	public double getX(){
		return position.getX();
	}
	public double getY(){
		return position.getY();
	}

	public double getXOffset(){
		return 400-position.getX();
	}
	public double getYOffset(){
		return 300-position.getY();
	}

}