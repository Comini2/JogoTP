import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


class Screen extends JPanel implements MouseListener{
	
	BufferedImage feet_running_sprite[] = new BufferedImage[20]; 
	BufferedImage player_running_sprite[] = new BufferedImage[20];
	BufferedImage player_shooting_sprite[] = new BufferedImage[3];
	BufferedImage feet_idle_sprite;
	BufferedImage player_idle_sprite[] = new BufferedImage[20];
	BufferedImage zombie_moving_sprite[] = new BufferedImage[16];
	BufferedImage zombie_attacking_sprite[] = new BufferedImage[9];
	BufferedImage background;
	BufferedImage trees[] = new BufferedImage[3];
	BufferedImage bloodsplat;
	BufferedImage skull;
	BufferedImage hearth;
	BufferedImage gameOverSprite;
	BufferedImage menuButtonSprite[] = new BufferedImage[2];
	BufferedImage exitButtonSprite[] = new BufferedImage[2];

	Rectangle menuButtonRect;
	Rectangle exitButtonRect;

	Font font;
	final int BACKGROUND_SCALE = 2;
	int screenWidth;
	int screenHeigth;
	float deltaTime;
	float soldierCounter = 0f;
	float zombieCounter = 0f;
	boolean gameOver = false;
	boolean waiting = true;

	int playerMoveIndex;
	int playerShootIndex = 0;
	int zombieMoveIndex = 0;
	int zombieAttackIndex;
	int round;
	int x;
	int y;
	int menuSprite = 0;
	int exitSprite = 0;

	int id;
	int deadId;
	Player player[];
	Zombie zombie[];
	Launcher launcher;
	
	Screen(int screenWidth, int screenHeigth,float deltaTime, Player player[], Zombie zombie[], int id, Launcher launcher){
		this.id = id;
		this.player = player;
		this.zombie = zombie;
		this.screenWidth = screenWidth;
		this.screenHeigth = screenHeigth;
		this.deltaTime = deltaTime;
		this.launcher = launcher;
		try {
			menuButtonSprite[0] = createResizedCopy(ImageIO.read(new File("img/menu.png")), 120, 50);
			menuButtonSprite[1] = createResizedCopy(ImageIO.read(new File("img/menu_hoover.png")), 120, 50);
			exitButtonSprite[0] = createResizedCopy(ImageIO.read(new File("img/exit.png")), 120, 50);
			exitButtonSprite[1] = createResizedCopy(ImageIO.read(new File("img/exit_hoover.png")), 120, 50);
			font = Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf")).deriveFont(Font.PLAIN, 18);
			skull = createResizedCopy(ImageIO.read(new File("img/skull.png")), 25, 25);
			hearth = createResizedCopy(ImageIO.read(new File("img/soldier/hearth.png")), 25, 25);
			bloodsplat = createResizedCopy(ImageIO.read(new File("img/zombie/bloodsplat.png")), 60, 60);
			background = createResizedCopy(ImageIO.read(new File("img/env/background.png")), screenWidth/BACKGROUND_SCALE, screenHeigth/BACKGROUND_SCALE);
			gameOverSprite = ImageIO.read(new File("img/gameover.png"));
			feet_idle_sprite = createResizedCopy(ImageIO.read(new File(("img/soldier/feet/idle/survivor-idle_0.png"))), 50, 50);
			for(int i = 0; i<20; i++){
				feet_running_sprite[i] = createResizedCopy(ImageIO.read(new File(("img/soldier/feet/run/survivor-run_"+ i +".png"))), 50, 50);
				player_running_sprite[i] = createResizedCopy(ImageIO.read(new File(("img/soldier/move/survivor-move_handgun_"+ i +".png"))), 75, 75);
				player_idle_sprite[i] = createResizedCopy(ImageIO.read(new File(("img/soldier/idle/survivor-idle_handgun_"+ i +".png"))), 75, 75);
				if(i < 3)
					player_shooting_sprite[i] = createResizedCopy(ImageIO.read(new File(("img/soldier/shoot/survivor-shoot_handgun_"+ i +".png"))), 75, 75);
				if(i < 16)
					zombie_moving_sprite[i] = createResizedCopy(ImageIO.read(new File(("img/zombie/move/skeleton-move_"+ i +".png"))), 85, 85);
				if(i < 9)
					zombie_attacking_sprite[i] = createResizedCopy(ImageIO.read(new File(("img/zombie/attack/skeleton-attack_"+ i +".png"))), 85, 85);
			}
			x = screenWidth/2 - gameOverSprite.getWidth()/2;
			y = screenHeigth/2 - gameOverSprite.getHeight()/2 - 100;
			menuButtonRect = new Rectangle(x + 100, y + gameOverSprite.getHeight() , menuButtonSprite[0].getWidth(), menuButtonSprite[0].getHeight());
			exitButtonRect = new Rectangle(x + gameOverSprite.getWidth() - exitButtonSprite[0].getWidth() - 100, y  + gameOverSprite.getHeight(), exitButtonSprite[0].getWidth(), exitButtonSprite[0].getHeight());

		} catch (IOException e) {
			e.printStackTrace();
		} catch(FontFormatException e){
			e.printStackTrace();
		}
		addMouseListener(this);
	}

