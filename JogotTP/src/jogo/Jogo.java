package jogo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Jogo extends JFrame implements ActionListener {
  static PrintStream os = null;
  static boolean paraThread = false;
  
  JTextArea input;
  static JTextArea chat;
  JButton send;
  JPanel left;
  JPanel right;
  
  Jogo(){
	  left = new JPanel();
	  right = new JPanel();
	  input = new JTextArea(5, 20);
	  chat = new JTextArea(30, 20);
	  chat.setEditable(false);	  
	  send = new JButton("Enviar");
	  send.addActionListener(this);
	  left.add(input);
	  left.add(send);
	  right.add(chat);
	  setSize(500,500);
	  add(left);
	  add(right, BorderLayout.EAST);
	  pack();
	  
	  setDefaultCloseOperation(EXIT_ON_CLOSE);
	  setVisible(true);
  }

  public static void main(String[] args) {
    Socket socket = null;
    Scanner is = null;
    new Jogo();

    try {
      socket = new Socket("127.0.0.1", 80);
      os = new PrintStream(socket.getOutputStream(), true);
      is = new Scanner(socket.getInputStream());
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host.");
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to host");
    }

    try {
      String inputLine;
      
      System.out.println("conectou");
      
      System.out.println(is.nextInt());

      do {
        chat.append((inputLine=(is.nextLine() + "\n")));
      } while (!inputLine.equals (""));

      paraThread = true;
      os.close();
      is.close();
      socket.close();
    } catch (UnknownHostException e) {
      System.err.println("Trying to connect to unknown host: " + e);
    } catch (IOException e) {
      System.err.println("IOException:  " + e);
    }
  }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		os.println(input.getText());
	}
}