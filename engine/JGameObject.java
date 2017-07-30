import java.awt.geom.*;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Shape;
import java.util.LinkedList;


public class JGameObject {

  //private LinkedList<Conduct> components = new LinkedList<>();

  private AffineTransform at;
  private Shape shape;
  private Rectangle2D bounds;
  protected Point2D position;
  public float rotation = 0f;
  private Camera mainCamera = Screen.mainCamera;

  public JGameObject(Shape shape){
    this.shape = shape;
    bounds = shape.getBounds2D();
    position = new Point2D.Double(bounds.getX() + mainCamera.getXOffset(), bounds.getY() + mainCamera.getYOffset());
    Screen.addGO(this);
  }

  public JGameObject(Point2D position){
  	this.position = new Point2D.Double(mainCamera.getXOffset() + position.getX(), mainCamera.getYOffset() + position.getY());
    Screen.addGO(this);
  }

  public JGameObject(float x, float y){
    this(new Point2D.Double(x, y));
  }

  public JGameObject(){
    this(new Point2D.Double(0, 0));
  }

  public void setLocation(float x, float y){
    position.setLocation(x + mainCamera.getXOffset(), y + mainCamera.getYOffset());
    if(shape != null){
      shape = new Rectangle2D.Double(x - 10f, y - 10f , 20f, 20f);
      bounds = shape.getBounds2D();
    }
  }

  public void move(float dx, float dy){
    position.setLocation(position.getX() + dx, position.getY() + dy);
    if(shape != null){
      shape = new Rectangle2D.Double(position.getX() - bounds.getWidth()/2, position.getY() - bounds.getHeight()/2, bounds.getWidth(), bounds.getHeight());
      bounds = shape.getBounds2D();
    }
  }


  public void renderGO(Graphics2D g2d){
    if(shape != null){
      at = AffineTransform.getRotateInstance(Math.toRadians(rotation), position.getX(), position.getY());
    	g2d.draw(at.createTransformedShape(shape));
    }
  }
}
