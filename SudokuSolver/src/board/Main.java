package board;

import java.util.List;

import solver.BacktrackingSolver;
import solver.BacktrackingSolverV2;
import solver.DLXSolver;
import solver.Solver;

public class Main {
	public static void main(String[] args) {
		SudokuBoard board = new SudokuBoard(4);
		/*board.fillSudoku();*/
		/*board.getBoard()[0][0] = 6;*/
		/*board.getBoard()[0][1] = 2;
		board.getBoard()[0][3] = 5;
		board.getBoard()[0][5] = 3;
		board.getBoard()[2][0] = 5;
		board.getBoard()[2][4] = 3;
		board.getBoard()[3][1] = 6;
		board.getBoard()[3][4] = 2;
		board.getBoard()[4][3] = 3;
		board.getBoard()[4][4] = 4;
		board.getBoard()[4][5] = 6;
		board.getBoard()[5][0] = 3;
		board.getBoard()[5][2] = 6;*/
		
		System.out.println(board);
		Solver solver = new DLXSolver();
		List<SudokuBoard> l = solver.solve(board);
		
		for(SudokuBoard s : l)
			System.out.println(s);
	}
}
