package test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import board.SudokuBoard;
import board.custom.MostDifficultSixBySixBoard;
import static org.junit.jupiter.api.Assertions.assertTrue;
import solver.DLXSolver;
import solver.Solver;

public class DLXSolverTest{
	private static Solver dlxSolver;
	private static List<SudokuBoard> sudokuBoards;
	 
	@BeforeAll
	static void init() {
		dlxSolver = new DLXSolver();
		sudokuBoards = new ArrayList<>();
		//add any board you want
		sudokuBoards.add(new MostDifficultSixBySixBoard());
	}
	
	@Test
	void testSolver() {
		List<List<SudokuBoard>> solutions = sudokuBoards.stream()
			.map(dlxSolver::solve)
			.collect(Collectors.toList());
		
		for(List<SudokuBoard> l : solutions) {
			for(SudokuBoard sol : l)
				assertTrue(sol.isValid());
		}
	}
	
}
