import java.util.ArrayList;
import java.util.Random;

public class Agent {
	private static Checkers checkers;
	public static String method;
	private static Random myRandom;
	
	public Agent(Checkers checkers, String method) {
		super();
		this.checkers = checkers;
		Agent.method = method;
		myRandom = new Random();
	}
	
	public static Move move()
	{
		switch(method)
		{
			case "random":
				return random();
			case "minimax":
				return miniMaxRoot();
			default:
				return random();
		}
	}
	private static Move random()
	{
		ArrayList<Move> moves = checkers.getAvailableMoves();
		if(moves.size() > 0)
		{
			//System.out.println(moves.size());
			int i = myRandom.nextInt(moves.size());
			return moves.get(i);
		}
		return null;
	}
	private static Move miniMaxRoot()
	{
		ArrayList<Move> moves = checkers.getAvailableMoves();
		if(moves.size() > 0)
		{
			
		}
		return null;
	}
	private static miniMax()
	{
		
	}
	
}
