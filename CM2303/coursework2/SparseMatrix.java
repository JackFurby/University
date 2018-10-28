/* c1619450 Jack Furby */

import java.util.*;
import java.io.*;

// A class that represents a dense vector and allows you to read/write its elements
class DenseVector {
	private int[] elements;

	public DenseVector(int n) {
		elements = new int[n];
	}

	public DenseVector(String filename) {
		File file = new File(filename);
		ArrayList<Integer> values = new ArrayList<Integer>();

		try {
			Scanner sc = new Scanner(file);

			while(sc.hasNextInt()) {
				values.add(sc.nextInt());
			}

			sc.close();

			elements = new int[ values.size() ];
			for(int i = 0; i < values.size(); ++ i) {
				elements[i] = values.get(i);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Read an element of the vector
	public int getElement(int idx) {
		return elements[idx];
	}

	// Modify an element of the vector
	public void setElement(int idx, int value) {
		elements[idx] = value;
	}

	// Return the number of elements
	public int size() {
		return (elements == null) ? 0 : (elements.length);
	}

	// Print all the elements
	public void print() {
		if(elements == null) {
			return;
		}

		for(int i = 0; i < elements.length; ++ i) {
			System.out.println(elements[i]);
		}
	}
}


// A class that represents a sparse matrix
public class SparseMatrix {

	// Auxiliary function that prints out the command syntax
	public static void printCommandError() {
		System.err.println("ERROR: use one of the following commands");
		System.err.println(" - Read a matrix and print information: java SparseMatrix -i <MatrixFile>");
		System.err.println(" - Read a matrix and print elements: java SparseMatrix -r <MatrixFile>");
		System.err.println(" - Transpose a matrix: java SparseMatrix -t <MatrixFile>");
		System.err.println(" - Add two matrices: java SparseMatrix -a <MatrixFile1> <MatrixFile2>");
		System.err.println(" - Matrix-vector multiplication: java SparseMatrix -v <MatrixFile> <VectorFile>");
	}


	public static void main(String [] args) throws Exception {
		if (args.length < 2) {
			printCommandError();
			System.exit(-1);
		}

		if(args[0].equals("-i")) {
			if(args.length != 2) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			System.out.println("Read matrix from " + args[1]);
			System.out.println("The matrix has " + mat.numRows() + " rows and " + mat.numColumns() + " columns");
			System.out.println("It has " + mat.numNonZeros() + " non-zeros");
		} else if(args[0].equals("-r")) {
			if(args.length != 2) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			System.out.println("Read matrix from " + args[1] + ":");
			mat.print();
		} else if(args[0].equals("-t")) {
			if(args.length != 2) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			System.out.println("Read matrix from " + args[1]);
			SparseMatrix transpose_mat = mat.transpose();
			System.out.println();
			System.out.println("Matrix elements:");
			mat.print();
			System.out.println();
			System.out.println("Transposed matrix elements:");
			transpose_mat.print();
		} else if(args[0].equals("-a")) {
			if(args.length != 3) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat1 = new SparseMatrix();
			mat1.loadEntries(args[1]);
			System.out.println("Read matrix 1 from " + args[1]);
			System.out.println("Matrix elements:");
			mat1.print();

			System.out.println();
			SparseMatrix mat2 = new SparseMatrix();
			mat2.loadEntries(args[2]);
			System.out.println("Read matrix 2 from " + args[2]);
			System.out.println("Matrix elements:");
			mat2.print();
			SparseMatrix mat_sum1 = mat1.add(mat2);

			System.out.println();
			mat1.multiplyBy(2);
			SparseMatrix mat_sum2 = mat1.add(mat2);

			mat1.multiplyBy(5);
			SparseMatrix mat_sum3 = mat1.add(mat2);

			System.out.println("Matrix1 + Matrix2 =");
			mat_sum1.print();
			System.out.println();

			System.out.println("Matrix1 * 2 + Matrix2 =");
			mat_sum2.print();
			System.out.println();

			System.out.println("Matrix1 * 10 + Matrix2 =");
			mat_sum3.print();
		} else if(args[0].equals("-v")) {
			if(args.length != 3) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			DenseVector vec = new DenseVector(args[2]);
			DenseVector mv = mat.multiply(vec);

			System.out.println("Read matrix from " + args[1] + ":");
			mat.print();
			System.out.println();

			System.out.println("Read vector from " + args[2] + ":");
			vec.print();
			System.out.println();

			System.out.println("Matrix-vector multiplication:");
			mv.print();
		}
	}


	// Loading matrix entries from a text file
	// You need to complete this implementation
	public void loadEntries(String filename) {
		File file = new File(filename);

		try {
			Scanner sc = new Scanner(file);
			int numRows = sc.nextInt();
			numCols = sc.nextInt();
			entries = new ArrayList< ArrayList<Entry> >();

			for(int i = 0; i < numRows; ++ i) {
				entries.add(null);
			}

			while(sc.hasNextInt()) {
				// Read the row index, column index, and value of an element
				int row = sc.nextInt();
				int col = sc.nextInt();
				int val = sc.nextInt();

				if (entries.get(row) == null) {
					entries.set(row, new ArrayList<Entry>());
				}

				// adds and sorts input matrix
				Entry currentEntry = new Entry(col, val);
				entries.get(row).add(currentEntry);
				Collections.sort(entries.get(row));
			}

		} catch (Exception e) {
			e.printStackTrace();
			numCols = 0;
			entries = null;
		}
	}

	// Default constructor
	public SparseMatrix() {
		numCols = 0;
		entries = null;
	}


	// A class representing a pair of column index and elements
	private class Entry implements Comparable<Entry> {

		@Override
		public int compareTo(Entry a) {
			return this.getColumn() - a.getColumn(); //Sorts the objects in ascending order
		}

		private int column;	// Column index
		private int value;	// Element value

		// Constructor using the column index and the element value
		public Entry(int col, int val) {
			this.column = col;
			this.value = val;
		}

		// Copy constructor
		public Entry(Entry entry) {
			this(entry.column, entry.value);
		}

		// Read column index
		int getColumn() {
			return column;
		}

		// Set column index
		void setColumn(int col) {
			this.column = col;
		}

		// Read element value
		int getValue() {
			return value;
		}

		// Set element value
		void setValue(int val) {
			this.value = val;
		}
	}

	// Adding two matrices
	public SparseMatrix add(SparseMatrix M) {
		// Add your code here

		SparseMatrix addMatrix = new SparseMatrix();
		addMatrix.numCols = M.numColumns();
		addMatrix.entries = new ArrayList< ArrayList<Entry> >();

		for (int i = 0; i < M.numRows(); i++) {
			addMatrix.entries.add(null);
		}

		for (int i = 0; i < M.numRows(); i++) { // if row in M is null, row <- row from this
			if (M.entries.get(i) == null) {
				if (addMatrix.entries.get(i) == null) {
					addMatrix.entries.set(i, new ArrayList<Entry>());
				}
				for (int j = 0; j < this.entries.get(i).size(); j++) {
					addMatrix.entries.get(i).add(this.entries.get(i).get(j));
				}
			} else if (this.entries.get(i) == null) { // if row in this is null, row <- row from M
				if (addMatrix.entries.get(i) == null) {
					addMatrix.entries.set(i, new ArrayList<Entry>());
				}
				for (int j = 0; j < M.entries.get(i).size(); j++) {
					addMatrix.entries.get(i).add(M.entries.get(i).get(j));
				}
			} else {
				boolean rowContinue = true;
				int mMatrixIndex = 0;
				int mMatrixSize = M.entries.get(i).size();
				int thisMatrixIndex = 0;
				int thisMatrixSize = this.entries.get(i).size();
				while (rowContinue) { // runs until the end of a row has been reached
					if (mMatrixIndex == mMatrixSize) { // if at the end of M elements in current row
						for (int j = thisMatrixIndex; j < this.entries.get(i).size(); j++) {
							addMatrix.entries.get(i).add(this.entries.get(i).get(j));
						}
						rowContinue = false;
					} else if (thisMatrixIndex == thisMatrixSize) { // if at the end of this elements in current row
						for (int j = mMatrixIndex; j < M.entries.get(i).size(); j++) {
							addMatrix.entries.get(i).add(M.entries.get(i).get(j));
						}
						rowContinue = false;
					} else { // still elements to compare
						int mMatrixCol = M.entries.get(i).get(mMatrixIndex).getColumn();
						int thisMatrixCol = this.entries.get(i).get(thisMatrixIndex).getColumn();
						if (mMatrixCol == thisMatrixCol) { // columns match, add values and add to addMatrix
							if (addMatrix.entries.get(i) == null) {
								addMatrix.entries.set(i, new ArrayList<Entry>());
							}
							int addMatrixVal = M.entries.get(i).get(mMatrixIndex).getValue();
							int thisMatrixVal = this.entries.get(i).get(thisMatrixIndex).getValue();
							if (addMatrixVal + thisMatrixVal != 0) { // if value == 0 then ignore
								Entry currentEntry = new Entry(mMatrixCol, addMatrixVal + thisMatrixVal);
								addMatrix.entries.get(i).add(currentEntry);
							}
							mMatrixIndex++;
							thisMatrixIndex++;
						} else if (mMatrixCol < thisMatrixCol) { // M element added to addMatrix
							if (addMatrix.entries.get(i) == null) {
								addMatrix.entries.set(i, new ArrayList<Entry>());
							}
							addMatrix.entries.get(i).add(M.entries.get(i).get(mMatrixIndex));
							mMatrixIndex++;
						} else { // this element added to addMatrix
							if (addMatrix.entries.get(i) == null) {
								addMatrix.entries.set(i, new ArrayList<Entry>());
							}
							addMatrix.entries.get(i).add(this.entries.get(i).get(thisMatrixIndex));
							thisMatrixIndex++;
						}
					}
				}
			}
		}
		return addMatrix;
	}

	// Transposing a matrix
	public SparseMatrix transpose() {
		// Add your code here
		SparseMatrix tranMatrix = new SparseMatrix();

		tranMatrix.numCols = this.numRows();
		tranMatrix.entries = new ArrayList< ArrayList<Entry> >();

		for(int i = 0; i < this.numColumns(); ++ i) {
			tranMatrix.entries.add(null);
		}

		for(int i = 0; i < this.numRows(); ++ i) {
			ArrayList<Entry> currentRow = this.entries.get(i);
			if (currentRow != null) {
				for(int j = 0; j < currentRow.size(); ++ j) { // adds each element to new matrix with column as row and row as column
					int newRow = currentRow.get(j).getColumn();
					if (tranMatrix.entries.get(newRow) == null) {
						tranMatrix.entries.set(newRow, new ArrayList<Entry>());
					}

					Entry item = this.entries.get(i).get(j);
					Entry newEntry = new Entry(i, item.getValue());
					tranMatrix.entries.get(newRow).add(newEntry);
				}
			}
		}
		return tranMatrix;
	}

	// Matrix-vector multiplication
	public DenseVector multiply(DenseVector v) {
		// Add your code here

		DenseVector result = new DenseVector(this.numRows());

		for (int i = 0; i < this.numRows(); i++) {
			if (this.entries.get(i) != null) {
				int total = 0; // running total for each result
				for(int j = 0; j < this.entries.get(i).size(); j++) {
					total += this.entries.get(i).get(j).getValue() * v.getElement(this.entries.get(i).get(j).getColumn());
				}
				result.setElement(i, total);
			}
		}

		return result;
	}

	// Count the number of non-zeros
	public int numNonZeros() {
		// Add your code here
		long rows = this.numRows();
		long columns = this.numColumns();
		long totalElements = rows * columns;
		int valueTotal = 0;
		for(int i = 0; i < rows; ++ i) {
			if (entries.get(i) != null) {
				valueTotal += entries.get(i).size();
			}
		}
		return valueTotal;
	}

	// Multiply the matrix by a scalar, and update the matrix elements
	public void multiplyBy(int scalar) {
		for(int i = 0; i < this.numRows(); i++) {
			ArrayList<Entry> currentRow = this.entries.get(i);
			if (currentRow != null) {
				for(int j = 0; j < currentRow.size(); j++) {
					Entry item = currentRow.get(j);
					int oldValue = item.getValue();
					int newValue = oldValue * scalar; // value after multiplication
					if (newValue != 0) { // if result is 0 discard
						Entry newItem = new Entry(item.getColumn(), newValue);
						currentRow.remove(j);
						currentRow.add(j, newItem);
					}
				}
			}
		}
	}

	// Number of rows of the matrix
	public int numRows() {
		if(this.entries != null) {
			return this.entries.size();
		} else {
			return 0;
		}
	}

	// Number of columns of the matrix
	public int numColumns() {
		return this.numCols;
	}

	// Output the elements of the matrix, including the zeros
	// Do not modify this method
	public void print() {
		int numRows = entries.size();
		for(int i = 0; i < numRows; ++ i) {
			ArrayList<Entry> currentRow = entries.get(i);
			int currentCol = -1, entryIdx = -1;
			if(currentRow != null && (!currentRow.isEmpty())) {
				entryIdx = 0;
				currentCol = currentRow.get(entryIdx).getColumn();
			}

			for(int j = 0;  j < numCols; ++ j) {
				if(j == currentCol) {
					System.out.print(currentRow.get(entryIdx).getValue());
					entryIdx++;
					currentCol = (entryIdx < currentRow.size()) ? currentRow.get(entryIdx).getColumn() : (-1);
				} else {
					System.out.print(0);
				}

				System.out.print(" ");
			}

			System.out.println();
		}
	}

	private int numCols;		// Number of columns
	private ArrayList< ArrayList<Entry> > entries;	// Entries for each row
}
