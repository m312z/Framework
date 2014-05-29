package core.menu;

import static core.Frame.FULLSCREEN;
import static core.Frame.SCREEN_SIZE;
import static core.Frame.WINDOW_SIZE;
import gui.FontManager.FontType;
import gui.Pallete;
import gui.ui.ButtonElement;
import gui.ui.HudElement.InteractionType;
import gui.ui.HudOverlay;
import gui.ui.TextElement;

import java.awt.Color;
import java.util.List;

import org.lwjgl.opengl.Display;

import phys.Point2D;
import phys.Shape;
import core.Frame;
import core.Frame.GameState;

/**
 * Main menu controller.
 * @author Michael Cashmore
 */
public class MainMenuScreen extends SetupScreen {

	/* GUI */
	HudOverlay menuOverlay;
		
	public MainMenuScreen(Frame frame) {
		super(frame);
		makeOverlay();
	}
	
	private void makeOverlay() {
		
		// create menu UI
		menuOverlay = new HudOverlay();
		float hs = SCREEN_SIZE[1]/10;
		Shape bs = new Shape(new Point2D[] {
				new Point2D(-hs*2,-hs/2),
				new Point2D( hs*2,-hs/2),
				new Point2D( hs*2, hs/2),
				new Point2D(-hs*2, hs/2)
		});
		
		ButtonElement startButton = new ButtonElement("start_button", bs, new Point2D(SCREEN_SIZE[0]/2, SCREEN_SIZE[1]/2 - 2*hs), Color.BLACK, Pallete.dull, Color.WHITE);
		startButton.addCommand(InteractionType.MOUSE_DOWN, "start");
		startButton.addElement(new TextElement("sbt", bs, new Point2D(0,0), "PLAY", FontType.FONT_32));
		
		ButtonElement joinButton = new ButtonElement("join_button", bs, new Point2D(SCREEN_SIZE[0]/2, SCREEN_SIZE[1]/2 - hs), Color.BLACK, Pallete.dull, Color.WHITE);
		joinButton.addCommand(InteractionType.MOUSE_DOWN, "join");
		joinButton.addElement(new TextElement("jbt", bs, new Point2D(0,0), "JOIN", FontType.FONT_32));
		
		ButtonElement optionsButton = new ButtonElement("options_button", bs, new Point2D(SCREEN_SIZE[0]/2, SCREEN_SIZE[1]/2), Color.BLACK, Pallete.dull, Color.WHITE);
		optionsButton.addCommand(InteractionType.MOUSE_DOWN, "options");
		optionsButton.addElement(new TextElement("obt", bs, new Point2D(), "OPTIONS", FontType.FONT_32));
		
		ButtonElement quitButton = new ButtonElement("quit_button", bs, new Point2D(SCREEN_SIZE[0]/2, SCREEN_SIZE[1]/2 + hs), Color.BLACK, Pallete.dull, Color.WHITE);
		quitButton.addCommand(InteractionType.MOUSE_DOWN, "quit");
		quitButton.addElement(new TextElement("qbt", bs,new Point2D(), "QUIT", FontType.FONT_32));
		
		menuOverlay.addElement(startButton);
		menuOverlay.addElement(joinButton);
		menuOverlay.addElement(optionsButton);
		menuOverlay.addElement(quitButton);
	}

	public void start() {
				
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
				finish();
		}
	}

	private void pollInput() {
		List<String> commands = menuOverlay.pollInput();
		for(String com: commands) {
			switch(com) {
			case "start":
				frame.state = GameState.HOST_GAME;
				finished = true;
				break;
			case "join":
				frame.state = GameState.CLIENT_GAME;
				finished = true;
				break;
			case "options":
				frame.state = GameState.SETUP;
				finished = true;
				break;
			case "quit":
				finish();
				break;
			}
		}
	}
		
	@Override
	public void cancel() {
		frame.state = GameState.MAINMENU;
		finished = true;
	}
	
	@Override
	public void finish() {
		frame.state = GameState.END;
		finished = true;
	}
	
	public void fullScreen() {
		FULLSCREEN = FULLSCREEN^true;
		frame.setDisplayMode(WINDOW_SIZE[0], WINDOW_SIZE[1], FULLSCREEN);
		makeOverlay();
	}
}
