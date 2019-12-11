import java.util.ArrayList;
import java.util.Stack;

public class Checkers {

	private ArrayList<Piece> pieces; 
	public static char currPlayer; 
	private Stack<State> moves;
	public Checkers()
	{
		currPlayer = 'b';
		pieces = new ArrayList<>();
		moves = new Stack<>();
		for(int i =0; i < 8; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				if(i < 3 && (i+j) % 2 == 1)
					pieces.add(new Piece(i, j, 'b'));
				else if(i > 4 && (i+j) % 2 == 1)
					pieces.add(new Piece(i, j, 'w'));
			}
		}
	}
	
	public Piece getPiece(int row, int col)
	{
		for(int i = 0; i < pieces.size(); i++)
			if(pieces.get(i).row == row && pieces.get(i).col == col)
				return pieces.get(i);
		return null;
	}
	
	public Piece getPiece(Position pos)
	{
		return getPiece(pos.row, pos.col);
	}
	
	
	private void makeMove(Position src, Position dest)
	{
		Piece myPiece = getPiece(src);
	
		myPiece.row = dest.row;
		myPiece.col = dest.col;
		//make a king if neccesary
		if(!myPiece.king)
		{
			if(myPiece.colour == 'w')
			{
				if(myPiece.row == 0)
				{
					myPiece.king = true;
					moves.peek().madeKing = true;
					
				}
			}
			else
			{
				if(myPiece.row == 7)
				{
					myPiece.king = true;
					moves.peek().madeKing = true;
				}
			}
			
		}
		
		
	
	}
	public boolean move(int r1, int c1, int r2, int c2)
	{
		return move(new Position(r1, c1), new Position(r2, c2));
	}
	
	public boolean move(Position src, Position dest)
	{
		return move(src, dest, false);
	}
	public boolean move(Move move)
	{
		if(move != null)
			return move(move.src, move.dest);
		return false;
	}
	public void undo()
	{
		
		 if(moves.size()>0)
		 {
		 	State currMove = moves.peek();
		 	for(int i = 0; i < currMove.taken.size(); i++)
				pieces.add(currMove.taken.get(i));
		 	currMove.prevPiece.row = currMove.prevPos.row;
			currMove.prevPiece.col = currMove.prevPos.col;
			if(currMove.madeKing)
				currMove.prevPiece.king = false;
			currPlayer = currPlayer == 'w' ? 'b' : 'w';
		 	moves.pop(); 
		 }
	}
	
	private State undo(State move)
	{
		
		if(move.taken.size() > 0)
		{
			Piece takenPiece = move.taken.get(move.taken.size()-1);
			pieces.add(takenPiece);
			move.taken.remove(move.taken.size()-1);
		
			
			//find row and col of the previous square
			if(takenPiece.row < move.prevPiece.row)
				move.prevPiece.row -= 2;//move piece down 2
			else if(takenPiece.row > move.prevPiece.row)
				move.prevPiece.row += 2;
			if(takenPiece.col < move.prevPiece.col)
				move.prevPiece.col -= 2;//move piece down 2
			else if(takenPiece.col > move.prevPiece.col)
				move.prevPiece.col += 2;
			if(move.madeKing)
				move.prevPiece.king = false;
		}
		
		return move;
	}
	
	private void makeMove(Position src, Position dest, State move)
	{
		Piece myPiece = getPiece(src);
	
		myPiece.row = dest.row;
		myPiece.col = dest.col;
		
		move.taken.add(getPiece((src.row + dest.row)/2,(src.col + dest.col)/2));
		pieces.remove(getPiece((src.row + dest.row)/2,(src.col + dest.col)/2));
		
		if(!myPiece.king)
		{
			if(myPiece.colour == 'w')
			{
				if(myPiece.row == 0)
				{
					myPiece.king = true;
					move.madeKing = true;
				}
			}
			else
			{
				if(myPiece.row == 7)
				{
					myPiece.king = true;
					move.madeKing = true;
				}
			}
			
		}
		
		
	}
		 
	private boolean BFS(Position src, Position dest, State move)
	{
		return BFS(src, dest, move, 0);
	}
	
	private boolean BFS(Position src, Position dest, State move, int numMoves) 
	{
		Piece myPiece = getPiece(src);
		if(src.equals(dest) && numMoves != 0)
			return true;
		//check top left
		if((myPiece.colour == 'w' || myPiece.king) && src.row > 1 && src.col > 1 && getPiece(src.row - 1, src.col - 1) != null && getPiece(src.row - 1, src.col - 1).colour != currPlayer && getPiece(src.row - 2, src.col - 2) == null)
		{
			makeMove(src, new Position(src.row - 2, src.col - 2), move);//move to top left, take the piece
			if(BFS(new Position(src.row - 2, src.col - 2), dest, move, numMoves+1))
			{
				return true;
			}
			numMoves--;
			undo(move);
		}
		//check top right
		if((myPiece.colour == 'w' || myPiece.king) && src.row > 1 && src.col < 6 && getPiece(src.row - 1, src.col + 1) != null && getPiece(src.row - 1, src.col + 1).colour != currPlayer && getPiece(src.row - 2, src.col + 2) == null)
		{
	
			makeMove(src, new Position(src.row - 2, src.col + 2), move);//move to top right, take the piece
			if(BFS(new Position(src.row - 2, src.col + 2), dest, move, numMoves+1))
			{
				return true;
				
			}
			numMoves--;
			undo(move);
		}
		//check bottom left
		if((myPiece.colour == 'b' || myPiece.king) && src.row < 6 && src.col > 1 && getPiece(src.row + 1, src.col - 1) != null && getPiece(src.row + 1, src.col - 1).colour != currPlayer && getPiece(src.row + 2, src.col - 2) == null)
		{
			
			makeMove(src, new Position(src.row + 2, src.col - 2), move);//move to bottom left, take the piece
			if(BFS(new Position(src.row + 2, src.col - 2), dest, move,numMoves+1))
			{
				return true;
			}
			numMoves--;
			undo(move);
		}
		//check bottom right
		//isn't working for some reason
		if((myPiece.colour == 'b' || myPiece.king) && src.row < 6 && src.col < 6 && getPiece(src.row + 1, src.col + 1) != null && getPiece(src.row + 1, src.col + 1).colour != currPlayer && getPiece(src.row + 2, src.col + 2) == null)
		{
			
			makeMove(src, new Position(src.row + 2, src.col + 2), move);//move to bottom right, take the piece
			if(BFS(new Position(src.row + 2, src.col + 2), dest, move, numMoves+1))
			{
				return true;
			}
			numMoves--;
			undo(move);
		}
		//no more valid moves
		return false;
	}
	
	public boolean move(Position src, Position dest, boolean jumping )
	{
		Piece myPiece = getPiece(src);
		if(myPiece.colour != currPlayer)
			return false;
		if(getPiece(dest) != null && (!getPiece(dest).equals(myPiece)))//destination has a piece on it
			return false;
		//check bounds
		if(src.row < 0 || src.col < 0 || dest.row < 0 || dest.col < 0 || src.row > 7 || src.col > 7 || dest.row > 7 || dest.col > 7)
			return false;
		if(!jumping)//destination is either 1 square away, or this is the first call to Move
		{
			
			//check if dest is one square away
			
			if(myPiece.king)
			{
				if(Math.abs(src.row - dest.row) == 1 && Math.abs(src.col - dest.col) == 1)
				{
					moves.push(new State());
					moves.peek().prevPiece = myPiece;
					moves.peek().prevPos = src;
			
					//make actual move
					makeMove(src, dest);
					currPlayer = currPlayer == 'w' ? 'b' : 'w';
					return true;
				}
			}
			switch(currPlayer)
			{
				case 'b':
					if(src.row + 1 == dest.row && Math.abs(src.col - dest.col) == 1)
					{
						moves.push(new State());
						moves.peek().prevPiece = myPiece;
						moves.peek().prevPos = src;
						
						//make actual move 
						makeMove(src, dest);
						currPlayer = 'w';
						return true;
					}
					break;
				case 'w':
					if(src.row - 1 == dest.row && Math.abs(src.col - dest.col) == 1)
					{
						moves.push(new State());
						moves.peek().prevPiece = myPiece;
						moves.peek().prevPos = src;
					
						//make actual move 
						makeMove(src, dest);
						currPlayer = 'b';
						return true;
					}
					break;
			}
			//jumping is false, but we're possibly jumping
		//	moves.add(new Move());
		//	moves.get(moves.size()-1).prevPiece = myPiece;
		//	moves.get(moves.size()-1).prevPos = src;
			
			return move(src, dest, true);
		}
		else // we're in the middle of a jump
		{
			State move = new State();
			move.prevPiece = myPiece;
			move.prevPos = src;
			boolean status = myPiece.king;
			if(BFS(src, dest, move))
			{
				State myMove = new State();
				if(status!= move.prevPiece.king)
					myMove.madeKing = true;
				myMove.prevPiece = myPiece;
				myMove.prevPos = src;
				myMove.taken = move.taken;
				moves.push(myMove);
				currPlayer = currPlayer == 'w' ? 'b' : 'w';
				return true;
			}
			return false;
			

		}
	
	}
	public ArrayList<Piece> getPieces()
	{
		return pieces;
	}
	public boolean canMove(Piece piece)//doesn't work
	{
		if(piece.king)
		{
			if(getPiece(piece.row - 1, piece.col - 1) == null || getPiece(piece.row - 1, piece.col + 1) == null)
				return true;
			if(getPiece(piece.row + 1, piece.col - 1) == null || getPiece(piece.row + 1, piece.col + 1) == null)
				return true;
			if((getPiece(piece.row - 1, piece.col - 1) != null && getPiece(piece.row - 1, piece.col - 1).colour != currPlayer && getPiece(piece.row - 2, piece.col - 2) == null) || (getPiece(piece.row - 1, piece.col + 1) != null && getPiece(piece.row - 1, piece.col + 1).colour != currPlayer && getPiece(piece.row - 2, piece.col + 2) == null))
				return true;
			if((getPiece(piece.row + 1, piece.col - 1) != null && getPiece(piece.row + 1, piece.col - 1).colour != currPlayer && getPiece(piece.row + 2, piece.col - 2) == null) || (getPiece(piece.row + 1, piece.col + 1) != null && getPiece(piece.row + 1, piece.col + 1).colour != currPlayer && getPiece(piece.row + 2, piece.col + 2) == null))
				return true;
		}
		else
		{
			//just look foward
			if(piece.colour == 'w')
			{
				//TODO: do the other checks like this one
				if((piece.row > 0) && ((piece.col > 0 && getPiece(piece.row - 1, piece.col - 1) == null) || ( piece.col < 7 && getPiece(piece.row - 1, piece.col + 1) == null)))
					return true;
				if((getPiece(piece.row - 1, piece.col - 1) != null && getPiece(piece.row - 1, piece.col - 1).colour != currPlayer && getPiece(piece.row - 2, piece.col - 2) == null) || (getPiece(piece.row - 1, piece.col + 1) != null && getPiece(piece.row - 1, piece.col + 1).colour != currPlayer && getPiece(piece.row - 2, piece.col + 2) == null))
					return true;
			}
			else
			{
				if(getPiece(piece.row + 1, piece.col - 1) == null || getPiece(piece.row + 1, piece.col + 1) == null)
					return true;
				if((getPiece(piece.row + 1, piece.col - 1) != null && getPiece(piece.row + 1, piece.col - 1).colour != currPlayer && getPiece(piece.row + 2, piece.col - 2) == null) || (getPiece(piece.row + 1, piece.col + 1) != null && getPiece(piece.row + 1, piece.col + 1).colour != currPlayer && getPiece(piece.row + 2, piece.col + 2) == null))
					return true;
			
			}
			
		}
		
		return false;
	}
	
	private ArrayList<Move> getAvailableMoves(Piece myPiece )
	{
		ArrayList<Move> possibleMoves = new ArrayList<>();

		//add all the moves that this piece can make
		//check direct moves first
		if(myPiece.king)
		{
			if(move(myPiece.row, myPiece.col, myPiece.row - 1, myPiece.col - 1))
			{
				undo();
				possibleMoves.add(new Move(new Position(myPiece.row, myPiece.col), new Position(myPiece.row - 1, myPiece.col - 1)));
				
			}
			if(move(myPiece.row, myPiece.col, myPiece.row - 1, myPiece.col + 1))
			{
				undo();
				possibleMoves.add(new Move(new Position(myPiece.row, myPiece.col), new Position(myPiece.row - 1, myPiece.col + 1)));
				
			}
			if(move(myPiece.row, myPiece.col, myPiece.row + 1, myPiece.col - 1))
			{
				undo();
				possibleMoves.add(new Move(new Position(myPiece.row, myPiece.col), new Position(myPiece.row + 1, myPiece.col - 1)));
				
			}
			if(move(myPiece.row, myPiece.col, myPiece.row + 1, myPiece.col + 1))
			{
				
				undo();
				possibleMoves.add(new Move(new Position(myPiece.row, myPiece.col), new Position(myPiece.row + 1, myPiece.col + 1)));
				
			}
		}
		else
		{
			if(myPiece.colour == 'w')
			{
				if(move(myPiece.row, myPiece.col, myPiece.row - 1, myPiece.col - 1))
				{
					
					undo();
					possibleMoves.add(new Move(new Position(myPiece.row, myPiece.col), new Position(myPiece.row - 1, myPiece.col - 1)));
					
				}
				if(move(myPiece.row, myPiece.col, myPiece.row - 1, myPiece.col + 1))
				{
					undo();
					possibleMoves.add(new Move(new Position(myPiece.row, myPiece.col), new Position(myPiece.row - 1, myPiece.col + 1)));
					
				}
			}
			else
			{
				if(move(myPiece.row, myPiece.col, myPiece.row + 1, myPiece.col - 1))
				{
					undo();
					possibleMoves.add(new Move(new Position(myPiece.row, myPiece.col), new Position(myPiece.row + 1, myPiece.col - 1)));
					
				}
				if(move(myPiece.row, myPiece.col, myPiece.row + 1, myPiece.col + 1))
				{
					undo();
					possibleMoves.add(new Move(new Position(myPiece.row, myPiece.col), new Position(myPiece.row + 1, myPiece.col + 1)));
					
				}
			}
		}
		
		//TODO: optomise, so that only the 8 (or less) possible squares are checked
		//if(myPiece.king)
		//{
		
		for(int i = 0; i < 8; i++)
		{
			int j = -1;
			if((myPiece.row % 4 == 1 && (myPiece.col == 6 || myPiece.col == 2)) || (myPiece.row % 4 == 3 && (myPiece.col == 4 || myPiece.col == 0)))
			{
				if(i % 4 == 1)
					j = 2;
				else
					j = 0;
			}
			else if((myPiece.row % 4 == 1 && (myPiece.col == 0 || myPiece.col == 4)) || (myPiece.row % 4 == 3 && (myPiece.col == 2 || myPiece.col == 6)))
			{
				if(i % 4 == 1)
					j = 0;
				else
					j = 2;
			}
			else if((myPiece.row % 4 == 0 && (myPiece.col == 3 || myPiece.col == 7)) || (myPiece.row % 4 == 2 && (myPiece.col == 1 || myPiece.col == 5)))
			{
				if(i % 4 == 0)
					j = 3;
				else
					j = 1;
			}
			else if((myPiece.row % 4 == 0 && (myPiece.col == 1 || myPiece.col == 5)) || (myPiece.row % 4 == 2 && (myPiece.col == 3 || myPiece.col == 7)))
			{
				if(i % 4 == 0)
					j = 1;
				else
					j = 3;
			}
			while(j < 8 && j != -1)
			{
				if(move(myPiece.row, myPiece.col, i, j))
				{
					undo();
					possibleMoves.add(new Move(new Position(myPiece.row, myPiece.col), new Position(i, j)));
					
				}
				j += 4;
			}

		}
	
			
			/*
			 * case 3:
			 *  7 0 //%4 = 3
			 *	7 4 //%4 = 3
			 *	5 2 //%4 = 1
			 *	5 6 //%4 = 1
			 *  3 0 //%4 = 3
			 *	3 4 //%4 = 3
			 *	1 2 //%4 = 1
			 *	1 6 //%4 = 1
			*/
				
			
			
			
		//}
		
		
		/*
		//now do jumps
		//do odd row first
		if(myPiece.row % 2 == 1)
		{
			if(myPiece.king)
			{
				//check both directions
				for(int j = 1; j < 8; j+=2)
				{
					for(int k = 0; k < 8; k+=2)
					{
						if(move(myPiece.row, myPiece.col, j, k))
						{
							undo();
							possibleMoves.add(new Move(new Position(myPiece.row, myPiece.col), new Position(j, k)));
	 						
						}
					}
				}
			}
			else
			{
				//just check forward
				if(myPiece.colour == 'w')
				{
					for(int j = 1; j < myPiece.row; j+=2)
					{
						for(int k = 0; k < 8; k+=2)
						{
							if(move(myPiece.row, myPiece.col, j, k))
							{
								undo();
								possibleMoves.add(new Move(new Position(myPiece.row, myPiece.col), new Position(j, k)));
		 						
							}
						}
					}
				}
				else
				{
					for(int j = myPiece.row + 2; j < 8; j+=2)
					{
						for(int k = 0; k < 8; k+=2)
						{
							if(move(myPiece.row, myPiece.col, j, k))
							{
								undo();
								possibleMoves.add(new Move(new Position(myPiece.row, myPiece.col), new Position(j, k)));
		 						
							}
						}
					}
				}
			}
		}
		else
		{
			//do even rows
			for(int j = 0; j < 8; j+=2)
			{
				for(int k = 1; k < 8; k+=2)
				{
					if(move(myPiece.row, myPiece.col, j, k))
					{
						undo();
						possibleMoves.add(new Move(new Position(myPiece.row, myPiece.col), new Position(j, k)));
 						
					}
				}
			}
		}
		*/
		return possibleMoves;
	}
	
	public ArrayList<Move> getAvailableMoves()//TODO: Veeeeeeeeeeery slow, really need to optimise
	{
		ArrayList<Move> possibleMoves = new ArrayList<>();
		for(int i = 0; i < pieces.size(); i++)
		{
			ArrayList<Move> movesForPiece = new ArrayList<>();
			Piece myPiece = pieces.get(i);
			if(myPiece.colour == currPlayer)
			{
				if(canMove(myPiece))//TODO: Doesn't work, need to fix
				{
					movesForPiece = getAvailableMoves(myPiece);
				}
			}
			for(int j = 0; j < movesForPiece.size(); j++)
			{
				possibleMoves.add(movesForPiece.get(j));//add the pieces found for this piece to the main array 
			}
		}
		return possibleMoves;
	}
	
	public static char getCurrPlayer() {
		return currPlayer;
	}

	public Stack<State> getMoves() {
		return moves;
	}

	public void print()
	{
		char[][] board = new char[8][8];
		for(int i = 0; i < 8; i++)
			for(int j = 0; j< 8; j++)
				board[i][j] = (i+j) % 2 == 0 ? ' ' : '#';
		for(int i = 0; i < pieces.size(); i++)
			board[pieces.get(i).row][pieces.get(i).col] = pieces.get(i).king ? Character.toUpperCase(pieces.get(i).colour) : pieces.get(i).colour;
		for(int i = 0; i < 8; i++)
			System.out.print(" -");
		System.out.println();
		for(int i = 0; i < 8; i++)
		{
			
			System.out.print("|");
			for(int j = 0; j < 8; j++)
			{
				System.out.print(board[i][j] + "|");			
			}
			System.out.println();
			for(int k = 0; k < 8; k++)
				System.out.print(" -");
			System.out.println();
		}
		
	}

}
