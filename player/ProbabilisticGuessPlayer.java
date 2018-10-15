package player;

import world.World;
import java.util.ArrayList;
import player.Cell;
import player.Cell.CellState;
import ship.AircraftCarrier;
import ship.Configurator;
import ship.Cruiser;
import ship.Frigate;
import ship.PatrolCraft;
import ship.Ship;
import ship.Submarine;

/**
* Probabilistic guess player (task C).
* @authors Liam Jeynes s3544919, Viet Quang Dao s3687103
*/
public class ProbabilisticGuessPlayer extends Guesser implements Player{
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
		configurator.initTotalCount(cell.total);
		configurator.setupConfigurations(cell.shipCounters, cell.total);
	}

	@Override
	public Answer getAnswer(Guess guess) {
		Answer answer = new Answer();
		// If guess resulted in a hit
		if (super.resultOfGuess(guess, answer)) {
			answer.isHit = true;
			// If ship has been sunk.
			if (answer.shipSunk != null) {
				cell.possibleTargets.clear();
				cell.resetPossibleTargets();
			}
		} 
		// Otherwise guess has missed.
		else 
			answer.isHit = false;
		
		return answer;
	}

	@Override
	public Guess makeGuess() {
		Guess guess = new Guess();
		// If a hit has been made (but the ship has no been sunk), enter target mode
		if (notSunk)
			guess = targetMode();
		// Otherwise enter hunt mode
		else
			guess = huntMode();
		
		return guess;
	} 
	

	@Override
	public void update(Guess guess, Answer answer) {
		// If the guess hit
		if(answer.isHit){
			// If it is the first hit.
			if(initHit == null)
				initHit = guess;
			
			// If the ship was sunk
			if(answer.shipSunk != null){
				initHit = null;
				notSunk = false;
				dirLength = 1;
			}
			else 
				notSunk = true;

			// Update cell as a hit
			cell.updateCell(CellState.hit, guess.row, guess.column);
		}
		// Otherwise attack was a miss
		else{
			// If attack missed and not sunk has been set to true, switch direction
			if(notSunk){
				direction = direction.next();
				dirLength = 1;
			}
			// Update cell as a miss.
			cell.updateCell (CellState.miss, guess.row, guess.column );
		}
	}

	@Override
	public boolean noRemainingShips() {
		Ship aCarrier = new AircraftCarrier();
		Ship cruiser = new Cruiser();
		Ship frigate = new Frigate();
		Ship pCraft = new PatrolCraft();
		Ship sub = new Submarine();
		
		// Returns true if the amount of hits taken is the same as the number of coordinates for all of ships.
		return hits.size() >= aCarrier.len() * aCarrier.width() + cruiser.len() * cruiser.width() + frigate.len() * frigate.width() + pCraft.len() * pCraft.width()
				+ sub.len() * sub.width();
	}

	// Method for targeting the possible targets
	private Guess targetMode(){
		Guess guess = null;
		// Each loop represents a direction.
		while(guess == null){ 
			// Goes in one direction for the next guess
			guess = getNextTarget(this.initHit); 
			dirLength++; 
			
			// Determines if the direction is a dead-end
			if(guess == null || configurator.isOutOfBounds(guess.row, guess.column) || configurator.isObstacle(guess.row, guess.column)){
				guess = null;
				direction = direction.next();
				dirLength = 1;
			}
		}
		return guess;
	}
	
	// Method for finding next target
	private Guess huntMode(){
		Guess guess = new Guess();
		// Clears the possible targets from the last ship
		cell.possibleTargets.clear(); 
		cell.resetPossibleTargets();
		
		// Looks for the cell with the highest configuration count
		int highestCount = 0;
		for(int row = 0; row < cell.numRows; ++row){ 
			for(int column = 0; column < cell.numColumns; ++column){ 
				// If the number is higher, set cell as the guess
				if(cell.total.configCounts[row][column] > highestCount){
					highestCount = cell.total.configCounts[row][column];
					guess.column = column;
					guess.row = row;
				}
			}
		}
		// Returns guess found
		return guess;
	}
	
	private Guess getNextTarget(Guess centre) {
		Guess guess = new Guess();
		guess.column = centre.column;
		guess.row = centre.row;
		
		switch(direction){
			case NORTH:
			guess.row = guess.row + dirLength;
			break;
			case EAST:
			guess.column = guess.column + dirLength;
			break;
			case WEST:
			guess.column = guess.column - dirLength;
			break;
			case SOUTH:
			guess.row = guess.row - dirLength;
			break;
		}
		return guess;
	}

}
