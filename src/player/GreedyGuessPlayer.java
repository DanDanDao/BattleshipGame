package player;

import world.World;
import world.World.Coordinate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import player.Cell.CellState;
import player.Cell;

/**
 * Greedy guess player (task B).
 * @authors Liam Jeynes s3544919, Viet Quang Dao s3687103
 */
public class GreedyGuessPlayer extends Guesser implements Player{
	private static final int NUM_OF_FLEET_COORDINATES = 19;
	public List<Guess> guesses;
	
    @Override
    public void initialisePlayer(World world) {
        this.world = world;
		this.cell = new Cell(world.numRow, world.numColumn);
		this.hits = new ArrayList<>();
		this.guesses = new ArrayList<>();
		initGuesses(guesses);
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
		if(cell.possibleTargets.size()>0)
			return targetMode();
		else
			return huntMode();
    } 

    @Override
    public void update(Guess guess, Answer answer) {
		if(answer.isHit)
			cell.updateCell(CellState.hit, guess.row, guess.column);
		else
			cell.updateCell(CellState.miss, guess.row, guess.column);
    } 


    @Override
    public boolean noRemainingShips() {
        return hits.size() >= NUM_OF_FLEET_COORDINATES;
    } 

	// Initialises guesses used in algorithm
	private void initGuesses(List<Guess> list){
		for(int row = 0; row<world.numRow; ++row){
			for(int column = row%2; column<world.numColumn; column+=2){
				Guess guess = new Guess();
				guess.row = row;
				guess.column = column;
				list.add(guess);
			}
		}
	}
	
	public Guess targetMode(){
		Guess guess = new Guess();
		Guess tempGuess = new Guess();
		Coordinate co = world.new Coordinate();
		
		co = cell.possibleTargets.remove(0);
		guess.row = co.row;
		guess.column = co.column;
		
		for(int i=0; i<guesses.size(); i++){
			tempGuess = guesses.get(i);
			if(tempGuess.row == guess.row && tempGuess.column == guess.column){
				guesses.remove(i);
				break;
			}
		}
		return guess;
	}
	
	public Guess huntMode(){
		Random rand = new Random();
		int index = rand.nextInt(1000)%guesses.size();
		return guesses.remove(index);
	}
} 
