package player;

import world.World;
import world.World.Coordinate;
import java.util.List;
import java.util.ArrayList;
import ship.Configuration;

/**
 * Class to store the state of the opponents world i.e. cells
 * @authors Liam Jeynes s3544919, Viet Quang Dao s3687103
 */
public class Cell{
	private World world = new World();
	public enum CellState{unchecked, possible, hit, miss};
	public CellState[][] cell = null;
	protected List<Coordinate> possibleTargets = new ArrayList<>();
	public List<Configuration> shipCounters = new ArrayList<>();
	protected Configuration aircraftCarrierSize;
	protected Configuration cruiserSize;
	protected Configuration frigateSize;
	protected Configuration patrolCraftSize;
	protected Configuration subSize;
	public Configuration total;
	public Configuration[] ships = null;
	
	// initializes all ship counters
	protected void initShipCounters(){
		total = new Configuration(numRows, numColumns, 0, 0);
		aircraftCarrierSize = new Configuration(numRows, numColumns, 3, 2);
		cruiserSize = new Configuration(numRows, numColumns, 2, 2);
		frigateSize = new Configuration(numRows, numColumns, 4, 1);
		patrolCraftSize = new Configuration(numRows, numColumns, 2, 1);
		subSize = new Configuration(numRows, numColumns, 3, 1);
		ships = new Configuration[]{aircraftCarrierSize, cruiserSize, frigateSize, patrolCraftSize, subSize};
		
		// adds all ships into shipCounters array list
		for(int i=0; i<ships.length; i++)
			shipCounters.add(ships[i]);
	}
	
	protected int numRows, numColumns;
	
	public Cell(int numRow, int numColumn){
		cell = new CellState[numRow][numColumn];
		this.numRows = numRow;
		this.numColumns = numColumn;
		initCells(cell);	
	}
	
	// initialize all cells to untested
	private void initCells(CellState[][] cell){
		for(int row = 0; row<numRows; ++row){
			for(int column = 0; column<numColumns; ++column){
				cell[row][column] = CellState.unchecked;
			}
		}
	}
	
	protected void updateCell(CellState state, int row, int column){
		cell[row][column] = state;
		if(state == CellState.hit)
			determineTargets(row, column);
	}
	
	// Method to determine if the cells (north, south, east, west) are unchecked i.e. a possible target
	private void determineTargets(int row, int column){
		int temp = row+1;
		if(temp<numRows)
			checkCell(temp, column);
		
		temp = row-1;
		if(temp>=0)
			checkCell(temp, column);
		
		temp = column+1;
		if(temp<numColumns)
			checkCell(row, temp);
		
		temp = column-1;
		if(temp>=0)
			checkCell(row, temp);
	}
	
	// Checks if the cell is unchecked i.e. hasn't been guesses/targetted
	private void checkCell(int row, int column){
		if(cell[row][column] == CellState.unchecked){
			Coordinate co = world.new Coordinate();
			co.row = row;
			co.column = column;
			
			cell[row][column] = CellState.possible;
			possibleTargets.add(co);	
		}
	}
	
	// Method to reset all possible targets
	protected void resetPossibleTargets(){
		for(int row=numRows-1; row>=0; row--){
			for(int col=0; col<numColumns; col++){
				if(cell[row][col] == CellState.possible)
					cell[row][col] = CellState.unchecked;
			}
		}
	}
	
}