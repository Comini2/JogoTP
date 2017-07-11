package jogo;

import java.net.*;
import java.io.*;

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
      
      new Servindo(clientSocket).start();
    }
      try {
		serverSocket.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
  }
}
