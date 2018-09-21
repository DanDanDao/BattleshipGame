package player;

/**
 * Direction enumeration created for Task C
 * @authors Liam Jeynes s3544919, Viet Quang Dao s3687103
 */
public enum Direction{
	NORTH, SOUTH, EAST, WEST;
	private static Direction[] directions = {NORTH,SOUTH,EAST,WEST};
	
	public Direction next(){
		return directions[(this.ordinal()+1)%directions.length];
	}
}