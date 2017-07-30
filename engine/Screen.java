import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;


class Screen extends JPanel {

	private JFrame window = new JFrame();
	private Graphics2D g2d;
	private static LinkedList<JGameObject> jGOs = new LinkedList<>();
	public static Camera mainCamera = new Camera();

	public Screen(){
		addKeyListener(new Input());
		setFocusable(true);
		window.add(this);
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	public static void addGO(JGameObject jGO){
		jGOs.add(jGO);
	}

	@Override
	public Dimension getPreferredSize(){
		return new Dimension(800, 600);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g2d = (Graphics2D)g;
		for(JGameObject jGO : jGOs){
			jGO.renderGO(g2d);
		}

		g2d.translate(-400 + mainCamera.getX(), -300 + mainCamera.getY());

	}

}