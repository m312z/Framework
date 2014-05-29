package gui;

import static core.Frame.SCREEN_SIZE;
import static gui.OpenGLDraw.fillRect;

import java.awt.Color;

import model.Board;
import model.Player;

/**
 * Draws the game to the screen.
 * Feel free to replace with a prettier version.
 * @author Michael Cashmore
 */
public class GameGUI {
	
	/* size of the view window in terms of game size units */
	public static float[] VIEW_SIZE = {
		Board.BOARD_SIZE[0],
		Board.BOARD_SIZE[1]
	};
	
	/* colour pallete */
	static final Color back = new Color(80,80,80);
		
	/* ratio of SCREEN_SIZE/VIEW_SIZE */
	public static float scale;
	
	/* offset to center the screen when full-screen */
	public static float offX = 0;
	public static float offY = 0;
	public static float hudSize = 0;
	
	/* visual stuff */
	float timer=0;
		
	/**
	 * Draw the game.
	 * @param board	the model to be drawn
	 * @param drawHUD	true if the size-bars are to be drawn
	 */
	public void draw(Board board, Player p, float dt) {

		scale = (SCREEN_SIZE[0]/GameGUI.VIEW_SIZE[0]);
		GameGUI.VIEW_SIZE[1] = SCREEN_SIZE[1]/scale;
		GameGUI.offX = (GameGUI.VIEW_SIZE[0]/2 - Board.BOARD_SIZE[0]/2) * scale;
		GameGUI.offY = (GameGUI.VIEW_SIZE[1]/2 - Board.BOARD_SIZE[1]/2) * scale;
		hudSize = (VIEW_SIZE[0]*scale)/10;
		
		offY += hudSize;
		
		// draw background
//		Texture tex;
		fillRect(Color.BLACK, 0, 0, SCREEN_SIZE[0], SCREEN_SIZE[1]);
		
		timer = (timer+dt)%720;
	}
}
