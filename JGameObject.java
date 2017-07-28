package javax.swing;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.PanelUI;

public class JPanel
  extends JComponent
  implements Accessible
{
  private static final String uiClassID = "GameObjectUI";
  
  public JPanel(LayoutManager paramLayoutManager, boolean paramBoolean)
  {
    setLayout(paramLayoutManager);
    setDoubleBuffered(paramBoolean);
    setUIProperty("opaque", Boolean.TRUE);
    updateUI();
  }
  
  public JPanel(LayoutManager paramLayoutManager)
  {
    this(paramLayoutManager, true);
  }
  
  public JPanel(boolean paramBoolean)
  {
    this(new FlowLayout(), paramBoolean);
  }
  
  public JPanel()
  {
    this(true);
  }
  
  public void updateUI()
  {
    setUI((PanelUI)UIManager.getUI(this));
  }
  
  public PanelUI getUI()
  {
    return (PanelUI)this.ui;
  }
  
  public void setUI(PanelUI paramPanelUI)
  {
    super.setUI(paramPanelUI);
  }
  
  public String getUIClassID()
  {
    return "GameObjectUI";
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.defaultWriteObject();
    if (getUIClassID().equals("GameObjectUI"))
    {
      byte b = JComponent.getWriteObjCounter(this);
      b = (byte)(b - 1);JComponent.setWriteObjCounter(this, b);
      if ((b == 0) && (this.ui != null)) {
        this.ui.installUI(this);
      }
    }
  }
  
  protected String paramString()
  {
    return super.paramString();
  }
}
