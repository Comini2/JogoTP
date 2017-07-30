class Game{
 
	Game(){
		new PlayerScript();
		new UpdateThread(new Screen()).start();
	}

	public static void main(String args[]){
		new Game();
	}
}