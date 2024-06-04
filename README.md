App to create and solve sudoku puzzles with different sizes. You can choose to randomly generate a sudoku or simply test the different solving algorithms on predifined puzzles.
The predefined puzzles are stored in the board.custom package.
I've given 3 different solving algorithms:
- Brute force approach implemented with a backtracking algorithm
- Same approach but every time a new path is discovered a different thread is created to explore the path
- Using Algorithm X to convert the puzzle in an exact cover problem: https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X
The 2nd and the 3rd approach will generate more than one solution (if available).
