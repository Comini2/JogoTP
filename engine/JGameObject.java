import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.Point;
import java.awt.Dimension;
import javax.swing.JComponent;
import java.util.HashMap;


public class JGameObject extends JComponent {

  private HashMap<String, Animation> animations = new HashMap<>();
  public Animation currAnim = null;
  public Point position;
  public Dimension size;
  public float rotation = 0f;
  private AffineTransform at;


  public JGameObject(Point position, Dimension size){
  	setDoubleBuffered(true);
  	this.position = position;
  	this.size = size;
  	this.setBounds();
  	JGameObjectRenderer.addGo(this);
  }
  public JGameObject(Point position){
  	this(position, new Dimension(50, 50));
  }

  public JGameObject(){
    this(new Point(0, 0));
  }

  protected void addAnimation(Animation animation, String animationName){
  	animations.put(animationName, animation);
  }

  private void setBounds(){
  	super.setBounds((int)position.x - size.width/2, (int)position.y - size.height/2,size.width, size.height);
  }

  protected void playAnimation(String animationName, float speed){
      currAnim = animations.get(animationName);
      currAnim.speed = speed;
  		size = new Dimension(currAnim.frameWidth, currAnim.frameHeight);
  		this.setBounds();
  }

  protected void playAnimation(String animationName){
      currAnim = animations.get(animationName);
      size = new Dimension(currAnim.frameWidth, currAnim.frameHeight);
      this.setBounds();
  }

  public void paintComponent(Graphics g){
  	super.paintComponent(g);
    if(currAnim != null){
    	Graphics2D g2d = (Graphics2D)g;
    	at = new AffineTransform();
    	at.translate(size.width/2, size.height/2);
    	at.rotate(Math.toRadians(rotation));
    	at.translate(-size.width/2, -size.height/2);
    	g2d.drawImage(currAnim.getCurrentSprite(), at, null);
    }
    else
      g.drawRect(0, 0, size.width, size.height);
  }

  //TODO: IMPLEMENTAR A CLASSE E INTERFACE DE SCRIPT
  
}
