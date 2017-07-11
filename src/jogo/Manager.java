package jogo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

public class Manager extends Thread implements KeyListener {
	
	Player localPlayer;
	Player remotePlayer;
	Vector<Zombie> zombies;
	Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;
	
	Manager(Socket socket, Player localPlayer, Player remotePlayer, Vector<Zombie> zombies){
		this.localPlayer = localPlayer;
		this.socket = socket;
		this.remotePlayer = remotePlayer;
		this.zombies = zombies;
	}
	
	public void getDataFromServer() {
		try {
			Object obj = in.readObject();
			
			if(obj instanceof Data) {
				Player player = (Player)((Data)obj).getUnit();
				if(player.id == localPlayer.id) {
					localPlayer.setPosition(player.getPosition());
				}else
					remotePlayer.setPosition(player.getPosition());
			}else {
				Vector<Zombie> zs = (Vector<Zombie>)obj;
				zombies.clear();
				for(Zombie z: zs)
					zombies.add(z);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			Data data = (Data) in.readObject();
			localPlayer.id = ((Player)data.getUnit()).id;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		while(true) {
				getDataFromServer();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Data data = new Data(localPlayer);
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				try {
					data.setMoving(true);
					data.setDirection(0);
					out.writeObject(data);
					out.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			case KeyEvent.VK_RIGHT:
				try {
					data.setMoving(true);
					data.setDirection(1);
					out.writeObject(data);
					out.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			case KeyEvent.VK_UP:
				try {
					data.setMoving(true);
					data.setDirection(2);
					out.writeObject(data);
					out.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			case KeyEvent.VK_DOWN:
				try {
					data.setMoving(true);
					data.setDirection(3);
					out.writeObject(data);
					out.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyReleased(KeyEvent e) {}
}
