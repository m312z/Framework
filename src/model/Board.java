package model;

import model.map.GameMap;

/**
 * The main model.
 * @author Michael Cashmore
 */
public class Board {
	
	/* size of the board in terms of game size units */
	public static float[] BOARD_SIZE = {300,300};
	
	/* size of the board in terms of the grid */
	public static int[] GRID_SIZE = {50,(int) (BOARD_SIZE[1]*50/BOARD_SIZE[0])};
	public static float TILE_SIZE = BOARD_SIZE[0]/GRID_SIZE[0];
	
	/* epsilon distance for collision detection etc. */
	public static final float NUDGE = 0.01f;
	
	/* adjacency list for convenience */
	public static final Integer[][] ADJ_LIST = {{-1,0},{0,-1},{1,0},{0,1}};
	
	/* time-stamp for networking */
	float time;
	
	/* timers for events */
	
	/* tile model */
	GameMap map;
	
	/**
	 * Constructor for a brand new, basic game.
	 */
	public Board() {
		
		map = new GameMap(GRID_SIZE[0],GRID_SIZE[1]);
		
	}
	
	/** 
	 * Constructor with supplied map. This board may require additional setup.
	 * @param map	The map (visual only) to start with.
	 */
	public Board(GameMap map) {
		this.map = map;
	}
			
	/* COPY METHOD */
	public Board clone() {
		
		Board board = new Board(map.clone());
		board.setTime(time);		
		return board;
	}
	
	/*-------------*/
	/* main update */
	/*-------------*/
	
	/**
	 * @return false if the game is to continue.
	 */
	public boolean tick(float dt) {

		// check timers

		time += dt;
		
		return false;
	}
	
	/*---------------------*/
	/* setters and getters */
	/*---------------------*/
		
	/* map and model */
	
	public float getTime() {
		return time;
	}
	
	public void setTime(float time) {
		this.time = time;
	}

	public GameMap getMap() {
		return map;
	}
}
