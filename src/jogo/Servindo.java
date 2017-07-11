package jogo;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Vector;

class Servindo extends Thread {
	
	Socket clientSocket;
	ObjectInputStream in;
	static ObjectOutputStream out[] = new ObjectOutputStream[2];
	static int cont=0;
	int idCliente;
	boolean gameover = false;
	Vector<Data> zombies = null;
  

  Servindo(Socket clientSocket, Vector<Data> zombies) {
    this.clientSocket = clientSocket;
    this.zombies = zombies;
  }

  public void run() {
    try {
		in = new ObjectInputStream(clientSocket.getInputStream());
		out[cont++] = new ObjectOutputStream(clientSocket.getOutputStream());
		idCliente = cont-1;
		
		Data data = new Data();
		
		data.setId(idCliente);
		
		out[idCliente].writeObject(data);
		out[idCliente].flush();
		do{
			data = new Data();
			data = (Data) in.readObject();
			
			if(data.isMoving())
				moveUnit(data);
			
			for(int i = 0; i<cont; i++) {
				out[i].writeObject(data);
				if(zombies.size() > 0) {
					out[i].writeObject(zombies);
				}
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
  
  void moveUnit(Data data) {
	  Point position = data.getPosition();
	  
	  switch(data.getDirection()) {
	  case 0:
		  data.setPosition(new Point(position.x - data.getSpeed(), position.y));
		  break;
	  case 1:
		  data.setPosition(new Point(position.x + data.getSpeed(), position.y));
		  break;
	  case 2:
		  data.setPosition(new Point(position.x, position.y - data.getSpeed()));
		  break;
	  case 3:
		  data.setPosition(new Point(position.x, position.y + data.getSpeed()));
		  break;
	  }
  }
}
