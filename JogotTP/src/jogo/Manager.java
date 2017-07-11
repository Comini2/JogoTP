package jogo;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Manager extends Thread implements KeyListener {
	
	Player localPlayer;
	Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;
	
	Manager(Socket socket, Player localPlayer){
		this.localPlayer = localPlayer;
		this.socket = socket;
	}
	
	public void getDataFromServer() {
		try {
			Data data = (Data) in.readObject();
			
			if(data.getPlayer().id == localPlayer.id) {
				localPlayer.setPosition(data.getPlayer().getPosition());
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
			localPlayer.id = data.getPlayer().id;
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
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				try {
					data.setMoving(true);
					data.setDirection(0);
					data.setPlayer(localPlayer);
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
					data.setPlayer(localPlayer);
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
					data.setPlayer(localPlayer);
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
					data.setPlayer(localPlayer);
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
