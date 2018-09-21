package ship;

import player.Guess;
import world.World;

import java.util.List;

import player.Cell;
import player.Cell.CellState;

public class Configurator {

	private World world;
	private Cell cell;

	public Configurator(World world, Cell cell) {
		this.world = world;
		this.cell = cell;
	}

	public void initialiseTotalCountToZero(Configuration board) {
		for (int y = 0; y < board.rows; ++y) { // for each row
			for (int x = 0; x < board.columns; ++x) { // for each column
				board.configCounts[y][x] = 0;
			}
		}

	}

	public void setupAllConfigurations(List<Configuration> shipCounters, Configuration total) {
		for (Configuration board : shipCounters) { // for each counter
			for (int y = 0; y < board.rows; ++y) { // for each row
				for (int x = 0; x < board.columns; ++x) { // for each cell
					// Count the configurations
					int configurations = getShipConfigurationCountForOneCell(y, x, board.shipSize);
					// Store the result in the board
					board.configCounts[y][x] = configurations;
					// Update the total Count for that cell
					total.configCounts[y][x] += configurations;
				}
			}
		}
	}

	public void recalculateTotalCount() {
		// reset all counts to zero
		initialiseTotalCountToZero(cell.total);
		// iterate through every cell in shipCounters and accumulate a total
		for (Configuration shipCounter : cell.shipCounters) { // for each ship
			for (int y = 0; y < world.numRow; ++y) { // for each row
				for (int x = 0; x < world.numColumn; ++x) { // for each column
					cell.total.configCounts[y][x] += shipCounter.configCounts[y][x];
				}
			}
		}
	}

	public int getShipConfigurationCountForOneCell(int column, int row, int shipSize) {
		int rowMax = getUpperBoundForRow(column, row, shipSize);
		int rowMin = getLowerBoundForRow(column, row, shipSize);
		int colMax = getUpperBoundForColumn(column, row, shipSize);
		int colMin = getLowerBoundForColumn(column, row, shipSize);
		// calculate the total number of cells available in the row and col
		int rowRange = rowMax - rowMin - 1;
		int colRange = colMax - colMin - 1;
		// calculate how many ways a ship can be placed in that space
		int rowConfigurations = max(0, 1 + rowRange - shipSize);
		int colConfigurations = max(0, 1 + colRange - shipSize);
		return rowConfigurations + colConfigurations;
	}

	public int getLowerBoundForColumn(int column, int row, int shipSize) {
		for (int y = 1; y < shipSize; ++y) { // cells below in the column
			if (isOutOfBounds(column, row - y) || isObstacle(column, row - y))
				return row - y;
		}
		return row - shipSize; // Bound is excluded from the range
	}

	public int getUpperBoundForColumn(int column, int row, int shipSize) {
		for (int y = 1; y < shipSize; ++y) { // cells above in the column
			if (isOutOfBounds(column, row + y) || isObstacle(column, row + y))
				return row + y;
		}
		return row + shipSize; // Bound is excluded from the range
	}

	public int getLowerBoundForRow(int column, int row, int shipSize) {
		for (int x = 1; x < shipSize; ++x) { // cells below in the row
			if (isOutOfBounds(column - x, row) || isObstacle(column - x, row))
				return column - x;
		}
		return column - shipSize; // Bound is excluded from the range
	}

	public int getUpperBoundForRow(int column, int row, int shipSize) {
		for (int x = 1; x < shipSize; ++x) { // cells above in the row
			if (isOutOfBounds(column + x, row) || isObstacle(column + x, row))
				return column + x;
		}
		return column + shipSize; // Bound is excluded from the range
	}

	public void updateConfigurationCount(Guess guess) {
		// for each shipCounter
		for (Configuration shipCounter : cell.shipCounters) {
			// set shot to zero
			shipCounter.configCounts[guess.row][guess.column] = 0;
			// travel each direction to update counts
			updateRowUpper(guess, shipCounter);
			updateRowLower(guess, shipCounter);
			updateColumnUpper(guess, shipCounter);
			updateColumnLower(guess, shipCounter);
		}
	}

	public void updateRowUpper(Guess guess, Configuration shipCounter) {
		// visit each cell above the shot in the row and recalculate the count
		for (int x = 1; x < shipCounter.shipSize; ++x) { // start at 1 to avoid
															// the shot cell
			// if there is an obstacle, stop
			if (isOutOfBounds(guess.row, guess.column + x) || isObstacle(guess.row, guess.column + x))
				break;
			else // recalculate the configurations
				shipCounter.configCounts[guess.row][guess.column + x] = getShipConfigurationCountForOneCell(guess.row,
						guess.column + x, shipCounter.shipSize);
		}
	}

	public void updateRowLower(Guess guess, Configuration shipCounter) {
		// visit each cell below the shot in the row and recalculate the count
		for (int x = 1; x < shipCounter.shipSize; ++x) {
			// if there is an obstacle, stop
			if (isOutOfBounds(guess.row, guess.column - x) || isObstacle(guess.row, guess.column - x))
				break;
			else // recalculate the configurations
				shipCounter.configCounts[guess.row][guess.column - x] = getShipConfigurationCountForOneCell(guess.row,
						guess.column - x, shipCounter.shipSize);
		}
	}

	public void updateColumnUpper(Guess guess, Configuration shipCounter) {
		// visit each cell above the shot in the column and recalculate the
		// count
		for (int y = 1; y < shipCounter.shipSize; ++y) {
			// if there is an obstacle, stop
			if (isOutOfBounds(guess.row + y, guess.column) || isObstacle(guess.row + y, guess.column))
				break;
			else // recalculate the configurations
				shipCounter.configCounts[guess.row + y][guess.column] = getShipConfigurationCountForOneCell(
						guess.row + y, guess.column, shipCounter.shipSize);
		}
	}

	public void updateColumnLower(Guess guess, Configuration shipCounter) {
		// visit each cell below the shot in the column and recalculate the
		// count
		for (int y = 1; y < shipCounter.shipSize; ++y) {
			// if there is an obstacle, stop
			if (isOutOfBounds(guess.row - y, guess.column) || isObstacle(guess.row - y, guess.column))
				break;
			else // recalculate the configurations
				shipCounter.configCounts[guess.row - y][guess.column] = getShipConfigurationCountForOneCell(
						guess.row - y, guess.column, shipCounter.shipSize);
		}
	}

	public boolean isObstacle(int y, int x) {
		return (cell.cell[y][x] == CellState.miss || cell.cell[y][x] == CellState.hit);
	}

	public boolean isOutOfBounds(int y, int x) {
		return (y < 0 || y >= world.numRow || x < 0 || x >= world.numColumn);
	}

	private int max(int x, int y) {
		if (x > y)
			return x;
		else
			return y;
	}

}
