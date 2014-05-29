package core.game;

import gui.ServerFrame;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import network.Server;
import network.packet.Packet;
import network.packet.StartGamePacket;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import sound.SoundManager;
import core.Frame;
import core.controller.PlayerController;


public class HostGame extends Game implements WindowListener {

	/* game info */
	private String gameName = "World";
	
	/* debug info for the host */ 
	private ServerFrame serverFrame;
	private boolean displayServerFrame = true;
	
	/* network connection */
	private Server server;
	
	public HostGame(Frame frame) {
		super(frame);
		// set up server
		reset();
	}
	
	/*--------------*/
	/* setup server */
	/*--------------*/

	private void reset() {
		
		// display debug info pane
		if(displayServerFrame) {
			serverFrame = new ServerFrame(gameName);
			serverFrame.addWindowListener(this);
			serverFrame.setVisible(true);
		}
		
		// create connection
		server = new Server(gameName,serverFrame);
		
		// create game controller
//		serverController = new GameServerController(this, server, board);
		
		if(displayServerFrame)
			serverFrame.output("GameServer started: "+gameName);
	}

	public void finish() {
		server.finish();
		server.disconnectAll();
		
		if(displayServerFrame) {
			serverFrame.setVisible(false);
			serverFrame.dispose();
		}
	}
	
	public Server getServer() {
		return server;
	}
	
	/*-----------*/
	/* main loop */
	/*-----------*/

	@Override
	public void start() {
		
		lastFPS = getTime();
		lastTick = getTime();
		float dt = 1f;		
		
		server.sendMessageToAll(new StartGamePacket());
		
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
		}
		server.finish();
		server.disconnectAll();
		
		if(displayServerFrame) {
			serverFrame.setVisible(false);
			serverFrame.dispose();
		}
	}
		
	protected void pollInput() {
		
		// escape
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			finished = true;
				
		// poll (local) controllers
		for(PlayerController controller: controllers.values()) {
			controller.pollInput(board, menuOverlay);
			// create input packet
			for(Packet p: controller.getCommands()) {
				tasks.put(p.getTime(),p);
				// send task to all (other) remote players
				server.sendMessageToAll(p);
			}
			controller.getCommands().clear();
		}
		
		// poll server connection
		while(server.hasTasks()) {
			Packet task = server.getTask(Float.MAX_VALUE);
			tasks.put(task.getTime(), task);
		}
	}
	
	/*-------------------------*/
	/* debug info pane methods */
	/*-------------------------*/

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		finished = true;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		finished = true;
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {	
	}

}
