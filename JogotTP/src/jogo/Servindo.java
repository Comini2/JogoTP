package jogo;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

class Servindo extends Thread {
	
	Socket clientSocket;
	ObjectInputStream in;
	static ObjectOutputStream out[] = new ObjectOutputStream[2];
	static int cont=0;
	int idCliente;
	boolean gameover = false;
  

  Servindo(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  public void run() {
    try {
		in = new ObjectInputStream(clientSocket.getInputStream());
		out[cont++] = new ObjectOutputStream(clientSocket.getOutputStream());
		idCliente = cont-1;
		
		Player player = new Player();
		
		player.id = idCliente;
		Data data = new Data();
		data.setPlayer(player);
		
		out[idCliente].writeObject(data);
		out[idCliente].flush();
		do{
			data = (Data) in.readObject();
			if(data.isMoving())
				movePlayer(data);
			for(int i = 0; i<cont; i++) { 
				out[i].writeObject(data);
				out[i].flush();
			}
		}while(data != null);
		         
		clientSocket.close();

    } catch (IOException e) {
      e.printStackTrace();
    } catch (NoSuchElementException e) {
      System.out.println("Conexacao terminada pelo cliente");
    }catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
  }
  
  void movePlayer(Data data) {
	  Player player = data.getPlayer();
	  
	  switch(data.getDirection()) {
	  case 0:
		  player.setPosition(new Point(player.getPosition().x - player.speed, player.getPosition().y));
		  break;
	  case 1:
		  player.setPosition(new Point(player.getPosition().x + player.speed, player.getPosition().y));
		  break;
	  case 2:
		  player.setPosition(new Point(player.getPosition().x, player.getPosition().y - player.speed));
		  break;
	  case 3:
		  player.setPosition(new Point(player.getPosition().x - player.speed, player.getPosition().y + player.speed));
		  break;
	  }
  }
}
