package jogo;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Vector;

class Servindo extends Thread {
	
	Socket clientSocket;
	ObjectInputStream in;
	static ObjectOutputStream out[] = new ObjectOutputStream[2];
	static int cont=0;
	int idCliente;
	boolean gameover = false;
	Vector<Zombie> zombies = null;
  

  Servindo(Socket clientSocket, Vector<Zombie> zombies) {
    this.clientSocket = clientSocket;
    this.zombies = zombies;
  }

  public void run() {
    try {
		in = new ObjectInputStream(clientSocket.getInputStream());
		out[cont++] = new ObjectOutputStream(clientSocket.getOutputStream());
		idCliente = cont-1;
		
		Data data = new Data(new Player(idCliente));
		
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
	  Unit unit = data.getUnit();
	  
	  switch(data.getDirection()) {
	  case 0:
		  unit.setPosition(new Point(unit.getPosition().x - unit.getSpeed(), unit.getPosition().y));
		  break;
	  case 1:
		  unit.setPosition(new Point(unit.getPosition().x + unit.getSpeed(), unit.getPosition().y));
		  break;
	  case 2:
		  unit.setPosition(new Point(unit.getPosition().x, unit.getPosition().y - unit.getSpeed()));
		  break;
	  case 3:
		  unit.setPosition(new Point(unit.getPosition().x, unit.getPosition().y + unit.getSpeed()));
		  break;
	  }
  }
}
