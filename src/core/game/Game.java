package core.game;

import static core.Frame.SCREEN_SIZE;
import gui.GameGUI;
import gui.ui.HudOverlay;

import java.util.Random;
import java.util.TreeMap;

import model.Board;
import network.packet.Packet;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import sound.SoundManager;
import core.Frame;
import core.controller.PlayerController;

/**
 * The main loop for the game.  Also contains some static fields
 * that are useful everywhere.
 * @author Michael Cashmore
 *
 */
public class Game {
	
	public static final float INTERVAL_FRAMES = 180;
	public static float GAMESPEED = 1f;
	public static int randomSeed = 1;
	public static Random randomGenerator;
	
	/* parent */
	Frame frame;
	
	/* game over */
	public boolean finished = false;
	
	/* GUI */
	protected GameGUI gui;
	protected HudOverlay menuOverlay;
	long lastFPS = 0;
	int fps;
	
	/* model */
	protected Board board;
	long lastTick;

	
	/* players playerID <-> Controller */
	TreeMap<String,PlayerController> controllers;
	
	/* player inputs */
	protected TreeMap<Float,Packet> tasks;
	
	/* board clone INTERVAL_FRAMES in the past (for synchronization) */
	protected Board syncBoard;
	
	/* tasks applied to local game, not yet applied to syncBoard */
	protected TreeMap<Float,Packet> syncTasks;
	
	public Game(Frame frame) {
		this.frame = frame;		
		setup();
	}

	public void setup() {
		
		// setup model
		randomGenerator = new Random(randomSeed);
		board = new Board();
		tasks = new TreeMap<Float,Packet>();
		syncBoard = board.clone();
		syncTasks = new TreeMap<Float, Packet>();
		
		// setup controllers / GUI
		controllers = new TreeMap<String,PlayerController>();
//		switch(frame.getPlayer().getPlayerType()) {
//			case BUILDER:
//				controllers.put(frame.getPlayer().getPlayerID(), new BuilderController(frame.getPlayer().getPlayerID()));
//				GameGUI.VIEW_SIZE[0] = 120;
//				break;
//			case SOLDIER:
//				controllers.put(frame.getPlayer().getPlayerID(), new SoldierController(frame.getPlayer().getPlayerID()));
//				GameGUI.VIEW_SIZE[0] = 160;
//				break;
//		}
		
		// setup GUI / HUD
		gui = new GameGUI();
		float scale = (SCREEN_SIZE[0]/GameGUI.VIEW_SIZE[0]);
		GameGUI.VIEW_SIZE[1] = SCREEN_SIZE[1]/scale;
		GameGUI.offX = (GameGUI.VIEW_SIZE[0]/2 - Board.BOARD_SIZE[0]/2) * scale;
		GameGUI.offY = (GameGUI.VIEW_SIZE[1]/2 - Board.BOARD_SIZE[1]/2) * scale;
		menuOverlay = new HudOverlay(); // HudFactory.makeHUD(frame.getPlayer().getPlayerType());
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void addTask(Packet p) {
		tasks.put(p.getTime(), p);
	}
	
	public TreeMap<String, PlayerController> getControllers() {
		return controllers;
	}
	
	/*-----------*/
	/* Main loop */
	/*-----------*/
	 
	public void start() {

		lastFPS = getTime();
		lastTick = getTime();
		float dt = 1f;		
		
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
	}

	protected void pollInput() {
		// escape
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			finished = true;
		
		// poll controllers
		for(PlayerController controller: controllers.values()) {
			controller.pollInput(board, menuOverlay);
			for(Packet p: controller.getCommands()) {
				tasks.put(p.getTime(),p);
			}
			controller.getCommands().clear();
		}
	}

	/**
	 * Process tasks in the order they were received.  Keep a clone of the game set
	 * INTERVAL_FRAMES in the past to account for lag issues.
	 */
	protected void processTasks() {
		
		// ideal time for syncBoard
		float syncTime = board.getTime() - INTERVAL_FRAMES;
		Board newSyncBoard = null;
		float dt;
		
		// add tasks applied to Board, not applied to syncBoard
		for(Float f: syncTasks.keySet())
			addTask(syncTasks.get(f));
		syncTasks.clear();
		
		// Process tasks until the syncTime
		Packet packet = getTask(syncTime);
		while (packet!=null) {
						
			// process packet
			dt = packet.getTime() - syncBoard.getTime();
			if(dt>0) syncBoard.tick(dt);
			PacketHandler.applyPacket(syncBoard,packet);
			
			// get next task
			packet = getTask(syncTime);
		}
				
		// create new syncBoard
		dt = syncTime - syncBoard.getTime();
		if(dt>0) syncBoard.tick(dt);
		newSyncBoard = syncBoard.clone();
		
		// process remaining tasks (until current time)
		packet = getTask(Float.MAX_VALUE);
		while (packet!=null) {
			
			// process packet
			dt = packet.getTime() - syncBoard.getTime();
			if(dt>0) syncBoard.tick(dt);
			PacketHandler.applyPacket(syncBoard,packet);
			
			// add to syncTasks for next cycle
			syncTasks.put(packet.getTime(),packet);
			
			// get next task
			packet = getTask(board.getTime());
		}
		
		// increment to current time
		dt = board.getTime() - syncBoard.getTime();
		if(dt>0) syncBoard.tick(dt);
		
		// swap to new boards
		board = syncBoard;
		syncBoard = newSyncBoard;
	}
	
	/**
	 * @param threshold a time value.
	 * @return the earliest task, if it is before threshold, otherwise nothing.
	 */
	public Packet getTask(float threshold) {
		if(hasTasks()) {
			Packet p = null;
			if(tasks.firstKey()<threshold)
				p = tasks.remove(tasks.firstKey());
			return p;
		} else {
			return null;
		}
	}
	
	/**
	 * @return true if there are player inputs to process.
	 */
	private boolean hasTasks() {
		return (tasks.size()>0);
	}
	
	protected void updateFPS() {
		if (Game.getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps + " Time: " + board.getTime());
			fps = 0;
			lastFPS = Game.getTime();
		}
		fps++;
	}
	
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public static void setSeed(int seed) {
		randomSeed = seed;
	}
	
	public static int getRandomSeed() {
		return randomSeed;
	}
	
	public static int getRandomInt(int max) {
		if(randomGenerator==null)
			randomGenerator = new Random(randomSeed);
		return randomGenerator.nextInt(max);
	}
}

