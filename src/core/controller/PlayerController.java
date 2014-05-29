package core.controller;

import java.util.ArrayList;
import java.util.List;

import network.packet.Packet;
import gui.ui.HudOverlay;
import model.Board;
import phys.Point2D;

public abstract class PlayerController {

	String player;
	int playerID;

	protected Point2D cursorPosition; 
	protected List<Packet> commands;
	
	public PlayerController(String player, int playerID) {
		this.player = player;
		this.playerID = playerID;
		this.cursorPosition = new Point2D();
		this.commands = new ArrayList<Packet>(2);
	}

	public String getPlayer() {
		return player;
	}
	
	public int getPlayerID() {
		return playerID;
	}
	
	public void setPlayer(String player) {
		this.player = player;
	}
		
	public Point2D getCursorPosition() {
		return cursorPosition;
	}
	
	protected float clampCursor(float value, float min, float max) {
		if(value>max) return max;
		if(value<min) return min;
		return value;
	}
	
	public List<Packet> getCommands() {
		return commands;
	}
	
	public abstract boolean isSelecting();
	public abstract boolean isPausing();
	public abstract void pollInput(Board board, HudOverlay overlay);
}
