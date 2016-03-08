package com.ipbsoft.tictactoe.controllers;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ipbsoft.tictactoe.core.Ack;
import com.ipbsoft.tictactoe.core.Move;
import com.ipbsoft.tictactoe.core.Player;
import com.ipbsoft.tictactoe.core.Room;
import com.ipbsoft.tictactoe.impl.ErrorAck;
import com.ipbsoft.tictactoe.impl.GenericAck;
import com.ipbsoft.tictactoe.impl.PlayerAck;
import com.ipbsoft.tictactoe.managers.PlayerPoolManager;
import com.ipbsoft.tictactoe.managers.RoomPoolManager;
import com.ipbsoft.tictactoe.wrappers.PlayerWrapper;

/**
 * This Controller handles all requests for the game
 * 
 * | url     | method | consumes      | produces | description               |
 * |---------|--------|---------------|----------|---------------------------|
 * | /new    | POST   | JSON: name    | JSON     | Creates a new player      |
 * | /join   | POST   | JSON: uuid    | JSON     | Joins a room              |
 * | /play   | POST   | JSON: move    | JSON     | Makes a move              |
 * | /whose  | GET    | param: uuid   | JSON     | Whose turn is it?         |
 * | /status | GET    | param: roomId | JSON     | Status of the game's room |
 * 
 * 
 * @author Iván
 *
 */
@RestController
public class MainController {

	private final Logger LOGGER = LogManager.getLogger(MainController.class);
	private RoomPoolManager roomPoolManager = new RoomPoolManager();
	private PlayerPoolManager playerPoolManager = new PlayerPoolManager();

	/**
	 * Creates a new player
	 * @return player's uuid
	 */
	@RequestMapping(value="/new", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Ack newPlayer(@RequestBody Player player) {
		playerPoolManager.addPlayer(player);
		return new PlayerAck(true, "User created", player.getUuid());
	}

	/**
	 * Joins to an available room or create one if necessary
	 * 
	 * @param player
	 * @return
	 */
	@RequestMapping(value="/join", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Ack joinRoom(@RequestBody PlayerWrapper wrapper) {
		Player player = playerPoolManager.getPlayer(wrapper.getUuid());
		//Check if player already exists
		if(player==null) {
			return new GenericAck(false, "Player does not exist");
		} 
		//Check if player is already playing in a room
		if(player.getRoomId() != 0) {
			return new GenericAck(false, "You are already playing in room: " + player.getRoomId(), player.getUuid());
		}
		//Joins the player an available room
		Room room = roomPoolManager.toAvailableRoom(player);
		//return the room
		return new GenericAck(true, String.format("Joined to room %s", room.getId()), player.getUuid());
	}



	/**
	 * Handles the request for playing
	 * @param Move object into JSON
	 * @return an ACK object into JSON
	 */
	@RequestMapping(value="/play", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Ack play(@Valid @RequestBody Move move) {

		//Check whether player exists
		Player player = playerPoolManager.getPlayer(move.getUuid());
		if(player == null) {
			return new ErrorAck("Provided player's uuid does not exist");
		}

		//Check if user is in a room
		Room room = roomPoolManager.getRoom(player.getRoomId());
		if(room==null) {
			return new ErrorAck("You are not in a room. Try to join");
		}

		//Check if user is alone in the room
		if(room.isAvailable()) {
			return new ErrorAck("Wait for a rival joins your room");
		}

		//Check whether game is finished
		if(room.isFinished()) {
			return new GenericAck(true, "Game is finished");
		}


		//Check whether is the user's turn
		if(!player.isTurn()) {
			return new ErrorAck("It is not your turn. Be patient buddy");
		}

		//Check whether the move is correct
		if(move.getPosition().getRow() < 0 || 
				move.getPosition().getRow() > 2 || 
				move.getPosition().getColumn() < 0 || 
				move.getPosition().getColumn() > 2) {
			return new ErrorAck("Wrong move! values accepted between 0 and 2");
		}

		//Check whether position is already taken
		if(room.isPositionTaken(move.getPosition())) {
			return new ErrorAck("Position already taken. Please choose another move");
		}

		//Registers player's move
		room.addMove(move);

		//Switches the turn
		room.switchTurn();

		//Check whether we have a winner
		if(room.hasWinner(move)) {
			return new GenericAck(true, "Congratulations! You won");
		}
		
		//Check whether game is draw
		if(room.isDraw()) {
			return new GenericAck(true, "Draw Game!");
		}

		//Returns ack
		return new GenericAck(true, "Move OK");
	}


	/**
	 * Checks whose turn is it
	 * @param uuid into JSON format
	 * @return an ACK object into JSON
	 */
	@RequestMapping(value="/whose", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Ack whoseTurn(@RequestParam String uuid) {
		Player player = playerPoolManager.getPlayer(uuid);

		//Check if player already exists
		if(player==null) {
			return new GenericAck(false, "Player uuid does not exist");
		} 

		//Check if user is in a room
		Room room = roomPoolManager.getRoom(player.getRoomId());
		if(room==null) {
			return new GenericAck(false, "You are not yet in a room.");
		}

		//Check if user is alone in the room
		if(room.isAvailable()) {
			return new GenericAck(false, "Wait for a rival joins your room");
		}

		//Check whether is the user's turn
		if(!player.isTurn()) {
			return new GenericAck(false, "It is not your turn. Wait for your rival's move");
		}

		return new GenericAck(true, "It is your turn. Moves accepted [0-2][0-2]");
	}

	/**
	 * Returns room's status
	 * @param room id in JSON format
	 * @return matrix board's status into JSON format
	 */
	@RequestMapping(value="/status", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Ack getStatus(@RequestParam int roomId) {

		//Check if room exist
		Room room = roomPoolManager.getRoom(roomId);
		if(room==null) {
			return new GenericAck(true, "Room does not exist");
		}

		//Check availability of the room
		if(room.isAvailable()) {
			return new GenericAck(true, "Room is waiting for player(s) to start the game");
		}

		//Check if users are still playing
		if(room.getMoves().size() < 9) {
			return new GenericAck(true, "Users are currently playing in the room");
		}
		return new GenericAck(true, "Game is finished");
	}


	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Ack handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		LOGGER.error("Wrong arguments: " + e.getMessage());
		return new ErrorAck("Incorrect JSON arguments");
	}


	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Ack handleErrorNotSupported(HttpMediaTypeNotSupportedException e)   {
		LOGGER.error("Http Media-Type not supported: " + e.getMessage());
		return new ErrorAck("Wrong request");
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	public Ack handleError422(HttpMessageNotReadableException e)   {
		LOGGER.error("Message not readable: " + e.getMessage());
		return new ErrorAck("Required request body is missing");
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
	public Ack handleMethodNotSupported(HttpRequestMethodNotSupportedException e)   {
		LOGGER.error("Method not allowed: " + e.getMessage());
		return new ErrorAck("Method not allowed");
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Ack handleError400(Exception e)   {
		LOGGER.error("Bad Request: " + e.getMessage());
		return new ErrorAck("Wrong request");
	}

}