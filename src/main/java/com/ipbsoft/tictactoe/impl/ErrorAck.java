package com.ipbsoft.tictactoe.impl;

import java.util.UUID;

import com.ipbsoft.tictactoe.core.Ack;

/**
 * ACK implementation for errors
 * @author Iván
 *
 */
public class ErrorAck implements Ack{
	
	private boolean success;
	private String message;
	private UUID uuid;
	
	public ErrorAck() {
		super();
	}
	
	public ErrorAck(String message) {
		super();
		this.success = false;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
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
