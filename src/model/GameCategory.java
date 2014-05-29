package model;

import java.awt.Color;

public enum GameCategory {

	NEUTRAL		(new Color(150,150,150)),
	HOUSING		(new Color(0,205,0)),
	SCIENCE		(new Color(128,0,128)),
	MATERIAL	(new Color(199,97,20)),
	POWER		(new Color(0,154,205)),
	MILITARY	(new Color(150,0,0));
	
	public Color colour;

	private GameCategory(Color debug) {
		this.colour = debug;
	}
}
