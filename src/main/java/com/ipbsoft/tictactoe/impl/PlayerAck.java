package com.ipbsoft.tictactoe.impl;

import java.util.UUID;

import com.ipbsoft.tictactoe.core.Ack;

/**
 * An Ack implementation for new player response
 * @author Iván
 *
 */
public class PlayerAck implements Ack{

	private boolean success;
	private String message;
	private UUID uuid;
	
	public PlayerAck() {
		super();
	}

	public PlayerAck(boolean success, UUID uuid) {
		super();
		this.success = success;
		this.uuid = uuid;
	}
	
	public PlayerAck(boolean success, String message, UUID uuid) {
		super();
		this.success = success;
		this.message = message;
		this.uuid = uuid;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public UUID getUuid() {
		return uuid;
	}

	@Override
	public void setUuid(UUID uuid) {	
		this.uuid = uuid;
	}

}
