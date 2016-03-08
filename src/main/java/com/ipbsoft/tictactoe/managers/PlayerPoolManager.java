package com.ipbsoft.tictactoe.managers;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ipbsoft.tictactoe.core.Player;

/**
 * Manages players pool
 * 
 * @author Iván
 *
 */
public class PlayerPoolManager {
	
	private final Logger LOGGER = LogManager.getLogger(PlayerPoolManager.class);
	private final Map<String, Player> pool = new HashMap<>();

	/**
	 * 
	 * @param uuid
	 * @return
	 */
	public Player getPlayer(String uuid) {
		return pool.get(uuid);
	}
	
	/**
	 * 
	 * @param player
	 */
	public void addPlayer(Player player) {
		pool.put(player.getUuid().toString(), player);
		LOGGER.info("Added to the system player " + player.toString());
	}


}
