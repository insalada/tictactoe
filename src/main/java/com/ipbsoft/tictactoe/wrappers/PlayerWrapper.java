package com.ipbsoft.tictactoe.wrappers;

import javax.validation.constraints.NotNull;

/**
 * Just a wrapper for the body request
 * @author insalada
 *
 */
public class PlayerWrapper {
	@NotNull(message="uuid is required")
	private String uuid;

	public PlayerWrapper() {
		super();
	}

	public PlayerWrapper(String uuid) {
		super();
		this.uuid = uuid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
