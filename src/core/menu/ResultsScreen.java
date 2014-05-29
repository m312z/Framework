package core.menu;

import static core.Frame.SCREEN_SIZE;
import gui.FontManager.FontType;
import gui.ui.ButtonElement;
import gui.ui.HudElement.InteractionType;
import gui.ui.HudOverlay;
import gui.ui.TextElement;

import java.awt.Color;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import phys.Point2D;
import phys.Shape;
import core.Frame;
import core.Frame.GameState;

/**
 * Main menu controller.
 * @author Michael Cashmore
 */
public class ResultsScreen extends SetupScreen {

	/* GUI */
	HudOverlay menuOverlay;
	
	Point2D[] cursorPosition = new Point2D[] {
			new Point2D(Frame.SCREEN_SIZE[0]/3f,Frame.SCREEN_SIZE[1]/2f),
			new Point2D(Frame.SCREEN_SIZE[0]*2/3f,Frame.SCREEN_SIZE[1]/2f)
	};
	
	boolean hotDown[] = {false, false};
	
	public ResultsScreen(Frame frame) {
		super(frame);	
		makeOverlay();
	}
	
	private void makeOverlay() {
		
		// create menu UI
		menuOverlay = new HudOverlay();
		float hs = SCREEN_SIZE[1]/10;
		Shape bs = new Shape(new Point2D[] {
				new Point2D(-hs*4/3f,-hs/2),
				new Point2D( hs*4/3f,-hs/2),
				new Point2D( hs*4/3f, hs/2),
				new Point2D(-hs*4/3f, hs/2)
		});
		
		ButtonElement backButton = new ButtonElement("quit_button", bs, new Point2D(SCREEN_SIZE[0]/2 - hs*8/3f, SCREEN_SIZE[1]-hs), Color.BLACK, Color.BLACK, Color.WHITE);
		backButton.addCommand(InteractionType.MOUSE_DOWN, "quit");
		backButton.addElement(new TextElement("bbt", bs,new Point2D(), "MAIN MENU", FontType.FONT_32));
		
		ButtonElement powerButton = new ButtonElement("play_button", bs, new Point2D(SCREEN_SIZE[0]/2, SCREEN_SIZE[1]-hs), Color.BLACK, Color.BLACK, Color.WHITE);
		powerButton.addCommand(InteractionType.MOUSE_DOWN, "play");
		powerButton.addElement(new TextElement("pbt", bs,new Point2D(), "POWER SETUP", FontType.FONT_32));

		ButtonElement restartButton = new ButtonElement("restart_button", bs, new Point2D(SCREEN_SIZE[0]/2 + hs*8/3f, SCREEN_SIZE[1]-hs), Color.BLACK, Color.BLACK, Color.WHITE);
		restartButton.addCommand(InteractionType.MOUSE_DOWN, "restart");
		restartButton.addElement(new TextElement("rbt", bs,new Point2D(), "RESTART", FontType.FONT_32));
		
		menuOverlay.addElement(powerButton);
		menuOverlay.addElement(restartButton);
		menuOverlay.addElement(backButton);
	}

	public void start() {
		
		Mouse.setGrabbed(true);
		finished = false;
		
		while(!finished) {

			// pollInput
			pollInput();
						
			// draw view
			frame.getBackground().draw();
			menuOverlay.draw();
			
			// opengl update
			Display.update();
			Display.sync(60);
			if (Display.wasResized()) {
	            frame.setDisplayMode(
	            		Display.getWidth(),
	            		Display.getHeight(),
	            		Frame.FULLSCREEN);
	            makeOverlay();
			}
			
			if(Display.isCloseRequested())
				finished = true;
		}
		
		Mouse.setGrabbed(false);
	}

	private void pollInput() {
		
		List<String> commands = menuOverlay.pollInput();
		for(String com: commands) {
			switch(com) {
			case "restart":
				frame.state = GameState.MAINMENU;
				finished = true;
				break;
			case "play": 
				frame.state = GameState.SETUP;
				finished = true;
				break;
			case "quit": 
				frame.state = GameState.MAINMENU;
				finished = true;
				break;
			}
		}
	}
	
	@Override
	public void cancel() {
		frame.state = GameState.MAINMENU;
		finished = true;		
	}
}
