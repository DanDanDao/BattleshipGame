package player;

import world.World;
import world.World.Coordinate;
import java.util.List;
import java.util.ArrayList;

/**
 * Class to store the state of the opponents world i.e. cells
 * @authors Liam Jeynes s3544919, Viet Quang Dao s3687103
 */
public class Cell{
	private World world = new World();
	public enum CellState{unchecked, possible, hit, miss};
	protected CellState[][] cell = null;
	protected List<Coordinate> possibleTargets = new ArrayList<>();
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
	
	public void updateCell(CellState state, int row, int column){
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
	
}