import java.awt.Graphics;
import javax.swing.JComponent;


public class JGameObject extends JComponent {

  private static final String uiClassID = "GameObjectUI";

  Animation animation;
  
  public JGameObject(){
    setDoubleBuffered(true);
  }

  public void paintComponent(Graphics g){


  }
  
  
}
