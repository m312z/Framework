
package core.game;

import network.Client;
import network.packet.Packet;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import sound.SoundManager;
import core.Frame;
import core.controller.PlayerController;

public class ClientGame extends Game {

	/* network connection */
	private Client clientConnection;
	
	/**
	 * Default Constructor.
	 * @param frame
	 */
	public ClientGame(Frame frame) {
		super(frame);
	}
	
	/**
	 * Set's up client game.  Uses <code>Client</code> to attempt a connection
	 * and retrieves game details from the server.
	 * @param password Server password, possibly an empty String.
	 * @param ip Host's IP address as a String.
	 * @return	true if a game was successfully joined.
	 */
	public boolean setup(String password, String ip) {
		clientConnection = new Client(password,ip,frame.getPlayer());
		boolean success = clientConnection.isSuccessfulConnection();
		return success;
	}
	
	public void finish() {
		finished = true;
		clientConnection.disconnect();
	}
	
	public Client getClientConnection() {
		return clientConnection;
	}
	
	@Override
	public void start() {
			
		lastFPS = getTime();
		lastTick = getTime();
		float dt = 1f;			
		
		board.setTime(clientConnection.getStartTime());
		syncBoard.setTime(clientConnection.getStartTime());
		
		while(!finished) {

			// update FPS info
			dt = (getTime() - lastTick) / GAMESPEED;
			lastTick = getTime();
			
			// get input
			pollInput();
			processTasks();

			// update model
			finished = ( finished || board.tick(dt) );
			
			// draw
			gui.draw(board, frame.getPlayer(), dt);
			menuOverlay.draw();
			updateFPS();
			
			// OpenGL update
			Display.update();
			Display.sync(60);
						
			// queue buffers
			SoundManager.update();
			
			if(Display.isCloseRequested())
				finished = true;
			
			if(!clientConnection.isSuccessfulConnection())
				finished = true;
		}
		clientConnection.disconnect();
	}
	
	protected void pollInput() {
		
		// escape
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			finished = true;
		
		// poll (local) controllers
		for(PlayerController controller: controllers.values()) {
			controller.pollInput(board, menuOverlay);
			for(Packet p: controller.getCommands()) {
				tasks.put(p.getTime(),p);
				// send task to server
				clientConnection.sendMessage(p);
			}
			controller.getCommands().clear();
		}
		
		// poll connection for tasks
		while(clientConnection.hasTasks()) {
			Packet task = clientConnection.getTask(Float.MAX_VALUE);
			tasks.put(task.getTime(), task);
		}
	}
}
