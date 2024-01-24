package solver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import board.SudokuBoard;

public class DLXSolver implements Solver{
	private List<List<DataObject>> solutions;
	
	public DLXSolver() {
		this.solutions = new ArrayList<>();
	}

	@Override
	public List<SudokuBoard> solve(SudokuBoard board) {
		this.solutions = new ArrayList<>();
		ColumnObject h = DLXSolver.buildSparseMatrix(board);
		this.solve(h, 0, new ArrayList<>());
		List<SudokuBoard> solutions = getSolutions(board);
		return solutions;
	}
	
	private List<SudokuBoard> getSolutions(SudokuBoard board) {
		List<SudokuBoard> s = new ArrayList<>();
		for(List<DataObject> sol : solutions) {
			int[][] b = new int[board.getSize()][board.getSize()];
			for(DataObject d : sol) 
				b[d.label.row][d.label.column] = d.label.num;
			s.add(new SudokuBoard(b));
		}
		return s;
	}

	private void solve(ColumnObject h, int k, List<DataObject> o) {
		if (h.right == h) {
			solutions.add(new ArrayList<>(o));
			return;
		}
		ColumnObject c = getMinColumn(h);
		cover(h, c);
		DataObject r = c.down;
		while (r != c) {
			grow(o, k+1);
			o.set(k, r);
			DataObject j = r.right;
			while (j != r) {
				cover(h, (ColumnObject)j.column);
				j = j.right;
			} //from 73 to 87 it's a FULL cover
			solve(h, k+1, o);
			j = r.left;
			while (j != r) {
				uncover(h, (ColumnObject)j.column);
				j = j.left;
			}
			r = r.down;
		}
		uncover(h, c);
		return;
	}
	
	private void grow(List<DataObject> o,int k) {
		if (o.size() < k) {
			while (o.size() < k) {
				o.add(null);
			}
		} else if (o.size() > k) {
			while (o.size() > k) {
				o.remove(o.size() - 1);
			}
		}
	}
	

	private static ColumnObject buildSparseMatrix(SudokuBoard board) {
		ColumnObject h = new ColumnObject();
		h.left = h; h.right = h; h.up = h; h.down = h;
		h.label.num = -1;
		h.label.column = -1;
		h.label.row = -1;
		Map<Integer, ColumnObject> constraintsMap = new HashMap<>();
		for (int j = 0; j < 4 * board.getSize() * board.getSize(); j++) {
			final ColumnObject newColHdr = new ColumnObject();
			newColHdr.label.num = -1;
			newColHdr.label.column = j + 1;
			newColHdr.label.row = -1;
			newColHdr.up = newColHdr;
			newColHdr.down = newColHdr;
			newColHdr.right = h;
			newColHdr.left = h.left;
			h.left.right = newColHdr;
			h.left = newColHdr;
			constraintsMap.put(newColHdr.label.column, newColHdr);
		}
		
		List<DataObject> thisRowObjects = new LinkedList<DataObject>();
		int size = board.getSize();
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				int kStart = board.getBoard()[i][j] == 0 ? 1 : board.getBoard()[i][j];
				int kEnd = board.getBoard()[i][j] == 0 ? size : board.getBoard()[i][j];
				for(int k = kStart; k <= kEnd; k++) {
					int cellConstraint = i * size + j + 1;
					int rowConstraint = (size * size) + (i * size) + k;
					int columnConstraint = (2 * size * size) + (j * size) + k;
					int regionConstraint = (3 * size * size) + 
							(((size / board.getRegColSize()) * (i / board.getRegRowSize())) 
									+ (j / board.getRegColSize())) * size + k;
					addDataNodeToColumn(constraintsMap.get(cellConstraint), i, j, k, thisRowObjects);
					addDataNodeToColumn(constraintsMap.get(rowConstraint), i, j, k, thisRowObjects);
					addDataNodeToColumn(constraintsMap.get(columnConstraint), i, j, k, thisRowObjects);
					addDataNodeToColumn(constraintsMap.get(regionConstraint), i, j, k, thisRowObjects);
					linkDataObjectsHorizontally(thisRowObjects);
					thisRowObjects.clear();
				}	
			}
		}
		return h;
	}


	private static void linkDataObjectsHorizontally(List<DataObject> thisRowObjects) {
		if (thisRowObjects.size() > 0) {
			final Iterator<DataObject> iter = thisRowObjects.iterator();
			final DataObject first = iter.next();
			while (iter.hasNext()) {
				final DataObject thisObj = iter.next();
				thisObj.left = first.left;
				thisObj.right = first;
				first.left.right = thisObj;
				first.left = thisObj;
			}
		}
	}
	
	static final void cover(ColumnObject h, ColumnObject columnObj) {
		/*the constraint is satisfied by a choice and therefore we can unlink 
		 * unlink the constraint
		 */
		columnObj.right.left = columnObj.left; 
		columnObj.left.right = columnObj.right;
		//get the first no
		DataObject i = columnObj.down;
		while (i != columnObj) {
			DataObject j = i.right;
			while (j != i) {
				j.down.up = j.up;
				j.up.down = j.down;
				((ColumnObject)j.column).size--;
				j = j.right;
			}

			i = i.down;
		}
	}
	
	static final void uncover(ColumnObject h, ColumnObject columnObj) {
		DataObject i = columnObj.up;
		while (i != columnObj) {
			DataObject j = i.left;
			while (j != i) {
				((ColumnObject)j.column).size++;
				j.down.up = j;
				j.up.down = j;
				j = j.left;
			}

			i = i.up;
		}
		columnObj.right.left = columnObj;
		columnObj.left.right = columnObj;
	}
	
	private static ColumnObject getMinColumn(ColumnObject h) {
		ColumnObject c = (ColumnObject)h.right;
		int s = Integer.MAX_VALUE;
		ColumnObject j = ((ColumnObject)h.right);
		while (j != h) {
			if (j.size < s) {
				s = j.size;
				c = j;
			}
			j = (ColumnObject)j.right;
		}
		return c;
	}


	private static void addDataNodeToColumn(ColumnObject columnObject, int i, int j, int num, List<DataObject> thisRowObjects) {
		DataObject obj = null;
		obj = new DataObject();
		obj.label.num = num;
		obj.label.row = i;
		obj.label.column = j;
		obj.up = columnObject.up;
		obj.down = columnObject;
		obj.left = obj;
		obj.right = obj;
		obj.column = columnObject;
		columnObject.up.down = obj;
		columnObject.up = obj;
		columnObject.size++;
		thisRowObjects.add(obj);
	}


	static class DataObject {
		public DataObject left, right, up, down, column;
		public Label label = new Label();
		@Override
		public String toString() {
			return label.toString();
		}
	}
	
	static class ColumnObject extends DataObject{
		public int size = 0;
	}
	
	static class Label {
		public int row;
		public int column;
		public int num;
		
		@Override
		public String toString() {
			return String.format("(num: %d row: %d col: %d)", num, row, column);
		}
	}

}

