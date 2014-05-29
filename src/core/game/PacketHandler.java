package core.game;

import model.Board;
import network.packet.Packet;
import network.packet.PlayerInputPacket;

public class PacketHandler {

	/**
	 * Apply a user generated event to the game.  Could be from remote player.
	 * Generally these are game actions; the validity of which is not checked here, 
	 * but by the model. 
	 * @param board		the model
	 * @param packet	the action/event to apply
	 */
	public static void applyPacket(Board board, Packet packet) {
		switch (packet.getType()) {
		case PLAYERINPUT:
			String[] command = ((PlayerInputPacket)packet).getCommand().split("_");
			if(command[0].equals("B")) {
			}
			break;
		}
	}
}