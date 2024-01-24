package solver;

import java.util.ArrayList;
import java.util.List;

import board.SudokuBoard;

public class BacktrackingSolver implements Solver{
	private int branches;
	private List<SudokuBoard> solutions;
	private List<SolutionThread> activeSolutionThreads;
	
	public BacktrackingSolver(int branches) {
		this.setBranches(branches);
		this.solutions = new ArrayList<>();
		this.activeSolutionThreads = new ArrayList<>();
	}
	
	private void setBranches(int branches) {
		if(branches < 0 || branches > 10)
			throw new IllegalArgumentException("Invalid number of branches");
		this.branches = branches;
	}
	
	public synchronized boolean addBranch() {
		if(this.branches > 0) {
			this.branches--;
			return true;
		}
		return false;
	}
	@Override
	public List<SudokuBoard> solve(SudokuBoard board){
		SolutionThread solutionThread = new SolutionThread(board, this, 0, 0, 1);
		solutionThread.start();
		try {
			solutionThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(!this.activeSolutionThreads.isEmpty())
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return this.solutions;
	}
	
	public void addSolution(SudokuBoard board) {
		this.solutions.add(board);
	}
	
	public synchronized void addSolutionThread(SolutionThread thread) {
		this.activeSolutionThreads.add(thread);
	}
	
	public synchronized void removeSolutionThread(SolutionThread thread) {
		this.activeSolutionThreads.remove(thread);
	}
}

 class SolutionThread extends Thread{
	private SudokuBoard board;
	private BacktrackingSolver solver;
	private int i;
	private int j;
	private int count;
	
	public SolutionThread(SudokuBoard board, BacktrackingSolver solver, int i, int j, int count) {
		this.board = board;
		this.solver = solver;
		this.i = i;
		this.j = j;
		this.count = count;
	}
	@Override
	public void run() {
		this.solve(i, j, board, count);
		this.solver.removeSolutionThread(this);
	}
	
	public boolean solve(int i, int j, SudokuBoard board, int count){
		if(i == -1) {
			this.solver.addSolution(board);
			return true;
		}
		int nextJ = evalNextJ(j, count, board.getSize());
		int nextI = count % board.getSize() == 0 ? evalNextI(i, count, board.getSize()) : i;
		if(board.getBoard()[i][j] == 0) {
			List<Integer> availableNums = getAvailableNums(i, j, board);
			if(availableNums.isEmpty())
				return false;
			for(int num : availableNums) {
				if(solver.addBranch()) {
					SudokuBoard newBoard = new SudokuBoard(board.copy());
					newBoard.getBoard()[i][j] = num;
					SolutionThread s = new SolutionThread(newBoard, solver, nextI, nextJ, count + 1);
					this.solver.addSolutionThread(s);
					s.start();
					continue;
				}
				board.getBoard()[i][j] = num;
				boolean isValid = solve(nextI, nextJ, board, count + 1);
				if(isValid)
					return true;
				else
					board.getBoard()[i][j] = 0;
			}
		}
		else 
			return solve(nextI, nextJ, board, count + 1);
		return false;
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
