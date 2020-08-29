/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package rushhour;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import search.Action;
import search.State;

/**
*
* @author steven
*/
public class GameState implements search.State {

	Integer[][] occupiedPositions;
	List<Car> cars; // target car is always the first one
	int nrRows;
	int nrCols;
	int spacesAbove; // spaces above target car on board
	int spacesBelow; // spaces below target car on board

	public GameState(String fileName) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		nrRows = Integer.parseInt(in.readLine().split("\\s")[0]);
		nrCols = Integer.parseInt(in.readLine().split("\\s")[0]);
		String s = in.readLine();
		cars = new ArrayList();
		while (s != null) {
			cars.add(new Car(s));
			s = in.readLine();
		}
		spacesAbove = cars.get(0).getRow();
		spacesBelow = nrRows - (cars.get(0).getRow() + 1);
		initOccupied();
	}

	public GameState(int nrRows, int nrCols, List<Car> cars) {
		this.nrRows = nrRows;
		this.nrCols = nrCols;
		this.cars = cars;
		this.spacesAbove = cars.get(0).getRow();
		this.spacesBelow = this.nrRows - (cars.get(0).getRow() + 1);
		initOccupied();
	}

	public GameState(GameState gs) {
		nrRows = gs.nrRows;
		nrCols = gs.nrCols;
		//occupiedPositions = new boolean[nrRows][nrCols];
		occupiedPositions = new Integer[nrRows][nrCols];
		for (int i = 0; i < nrRows; i++) {
			for (int j = 0; j < nrCols; j++) {
				occupiedPositions[i][j] = gs.occupiedPositions[i][j];
			}
		}
		cars = new ArrayList();
		for (Car c : gs.cars) {
			cars.add(new Car(c));
		}
		spacesAbove = gs.cars.get(0).getRow();
		spacesBelow = gs.nrCols - (gs.cars.get(0).getRow() + 1);
	}

	public void moveCar(Car car, int row, int col, int carNum) {
		// set car positions to false
		List<Position> prevPositions = car.getOccupyingPositions();
		for (Position pos : prevPositions) {
			occupiedPositions[pos.getRow()][pos.getCol()] = null;
		}
		car.setRow(row);
		car.setCol(col);
		// set car new positions to true
		prevPositions = car.getOccupyingPositions();
		for (Position pos : prevPositions) {
			occupiedPositions[pos.getRow()][pos.getCol()] = carNum;
		}
	}

	public void printState() {
		int[][] state = new int[nrRows][nrCols];

		for (int i = 0; i < cars.size(); i++) {
			List<Position> l = cars.get(i).getOccupyingPositions();
			for (Position pos : l) {
				state[pos.getRow()][pos.getCol()] = i + 1;
			}
		}

		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[0].length; j++) {
				if (state[i][j] == 0) {
					System.out.print(".");
				} else {
					System.out.print(state[i][j] - 1);
				}
			}
			System.out.println();
		}
	}

	// prints given game state in an easy to visualise format
	public void printBoard() {
		String[][] state = new String[nrRows][nrCols];
		String direction;

		// Sets all deep array values to empty string
		for (int i = 0; i < nrRows; i++) {
			for (int j = 0; j < nrCols; j++) {
				state[i][j] = "#";
			}
		}

		// populate car positions with number and vertical or horizontal
		for (int i = 0; i < cars.size(); i++) {
			List<Position> l = cars.get(i).getOccupyingPositions();
			for (Position pos : l) {
				if (cars.get(i).isVertical()) {
					direction = "V";
				} else {
					direction = "H";
				}
				state[pos.getRow()][pos.getCol()] = Integer.toString(i) + direction;
			}
		}

		// print deep array as table
		for(String[] row : state) {
			for (String i : row) {
				System.out.print(i);
				System.out.print("\t");
			}
			System.out.println();
		}
	}

	// prints all occupied values as a table (nrcols, nrRows)
	public void printOccupied() {
		boolean[][] state = new boolean[nrRows][nrCols];

		for (int i = 0; i < nrRows; i++) {
			for (int j = 0; j < nrCols; j++) {
				if (occupiedPositions[i][j] == null) {
					state[i][j] = false;
				}	else {
					state[i][j] = true;
				}
			}
		}

		// print deep array as table
		for(boolean[] row : state) {
			for (boolean i : row) {
				System.out.print(i);
				System.out.print("\t");
			}
			System.out.println();
		}
	}

	// prints all occupied values as a table (nrcols, nrRows)
	public void printCars() {
		Integer[][] state = new Integer[nrRows][nrCols];

		for (int i = 0; i < nrRows; i++) {
			for (int j = 0; j < nrCols; j++) {
				state[i][j] = occupiedPositions[i][j];
			}
		}

		// print deep array as table
		for(Integer[] row : state) {
			for (Integer i : row) {
				System.out.print(i);
				System.out.print("\t");
			}
			System.out.println();
		}
	}

	private void initOccupied() {
		occupiedPositions = new Integer[nrRows][nrCols];
		Car c;
		for (int i=0; i < cars.size(); i++) { // start after target car (already processed it)
			c = cars.get(i);
			List<Position> l = c.getOccupyingPositions();
			for (Position pos : l) {
				occupiedPositions[pos.getRow()][pos.getCol()] = i;
			}
		}
	}

	public boolean isGoal() {
		Car goalCar = cars.get(0);
		return goalCar.getCol() + goalCar.getLength() == nrCols;
	}

	public boolean equals(Object o) {
		if (!(o instanceof GameState)) {
			return false;
		} else {
			GameState gs = (GameState) o;
			return nrRows == gs.nrRows && nrCols == gs.nrCols && cars.equals(gs.cars); // note that we don't need to check equality of occupiedPositions since that follows from the equality of cars
		}
	}

	public int hashCode() {
		return cars.hashCode();
	}

	public void printToFile(String fn) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(fn));
			out.println(nrRows);
			out.println(nrCols);
			for (Car c : cars) {
				out.println(c.getRow() + " " + c.getCol() + " " + c.getLength() + " " + (c.isVertical() ? "V" : "H"));
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Return an arraylist of legal actions for the given game state
	public List<Action> getLegalActions() {
		ArrayList<Action> legalActions = new ArrayList();
		boolean up = false;
		boolean down = false;
		boolean right = false;
		boolean left = false;
		int positions;
		Position carPositionStart;
		Position carPositionEnd;
		int carIndex = 0;

		for (Car c : cars) {
			carPositionStart = c.getstartEndPositions().get(0); // Get coordinates of front of car
			carPositionEnd = c.getstartEndPositions().get(1); // Get coordinates of back of car
			positions = 1;

			if (c.isVertical()) {
				up = true;
				down = true;
				while (up) {
					if (carPositionStart.getRow() - positions >= 0) {
						Move move = new Move(carIndex, positions, "up");
						if(isLegal(move)) {
							legalActions.add(move);
							positions++;
						} else {
							up = false; // no more space above car
							positions = 1; // reset positions to move
						}
					} else {
						up = false; // no more space above of car
						positions = 1; // reset positions to move
					}
				}
				while (down) {
					if (carPositionEnd.getRow() + positions <= nrRows - 1) {
						Move move = new Move(carIndex, positions, "down");
						if(isLegal(move)) {
							legalActions.add(move);
							positions++;
						} else {
							down = false; // no more space below car
							positions = 1; // reset positions to move
						}
					} else {
						down = false; // no more space below of car
						positions = 1; // reset positions to move
					}
				}
			} else {
				right = true;
				left = true;
				while (left) {
					if (carPositionStart.getCol() - positions >= 0) {
						Move move = new Move(carIndex, positions, "left");
						if(isLegal(move)) {
							legalActions.add(move);
							positions++;
						} else {
							left = false; // no more space left of car
							positions = 1; // reset positions to move
						}
					} else {
						left = false; // no more space left of car
						positions = 1; // reset positions to move
					}
				}
				while (right) {
					if (carPositionEnd.getCol() + positions <= nrCols - 1) {
						Move move = new Move(carIndex, positions, "right");
						if(isLegal(move)) {
							legalActions.add(move);
							positions++;
						} else {
							right = false; // no more space right of car
							positions = 1; // reset positions to move
						}
					} else {
						right = false; // no more space right of car
						positions = 1; // reset positions to move
					}
				}
			}
			carIndex++;
		}
		return legalActions;
	}

	public boolean isLegal(Action action) {
		if (!(action instanceof Move)) {
			return false;
		}
		Move move = (Move) action;
		Car car = cars.get(move.getCar());
		Position carStart = car.getstartEndPositions().get(0);
		Position carEnd = car.getstartEndPositions().get(1);
		boolean legal = false;
		if (move.isUp()) {
			if (occupiedPositions[carStart.getRow() - move.getSpaces()][carStart.getCol()] == null) {
				legal = true;
			}
		} else if (move.isDown()) {
			if (occupiedPositions[carEnd.getRow() + move.getSpaces()][carEnd.getCol()] == null) {
				legal = true;
			}
		}	else if (move.isLeft()) {
			if (occupiedPositions[carStart.getRow()][carStart.getCol() - move.getSpaces()] == null) {
				legal = true;
			}
		} else if (move.isRight()) {
			if (occupiedPositions[carEnd.getRow()][carEnd.getCol() + move.getSpaces()] == null) {
				legal = true;
			}
		}
		return legal;
	}

	public State doAction(Action action) {
		Move move = (Move) action;
		GameState state = new GameState(this);
		Car currentCar = state.cars.get(move.getCar());
		Position carPositionStart = currentCar.getstartEndPositions().get(0); // Get coordinates of front of car
		Position carPositionEnd = currentCar.getstartEndPositions().get(1); // Get coordinates of back of car

		if (move.isLeft()) {
			if(state.occupiedPositions[carPositionStart.getRow()][carPositionStart.getCol() - move.getSpaces()] == null) {
				state.moveCar(currentCar, currentCar.getRow(), currentCar.getCol() - move.getSpaces(), move.getCar());
			}
		}	else if(move.isRight()) {
			if(state.occupiedPositions[carPositionEnd.getRow()][carPositionEnd.getCol() + move.getSpaces()] == null) {
				state.moveCar(currentCar, currentCar.getRow(), currentCar.getCol() + move.getSpaces(), move.getCar());
			}
		} else if(move.isUp()) {
			if(state.occupiedPositions[carPositionStart.getRow() - move.getSpaces()][carPositionStart.getCol()] == null) {
				state.moveCar(currentCar, currentCar.getRow() - move.getSpaces(), currentCar.getCol(), move.getCar());
			}
		} else if(move.isDown()) {
			if(state.occupiedPositions[carPositionEnd.getRow() + move.getSpaces()][carPositionEnd.getCol()] == null) {
				state.moveCar(currentCar, currentCar.getRow() + move.getSpaces(), currentCar.getCol(), move.getCar());
			}
		} else {
			return null;
		}
		return state;
	}

	// return the blocking cost to move a selected car
	public ArrayList<Integer> getBlockingCost(Car selectedCar, int frontSpace, int backSpace, int lastCarLoc, int depth) {
		Position carStart = selectedCar.getstartEndPositions().get(0); // Get coordinates of front of car
		Position carEnd = selectedCar.getstartEndPositions().get(1); // Get coordinates of back of car
		int spacesToMoveFront;
		int spacesToMoveBack;
		boolean front;
		boolean back;
		int startLoc; // either col or row depending what one we are intrested in
		int endLoc; // either col or row depending what one we are intrested in
		boolean blockedFront;
		boolean blockedBack;
		int spacesLeft;
		int spacesRight;
		int carLoc;
		int searchCol;
		int searchRow;
		ArrayList<Integer> uniqueCars = new ArrayList<Integer>();
		ArrayList<Integer> CarsFront = new ArrayList<Integer>();
		ArrayList<Integer> CarsBack = new ArrayList<Integer>();

		if (selectedCar.isVertical()) {
			startLoc = carStart.getRow();
			endLoc = carEnd.getRow();
			spacesLeft = selectedCar.getCol();
			spacesRight = nrCols - (selectedCar.getCol() + 1);
			carLoc = selectedCar.getCol();
		} else {
			startLoc = carStart.getCol();
			endLoc = carEnd.getCol();
			spacesLeft = selectedCar.getRow();
			spacesRight = nrCols - (selectedCar.getRow() + 1);
			carLoc = selectedCar.getRow();;
		}

		// select above and below to search (depending if car fits above/below target car)
		if (selectedCar.getLength() > frontSpace) {
			front = false;
			spacesToMoveFront = 0;
		} else {
			front = true;
			spacesToMoveFront = endLoc - (lastCarLoc - 1);
		}
		if (selectedCar.getLength() > backSpace) {
			back = false;
			spacesToMoveBack = 0;
		} else {
			back = true;
			spacesToMoveBack = (lastCarLoc + 1) - startLoc;
		}
		// see if car can move out of the way
		blockedFront = false;
		blockedBack = false;
		if (front) {
			for (int i = 1; i <= spacesToMoveFront; i++) {
				if (selectedCar.isVertical()) {
					searchCol = carLoc;
					searchRow = carStart.getRow() - i;
				} else {
					searchCol = carStart.getCol() - i;
					searchRow = carLoc;
				}
				if (occupiedPositions[searchRow][searchCol] != null) {
					Car blockingCar = cars.get(occupiedPositions[searchRow][searchCol]);
					CarsFront.add(occupiedPositions[searchRow][searchCol]);
					if (depth - 1 > 0) {
						ArrayList<Integer> deeperCars = getBlockingCost(blockingCar, spacesLeft, spacesRight, carLoc, depth - 1);
						for (int c : deeperCars) {
							if(!CarsFront.contains(c)) {
								CarsFront.add(c);
							}
						}
					}
					blockedFront = true;
				}
			}
		}
		if (back) {
			for (int i = 1; i <= spacesToMoveBack; i++) {
				if (selectedCar.isVertical()) {
					searchCol = carLoc;
					searchRow = carEnd.getRow() + i;
				} else {
					searchCol = carEnd.getCol() + i;
					searchRow = carLoc;
				}
				if (occupiedPositions[searchRow][searchCol] != null) {
					CarsBack.add(occupiedPositions[searchRow][searchCol]);
					Car blockingCar = cars.get(occupiedPositions[searchRow][searchCol]);
					if (depth - 1 > 0) {
						ArrayList<Integer> deeperCars = getBlockingCost(blockingCar, spacesLeft, spacesRight, carLoc, depth - 1);
						for (int c : deeperCars) {
							if(!CarsBack.contains(c)) {
								CarsBack.add(c);
							}
						}
					}
					blockedBack = true;
				}
			}
		}

		// Add the lower number of blocking cars to uniqueCars
		if (front && back)  {
			if (blockedFront && blockedBack) {
				if (CarsBack.size() > CarsFront.size()) {
					for (int c : CarsFront) {
						if(!uniqueCars.contains(c)) {
							uniqueCars.add(c);
						}
					}
				} else {
					for (int c : CarsBack) {
						if(!uniqueCars.contains(c)) {
							uniqueCars.add(c);
						}
					}
				}
			}
		} else if (front) {
			if (blockedFront) {
				for (int c : CarsFront) {
					if(!uniqueCars.contains(c)) {
						uniqueCars.add(c);
					}
				}
			}
		} else if (back) {
			if (blockedBack) {
				for (int c : CarsBack) {
					if(!uniqueCars.contains(c)) {
						uniqueCars.add(c);
					}
				}
			}
		}
		return uniqueCars;
	}

	public int getEstimatedDistanceToGoalDeep() {
		int distance = 0;
		int carRow = cars.get(0).getstartEndPositions().get(1).getRow();
		int carCol = cars.get(0).getstartEndPositions().get(1).getCol();
		Car car;
		ArrayList<Integer> deeperCars = new ArrayList<Integer>();

		// If target car is not at target then add 1 to distance
		if (carCol != nrCols - 1) {
			distance++;
		}
		for (int col=carCol + 1; col < nrCols; col++) {
			if (occupiedPositions[carRow][col] != null) {
				distance++;
				car = cars.get(occupiedPositions[carRow][col]);
				deeperCars = getBlockingCost(car, spacesAbove, spacesBelow, carRow, 2);
			}
		}
		distance += deeperCars.size();
		return distance;
	}


	public int getEstimatedDistanceToGoal() {
		int distance = 0;
		int carRow = cars.get(0).getstartEndPositions().get(1).getRow();
		int carCol = cars.get(0).getstartEndPositions().get(1).getCol();
		Car car;
		Position carStart;
		Position carEnd;
		boolean above;
		boolean below;
		Car blockingCar;
		boolean blockedUp;
		boolean blockedbelow;
		int spacesToMoveUp;
		int spacesToMoveDown;
		// If target car is not at target then add 1 to distance
		if (carCol != nrCols - 1) {
			distance++;
		}
		ArrayList<Integer> uniqueCars = new ArrayList<Integer>(); // list of blocking cars (a car can only be in here once)
		for (int col=carCol + 1; col < nrCols; col++) {
			if (occupiedPositions[carRow][col] != null) {
				car = cars.get(occupiedPositions[carRow][col]);
				carStart = car.getstartEndPositions().get(0); // Get coordinates of front of car
				carEnd = car.getstartEndPositions().get(1); // Get coordinates of back of car
				distance++;
				// select above and below to search (depending if car fits above/below target car)
				if (car.getLength() > spacesAbove) {
					above = false;
					spacesToMoveUp = 0;
				} else {
					above = true;
					spacesToMoveUp = carEnd.getRow() - (carRow - 1);
				}
				if (car.getLength() > spacesBelow) {
					below = false;
					spacesToMoveDown = 0;
				} else {
					below = true;
					spacesToMoveDown = (carRow + 1) - carStart.getRow();
				}
				// see if car can move out of the way. If another car is blocking it then add it to a list
				blockedUp = false;
				blockedbelow = false;
				ArrayList<Integer> CarsBack = new ArrayList<Integer>();
				ArrayList<Integer> CarsFront = new ArrayList<Integer>();
				if (above) {
					for (int i = 1; i <= spacesToMoveUp; i++) {
						if (occupiedPositions[carStart.getRow() - i][col] != null) {
							blockingCar = cars.get(occupiedPositions[carStart.getRow() - i][col]);
							blockedUp = true;
							if(!CarsFront.contains(occupiedPositions[carStart.getRow() - i][col])) {
								CarsFront.add(occupiedPositions[carStart.getRow() - i][col]);
							}
						}
					}
				}
				if (below) {
					for (int i = 1; i <= spacesToMoveDown; i++) {
						if (occupiedPositions[carEnd.getRow() + i][col] != null) {
							blockingCar = cars.get(occupiedPositions[carEnd.getRow() + i][col]);
							blockedbelow = true;
							if(!CarsBack.contains(occupiedPositions[carEnd.getRow() + i][col])) {
								CarsBack.add(occupiedPositions[carEnd.getRow() + i][col]);
							}
						}
					}
				}

				// Add the lower number of blocking cars to uniqueCars
				if (above && below)  {
					if (blockedUp && blockedbelow) {
						if (CarsBack.size() > CarsFront.size()) {
							for (int c : CarsFront) {
								if(!uniqueCars.contains(c)) {
									uniqueCars.add(c);
								}
							}
						} else {
							for (int c : CarsBack) {
								if(!uniqueCars.contains(c)) {
									uniqueCars.add(c);
								}
							}
						}
					}
				} else if (above) {
					if (blockedUp) {
						for (int c : CarsFront) {
							if(!uniqueCars.contains(c)) {
								uniqueCars.add(c);
							}
						}
					}
				} else if (below) {
					if (blockedbelow) {
						for (int c : CarsBack) {
							if(!uniqueCars.contains(c)) {
								uniqueCars.add(c);
							}
						}
					}
				}
			}
		}
		distance += uniqueCars.size(); // each car in here is equal to one move to make
		return distance;
	}

}
