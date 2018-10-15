package player;

import world.World;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import player.Cell.CellState;
import ship.AircraftCarrier;
import ship.Cruiser;
import ship.Frigate;
import ship.PatrolCraft;
import ship.Ship;
import ship.Submarine;
import player.Cell;

/**
 * Random guess player (task A).
 * @authors Liam Jeynes s3544919, Viet Quang Dao s3687103
 */
public class RandomGuessPlayer extends Guesser implements Player{
	public List<Guess> guesses;
   
   @Override
    public void initialisePlayer(World world) {
        this.world = world;
		this.cell = new Cell(world.numRow, world.numColumn);
		this.guesses = new ArrayList<>();
		this.hits = new ArrayList<>();
		initGuesses(guesses);
    } 
	
	// Initialises guesses used in algorithm i.e. every cell
	private void initGuesses(List<Guess> list){
		for(int row = 0; row<world.numRow; ++row){
			for(int column = 0; column<world.numColumn; ++column){
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
			// If the ship was sunk
			if (answer.shipSunk != null) {
				cell.possibleTargets.clear();
				cell.resetPossibleTargets();
			}
		} 
		// Otherwise the guess missed
		else 
			answer.isHit = false;
		
		return answer;
    }

	// Guesses a random cell from the list of guesses
    @Override
    public Guess makeGuess() {
		Random rand = new Random();
		int index = rand.nextInt(1000)%guesses.size();
        return guesses.remove(index);
    } 

	// Updates the state of the cells
	// However has zero affect on the RandomGuessPlayer algorithm
    @Override
    public void update(Guess guess, Answer answer) {
      if(answer.isHit)
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

} 