	public void setRound(int round){
		this.round = round;
	}

	public void startGame(){
		waiting = false;
	}

	public void gameOver(int id){
		deadId = id;
		addMouseListener(this);
		gameOver = true;
	}

	class Bloodsplat{
		Point p;
		int alpha = 255;

		Bloodsplat(Point p){
			this.p = p;
		}
	}

	ArrayList<Bloodsplat> splats = new ArrayList<>();
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setFont(font);
		g.setColor(Color.BLACK);
		for(int i = 0; i<BACKGROUND_SCALE; i++)
			for(int j = 0; j<BACKGROUND_SCALE; j++)
				g.drawImage(background, screenWidth*i/BACKGROUND_SCALE, screenHeigth*j/BACKGROUND_SCALE, null);
		Graphics2D g2d = (Graphics2D)g;
		for(Iterator<Bloodsplat> i = splats.iterator(); i.hasNext();){
			Bloodsplat b = i.next();
			BufferedImage img = deepCopy(bloodsplat);
			setAlpha(img, (byte)b.alpha);
			g.drawImage(img, b.p.x, b.p.y, null);
			b.alpha -= 1;
			if(b.alpha <= 0)
				i.remove();
		}
		g.drawString("ROUND " + Integer.toString(round), 50, 50);
		g.drawImage(hearth, 50, screenHeigth - 100, null);
		g.drawImage(skull, 50, screenHeigth - 70, null);
		g.drawString(Integer.toString(player[id].health), 85, screenHeigth - 82);
		g.drawString(Integer.toString(player[id].killCount), 85, screenHeigth - 52);
		g.setColor(new Color(0, 153, 0));
		g.drawOval(player[id].x - 30, player[id].y - 30, 67, 67);
		for(int i = 0; i<2; i++){
			playerMoveIndex = soldierCounter <= 0 ? player[i].getNextSpriteIndex() : player[i].getSpriteIndex();
			AffineTransform at1 = new AffineTransform();
			at1.translate(player[i].x, player[i].y + 5);
			at1.rotate(Math.toRadians(player[i].direction));
			at1.translate(-feet_running_sprite[playerMoveIndex].getWidth()/2, -feet_running_sprite[playerMoveIndex].getHeight()/2);

			AffineTransform at2 = new AffineTransform();
			at2.translate(player[i].x, player[i].y);
			at2.rotate(Math.toRadians(player[i].direction));
			at2.translate(-player_running_sprite[playerMoveIndex].getWidth()/2, -player_running_sprite[playerMoveIndex].getHeight()/2);

			if(player[i].shooting && !player[i].idle){
				playerShootIndex = soldierCounter <= 0 ? player[i].getNextShootSpriteIndex() : player[i].getShootSpriteIndex();
				g2d.drawImage(feet_running_sprite[playerMoveIndex], at1, null);
				g2d.drawImage(player_shooting_sprite[playerShootIndex], at2, null);
			}else if(player[i].shooting && player[i].idle){
				playerShootIndex = soldierCounter <= 0 ? player[i].getNextShootSpriteIndex() : player[i].getShootSpriteIndex();
				g2d.drawImage(feet_idle_sprite, at1, null);
				g2d.drawImage(player_shooting_sprite[playerShootIndex], at2, null);
			}
			else if(!player[i].idle){
				g2d.drawImage(feet_running_sprite[playerMoveIndex], at1, null);
				g2d.drawImage(player_running_sprite[playerMoveIndex], at2, null);
			}else{
				g2d.drawImage(feet_idle_sprite, at1, null);
				g2d.drawImage(player_idle_sprite[playerMoveIndex], at2, null);
			}
		}
		if(zombie != null){
			for(int i = 0, n = zombie.length; i < n; i++){
				if(zombie[i].x == -200 && zombie[i].y == -200)
					break;
				if(zombie[i].x == -100 && zombie[i].y == -100)
					continue;

				zombieMoveIndex = zombieCounter <= 0 ? zombie[i].getNextSpriteIndex() : zombie[i].getSpriteIndex();
				AffineTransform at = new AffineTransform();
				at.translate(zombie[i].x, zombie[i].y);
				at.rotate(Math.toRadians(zombie[i].direction));
				at.translate(-zombie_moving_sprite[zombieMoveIndex].getWidth()/2, -zombie_moving_sprite[zombieMoveIndex].getHeight()/2);

				if(zombie[i].attacking){
					zombieAttackIndex = zombieCounter <= 0 ? zombie[i].getNextAttackSpriteIndex() : zombie[i].getAttackSpriteIndex();
					g2d.drawImage(zombie_attacking_sprite[zombieAttackIndex], at, null);					
				}else
					g2d.drawImage(zombie_moving_sprite[zombieMoveIndex], at, null);

				if(zombie[i].hit){
					splats.add(new Bloodsplat(new Point(zombie[i].x - 20, zombie[i].y - 20)));
					zombie[i].hit = false;
				}
			}
		}
		if(soldierCounter <= 0)
			soldierCounter = deltaTime*2;
		if(zombieCounter <= 0)
			zombieCounter = deltaTime*2;

