import java.awt.image.BufferedImage;
import java.io.File;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.LinkedList;

class SpriteLoader{
	

	public static BufferedImage[] loadSpritesOnFolder(String url, int frames){
		LinkedList<BufferedImage> sprites = new LinkedList<>();
		try{
			File folder = new File(url);
			for(File fileEntry: folder.listFiles()){
				if(!fileEntry.isDirectory()){
					sprites.add(ImageIO.read(fileEntry));
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}

		return sprites.toArray(new BufferedImage[frames]);
	}
}