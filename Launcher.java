import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

class Launcher extends JFrame{

	final int SCREEN_WIDTH = 1024;
	final int SCREEN_HEIGTH = 768;

	LauncherContainer container = new LauncherContainer(this, SCREEN_WIDTH, SCREEN_HEIGTH);

	Launcher(){
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
}

class LauncherContainer extends JPanel implements MouseListener{

	BufferedImage exitButtonSprite[] = new BufferedImage[2];
	BufferedImage startButtonSprite[] = new BufferedImage[2];
	BufferedImage backgroundImage;
	Rectangle exitButtonRect;
	Rectangle startButtonRect;
	int startSprite = 0;
	int exitSprite = 0;
	int screenWidth;
	int screenHeigth;
	final int BACKGROUND_SCALE = 2;
	JFrame launcher;
	boolean error;
	Font font;

	LauncherContainer(JFrame launcher, int screenWidth, int screenHeigth){
		this.launcher = launcher;
		this.screenWidth = screenWidth;
		this.screenHeigth = screenHeigth;
		exitButtonRect = new Rectangle(screenWidth/2 - 100, 400, 200, 50);
		startButtonRect = new Rectangle(screenWidth/2 - 100, 330, 200, 50);
		try{
			font = Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf")).deriveFont(Font.BOLD, 24);
			backgroundImage = createResizedCopy(ImageIO.read(new File("img/env/background.png")), screenWidth/BACKGROUND_SCALE, screenHeigth/BACKGROUND_SCALE);
			startButtonSprite[0] = createResizedCopy(ImageIO.read(new File("img/start.png")), 200, 50);
			startButtonSprite[1] = createResizedCopy(ImageIO.read(new File("img/start_hoover.png")), 200, 50);
			exitButtonSprite[0] = createResizedCopy(ImageIO.read(new File("img/exit.png")), 200, 50);
			exitButtonSprite[1] = createResizedCopy(ImageIO.read(new File("img/exit_hoover.png")), 200, 50);
		}catch(IOException e){
			e.printStackTrace();
		}catch(FontFormatException e){
			e.printStackTrace();
		}
		addMouseListener(this);
	}

	public void paintComponent(Graphics g){
		for(int i = 0; i<BACKGROUND_SCALE; i++)
			for(int j = 0; j<BACKGROUND_SCALE; j++)
				g.drawImage(backgroundImage, screenWidth*i/BACKGROUND_SCALE, screenHeigth*j/BACKGROUND_SCALE, null);
		g.setColor(new Color(0, 0, 0, 180));
		g.fillRect(0, 0, screenWidth, screenHeigth);
		g.drawImage(startButtonSprite[startSprite], startButtonRect.x, startButtonRect.y, null);
		g.drawImage(exitButtonSprite[exitSprite], exitButtonRect.x, exitButtonRect.y, null);

		if(error == true){
			g.setColor(Color.RED);
			g.setFont(font);
			g.drawString("ERRO AO CONECTAR AO SERVIDOR", 220, screenHeigth - 50);
		}
	}

	public void mousePressed(MouseEvent e) {
    	if(startButtonRect.contains(e.getPoint())){
    		startSprite = 1;
    		repaint();
    	}else if(exitButtonRect.contains(e.getPoint())){
    		exitSprite = 1;
    		repaint();
    	}
    }

    public void mouseReleased(MouseEvent e) {
    	if(startSprite == 1){
    		startSprite = 0;
    		repaint();
    	}
    	if(exitSprite == 1){
    		exitSprite = 0;
    		repaint();
    	}
    	if(startButtonRect.contains(e.getPoint())){
    		try{
    			new Client(launcher, screenWidth, screenHeigth);
    		}catch(IOException exc){
    			error = true;
    		}

    	}else if(exitButtonRect.contains(e.getPoint())){
    		System.exit(0);
    	}
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight){
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledBI.createGraphics();
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
        g.dispose();
        return scaledBI;
    }

}