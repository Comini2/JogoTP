import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class Launcher extends JFrame implements ActionListener{

	final int SCREEN_WIDTH = 1024;
	final int SCREEN_HEIGTH = 768;

	JButton startButton = new JButton("START");
	JPanel container = new JPanel(new BorderLayout());

	Launcher(){
		startButton.setSize(100, 50);
		startButton.addActionListener(this);
		container.add(startButton, BorderLayout.CENTER);
		add(container);
		pack();
		setSize(SCREEN_WIDTH, SCREEN_HEIGTH);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String args[]){
		Launcher launcher = new Launcher();
	}

	public void actionPerformed(ActionEvent e){
		getContentPane().removeAll();
		Client client = new Client(this, SCREEN_WIDTH, SCREEN_HEIGTH);
	}

	public void restartLauncher(){
		
	}

}