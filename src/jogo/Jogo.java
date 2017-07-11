package jogo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Jogo extends JFrame{
	int nCliente;
	Socket socket;
	InputStream in;
	OutputStream out;
	Manager manager;
	
	Player localPlayer = new Player();
	Player remotePlayer = new Player();
	Vector<Zombie> zombies = new Vector<Zombie>();
	
	
	Jogo(Socket socket){
		this.socket = socket;
		manager = new Manager(socket, localPlayer, remotePlayer, zombies);
		
		Tela tela = new Tela(localPlayer, remotePlayer, zombies);
		tela.addKeyListener(manager);
		tela.setFocusable(true);
		add(tela);
		setSize(1024, 768);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
		manager.start();
		
	}
	
	
}
