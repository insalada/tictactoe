package com.ipbsoft.tictactoe.core;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Stores move position
 * 
 * @author insalada
 *
 */
public class Position {
	
	
	@Min(0)
    @Max(2)
	private int row;
	
	@Min(0)
    @Max(2)
	private int column;
	
	public Position() {
		super();
	}
	
	public Position(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}
	
	

	@Override
	public String toString() {
		return "[" + row + "][" + column + "]";
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
	

}
