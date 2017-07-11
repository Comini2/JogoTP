package jogo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
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
				Data data = (Data)obj;
				if(data.getId() == localPlayer.id) {
					localPlayer.setPosition(data.getPosition());
				}else
					remotePlayer.setPosition(data.getPosition());
			}else {
				Vector<Data> data = (Vector<Data>)obj;
				zombies.clear();
				for(Data d: data) {
					Zombie z = new Zombie(0, d.getSpeed(), d.getPosition());
					zombies.add(z);
				}
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
			localPlayer.id = data.getId();
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
		Data data = new Data();
		data.setId(localPlayer.id);
		data.setPosition(localPlayer.getPosition());
		data.setPlayerData(true);
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				try {
					data.setMoving(true);
					data.setDirection(0);
					data.setSpeed(localPlayer.speed);
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
					data.setSpeed(localPlayer.speed);
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
					data.setSpeed(localPlayer.speed);
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
					data.setSpeed(localPlayer.speed);
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
