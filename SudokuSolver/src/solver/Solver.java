package solver;
import java.util.List;

import board.SudokuBoard;

public interface Solver {
	public List<SudokuBoard> solve(SudokuBoard board);
}
