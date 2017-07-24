import java.io.*;
import java.net.*;
import java.awt.geom.Line2D;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JFrame;

public class Server extends JFrame{

	static final int SCREEN_WIDTH = 1024;
	static final int SCREEN_HEIGTH = 768;
	
	static ServerSocket serverSocket;
	static Users[] user = new Users[2];
	static Spawner spawner = new Spawner(SCREEN_WIDTH, SCREEN_HEIGTH);
	static int cont = 0;
	static boolean playing = false;
	
	public static void main(String args[]) throws IOException{
		serverSocket = new ServerSocket(7777);
		Thread spawnerThread = new Thread(spawner);
		
		while(true){
			if(cont == 2 && !playing){
				spawner.nextRound();
				spawnerThread.start();
				playing = true;
			}
			Socket socket = serverSocket.accept();
			for(int i = 0; i<2; i++){
				if(user[i] == null){
					cont++;
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					DataInputStream in = new DataInputStream(socket.getInputStream());
					user[i] = new Users(out, in, user, i, spawner);
					Thread t = new Thread(user[i]);
					t.start();
					break;
				}
			}
		}
	}
}

class Users implements Runnable{
	
	DataOutputStream out;
	DataInputStream in;
	static Users[] user = new Users[2];
	int id;
	int protocol;
	static Spawner spawner;
	static Player player[];
	static Zombie zombie[];
	static boolean gameOver = false;

	static final int START = 1117;
	static final int PLAYER_DATA_PROTOCOL = 1227;
	static final int PLAYER_SHOOT_PROTOCOL = 1237;
	static final int ZOMBIE_DATA_PROTOCOL = 1337;
	static final int ZOMBIE_ATTACK_PROTOCOL = 1447;
	static final int GAMEOVER_PROTOCOL = 666;
	static final int INITIAL_HEALTH = 100;

	final int SCREEN_WIDTH = 1024;
	final int SCREEN_HEIGHT = 800;

	Point spawns[] = {new Point(SCREEN_WIDTH/2 - 50, SCREEN_HEIGHT/2), new Point(SCREEN_WIDTH/2 + 50, SCREEN_HEIGHT/2)};
	
	Users(DataOutputStream out, DataInputStream in, Users[] user, int id, Spawner spawner){
		this.out = out;
		this.in = in;
		this.user = user;
		this.id = id;
		this.spawner = spawner;
		player = spawner.player;
		zombie = spawner.zombie;
	}

	@Override
	public void run() {
		try {
			out.writeInt(id);
			out.writeInt(INITIAL_HEALTH);
			player[id].x = spawns[id].x;
			player[id].y = spawns[id].y;
			out.writeInt(spawns[id].x);
			out.writeInt(spawns[id].y);
			//while(cont != 2);
			//out.writeInt(START);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while(!gameOver){
			try {
				protocol = in.readInt();

				if(protocol == PLAYER_DATA_PROTOCOL){
					int idin = in.readInt();
					player[idin].health = in.readInt();
					player[idin].x = in.readInt();
					player[idin].y = in.readInt();

					sendPlayerDataResponse(idin);

				}else if(protocol == ZOMBIE_DATA_PROTOCOL){
					sendZombieDataResponse(id);
				}else if(protocol == ZOMBIE_ATTACK_PROTOCOL){
					int zombieid = in.readInt();
					int idin = in.readInt();
					player[idin].health = in.readInt() - 5;
					sendZombieAttackResponse(zombieid, idin);
					if(player[idin].health <= 0){
						gameOver = true;
						sendGameOverProtocol(idin);
					}
				}else if(protocol == PLAYER_SHOOT_PROTOCOL){
					int idin = in.readInt();
					int direction = in.readInt();
					int killCount = in.readInt();
					int zid = checkShoot(idin, direction);
					
					sendPlyerShootResponse(idin, zid);
				}
			} catch (IOException e) {
				this.user[id] = null;
				break;
			}
		}
		this.user[id] = null;
	}

	private synchronized static void sendGameOverProtocol(int pid) throws IOException{
		for(int i = 0; i<2; i++){
			if(user[i] != null){
				user[i].out.writeInt(GAMEOVER_PROTOCOL);
				user[i].out.writeInt(pid);
			}
		}
	}

	private synchronized static void sendPlayerDataResponse(int idin) throws IOException{
		for(int i = 0; i<2; i++){
			if(user[i] != null){
				user[i].out.writeInt(PLAYER_DATA_PROTOCOL);
				user[i].out.writeInt(idin);
				user[i].out.writeInt(player[idin].health);
				user[i].out.writeInt(player[idin].x);
				user[i].out.writeInt(player[idin].y);
			}
		}
	}

	private synchronized static void sendZombieDataResponse(int pid) throws IOException{
		user[pid].out.writeInt(ZOMBIE_DATA_PROTOCOL);
		int n = spawner.getQuantity();
		int round = spawner.getRound();
		user[pid].out.writeInt(n);
		user[pid].out.writeInt(round);
		for(int i = 0; i<n; i++){
			if(user[pid] != null){
				user[pid].out.writeInt(zombie[i].x);
				user[pid].out.writeInt(zombie[i].y);
				user[pid].out.writeInt(zombie[i].health);
			}
		}
	}

	private synchronized static void sendZombieAttackResponse(int zombieid, int idin) throws IOException{
		for(int i = 0; i<2; i++){
			if(user[i] != null){
				user[i].out.writeInt(ZOMBIE_ATTACK_PROTOCOL);
				user[i].out.writeInt(zombieid);
				user[i].out.writeInt(idin);
				user[i].out.writeInt(player[idin].health);
			}
		}
	}

	private synchronized static void sendPlyerShootResponse(int idin, int zid) throws IOException{
		for(int i = 0; i<2; i++){
			if(user[i] != null){
				user[i].out.writeInt(PLAYER_SHOOT_PROTOCOL);
				user[i].out.writeInt(idin);
				user[i].out.writeInt(zid);
				user[i].out.writeInt(player[idin].killCount);
			}
		}
	}

	public int checkShoot(int idin, int dir){
		Line2D line = null;
		switch(dir){
			case 0:
			line = new Line2D.Float(new Point(player[idin].x + 75/2, player[idin].y + 20 + 75/2), new Point(SCREEN_WIDTH, player[idin].y + 20 + 75/2));
			break;
			case 180:
			line = new Line2D.Float(new Point(player[idin].x + 75/2, player[idin].y - 20 + 75/2), new Point(0, player[idin].y - 20 + 75/2));
			break;
			case 90:
			line = new Line2D.Float(new Point(player[idin].x + 75/2 - 20, player[idin].y + 75/2), new Point(player[idin].x + 75/2 - 20, SCREEN_HEIGHT));
			break;
			case 270:
			line = new Line2D.Float(new Point(player[idin].x + 75/2 + 20, player[idin].y + 75/2), new Point(player[idin].x + 75/2 + 20, 0));
			break;
		}
		for(int i = 0; i<spawner.getQuantity(); i++){
			if(line.intersects(new Rectangle(zombie[i].x, zombie[i].y, 75, 75))){
				zombie[i].health -= 5;
				if(zombie[i].health <= 0)
					player[idin].killCount++;
				return i;
			}
		}
		return -1;
	}

}
