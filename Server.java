import java.io.*;
import java.net.*;
import java.awt.geom.Line2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;

import javax.swing.JFrame;

class Server implements Runnable{

	final int SCREEN_WIDTH = 1024;
	final int SCREEN_HEIGTH = 768;
	
	ServerSocket serverSocket;
	Users[] user = new Users[2];
	Spawner spawner = new Spawner(SCREEN_WIDTH, SCREEN_HEIGTH);
	Thread spawnerThread = new Thread(spawner);
	int cont = 0;
	volatile boolean serving = false;

	Server(){
		(new Thread(this)).start();
	}

	public void run(){
		try{
			new Thread(new Runnable(){
				public void run(){
					while(!serving);
					while(serving){
						int offUsers = 0;
						for(int i = 0; i<2; i++)
							if(user[i] == null)
								offUsers++;
						cont = 2 - offUsers;
						if(cont == 0){
							System.out.println("Reiniciando Servidor!");
							serving = false;
							cont = 0;
							spawner.terminate();
							user = new Users[2];
							spawner = new Spawner(SCREEN_WIDTH, SCREEN_HEIGTH);
							spawnerThread = new Thread(spawner);
							System.out.println("Servidor reiniciado!");
							new Thread(this).start();
							break;
						}
					}
				}
			}).start();

			serverSocket = new ServerSocket(7777);
			
			while(true){
				if(cont == 2 && !serving){
					spawnerThread.start();
					serving = true;
					for(int i = 0; i<2; i++)
						user[i].startService();
					System.out.println("Inicializando Servico");
				}else if(cont == 2 && serving){
					for(int i = 0; i<2; i++)
						user[i].startService();
				}
				System.out.println("Esperando conexão...");
				Socket socket = serverSocket.accept();
				System.out.println("Conectado!");
				for(int i = 0; i<2; i++){
					if(user[i] == null){
						cont++;
						user[i] = new Users(socket, user, i, spawner);
						Thread t = new Thread(user[i]);
						t.start();
						break;
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public static void main(String args[]){
		new Server();
	}
}

class Users implements Runnable{
	
	DataOutputStream out;
	DataInputStream in;
	int id;
	int protocol;
	Socket socket;

	static Spawner spawner;
	static Users[] user = new Users[2];
	static Player player[];
	static Zombie zombie[];
	static boolean gameOver = false;
	static int cont = 0;
	volatile boolean serving;

	static final int START = 1117;
	static final int PLAYER_DATA_PROTOCOL = 1227;
	static final int PLAYER_SHOOT_PROTOCOL = 1237;
	static final int ZOMBIE_DATA_PROTOCOL = 1337;
	static final int ZOMBIE_ATTACK_PROTOCOL = 1447;
	static final int GAMEOVER_PROTOCOL = 666;
	static final int WAIT_PROTOCOL = 1577;
	static final int START_PROTOCOL = 1578;
	static final int INITIAL_HEALTH = 100;

	static final int SCREEN_WIDTH = 1024;
	static final int SCREEN_HEIGHT = 800;

	Point spawns[] = {new Point(SCREEN_WIDTH/2 - 50, SCREEN_HEIGHT/2), new Point(SCREEN_WIDTH/2 + 50, SCREEN_HEIGHT/2)};
	
	Users(Socket socket, Users[] user, int id, Spawner spawner){
		try{
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
		}catch(IOException e){
			e.printStackTrace();
		}
		this.socket = socket;
		this.out = out;
		this.in = in;
		this.user = user;
		this.id = id;
		this.spawner = spawner;
		player = spawner.player;
		zombie = spawner.zombie;
		cont++;
	}

	public void startService(){
		serving = true;
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

			while(!serving);
			
			System.out.println("Servindo!");
			
			out.writeInt(START_PROTOCOL);
			System.out.println("Start");
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
					new Thread(new Runnable(){
						public void run(){
							int zid = checkShoot(idin, direction);
							try{
								sendPlayerShootResponse(idin, zid);
							}catch(IOException e){
								e.printStackTrace();
							}
						}
					}).start();
				}
			} catch (IOException e) {
				serving = false;
				this.user[id] = null;
				System.out.println("Usuario " + id + " se desconectou.");
				break;
			}
		}
		serving = false;
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

	private synchronized static void sendPlayerShootResponse(int idin, int zid) throws IOException{
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
		ArrayList<Integer> zombiesCoord = new ArrayList<>();
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
		int minDist = Integer.MAX_VALUE;
		int zid = -1;
		for(int i = 0; i<spawner.getQuantity(); i++){
			if(line.intersects(new Rectangle(zombie[i].x, zombie[i].y, 75, 75))){
				int dist = Math.abs(zombie[i].x - player[idin].x) + Math.abs(zombie[i].y - player[idin].y);
				if(dist < minDist){
					zid = i;
					minDist = dist;
				}
			}
		}
		if(zid != -1){
			zombie[zid].health -= 5;
			if(zombie[zid].health <= 0)
				player[idin].killCount++;
		}
		return zid;
	}
}
