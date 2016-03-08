package com.ipbsoft.tictactoe.core;

import java.util.UUID;

/**
 * Interface to provide implementations for any ACK response
 * 
 * @author insalada
 *
 */
public interface Ack {
	/**
	 * Determines whether the response is success
	 * @return
	 */
	public boolean isSuccess();
	
	/**
	 * Get the message response
	 * @return
	 */
	public String getMessage();
	
	/**
	 * Get player's uuid response
	 * @return
	 */
	public UUID getUuid();
	
	/**
	 * Set whether response is success or not
	 * @param success
	 */
	public void setSuccess(boolean success);
	
	/**
	 * Set message response
	 * @param message
	 */
	public void setMessage(String message);
	
	/**
	 * Set player's uuid response
	 * @param uuid
	 */
	public void setUuid(UUID uuid);
}
