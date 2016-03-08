package com.ipbsoft.tictactoe.core;

import javax.validation.constraints.NotNull;

/**
 * This class handles every player's move
 * @author insalada
 *
 */
public class Move {
	
	@NotNull(message="Player uuid is required")
	private String uuid;
	@NotNull(message="Position is required")
	private Position position;
	
	public Move() {
		super();
	}
	
	public Move(String uuid, Position position) {
		super();
		this.position = position;
		this.uuid = uuid;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getUuid() {
		return uuid;
	}

	public void setPlayerId(String uuid) {
		this.uuid = uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
