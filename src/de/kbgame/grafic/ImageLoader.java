package de.kbgame.grafic;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import de.kbgame.geometry.ImageKey;

public class ImageLoader {
	
	/** Singleton Instance von ImageLoader */
	private static ImageLoader instance = new ImageLoader();
	/** Map von alle Bilder. Jeder Bilder wird zur einem bestimmten Key gespeichert */
	private final Map<ImageKey, BufferedImage> images = new HashMap<ImageKey, BufferedImage>();
	
	/**
	 * Konstruktor von ImageLoader, alle Bilder wird durch eine Konfigurationsdatei gespeichert
	 */
	private ImageLoader() {
		loadImages("Images/confImage/standard.txt");
	}
	
	/**
	 * Laden alle Bilder von eine Konfigurationsdatei.
	 * Syntax von Konfiguration: "ImageKey:image-path.png"
	 * 
	 * @param configFile Konfigurationsdatei
	 */
	public void loadImages(String configFile){
		BufferedReader bfr = null;
		try {
			FileReader fstream = new FileReader(configFile);
			bfr = new BufferedReader(fstream);
			String line;
			while ( (line = bfr.readLine()) != null){
				if(line.contains(":")) {
					String[] s = line.trim().split(":");
					if(s.length == 2 && !"".equals(s[0]) && !"".equals(s[1])) {
						images.put(ImageKey.valueOf(s[0]), ImageIO.read(new File(s[1])));
					}
				}
			}
		} catch(Exception e){ 
			e.printStackTrace();
		} finally{
			try{ 
				bfr.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public BufferedImage getImageByKey(ImageKey key) {
		return images.get(key);
	}
	
	public static ImageLoader getInstance() {
		return ImageLoader.instance;
	}
}
