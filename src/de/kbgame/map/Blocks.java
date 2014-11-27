package de.kbgame.map;

public final class Blocks {

	private Blocks() {
        throw new AssertionError();
    }

	public final static byte Empty = 0;
	public final static byte Solid = 1;
	public final static byte Plattform = 2;
	public final static byte Boden = 3;
	public final static byte QuestionBlock = 4;
	public static final byte ENEMY = 5;
}
