package player;


import world.World;
import java.util.ArrayList;
import player.Cell;
import player.Cell.CellState;
import ship.Configurator;

/**
 * Probabilistic guess player (task C).
 * @authors Liam Jeynes s3544919, Viet Quang Dao s3687103
 */
public class ProbabilisticGuessPlayer extends Guesser implements Player{
	private static final int NUM_OF_FLEET_COORDINATES = 19;
	private boolean notSunk;
	private Guess initHit;
	private int dirLength;
	private Direction direction;
	private Configurator configurator;
	
    @Override
    public void initialisePlayer(World world) {
        this.world = world;
		this.cell = new Cell(world.numRow, world.numColumn);
		this.configurator = new Configurator(world, cell);
		this.hits = new ArrayList<>();
		this.cell.initShipCounters();
		this.notSunk = false;
		this.direction = Direction.NORTH;
		this.dirLength = 1;
		configurator.initialiseTotalCountToZero(cell.total);
		configurator.setupAllConfigurations(cell.shipCounters, cell.total);
    } 

    @Override
    public Answer getAnswer(Guess guess) {
		Answer answer = new Answer();
		// If guess resulted in a hit
		if (super.resultOfGuess(guess, answer))
			answer.isHit = true;
		else
			answer.isHit = false;
		return answer;  
	}

    @Override
	public Guess makeGuess() {
		Guess g = null;
		// **Targeting Mode
		if (notSunk){
			while(g == null){ // each loop represents a direction
				g = getNextTarget(this.initHit); //go in one direction for the next guess
				dirLength++; // increment for the next guess 
				// if that direction is a dead end
				if(g == null || configurator.isOutOfBounds(g.row, g.column) || configurator.isObstacle(g.row, g.column)){
					g = null;
					direction = direction.next();
					dirLength = 1;
				}
			}
		}
		// **Hunting Mode**
		else{
			g = new Guess();
			//clear the possible targets from the last ship
			cell.possibleTargets.clear(); 
			cell.resetPossibleTargets();
			// look for the cell with the highest configuration count
			int highestCount = 0;
			for(int y = 0; y < cell.numRows; ++y){ //for each row
				for(int x = 0; x < cell.numColumns; ++x){ // for each column
					// if it is a higher number, make it the new guess
					if(cell.total.configCounts[y][x] > highestCount){
						highestCount = cell.total.configCounts[y][x];
						g.column = x;
						g.row = y;
					}
				}
			}
		}
		return g;
	} // end of makeGuess()


	@Override
	public void update(Guess guess, Answer answer) {
		// Check actions for a hit
		if(answer.isHit){
			if(initHit == null){
				initHit = guess;
			}
			cell.updateCell (CellState.hit, guess.row, guess.column );
			notSunk = true;
			if(answer.shipSunk != null){
				initHit = null;
				notSunk = false;
				dirLength = 1;
			}
		}
		else{
			if(notSunk){
				direction = direction.next();
				dirLength = 1;
			}
			cell.updateCell (CellState.miss, guess.row, guess.column );
		}
		
		// Check actions if is ship sunk
		configurator.updateConfigurationCount(guess);
		configurator.recalculateTotalCount();
	}


    @Override
    public boolean noRemainingShips() {
        return hits.size() >= NUM_OF_FLEET_COORDINATES;
    } 
    
	public Guess getNextTarget(Guess centre) {
		Guess g = new Guess();
		g.column = centre.column;
		g.row = centre.row;
		switch(direction){
			case NORTH:
				g.row = g.row + dirLength;
				break;
			case EAST:
				g.column = g.column + dirLength;
				break;
			case WEST:
				g.column = g.column - dirLength;
				break;
			case SOUTH:
				g.row = g.row - dirLength;
				break;
		}
		
		return g;
	}

} 
