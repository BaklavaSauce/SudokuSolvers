package solver;

import java.util.ArrayList;
import java.util.List;

import board.SudokuBoard;

public class BacktrackingSolverV2 implements Solver{
	private List<SudokuBoard> solutions = new ArrayList<>();
	
	@Override
	public List<SudokuBoard> solve(SudokuBoard board) {
		this.solve(0, 0, board, 1);
		return solutions;
	}
	
	private void solve(int i, int j, SudokuBoard board, int count){
		if(i == -1) {
			this.solutions.add(new SudokuBoard(board.copy()));
			return;
		}
		int nextJ = evalNextJ(j, count, board.getSize());
		int nextI = count % board.getSize() == 0 ? evalNextI(i, count, board.getSize()) : i;
		if(board.getBoard()[i][j] == 0) {
			List<Integer> availableNums = getAvailableNums(i, j, board);
			if(availableNums.isEmpty())
				return;
			for(int num : availableNums) {
				board.getBoard()[i][j] = num;
				solve(nextI, nextJ, board, count + 1);
				board.getBoard()[i][j] = 0;
			}
		}
		else 
			solve(nextI, nextJ, board, count + 1);
		return;
	}
	
	private int evalNextI(int i, int count, int size) {
		if(count == size * size)
			return -1;
		return i + 1;
	}

	private List<Integer> getAvailableNums(int i, int j, SudokuBoard board) {
		boolean[] nums = new boolean[board.getSize() + 1];
		int[][] b = board.getBoard();
		for(int c = 0; c < b.length; c++) {
			nums[b[i][c]] = true;
			nums[b[c][j]] = true;
		}
		int r = (i / board.getRegRowSize()) * board.getRegRowSize();
		int rBound = r + board.getRegRowSize();
		int c = (j / board.getRegColSize()) * board.getRegColSize();
		int cBound = c + board.getRegColSize();
		for(int i1 = r; i1 < rBound; i1++) {
			for(int j1 = c; j1 < cBound; j1++) 	
				nums[b[i1][j1]] = true;
		}
		List<Integer> availableNums = new ArrayList<>();
		for(int k = 1; k < nums.length; k++) {
			if(!nums[k])
				availableNums.add(k);
		}
		return availableNums;
	}

	private int evalNextJ(int j, int count, int size) {
		if(count == size * size || j > 0 && j % (size - 1) == 0 )
			return 0;
		return j + 1;
	}

}
