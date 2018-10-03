package ship;

/**
 * Class to store the no. of possible ship configurations
 * @authors Liam Jeynes s3544919, Viet Quang Dao s3687103
 */
 public class Configuration{
	 public int[][] configCounts;
	 public int shipLen, shipWidth, rows, columns;
	 
	 public Configuration(int rows, int columns, int shipLen, int shipWidth){
		 this.configCounts = new int[rows][columns];
		 this.shipLen = shipLen;
		 this.shipWidth = shipWidth;
		 this.rows = rows;
		 this.columns = columns;
		 initArray();
	 }
	 
	 // Initializes array to zero
	 private void initArray(){
		 for(int row=0; row<rows; ++row){
			 for(int col=0; col<columns; ++col)
				 configCounts[row][col] = 0;
		 }
	 }
	 
 }
 
 