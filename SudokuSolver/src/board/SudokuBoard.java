package board;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SudokuBoard{
	private int size;
	private int[][] board;
	private int regRowSize;
	private int regColSize;
	
	public SudokuBoard(int size) {
		this.setSize(size);
		this.setBoard();
		this.setRegRowSize();
		this.setRegColSize();
	}
	
	public SudokuBoard(int[][] board) {
		this.setSize(board.length);
		this.setBoard(board);
		this.setRegRowSize();
		this.setRegColSize();
	}

	public int getSize() {
		return size;
	}
	

	public int getRegRowSize() {
		return regRowSize;
	}

	public int getRegColSize() {
		return regColSize;
	}

	private void setRegRowSize() {
		List<Integer> l = new ArrayList<>();
		for(int i = 2; i * i <= size; i++) {
			if(size % i == 0) 
				l.add(i);
		}
		this.regRowSize = l.get(l.size() - 1);
	}

	private void setRegColSize() {
		this.regColSize = size / this.regRowSize;
	}

	public int[][] getBoard() {
		return board;
	}

	private void setSize(int size) {
		if(size > 24) 
			throw new IllegalArgumentException("Max size allowed 24");
		for(int i = 2; i * i <= size; i++) {
			if(size % i == 0) {
				this.size = size;
				return;
			}
		}
		throw new IllegalArgumentException("Invalid size");
	}

	private void setBoard() {
		this.board = new int[this.size][this.size];
	}
	
	private void setBoard(int[][] board) {
		this.board = board;
	}
	
	public void setBoardAt(int i, int j, int num) {
		this.board[i][j] = num;
	}
	
	/*Implementation should be revised*/
	/*public void fillSudoku() {
		int v = 0;
		for(int i = 0; i < this.size; i++){
			for(int j = 0; j < this.size; j++){
				v = (int)(Math.random() * (size + 1));
				if(isValid(i,j,v) || v == 0)
					board[i][j] = v;	
			}
		}
	}*/
	
	public boolean isValid() {
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				if(!isValid(i, j, this.board[i][j]) && this.board[i][j] != 0)
					return false;
			}
		}
		return true;
	}

	private boolean isValid(int i, int j, int v) {
		//check row and column simultaneosly
		for(int c = 0; c < board.length; c++) 	
			if((c != i && board[c][j] == v) || (c != j && board[i][c] == v))
				return false;
		int r = (i / this.regRowSize) * this.regRowSize;
		int rBound = r + this.regRowSize;
		int c = (j / this.regColSize) * this.regColSize;
		int cBound = c + this.regColSize;
		//check region
		for(int r1 = r; r1 < rBound; r1++) {
			for(int c1 = c; c1 < cBound; c1++) {
				if((r1 != i && c1 != j) && board[r1][c1] == v)
					return false;
			}
		}
		return true;
	}


	@Override
	public String toString() {
		StringBuilder board = new StringBuilder();
		String line = line();
		for(int i = 0; i < size; i++) {
			if(i != 0 && i % regRowSize == 0)
				board.append(line + "\n");
			for(int j = 0; j < size; j++)
				board.append(j != 0 && j % regColSize == 0 ? 
						"|" + this.board[i][j] + " " : this.board[i][j] + " ");
			board.append("\n");	
		}
		return board.toString();
	}

	private String line() {
		StringBuilder line = new StringBuilder(" ");
		for(int i = 1; i <= size; i++)
			line.append("- ");
		return line.toString();
	}
	
	public int[][] copy() {
		int[][] newBoard = new int[this.size][this.size];
		for(int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++)
				newBoard[i][j] = this.board[i][j];
		}
		return newBoard;
	}
}

