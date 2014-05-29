package core.controller;

import gui.ui.HudOverlay;
import model.Board;

import org.lwjgl.input.Mouse;

import core.Frame;

public class MouseController extends PlayerController {

	public MouseController(String playerID) {
		super(playerID);
	}
		
	@Override
	public boolean isSelecting() {
		return Mouse.isButtonDown(0);
	}
	
	@Override
	public void pollInput(Board board, HudOverlay overlay) {
		
		// cursor position 
		cursorPosition.x = Mouse.getX();
		cursorPosition.y = Frame.SCREEN_SIZE[1] - Mouse.getY();
	}

	@Override
	public boolean isPausing() {
		return false;
	}
}
