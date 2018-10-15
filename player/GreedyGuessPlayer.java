package player;

import world.World;
import world.World.Coordinate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import player.Cell.CellState;
import ship.AircraftCarrier;
import ship.Cruiser;
import ship.Frigate;
import ship.PatrolCraft;
import ship.Ship;
import ship.Submarine;
import player.Cell;

/**
 * Greedy guess player (task B).
 * @authors Liam Jeynes s3544919, Viet Quang Dao s3687103
 */
public class GreedyGuessPlayer extends Guesser implements Player {
	public List<Guess> guesses;
	public Set<Ship> ships = new HashSet<Ship>();

	@Override
	public void initialisePlayer(World world) {
		this.world = world;
		this.cell = new Cell(world.numRow, world.numColumn);
		this.hits = new ArrayList<>();
		this.guesses = new ArrayList<>();
		initGuesses(guesses);
	}
	
	// Initializes guesses used in algorithm i.e. every second cell.
	private void initGuesses(List<Guess> list) {
		for (int row = 0; row < world.numRow; ++row) {
			for (int column = row % 2; column < world.numColumn; column += 2) {
				Guess guess = new Guess();
				guess.row = row;
				guess.column = column;
				list.add(guess);
			}
		}
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
		// If there are possible targets, enter targetting mode.
		if (cell.possibleTargets.size() > 0)
			return targetMode();
		// Otherwise hunts for a hit.
		else
			return huntMode();
		

	}
	
	// Method updates state of the cell that was guessed accordingly i.e. hit or missed
	@Override
	public void update(Guess guess, Answer answer) {
		if (answer.isHit) 
			cell.updateCell(CellState.hit, guess.row, guess.column);
		else 
			cell.updateCell(CellState.miss, guess.row, guess.column);
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
	private Guess targetMode() {
		Guess guess = new Guess();
		Guess tempGuess = new Guess();
		Coordinate co = world.new Coordinate();
	
		// Removes the first item in possibleTargets list and sets it to temp coordinate, co
		co = cell.possibleTargets.remove(0);
		// Guess is then set to the location of this temp coordinate.
		guess.row = co.row;
		guess.column = co.column;
		
		// Loops through list of guesses until it finds the target and removes it.
		for (int i = 0; i < guesses.size(); i++) {
			tempGuess = guesses.get(i);
			if (tempGuess.row == guess.row && tempGuess.column == guess.column) {
				guesses.remove(i);
				break;
			}
		}
		return guess;
	}
	
	// Guesses a random cell from the list of guesses
	private Guess huntMode() {
		Random rand = new Random();
		int index = rand.nextInt(1000) % guesses.size();
		return guesses.remove(index);
	}
}