		soldierCounter -= deltaTime;
		zombieCounter -= deltaTime;
		if(gameOver){
			g.setColor(new Color(153, 0, 0, 120));
			g.fillRect(0, 0, screenWidth, screenHeigth);
			g.drawImage(gameOverSprite, x, y, null);
			g.setColor(Color.BLACK);
			g.drawImage(menuButtonSprite[menuSprite], menuButtonRect.x, menuButtonRect.y, null);
			g.drawImage(exitButtonSprite[exitSprite], exitButtonRect.x, exitButtonRect.y, null);
			g.setFont(font.deriveFont(Font.PLAIN, 24));
			if(deadId == id)
				g.drawString("Voce morreu!", 415, 380);
			else
				g.drawString("Seu parceiro morreu!", 380, 380);
		}
		if(waiting){
			g.setColor(new Color(0, 0, 0, 110));
			g.fillRect(0, 0, screenWidth, screenHeigth);
			g.setColor(Color.BLACK);
			g.setFont(font.deriveFont(Font.PLAIN, 24));
			g.drawString("ESPERANDO O SEGUNDO JOGADOR", 150, screenHeigth - 50);
		}
	}	
	BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight){
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledBI.createGraphics();
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
        g.dispose();
        return scaledBI;
    }

    BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

    void setAlpha(BufferedImage img, byte alpha) {       
    	alpha %= 0xff; 
	    for (int cx=0;cx<img.getWidth();cx++) {          
	        for (int cy=0;cy<img.getHeight();cy++) {
	            int color = img.getRGB(cx, cy);

	            int mc = (alpha << 24) | 0x00ffffff;
	            int newcolor = color & mc;
	            img.setRGB(cx, cy, newcolor);
	        }
	    }
	}	
    public void mousePressed(MouseEvent e) {
    	if(gameOver){
	    	if(menuButtonRect.contains(e.getPoint())){
	    		menuSprite = 1;
	    	}else if(exitButtonRect.contains(e.getPoint())){
	    		exitSprite = 1;
	    	}
	    }
    }

    public void mouseReleased(MouseEvent e) {
    	if(gameOver){
	    	if(menuSprite == 1)
	    		menuSprite = 0;
	    	if(exitSprite == 1)
	    		exitSprite = 0;
	    	if(menuButtonRect.contains(e.getPoint())){
	    		launcher.restartLauncher();
	    	}else if(exitButtonRect.contains(e.getPoint())){
	    		System.exit(0);
	    	}
    	}
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }
}
