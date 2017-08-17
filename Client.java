import java.net.*;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.awt.Point;


class Client implements Runnable, KeyListener{
	Socket socket;
	DataInputStream in;
	DataOutputStream out;
	
	Screen screen;
	
	int id;
	int remoteId;
	int px, py;
	
	boolean left, right, up, down, shoot, gameOver = false, waiting = true;
	
	Player player[] = {new Player(), new Player()};
	Zombie zombie[] = new Zombie[1024];

	final int PLAYER_DATA_PROTOCOL = 1227;
	final int PLAYER_SHOOT_PROTOCOL = 1237;
	final int ZOMBIE_DATA_PROTOCOL = 1337;
	final int ZOMBIE_ATTACK_PROTOCOL = 1447;

	int screenWidth;
	int screenHeight;
	final long DELTA_TIME = 1000/60;
		
	Client(Launcher launcher, int screenWidth, int screenHeight) throws IOException{
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		Point spawns[] = {new Point(screenWidth/2 - 50, screenHeight/2), new Point(screenWidth/2 + 50, screenHeight/2)};
		for(int i = 0; i<zombie.length; i++)
			zombie[i] = new Zombie();
		
		socket = new Socket("192.168.173.1", 7777);
		in = new DataInputStream(socket.getInputStream());
		id = in.readInt();
		remoteId = id == 0 ? 1 : 0;
		player[id].health = in.readInt();

		px = spawns[id].x;
		py = spawns[id].y;
		player[id].x = px;
		player[id].y = py;

		player[remoteId].x = spawns[remoteId].x;
		player[remoteId].y = spawns[remoteId].y;

		out = new DataOutputStream(socket.getOutputStream());
		screen = new Screen(screenWidth, screenHeight, DELTA_TIME,player, zombie, id, launcher);
		launcher.getContentPane().removeAll();
		launcher.add(screen);
		launcher.pack();
		launcher.setSize(screenWidth, screenHeight);
		launcher.addKeyListener(this);
		launcher.setFocusable(true);
		launcher.repaint();

		Input input = new Input(in, this);
		Thread t = new Thread(input);
		t.start();
		Thread t2 = new Thread(this);
		t2.start();
	}

	public void startGame(){
		waiting = false;
		screen.startGame();
	}
	
	public void updatePlayerData(int pid,int healthin, int xin, int yin){
		if(xin > player[pid].x)
			player[pid].direction = 0;
		else if(xin < player[pid].x )
			player[pid].direction = 180;
		else if(yin > player[pid].y )
			player[pid].direction = 90;
		else if(yin < player[pid].y)
			player[pid].direction = 270;

		if(player[pid].x == xin && player[pid].y == yin)
			player[pid].shooting = true;

		player[pid].health = healthin;
		player[pid].x = xin;
		player[pid].y = yin;
	}

	public void updatePlayerData(int pid, int newHealth){
		player[pid].health = newHealth;
	}

	public void updateZombieData(int i, int xin, int yin, int healthin){
		if(xin > zombie[i].x)
			zombie[i].direction = 0; //OLHANDO PARA A DIREITA
		else if(xin < zombie[i].x )
			zombie[i].direction = 180; // OLHANDO PARA A ESQUERDA
		else if(yin > zombie[i].y )
			zombie[i].direction = 90; // OLHANDO PARA BAIXO
		else if(yin < zombie[i].y)
			zombie[i].direction = 270; // OLHANDO PARA CIMA

		if(zombie[i].attackCooldown <= 0){
			int xDist = Math.abs(xin - player[id].x);	
			int yDist = Math.abs(yin - player[id].y);
			if(xDist <= 25 && yDist <=25){
				sendZombieAttackRequest(i);
			}
		}
		if(zombie[i].attackCooldown > 0)
			zombie[i].attackCooldown -= DELTA_TIME;
		zombie[i].x = xin;
		zombie[i].y = yin;
		zombie[i].health = healthin;
	}

	public void zombieAttackRequestResponse(int zombieId){
		zombie[zombieId].attacking = true;
		zombie[zombieId].attackCooldown = zombie[zombieId].attackRate;
	}

	public void updateLimit(int n, int round){
		screen.setRound(round);
		zombie[n].x = -200;
		zombie[n].y = -200;
	}

	public void shootRequestResponse(int id, int zid, int killCount){
		player[id].shooting = true;
		player[id].killCount = killCount;
		if(zid != -1){
			zombie[zid].hit = true;		
		}
	}

	public void sendZombieAttackRequest(int i){
		try {
			out.writeInt(ZOMBIE_ATTACK_PROTOCOL);
			out.writeInt(i);
			out.writeInt(id);
			out.writeInt(player[id].health);
		} catch (IOException e) {
			out = null;
		}
	}

