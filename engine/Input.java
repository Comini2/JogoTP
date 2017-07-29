import java.awt.event.*;

class Input implements KeyListener{

	private static boolean keysPressed[] = new boolean[256];
	 @Override
    public synchronized void keyPressed(KeyEvent e) {
        keysPressed[e.getKeyCode()] = true;
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        keysPressed[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {/* Not used */ }

    public static boolean isKeyPressed(int key){
    	return keysPressed[key];
    }

    public static boolean isKeyReleased(int key){
    	return !keysPressed[key];
    }

}