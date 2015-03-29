package de.kbgame.map;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.kbgame.game.Game;
import de.kbgame.game.level.LevelSegment;
import de.kbgame.grafic.ImageLoader;

public class LevelBuilder implements Iterator<Level> {

	private ArrayList<String> configLines = new ArrayList<String>();
	private int currentIndex = 0;
	private Game game;
	private Point playerStart;
	
	public final static String CONFIG_REG_EX = "(\\w+)\\((.*)\\)";
	public final static String LEVEL_SEGMENT_PACKAGE = "de.kbgame.game.level";

	/**
	 * Erstellt anhand einer Konfigurationsdatei Level.
	 * 
	 * @param config die Konfigurationsdatei, welche die Beschreibung der Level und deren Segmente enthält
	 * @param g
	 * @param playerStart wird während des Levelaufbaus mit einem Startpunkt überschrieben
	 * @throws IOException
	 */
	public LevelBuilder(File config, Game g, Point playerStart) throws IOException {
		readConfig(config);

		game = g;
		this.playerStart = playerStart;
	}

	/**
	 * Liest die übergebene Kongiuration zeilenweise ein und speichert diese für die
	 * weitere Verarbeitung.
	 * 
	 * @param config die Konfigurationsdatei
	 * @throws IOException
	 */
	private void readConfig(File config) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(config));
		String line;

		while ((line = reader.readLine()) != null) {
			configLines.add(line);
		}

		reader.close();
	}
	
	/**
	 * Erstellt und gibt das aktuelle Level zurück.
	 * 
	 * @return das aktuelle Level
	 */
	public Level current() {
		currentIndex--;
		return next();
	}
	
	/**
	 * Beginnt mit dem Erstellen der Levels von vorne.
	 */
	public void restartGame() {
		currentIndex = 0;
	}

	@Override
	public boolean hasNext() {
		return currentIndex - 1 < configLines.size();
	}

	/**
	 * Erstellt das nächste Level aus der Konfiguration und liefert diese zurück.
	 */
	@Override
	public Level next() {
		if (currentIndex >= configLines.size()) {
			throw new ArrayIndexOutOfBoundsException(currentIndex);
		}
		
		Level level = null;
		String line = configLines.get(currentIndex++);
		LevelSegment segment;
		ArrayList<LevelSegment> levelSegments = new ArrayList<LevelSegment>();
		String[] levelSettings = null;

		// split current line in level segments
		String[] levelSegmentsString = line.trim().replaceAll("(\\s)", "").split(";");
		
		if (levelSegmentsString.length > 0) {
			levelSettings = parseLevelSettings(levelSegmentsString[0]);
			if(levelSettings[0].length() > 0)
				ImageLoader.getInstance().loadImages("Images/confImage/" + levelSettings[0] + ".txt");
		}

		// process each level segment
		for (int i = 1; i < levelSegmentsString.length; i++) {
			// try to create a level segment
			segment = parseSegment(levelSegmentsString[i], game);
			if (segment != null) {
				// if a level segment could be created by its string representation, it's added to the level list
				levelSegments.add(segment);
			}
		}

		if (levelSegments.size() > 0) {
			level = Level.createLevel(levelSegments, game, playerStart, levelSettings);
		}

		return level;
	}

	/**
	 * Erstellt ein einzelndes Segment aus dessen Beschreibung in der Konfigurationsdatei.
	 * 
	 * @param segmentString die Beschreibung des Segments
	 * @param g
	 * @return die Java Repräsentation des Segments
	 */
	private static LevelSegment parseSegment(String segmentString, Game g) {
		Class<?> levelSegmentCl;

		Pattern pattern = Pattern.compile(CONFIG_REG_EX);
		// split the string into segment class name and arguments
		Matcher matcher = pattern.matcher(segmentString);
		matcher.find();

		// 1st group holds the segment class name
		String segmentClass = matcher.group(1);
		// 2nd group contains a comma separated argument list
		String[] constructorArgs = matcher.group(2).split(",");

		LevelSegment level = null;

		try {
			// look up the level segment class name in the associated package
			levelSegmentCl = Class.forName(LEVEL_SEGMENT_PACKAGE + "." + segmentClass);
			// search for a constructor that accepts exact 2 arguments of the types Game and String[]
			Constructor<?> constr = levelSegmentCl.getConstructor(Game.class, String[].class);

			if (levelSegmentCl.getSuperclass() == LevelSegment.class) {
				// if a such a constructor exists it's invoked with the specific parameters
				level = (LevelSegment) constr.newInstance(g, constructorArgs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// ignore unknown classes
			// ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			// IllegalAccessException, IllegalArgumentException, InvocationTargetException
			System.err.println(segmentClass + " couldn't be instantiated");
		}

		return level;
	}
	
	/**
	 * Spaltet die Leveleinstellungen in dessen Komponenten.
	 * 
	 * @param tupel durch Kommas getrennte Einstellungen
	 * @return die Einstellungen
	 */
	private String[] parseLevelSettings(String tupel) {
		return tupel.replaceAll("\\(|\\)", "").split(",");
	}

}
