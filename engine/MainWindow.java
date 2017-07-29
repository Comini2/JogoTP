import javax.swing.JFrame;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.*;
import java.awt.Point;
import java.awt.image.BufferedImage;

class MainWindow extends JFrame{

	MainWindow(){
		JGameObject gO = new JGameObject(new Point(300, 300));
		Animation anim = null;
		BufferedImage sprites[] = new BufferedImage[20];
		try{
			for(int i = 0; i<20; i++){
				sprites[i] = ImageIO.read(new File(("../img/soldier/idle/survivor-idle_handgun_" + i +".png")));
			}
			anim = new Animation(sprites, 1);
		}catch(IOException e){
			e.printStackTrace();
		}

		setLayout(null);
		gO.addAnimation(anim, "Idle");
		gO.playAnimation("Idle");
		add(gO);
		pack();
		setSize(600, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		new JGameObjectRenderer().start();
	}

	public static void main(String args[]){
		new MainWindow();
	}
}