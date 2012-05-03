package com.Reader.Command;

public interface Command {
	public static final int NEXTLINE = 0;
	public static final int NEXTPAGE = 1;
	public static final int JUMP = 2;
	public static final int PRELINE = 3;
	public static final int PREPAGE = 4;
	public static final int PRECHAPTER = 5;
	public static final int NEXTCHAPTER = 6;
	public static final int EXIT = 7;
	public static final int RETURN = 8;
	public void excute();
}
