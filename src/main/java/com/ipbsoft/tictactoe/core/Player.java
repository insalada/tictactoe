package com.ipbsoft.tictactoe.core;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.context.annotation.Scope;

/**
 * Stores players info
 * 
 * @author insalada
 *
 */
@Scope("prototype")
public class Player {

	private UUID uuid;
	@NotNull(message="name cannot be empty")
	private String name;
	private int roomId;
	private boolean turn;
	private String flag;
	
	public Player() {
		super();
		this.uuid = UUID.randomUUID();
	}
	
	public Player(String name) {
		super();
		this.uuid = UUID.randomUUID();
		this.name = name;
	}
			
	@Override
	public String toString() {
		return "[" + uuid + ", " + name + "]";
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public boolean isTurn() {
		return turn;
	}

	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}