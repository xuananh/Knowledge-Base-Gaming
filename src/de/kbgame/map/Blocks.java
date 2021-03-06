package de.kbgame.map;

public final class Blocks {

	private Blocks() {
        throw new AssertionError();
    }

	public final static byte Empty = 0;
	public final static byte Solid = 1;
	public final static byte Plattform = 2;
	public final static byte Floor = 3;
	public final static byte QuestionBlock = 4;
	public final static byte ENEMY = 5;
	public final static byte QUESTION_BLOCK_BOUNCED = 6;
	public final static byte START = 10;
	public final static byte GOAL = 11;
	public final static byte JUMP = 12;
	public final static byte FireBlock = 9;
	public final static byte CannonBlock = 13;
	public final static byte COIN = 14;
	public final static byte CAVE = 15;
	
	public final static int NON_BLOCKING_BITMASK = (int) (Math.pow(2, Empty) + Math.pow(2, GOAL) + Math.pow(2, FireBlock) + Math.pow(2, COIN) + Math.pow(2, CAVE));
	public final static int KILLING_BITMASK = (int) Math.pow(2, FireBlock);
}
