package board.custom;

import board.SudokuBoard;

public class MostDifficultSixBySixBoard extends SudokuBoard{

	public MostDifficultSixBySixBoard() {
		super(6);
		init();
	}

	private void init() {
		this.setBoardAt(0, 0, 6);
		this.setBoardAt(0, 1, 2);
		this.setBoardAt(0, 3, 5);
		this.setBoardAt(0, 5, 3);
		this.setBoardAt(2, 0, 5);
		this.setBoardAt(2, 4, 3);
		this.setBoardAt(3, 1, 6);
		this.setBoardAt(3, 4, 2);
		this.setBoardAt(4, 3, 3);
		this.setBoardAt(4, 4, 4);
		this.setBoardAt(4, 5, 6);
		this.setBoardAt(5, 0, 3);
		this.setBoardAt(5, 2, 6);
		/*this.getBoard()[0][0] = 6;
		this.getBoard()[0][1] = 2;
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
	}

}
