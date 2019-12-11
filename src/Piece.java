
public class Piece {
	int row, col;
	char colour;
	boolean king;
	/**
	 * @param row
	 * @param col
	 * @param colour
	 */
	public Piece(int row, int col, char colour) {
		super();
		this.row = row;
		this.col = col;
		this.colour = colour;
		this.king = false;
	}
	@Override
	public String toString() {
		if(king)
			return Character.toString(Character.toUpperCase(colour));
		return Character.toString(colour);  
		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	} 
	
	
	

}
