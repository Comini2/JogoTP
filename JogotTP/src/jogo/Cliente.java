package jogo;

import java.io.*;
import java.net.*;

public class Cliente{
  static boolean paraThread = false;
  static int nCliente;
  Socket socket = null;
  
  Cliente(){
     try {
		socket = new Socket("127.0.0.1", 80);
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     new Jogo(socket);
  }

  public static void main(String[] args) {
	  new Cliente();
  }
}