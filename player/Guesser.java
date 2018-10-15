package player;

import java.util.List;
import world.World;
import world.World.Coordinate;
import world.World.ShipLocation;

/**
 * Abstract Guesser class extended by guessing classes
 * @authors Liam Jeynes s3544919, Viet Quang Dao s3687103
 */
public abstract class Guesser implements Player{
	protected World world;
	protected List<Guess> hits;
	protected Cell cell;
	
	// Method used by getAnswer method to determine the result of the guess
	protected boolean resultOfGuess(Guess guess, Answer answer){
		// Loop through ships
		for(ShipLocation ship: world.shipLocations){
			// Loop through coordinates of each ship
			for (Coordinate co: ship.coordinates){
				// If the guess hits a ship
				if(sameAs(co, guess)){
					hits.add(guess);
					// If ship has been sunk i.e. all coordinates been hit
					if(isSunk(ship))
						answer.shipSunk = ship.ship;
					return true;
				}
			}
		}
		return false;
	}

	// Method to check if the coordinates are the same i.e. the guess hits a ship
	protected boolean sameAs(Coordinate co, Guess guess){
        return co.row == guess.row && co.column == guess.column;
    }

	// Method to check if the ship hit has been sunk
	private boolean isSunk(ShipLocation ship){
		// Loop through coordinates of the ship
		for(Coordinate co: ship.coordinates){
			Guess currGuess = createGuess(co);
			// Loops through hits to players fleet
			for(Guess guess: hits){
				// Returns false if any part of the ship hasn't been hit
				if(currGuess.row != guess.row || currGuess.column != guess.column)
					return false;
			}
		}
		// Otherwise return true as the ship must have been sunk
		return true;
	}
	
    protected Guess createGuess(Coordinate co){
        Guess guess = new Guess();
        guess.row = co.row;
        guess.column = co.column;
        return guess;
    }
	
}
