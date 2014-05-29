package core.menu;

import static core.Frame.SCREEN_SIZE;
import gui.FontManager.FontType;
import gui.ui.ButtonElement;
import gui.ui.HudElement.InteractionType;
import gui.ui.HudOverlay;
import gui.ui.TextElement;

import java.awt.Color;
import java.util.List;

import network.Client;
import network.packet.Packet;

import org.lwjgl.opengl.Display;

import phys.Point2D;
import phys.Shape;
import core.Frame;

public class ClientLobbySetup extends SetupScreen {

	/* network connection */
	Client connection;
	
	/* true if the game is starting */
	boolean play = false;
	
	/* GUI */
	HudOverlay menuOverlay;
		
	public ClientLobbySetup(Frame frame, Client client) {
		super(frame);
		this.connection = client;
		menuOverlay = new HudOverlay();
		float hs = SCREEN_SIZE[1]/10;
		Shape bs = new Shape(new Point2D[] {
				new Point2D(-hs*2,-hs/2),
				new Point2D(hs*2,-hs/2),
				new Point2D(hs*2,hs/2),
				new Point2D(-hs*2,hs/2)
		});		
		ButtonElement quitButton = new ButtonElement("quit_button", bs, new Point2D(SCREEN_SIZE[0]/2, SCREEN_SIZE[1]/2 + hs), Color.BLACK, Color.BLUE);
		quitButton.addCommand(InteractionType.MOUSE_DOWN, "quit");
		quitButton.addElement(new TextElement("qbt", bs,new Point2D(), "QUIT", Color.WHITE, FontType.FONT_32));
		menuOverlay.addElement(quitButton);
	}
	
	public void clientLobbyStart() {
		
		while(!finished) {
						
			pollInput();
			
			// messages
			getMessages();
			
			// draw view
			frame.getBackground().draw();
			menuOverlay.draw();
			
			// opengl update
			Display.update();
			Display.sync(60);
						
			if(Display.isCloseRequested())
				finished = true;
		}
	}

	private void pollInput() {
		List<String> commands = menuOverlay.pollInput();
		for(String com: commands) {
			switch(com) {
			case "quit": cancel(); break;
			}
		}
	}
	
	private void getMessages() {
		
		if(!connection.isSuccessfulConnection()) {
			play = false;
			finished = true;
			return;
		}
		
		// Process tasks
		Packet packet = connection.getTask(Float.MAX_VALUE);
		while (packet!=null && !finished) {
			// process packet
			switch(packet.getType()) {
			case PLAYERIDENT:
				// add player
				break;
			case PLAYERDISCONNECT:
				// remove player
				break;
			case STARTGAME:
				// start game
				finish();
				break;
			default:
				// nothing
				break;
			}
			
			if(!finished) {
				// get next task
				packet = connection.getTask(Float.MAX_VALUE);
			}
		}
	}

	public boolean toPlay() {
		return play;
	}
	
	@Override
	public void cancel() {
		play = false;
		finished = true;
	}
	
	@Override
	public void finish() {
		play = true;
		finished = true;
	}
}
