package core.controller;

import gui.ui.HudOverlay;
import model.Board;
import core.Frame;

public class KeyboardController extends PlayerController {

	public KeyboardController(String player, int playerID) {
		super(player, playerID);
	}

	@Override
	public void pollInput(Board board, HudOverlay overlay) {

		cursorPosition.x = clampCursor(cursorPosition.x,0,Frame.SCREEN_SIZE[0]-10);
		cursorPosition.y = clampCursor(cursorPosition.y,0,Frame.SCREEN_SIZE[1]-10);
	}

	@Override
	public boolean isPausing() {
		return false;
	}

	@Override
	public boolean isSelecting() {
		return false;
	}
}
