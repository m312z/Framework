package model.map;


public class GameMap {
	
	int[] size;
	TileType[][] map;
	
	public GameMap(int x, int y) {
		size = new int[] {x,y};
		map = new TileType[x][y];		
		for(int i=0;i<x;i++)
		for(int j=0;j<y;j++)
			map[i][j] = TileType.GROUND;
	}
	
	/**
	 * Clone constructor
	 * @param gameMap	the GameMap to clone
	 */
	public GameMap(GameMap gameMap) {
		size = new int[] {gameMap.size[0],gameMap.size[1]};
		map = new TileType[size[0]][size[1]];
		for(int i=0;i<size[0];i++)
		for(int j=0;j<size[1];j++)
			map[i][j] = gameMap.getTile(i, j);
	}

	public GameMap clone() {
		return new GameMap(this);
	}
	
	/*---------------------*/
	/* setters and getters */
	/*---------------------*/
	
	public int[] getSize() {
		return size;
	}
	
	public TileType getTile(int x, int y) {
		return map[x][y];
	}
	
	public void setTile(int x, int y, TileType tile) {
		map[x][y] = tile;
	}
	

}