	public void sendShootRequest(){
		try{
			out.writeInt(PLAYER_SHOOT_PROTOCOL);
			out.writeInt(id);
			out.writeInt(player[id].direction);
		}catch(IOException e){
			out = null;
		}
	}

	public void sendPlayerData(){
		try {
			out.writeInt(PLAYER_DATA_PROTOCOL);
			out.writeInt(id);
			out.writeInt(player[id].health);
			out.writeInt(px);
			out.writeInt(py);
		} catch (IOException e) {
			out = null;
		}
	}

	public void requestZombieData(){
		try {
			out.writeInt(ZOMBIE_DATA_PROTOCOL);
		} catch (IOException e) {
			out = null;
		}
	}

	public void gameOver(int pid){
		gameOver = true;
		try{
			out.close();
			in.close();
			socket.close();
		}catch(IOException e){
			e.printStackTrace();
		}

		screen.gameOver(pid);
	}

	@Override
	public void run() {
		int oldX = 0;
		int oldY = 0;
		float fireRate = 400f;
		float shootCounter = 0f;
		while(true){
			if(!gameOver && !waiting){
				if(right)
					px += 3;
				else if(left)
					px -= 3;
				else if(up)
					py -= 3;
				else if(down)
					py += 3;

				if(right || left || up || down){
					sendPlayerData();
					player[id].idle = false;
				}else
					player[id].idle = true;

				if(shoot && shootCounter <= 0){
					sendShootRequest();
					shootCounter = fireRate;
				}
				if(player[remoteId].x == oldX && player[remoteId].y == oldY)
					player[remoteId].idle = true;
				else
					player[remoteId].idle = false;

				oldX = player[remoteId].x;
				oldY = player[remoteId].y;

				requestZombieData();

				if(shootCounter > 0)
					shootCounter -= DELTA_TIME;
			}
			screen.repaint();
			try {
				Thread.sleep(DELTA_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(px > 0)
					left = true;
				break;
			case KeyEvent.VK_RIGHT:
				if(px < screenWidth)
					right = true;
				break;
			case KeyEvent.VK_UP:
				if(py > 0)
					up = true;
				break;
			case KeyEvent.VK_DOWN:
				if(py < screenHeight)
					down = true;
				break;
			case KeyEvent.VK_SPACE:
				shoot = true;
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left = false;
			break;
		case KeyEvent.VK_RIGHT:
			right = false;
			break;
		case KeyEvent.VK_UP:
			up = false;
			break;
		case KeyEvent.VK_DOWN:
			down = false;
			break;
		case KeyEvent.VK_SPACE:
			shoot = false;
			break;
		}
	}
	@Override
	public void keyTyped(KeyEvent arg0) {}
}

class Input implements Runnable{

	DataInputStream in;
	Client client;
	int protocol;
	final int PLAYER_DATA_PROTOCOL = 1227;
	final int PLAYER_SHOOT_PROTOCOL = 1237;
	final int ZOMBIE_DATA_PROTOCOL = 1337;
	final int ZOMBIE_ATTACK_PROTOCOL = 1447;
	final int WAIT_PROTOCOL = 1577;
	final int START_PROTOCOL = 1578;
	final int GAMEOVER_PROTOCOL = 666;
	
	Input(DataInputStream in, Client client){
		this.in = in;
		this.client = client;
	}
	
	@Override
	public void run() {
		while(true){
			try {
				protocol = in.readInt();
				if(protocol == PLAYER_DATA_PROTOCOL){
					int pid = in.readInt();
					int health = in.readInt();
					int x = in.readInt();
					int y = in.readInt();
					client.updatePlayerData(pid, health, x, y);
				}else if(protocol == ZOMBIE_DATA_PROTOCOL){
					int n=in.readInt();
					int round = in.readInt();
					for(int i = 0; i<n; i++){
						int x = in.readInt();
						int y = in.readInt();
						int health = in.readInt();
						client.updateZombieData(i, x, y, health);
					}
					client.updateLimit(n, round);
				}else if(protocol == ZOMBIE_ATTACK_PROTOCOL){
					int zombieId = in.readInt();
					int pid = in.readInt();
					int health = in.readInt();
					client.updatePlayerData(pid, health);
					client.zombieAttackRequestResponse(zombieId);
				}else if(protocol == PLAYER_SHOOT_PROTOCOL){
					int pid = in.readInt();
					int zid = in.readInt();
					int killCount = in.readInt();
					client.shootRequestResponse(pid, zid, killCount);
				}
				else if(protocol == GAMEOVER_PROTOCOL){
					int pid = in.readInt();
					client.gameOver(pid);
					break;
				}else if(protocol == START_PROTOCOL){
					client.startGame();
				}
			} catch (IOException e) {
				in = null;
				break;
			}
		}
	}
	
}
