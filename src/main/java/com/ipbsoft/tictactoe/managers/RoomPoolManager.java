package com.ipbsoft.tictactoe.managers;

import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ipbsoft.tictactoe.core.Player;
import com.ipbsoft.tictactoe.core.Room;

/**
 * Manages rooms pool
 * 
 * @author Iván
 *
 */
public class RoomPoolManager {

	private final Logger LOGGER = LogManager.getLogger(RoomPoolManager.class);
	private final SortedMap<Integer, Room> rooms = new TreeMap<Integer, Room>();

	/**
	 * Returns the room by id
	 * @param uuid
	 * @return Room
	 */
	public synchronized Room getRoom(int id) {
		return rooms.get(id);
	}

	/**
	 * Add a room to the system
	 * @param player
	 */
	public synchronized void addRoom(Room room) {
		rooms.put(room.getId(), room);
		LOGGER.info("Added to the system room: " + room.getId());
	}
	
	/**
	 * Finds the first available room
	 * @return
	 */
	public synchronized Room findAvailable() throws NoSuchElementException{
		Predicate<Room> predicate = e -> e.isAvailable();
		Room r = rooms.values().stream().filter(predicate).findFirst().get();
		return r;
	}

	/**
	 * Joins the player to an available room or creates a new one if necessary
	 * Performs all operations atomically
	 * @return Room
	 */
	public synchronized Room toAvailableRoom(Player player) {
		Room room = null;
		try {
			room = findAvailable();
			room.addPlayer(player);
		}catch(NoSuchElementException nse) {
			room = new Room(player);
			rooms.put(room.getId(), room);
		}
		return room;
	}
}
