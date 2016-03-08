package com.ipbsoft.tictactoe.core;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.validation.constraints.Size;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class handles each game's flow per room
 * @author insalada
 *
 */
public class Room {
	
	private final Logger LOGGER = LogManager.getLogger(Room.class);
	private int boardSize = 3;
	private static final AtomicInteger roomCounter = new AtomicInteger();
	@Size(max=2)
	private List<Player> players = new ArrayList<>();
	private Set<Move> moves = new  LinkedHashSet<>();
	private String[][] board = new String[boardSize][boardSize];
	private boolean finished = false;
	private int id;
		
	public Room() {
		super();
		id = roomCounter.incrementAndGet();
		cleanBoard();
	}
	
	/**
	 * Creates a room and joins a player to it
	 * @param player
	 */
	public Room(Player player) {
		super();
		id = roomCounter.incrementAndGet();
		cleanBoard();
		player.setRoomId(id);
		player.setTurn(true);
		player.setFlag("O");
		players.add(player);
		LOGGER.info("Created new room: " + id + " and joins player: " + player.toString());
	}

	public boolean isAvailable() {
		return (players.size() < 2)? true : false;
	}
	
	/**
	 * Add a player to the room
	 * @param player
	 */
	public void addPlayer(Player player) {
		player.setRoomId(id);
		player.setTurn(false);
		player.setFlag("X");
		players.add(player);
		LOGGER.info("Player " + player.toString() + " joins room " + id);
	}
	
	/**
	 * Add a new move to the moves list and set the flag into the game's board
	 * @param move
	 */
	public void addMove(Move move) {
		moves.add(move);
		putFlag(move);
		LOGGER.info("Added move: " + move.getPosition().toString());
	}
	
	/**
	 * Initialize game's board
	 */
	private void cleanBoard() {
		for(int i=0; i<boardSize; i++) {
			for(int j=0; j<boardSize; j++) {
				board[i][j] = "";
			}
		}
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Set<Move> getMoves() {
		return moves;
	}

	public void setMoves(Set<Move> moves) {
		this.moves = moves;
	}

	public AtomicInteger getRoomCounter() {
		return roomCounter;
	}
	
	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}


	/**
	 * Put the player's flag into the board matrix
	 * @param move
	 */
	public void putFlag(Move move) {
		board[move.getPosition().getRow()][move.getPosition().getColumn()] = move.getUuid();
	}

	/**
	 * Returns players uuid taken for the provided position
	 * @param position
	 * @return String
	 */
	public String getFlag(Position position) {
		return board[position.getRow()][position.getColumn()];
	}
	
	/**
	 * Checks whether provided position is already taken
	 * @param position
	 * @return true if taken, false otherwise
	 */
	public boolean isPositionTaken(Position position) {
		if(board[position.getRow()][position.getColumn()] != null &&
				!board[position.getRow()][position.getColumn()].isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 * Switches the turn between players
	 */
	public void switchTurn() {
		if(players.size() == 2) {
			if(players.get(0).isTurn()) {
				players.get(0).setTurn(false);
				players.get(1).setTurn(true);
			} else {
				players.get(0).setTurn(true);
				players.get(1).setTurn(false);
			}
		}
	}
	
	
	/**
	 * Checks whether we have a winner
	 * Checks horizontal, then vertical, diagonal, and reverse diagonal
	 * @param move
	 * @return true if there is a winner, false otherwise
	 */
	public boolean hasWinner(Move move) {
		
		int horizontal = 0;
		int vertical = 0;
		int diagonal = 0;
		int reverse = 0;
		
		for(int i = 0; i < boardSize; i++) {
			//Check horizontal
    		if(board[move.getPosition().getRow()][i].equals(move.getUuid())) {
    			horizontal++;
    		}
    		
    		//Check vertical
    		if(board[i][move.getPosition().getColumn()].equals(move.getUuid())) {
    			vertical++;
    		}
    		
    		//Check diagonal
    		if(board[i][i].equals(move.getUuid())) {
        		diagonal++;
        	}
    		
    		//Check reverse diagonal
    		if(board[(boardSize-1)-i][i].equals(move.getUuid())) {
        		reverse++;
        	}
    	}
		
		if(horizontal == boardSize || 
				vertical == boardSize ||
				diagonal == boardSize ||
				reverse == boardSize) {
			setFinished(true);
			LOGGER.info("We have a winner: " + move.getUuid());
			return true;
		}
		return false;
	}

	/**
	 * Check whether the game ends in draw
	 * @return
	 */
	public boolean isDraw() {
		if(moves.size() >= 9) {
			setFinished(true);
			LOGGER.info("Game finished in Draw!");
			return true;
		}
		return false;
	}

	public String[][] getBoard() {
		return board;
	}

	public void setBoard(String[][] board) {
		this.board = board;
	}
	
	
	
}
