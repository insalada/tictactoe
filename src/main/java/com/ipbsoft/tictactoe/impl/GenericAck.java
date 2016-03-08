package com.ipbsoft.tictactoe.impl;

import java.util.UUID;

import com.ipbsoft.tictactoe.core.Ack;

/**
 * An Ack generic implementation 
 * @author Iván
 *
 */
public class GenericAck implements Ack{

	private boolean success;
	private String message;
	private UUID uuid;
	
	public GenericAck() {
		super();
	}

	public GenericAck(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	
	public GenericAck(boolean success, String message, UUID uuid) {
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
