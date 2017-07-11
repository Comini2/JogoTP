package jogo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Jogo extends JFrame{
	int nCliente;
	Socket socket;
	InputStream in;
	OutputStream out;
	Manager manager;
	
	Player localPlayer = new Player();
	
	
	Jogo(Socket socket){
		this.socket = socket;
		manager = new Manager(socket, localPlayer);
		
		Tela tela = new Tela(localPlayer);
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
