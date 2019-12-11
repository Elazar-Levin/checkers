import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		Checkers board = new Checkers();
		@SuppressWarnings("unused")
		Board myBoard = new Board(board);
		//ArrayList<Move> moves = board.getAvailableMoves();
		Agent myAgent = new Agent(board, "random");
		
		while(true)
		{
			//System.out.println(Checkers.getCurrPlayer());
			System.out.println("print statement");
			
			if(Checkers.getCurrPlayer() == 'b')//only reached once for some reason, unless the print statement above is uncommented
			{
				//System.out.println("in");
				board.move(Agent.move());
				myBoard.refresh();
	  		}
		}
	
		
		
	}

}
