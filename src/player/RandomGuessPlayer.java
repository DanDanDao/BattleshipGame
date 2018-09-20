package player;

import java.util.Scanner;
import world.World;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Random guess player (task A).
 * @authors Liam Jeynes s3544919, Viet Quang Dao s3687103
 */
public class RandomGuessPlayer extends Guesser implements Player{
	public List<Guess> guesses;
	private static final int NUM_OF_FLEET_COORDINATES = 19;
   
   @Override
    public void initialisePlayer(World world) {
        this.world = world;
		this.guesses = new ArrayList<>();
		this.hits = new ArrayList<>();
		initGuesses(guesses);
    } 

    @Override
    public Answer getAnswer(Guess guess) {
		Answer answer = new Answer();
		if (super.resultOfGuess(guess, answer))
			answer.isHit = true;
		else
			answer.isHit = false;
		return answer;
    }


    @Override
    public Guess makeGuess() {
		Random rand = new Random();
		int index = rand.nextInt(1000)%guesses.size();
        return guesses.remove(index);
    } 

	// Empty as Random Guess player does not require any updating
	// as the result of a hit does not affect the algorithm
    @Override
    public void update(Guess guess, Answer answer) {
      
    } 

    @Override
    public boolean noRemainingShips() {
       return hits.size() >= NUM_OF_FLEET_COORDINATES;
    } 
	
	// Initialises guesses used in algorithm
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

} 