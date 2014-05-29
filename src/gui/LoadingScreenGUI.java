package gui;

import static core.Frame.SCREEN_SIZE;
import static gui.GameGUI.VIEW_SIZE;
import static gui.GameGUI.hudSize;
import static gui.GameGUI.scale;
import static gui.OpenGLDraw.fillRect;

import java.awt.Color;

import model.Board;

public class LoadingScreenGUI {

	public void draw(float completion) {
	
		scale = (SCREEN_SIZE[0]/GameGUI.VIEW_SIZE[0]);
		GameGUI.VIEW_SIZE[1] = SCREEN_SIZE[1]/scale;
		GameGUI.offX = (GameGUI.VIEW_SIZE[0]/2 - Board.BOARD_SIZE[0]/2) * scale;
		GameGUI.offY = (GameGUI.VIEW_SIZE[1]/2 - Board.BOARD_SIZE[1]/2) * scale;
		hudSize = (VIEW_SIZE[0]*scale)/10;
		
		// draw background
		fillRect(Color.BLACK, 0, 0, SCREEN_SIZE[0], SCREEN_SIZE[1]);
	}
}
