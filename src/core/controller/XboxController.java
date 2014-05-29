package core.controller;

import gui.ui.HudOverlay;
import model.Board;
import core.Frame;

public class XboxController extends core.controller.PlayerController {

	net.java.games.input.Controller controller;
	
	public XboxController(String playerID, net.java.games.input.Controller controller) {
		super(playerID);
		this.controller = controller;
	}

	public net.java.games.input.Controller getController() {
		return controller;
	}

	@Override
	public void pollInput(Board board, HudOverlay overlay) {
		
		controller.poll();
		
//		EventQueue queue = controller.getEventQueue();
//		Event event = new Event();		
		
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
