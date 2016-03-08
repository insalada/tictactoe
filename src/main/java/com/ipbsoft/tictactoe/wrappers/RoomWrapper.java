package com.ipbsoft.tictactoe.wrappers;

import javax.validation.constraints.NotNull;

/**
 * Just a wrapper for the body request
 * @author insalada
 *
 */
public class RoomWrapper {
	@NotNull(message="room id is required")
	private int roomId;
	
	public RoomWrapper() {
		super();
	}
	
	public RoomWrapper(int roomId) {
		super();
		this.roomId = roomId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	
	
}
