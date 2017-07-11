package jogo;

import java.awt.Graphics;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Tela extends JPanel implements Runnable {
	Player localPlayer;
	
	Tela(Player localPlayer){
		this.localPlayer = localPlayer;
		
		(new Thread(this)).start();		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(localPlayer != null && localPlayer.getPosition() != null) {
			g.drawOval(localPlayer.getPosition().x, localPlayer.getPosition().y, 10, 10);
		}
	}

	@Override
	public void run() {
		while(true) {
			repaint();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
