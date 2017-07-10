package servidor;

import java.net.*;
import java.io.*;
import java.util.*;
import java.io.OutputStream;

class Servidor {
  public static void main(String[] args) {
    ServerSocket serverSocket=null;

    try {
      serverSocket = new ServerSocket(80);
    } catch (IOException e) {
      System.out.println("Could not listen on port: " + 80 + ", " + e);
      System.exit(1);
    }

    for (int i=0; i<2; i++) {
      Socket clientSocket = null;
      try {
        clientSocket = serverSocket.accept();
      } catch (IOException e) {
        System.out.println("Accept failed: " + 80 + ", " + e);
        System.exit(1);
      }

      System.out.println("Accept Funcionou!");
      
      
      new Servindo(clientSocket, i).start();

    }

    try {
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}


class Servindo extends Thread {
	
  Socket clientSocket;
  int nCliente;
  OutputStream out;
  boolean gameover = false;
  
  static int cont=0;

  Servindo(Socket clientSocket, int nCliente) {
    this.clientSocket = clientSocket;
    this.nCliente = nCliente;
  }

  public void run() {
    try {
		out = new DataOutputStream(clientSocket.getOutputStream());
		out.write(nCliente);
		out.flush();
		
		do{
			
		}while(!gameover);
		         
		clientSocket.close();

    } catch (IOException e) {
      e.printStackTrace();
    } catch (NoSuchElementException e) {
      System.out.println("Conexacao terminada pelo cliente");
    }
    
  }
}
