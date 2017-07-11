package jogo;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Tela extends JPanel implements Runnable {
	Player localPlayer;
	Player remotePlayer;
	Vector<Zombie> zombies;
	
	
	Tela(Player localPlayer, Player remotePlayer, Vector<Zombie> zombies){
		this.localPlayer = localPlayer;
		this.remotePlayer = remotePlayer;
		this.zombies = zombies;
		
		(new Thread(this)).start();		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(localPlayer != null && localPlayer.getPosition() != null) {
			g.drawOval(localPlayer.getPosition().x, localPlayer.getPosition().y, 10, 10);
		}
		if(remotePlayer != null && remotePlayer.getPosition() != null) {
			g.drawOval(remotePlayer.getPosition().x, remotePlayer.getPosition().y, 10, 10);
		}
		if(zombies.size() > 0) {
			for(Zombie z : zombies) {
				g.drawRect(z.getPosition().x, z.getPosition().y, 10, 10);
			}
		}
	}

	@Override
	public void run() {
		while(true) {
			repaint();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
