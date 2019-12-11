import java.util.ArrayList;

public class State {
	public ArrayList<Piece> taken; //list of all the pieces that were taken off last move
	public Piece prevPiece;// the last piece that moved
	public Position prevPos; // the original position of the last piece that moved 
	public boolean madeKing; //whether or not the last move created a king 
	public State()
	{
		taken = new ArrayList<>();
		madeKing = false;
	}
}
