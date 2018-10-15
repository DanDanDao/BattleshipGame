package ship;

import world.World;
import java.util.List;
import player.Cell;
import player.Cell.CellState;

/**
 * Class used for task C to setup/initialize and calculate ship configuration counts.
 * @authors Liam Jeynes s3544919, Viet Quang Dao s3687103
 */
public class Configurator {
	private World world;
	private Cell cell;

	public Configurator(World world, Cell cell) {
		this.world = world;
		this.cell = cell;
	}
	
	// Method to initialize total count to zero.
	public void initTotalCount(Configuration board) {
		for (int row = 0; row < board.rows; ++row) { 
			for (int column = 0; column < board.columns; ++column) 
				board.configCounts[row][column] = 0;
		}
	}

	// Method to setup all of the configurations
	public void setupConfigurations(List<Configuration> shipCounters, Configuration total) {
		// For each counter
		for (Configuration board : shipCounters) { 
			for (int row = 0; row < board.rows; ++row) { 
				for (int column = 0; column < board.columns; ++column) { 
					// Counts the configurations
					int configurations = getShipConfigurationCount(row, column, board.shipSize);
					// Stores the result in the board
					board.configCounts[row][column] = configurations;
					// Updates the total count for that cell
					total.configCounts[row][column] += configurations;
				}
			}
		}
	}
	
	// Method that gets ship configuration counts for a single cell
	private int getShipConfigurationCount(int column, int row, int shipSize) {
		int rowMax = getUpperBoundForRow(column, row, shipSize);
		int rowMin = getLowerBoundForRow(column, row, shipSize);
		int colMax = getUpperBoundForColumn(column, row, shipSize);
		int colMin = getLowerBoundForColumn(column, row, shipSize);
		
		// Calculates the total number of cells available
		int rowRange = rowMax - rowMin - 1;
		int colRange = colMax - colMin - 1;
		
		// Calculates how many different ways a ship can be placed in that space
		int rowConfigurations = max(0, 1 + rowRange - shipSize);
		int colConfigurations = max(0, 1 + colRange - shipSize);
		
		return rowConfigurations + colConfigurations;
	}

	private int getLowerBoundForColumn(int column, int row, int shipSize) {
		// Loops through cells below in the column
		for (int y = 1; y < shipSize; ++y) { 
			if (isOutOfBounds(column, row - y) || isObstacle(column, row - y))
				return row - y;
		}
		return row - shipSize; 
	}

	private int getUpperBoundForColumn(int column, int row, int shipSize) {
		// Loops through cells above in the column
		for (int y = 1; y < shipSize; ++y) { 
			if (isOutOfBounds(column, row + y) || isObstacle(column, row + y))
				return row + y;
		}
		return row + shipSize; 
	}

	private int getLowerBoundForRow(int column, int row, int shipSize) {
		// Loops through cells below in the row
		for (int x = 1; x < shipSize; ++x) { 
			if (isOutOfBounds(column - x, row) || isObstacle(column - x, row))
				return column - x;
		}
		return column - shipSize; 
	}

	private int getUpperBoundForRow(int column, int row, int shipSize) {
		// Loops through cells above in the row
		for (int x = 1; x < shipSize; ++x) { 
			if (isOutOfBounds(column + x, row) || isObstacle(column + x, row))
				return column + x;
		}
		return column + shipSize; 
	}
	
	public boolean isObstacle(int y, int x) {
		return (cell.cell[y][x] == CellState.miss || cell.cell[y][x] == CellState.hit);
	}

	// Method to determine if the coordinates are within the size of the board/world.
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
